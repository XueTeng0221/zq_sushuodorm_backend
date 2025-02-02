package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.dto.item.FetchQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderUpdateRequest;
import com.ziqiang.sushuodorm.entity.item.order.FetchItem;
import com.ziqiang.sushuodorm.entity.vo.FetchVo;

import java.util.List;

public interface FetchService extends IService<FetchItem> {
    boolean save(String fetchId, String userId, String roomId, String description);

    boolean remove(String fetchId, String userId);

    boolean update(String userId, String fetchId, String description, OrderUpdateRequest orderUpdateRequest);

    List<FetchVo> getFetchesByUserId(String userId);

    IPage<FetchVo> getFetchesByRoomId(String roomId, FetchQueryRequest fetchQueryRequest);

    IPage<FetchVo> getFetchesByFromDorm(String fromDormId, FetchQueryRequest fetchQueryRequest);

    IPage<FetchVo> getFetchesByToDorm(String toDormId, FetchQueryRequest fetchQueryRequest);
}
