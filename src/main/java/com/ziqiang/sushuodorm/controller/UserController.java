package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.entity.item.UserItem;
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
    public UserItem getLoginUser(@RequestBody HttpServletRequest request) {
        return userService.getLoginUser(request);
    }

    @PostMapping("/getUserId")
    public String getUserId(@RequestParam String code) {
        return userService.getUserId(code);
    }

    @PostMapping("/getRoomId")
    public String getRoomId(@RequestParam String username) {
        return userService.getRoomId(username);
    }

    @PostMapping("/updateUserPhone")
    public boolean updateUserPhone(@RequestParam String userId, @RequestParam String phone) {
        return userService.updateUserPhone(userId, phone);
    }

    @PostMapping("/updateUserProfile")
    public boolean updateUserProfile(@RequestParam String userId, @RequestParam String gender, @RequestParam String avatar) {
        return userService.updateUserProfile(userId, gender, avatar);
    }

    @PostMapping("/updateUserName")
    public boolean updateUserName(@RequestParam String userId, @RequestParam String name) {
        return userService.updateUserName(userId, name);
    }

    @PostMapping("/updateRoomId")
    public boolean updateRoomId(@RequestParam String userId, @RequestParam String roomId) {
        return userService.updateRoomId(userId, roomId);
    }

    @PutMapping("/insertUserProfile")
    public boolean insertUserProfile(@RequestParam String gender, @RequestParam String nickname, @RequestParam String avatar) {
        return userService.insertUserProfile(gender, nickname, avatar);
    }
}
