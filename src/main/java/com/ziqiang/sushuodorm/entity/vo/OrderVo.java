package com.ziqiang.sushuodorm.entity.vo;

import com.ziqiang.sushuodorm.entity.enums.OrderStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;

@Data
@Accessors(chain = true)
public class OrderVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private OrderStatusEnum status;
    private String userId;
    private String fromDormId;
    private String toDormId;
    private Date startDate;
    private Date endDate;
}
