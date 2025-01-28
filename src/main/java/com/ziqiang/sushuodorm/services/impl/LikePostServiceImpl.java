package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.item.LikePostItem;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import com.ziqiang.sushuodorm.exception.BizException;
import com.ziqiang.sushuodorm.exception.NoSuchPostException;
import com.ziqiang.sushuodorm.mapper.LikePostMapper;
import com.ziqiang.sushuodorm.mapper.PostMapper;
import com.ziqiang.sushuodorm.services.LikePostService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Service;

@Service
@EqualsAndHashCode(callSuper = true)
@Data
public class LikePostServiceImpl extends ServiceImpl<LikePostMapper, LikePostItem> implements LikePostService {
    private LikePostMapper likePostMapper;
    private PostMapper postMapper;

    public LikePostServiceImpl(LikePostMapper likePostMapper, PostMapper postMapper) {
        this.likePostMapper = likePostMapper;
        this.postMapper = postMapper;
    }

    @Override
    public boolean save(String userId, Long postId) {
        try {
            LambdaQueryChainWrapper<PostItem> queryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                    .eq(PostItem::getAuthor, userId)
                    .eq(PostItem::getId, postId)
                    .eq(PostItem::getIsDeleted, false);
            PostItem postItem = postMapper.selectOne(queryWrapper);
            postItem.setLikes(postItem.getLikes() + 1);
            return likePostMapper.insert(new LikePostItem().setPostId(postId)) > 0;
        } catch (BizException e) {
            log.error("Error occurred while fetching posts: ", e);
            throw new NoSuchPostException(ErrorCode.CLIENT_ERROR);
        }
    }

    @Override
    public boolean remove(String userId, Long postId) {
        LambdaQueryChainWrapper<LikePostItem> queryWrapper = new QueryChainWrapper<>(likePostMapper).lambda()
                .eq(LikePostItem::getUserId, userId)
                .eq(LikePostItem::getPostId, postId)
                .eq(LikePostItem::getIsDeleted, false);
        QueryChainWrapper<PostItem> postWrapper = new QueryChainWrapper<>(postMapper)
                .eq("id", userId)
                .eq("post_id", postId)
                .eq("is_deleted", false);
        PostItem postItem = postMapper.selectOne(postWrapper);
        postItem.setLikes(postItem.getLikes() - 1);
        return postMapper.updateById(postItem) > 0 && likePostMapper.delete(queryWrapper) > 0;
    }

    @Override
    public IPage<LikePostItem> getPage(String userId, int pageNum, int pageId) {
        LambdaQueryChainWrapper<LikePostItem> queryWrapper = new QueryChainWrapper<>(likePostMapper).lambda()
                .eq(LikePostItem::getUserId, userId)
                .eq(LikePostItem::getIsDeleted, false)
                .orderByDesc(LikePostItem::getDate);
        return likePostMapper.selectPage(new Page<>(pageNum, pageId), queryWrapper).convert(
                likePostItem -> new LikePostItem()
                        .setUserId(likePostItem.getUserId())
        );
    }
}

