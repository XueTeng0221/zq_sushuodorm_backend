package com.ziqiang.sushuodorm.entity.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String userName;
    private String userAvatar;
    private String userRole;
    private String userProfile;
    private String phone;
    private String roomId;
}
