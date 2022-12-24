package com.allen.demoserver.service;

import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

/**
 * @Author xuguocai
 * @Date 21:15 2022/12/20
 **/

public interface RedisToJdbcService {

	/**
	 * 授权
	 * @param authorization
	 */
	void save(OAuth2Authorization authorization);

	void remove(OAuth2Authorization authorization);

	@Nullable
	OAuth2Authorization findById(String id);

	@Nullable
	OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType);

	/**
	 * ---------------------------------------权限
	 * @param authorizationConsent
	 */
	void save(OAuth2AuthorizationConsent authorizationConsent);

	void remove(OAuth2AuthorizationConsent authorizationConsent);

	@Nullable
	OAuth2AuthorizationConsent findById(String registeredClientId, String principalName);
}
