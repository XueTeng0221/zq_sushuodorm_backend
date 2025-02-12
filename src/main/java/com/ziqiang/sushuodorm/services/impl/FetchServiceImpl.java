package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.dto.item.FetchQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderUpdateRequest;
import com.ziqiang.sushuodorm.entity.enums.OrderStatusEnum;
import com.ziqiang.sushuodorm.entity.item.order.FetchItem;
import com.ziqiang.sushuodorm.entity.item.order.OrderItem;
import com.ziqiang.sushuodorm.entity.vo.FetchVo;
import com.ziqiang.sushuodorm.mapper.FetchMapper;
import com.ziqiang.sushuodorm.mapper.OrderMapper;
import com.ziqiang.sushuodorm.services.FetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FetchServiceImpl extends ServiceImpl<FetchMapper, FetchItem> implements FetchService {
    @Autowired
    private FetchMapper fetchMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public boolean save(String fetchId, String userId, String roomId, String description) {
        return fetchMapper.insert(new FetchItem()
                .setFetchId(fetchId)
                .setUserId(userId)
                .setFromDormId(roomId.substring(0, roomId.indexOf("-")))
                .setDescription(description)) > 0;
    }

    @Override
    public boolean remove(String fetchId, String userId) {
        LambdaQueryChainWrapper<FetchItem> queryWrapper = new QueryChainWrapper<>(fetchMapper).lambda()
                .eq(FetchItem::getFetchId, fetchId)
                .eq(FetchItem::getUserId, userId);
        return fetchMapper.delete(queryWrapper) > 0;
    }

    @Override
    public boolean update(String userId, String fetchId, String description, OrderUpdateRequest orderUpdateRequest) {
        LambdaQueryChainWrapper<FetchItem> queryWrapper = new QueryChainWrapper<>(fetchMapper).lambda()
                .eq(FetchItem::getFetchId, fetchId)
                .eq(FetchItem::getUserId, userId);
        FetchItem fetchItem = fetchMapper.selectOne(queryWrapper);
        String toDormId = orderUpdateRequest.getStatus() == OrderStatusEnum.FINISHED ? fetchItem.getFromDormId() : null;
        return fetchMapper.update(fetchItem.setDescription(description).setToDormId(toDormId), queryWrapper) > 0;
    }

    public IPage<FetchVo> getFetchesByRoomId(String roomId, FetchQueryRequest fetchQueryRequest) {
        LambdaQueryChainWrapper<FetchItem> queryWrapper = new QueryChainWrapper<>(fetchMapper).lambda()
                .eq(FetchItem::getFromDormId, roomId)
                .eq(FetchItem::getToDormId, roomId);
        List<FetchItem> fetchItems = fetchMapper.selectList(queryWrapper);
        List<FetchVo> fetchVos = new ArrayList<>();
        fetchItems.forEach(fetchItem -> fetchVos.add(new FetchVo()
                .setFetchId(fetchItem.getFetchId())
                .setUserId(fetchItem.getUserId())
                .setStartDate(fetchItem.getStartDate())
                .setEndDate(fetchItem.getEndDate())));
        Page<FetchVo> page = new Page<>(fetchQueryRequest.getCurrentId(), fetchQueryRequest.getPageSize());
        page.setRecords(fetchVos);
        return page;
    }

    @Override
    public IPage<FetchVo> getFetchesByFromDorm(String fromDormId, FetchQueryRequest fetchQueryRequest) {
        LambdaQueryChainWrapper<FetchItem> fetchWrapper = new QueryChainWrapper<>(fetchMapper).lambda()
                .eq(FetchItem::getFromDormId, fromDormId);
        LambdaQueryChainWrapper<OrderItem> orderWrapper = new QueryChainWrapper<>(orderMapper).lambda()
                .eq(OrderItem::getFromDormId, fromDormId);
        OrderItem orderItem = orderMapper.selectOne(orderWrapper);
        if (orderItem != null) {
            fetchWrapper.ge(FetchItem::getStartDate, orderItem.getStartDate())
                    .le(FetchItem::getEndDate, orderItem.getEndDate());
        }

        List<FetchItem> fetchItems = fetchMapper.selectList(fetchWrapper);
        List<FetchVo> fetchVos = new ArrayList<>();
        fetchItems.forEach(fetchItem -> fetchVos.add(new FetchVo()
                .setFetchId(fetchItem.getFetchId())
                .setUserId(fetchItem.getUserId())
                .setStartDate(fetchItem.getStartDate())
                .setEndDate(fetchItem.getEndDate())));
        Page<FetchVo> page = new Page<>(fetchQueryRequest.getCurrentId(), fetchQueryRequest.getPageSize());
        page.setRecords(fetchVos);
        return page;
    }

    @Override
    public IPage<FetchVo> getFetchesByToDorm(String toDormId, FetchQueryRequest fetchQueryRequest) {
        LambdaQueryChainWrapper<FetchItem> fetchWrapper = new QueryChainWrapper<>(fetchMapper).lambda()
                .eq(FetchItem::getToDormId, toDormId);
        List<FetchItem> fetchItems = fetchMapper.selectList(fetchWrapper);

        List<FetchVo> fetchVos = new ArrayList<>();
        fetchItems.forEach(fetchItem -> fetchVos.add(new FetchVo()
                .setFetchId(fetchItem.getFetchId())
                .setUserId(fetchItem.getUserId())
                .setStartDate(fetchItem.getStartDate())
                .setEndDate(fetchItem.getEndDate())));
        Page<FetchVo> page = new Page<>(fetchQueryRequest.getCurrentId(), fetchQueryRequest.getPageSize());
        page.setRecords(fetchVos);
        return page;
    }

    @Override
    public List<FetchVo> getFetchesByUserId(String userId) {
        LambdaQueryChainWrapper<FetchItem> queryWrapper = new QueryChainWrapper<>(fetchMapper).lambda()
                .eq(FetchItem::getUserId, userId);
        return fetchMapper.selectList(queryWrapper).stream().map(fetchItem -> new FetchVo()
                .setFetchId(fetchItem.getFetchId())
                .setUserId(fetchItem.getUserId())
                .setStartDate(fetchItem.getStartDate())).toList();
    }
}
