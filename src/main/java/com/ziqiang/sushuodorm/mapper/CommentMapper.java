package com.ziqiang.sushuodorm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.entity.item.CommentItem;

import java.util.List;

public interface CommentMapper extends BaseMapper<CommentItem> {
    List<CommentItem> listCommentByPage(IPage<CommentItem> page, Wrapper<CommentItem> queryWrapper);

    List<CommentItem> listCommentByTags(List<String> tags);
}
