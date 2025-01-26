package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        mailMapper.insert(mailItem);
        return save(mailItem);
    }

    @Override
    public boolean remove(String mailId) {
        QueryWrapper<MailItem> queryWrapper = new QueryWrapper<MailItem>().eq("mailId", Long.parseLong(mailId));
        mailMapper.delete(queryWrapper);
        return remove(queryWrapper);
    }

    @Override
    public boolean reply(String userId, List<UserItem> receivers) {
        QueryChainWrapper<MailItem> queryWrapper = new QueryChainWrapper<>(mailMapper)
                .in("id", receivers.stream().map(UserItem::getUserName).collect(Collectors.toList()));
        return CollectionUtils.isEmpty(receivers) && queryWrapper.list().stream()
                .map(mailItem -> mailItem
                .setIsReplied(true).setTitle("Re: ")
        ).allMatch(mailItem -> mailMapper.updateById(mailItem) > 0);
    }

    @Override
    public boolean update(String postId, String title, String subject) {
        QueryChainWrapper<MailItem> queryWrapper = new QueryChainWrapper<>(mailMapper)
                .eq("postId", Long.parseLong(postId));
        MailItem mailItem = new MailItem().setUserId(Long.parseLong(postId))
                .setTitle(title)
                .setSubject(subject);
        mailItem.setIsDeleted(false);
        return update(mailItem, queryWrapper);
    }

    @Override
    public IPage<MailItem> getItemByUsername(String username, MailQueryRequest queryRequest) {
        QueryChainWrapper<MailItem> queryWrapper = new QueryChainWrapper<>(mailMapper).eq("senderName", username);
        return mailMapper.selectPage(queryRequest.getPage(), queryWrapper).setRecords(
                new ArrayList<>(queryWrapper.list()));
    }

    @Override
    public IPage<UserVo> getReceiverByMailId(String mailId, int currentSize, int pageSize) {
        QueryChainWrapper<MailItem> queryWrapper = new QueryChainWrapper<>(mailMapper)
                .eq("id", Long.parseLong(mailId));
        MailItem mailItem = mailMapper.selectOne(queryWrapper);
        return new Page<UserVo>(currentSize, pageSize)
                .setTotal(mailItem.getReceivers().size()).setRecords(
                        mailItem.getReceivers().keySet().stream().map(
                        replaced -> new UserVo()
                        .setUserName(replaced)
                        .setUserAvatar(mailItem.getReceivers().get(replaced).getUserAvatar()))
                                .collect(Collectors.toList()));
    }

    @Override
    public IPage<UserVo> getReceiverByUsername(String username, int currentSize, int pageSize) {
        QueryChainWrapper<MailItem> queryWrapper = new QueryChainWrapper<>(mailMapper)
                .eq("senderName", username);
        MailItem sent = mailMapper.selectOne(queryWrapper);
        Map<String, Set<UserItem>> receiverMap = userMapper.selectList(new QueryWrapper<UserItem>()
                .in("username", sent.getReceivers().keySet()))
                .stream()
                .collect(Collectors.groupingBy(
                        UserItem::getUserName,
                        Collectors.mapping(replaced -> replaced, Collectors.toSet()))
        );
        return new Page<UserVo>(currentSize, pageSize)
                .setTotal(receiverMap.size())
                .setRecords(receiverMap.keySet().stream().map(
                    userItems -> new UserVo()
                    .setUserName(userItems)
                    .setUserAvatar(receiverMap.get(userItems).stream().findFirst().orElseThrow().getUserAvatar()))
                .toList());
    }
}
