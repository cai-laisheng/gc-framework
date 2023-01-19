package com.allen.demoserver.extension.captcha;

import com.allen.demoserver.enums.ResponseStatusEnum;
import com.allen.demoserver.enums.SmarketingGrant;
import com.allen.demoserver.exception.ServerAuthencticationException;
import com.allen.demoserver.util.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Attempts to extract an Access Token Request from {@link HttpServletRequest} for the OAuth 2.0 Password Grant
 * and then converts it to an {@link OAuth2CaptchaAuthenticationToken} used for authenticating the authorization grant.
 *
 * @author ZnPi
 * @date 2022-10-20
 */
public class OAuth2CaptchaAuthenticationConverter implements AuthenticationConverter {
    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!SmarketingGrant.CAPTCHA.equals(grantType)) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

        // phone (REQUIRED)
        String phone = parameters.getFirst(SmarketingGrant.PHONE);
        if (!StringUtils.hasText(phone) ||
                parameters.get(SmarketingGrant.PHONE).size() != 1) {
            throw new ServerAuthencticationException(ResponseStatusEnum.ACCOUNT_NOT_EXIST, "phone cannot be null");
        }

        // captcha (REQUIRED)
        String captcha = parameters.getFirst(SmarketingGrant.CAPTCHA);
        if (!StringUtils.hasText(captcha) ||
                parameters.get(SmarketingGrant.CAPTCHA).size() != 1) {
            throw new ServerAuthencticationException(ResponseStatusEnum.CAPTCHA_INCORRECT, "captcha cannot be null");
        }

        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(SmarketingGrant.PHONE) &&
                    !key.equals(SmarketingGrant.CAPTCHA)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        return new OAuth2CaptchaAuthenticationToken(
                phone, captcha, clientPrincipal, additionalParameters);
    }
}
