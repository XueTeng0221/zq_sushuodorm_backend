package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.dto.room.RoomQueryRequest;
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

import java.util.ArrayList;
import java.util.HashMap;
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
    public IPage<RoomVo> getRoomsByOccupants(String roomName, List<String> occupants, RoomQueryRequest roomQueryRequest) {
        LambdaQueryChainWrapper<RoomItem> queryWrapper = new QueryChainWrapper<>(roomMapper).lambda()
                .eq(RoomItem::getRoomName, roomName);
        List<RoomItem> roomItems = roomMapper.selectList(queryWrapper);

        LambdaQueryChainWrapper<UserItem> userQueryWrapper = new QueryChainWrapper<>(userMapper).lambda()
                .in(UserItem::getUserName, occupants);
        List<UserItem> userItems = userMapper.selectList(userQueryWrapper);

        Map<String, String> usernameRoomNameMap = new HashMap<>();
        Map<String, RoomItem> roomNameRoomItemMap = new HashMap<>();

        userItems.forEach(userItem -> usernameRoomNameMap.put(userItem.getUserName(), userItem.getRoomId()));
        roomItems.forEach(roomItem -> roomNameRoomItemMap.put(roomItem.getRoomName(), roomItem));
        Map<String, Map<String, UserItem>> roomNameOccupantsMap = new HashMap<>();
        usernameRoomNameMap.forEach((key, value) -> {
            Map<String, UserItem> occupantMap = roomNameRoomItemMap.get(value).getOccupants();
            roomNameOccupantsMap.put(value, occupantMap);
        });
        List<RoomVo> roomVos = new ArrayList<>();
        roomItems.forEach(roomItem -> roomVos.add(new RoomVo()
                .setRoomName(roomItem.getRoomName())
                .setDormName(roomItem.getRoomName().substring(0, roomItem.getRoomName().indexOf('-')))
                .setCapacity(roomItem.getCapacity())
                .setOccupantMap(roomNameOccupantsMap.getOrDefault(roomItem.getRoomName(), new HashMap<>()))
                .setRoomId(Integer.parseInt(roomItem.getRoomName().substring(roomItem.getRoomName().indexOf('-') + 1)))
            )
        );
        IPage<RoomVo> page = new Page<>(roomQueryRequest.getCurrentId(), roomQueryRequest.getPageSize());
        page.setRecords(roomVos);
        return page;
    }

    @Override
    public IPage<UserVo> getOccupantsByRoomId(String roomId, RoomQueryRequest roomQueryRequest) {
        LambdaQueryChainWrapper<RoomItem> queryWrapper = new QueryChainWrapper<>(roomMapper).lambda()
                .eq(RoomItem::getRoomId, roomId)
                .like(RoomItem::getDormName, roomQueryRequest.getDormName());
        RoomItem roomItem = roomMapper.selectOne(queryWrapper);
        List<UserItem> userItems = new ArrayList<>(roomItem.getOccupants().values());
        List<UserVo> userVos = new ArrayList<>();
        userItems.forEach(userItem -> userVos.add(new UserVo()
            .setUserName(userItem.getUserName())
            .setUserAvatar(userItem.getUserAvatar())
            .setRoomId(userItem.getRoomId()))
        );
        IPage<UserVo> page = new Page<>(roomQueryRequest.getCurrentId(), roomQueryRequest.getPageSize());
        page.setRecords(userVos);
        return page;
    }

    @Override
    public IPage<RoomVo> searchByRoomId(String dormName, String roomId, RoomQueryRequest roomQueryRequest) {
        LambdaQueryChainWrapper<RoomItem> queryWrapper = new QueryChainWrapper<>(roomMapper).lambda()
                .like(RoomItem::getRoomId, Integer.parseInt(roomId.substring(roomId.indexOf("-") + 1)))
                .like(RoomItem::getDormName, roomId.substring(0, roomId.indexOf("-")));
        List<RoomItem> roomItems = roomMapper.selectList(queryWrapper);
        List<RoomVo> roomVos = new ArrayList<>();
        roomItems.forEach(roomItem -> roomVos.add(
            new RoomVo()
                .setRoomName(roomItem.getRoomName())
                .setDormName(roomItem.getDormName())
                .setCapacity(roomItem.getCapacity())
                .setOccupantMap(roomItem.getOccupants())
                .setRoomId(Integer.parseInt(roomItem.getRoomName().substring(roomItem.getRoomName().indexOf('-') + 1)))
        ));
        IPage<RoomVo> page = new Page<>(roomQueryRequest.getCurrentId(), roomQueryRequest.getPageSize());
        page.setRecords(roomVos);
        return page;
    }

    @Override
    public IPage<RoomVo> searchByRoomName(String roomName, RoomQueryRequest roomQueryRequest) {
        LambdaQueryChainWrapper<RoomItem> queryWrapper = new QueryChainWrapper<>(roomMapper).lambda()
                .like(RoomItem::getRoomName, roomName);
        List<RoomItem> roomItems = roomMapper.selectList(queryWrapper);
        List<RoomVo> roomVos = new ArrayList<>();
        roomItems.forEach(roomItem -> roomVos.add(
            new RoomVo()
                .setRoomName(roomItem.getRoomName())
                .setDormName(roomItem.getDormName())
                .setCapacity(roomItem.getCapacity())
                .setOccupantMap(roomItem.getOccupants())
                .setRoomId(Integer.parseInt(roomItem.getRoomName().substring(roomItem.getRoomName().indexOf('-') + 1)))
        ));
        IPage<RoomVo> page = new Page<>(roomQueryRequest.getCurrentId(), roomQueryRequest.getPageSize());
        page.setRecords(roomVos);
        return page;
    }
}
