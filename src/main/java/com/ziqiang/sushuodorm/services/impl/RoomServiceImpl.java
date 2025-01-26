package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.dto.room.RoomQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.user.UserUpdateRequest;
import com.ziqiang.sushuodorm.entity.item.RoomItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.mapper.RoomMapper;
import com.ziqiang.sushuodorm.mapper.UserMapper;
import com.ziqiang.sushuodorm.services.RoomService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@EqualsAndHashCode(callSuper = false)
@Data
public class RoomServiceImpl extends ServiceImpl<RoomMapper, RoomItem> implements RoomService {
    private RoomMapper roomMapper;
    private UserMapper userMapper;

    public RoomServiceImpl(RoomMapper roomMapper, UserMapper userMapper) {
        this.roomMapper = roomMapper;
        this.userMapper = userMapper;
    }

    public boolean saveRoom(Map<String, UserItem> occupants, String roomName) {
        RoomItem roomItem = new RoomItem()
                .setOccupants(occupants)
                .setRoomName(roomName)
                .setRoomId(Integer.parseInt(roomName.substring(roomName.indexOf("-") + 1)))
                .setDormName(roomName.substring(0, roomName.indexOf("-")));
        roomMapper.insert(roomItem);
        for (Map.Entry<String, UserItem> entry : occupants.entrySet()) {
            UserItem userItem = entry.getValue();
            userItem.setRoomId(roomName.substring(roomName.indexOf("-") + 1));
        }
        return saveOrUpdate(roomItem);
    }

    public boolean updateRoom(UserUpdateRequest userUpdateRequest, Map<String, UserItem> occupants) {
        RoomItem roomItem = new RoomItem()
                .setOccupants(occupants)
                .setRoomName(userUpdateRequest.getRoomId())
                .setRoomId(Integer.parseInt(
                        userUpdateRequest.getRoomId().substring(userUpdateRequest.getRoomId().indexOf("-") + 1)))
                .setDormName(userUpdateRequest.getRoomId().substring(0, userUpdateRequest.getRoomId().indexOf("-")));
        roomMapper.updateById(roomItem);
        for (Map.Entry<String, UserItem> entry : occupants.entrySet()) {
            UserItem userItem = entry.getValue();
            userItem.setRoomId(userUpdateRequest.getRoomId().substring(userUpdateRequest.getRoomId().indexOf("-") + 1));
        }
        return saveOrUpdate(roomItem);
    }

    public boolean removeRoom(String roomId) {
        QueryWrapper<RoomItem> roomWrapper = new QueryWrapper<RoomItem>().eq("room_id", roomId);
        QueryWrapper<UserItem> userWrapper = new QueryWrapper<UserItem>().eq("room_id", roomId);
        List<UserItem> userItems = userMapper.selectList(userWrapper);
        for (UserItem userItem : userItems) {
            userItem.setRoomId(null);
            userMapper.updateById(userItem);
        }
        return roomMapper.delete(roomWrapper) > 0 && userMapper.update(null, userWrapper) > 0;
    }
}
