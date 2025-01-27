package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.item.LikeCommentItem;
import com.ziqiang.sushuodorm.mapper.CommentMapper;
import com.ziqiang.sushuodorm.mapper.LikeCommentMapper;
import com.ziqiang.sushuodorm.services.LikeCommentService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Service;

@Service
@Data
@EqualsAndHashCode(callSuper = false)
public class LikeCommentServiceImpl extends ServiceImpl<LikeCommentMapper, LikeCommentItem> implements LikeCommentService {
    private LikeCommentMapper likeCommentMapper;
    private CommentMapper commentMapper;

    public LikeCommentServiceImpl(LikeCommentMapper likeCommentMapper, CommentMapper commentMapper) {
        this.likeCommentMapper = likeCommentMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public boolean save(String userId, Long commentId) {
        QueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper)
                .eq("id", userId)
                .eq("comment_id", commentId)
                .eq("is_deleted", false);
        CommentItem commentItem = commentMapper.selectOne(queryWrapper);
        commentItem.setLikes(commentItem.getLikes() + 1);
        return commentMapper.updateById(commentItem) > 0;
    }

    @Override
    public boolean remove(String userId, Long commentId) {
        QueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper)
                .eq("id", userId)
                .eq("comment_id", commentId)
                .eq("is_deleted", false);
        CommentItem commentItem = commentMapper.selectOne(queryWrapper);
        commentItem.setLikes(commentItem.getLikes() - 1);
        return commentMapper.updateById(commentItem) > 0;
    }

    @Override
    public IPage<LikeCommentItem> getPage(String userId, int pageNum, int pageId) {
        QueryChainWrapper<LikeCommentItem> queryWrapper = new QueryChainWrapper<>(likeCommentMapper)
                .eq("user_id", userId)
                .eq("is_deleted", false);
        return likeCommentMapper.selectPage(new Page<>(pageNum, pageId), queryWrapper).convert(
                likeCommentItem -> new LikeCommentItem()
                        .setUserId(likeCommentItem.getUserId())
        );
    }
}
