package com.ziqiang.sushuodorm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ziqiang.sushuodorm.entity.item.FavoriteItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteMapper extends BaseMapper<FavoriteItem> {
}
