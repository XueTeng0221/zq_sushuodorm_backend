package com.ziqiang.sushuodorm.exception;

import com.ziqiang.sushuodorm.common.ErrorCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NoSuchPostException extends BizException {
    private List<String> missingPosts = new ArrayList<>();

    public NoSuchPostException(List<String> missingPosts, int code, String message) {
        super(code, message);
        this.missingPosts = missingPosts;
    }

    public NoSuchPostException(ErrorCode errorCode) {
        super(errorCode.getCode(), "帖子不存在，错误码：" + errorCode.getCode());
    }

    public NoSuchPostException(ErrorCode errorCode, String message) {
        super(errorCode.getCode(), "帖子不存在，错误码：" + errorCode.getCode() + "，自定义错误信息：" + message);
    }
}
