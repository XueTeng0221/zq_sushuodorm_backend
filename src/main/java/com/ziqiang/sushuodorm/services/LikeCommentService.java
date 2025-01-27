package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.item.LikeCommentItem;
import com.ziqiang.sushuodorm.entity.item.LikePostItem;

public interface LikeCommentService extends IService<LikeCommentItem> {
    boolean save(String userId, Long commentId);

    boolean remove(String userId, Long commentId);

    IPage<LikeCommentItem> getPage(String userId, int pageNum, int pageId);
}
