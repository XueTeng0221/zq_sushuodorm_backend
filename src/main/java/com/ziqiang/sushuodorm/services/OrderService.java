package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.dto.item.FetchQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderUpdateRequest;
import com.ziqiang.sushuodorm.entity.item.order.OrderItem;
import com.ziqiang.sushuodorm.entity.vo.FetchVo;
import com.ziqiang.sushuodorm.entity.vo.OrderVo;

import java.util.List;

public interface OrderService extends IService<OrderItem> {
    boolean save(String orderId, String userId, String roomId, String toDormId, String title, String description);

    boolean remove(String orderId, String userId);

    boolean update(String userId, String orderId, String title, String description, OrderUpdateRequest orderUpdateRequest);

    List<OrderVo> getOrdersByUserId(String userId);

    IPage<OrderVo> getOrdersByRoomId(String roomId, OrderQueryRequest orderQueryRequest);

    IPage<OrderVo> getOrdersByFromDorm(String fromDormId, OrderQueryRequest orderQueryRequest);

    IPage<OrderVo> getOrdersByToDorm(String toDormId, OrderQueryRequest orderQueryRequest);

    IPage<OrderVo> getOrdersByKeywords(List<String> keywords, OrderQueryRequest orderQueryRequest);

    IPage<OrderVo> getOrdersByOccupants(List<String> occupants, OrderQueryRequest orderQueryRequest);

    IPage<FetchVo> getFetchesByOrderId(String orderId, FetchQueryRequest fetchQueryRequest);
}
