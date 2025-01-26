package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.item.MsgItem;

import java.util.List;

public interface MsgService extends IService<MsgItem> {
    boolean saveItem(String userId, String content);

    List<MsgItem> getMsgList(String userId);

    Page<MsgItem> getPage(String userId, int currentPage, int pageSize);
}
