package com.ziqiang.sushuodorm.entity.vo;

import com.ziqiang.sushuodorm.entity.item.UserItem;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

@Data
@Accessors(chain = true)
public class RoomVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<Integer, UserItem> occupantMap;
    private String dormName;
    private String roomName;
    private Integer roomId;
    private Integer capacity;
}
