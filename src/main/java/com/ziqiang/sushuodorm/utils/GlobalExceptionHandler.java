package com.ziqiang.sushuodorm.utils;

import com.ziqiang.sushuodorm.common.BaseResponse;
import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.vo.ResponseBeanVo;
import com.ziqiang.sushuodorm.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BizException.class)
    public BaseResponse<?> bizExceptionHandler(BizException e) {
        log.error("bizException:", e);
        return new BaseResponse<>(ErrorCode.CLIENT_ERROR, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException:", e);
        return new BaseResponse<>(ErrorCode.CLIENT_ERROR, e.getMessage());
    }
}
