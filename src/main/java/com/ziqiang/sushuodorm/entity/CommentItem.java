package com.ziqiang.sushuodorm.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Accessors(chain = true)
@Table("comment")
public class CommentItem {
}
