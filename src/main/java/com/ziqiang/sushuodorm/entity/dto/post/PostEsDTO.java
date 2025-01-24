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

    public static PostEsDTO objectToDTO(PostItem postItem) {
        if (postItem == null) {
            return null;
        }
        PostEsDTO postEsDTO = new PostEsDTO();
        BeanUtils.copyProperties(postItem, postEsDTO);
        String tagsStr = postItem.getTags();
        if (StringUtils.isNotBlank(tagsStr)) {
            postEsDTO.setTags(List.of(tagsStr.split(",")));
        }
        if (StringUtils.isNotBlank(postItem.getDate().toString())) {
            postEsDTO.setDate(postItem.getDate());
        }
        if (!CollectionUtils.isEmpty(postItem.getComments())) {
            postEsDTO.setComments(postItem.getComments());
        }
        return postEsDTO;
    }

    public static PostItem dtoToObject(PostEsDTO postEsDTO) {
        if (postEsDTO == null) {
            return null;
        }
        PostItem postItem = new PostItem();
        BeanUtils.copyProperties(postEsDTO, postItem);
        List<String> tagList = postEsDTO.getTags();
        if (!CollectionUtils.isEmpty(tagList)) {
            postItem.setTags(StringUtils.join(tagList, ","));
        }
        if (StringUtils.isNotBlank(postEsDTO.getDate().toString())) {
            postItem.setDate(postEsDTO.getDate());
        }
        if (!CollectionUtils.isEmpty(postEsDTO.getComments())) {
            postItem.setComments(postEsDTO.getComments());
        }
        return postItem;
    }
}
