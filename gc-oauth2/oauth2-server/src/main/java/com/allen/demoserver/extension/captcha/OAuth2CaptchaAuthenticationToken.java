package com.allen.demoserver.extension.captcha;

import com.allen.demoserver.enums.SmarketingGrant;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * An Authentication implementation used for the OAuth 2 Password Grant.
 *
 * @author ZnPi
 * @date 2022-10-21
 */
public class OAuth2CaptchaAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {
    @Getter
    private final String phone;
    @Getter
    private final String captcha;

    /**
     * Constructs an {@code OAuth2PasswordAuthenticationToken} using the provided parameters.
     *
     * @param phone             the username
     * @param captcha             the password
     * @param clientPrincipal      the authenticated client principal
     * @param additionalParameters the additional parameters
     */
    protected OAuth2CaptchaAuthenticationToken(String phone, String captcha, Authentication clientPrincipal,
                                               Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(SmarketingGrant.CAPTCHA), clientPrincipal, additionalParameters);
        Assert.hasText(phone, "username cannot be empty");
        Assert.hasText(captcha, "password cannot be empty");
        this.phone = phone;
        this.captcha = captcha;
    }
}
