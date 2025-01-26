package com.ziqiang.sushuodorm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<PostItem> {
    List<PostItem> listPostByPage(IPage<PostItem> page, Wrapper<PostItem> queryWrapper);

    List<PostItem> listPostWithDelete(Date minUpdateTime);
}
