package com.allen.demoserver.extension.captcha;

import com.allen.demoserver.enums.ResponseStatusEnum;
import com.allen.demoserver.enums.SmarketingGrant;
import com.allen.demoserver.exception.ServerAuthencticationException;
import com.allen.demoserver.util.OAuth2AuthenticationProviderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.Collections;

/**
 * An {@link AuthenticationProvider} implementation for the OAuth 2.0 Password Grant.
 *
 * @author ZnPi
 * @date 2022-10-21
 */
@Slf4j
public class OAuth2CaptchaAuthenticationProvider implements AuthenticationProvider {
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
    private final AuthenticationManager authenticationManager;
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    /**
     * Constructs an {@code OAuth2PasswordAuthenticationProvider} using the provided parameters.
     *
     * @param authorizationService the authorization service
     * @param tokenGenerator       the token generator
     */
    public OAuth2CaptchaAuthenticationProvider(AuthenticationManager authenticationManager,
                                               OAuth2AuthorizationService authorizationService,
                                               OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authenticationManager = authenticationManager;
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2CaptchaAuthenticationToken passwordAuthentication = (OAuth2CaptchaAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal =
                OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient(passwordAuthentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        Assert.notNull(registeredClient, "registeredClient cannot be null");

        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.PASSWORD)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        if (log.isTraceEnabled()) {
            log.trace("Retrieved registered client");
        }

        // Attempts to authenticate the passwordAuthentication
        Authentication authenticate = authenticate(passwordAuthentication);

        DefaultOAuth2TokenContext.Builder tokenContextBuilder = getTokenContextBuilder(passwordAuthentication,
                registeredClient, authenticate);

        OAuth2Authorization.Builder authorizationBuilder = getAuthorizationBuilder(passwordAuthentication,
                registeredClient);

        // 可拓展 attribute
        authorizationBuilder.attribute(Principal.class.getName(), authentication);

        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();

        // ----- Access token -----
        OAuth2AccessToken accessToken = getAccessToken(authorizationBuilder, tokenContext);

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = getRefreshToken(clientPrincipal, registeredClient, tokenContextBuilder, authorizationBuilder);

        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient, clientPrincipal, accessToken, refreshToken, Collections.emptyMap());
    }

    private Authentication authenticate(OAuth2CaptchaAuthenticationToken passwordAuthentication) {
        // todo 这里自定义验证码处理，通过手机号获取到用户信息，然后赋予账号密码
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken("admin",
                        "123456");
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(token);
        } catch (Exception e) {
            throw oAuth2AuthenticationException(e);
        }

        return authenticate;
    }

    private static DefaultOAuth2TokenContext.Builder getTokenContextBuilder(
            OAuth2CaptchaAuthenticationToken passwordAuthentication,
            RegisteredClient registeredClient,
            Authentication authenticate) {
        return DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authenticate)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizationGrantType(new AuthorizationGrantType(SmarketingGrant.CAPTCHA))
                .authorizationGrant(passwordAuthentication);
    }

    private static OAuth2Authorization.Builder getAuthorizationBuilder(
            OAuth2CaptchaAuthenticationToken passwordAuthentication,
            RegisteredClient registeredClient) {
        return OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(passwordAuthentication.getName())
                .authorizationGrantType(new AuthorizationGrantType(SmarketingGrant.CAPTCHA));
    }

    private OAuth2AccessToken getAccessToken(OAuth2Authorization.Builder authorizationBuilder,
                                             OAuth2TokenContext tokenContext) {
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        if (log.isTraceEnabled()) {
            log.trace("Generated access token");
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                            ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }
        return accessToken;
    }

    private OAuth2RefreshToken getRefreshToken(OAuth2ClientAuthenticationToken clientPrincipal,
                                               RegisteredClient registeredClient,
                                               DefaultOAuth2TokenContext.Builder tokenContextBuilder,
                                               OAuth2Authorization.Builder authorizationBuilder) {
        OAuth2TokenContext tokenContext;
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // Do not issue refresh token to public client
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            if (log.isTraceEnabled()) {
                log.trace("Generated refresh token");
            }

            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);

            this.authorizationService.save(authorizationBuilder.build());

            if (log.isTraceEnabled()) {
                log.trace("Saved authorization");
            }
        }
        return refreshToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2CaptchaAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * OAuth2 认证失败处理器只能处理 OAuth2AuthenticationException，故转换
     *
     * @param authenticationException 身份验证异常
     * @return {@link OAuth2AuthenticationException}
     */
    private OAuth2AuthenticationException oAuth2AuthenticationException(Exception authenticationException) {
        if (authenticationException instanceof UsernameNotFoundException) {
            return new ServerAuthencticationException(ResponseStatusEnum.ACCOUNT_NOT_EXIST);
        }
        if (authenticationException instanceof BadCredentialsException) {
            return new ServerAuthencticationException(ResponseStatusEnum.USERNAME_OR_PASSWORD_INCORRECT);
        }
        if (authenticationException instanceof LockedException) {
            return new ServerAuthencticationException(ResponseStatusEnum.ACCOUNT_FROZEN);
        }
        if (authenticationException instanceof AccountExpiredException) {
            return new ServerAuthencticationException(ResponseStatusEnum.ACCOUNT_EXPIRED);
        }
        if (authenticationException instanceof CredentialsExpiredException) {
            return new ServerAuthencticationException(ResponseStatusEnum.PASSWORD_EXPIRED);
        }
        if (authenticationException instanceof InternalAuthenticationServiceException) {
            if (authenticationException.getCause() instanceof DisabledException) {
                return new ServerAuthencticationException(ResponseStatusEnum.ACCOUNT_DISABLE);
            }
        }
        return new ServerAuthencticationException(ResponseStatusEnum.IDENTITY_VERIFICATION_FAILED);
    }
}
