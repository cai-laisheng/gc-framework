package com.allen.demoserver.exception;

import com.allen.demoserver.enums.ResponseStatusEnum;
import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * @author ZnPi
 * @date 2022-10-21
 */
@Getter
public class ServerAuthencticationException extends OAuth2AuthenticationException {
    private final String code;

    public ServerAuthencticationException(ResponseStatusEnum status) {
        super(new OAuth2Error(status.getMsg()), status.getMsg());
        this.code = status.getCode();
    }

    public ServerAuthencticationException(ResponseStatusEnum status, String msg) {
        super(new OAuth2Error(status.getMsg()), msg);
        this.code = status.getCode();
    }
}