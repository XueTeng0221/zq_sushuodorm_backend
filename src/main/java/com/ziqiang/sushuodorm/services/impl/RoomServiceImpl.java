package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.dto.room.RoomQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.user.UserUpdateRequest;
import com.ziqiang.sushuodorm.entity.item.RoomItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.entity.vo.RoomVo;
import com.ziqiang.sushuodorm.entity.vo.UserVo;
import com.ziqiang.sushuodorm.mapper.RoomMapper;
import com.ziqiang.sushuodorm.mapper.UserMapper;
import com.ziqiang.sushuodorm.services.RoomService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
@EqualsAndHashCode(callSuper = false)
@Data
public class RoomServiceImpl extends ServiceImpl<RoomMapper, RoomItem> implements RoomService {
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private UserMapper userMapper;

    public boolean saveRoom(Map<String, UserItem> occupants, String roomName) {
        RoomItem roomItem = new RoomItem()
                .setOccupants(occupants)
                .setRoomName(roomName)
                .setRoomId(Integer.parseInt(roomName.substring(roomName.indexOf("-") + 1)))
                .setDormName(roomName.substring(0, roomName.indexOf("-")));
        occupants.forEach((key, value) -> userMapper.updateById(
                value.setRoomId(roomName.substring(roomName.indexOf("-") + 1))));
        return roomMapper.insert(roomItem) > 0;
    }

    public boolean updateRoom(UserUpdateRequest userUpdateRequest, Map<String, UserItem> occupants) {
        LambdaQueryChainWrapper<RoomItem> roomWrapper = new QueryChainWrapper<>(roomMapper).lambda()
                .eq(RoomItem::getRoomId, userUpdateRequest.getRoomId());
        RoomItem roomItem = new RoomItem()
                .setOccupants(occupants)
                .setRoomName(userUpdateRequest.getRoomId())
                .setRoomId(Integer.parseInt(
                        userUpdateRequest.getRoomId().substring(userUpdateRequest.getRoomId().indexOf("-") + 1)))
                .setDormName(userUpdateRequest.getRoomId().substring(0, userUpdateRequest.getRoomId().indexOf("-")));
        occupants.forEach((key, value) -> userMapper.updateById(
                value.setRoomId(userUpdateRequest.getRoomId().substring(userUpdateRequest.getRoomId().indexOf("-") + 1))));
        return roomMapper.update(roomItem, roomWrapper) > 0;
    }

    public boolean removeRoom(String roomId) {
        LambdaQueryChainWrapper<RoomItem> roomWrapper = new QueryChainWrapper<>(roomMapper).lambda()
                .eq(RoomItem::getRoomId, roomId);
        LambdaQueryChainWrapper<UserItem> userWrapper = new QueryChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getRoomId, roomId);
        List<UserItem> userItems = userMapper.selectList(userWrapper);
        userItems.forEach(userItem -> userMapper.updateById(userItem.setRoomId(null)));
        return roomMapper.delete(roomWrapper) > 0 && userMapper.delete(userWrapper) > 0;
    }

    @Override
    public IPage<RoomVo> getAllRooms(RoomQueryRequest roomQueryRequest) {
        LambdaQueryChainWrapper<RoomItem> queryWrapper = new QueryChainWrapper<>(roomMapper).lambda()
                .like(RoomItem::getRoomName, roomQueryRequest.getRoomName());
        roomQueryRequest.getOccupants().forEach((key, value) -> queryWrapper.like(RoomItem::getOccupants, value.getUserName()));
        return queryWrapper.page(new Page<>(roomQueryRequest.getCurrentId(), roomQueryRequest.getPageSize()))
                .convert(roomItem -> new RoomVo()
                        .setOccupantMap(roomItem.getOccupants())
                        .setDormName(roomItem.getDormName())
                        .setRoomName(roomItem.getRoomName()));
    }

    @Override
    public IPage<RoomVo> getRoomsByOccupants(List<String> occupants, RoomQueryRequest roomQueryRequest) {
        QueryChainWrapper<RoomItem> queryWrapper = new QueryChainWrapper<>(roomMapper)
                .like("roomName", roomQueryRequest.getRoomName());
        if (!CollectionUtils.isEmpty(roomQueryRequest.getOccupants())) {
            Map<String, UserItem> occupantMap = roomQueryRequest.getOccupants();
            if (occupantMap.keySet().containsAll(occupants)) {
                occupantMap.forEach((key, value) -> queryWrapper.like("occupantName", key));
            }
        }
        return queryWrapper.page(new Page<>(roomQueryRequest.getCurrentId(), roomQueryRequest.getPageSize()))
                .convert(roomItem -> new RoomVo()
                        .setOccupantMap(roomItem.getOccupants())
                        .setDormName(roomItem.getDormName())
                        .setRoomName(roomItem.getRoomName()));
    }

    @Override
    public IPage<UserVo> getOccupantsByRoomId(String roomId, RoomQueryRequest roomQueryRequest) {
        QueryChainWrapper<RoomItem> queryWrapper = new QueryChainWrapper<>(roomMapper)
                .eq("room_id", roomId);
        return queryWrapper.page(new Page<>(roomQueryRequest.getCurrentId(), roomQueryRequest.getPageSize()))
                .convert(roomItem -> new UserVo().setRoomId(roomId));
    }

    @Override
    public IPage<RoomVo> searchByOccupant(String occupant, RoomQueryRequest roomQueryRequest) {
        QueryChainWrapper<RoomItem> queryWrapper = new QueryChainWrapper<>(roomMapper);
        if (!CollectionUtils.isEmpty(roomQueryRequest.getOccupants()) && roomQueryRequest.getOccupants().containsKey(occupant)) {
            return queryWrapper.page(new Page<>(roomQueryRequest.getCurrentId(), roomQueryRequest.getPageSize()))
                    .convert(roomItem -> new RoomVo()
                            .setOccupantMap(roomItem.getOccupants())
                            .setDormName(roomItem.getDormName())
                            .setRoomName(roomItem.getRoomName()));
        }
        return queryWrapper.page(new Page<>(roomQueryRequest.getCurrentId(), roomQueryRequest.getPageSize()))
                .convert(roomItem -> new RoomVo());
    }

    @Override
    public IPage<RoomVo> searchByRoomId(String roomId, RoomQueryRequest roomQueryRequest) {
        QueryChainWrapper<RoomItem> queryWrapper = new QueryChainWrapper<>(roomMapper)
                .eq("roomId", Integer.parseInt(roomId.substring(roomId.indexOf("-") + 1)));
        return queryWrapper.page(new Page<>(roomQueryRequest.getCurrentId(), roomQueryRequest.getPageSize()))
                .convert(roomItem -> new RoomVo()
                        .setOccupantMap(roomItem.getOccupants())
                        .setDormName(roomItem.getDormName())
                );
    }

    @Override
    public IPage<RoomVo> searchByRoomName(String roomName, RoomQueryRequest roomQueryRequest) {
        QueryChainWrapper<RoomItem> queryWrapper = new QueryChainWrapper<>(roomMapper)
                .like("roomName", roomName);
        return queryWrapper.page(new Page<>(roomQueryRequest.getCurrentId(), roomQueryRequest.getPageSize()))
                .convert(roomItem -> new RoomVo()
                        .setOccupantMap(roomItem.getOccupants())
                        .setDormName(roomItem.getDormName())
                );
    }
}
