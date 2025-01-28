package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.item.RoomItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.mapper.RoomMapper;
import com.ziqiang.sushuodorm.mapper.UserMapper;
import com.ziqiang.sushuodorm.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@EqualsAndHashCode(callSuper = false)
@Data
public class UserServiceImpl extends ServiceImpl<UserMapper, UserItem> implements UserService {
    @Value("${wxapp.appId}")
    private String appId;
    @Value("${wxapp.appSecret}")
    private String appSecret;
    private UserMapper userMapper;
    private RoomMapper roomMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, RoomMapper roomMapper) {
        this.userMapper = userMapper;
        this.roomMapper = roomMapper;
    }

    @Override
    public UserItem getLoginUser(HttpServletRequest request) {
        LambdaQueryChainWrapper<UserItem> queryWrapper = new QueryChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUnionId, request.getParameter("code"));
        UserItem loginUser = userMapper.selectOne(queryWrapper);
        return ObjectUtils.isEmpty(loginUser) ? null : loginUser;
    }

    @Override
    public String getUserId(String code) {
        LambdaQueryChainWrapper<UserItem> queryWrapper = new QueryChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUnionId, code);
        UserItem userItem = userMapper.selectOne(queryWrapper);
        return ObjectUtils.isEmpty(userItem) ? null : userItem.getUserName();
    }

    @Override
    public String getRoomId(String username) {
        LambdaQueryChainWrapper<UserItem> queryWrapper = new QueryChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUserName, username);
        UserItem userItem = userMapper.selectOne(queryWrapper);
        return ObjectUtils.isEmpty(userItem) ? null : userItem.getRoomId();
    }

    @Override
    public boolean updateUserPhone(String userId, String phone) {
        LambdaUpdateChainWrapper<UserItem> updateWrapper = new UpdateChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUserName, userId);
        return userMapper.update(userMapper.selectOne(updateWrapper).setPhone(phone), updateWrapper) > 0;
    }

    @Override
    public boolean updateUserProfile(String userId, String gender, String avatar) {
        LambdaUpdateChainWrapper<UserItem> updateWrapper = new UpdateChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUserName, userId);
        return userMapper.update(userMapper.selectOne(updateWrapper)
                .setGender(gender)
                .setUserAvatar(avatar), updateWrapper) > 0;
    }

    @Override
    public boolean updateUserName(String userId, String name) {
        LambdaUpdateChainWrapper<UserItem> updateWrapper = new UpdateChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUserName, userId);
        return userMapper.update(userMapper.selectOne(updateWrapper).setUserName(name), updateWrapper) > 0;
    }

    @Override
    public boolean updateRoomId(String userId, String roomId) {
        QueryChainWrapper<UserItem> queryWrapper = new QueryChainWrapper<>(userMapper).eq("user_id", userId);
        UserItem userItem = userMapper.selectOne(queryWrapper);
        if (userItem.getRoomId().equals(roomId)) {
            return false;
        }
        QueryChainWrapper<RoomItem> roomWrapper = new QueryChainWrapper<>(roomMapper).eq("room_id", userItem.getRoomId());
        if (roomWrapper.exists()) {
            RoomItem roomItem = roomMapper.selectOne(roomWrapper);
            roomItem.getOccupants().remove(userItem.getUserName());
        }
        LambdaUpdateChainWrapper<RoomItem> roomUpdateWrapper = new UpdateChainWrapper<>(roomMapper).lambda()
                .eq(RoomItem::getRoomId, roomId)
                .setSql("occupants = concat(occupants, '" + userItem.getUserName() + "')");
        RoomItem newRoomItem = roomMapper.selectOne(roomUpdateWrapper);
        if (ObjectUtils.isEmpty(newRoomItem)) {
            RoomItem roomItem = new RoomItem().setRoomId(Integer.parseInt(roomId.substring(roomId.indexOf("-") + 1)));
            roomItem.getOccupants().put(userItem.getUserName(), userItem);
            return roomMapper.insert(roomItem) > 0;
        }
        if (newRoomItem.getCapacity() <= newRoomItem.getOccupants().size()) {
            newRoomItem.getOccupants().put(userItem.getUserName(), userItem);
        }
        LambdaUpdateChainWrapper<UserItem> userUpdateWrapper = new UpdateChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUserName, userId);
        return userMapper.update(userItem, userUpdateWrapper) > 0 && roomMapper.update(null, roomWrapper) > 0;
    }

    @Override
    public boolean insertUserProfile(String gender, String nickname, String avatar) {
        UserItem userItem = new UserItem()
                .setGender(gender)
                .setUserName(nickname)
                .setUserAvatar(avatar);
        return userMapper.insert(userItem) > 0;
    }
}
