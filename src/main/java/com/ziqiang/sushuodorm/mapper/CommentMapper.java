package com.ziqiang.sushuodorm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface CommentMapper extends BaseMapper<CommentItem> {
    default Optional<CommentItem> selectOptional(Wrapper<CommentItem> queryWrapper) {
        return Optional.ofNullable(selectOne(queryWrapper));
    }
}
