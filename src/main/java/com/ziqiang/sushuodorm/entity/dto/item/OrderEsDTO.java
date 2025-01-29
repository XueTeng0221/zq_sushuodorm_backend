package com.ziqiang.sushuodorm.entity.dto.item;

import lombok.Data;

import java.sql.Date;

@Data
public class OrderEsDTO {
    String orderId;
    String userId;
    String fromDormId;
    String toDormId;
    Date startDate;
}
