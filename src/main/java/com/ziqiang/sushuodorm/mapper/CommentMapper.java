package com.ziqiang.sushuodorm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<CommentItem> {
    List<CommentItem> listCommentByUserId(Long userId);

    List<CommentItem> listCommentByPage(IPage<CommentItem> page, Wrapper<CommentItem> queryWrapper);

    List<CommentItem> listCommentByTags(List<String> tags);

    Long getReplies(Long commentId, Long postId);

    Long getReplies(String replierName, String userName, Long postId);
}
