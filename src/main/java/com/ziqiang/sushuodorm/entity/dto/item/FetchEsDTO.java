package com.ziqiang.sushuodorm.entity.dto.item;

import lombok.Data;

import java.sql.Date;

@Data
public class FetchEsDTO {
    String fetchId;
    String userId;
    String fromDormId;
    String toDormId;
    Date startDate;
}
