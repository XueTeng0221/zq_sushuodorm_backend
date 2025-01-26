package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.item.RoomItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.mapper.RoomMapper;
import com.ziqiang.sushuodorm.mapper.UserMapper;
import com.ziqiang.sushuodorm.services.UserService;
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
    public String getUserId(String code) {
        QueryWrapper<UserItem> queryWrapper = new QueryWrapper<UserItem>().eq("union_id", code);
        UserItem userItem = userMapper.selectOne(queryWrapper);
        return ObjectUtils.isEmpty(userItem) ? null : userItem.getUserName();
    }

    @Override
    public String getRoomId(String username) {
        QueryWrapper<UserItem> queryWrapper = new QueryWrapper<UserItem>().eq("user_id", username);
        UserItem userItem = userMapper.selectOne(queryWrapper);
        return ObjectUtils.isEmpty(userItem) ? null : userItem.getRoomId();
    }

    @Override
    public boolean updateUserPhone(String userId, String phone) {
        UserItem userItem = new UserItem().setPhone(phone);
        UpdateWrapper<UserItem> updateWrapper = new UpdateWrapper<UserItem>().eq("user_id", userId);
        return userMapper.update(userItem, updateWrapper) > 0;
    }

    @Override
    public boolean updateUserProfile(String userId, String gender, String avatar) {
        UserItem userItem = new UserItem().setGender(gender).setUserAvatar(avatar);
        UpdateWrapper<UserItem> updateWrapper = new UpdateWrapper<UserItem>().eq("user_id", userId);
        return userMapper.update(userItem, updateWrapper) > 0;
    }

    @Override
    public boolean updateUserName(String userId, String name) {
        UserItem userItem = new UserItem().setUserName(name);
        UpdateWrapper<UserItem> updateWrapper = new UpdateWrapper<UserItem>().eq("user_id", userId);
        return userMapper.update(userItem, updateWrapper) > 0;
    }

    @Override
    public boolean updateRoomId(String userId, String roomId) {
        QueryWrapper<UserItem> queryWrapper = new QueryWrapper<UserItem>().eq("user_id", userId);
        UserItem userItem = userMapper.selectOne(queryWrapper);
        if (userItem.getRoomId().equals(roomId)) {
            return false;
        }
        QueryChainWrapper<RoomItem> roomWrapper = new QueryChainWrapper<>(roomMapper).eq("room_id", userItem.getRoomId());
        if (roomWrapper.exists()) {
            RoomItem roomItem = roomMapper.selectOne(roomWrapper);
            roomItem.getOccupants().remove(userItem.getUserName());
            roomMapper.updateById(roomItem);
        }
        UpdateWrapper<RoomItem> roomUpdateWrapper = new UpdateWrapper<RoomItem>()
                .eq("room_id", roomId)
                .setSql("occupants = concat(occupants, '" + userItem.getUserName() + "')");
        RoomItem newRoomItem = roomMapper.selectOne(roomUpdateWrapper);
        if (ObjectUtils.isEmpty(newRoomItem)) {
            RoomItem roomItem = new RoomItem().setRoomId(Integer.parseInt(roomId));
            roomItem.getOccupants().put(userItem.getUserName(), userItem);
            return roomMapper.insert(roomItem) > 0;
        }
        if (newRoomItem.getCapacity() <= newRoomItem.getOccupants().size()) {
            newRoomItem.getOccupants().put(userItem.getUserName(), userItem);
        }
        UpdateWrapper<UserItem> userUpdateWrapper = new UpdateWrapper<UserItem>().eq("user_id", userId);
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
