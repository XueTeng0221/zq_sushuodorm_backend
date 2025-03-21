package com.ziqiang.sushuodorm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ziqiang.sushuodorm.entity.item.order.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderItem> {
}
