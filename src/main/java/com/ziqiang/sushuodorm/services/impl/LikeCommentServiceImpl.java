package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@EqualsAndHashCode(callSuper = false)
public class LikeCommentServiceImpl extends ServiceImpl<LikeCommentMapper, LikeCommentItem> implements LikeCommentService {
    @Autowired
    private LikeCommentMapper likeCommentMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public boolean save(String userId, Long commentId) {
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getAuthor, userId)
                .eq(CommentItem::getId, commentId);
        CommentItem commentItem = commentMapper.selectOne(queryWrapper);
        commentItem.setLikes(commentItem.getLikes() + 1);
        return commentMapper.updateById(commentItem) > 0;
    }

    @Override
    public boolean remove(String userId, Long commentId) {
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getAuthor, userId)
                .eq(CommentItem::getId, commentId);
        CommentItem commentItem = commentMapper.selectOne(queryWrapper);
        commentItem.setLikes(commentItem.getLikes() - 1);
        return commentMapper.updateById(commentItem) > 0;
    }

    @Override
    public IPage<LikeCommentItem> getPage(String userId, int pageNum, int pageId) {
        LambdaQueryChainWrapper<LikeCommentItem> queryWrapper = new QueryChainWrapper<>(likeCommentMapper).lambda()
                .eq(LikeCommentItem::getUserId, userId)
                .orderByDesc(LikeCommentItem::getDate);
        return likeCommentMapper.selectPage(new Page<>(pageNum, pageId), queryWrapper).convert(
                likeCommentItem -> new LikeCommentItem()
                        .setUserId(likeCommentItem.getUserId())
        );
    }
}
