package com.ziqiang.sushuodorm.entity.dto.user;

import com.ziqiang.sushuodorm.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String unionId;
    private String userName;
    private String userProfile;
    private String userAvatar;
    private String userRole;
}
