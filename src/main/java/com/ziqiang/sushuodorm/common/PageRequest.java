package com.ziqiang.sushuodorm.common;

import lombok.Data;

@Data
public class PageRequest {
    private int currentId = 1;
    private int pageSize = 10;
    private String sortField;
}
