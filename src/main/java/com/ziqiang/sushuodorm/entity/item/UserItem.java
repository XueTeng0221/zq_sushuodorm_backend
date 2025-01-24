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

    private Date updateTime;

    private String userAccount;

    private String userPassword;

    private String unionId;

    private String userName;

    private String userAvatar;

    private String userProfile;

    private String userRole;

    @TableLogic
    public Boolean isDeleted;
}
