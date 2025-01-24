package com.ziqiang.sushuodorm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ziqiang.sushuodorm.entity.item.PostItem;

import java.sql.Date;
import java.util.List;

public interface PostMapper extends BaseMapper<PostItem> {
    List<PostItem> listPostWithDelete(Date minUpdateTime);
}
