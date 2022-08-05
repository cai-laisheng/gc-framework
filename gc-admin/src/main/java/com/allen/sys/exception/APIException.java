package com.allen.sys.exception;

import com.allen.sys.constants.ResponseCodeEnum;
import lombok.Getter;

/**
 * @author xuguocai
 * @date 2022/8/5 14:45
 */
@Getter
public class APIException extends RuntimeException{
    private int code;
    private String msg;

    // 手动设置异常
    public APIException(ResponseCodeEnum statusCode, String message) {
        // message用于用户设置抛出错误详情，例如：当前价格-5，小于0
        super(message);
        // 状态码
        this.code = statusCode.getCode();
        // 状态码配套的msg
        this.msg = statusCode.getfMsg();
    }

    // 默认异常使用APP_ERROR状态码
    public APIException(String message) {
        super(message);
        this.code = ResponseCodeEnum.FAIL.getCode();
        this.msg = ResponseCodeEnum.FAIL.getfMsg();
    }
}
