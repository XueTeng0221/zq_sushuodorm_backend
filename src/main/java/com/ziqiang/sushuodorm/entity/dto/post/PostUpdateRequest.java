package com.ziqiang.sushuodorm.entity.dto.post;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PostUpdateRequest implements Serializable {
    private Long id;
    private String title;
    private String content;
    private List<String> tags;
}
