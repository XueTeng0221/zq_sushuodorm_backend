package com.ziqiang.sushuodorm.entity.item;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@Data
@Accessors(chain = true)
@TableName("room")
public class RoomItem implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(value = "dorm_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String dormName;

    @TableId
    @NotNull(message = "寝室号不能为空")
    private Integer roomId;

    @TableField(value = "room_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String roomName;

    private Integer capacity;

    @TableField(value = "occupants", updateStrategy = FieldStrategy.NOT_EMPTY)
    private Map<Integer, UserItem> occupants;
}
