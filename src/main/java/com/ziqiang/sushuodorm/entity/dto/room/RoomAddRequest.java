package com.ziqiang.sushuodorm.entity.dto.room;

import com.ziqiang.sushuodorm.entity.item.UserItem;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class RoomAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dormName;
    private String roomName;
    private Integer capacity;
    private Long roomId;
    private Map<Integer, UserItem> occupants;
}
