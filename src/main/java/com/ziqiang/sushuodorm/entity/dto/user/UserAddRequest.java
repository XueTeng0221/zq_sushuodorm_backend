package com.ziqiang.sushuodorm.entity.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userName;
    private String userAccount;
    private String userAvatar;
    private String userRole;
    private String userProfile;
    private String gender;
    private String roomId;
    private String phone;
}
