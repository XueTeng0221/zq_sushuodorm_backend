package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
                .setReceivers(receivers);
        mailMapper.insert(mailItem);
        return save(mailItem);
    }

    @Override
    public boolean remove(String userId) {
        QueryWrapper<MailItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", Long.parseLong(userId));
        mailMapper.delete(queryWrapper);
        return remove(queryWrapper);
    }

    @Override
    public boolean update(String userId, String postId) {
        QueryWrapper<MailItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", Long.parseLong(userId))
                .eq("id", Long.parseLong(postId));
        MailItem mailItem = new MailItem().setUserId(Long.parseLong(userId));
        mailItem.setIsDeleted(false);
        return update(mailItem, queryWrapper);
    }

    @Override
    public IPage<MailItem> getItemByUsername(String username, MailQueryRequest queryRequest) {
        Wrapper<MailItem> queryWrapper = Wrappers.<MailItem>lambdaQuery()
                .eq(MailItem::getUserId, Long.parseLong(username))
                .eq(MailItem::getIsDeleted, false);
        return mailMapper.selectPage(queryRequest.getPage(), queryWrapper);
    }

    @Override
    public IPage<UserVo> getReceiverByUsername(String username, int currentSize, int pageSize) {
        QueryWrapper<MailItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sender_name", username)
                .select("sender_name", "receiver_name");
        MailItem sent = mailMapper.selectOne(queryWrapper);
        Map<String, Set<UserItem>> receiverMap = userMapper.selectList(new QueryWrapper<UserItem>()
                .in("username", sent.getReceivers().keySet())).stream().collect(
                Collectors.groupingBy(UserItem::getUserName, Collectors.mapping(v -> v, Collectors.toSet()))
        );
        IPage<UserVo> queryRequest = new Page<>(currentSize, pageSize);
        queryRequest.setTotal(receiverMap.size());
        queryRequest.setRecords(receiverMap.keySet().stream().map(userItems -> new UserVo()
                .setUserName(userItems)
                .setUserAvatar(receiverMap.get(userItems).stream().findFirst().orElseThrow().getUserAvatar())
        ).toList());
        return queryRequest;
    }
}
