package com.allen.demoserver.config;

import com.allen.component.redis.repository.RedisRepository;
import com.allen.demoserver.service.RedisToJdbcService;
import com.allen.demoserver.service.impl.RedisOAuth2AuthorizationConsentService;
import com.allen.demoserver.service.impl.RedisOAuth2AuthorizationService;
import com.allen.demoserver.service.impl.RedisToJdbcServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * 基于Redis实现 （ServerRedisConfig、ServerJdbcConfig、ServerRedisConfig 使用其一即可）
 */
@Configuration
public class ServerRedisConfig {


    @Bean
    public RedisToJdbcService redisToJdbcService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new RedisToJdbcServiceImpl(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(RegisteredClientRepository registeredClientRepository, RedisRepository redisRepository
            , RedisToJdbcService redisToJdbcService) {
        return new RedisOAuth2AuthorizationService(registeredClientRepository,redisRepository,redisToJdbcService);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(RedisRepository redisRepository, RedisToJdbcService redisToJdbcService) {
        return new RedisOAuth2AuthorizationConsentService(redisRepository,redisToJdbcService);
    }


}
