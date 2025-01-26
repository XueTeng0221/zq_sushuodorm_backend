package com.ziqiang.sushuodorm.entity.dto.post;

import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Data
public class PostEsDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String> tags;
    private Set<CommentItem> comments;
    private Date date;
    private String title;
    private String content;
    private String author;
    private Long likes;
    private Long favorites;
    private Long userId;
    private Boolean isDelete;
}
