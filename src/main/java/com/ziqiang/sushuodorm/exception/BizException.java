package com.ziqiang.sushuodorm.exception;

import com.ziqiang.sushuodorm.common.ErrorCode;
import lombok.Getter;

import java.util.List;

@Getter
public class BizException extends RuntimeException {
    private final int code;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(ErrorCode errorCode) {
        super("错误码：" + errorCode.getCode());
        this.code = errorCode.getCode();
    }

    public BizException(ErrorCode errorCode, String message) {
        super("错误码：" + errorCode.getCode() + "，自定义错误信息：" + message);
        this.code = errorCode.getCode();
    }
}
