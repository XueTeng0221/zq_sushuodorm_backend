package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;

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
            QueryChainWrapper<PostItem> queryWrapper = new QueryChainWrapper<>(postMapper)
                    .eq("id", userId)
                    .eq("post_id", postId)
                    .eq("is_deleted", false);
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
        QueryChainWrapper<LikePostItem> queryWrapper = new QueryChainWrapper<>(likePostMapper)
                .eq("id", userId)
                .eq("post_id", postId)
                .eq("is_deleted", false);
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
        QueryChainWrapper<LikePostItem> queryWrapper = new QueryChainWrapper<>(likePostMapper)
                .eq("user_id", userId)
                .eq("is_deleted", false);
        return likePostMapper.selectPage(new Page<>(pageNum, pageId), queryWrapper).convert(
                likePostItem -> new LikePostItem()
                        .setUserId(likePostItem.getUserId())
        );
    }
}

