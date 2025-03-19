package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.item.FavoriteItem;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import com.ziqiang.sushuodorm.exception.NoSuchPostException;
import com.ziqiang.sushuodorm.mapper.FavoriteMapper;
import com.ziqiang.sushuodorm.mapper.PostMapper;
import com.ziqiang.sushuodorm.services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, FavoriteItem> implements FavoriteService {
    @Autowired
    private FavoriteMapper favoriteMapper;
    @Autowired
    private PostMapper postMapper;

    @Override
    public boolean save(String userId, Long postId) {
        FavoriteItem favoriteItem = new FavoriteItem()
                .setUserId(Long.parseLong(userId))
                .setPostId(postId)
                .setIsDeleted(false);
        return favoriteMapper.insertOrUpdate(favoriteItem);
    }

    public Optional<PostItem> selectOptional(Wrapper<PostItem> queryWrapper) {
        return Optional.ofNullable(postMapper.selectOne(queryWrapper));
    }

    @Override
    public boolean remove(String userId, Long postId) {
        LambdaQueryChainWrapper<FavoriteItem> queryWrapper = new QueryChainWrapper<>(favoriteMapper).lambda()
                .eq(FavoriteItem::getUserId, Long.parseLong(userId))
                .eq(FavoriteItem::getPostId, postId);
        return favoriteMapper.delete(queryWrapper) > 0;
    }

    @Override
    public boolean update(String userId, Long postId) throws NoSuchPostException {
        LambdaQueryChainWrapper<PostItem> postWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getId, postId);
        PostItem postItem = selectOptional(postWrapper).orElseThrow(NoSuchPostException::new);

        LambdaQueryChainWrapper<FavoriteItem> queryWrapper = new QueryChainWrapper<>(favoriteMapper).lambda()
                .eq(FavoriteItem::getUserId, Long.parseLong(userId))
                .eq(FavoriteItem::getPostId, postItem.getId());
        FavoriteItem favoriteItem = favoriteMapper.selectOne(queryWrapper)
                .setUserId(Long.parseLong(userId))
                .setPostId(postId)
                .setIsDeleted(false);
        return favoriteMapper.insertOrUpdate(favoriteItem);
    }

    @Override
    public IPage<FavoriteItem> getPage(String userId, int pageNum, int pageId) {
        LambdaQueryChainWrapper<FavoriteItem> queryWrapper = new QueryChainWrapper<>(favoriteMapper).lambda()
                .eq(FavoriteItem::getUserId, Long.parseLong(userId))
                .eq(FavoriteItem::getIsDeleted, false);
        return favoriteMapper.selectPage(new Page<>(pageNum, pageId), queryWrapper);
    }

    @Override
    public IPage<FavoriteItem> searchByPostId(String userId, Long postId, int pageNum, int pageId) throws NoSuchPostException {
        LambdaQueryChainWrapper<PostItem> postWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getId, postId);
        PostItem postItem = selectOptional(postWrapper).orElseThrow(NoSuchPostException::new);

        LambdaQueryChainWrapper<FavoriteItem> queryWrapper = new QueryChainWrapper<>(favoriteMapper).lambda()
                .eq(FavoriteItem::getUserId, Long.parseLong(userId))
                .eq(FavoriteItem::getPostId, postItem.getId())
                .eq(FavoriteItem::getIsDeleted, false);
        return favoriteMapper.selectPage(new Page<>(pageNum, pageId), queryWrapper);
    }
}