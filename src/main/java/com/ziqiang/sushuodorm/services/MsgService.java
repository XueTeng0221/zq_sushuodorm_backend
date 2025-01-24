package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.item.MsgItem;
import org.springframework.data.domain.Page;

public interface MsgService extends IService<MsgItem> {
    boolean saveItem(String userId, String content);

    Page<MsgItem> getPage(int currentPage, int pageSize);
}
