package com.ziqiang.sushuodorm.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.entity.vo.ResponseBeanVo;
import com.ziqiang.sushuodorm.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping("/getLoginUser")
    public ResponseBeanVo<?> getLoginUser(@RequestBody HttpServletRequest request) {
        UserItem userItem = userService.getLoginUser(request);
        return ObjectUtils.isNotNull(userItem) ? ResponseBeanVo.ok(userItem) : ResponseBeanVo.error(ErrorCode.LOGIN_FAILED, null);
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
    public ResponseBeanVo<?> insertUserProfile(@RequestParam String gender, @RequestParam String nickname, @RequestParam String avatar) {
        boolean b = userService.insertUserProfile(gender, nickname, avatar);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.REQUEST_ERROR,null);
    }
}
