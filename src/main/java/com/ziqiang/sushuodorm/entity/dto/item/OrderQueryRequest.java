package com.ziqiang.sushuodorm.entity.dto.item;

import com.ziqiang.sushuodorm.common.PageRequest;
import com.ziqiang.sushuodorm.entity.enums.OrderStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private OrderStatusEnum status;
    private String fetchId;
    private String postId;
    private String userId;
    private String description;
    private String fromDormId;
    private String toDormId;
    private Date startDate;
    private Date endDate;
}
