package com.ziqiang.sushuodorm.entity.item;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.boot.autoconfigure.mail.MailProperties;

import java.io.Serializable;
import java.sql.Date;
import java.util.Map;

@Data
@TableName(value = "mail")
public class MailItem implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    private MailProperties mailProperties;

    private Map<String, UserItem> receivers;

    private Long id;

    private Long userId;

    private String senderName;

    private UserItem sender;

    private String email;

    private String title;

    private String subject;

    private Date date;

    @TableLogic
    private Boolean isRead;

    @TableLogic
    private Boolean isDeleted;

    @TableLogic
    private Boolean isReplied;
}