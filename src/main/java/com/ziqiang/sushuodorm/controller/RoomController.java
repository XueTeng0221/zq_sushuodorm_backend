package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.dto.room.RoomQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.user.UserUpdateRequest;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.entity.vo.ResponseBeanVo;
import com.ziqiang.sushuodorm.entity.vo.RoomVo;
import com.ziqiang.sushuodorm.entity.vo.UserVo;
import com.ziqiang.sushuodorm.services.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
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
    public ResponseBeanVo<?> save(@PathVariable("occupants") @NotNull List<UserItem> occupants, String roomName) {
        boolean b = roomService.saveRoom(occupants.stream().collect(
                Collectors.toMap(UserItem::getUserName, userItem -> userItem, (i1, i2) -> i1, HashMap::new)), roomName);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PutMapping("/update")
    @RequestMapping("/{occupants}")
    public ResponseBeanVo<?> update(@PathVariable("occupants") @NotNull List<UserItem> occupants) {
        boolean b = roomService.updateRoom(new UserUpdateRequest(), occupants.stream().collect(
                Collectors.toMap(UserItem::getUserName, userItem -> userItem, (i1, i2) -> i1, HashMap::new)));
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PutMapping("/remove")
    @RequestMapping("/{roomId}")
    public ResponseBeanVo<?> remove(@PathVariable("roomId") String roomId) {
        boolean b = roomService.removeRoom(roomId);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PostMapping("/getAllRooms")
    public ResponseBeanVo<List<RoomVo>> getAllRooms(@Valid @NotNull RoomQueryRequest roomQueryRequest) {
        return ResponseBeanVo.ok(roomService.getAllRooms(roomQueryRequest).getRecords());
    }

    @PostMapping("/getRoomsByOccupants")
    public ResponseBeanVo<List<RoomVo>> getRoomsByOccupants(@NotNull List<String> occupants,
                                                            @Valid @NotNull RoomQueryRequest roomQueryRequest) {
        return ResponseBeanVo.ok(roomService.getRoomsByOccupants(occupants, roomQueryRequest).getRecords());
    }

    @PostMapping("/getOccupantsByRoomId")
    public ResponseBeanVo<List<UserVo>> getOccupantsByRoomId(@RequestParam String roomId,
                                                             @Valid @NotNull RoomQueryRequest roomQueryRequest) {
        return ResponseBeanVo.ok(roomService.getOccupantsByRoomId(roomId, roomQueryRequest).getRecords());
    }

    @PostMapping("/searchByOccupant")
    public ResponseBeanVo<List<RoomVo>> searchByOccupant(@RequestParam String occupant,
                                                         @Valid @NotNull RoomQueryRequest roomQueryRequest) {
        return ResponseBeanVo.ok(roomService.searchByOccupant(occupant, roomQueryRequest).getRecords());
    }

    @PostMapping("/searchByRoomId")
    public ResponseBeanVo<List<RoomVo>> searchByRoomId(@RequestParam String roomId,
                                                       @Valid @NotNull RoomQueryRequest roomQueryRequest) {
        return ResponseBeanVo.ok(roomService.searchByRoomId(roomId, roomQueryRequest).getRecords());
    }

    @PostMapping("/searchByRoomName")
    public ResponseBeanVo<List<RoomVo>> searchByRoomName(@RequestParam String roomName,
                                                         @Valid @NotNull RoomQueryRequest roomQueryRequest) {
        return ResponseBeanVo.ok(roomService.searchByRoomName(roomName, roomQueryRequest).getRecords());
    }
}
