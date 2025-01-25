package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.item.FavoriteItem;
import com.ziqiang.sushuodorm.mapper.FavoriteMapper;
import com.ziqiang.sushuodorm.services.FavoriteService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@EqualsAndHashCode(callSuper = false)
@Data
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, FavoriteItem> implements FavoriteService {
    private final FavoriteMapper favoriteMapper;

    public FavoriteServiceImpl(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    public boolean save(String userId, Long postId) {
        FavoriteItem favoriteItem = new FavoriteItem();
        favoriteItem.setUserId(Long.parseLong(userId));
        favoriteItem.setPostId(postId);
        favoriteItem.setDate(new Date());
        favoriteItem.setIsDeleted(false);
        return save(favoriteItem);
    }

    @Override
    public boolean remove(String userId, Long postId) {
        Wrapper<FavoriteItem> queryWrapper = Wrappers.<FavoriteItem>lambdaQuery()
                .eq(FavoriteItem::getUserId, Long.parseLong(userId))
                .eq(FavoriteItem::getPostId, postId);
        return remove(queryWrapper);
    }

    @Override
    public boolean update(String userId, Long postId) {
        Wrapper<FavoriteItem> queryWrapper = Wrappers.<FavoriteItem>lambdaQuery()
                .eq(FavoriteItem::getUserId, Long.parseLong(userId))
                .eq(FavoriteItem::getPostId, postId);
        FavoriteItem favoriteItem = new FavoriteItem();
        favoriteItem.setIsDeleted(false);
        return update(favoriteItem, queryWrapper);
    }

    @Override
    public IPage<FavoriteItem> getPage(String userId, int pageNum, int pageId) {
        IPage<FavoriteItem> page = new Page<>(pageNum, pageId);
        Wrapper<FavoriteItem> queryWrapper = Wrappers.<FavoriteItem>lambdaQuery()
                .eq(FavoriteItem::getUserId, Long.parseLong(userId));
        return page(page, queryWrapper);
    }

    @Override
    public IPage<FavoriteItem> searchByPostId(String userId, Long postId, int pageNum, int pageId) {
        IPage<FavoriteItem> page = new Page<>(pageNum, pageId);
        Wrapper<FavoriteItem> queryWrapper = Wrappers.<FavoriteItem>lambdaQuery()
                .eq(FavoriteItem::getUserId, Long.parseLong(userId))
                .eq(FavoriteItem::getPostId, postId);
        return page(page, queryWrapper);
    }
}