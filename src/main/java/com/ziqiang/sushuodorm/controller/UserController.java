package com.ziqiang.sushuodorm.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.dto.user.UserLoginRequest;
import com.ziqiang.sushuodorm.entity.dto.user.UserQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.user.UserRegisterRequest;
import com.ziqiang.sushuodorm.entity.vo.ResponseBeanVo;
import com.ziqiang.sushuodorm.entity.vo.UserVo;
import com.ziqiang.sushuodorm.exception.BizException;
import com.ziqiang.sushuodorm.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping("/getLoginUser")
    public ResponseBeanVo<?> userRegister(@RequestBody UserRegisterRequest registerRequest) {
        if (ObjectUtils.isNull(registerRequest)) {
            throw new BizException(ErrorCode.PARAM_ERROR, "参数为空");
        }
        String userAccount = registerRequest.getUserAccount();
        String userPassword = registerRequest.getUserPassword();
        String checkPassword = registerRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BizException(ErrorCode.PARAM_ERROR, "参数为空");
        }
        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResponseBeanVo.ok(result);
    }

    @PostMapping("/login")
    public ResponseBeanVo<?> getLoginUser(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request) {
        if (ObjectUtils.isNull(loginRequest)) {
            throw new BizException(ErrorCode.PARAM_ERROR, "参数为空");
        }
        String userAccount = loginRequest.getUserAccount();
        String userPassword = loginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BizException(ErrorCode.PARAM_ERROR, "参数为空");
        }
        UserVo userVo = userService.getLoginUser(userAccount, userPassword, request);
        return userVo != null ? ResponseBeanVo.ok(userVo) : ResponseBeanVo.error(ErrorCode.REQUEST_ERROR, null);
    }

    @PostMapping("/getUserId")
    public ResponseBeanVo<?> getUserId(@RequestParam String code) {
        String userId = userService.getUserId(code);
        return StringUtils.isNotBlank(userId) ? ResponseBeanVo.ok(userId) : ResponseBeanVo.error(ErrorCode.PARAM_ERROR, null);
    }

    @PostMapping("/getRoomId")
    public ResponseBeanVo<?> getRoomId(@RequestParam String username) {
        String userId = userService.getRoomId(username);
        return StringUtils.isNotBlank(userId) ? ResponseBeanVo.ok(userId) : ResponseBeanVo.error(ErrorCode.PARAM_ERROR, null);
    }

    @PostMapping("/updateUserPhone")
    public ResponseBeanVo<?> updateUserPhone(@RequestParam String userId, @RequestParam String phone) {
        boolean b = userService.updateUserPhone(userId, phone);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.PHONE_NOT_EXIST, "手机号不存在");
    }

    @PostMapping("/updateUserProfile")
    public ResponseBeanVo<?> updateUserProfile(@RequestParam String userId, @RequestParam String gender, @RequestParam String avatar) {
        boolean b = userService.updateUserProfile(userId, gender, avatar);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.REQUEST_ERROR, "更新失败");
    }

    @PostMapping("/updateUserName")
    public ResponseBeanVo<?> updateUserName(@RequestParam String userId, @RequestParam String name) {
        boolean b = userService.updateUserName(userId, name);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.REQUEST_ERROR, "更新失败");
    }

    @PostMapping("/updateRoomId")
    public ResponseBeanVo<?> updateRoomId(@RequestParam String userId, @RequestParam String roomId) {
        boolean b = userService.updateRoomId(userId, roomId);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.REQUEST_ERROR, "更新失败");
    }

    @PutMapping("/insertUserProfile")
    public ResponseBeanVo<?> insertUserProfile(@RequestBody UserQueryRequest userQueryRequest, @RequestParam String nickname,
                                               @RequestParam String roomId, @RequestParam Date date) {
        boolean b = userService.insertUserProfile(userQueryRequest.getUserName(), userQueryRequest.getGender(),
                nickname, userQueryRequest.getUserAvatar(), roomId, date);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.REQUEST_ERROR,null);
    }
}
