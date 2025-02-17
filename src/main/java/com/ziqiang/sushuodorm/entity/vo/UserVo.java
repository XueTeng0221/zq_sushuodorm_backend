package com.ziqiang.sushuodorm.entity.vo;

import com.ziqiang.sushuodorm.entity.enums.UserRoleEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class UserVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private UserRoleEnum userRole;
    private Date createTime;
    private String userName;
    private String userAccount;
    private String userAvatar;
    private String userProfile;
    private String gender;
    private String phone;
    private String roomId;
}
