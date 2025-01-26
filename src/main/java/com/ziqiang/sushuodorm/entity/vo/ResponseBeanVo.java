package com.ziqiang.sushuodorm.entity.vo;

import com.ziqiang.sushuodorm.common.BaseResponse;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponseBeanVo<T> {
    private String code;
    private T data;
    private String message;
    private Boolean isSuccess;

    public ResponseBeanVo(BaseResponse baseResponse, T data) {
        this.code = baseResponse.getCode() + "";
        this.data = data;
        this.message = baseResponse.getMessage();
        this.isSuccess = baseResponse.getCode() == 0;
    }
}
