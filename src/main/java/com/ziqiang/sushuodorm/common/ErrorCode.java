package com.ziqiang.sushuodorm.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(0, "ok", "00000"),
    CLIENT_ERROR(400, "客户端错误", "A0000"),
    LOGIN_FAILED(400, "登录失败", "A0210"),
    USERNAME_NOT_EXIST(400, "用户名不存在", "A0211"),
    PASSWORD_ERROR(400, "密码错误", "A0212"),
    PHONE_NOT_EXIST(400, "手机号不存在", "A0213"),
    LOGIN_EXPIRED(401, "登录过期", "A0220"),
    TOKEN_INVALID(401, "token无效", "A0221"),
    THIRD_LOGIN_FAILED(401, "第三方登录失败", "A0230"),
    THIRD_LOGIN_CAPTCHA_ERROR(401, "第三方登录验证码错误", "A0232"),
    THIRD_LOGIN_EXPIRED(401, "第三方登录过期", "A0233"),
    PARAM_ERROR(400, "参数错误", "A0400"),
    JSON_PARSE_FAILED(400, "JSON解析失败", "A0410"),
    PARAM_EMPTY(400, "参数为空", "A0420"),
    REQUEST_ERROR(400, "请求错误", "A0500"),
    THIRD_SERVICE_ERROR(500, "第三方服务错误", "C0000"),
    THIRD_SERVICE_TIMEOUT_ERROR(500, "第三方服务超时", "C0200"),
    DATABASE_ERROR(500, "数据库错误", "C0300"),
    CACHE_ERROR(500, "缓存错误", "C0400"),
    NOTIFICATION_ERROR(500, "通知错误", "C0500");

    private final int code;
    private final String message;
    private final String description;
}
