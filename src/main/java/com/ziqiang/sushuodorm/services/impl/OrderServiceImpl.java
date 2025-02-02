package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.dto.item.FetchQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderUpdateRequest;
import com.ziqiang.sushuodorm.entity.enums.OrderStatusEnum;
import com.ziqiang.sushuodorm.entity.item.RoomItem;
import com.ziqiang.sushuodorm.entity.item.order.FetchItem;
import com.ziqiang.sushuodorm.entity.item.order.OrderItem;
import com.ziqiang.sushuodorm.entity.vo.FetchVo;
import com.ziqiang.sushuodorm.entity.vo.OrderVo;
import com.ziqiang.sushuodorm.mapper.FetchMapper;
import com.ziqiang.sushuodorm.mapper.OrderMapper;
import com.ziqiang.sushuodorm.mapper.RoomMapper;
import com.ziqiang.sushuodorm.mapper.UserMapper;
import com.ziqiang.sushuodorm.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderItem> implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FetchMapper fetchMapper;

    @Override
    public boolean save(String orderId, String userId, String roomId, String toDormId, String title, String description) {
        return orderMapper.insert(new OrderItem()
                .setOrderId(orderId)
                .setUserId(userId)
                .setTitle(title)
                .setFromDormId(roomId.substring(0, roomId.indexOf("-")))
                .setToDormId(toDormId)
                .setDescription(description)) > 0;
    }

    @Override
    public boolean remove(String orderId, String userId) {
        LambdaQueryChainWrapper<OrderItem> queryWrapper = new QueryChainWrapper<>(orderMapper).lambda()
                .eq(OrderItem::getOrderId, orderId)
                .eq(OrderItem::getUserId, userId);
        return orderMapper.delete(queryWrapper) > 0;
    }

    @Override
    public boolean update(String userId, String orderId, String title, String description, OrderUpdateRequest orderUpdateRequest) {
        LambdaQueryChainWrapper<OrderItem> queryWrapper = new QueryChainWrapper<>(orderMapper).lambda()
                .eq(OrderItem::getOrderId, orderId)
                .eq(OrderItem::getUserId, userId);
        fetchMapper.insert(new FetchItem()
                .setFetchId(orderId)
                .setUserId(userId)
                .setToDormId(orderUpdateRequest.getStatus() == OrderStatusEnum.FINISHED ?
                        orderUpdateRequest.getToDormId() : null));
        return orderMapper.update(new OrderItem()
                .setTitle(title)
                .setDescription(description)
                .setStatus(orderUpdateRequest.getStatus()), queryWrapper) > 0;
    }

    public IPage<OrderVo> getOrdersByRoomId(String userId, OrderQueryRequest orderQueryRequest) {
        LambdaQueryChainWrapper<OrderItem> orderWrapper = new QueryChainWrapper<>(orderMapper).lambda()
                .eq(OrderItem::getUserId, userId)
                .orderByDesc(OrderItem::getStartDate);
        Map<String, RoomItem> occupantRoomMap = roomMapper.selectList(new QueryChainWrapper<>(roomMapper).lambda()
                .eq(RoomItem::getRoomId, userMapper.selectById(userId).getRoomId())).stream().collect(
                Collectors.toMap(RoomItem::getRoomName, roomItem -> roomItem, (a, b) -> a, HashMap::new));
        LambdaQueryChainWrapper<RoomItem> roomWrapper = new QueryChainWrapper<>(roomMapper).lambda()
                .in(RoomItem::getRoomId, occupantRoomMap.keySet());
        List<RoomItem> roomItems = roomMapper.selectList(roomWrapper);
        occupantRoomMap.forEach((key, value) -> occupantRoomMap.put(key, roomItems.stream().filter(roomItem -> roomItem.getRoomId()
                .equals(value.getRoomId())).findFirst().orElse(null)));
        return orderMapper.selectPage(new Page<>(orderQueryRequest.getCurrentId(), orderQueryRequest.getPageSize()), orderWrapper)
                .convert(orderItem -> new OrderVo()
                        .setUserId(orderItem.getUserId())
                        .setFromDormId(occupantRoomMap.get(orderItem.getFromDormId()).getDormName())
                        .setToDormId(occupantRoomMap.get(orderItem.getToDormId()).getDormName())
                        .setStartDate(orderItem.getStartDate())
                );
    }

    public IPage<OrderVo> getOrdersByFromDorm(String fromDormId, OrderQueryRequest orderQueryRequest) {
        LambdaQueryChainWrapper<OrderItem> queryWrapper = new QueryChainWrapper<>(orderMapper).lambda()
                .eq(OrderItem::getFromDormId, fromDormId)
                .orderByDesc(OrderItem::getStartDate);
        List<OrderItem> orderItems = orderMapper.selectList(queryWrapper);
        Map<String, RoomItem> occupantRoomMap = roomMapper.selectList(new QueryChainWrapper<>(roomMapper).lambda()
                .eq(RoomItem::getRoomId, userMapper.selectById(orderItems.get(0).getUserId()).getRoomId())).stream().collect(
                Collectors.toMap(RoomItem::getRoomName, roomItem -> roomItem, (a, b) -> a, HashMap::new));
        return orderMapper.selectPage(new Page<>(orderQueryRequest.getCurrentId(), orderQueryRequest.getPageSize()), queryWrapper)
                .convert(orderItem -> new OrderVo()
                        .setUserId(orderItem.getUserId())
                        .setFromDormId(occupantRoomMap.get(orderItem.getFromDormId()).getDormName())
                        .setToDormId(occupantRoomMap.get(orderItem.getToDormId()).getDormName())
                        .setStartDate(orderItem.getStartDate())
                );
    }

    public IPage<OrderVo> getOrdersByToDorm(String toDormId, OrderQueryRequest orderQueryRequest) {
        LambdaQueryChainWrapper<OrderItem> queryWrapper = new QueryChainWrapper<>(orderMapper).lambda()
                .eq(OrderItem::getToDormId, toDormId)
                .orderByDesc(OrderItem::getStartDate);
        Map<String, RoomItem> occupantRoomMap = roomMapper.selectList(new QueryChainWrapper<>(roomMapper).lambda()
                .like(RoomItem::getRoomId, toDormId)).stream().collect(
                        Collectors.toMap(RoomItem::getRoomName, roomItem -> roomItem, (a, b) -> a, HashMap::new));
        return orderMapper.selectPage(new Page<>(orderQueryRequest.getCurrentId(), orderQueryRequest.getPageSize()), queryWrapper)
                .convert(orderItem -> new OrderVo()
                        .setUserId(orderItem.getUserId())
                        .setFromDormId(occupantRoomMap.get(orderItem.getFromDormId()).getDormName())
                        .setToDormId(occupantRoomMap.get(orderItem.getToDormId()).getDormName())
                        .setStartDate(orderItem.getStartDate())
                );
    }

    public IPage<OrderVo> getOrdersByKeywords(List<String> keywords, OrderQueryRequest orderQueryRequest) {
        LambdaQueryChainWrapper<OrderItem> queryWrapper = new QueryChainWrapper<>(orderMapper).lambda();
        keywords.forEach(keyword -> queryWrapper.or().like(OrderItem::getTitle, keyword)
                .or().like(OrderItem::getDescription, keyword));
        return orderMapper.selectPage(new Page<>(orderQueryRequest.getCurrentId(), orderQueryRequest.getPageSize()), queryWrapper)
                .convert(orderItem -> new OrderVo()
                        .setUserId(orderItem.getUserId())
                        .setFromDormId(orderItem.getFromDormId())
                        .setToDormId(orderItem.getToDormId())
                        .setStartDate(orderItem.getStartDate())
                );
    }

    public IPage<OrderVo> getOrdersByOccupants(List<String> occupants, OrderQueryRequest orderQueryRequest) {
        LambdaQueryChainWrapper<OrderItem> queryWrapper = new QueryChainWrapper<>(orderMapper).lambda();
        Map<String, RoomItem> occupantRoomMap = roomMapper.selectList(new QueryChainWrapper<>(roomMapper).lambda()
                .eq(RoomItem::getRoomId, userMapper.selectById(occupants.get(0)).getRoomId())).stream().collect(
                Collectors.toMap(RoomItem::getRoomName, roomItem -> roomItem, (a, b) -> a, HashMap::new));
        occupants.forEach(occupant -> queryWrapper.or().eq(OrderItem::getUserId, occupant)
                .or().eq(OrderItem::getFromDormId, occupantRoomMap.get(occupant).getRoomId())
                .or().eq(OrderItem::getToDormId, occupantRoomMap.get(occupant).getRoomId()));
        return orderMapper.selectPage(new Page<>(orderQueryRequest.getCurrentId(), orderQueryRequest.getPageSize()), queryWrapper)
                .convert(orderItem -> new OrderVo()
                        .setUserId(orderItem.getUserId())
                        .setFromDormId(occupantRoomMap.get(orderItem.getFromDormId()).getDormName())
                        .setToDormId(occupantRoomMap.get(orderItem.getToDormId()).getDormName())
                        .setStartDate(orderItem.getStartDate())
                );
    }

    public IPage<FetchVo> getFetchesByOrderId(String orderId, FetchQueryRequest fetchQueryRequest) {
        LambdaQueryChainWrapper<OrderItem> orderWrapper = new QueryChainWrapper<>(orderMapper).lambda()
                .eq(OrderItem::getOrderId, orderId)
                .orderByDesc(OrderItem::getStartDate);
        OrderItem orderItem = orderMapper.selectOne(orderWrapper);
        LambdaQueryChainWrapper<FetchItem> fetchWrapper = new QueryChainWrapper<>(fetchMapper).lambda()
                .eq(FetchItem::getFetchId, orderId);
        Map<String, FetchItem> fetchMap = fetchMapper.selectList(fetchWrapper).stream().collect(
                Collectors.toMap(FetchItem::getFetchId, fetchItem -> fetchItem, (a, b) -> a, HashMap::new));
        fetchMap.forEach((key, value) -> {
            fetchWrapper.ge(FetchItem::getStartDate, value.getStartDate())
                    .le(FetchItem::getEndDate, value.getEndDate());
        });
        return fetchMapper.selectPage(new Page<>(fetchQueryRequest.getCurrentId(), fetchQueryRequest.getPageSize()), fetchWrapper)
                .convert(fetchItem -> new FetchVo()
                        .setFetchId(fetchItem.getFetchId())
                        .setUserId(fetchItem.getUserId())
                        .setStartDate(fetchMap.get(orderItem.getOrderId()).getStartDate())
                        .setEndDate(fetchMap.get(orderItem.getOrderId()).getEndDate()));
     }

    public List<OrderVo> getOrdersByUserId(String userId) {
        LambdaQueryChainWrapper<OrderItem> queryWrapper = new QueryChainWrapper<>(orderMapper).lambda()
                .eq(OrderItem::getUserId, userId)
                .orderByDesc(OrderItem::getStartDate);
        return orderMapper.selectList(queryWrapper).stream().map(orderItem -> new OrderVo()
                .setUserId(orderItem.getUserId())
                .setFromDormId(orderItem.getFromDormId())
                .setToDormId(orderItem.getToDormId())
                .setStartDate(orderItem.getStartDate())
        ).toList();
    }
}
