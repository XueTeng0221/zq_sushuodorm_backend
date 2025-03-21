package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.item.LikePostItem;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import com.ziqiang.sushuodorm.exception.NoSuchPostException;
import com.ziqiang.sushuodorm.mapper.LikePostMapper;
import com.ziqiang.sushuodorm.mapper.PostMapper;
import com.ziqiang.sushuodorm.services.LikePostService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@EqualsAndHashCode(callSuper = true)
@Data
public class LikePostServiceImpl extends ServiceImpl<LikePostMapper, LikePostItem> implements LikePostService {
    @Autowired
    private LikePostMapper likePostMapper;
    @Autowired
    private PostMapper postMapper;

    public Optional<PostItem> selectOptional(Wrapper<PostItem> queryWrapper) {
        return Optional.ofNullable(postMapper.selectOne(queryWrapper));
    }

    @Override
    public boolean save(String userId, Long postId) throws NoSuchPostException {
        LambdaQueryChainWrapper<PostItem> queryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getAuthor, userId)
                .eq(PostItem::getId, postId)
                .eq(PostItem::getIsDeleted, false);
        PostItem postItem = selectOptional(queryWrapper).orElseThrow(NoSuchPostException::new);
        postItem.setLikes(postItem.getLikes() + 1);
        return likePostMapper.insert(new LikePostItem().setPostId(postId)) > 0;
    }

    @Override
    public boolean remove(String userId, Long postId) throws NoSuchPostException {
        LambdaQueryChainWrapper<LikePostItem> queryWrapper = new QueryChainWrapper<>(likePostMapper).lambda()
                .eq(LikePostItem::getUserId, userId)
                .eq(LikePostItem::getPostId, postId)
                .eq(LikePostItem::getIsDeleted, false);
        LambdaQueryChainWrapper<PostItem> postWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getId, postId);
        PostItem postItem = selectOptional(postWrapper).orElseThrow(NoSuchPostException::new);
        postItem.setLikes(postItem.getLikes() - 1);
        return postMapper.updateById(postItem) > 0 && likePostMapper.delete(queryWrapper) > 0;
    }

    @Override
    public IPage<LikePostItem> getPage(String userId, int pageNum, int pageId) {
        LambdaQueryChainWrapper<LikePostItem> queryWrapper = new QueryChainWrapper<>(likePostMapper).lambda()
                .eq(LikePostItem::getUserId, userId)
                .eq(LikePostItem::getIsDeleted, false)
                .orderByDesc(LikePostItem::getDate);
        List<LikePostItem> likePostList = likePostMapper.selectList(queryWrapper);
        likePostList.forEach(likePostItem -> {
                LambdaQueryChainWrapper<PostItem> commentItemQueryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                        .eq(PostItem::getId, likePostItem.getPostId());
                PostItem postItem = postMapper.selectOne(commentItemQueryWrapper);
                likePostItem.setPostId(postItem.getId());
            }
        );
        IPage<LikePostItem> page = new Page<>(pageNum, pageId);
        page.setRecords(likePostList);
        return page;
    }
}

