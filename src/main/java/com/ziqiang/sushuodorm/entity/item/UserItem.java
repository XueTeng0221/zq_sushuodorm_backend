package com.ziqiang.sushuodorm.entity.item;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;

@Data
@Accessors(chain = true)
@TableName(value = "user")
public class UserItem implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableField(value = "update_time")
    private Date updateTime;
    @TableField
    private String userAccount;
    @TableField
    private String userPassword;
    @TableField(value = "union_id")
    private Long unionId;
    @TableField(value = "username")
    private String userName;
    @TableField(value = "avatar_url")
    private String userAvatar;
    @TableField(value = "profile")
    private String userProfile;
    @TableField(value = "role")
    private String userRole;
    @TableField(value = "gender")
    private String gender;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "room_id")
    private String roomId;
    @TableLogic
    public Boolean isDeleted;
}
