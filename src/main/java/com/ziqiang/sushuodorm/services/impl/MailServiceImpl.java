package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.dto.mail.MailQueryRequest;
import com.ziqiang.sushuodorm.entity.item.MailItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.entity.vo.UserVo;
import com.ziqiang.sushuodorm.mapper.MailMapper;
import com.ziqiang.sushuodorm.mapper.UserMapper;
import com.ziqiang.sushuodorm.services.MailService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@EqualsAndHashCode(callSuper = false)
@Data
public class MailServiceImpl extends ServiceImpl<MailMapper, MailItem> implements MailService {
    private MailMapper mailMapper;
    private UserMapper userMapper;

    public MailServiceImpl(MailMapper mailMapper, UserMapper userMapper) {
        this.mailMapper = mailMapper;
        this.userMapper = userMapper;
    }

    @Override
    public boolean save(String userId, Map<String, UserItem> receivers) {
        MailItem mailItem = new MailItem()
                .setUserId(Long.parseLong(userId))
                .setIsDeleted(false)
                .setIsRead(false)
                .setReceivers(receivers);
        return mailMapper.insertOrUpdate(mailItem);
    }

    @Override
    public boolean remove(String mailId) {
        LambdaQueryChainWrapper<MailItem> queryWrapper = new QueryChainWrapper<>(mailMapper).lambda()
                .eq(MailItem::getId, Long.parseLong(mailId));
        return mailMapper.delete(queryWrapper) > 0;
    }

    @Override
    public boolean reply(String userId, List<UserItem> receivers) {
        LambdaQueryChainWrapper<MailItem> queryWrapper = new QueryChainWrapper<>(mailMapper).lambda();
        Map<String, UserItem> userItemMap = new HashMap<>();
        receivers.forEach(receiver -> userItemMap.put(receiver.getUserName(), receiver));
        queryWrapper.in(MailItem::getReceivers, userItemMap);
        return CollectionUtils.isEmpty(receivers) && mailMapper.selectList(queryWrapper).stream().map(mailItem ->
                mailItem.setIsReplied(true).setTitle("Re: " + mailItem.getTitle())
        ).allMatch(mailItem -> mailMapper.updateById(mailItem) > 0);
    }

    @Override
    public boolean update(String postId, String title, String subject) {
        LambdaQueryChainWrapper<MailItem> queryWrapper = new QueryChainWrapper<>(mailMapper).lambda()
                .eq(MailItem::getId, Long.parseLong(postId));
        MailItem mailItem = new MailItem().setUserId(Long.parseLong(postId))
                .setTitle(title)
                .setSubject(subject)
                .setIsDeleted(false);
        return update(mailItem, queryWrapper);
    }

    @Override
    public IPage<MailItem> getItemByUsername(String username, MailQueryRequest queryRequest) {
        LambdaQueryChainWrapper<MailItem> queryWrapper = new QueryChainWrapper<>(mailMapper).lambda()
                .eq(MailItem::getSenderName, username)
                .eq(MailItem::getIsDeleted, false);
        return mailMapper.selectPage(queryRequest.getPage(), queryWrapper).setRecords(
                new ArrayList<>(queryWrapper.list()));
    }

    @Override
    public IPage<UserVo> getReceiverByMailId(String mailId, int currentSize, int pageSize) {
        LambdaQueryChainWrapper<MailItem> queryWrapper = new QueryChainWrapper<>(mailMapper).lambda()
                .eq(MailItem::getId, Long.parseLong(mailId));
        MailItem mailItem = mailMapper.selectOne(queryWrapper);
        List<UserVo> userVos = new ArrayList<>();
        mailItem.getReceivers().forEach((receiverName, userItem) -> {
            userVos.add(new UserVo()
                    .setUserName(receiverName)
                    .setUserAvatar(userItem.getUserAvatar())
                    .setRoomId(userItem.getRoomId()));
        });
        Page<UserVo> page = new Page<>(currentSize, pageSize);
        page.setTotal(mailItem.getReceivers().size()).setRecords(userVos);
        return page;
    }

    @Override
    public IPage<UserVo> getReceiverByUsername(String username, int currentSize, int pageSize) {
        LambdaQueryChainWrapper<MailItem> queryWrapper = new QueryChainWrapper<>(mailMapper).lambda()
                .eq(MailItem::getSenderName, username);
        List<MailItem> mailItems = mailMapper.selectList(queryWrapper);
        Map<String, Set<UserItem>> receiverMap = new HashMap<>();
        mailItems.forEach(mailItem ->
            mailItem.getReceivers().keySet().forEach(receiverName ->
                receiverMap.putIfAbsent(receiverName, new HashSet<>())
        ));
        List<UserVo> userVos = new ArrayList<>();
        receiverMap.keySet().forEach(receiverName -> {
            Set<UserItem> userItems = receiverMap.get(receiverName);
            userVos.addAll(userItems.stream().map(userItem -> new UserVo()
                    .setUserName(userItem.getUserName())
                    .setUserAvatar(userItem.getUserAvatar())
                    .setRoomId(userItem.getRoomId())).toList());
        });
        Page<UserVo> page = new Page<>(currentSize, pageSize);
        page.setRecords(userVos);
        page.setTotal(receiverMap.size());
        return page;
    }
}
