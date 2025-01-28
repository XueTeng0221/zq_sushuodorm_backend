package com.ziqiang.sushuodorm.services.impl;

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
    private UserMapper userMapper;
    private MsgMapper msgMapper;

    @Autowired
    public MsgServiceImpl(UserMapper userMapper, MsgMapper msgMapper) {
        this.userMapper = userMapper;
        this.msgMapper = msgMapper;
    }

    @Override
    public boolean saveItem(String userId, String content) {
        MsgItem msgItem = new MsgItem()
                .setAuthor(userId)
                .setContent(content);
        return msgMapper.insertOrUpdate(msgItem);
    }

    @Override
    public List<MsgItem> getMsgList(String userId) {
        QueryChainWrapper<MsgItem> queryWrapper = new QueryChainWrapper<>(msgMapper)
                .eq("is_deleted", false)
                .eq("user_id", userId);
        return msgMapper.selectList(queryWrapper);
    }

    @Override
    public Page<MsgItem> getPage(String userId, int currentPage, int pageSize) {
        QueryChainWrapper<MsgItem> queryWrapper = new QueryChainWrapper<>(msgMapper)
                .eq("is_deleted", false)
                .eq("user_id", userId);
        List<MsgItem> msgList = msgMapper.selectList(queryWrapper);
        return new Page<MsgItem>(currentPage, pageSize).setRecords(msgList);
    }
}
