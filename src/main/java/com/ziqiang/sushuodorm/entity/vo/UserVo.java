package com.ziqiang.sushuodorm.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class UserVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Date createTime;
    private String userName;
    private String userAccount;
    private String userAvatar;
    private String userProfile;
}
