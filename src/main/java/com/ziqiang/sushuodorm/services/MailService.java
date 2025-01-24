package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.item.MailItem;

public interface MailService extends IService<MailItem> {
    boolean save(String userId, String postId);

    boolean remove(String userId, String postId);

    boolean update(String userId, String postId);

    IPage<MailItem> searchById(String userId, Long mailId, int pageNum, int pageId);

    IPage<MailItem> getItem(String userId, int pageNum, int pageId);
}
