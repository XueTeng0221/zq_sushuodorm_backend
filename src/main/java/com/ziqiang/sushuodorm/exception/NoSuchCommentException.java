package com.ziqiang.sushuodorm.exception;

import com.ziqiang.sushuodorm.common.ErrorCode;

import java.util.ArrayList;
import java.util.List;

public class NoSuchCommentException extends BizException {
    private List<String> missingComments = new ArrayList<>();

    public NoSuchCommentException() {
        super(ErrorCode.REQUEST_ERROR.getCode(), "评论不存在，错误码：" + ErrorCode.REQUEST_ERROR.getCode());
    }

    public NoSuchCommentException(List<String> missingComments, int code, String message) {
        super(code, message);
        this.missingComments = missingComments;
    }

    public NoSuchCommentException(ErrorCode errorCode) {
        super(errorCode.getCode(), "评论不存在，错误码：" + errorCode.getCode());
    }

    public NoSuchCommentException(ErrorCode errorCode, String message) {
        super(errorCode.getCode(), "评论不存在，错误码：" + errorCode.getCode() + "，自定义错误信息：" + message);
    }
}
