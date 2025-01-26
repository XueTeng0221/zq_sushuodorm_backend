package com.ziqiang.sushuodorm.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(0, "ok", ""),
    CLIENT_ERROR(400, "客户端错误", "A0000");

    private final int code;
    private final String message;
    private final String description;
}
