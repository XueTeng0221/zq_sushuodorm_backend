package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.item.MsgItem;
import com.ziqiang.sushuodorm.mapper.MsgMapper;
import com.ziqiang.sushuodorm.mapper.UserMapper;
import com.ziqiang.sushuodorm.services.MsgService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EqualsAndHashCode(callSuper = false)
@Data
public class MsgServiceImpl extends ServiceImpl<MsgMapper, MsgItem> implements MsgService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MsgMapper msgMapper;

    @Override
    public boolean saveItem(String userId, String content) {
        MsgItem msgItem = new MsgItem()
                .setAuthor(userId)
                .setContent(content);
        return msgMapper.insertOrUpdate(msgItem);
    }

    @Override
    public List<MsgItem> getMsgList(String userId) {
        LambdaQueryChainWrapper<MsgItem> queryWrapper = new QueryChainWrapper<>(msgMapper).lambda()
                .eq(MsgItem::getDeleted, false)
                .eq(MsgItem::getAuthor, userId);
        return msgMapper.selectList(queryWrapper);
    }

    @Override
    public Page<MsgItem> getPage(String userId, int currentPage, int pageSize) {
        LambdaQueryChainWrapper<MsgItem> queryWrapper = new QueryChainWrapper<>(msgMapper).lambda()
                .eq(MsgItem::getDeleted, false)
                .eq(MsgItem::getAuthor, userId);
        List<MsgItem> msgList = msgMapper.selectList(queryWrapper);
        return new Page<MsgItem>(currentPage, pageSize).setRecords(msgList);
    }
}
