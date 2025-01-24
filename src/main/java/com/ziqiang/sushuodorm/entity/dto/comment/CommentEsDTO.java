package com.ziqiang.sushuodorm.entity.dto.comment;

import com.ziqiang.sushuodorm.entity.item.CommentItem;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Data
public class CommentEsDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Date date;
    private String title;
    private String content;
    private String author;
    private Long likes;
    private Long favorites;
    private Long userId;
    private Boolean isDelete;

    public static CommentEsDTO objectToDTO(CommentItem commentItem) {
        if (commentItem == null) {
            return null;
        }
        CommentEsDTO commentEsDTO = new CommentEsDTO();
        BeanUtils.copyProperties(commentItem, commentEsDTO);
        if (StringUtils.isNotBlank(commentItem.getDate().toString())) {
            commentEsDTO.setDate(commentItem.getDate());
        }
        return commentEsDTO;
    }

    public static CommentItem dtoToObject(CommentEsDTO commentEsDTO) {
        if (commentEsDTO == null) {
            return null;
        }
        CommentItem commentItem = new CommentItem();
        BeanUtils.copyProperties(commentEsDTO, commentItem);
        if (commentEsDTO.getDate() != null) {
            commentItem.setDate(commentEsDTO.getDate());
        }
        return commentItem;
    }
}
