package com.ziqiang.sushuodorm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface PostMapper extends BaseMapper<PostItem> {
   default Optional<PostItem> selectOptional(Wrapper<PostItem> queryWrapper) {
       return Optional.ofNullable(selectOne(queryWrapper));
   }
}
