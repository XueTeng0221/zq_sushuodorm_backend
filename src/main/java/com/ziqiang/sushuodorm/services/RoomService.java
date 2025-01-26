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

    IPage<RoomVo> getPage(int pageNum, int pageId);

    IPage<RoomVo> searchByOccupant(String occupant, int pageNum, int pageId);

    IPage<RoomVo> searchByRoomId(String roomId, int pageNum, int pageId);

    IPage<RoomVo> getAllRooms(RoomQueryRequest roomQueryRequest);

    IPage<RoomVo> getRoomsByOccupants(List<String> occupants, int pageNum, int pageId);

    IPage<UserVo> getOccupantsByRoomId(String roomId, int pageNum, int pageId);
}
