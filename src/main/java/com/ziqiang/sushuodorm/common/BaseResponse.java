package com.ziqiang.sushuodorm.common;

import com.ziqiang.sushuodorm.entity.vo.ResponseBeanVo;
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

    public BaseResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public BaseResponse(ErrorCode errorCode, T data) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = data;
    }
}
