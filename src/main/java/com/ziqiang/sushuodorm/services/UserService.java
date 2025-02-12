package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.entity.vo.UserVo;
import com.ziqiang.sushuodorm.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService extends IService<UserItem> {
    long userRegister(String userAccount, String userPassword, String checkPassword) throws BizException;

    UserVo getLoginUser(String userAccount, String userPassword, HttpServletRequest request);

    String getUserId(String code);

    String getRoomId(String username);

    boolean updateUserPhone(String userId, String phone);

    boolean updateUserProfile(String userId, String gender, String avatar);

    boolean updateUserName(String userId, String name);

    boolean updateRoomId(String userId, String roomId);

    boolean insertUserProfile(String gender, String nickname, String avatar);
}
