package com.ziqiang.sushuodorm.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    UNFINISHED("未完成", "unfinished"),
    FINISHED("已完成", "finished"),
    CANCELLED("已取消", "cancelled");

    private final String text;
    private final String value;

    public static OrderStatusEnum getEnumByValue(String value) {
        for (OrderStatusEnum orderStatusEnum : values()) {
            if (orderStatusEnum.getValue().equals(value)) {
                return orderStatusEnum;
            }
        }
        return null;
    }
}
