package com.ziqiang.sushuodorm.entity.dto.item;

import com.ziqiang.sushuodorm.entity.enums.OrderStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class OrderUpdateRequest implements Serializable {
    String fetchId;
    String userId;
    String postId;
    String fromDormId;
    String toDormId;
    Date startDate;
    Date endDate;
    OrderStatusEnum status;
}
