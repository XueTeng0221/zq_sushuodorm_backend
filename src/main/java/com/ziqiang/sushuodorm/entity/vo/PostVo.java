package com.ziqiang.sushuodorm.entity.vo;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
public class PostVo implements Serializable {
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

    public static PostVo objectToVO(PostItem postItem) {
        if (ObjectUtils.isNull(postItem)) {
            return null;
        }
        PostVo postVo = new PostVo();
        BeanUtils.copyProperties(postItem, postVo);
        String tagsStr = postItem.getTags();
        if (StringUtils.isNotBlank(tagsStr)) {
            postVo.setTags(List.of(tagsStr.split(",")));
        }
        if (StringUtils.isNotBlank(postItem.getDate().toString())) {
            postVo.setDate(postItem.getDate());
        }
        if (!CollectionUtils.isEmpty(postItem.getComments())) {
            postVo.setComments(postItem.getComments());
        }
        return postVo;
    }

    public static PostItem voToObject(PostVo postVo) {
        if (ObjectUtils.isNull(postVo)) {
            return null;
        }
        PostItem postItem = new PostItem();
        BeanUtils.copyProperties(postVo, postItem);
        List<String> tagList = postVo.getTags();
        if (!CollectionUtils.isEmpty(tagList)) {
            postItem.setTags(StringUtils.join(tagList, ","));
        }
        if (postVo.getDate() != null) {
            postItem.setDate(postVo.getDate());
        }
        if (!CollectionUtils.isEmpty(postVo.getComments())) {
            postItem.setComments(postVo.getComments());
        }
        return postItem;
    }
}
