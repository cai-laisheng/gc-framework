package com.allen.demoserver.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * @Author xuguocai
 * @Date 11:00 2022/12/20
 **/
@Setter
@Getter
@ToString
public class RedisOAuth2AuthorizationConsent implements Serializable {
	public static final String AUTHORITIES_SCOPE_PREFIX = "SCOPE_";
	private static final long serialVersionUID = 1L;
	private  String registeredClientId;
	private  String principalName;
	private  Set<String> authorities;

}
