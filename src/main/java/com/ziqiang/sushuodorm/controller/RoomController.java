package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.entity.dto.room.RoomQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.user.UserUpdateRequest;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.entity.vo.RoomVo;
import com.ziqiang.sushuodorm.entity.vo.UserVo;
import com.ziqiang.sushuodorm.services.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/room")
@RestController
@Slf4j
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PutMapping("/save")
    @RequestMapping("/{occupants}")
    public boolean save(@PathVariable("occupants") @NotNull List<UserItem> occupants, String roomName) {
        return roomService.saveRoom(occupants.stream().collect(
                Collectors.toMap(UserItem::getUserName, userItem -> userItem)), roomName);
    }

    @PutMapping("/update")
    @RequestMapping("/{occupants}")
    public boolean update(@PathVariable("occupants") @NotNull List<UserItem> occupants) {
        return roomService.updateRoom(new UserUpdateRequest(), occupants.stream().collect(
                Collectors.toMap(UserItem::getUserName, userItem -> userItem)));
    }

    @PutMapping("/remove")
    @RequestMapping("/{roomId}")
    public boolean remove(@PathVariable("roomId") String roomId) {
        return roomService.removeRoom(roomId);
    }

    @PostMapping("/getAllRooms")
    public List<RoomVo> getAllRooms(@Valid @NotNull RoomQueryRequest roomQueryRequest) {
        return roomService.getAllRooms(roomQueryRequest).getRecords();
    }

    @PostMapping("/getRoomsByOccupants")
    public List<RoomVo> getRoomsByOccupants(@NotNull List<String> occupants, @Valid @NotNull RoomQueryRequest roomQueryRequest) {
        return roomService.getRoomsByOccupants(occupants, roomQueryRequest).getRecords();
    }

    @PostMapping("/getOccupantsByRoomId")
    public List<UserVo> getOccupantsByRoomId(@RequestParam String roomId, @Valid @NotNull RoomQueryRequest roomQueryRequest) {
        return roomService.getOccupantsByRoomId(roomId, roomQueryRequest).getRecords();
    }

    @PostMapping("/searchByOccupant")
    public List<RoomVo> searchByOccupant(@RequestParam String occupant, @Valid @NotNull RoomQueryRequest roomQueryRequest) {
        return roomService.searchByOccupant(occupant, roomQueryRequest).getRecords();
    }

    @PostMapping("/searchByRoomId")
    public List<RoomVo> searchByRoomId(@RequestParam String roomId, @Valid @NotNull RoomQueryRequest roomQueryRequest) {
        return roomService.searchByRoomId(roomId, roomQueryRequest).getRecords();
    }

    @PostMapping("/searchByRoomName")
    public List<RoomVo> searchByRoomName(@RequestParam String roomName, @Valid @NotNull RoomQueryRequest roomQueryRequest) {
        return roomService.searchByRoomName(roomName, roomQueryRequest).getRecords();
    }
}
