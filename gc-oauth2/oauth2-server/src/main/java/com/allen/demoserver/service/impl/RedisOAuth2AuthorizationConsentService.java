package com.allen.demoserver.service.impl;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.allen.component.redis.repository.RedisRepository;
import com.allen.demoserver.entity.RedisOAuth2AuthorizationConsent;
import com.allen.demoserver.service.RedisOAuth2Constant;
import com.allen.demoserver.service.RedisToJdbcService;
import com.allen.demoserver.service.SecurityConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 使用redis缓存  OAuth2AuthorizationConsent
 */
@Slf4j
public final class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private static final String AUTHORIZATION = SecurityConstant.AUTHORIZATION_CONSENT;

    private final RedisRepository redisRepository;
	private final RedisToJdbcService redisToJdbcService;

    public RedisOAuth2AuthorizationConsentService(RedisRepository redisRepository,RedisToJdbcService redisToJdbcService) {
        this.redisRepository = redisRepository;
		this.redisToJdbcService = redisToJdbcService;
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		// 先操作数据库
		redisToJdbcService.save(authorizationConsent);

		RedisOAuth2AuthorizationConsent redisOAuth2AuthorizationConsent = setConsent(authorizationConsent);
		String key = AUTHORIZATION+ RedisOAuth2Constant.LINE+getId(authorizationConsent);

		redisRepository.set(key, JSON.toJSONString(redisOAuth2AuthorizationConsent));
    }

	private RedisOAuth2AuthorizationConsent setConsent(OAuth2AuthorizationConsent authorizationConsent){
		RedisOAuth2AuthorizationConsent consent = new RedisOAuth2AuthorizationConsent();
		consent.setRegisteredClientId(authorizationConsent.getRegisteredClientId());
		consent.setPrincipalName(authorizationConsent.getPrincipalName());
		Set<GrantedAuthority> authorities = authorizationConsent.getAuthorities();
		if (!CollectionUtils.isEmpty(authorities)){
			Set<String> redisAuthorities = new HashSet<>(authorities.size());
			for (GrantedAuthority item : authorities){
				redisAuthorities.add(item.getAuthority());
			}
			consent.setAuthorities(redisAuthorities);
		}

		return consent;
	}

	private OAuth2AuthorizationConsent setOAuth2AuthorizationConsent(String context){
		RedisOAuth2AuthorizationConsent consent = JSONObject.parseObject(context, RedisOAuth2AuthorizationConsent.class);

		Set<String> authorities = consent.getAuthorities();
		OAuth2AuthorizationConsent authorization = OAuth2AuthorizationConsent.withId(consent.getRegisteredClientId(), consent.getPrincipalName()).build();
		if (!CollectionUtils.isEmpty(authorities)){
			for (String grantedAuthority :authorities){
				SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(grantedAuthority);
				authorization.getAuthorities().add(simpleGrantedAuthority);
			}
		}

		return authorization;
	}

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		redisToJdbcService.remove(authorizationConsent);
		String key = AUTHORIZATION+RedisOAuth2Constant.LINE+getId(authorizationConsent);
		redisRepository.del(key);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
//		String key = AUTHORIZATION+RedisOAuth2Constant.LINE+getId(registeredClientId, principalName);
//		String context = redisRepository.get(key);
//		if (StringUtils.isBlank(context)){
//			return null;
//		}
//		OAuth2AuthorizationConsent authorizationConsent = setOAuth2AuthorizationConsent(context);
//
		return redisToJdbcService.findById(registeredClientId, principalName);
    }

    private static String getId(OAuth2AuthorizationConsent authorizationConsent) {
        return getId(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
    }

    private static String getId(String registeredClientId, String principalName) {
        return registeredClientId + principalName;
    }

}
