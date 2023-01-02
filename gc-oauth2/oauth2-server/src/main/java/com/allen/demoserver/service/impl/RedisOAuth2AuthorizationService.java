package com.allen.demoserver.service.impl;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.allen.component.redis.repository.RedisRepository;
import com.allen.demoserver.entity.CustomUserDetails;
import com.allen.demoserver.entity.RedisOAuth2Authorization;
//import com.allen.demoserver.entity.SysUserDetails;
import com.allen.demoserver.entity.SysUser;
import com.allen.demoserver.service.RedisConsts;
import com.allen.demoserver.service.RedisToJdbcService;
import com.allen.demoserver.service.SecurityConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.*;

/**
 * 使用redis进行缓存  OAuth2Authorization
 */
@Slf4j
public final class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {
	/**
	 * 过期时间，默认一小时
	 */
	private static long DEFAULT_EXPIRE= RedisConsts.DEFAULT_EXPIRE ;
    private static final String AUTHORIZATION = SecurityConstant.AUTHORIZATION;
    public static final String UNDERSCORE = "_";
    private final static String AUTHORIZATION_UNDERSCORE = AUTHORIZATION + UNDERSCORE;
	private final static String STATEKEY = AUTHORIZATION_UNDERSCORE + OAuth2ParameterNames.STATE + UNDERSCORE;
	private final static String AUTHCODETOKENKEY = AUTHORIZATION_UNDERSCORE + OAuth2ParameterNames.CODE + UNDERSCORE;
	private final static String REFRESHTOKENKEY = AUTHORIZATION_UNDERSCORE + OAuth2TokenType.REFRESH_TOKEN.getValue() + UNDERSCORE;
	private final static String ACCESSTOKENKEY = AUTHORIZATION_UNDERSCORE + OAuth2TokenType.ACCESS_TOKEN.getValue() + UNDERSCORE;
	private final RegisteredClientRepository registeredClientRepository;

	private final RedisToJdbcService redisToJdbcService;
	private final RedisRepository redisRepository;

    public RedisOAuth2AuthorizationService(RegisteredClientRepository registeredClientRepository,RedisRepository redisRepository,RedisToJdbcService redisToJdbcService) {
		this.registeredClientRepository = registeredClientRepository;
		this.redisRepository = redisRepository;
		this.redisToJdbcService = redisToJdbcService;
    }

	private RegisteredClient getRegisteredClient(String clientId){
		return registeredClientRepository.findById(clientId);
	}
    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
		// 先保存到数据库
		redisToJdbcService.save(authorization);

