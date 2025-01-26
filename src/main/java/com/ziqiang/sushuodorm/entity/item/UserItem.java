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
    @TableField
    private Date updateTime;
    @TableField
    private String userAccount;
    @TableField
    private String userPassword;
    @TableField
    private String unionId;
    @TableField
    private String userName;
    @TableField
    private String userAvatar;
    @TableField
    private String userProfile;
    @TableField
    private String userRole;
    @TableField
    private String gender;
    @TableField
    private String phone;
    @TableField
    private String roomId;
    @TableLogic
    public Boolean isDeleted;
}
