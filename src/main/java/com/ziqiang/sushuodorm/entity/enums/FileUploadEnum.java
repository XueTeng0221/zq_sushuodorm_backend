package com.ziqiang.sushuodorm.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum FileUploadEnum {
    USER_AVATAR("用户头像", "userAvatar"), IMAGE("图片", "image");

    private final String text;
    private final String value;

    public static List<String> getValues() {
        return Arrays.stream(FileUploadEnum.values()).map(FileUploadEnum::getValue).collect(Collectors.toList());
    }

    public static FileUploadEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (FileUploadEnum fileUploadEnum : FileUploadEnum.values()) {
            if (fileUploadEnum.getValue().equals(value)) {
                return fileUploadEnum;
            }
        }
        return null;
    }
}
