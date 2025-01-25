package com.ziqiang.sushuodorm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.entity.item.MailItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MailMapper extends BaseMapper<MailItem> {
    List<MailItem> listMailByPage(IPage<MailItem> page, Wrapper<MailItem> queryWrapper);
}
