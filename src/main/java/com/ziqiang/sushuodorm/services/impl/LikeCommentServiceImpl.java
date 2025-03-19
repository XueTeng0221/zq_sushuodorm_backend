package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.item.LikeCommentItem;
import com.ziqiang.sushuodorm.exception.NoSuchCommentException;
import com.ziqiang.sushuodorm.exception.NoSuchPostException;
import com.ziqiang.sushuodorm.mapper.CommentMapper;
import com.ziqiang.sushuodorm.mapper.LikeCommentMapper;
import com.ziqiang.sushuodorm.services.LikeCommentService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
@EqualsAndHashCode(callSuper = false)
public class LikeCommentServiceImpl extends ServiceImpl<LikeCommentMapper, LikeCommentItem> implements LikeCommentService {
    @Autowired
    private LikeCommentMapper likeCommentMapper;
    @Autowired
    private CommentMapper commentMapper;

    public Optional<CommentItem> selectOptional(Wrapper<CommentItem> commentItemWrapper) {
        return Optional.ofNullable(commentMapper.selectOne(commentItemWrapper));
    }
    
    @Override
    public boolean save(String userId, Long commentId) throws NoSuchCommentException {
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getAuthor, userId)
                .eq(CommentItem::getId, commentId);
        CommentItem commentItem = selectOptional(queryWrapper).orElseThrow(NoSuchCommentException::new);
        commentItem.setLikes(commentItem.getLikes() + 1);
        return commentMapper.updateById(commentItem) > 0;
    }

    @Override
    public boolean remove(String userId, Long commentId) throws NoSuchPostException {
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getAuthor, userId)
                .eq(CommentItem::getId, commentId);
        CommentItem commentItem = selectOptional(queryWrapper).orElseThrow(NoSuchCommentException::new);
        commentItem.setLikes(commentItem.getLikes() - 1);
        return commentMapper.updateById(commentItem) > 0;
    }

    @Override
    public IPage<LikeCommentItem> getPage(String userId, int pageNum, int pageId) {
        LambdaQueryChainWrapper<LikeCommentItem> queryWrapper = new QueryChainWrapper<>(likeCommentMapper).lambda()
                .eq(LikeCommentItem::getUserId, userId)
                .orderByDesc(LikeCommentItem::getDate);
        List<LikeCommentItem> likeCommentList = likeCommentMapper.selectList(queryWrapper);
        likeCommentList.forEach(likeCommentItem -> {
                LambdaQueryChainWrapper<CommentItem> commentItemQueryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                        .eq(CommentItem::getId, likeCommentItem.getCommentId());
                CommentItem commentItem = commentMapper.selectOne(commentItemQueryWrapper);
                likeCommentItem.setCommentId(commentItem.getId());
            }
        );
        IPage<LikeCommentItem> page = new Page<>(pageNum, pageId);
        page.setRecords(likeCommentList);
        return page;
    }
}
