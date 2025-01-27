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
    private Long postId;
    private Long commentId;
    private Boolean isDelete;
}
