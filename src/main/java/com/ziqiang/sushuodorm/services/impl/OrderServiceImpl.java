package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.dto.item.FetchQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderUpdateRequest;
import com.ziqiang.sushuodorm.entity.item.RoomItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        OrderItem orderItem = new OrderItem()
                .setOrderId(orderId)
                .setUserId(userId)
                .setTitle(title)
                .setFromDormId(roomId.substring(0, roomId.indexOf("-")))
                .setToDormId(toDormId)
                .setDescription(description);
        return orderMapper.insertOrUpdate(orderItem);
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
        OrderItem orderItem = orderMapper.selectOne(queryWrapper);
        FetchItem fetchItem = new FetchItem()
                .setFetchId(orderId)
                .setUserId(userId)
                .setStartDate(orderUpdateRequest.getStartDate())
                .setEndDate(orderUpdateRequest.getEndDate());
        return orderMapper.insertOrUpdate(orderItem) && fetchMapper.insertOrUpdate(fetchItem);
    }

    public IPage<OrderVo> getOrdersByFromDorm(String fromDormId, OrderQueryRequest orderQueryRequest) {
        LambdaQueryChainWrapper<OrderItem> orderWrapper = new QueryChainWrapper<>(orderMapper).lambda()
                .eq(OrderItem::getFromDormId, fromDormId)
                .orderByDesc(OrderItem::getStartDate);
        List<OrderItem> orderItems = orderMapper.selectList(orderWrapper);

        LambdaQueryChainWrapper<RoomItem> roomWrapper = new QueryChainWrapper<>(roomMapper).lambda()
                .eq(RoomItem::getRoomId, userMapper.selectById(orderItems.get(0).getUserId()).getRoomId());
        RoomItem roomItem = roomMapper.selectOne(roomWrapper);

        Map<String, UserItem> occupants = roomItem.getOccupants();
        Map<String, String> fromDormToDormMap = new HashMap<>();
        orderItems.forEach(orderItem -> fromDormToDormMap.put(orderItem.getFromDormId(), orderItem.getToDormId()));
        List<OrderVo> orderVos = new ArrayList<>();
        orderItems.forEach(orderItem -> orderVos.add(new OrderVo()
                .setUserId(occupants.get(orderItem.getUserId()).getUserName())
                .setFromDormId(orderItem.getFromDormId())
                .setToDormId(fromDormToDormMap.get(orderItem.getToDormId()))
                .setStartDate(orderItem.getStartDate())
        ));
        Page<OrderVo> page = new Page<>(orderQueryRequest.getCurrentId(), orderQueryRequest.getPageSize());
        page.setRecords(orderVos);
        return page;
    }

    public IPage<OrderVo> getOrdersByToDorm(String toDormId, OrderQueryRequest orderQueryRequest) {
        LambdaQueryChainWrapper<OrderItem> queryWrapper = new QueryChainWrapper<>(orderMapper).lambda()
                .eq(OrderItem::getToDormId, toDormId)
                .orderByDesc(OrderItem::getStartDate);
        List<OrderItem> orderItems = orderMapper.selectList(queryWrapper);

        LambdaQueryChainWrapper<RoomItem> roomWrapper = new QueryChainWrapper<>(roomMapper).lambda()
                .in(RoomItem::getRoomId, new ArrayList<>(orderItems.stream().map(OrderItem::getFromDormId).toList()));
        List<RoomItem> roomItems = roomMapper.selectList(roomWrapper);

        Map<String, String> orderIdUserIdMap = new HashMap<>();
        Map<String, RoomItem> orderIdFromDormMap = new HashMap<>();
        orderItems.forEach(orderItem -> orderIdUserIdMap.put(orderItem.getOrderId(), orderItem.getUserId()));
        roomItems.forEach(roomItem -> orderIdFromDormMap.put(roomItem.getRoomName(), roomItem));
        List<OrderVo> orderVos = new ArrayList<>();
        orderItems.forEach(orderItem -> orderVos.add(new OrderVo()
                .setUserId(orderIdUserIdMap.get(orderItem.getOrderId()))
                .setFromDormId(orderIdFromDormMap.get(orderItem.getFromDormId()).getDormName())
                .setToDormId(toDormId)
                .setStartDate(orderItem.getStartDate())
                .setEndDate(orderItem.getEndDate())
        ));
        Page<OrderVo> page = new Page<>(orderQueryRequest.getCurrentId(), orderQueryRequest.getPageSize());
        page.setRecords(orderVos);
        return page;
    }

    public IPage<OrderVo> getOrdersByKeywords(List<String> keywords, OrderQueryRequest orderQueryRequest) {
        LambdaQueryChainWrapper<OrderItem> queryWrapper = new QueryChainWrapper<>(orderMapper).lambda();
        keywords.forEach(keyword -> queryWrapper.or().like(OrderItem::getTitle, keyword)
                .or().like(OrderItem::getDescription, keyword));
        List<OrderItem> orderItems = orderMapper.selectList(queryWrapper);
        List<OrderVo> orderVos = new ArrayList<>();
        orderItems.forEach(orderItem -> orderVos.add(new OrderVo()
                .setUserId(orderItem.getUserId())
                .setFromDormId(orderItem.getFromDormId())
                .setToDormId(orderItem.getToDormId())
                .setStartDate(orderItem.getStartDate())
                .setEndDate(orderItem.getEndDate())
        ));
        Page<OrderVo> page = new Page<>(orderQueryRequest.getCurrentId(), orderQueryRequest.getPageSize());
        page.setRecords(orderVos);
        return page;
    }

    public IPage<OrderVo> getOrdersByOccupants(List<String> occupants, OrderQueryRequest orderQueryRequest) {
        LambdaQueryChainWrapper<OrderItem> queryWrapper = new QueryChainWrapper<>(orderMapper).lambda();
        LambdaQueryChainWrapper<UserItem> userWrapper = new QueryChainWrapper<>(userMapper).lambda();
        occupants.forEach(occupant -> {
            queryWrapper.or().eq(OrderItem::getUserId, occupant);
            userWrapper.or().eq(UserItem::getUserName, occupant);
        });
        List<OrderItem> orderItems = orderMapper.selectList(queryWrapper);
        List<UserItem> userItems = userMapper.selectList(userWrapper);

        LambdaQueryChainWrapper<RoomItem> roomWrapper = new QueryChainWrapper<>(roomMapper).lambda()
                .in(RoomItem::getRoomId, new ArrayList<>(userItems.stream().map(UserItem::getRoomId).toList()));
        List<RoomItem> roomItems = roomMapper.selectList(roomWrapper);
        Map<String, UserItem> mergedOccupantMap = new HashMap<>();
        roomItems.forEach(roomItem -> {
            Map<String, UserItem> occupantMap = roomItem.getOccupants();
            mergedOccupantMap.putAll(occupantMap);
        });
        Map<String, String> occupantNameRoomIdMap = new HashMap<>();
        mergedOccupantMap.forEach((key, value) -> occupantNameRoomIdMap.put(key, mergedOccupantMap.get(key).getRoomId()));
        orderItems.addAll(new QueryChainWrapper<>(orderMapper).lambda().in(OrderItem::getToDormId, occupantNameRoomIdMap.values()).list());

        List<OrderVo> orderVos = new ArrayList<>();
        orderItems.forEach(orderItem -> orderVos.add(new OrderVo()
                .setUserId(mergedOccupantMap.get(orderItem.getUserId()).getUserName())
                .setFromDormId(orderItem.getFromDormId())
                .setToDormId(orderItem.getToDormId())
                .setStartDate(orderItem.getStartDate())
                .setEndDate(orderItem.getEndDate())
        ));
        Page<OrderVo> page = new Page<>(orderQueryRequest.getCurrentId(), orderQueryRequest.getPageSize());
        page.setRecords(orderVos);
        return page;
    }

    public IPage<FetchVo> getFetchesByOrderId(String orderId, FetchQueryRequest fetchQueryRequest) {
        LambdaQueryChainWrapper<OrderItem> orderWrapper = new QueryChainWrapper<>(orderMapper).lambda()
                .eq(OrderItem::getOrderId, orderId)
                .orderByDesc(OrderItem::getStartDate);
        OrderItem orderItem = orderMapper.selectOne(orderWrapper);

        LambdaQueryChainWrapper<FetchItem> fetchWrapper = new QueryChainWrapper<>(fetchMapper).lambda()
                .eq(FetchItem::getFetchId, orderId);
        List<FetchItem> fetchItems = fetchMapper.selectList(fetchWrapper);
        Map<String, FetchItem> fetchMap = new HashMap<>();
        fetchItems.forEach(fetchItem -> fetchMap.put(fetchItem.getFetchId(), fetchItem));
        fetchMap.forEach((key, value) ->
                fetchWrapper.ge(FetchItem::getStartDate, value.getStartDate())
                        .le(FetchItem::getEndDate, value.getEndDate()));

        List<FetchVo> fetchVos = new ArrayList<>();
        fetchItems.forEach(fetchItem -> fetchVos.add(new FetchVo()
                .setFetchId(fetchItem.getFetchId())
                .setUserId(fetchItem.getUserId())
                .setStartDate(fetchMap.get(orderItem.getOrderId()).getStartDate())
                .setEndDate(fetchMap.get(orderItem.getOrderId()).getEndDate())));
        Page<FetchVo> page = new Page<>(fetchQueryRequest.getCurrentId(), fetchQueryRequest.getPageSize());
        page.setRecords(fetchVos);
        return page;
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
