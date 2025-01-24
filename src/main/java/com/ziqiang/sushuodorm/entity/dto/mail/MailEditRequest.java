package com.ziqiang.sushuodorm.entity.dto.mail;

import lombok.Data;

import java.io.Serializable;

@Data
public class MailEditRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String email;
    private String title;
    private String subject;
}
