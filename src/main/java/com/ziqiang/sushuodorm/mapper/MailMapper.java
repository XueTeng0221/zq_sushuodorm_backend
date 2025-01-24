package com.ziqiang.sushuodorm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.entity.item.MailItem;

import java.util.List;

public interface MailMapper extends BaseMapper<MailItem> {
    List<MailItem> listMailByPage(IPage<MailItem> page, Wrapper<MailItem> queryWrapper);
}
