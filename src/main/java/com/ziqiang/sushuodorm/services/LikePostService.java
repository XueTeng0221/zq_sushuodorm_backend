package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.item.LikePostItem;

public interface LikePostService extends IService<LikePostItem> {
    boolean save(String userId, Long postId);

    boolean update(String userId, Long postId);

    boolean remove(String userId, Long postId);

    IPage<LikePostItem> getPage(String userId, int pageNum, int pageId);
}
