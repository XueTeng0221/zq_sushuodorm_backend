package com.ziqiang.sushuodorm.entity.item.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;

@Data
@Accessors(chain = true)
public class FetchItem implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId
    private String fetchId;
    @TableField(value = "user_id")
    private String userId;
    @TableField(value = "description")
    private String description;
    @TableField(value = "from_dorm_id")
    private String fromDormId;
    @TableField(value = "to_dorm_id")
    private String toDormId;
    @TableField(value = "start_date")
    private Date startDate;
    @TableField(value = "end_date")
    private Date endDate;
    @TableLogic
    private boolean isDeleted;
}