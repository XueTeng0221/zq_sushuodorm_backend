package com.ziqiang.sushuodorm.entity.dto.room;

import com.ziqiang.sushuodorm.common.PageRequest;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class RoomQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dormName;
    private String roomName;
    private Integer capacity;
    private Long roomId;
    private Map<String, UserItem> occupants;
}
