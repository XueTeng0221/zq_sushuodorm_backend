package com.ziqiang.sushuodorm.entity.dto.mail;

import lombok.Data;
import org.springframework.boot.autoconfigure.mail.MailProperties;

import java.io.Serializable;

@Data
public class MailAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private MailProperties mailProperties;
    private String email;
    private String title;
    private String subject;
}
