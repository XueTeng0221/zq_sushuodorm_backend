package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.enums.UserRoleEnum;
import com.ziqiang.sushuodorm.entity.item.RoomItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.entity.vo.UserVo;
import com.ziqiang.sushuodorm.exception.BizException;
import com.ziqiang.sushuodorm.mapper.RoomMapper;
import com.ziqiang.sushuodorm.mapper.UserMapper;
import com.ziqiang.sushuodorm.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.sql.Date;

@Service
@EqualsAndHashCode(callSuper = false)
@Data
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserItem> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoomMapper roomMapper;

    public static final String SALT = "sushuodorm";

    public long userRegister(String userAccount, String userPassword, String checkPassword) throws BizException {
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BizException(ErrorCode.PARAM_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BizException(ErrorCode.PARAM_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BizException(ErrorCode.PARAM_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BizException(ErrorCode.PARAM_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            if (userMapper.selectCount(new QueryChainWrapper<>(userMapper).lambda()
                    .eq(UserItem::getUserName, userAccount)) > 0) {
                throw new BizException(ErrorCode.PARAM_ERROR, "账号重复");
            }
            String encryptedPassword = DigestUtils.md5DigestAsHex((userAccount + SALT + userPassword).getBytes());
            UserItem userItem = new UserItem().setUserName(userAccount).setUserPassword(encryptedPassword)
                    .setUserRole(UserRoleEnum.USER.getValue());
            boolean b = userMapper.insert(userItem) > 0;
            if (!b) {
                throw new BizException(ErrorCode.PARAM_ERROR, "注册失败");
            }
            return userItem.getUnionId();
        }
    }

    @Override
    public UserVo getLoginUser(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BizException(ErrorCode.PARAM_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BizException(ErrorCode.PARAM_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8) {
            throw new BizException(ErrorCode.PARAM_ERROR, "用户密码过短");
        }
        String encryptedPassword = DigestUtils.md5DigestAsHex((userAccount + SALT + userPassword).getBytes());
        LambdaQueryChainWrapper<UserItem> queryWrapper = new QueryChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUnionId, request.getParameter("code"))
                .eq(UserItem::getUserPassword, encryptedPassword);
        UserItem loginUser = userMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(loginUser)) {
            log.info("userAccount cannot match userPassword");
            throw new BizException(ErrorCode.PARAM_ERROR, "用户不存在或密码错误");
        }
        request.getSession().setAttribute("user", loginUser);
        return new UserVo().setUserName(loginUser.getUserName());
    }

    @Override
    public String getUserId(String code) {
        LambdaQueryChainWrapper<UserItem> queryWrapper = new QueryChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUnionId, code);
        UserItem userItem = userMapper.selectOne(queryWrapper);
        return ObjectUtils.isEmpty(userItem) ? null : userItem.getUserName();
    }

    @Override
    public String getRoomId(String username) {
        LambdaQueryChainWrapper<UserItem> queryWrapper = new QueryChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUserName, username);
        UserItem userItem = userMapper.selectOne(queryWrapper);
        return ObjectUtils.isEmpty(userItem) ? null : userItem.getRoomId();
    }

    @Override
    public boolean updateUserPhone(String userId, String phone) {
        LambdaUpdateChainWrapper<UserItem> updateWrapper = new UpdateChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUserName, userId);
        return userMapper.update(userMapper.selectOne(updateWrapper).setPhone(phone), updateWrapper) > 0;
    }

    @Override
    public boolean updateUserProfile(String userId, String gender, String avatar) {
        LambdaUpdateChainWrapper<UserItem> updateWrapper = new UpdateChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUserName, userId);
        return userMapper.update(userMapper.selectOne(updateWrapper)
                .setGender(gender)
                .setUserAvatar(avatar), updateWrapper) > 0;
    }

    @Override
    public boolean updateUserName(String userId, String name) {
        LambdaUpdateChainWrapper<UserItem> updateWrapper = new UpdateChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUserName, userId);
        return userMapper.update(userMapper.selectOne(updateWrapper).setUserName(name), updateWrapper) > 0;
    }

    @Override
    public boolean updateRoomId(String userId, String roomId) {
        LambdaQueryChainWrapper<UserItem> userWrapper = new QueryChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUserName, userId)
                .eq(UserItem::getUserRole, UserRoleEnum.USER.getValue());
        UserItem userItem = userMapper.selectOne(userWrapper);
        if (userItem.getRoomId().equals(roomId)) {
            return false;
        }
        LambdaUpdateChainWrapper<RoomItem> roomUpdateWrapper = new UpdateChainWrapper<>(roomMapper).lambda()
                .eq(RoomItem::getRoomId, roomId);
        RoomItem newRoomItem = roomMapper.selectOne(roomUpdateWrapper);
        if (ObjectUtils.isEmpty(newRoomItem)) {
            RoomItem roomItem = new RoomItem().setRoomId(Integer.parseInt(roomId.substring(roomId.indexOf("-") + 1)));
            roomItem.getOccupants().put(userItem.getUserName(), userItem);
            return roomMapper.insert(roomItem) > 0;
        }
        if (newRoomItem.getCapacity() <= newRoomItem.getOccupants().size()) {
            newRoomItem.getOccupants().put(userItem.getUserName(), userItem);
        }
        LambdaUpdateChainWrapper<UserItem> userUpdateWrapper = new UpdateChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUserName, userId);
        return userMapper.update(userItem, userUpdateWrapper) > 0 && roomMapper.update(newRoomItem, roomUpdateWrapper) > 0;
    }

    @Override
    public boolean insertUserProfile(String username, String gender, String nickname, String avatar, String roomId, Date date) {
        LambdaUpdateChainWrapper<UserItem> queryWrapper = new UpdateChainWrapper<>(userMapper).lambda()
                .eq(UserItem::getUserName, username);
        UserItem userItem = userMapper.selectOne(queryWrapper)
                .setGender(gender)
                .setUserName(nickname)
                .setUserAvatar(avatar)
                .setRoomId(roomId)
                .setUpdateTime(date);
        return userMapper.insert(userItem) > 0;
    }
}
