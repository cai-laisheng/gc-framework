package org.springframework.core;

import org.springframework.lang.Nullable;

import java.io.IOException;

/**
 * @Author xuguocai
 * @Date 14:22 2022/12/25
 **/
public class NestedIOException extends IOException {
    public NestedIOException(String msg) {
        super(msg);
    }

    public NestedIOException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    @Nullable
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), this.getCause());
    }

    static {
        NestedExceptionUtils.class.getName();
    }
}
