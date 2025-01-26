package com.ziqiang.sushuodorm.entity.item;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.autoconfigure.mail.MailProperties;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@TableName(value = "mail")
public class MailItem implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableField(value = "receivers")
    private Map<String, UserItem> receivers;
    @TableField(value = "sender")
    private UserItem sender;
    @TableField(value = "date")
    private Date date;
    @TableField(value = "id")
    private Long id;
    @TableField(value = "userId")
    private Long userId;
    @TableField(value = "senderName")
    private String senderName;
    @TableField(value = "email")
    private String email;
    @TableField(value = "title")
    private String title;
    @TableField(value = "subject")
    private String subject;
    @TableLogic
    private Boolean isRead;
    @TableLogic
    private Boolean isDeleted;
    @TableLogic
    private Boolean isReplied;
}
