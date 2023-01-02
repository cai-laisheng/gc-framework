package com.allen.demoserver.service.impl;

import com.allen.demoserver.deserializer.CustomUserDetailsMxin;
import com.allen.demoserver.entity.CustomUserDetails;
import com.allen.demoserver.service.RedisToJdbcService;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

import java.util.List;

/**
 * @Author xuguocai   redis 与数据库交互类
 * @Date 21:16 2022/12/20
 **/
public class RedisToJdbcServiceImpl implements RedisToJdbcService {

	private final JdbcOAuth2AuthorizationService jdbcOAuth2AuthorizationService;
	private final JdbcOAuth2AuthorizationConsentService jdbcOAuth2AuthorizationConsentService;

	public RedisToJdbcServiceImpl(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
		jdbcOAuth2AuthorizationService = new JdbcOAuth2AuthorizationService(jdbcTemplate,registeredClientRepository);
		jdbcObjectMapper(registeredClientRepository);
		jdbcOAuth2AuthorizationConsentService = new JdbcOAuth2AuthorizationConsentService(jdbcTemplate,registeredClientRepository);
	}

	private void jdbcObjectMapper(RegisteredClientRepository registeredClientRepository){
		JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper authorizationRowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(
				registeredClientRepository);
		authorizationRowMapper.setLobHandler(new DefaultLobHandler());

		ObjectMapper objectMapper = new ObjectMapper();
		ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
		List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
		objectMapper.registerModules(securityModules);
		objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
		objectMapper.addMixIn(CustomUserDetails.class, CustomUserDetailsMxin.class);
		authorizationRowMapper.setObjectMapper(objectMapper);

		this.jdbcOAuth2AuthorizationService.setAuthorizationRowMapper(authorizationRowMapper);
	}

	@Override
	public void save(OAuth2Authorization authorization) {
		jdbcOAuth2AuthorizationService.save(authorization);
	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		jdbcOAuth2AuthorizationService.remove(authorization);
	}

	@Override
	public OAuth2Authorization findById(String id) {
		return jdbcOAuth2AuthorizationService.findById(id);
	}

	@Override
	public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
		return jdbcOAuth2AuthorizationService.findByToken(token,tokenType);
	}

	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		jdbcOAuth2AuthorizationConsentService.save(authorizationConsent);
	}

	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		jdbcOAuth2AuthorizationConsentService.remove(authorizationConsent);
	}

	@Override
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		return jdbcOAuth2AuthorizationConsentService.findById(registeredClientId,principalName);
	}
}
