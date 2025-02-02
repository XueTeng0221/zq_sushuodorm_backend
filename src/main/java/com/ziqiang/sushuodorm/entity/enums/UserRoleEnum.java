package com.ziqiang.sushuodorm.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum UserRoleEnum {
    ADMIN("管理员", "admin"), USER("学生", "student");

    private final String text;
    private final String value;

    public static List<String> getValues() {
        return Arrays.stream(values()).map(UserRoleEnum::getValue).collect(Collectors.toList());
    }

    public static UserRoleEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserRoleEnum roleEnum : UserRoleEnum.values()) {
            if (roleEnum.value.equals(value)) {
                return roleEnum;
            }
        }
        return null;
    }
}
