package com.ziqiang.sushuodorm.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = -166666666666666666L;
    private Long id;
}
