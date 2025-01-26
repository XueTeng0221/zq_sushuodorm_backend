package com.ziqiang.sushuodorm.entity.dto.post;

import java.io.Serializable;
import java.util.List;

public class PostEditRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String content;
    private List<String> tags;
}
