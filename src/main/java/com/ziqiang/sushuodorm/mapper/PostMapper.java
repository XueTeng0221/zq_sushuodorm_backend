package com.ziqiang.sushuodorm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseMapper<PostItem> {
}
