package com.ziqiang.sushuodorm.entity.dto.item;

import lombok.Data;

import java.sql.Date;

@Data
public class OrderAddRequest {
    String orderId;
    String userId;
    String postId;
    String roomId;
    String title;
    String description;
    String fromDormId;
    String toDormId;
    Date startDate;
    Date endDate;
}
