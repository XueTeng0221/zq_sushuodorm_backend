package com.ziqiang.sushuodorm.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public BaseResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public BaseResponse(ErrorCode errorCode, T data) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = data;
    }

    public static <T> BaseResponse<T> error(int code, T data, String message) {
        return new BaseResponse<>(code, data, message);
    }

    public static <T> BaseResponse<T> ok(T data, String message) {
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), data, message);
    }
}
