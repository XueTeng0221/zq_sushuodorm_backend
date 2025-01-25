package com.ziqiang.sushuodorm.entity.dto.mail;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ziqiang.sushuodorm.entity.item.MailItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.sql.Date;
import java.util.Map;

@Data
public class MailEsDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, UserItem> receivers;
    private UserItem sender;
    private Date date;
    private String email;
    private String title;
    private String subject;
    private String senderName;
    private Boolean isDeleted;
    private Long id;
    private Long userId;

    public static MailEsDTO objectToDTO(MailItem mailItem) {
        if (ObjectUtils.isEmpty(mailItem)) {
            return null;
        }
        MailEsDTO mailEsDTO = new MailEsDTO();
        BeanUtils.copyProperties(mailItem, mailEsDTO);
        if (StringUtils.isNotBlank(mailItem.getDate().toString())) {
            mailEsDTO.setDate(mailItem.getDate());
        }
        if (CollectionUtils.isNotEmpty(mailItem.getReceivers())) {
            mailEsDTO.setReceivers(mailItem.getReceivers());
        }
        return mailEsDTO;
    }

    public static MailItem dtoToObject(MailEsDTO mailEsDTO) {
        if (ObjectUtils.isEmpty(mailEsDTO)) {
            return null;
        }
        MailItem mailItem = new MailItem();
        BeanUtils.copyProperties(mailEsDTO, mailItem);
        if (StringUtils.isNotBlank(mailEsDTO.getDate().toString())) {
            mailItem.setDate(mailEsDTO.getDate());
        }
        if (CollectionUtils.isNotEmpty(mailEsDTO.getReceivers())) {
            mailItem.setReceivers(mailEsDTO.getReceivers());
        }
        return mailItem;
    }
}
