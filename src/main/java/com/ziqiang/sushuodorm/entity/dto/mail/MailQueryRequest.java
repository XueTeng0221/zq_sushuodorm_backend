package com.ziqiang.sushuodorm.entity.dto.mail;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ziqiang.sushuodorm.common.PageRequest;
import com.ziqiang.sushuodorm.entity.item.MailItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.mail.MailProperties;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class MailQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Page<MailItem> page;
    private String email;
    private String title;
    private String subject;
}
