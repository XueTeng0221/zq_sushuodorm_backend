package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.dto.room.RoomQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.user.UserUpdateRequest;
import com.ziqiang.sushuodorm.entity.item.RoomItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.entity.vo.RoomVo;
import com.ziqiang.sushuodorm.entity.vo.UserVo;

import java.util.List;
import java.util.Map;

public interface RoomService extends IService<RoomItem> {
    boolean saveRoom(Map<String, UserItem> occupants, String roomName);

    boolean updateRoom(UserUpdateRequest updateRequest, Map<String, UserItem> occupants);

    boolean removeRoom(String roomId);

    IPage<RoomVo> searchByOccupant(String occupant, RoomQueryRequest roomQueryRequest);

    IPage<RoomVo> searchByRoomId(String roomId, RoomQueryRequest roomQueryRequest);

    IPage<RoomVo> searchByRoomName(String roomName, RoomQueryRequest roomQueryRequest);

    IPage<RoomVo> getAllRooms(RoomQueryRequest roomQueryRequest);

    IPage<RoomVo> getRoomsByOccupants(List<String> occupants, RoomQueryRequest roomQueryRequest);

    IPage<UserVo> getOccupantsByRoomId(String roomId, RoomQueryRequest roomQueryRequest);
}
