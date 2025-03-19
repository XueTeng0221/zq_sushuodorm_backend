package com.ziqiang.sushuodorm.entity.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateMyRequest implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;
    private String userAccount;
    private String userAvatar;
    private String userProfile;
}