        String authId = AUTHORIZATION_UNDERSCORE + authorization.getId();
		RedisOAuth2Authorization auth2Authorization = setRedisAuthorization(authorization);
		String json = JSON.toJSONString(auth2Authorization);
		log.info("实体json参数:{}",json);
		// 60*60 一小时时间
		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
		if (accessToken != null){
			long expiresAt = authorization.getAccessToken().getToken().getExpiresAt().getEpochSecond();
			long issuedAt = accessToken.getToken().getIssuedAt().getEpochSecond();
			log.info("授权配置的过期时间：{}",expiresAt-issuedAt);
			DEFAULT_EXPIRE = expiresAt-issuedAt;
		}
		redisRepository.setExpire(authId,json,DEFAULT_EXPIRE);
        //将授权信息以token为key存入缓存
        saveTokenToCache(authorization);
    }

	private RedisOAuth2Authorization setRedisAuthorization(OAuth2Authorization authorization){
		RedisOAuth2Authorization auth2Authorization = new RedisOAuth2Authorization();
		auth2Authorization.setId(authorization.getId());
		auth2Authorization.setRegisteredClientId(authorization.getRegisteredClientId());
		auth2Authorization.setPrincipalName(authorization.getPrincipalName());
		auth2Authorization.setAuthorizedScopes(authorization.getAuthorizedScopes());
		auth2Authorization.setAttributes(authorization.getAttributes());
		auth2Authorization.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());

		// code
		Map<String, Object> authorizationCodes = new HashMap<>();
		OAuth2Authorization.Token<?> oAuth2AuthorizationCode = getTokenObjByAuth(authorization, OAuth2AuthorizationCode.class);
		if (oAuth2AuthorizationCode != null){
			OAuth2Token token = oAuth2AuthorizationCode.getToken();
			authorizationCodes.put("authorization_code_value",token.getTokenValue());
			authorizationCodes.put("authorization_code_issued_at",token.getIssuedAt());
			authorizationCodes.put("authorization_code_expires_at",token.getExpiresAt());
			authorizationCodes.put("authorization_code_metadata",oAuth2AuthorizationCode.getMetadata());
		}
		auth2Authorization.setAuthorizationCodes(authorizationCodes);

		Map<String, Object> accessTokens = new HashMap<>();
		OAuth2Authorization.Token<?> accessToken = getTokenObjByAuth(authorization, OAuth2AccessToken.class);
		if (accessToken != null){
			OAuth2Token token = accessToken.getToken();
			accessTokens.put("access_token_value",token.getTokenValue());
			accessTokens.put("access_token_issued_at",token.getIssuedAt());
			accessTokens.put("access_token_expires_at",token.getExpiresAt());
			accessTokens.put("access_token_metadata",accessToken.getMetadata());
		}
		auth2Authorization.setAccessTokens(accessTokens);

		Map<String, Object> refreshTokens = new HashMap<>();
		OAuth2Authorization.Token<?> refreshToken = getTokenObjByAuth(authorization, OAuth2RefreshToken.class);
		if (refreshToken != null){
			OAuth2Token token = refreshToken.getToken();
			refreshTokens.put("refresh_token_value",token.getTokenValue());
			refreshTokens.put("refresh_token_issued_at",token.getIssuedAt());
			refreshTokens.put("refresh_token_expires_at",token.getExpiresAt());
			refreshTokens.put("refresh_token_metadata",refreshToken.getMetadata());
		}
		auth2Authorization.setRefreshTokens(refreshTokens);

		Map<String, Object> oidcIdTokens = new HashMap<>();
		OAuth2Authorization.Token<?> oidcIdToken = getTokenObjByAuth(authorization, OidcIdToken.class);
		if (oidcIdToken != null){
			OAuth2Token token = oidcIdToken.getToken();
			oidcIdTokens.put("oidc_id_token_value",token.getTokenValue());
			oidcIdTokens.put("oidc_id_token_issued_at",token.getIssuedAt());
			oidcIdTokens.put("oidc_id_token_expires_at",token.getExpiresAt());
			oidcIdTokens.put("oidc_id_token_metadata",oidcIdToken.getMetadata());
		}
		auth2Authorization.setOidcIdTokens(oidcIdTokens);

		String state = getTokenByAuth(authorization, OAuth2ParameterNames.STATE);
		auth2Authorization.setState(state);

		return auth2Authorization;
	}

	private OAuth2Authorization setOAuth2Authorization(String context){
		RedisOAuth2Authorization authorization = JSONObject.parseObject(context, RedisOAuth2Authorization.class);

		String authorizationGrantType = authorization.getAuthorizationGrantType();

		if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)||
			AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)){
			return getTokenByCode(authorization);
		}else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)){
			return getClientCredentials(authorization);
		}
		return null;
	}

	/**
	 * code的处理方式
	 * @param authorization
	 * @return
	 */
	private OAuth2Authorization getTokenByCode(RedisOAuth2Authorization authorization){
		RegisteredClient registeredClient = getRegisteredClient(authorization.getRegisteredClientId());
		// accessTokens
		Map<String, Object> accessTokens = authorization.getAccessTokens();
		if (CollectionUtils.isEmpty(accessTokens)){
			OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient).id(authorization.getId())
				.authorizedScopes(authorization.getAuthorizedScopes())
				.principalName(authorization.getPrincipalName()).authorizationGrantType(new AuthorizationGrantType(authorization.getAuthorizationGrantType()));

			Map<String, Object> attributes = authorization.getAttributes();
			if (!CollectionUtils.isEmpty(attributes)){
				for (Map.Entry<String, Object> entry:attributes.entrySet()){
					builder.attribute(entry.getKey(),entry.getValue());
				}
			}

			return builder.build();
		}
		OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,(String) accessTokens.get("access_token_value"),
			(Instant) accessTokens.get("access_token_issued_at"), (Instant) accessTokens.get("access_token_expires_at"));

		// refreshTokens
		Map<String, Object> refreshTokens = authorization.getRefreshTokens();
		if (CollectionUtils.isEmpty(refreshTokens)){
			OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient).id(authorization.getId())
				.authorizedScopes(authorization.getAuthorizedScopes())
				.principalName(authorization.getPrincipalName()).authorizationGrantType(new AuthorizationGrantType(authorization.getAuthorizationGrantType()))
				.accessToken(accessToken);

			Map<String, Object> attributes = authorization.getAttributes();
			if (!CollectionUtils.isEmpty(attributes)){
				for (Map.Entry<String, Object> entry:attributes.entrySet()){
					builder.attribute(entry.getKey(),entry.getValue());
				}
			}

			return builder.build();
		}
		OAuth2RefreshToken refreshToken = new OAuth2RefreshToken((String) refreshTokens.get("refresh_token_value"),
			(Instant) refreshTokens.get("refresh_token_issued_at"), (Instant) refreshTokens.get("refresh_token_expires_at"));

		OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient).id(authorization.getId())
			.authorizedScopes(authorization.getAuthorizedScopes())
			.principalName(authorization.getPrincipalName()).authorizationGrantType(new AuthorizationGrantType(authorization.getAuthorizationGrantType()))
			.accessToken(accessToken).refreshToken(refreshToken);

		Map<String, Object> attributes = authorization.getAttributes();
		if (!CollectionUtils.isEmpty(attributes)){
			for (Map.Entry<String, Object> entry:attributes.entrySet()){
				builder.attribute(entry.getKey(),entry.getValue());
			}
		}

		return builder.build();
	}

	/**
	 * 客户端登录方式
	 * @param authorization
	 * @return
	 */
	private OAuth2Authorization getClientCredentials(RedisOAuth2Authorization authorization){
		RegisteredClient registeredClient = getRegisteredClient(authorization.getRegisteredClientId());
		// accessTokens
		Map<String, Object> accessTokens = authorization.getAccessTokens();
		OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,(String) accessTokens.get("access_token_value"),
			(Instant) accessTokens.get("access_token_issued_at"), (Instant) accessTokens.get("access_token_expires_at"));

		OAuth2Authorization oAuth2Authorization = OAuth2Authorization.withRegisteredClient(registeredClient).id(authorization.getId())
			.authorizedScopes(authorization.getAuthorizedScopes())
			.principalName(authorization.getPrincipalName()).authorizationGrantType(new AuthorizationGrantType(authorization.getAuthorizationGrantType()))
			.accessToken(accessToken)
			.build();
		oAuth2Authorization.getAttributes().putAll(authorization.getAttributes());
		return oAuth2Authorization;
	}

    private void saveTokenToCache( OAuth2Authorization authorization) {

        String accessToken = getTokenByAuth(authorization, OAuth2AccessToken.class);
        //判断该授权信息中4个类型的token是否不为空，如果有值，则需要把对应token类型+MD5(token)为key,授权id为value的set格式放入缓存(摘要算法可能重复)，过期时间为一天
        if (StringUtils.isNotBlank(accessToken)) {
            String accessTokenKey = ACCESSTOKENKEY + accessToken;
			// 获取用户信息，此处调用系统管理的用户。若有其他的可以调整 todo
			SysUser userVO = null;//userClient.selectUserByUserName(authorization.getPrincipalName());
			if (userVO == null){
				userVO = new SysUser();
				userVO.setUsername(authorization.getPrincipalName());
			}
			long expiresAt = authorization.getAccessToken().getToken().getExpiresAt().getEpochSecond();
			long issuedAt = authorization.getAccessToken().getToken().getIssuedAt().getEpochSecond();
			redisRepository.setExpire(accessTokenKey, JSON.toJSONString(userVO), expiresAt-issuedAt);
		}
        String refreshToken = getTokenByAuth(authorization, OAuth2RefreshToken.class);
        if (StringUtils.isNotBlank(refreshToken)) {
            String refreshTokenKey =REFRESHTOKENKEY+ refreshToken;
			long expiresAt = authorization.getRefreshToken().getToken().getExpiresAt().getEpochSecond();
			long issuedAt = authorization.getRefreshToken().getToken().getIssuedAt().getEpochSecond();

			redisRepository.setExpire(refreshTokenKey,refreshToken, expiresAt-issuedAt);
		}
        String authCodeToken = getTokenByAuth(authorization, OAuth2AuthorizationCode.class);
        if (StringUtils.isNotBlank(authCodeToken)) {
            String authCodeTokenKey = AUTHCODETOKENKEY + authCodeToken;
			long expiresAt = authorization.getToken(OAuth2AuthorizationCode.class).getToken().getExpiresAt().getEpochSecond();
			long issuedAt = authorization.getToken(OAuth2AuthorizationCode.class).getToken().getIssuedAt().getEpochSecond();

			redisRepository.setExpire(authCodeTokenKey,authCodeToken,expiresAt-issuedAt);
		}
        String state = getTokenByAuth(authorization, OAuth2ParameterNames.STATE);
        if (StringUtils.isNotBlank(state)) {
            String stateKey = STATEKEY + state;
            redisRepository.setExpire(stateKey,state, RedisConsts.DEFAULT_EXPIRE);
		}
    }


    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
		// 先操作数据库
		redisToJdbcService.remove(authorization);
		//删除实体
		String authId = AUTHORIZATION_UNDERSCORE + authorization.getId();
		redisRepository.delKey(authId);
        //删除以token为key的数据
        removeTokenToCache(authorization);
    }

    private void removeTokenToCache(OAuth2Authorization authorization) {

        String accessToken = getTokenByAuth(authorization, OAuth2AccessToken.class);
        //判断该授权信息中4个类型的token是否不为空，如果有值，则需要把对应token类型+MD5(token)为key,授权id为value的set结构中将对应value值删除
        if (StringUtils.isNotBlank(accessToken)) {
			String md5HexKey = ACCESSTOKENKEY + DigestUtils.md5Hex(accessToken);
			redisRepository.delKey(md5HexKey);
			String accessTokenKey = ACCESSTOKENKEY + accessToken;
			redisRepository.delKey(accessTokenKey);
        }
        String refreshToken = getTokenByAuth(authorization, OAuth2RefreshToken.class);
        if (StringUtils.isNotBlank(refreshToken)) {
			String refreshTokenKey =REFRESHTOKENKEY+ refreshToken;
			redisRepository.delKey(refreshTokenKey);
			String md5HexKey = REFRESHTOKENKEY + DigestUtils.md5Hex(accessToken);
			redisRepository.delKey(md5HexKey);
		}
        String authCodeToken = getTokenByAuth(authorization, OAuth2AuthorizationCode.class);
        if (StringUtils.isNotBlank(authCodeToken)) {
			String authCodeTokenKey = AUTHCODETOKENKEY + authCodeToken;
			redisRepository.delKey(authCodeTokenKey);
			String md5HexKey = AUTHCODETOKENKEY+ DigestUtils.md5Hex(authCodeToken);
			redisRepository.delKey(md5HexKey);
		}
        String attribute = getTokenByAuth(authorization, OAuth2ParameterNames.STATE);
        if (StringUtils.isNotBlank(attribute)) {
			String stateKey = STATEKEY + attribute;
			redisRepository.delKey(stateKey);
			String md5HexKey = STATEKEY + DigestUtils.md5Hex(attribute);
			redisRepository.delKey(md5HexKey);
		}
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
		// 操作数据库
		return redisToJdbcService.findById(id);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
		// 操作数据库
		OAuth2Authorization authByCacheToken = null;
		authByCacheToken = redisToJdbcService.findByToken(token, tokenType);
		// 刷新操作，则需删除原来的缓存token信息
		if (OAuth2TokenType.REFRESH_TOKEN.getValue().equals(tokenType.getValue())){
			removeTokenToCache(authByCacheToken);
		}
        return authByCacheToken;
    }

    private static boolean hasToken(OAuth2Authorization authorization, String token, @Nullable String tokenType) {
        if (tokenType == null) {
            return matchesState(authorization, token) ||
                    matchesAuthorizationCode(authorization, token) ||
                    matchesAccessToken(authorization, token) ||
                    matchesRefreshToken(authorization, token);
        } else if (OAuth2ParameterNames.STATE.equals(tokenType)) {
            return matchesState(authorization, token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType)) {
            return matchesAuthorizationCode(authorization, token);
        } else if (OAuth2TokenType.ACCESS_TOKEN.getValue().equals(tokenType)) {
            return matchesAccessToken(authorization, token);
        } else if (OAuth2TokenType.REFRESH_TOKEN.getValue().equals(tokenType)) {
            return matchesRefreshToken(authorization, token);
        }
        return false;
    }

    private static boolean matchesState(OAuth2Authorization authorization, String token) {
        return token.equals(authorization.getAttribute(OAuth2ParameterNames.STATE));
    }

    private static boolean matchesAuthorizationCode(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                authorization.getToken(OAuth2AuthorizationCode.class);
        return authorizationCode != null && authorizationCode.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesAccessToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
                authorization.getToken(OAuth2AccessToken.class);
        return accessToken != null && accessToken.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesRefreshToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                authorization.getToken(OAuth2RefreshToken.class);
        return refreshToken != null && refreshToken.getToken().getTokenValue().equals(token);
    }

    private String getTokenByAuth(OAuth2Authorization authorization, String tokenType) {
        return authorization.getAttribute(tokenType);
    }

	private <T extends OAuth2Token> OAuth2Authorization.Token<?> getTokenObjByAuth(OAuth2Authorization authorization, Class<? extends OAuth2Token> tokenType) {
		OAuth2Authorization.Token<?> accessToken = authorization.getToken(tokenType);
		if (Objects.isNull(accessToken)) {
			return null;
		}
		return accessToken;
	}

    private String getTokenByAuth(OAuth2Authorization authorization, Class<? extends OAuth2Token> tokenType) {
        OAuth2Authorization.Token<?> accessToken = authorization.getToken(tokenType);
        if (Objects.isNull(accessToken)) {
            return null;
        }
        return accessToken.getToken().getTokenValue();
    }

    private OAuth2Authorization getAuthByCacheToken(String token, Object tokenType) {
        String type = null;
        if (tokenType instanceof OAuth2TokenType) {
            type = ((OAuth2TokenType) tokenType).getValue();
        } else {
            type = (String) tokenType;
        }
        //从set集合中取出值,因为key为摘要算法生产，可能存在相同key，所以值可能有多个
		String cacheKey = AUTHORIZATION_UNDERSCORE + type + UNDERSCORE + DigestUtils.md5Hex(token);
        Set authIdSet = redisRepository.setMembers(cacheKey);
        if (CollectionUtils.isEmpty(authIdSet)) {
            return null;
        }
        List<Object> authList = new ArrayList<>(authIdSet);
        //当值集合只有1个时，直接取对应的权限信息
        if (authList.size() == 1) {
			String authIdKey = AUTHORIZATION_UNDERSCORE + authList.get(0);
			String context = redisRepository.get(authIdKey);
			return setOAuth2Authorization(context);
        } else {
            //如果有多个，则需要循环key下的所有值，判断token是否与取得对权限中对应类型token相同，相同则返回
            for (Object authId : authList) {
				String authIdKey = AUTHORIZATION_UNDERSCORE + authId;
				String context = redisRepository.get(authIdKey);
				OAuth2Authorization authorization = setOAuth2Authorization(context);
                if (hasToken(authorization, token, type)) {
                    return authorization;
                }
            }
        }
        return null;
    }
}
