package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.item.FavoriteItem;

public interface FavoriteService extends IService<FavoriteItem> {
    boolean save(String userId, Long postId);

    boolean remove(String userId, Long postId);

    boolean update(String userId, Long postId);

    IPage<FavoriteItem> getPage(String userId, int pageNum, int pageId);

    IPage<FavoriteItem> searchByPostId(String userId, Long postId, int pageNum, int pageId);
}
