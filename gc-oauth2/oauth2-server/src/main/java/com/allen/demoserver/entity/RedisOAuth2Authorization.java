package com.allen.demoserver.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @Author xuguocai
 * @Date 13:26 2022/12/20
 **/
@Setter
@Getter
@ToString
public class RedisOAuth2Authorization implements Serializable {
	private static final long serialVersionUID = 10L;
	private String id;
	private String registeredClientId;
	private String principalName;
	private String authorizationGrantType;
	private Set<String> authorizedScopes;

	private String state;
//	private Map<Class<? extends OAuth2Token>, OAuth2Authorization.Token<?>> tokens;
	private Map<String, Object> attributes;

	private Map<String, Object> authorizationCodes;

	private Map<String, Object> accessTokens;

	private Map<String, Object> oidcIdTokens;

	private Map<String, Object> refreshTokens;

}
