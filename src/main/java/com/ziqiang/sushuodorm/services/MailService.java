package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.dto.mail.MailQueryRequest;
import com.ziqiang.sushuodorm.entity.item.MailItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.entity.vo.UserVo;

import java.util.Map;

public interface MailService extends IService<MailItem> {
    boolean save(String userId, Map<String, UserItem> receivers);

    boolean remove(String userId);

    boolean update(String userId, String postId);

    IPage<MailItem> getItemByUsername(String username, MailQueryRequest queryRequest);

    IPage<UserVo> getReceiverByUsername(String username, int currentSize, int pageSize);
}
