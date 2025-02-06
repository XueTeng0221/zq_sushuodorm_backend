package com.ziqiang.sushuodorm.entity.vo;

import com.ziqiang.sushuodorm.common.BaseResponse;
import com.ziqiang.sushuodorm.common.ErrorCode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ResponseBeanVo<T> {
    private String code;
    private T data;
    private String message;
    private Boolean isSuccess;

    public ResponseBeanVo(BaseResponse<T> baseResponse, T data) {
        this.code = baseResponse.getCode() + "";
        this.data = data;
        this.message = baseResponse.getMessage();
        this.isSuccess = baseResponse.getCode() == 0;
    }

    public static ResponseBeanVo<?> ok() {
        return new ResponseBeanVo<>(new BaseResponse<>(ErrorCode.SUCCESS), null);
    }

    public static <T> ResponseBeanVo<T> ok(T data) {
        return new ResponseBeanVo<>(new BaseResponse<>(ErrorCode.SUCCESS), data);
    }

    public static <T> ResponseBeanVo<?> ok(ErrorCode errorCode, T data) {
        return new ResponseBeanVo<>(new BaseResponse<>(errorCode), data);
    }

    public static <T> ResponseBeanVo<?> ok(String code, T data) {
        Map<String, T> map = new HashMap<>();
        map.put(code, data);
        return new ResponseBeanVo<>(new BaseResponse<>(ErrorCode.SUCCESS), map);
    }

    public static <T> ResponseBeanVo<?> ok(ErrorCode errorCode, String message, T data) {
        Map<String, T> map = new HashMap<>();
        map.put(message, data);
        return new ResponseBeanVo<>(new BaseResponse<>(errorCode, message), map);
    }

    public static <T> ResponseBeanVo<?> error(ErrorCode errorCode, T data) {
        return new ResponseBeanVo<>(new BaseResponse<>(errorCode), data);
    }

    public static <T> ResponseBeanVo<?> error(ErrorCode errorCode, String message, T data) {
        Map<String, T> map = new HashMap<>();
        map.put(message, data);
        return new ResponseBeanVo<>(new BaseResponse<>(errorCode, message), map);
    }

    public static <T> ResponseBeanVo<?> error(ErrorCode errorCode) {
        return new ResponseBeanVo<>(new BaseResponse<>(errorCode), null);
    }
}
