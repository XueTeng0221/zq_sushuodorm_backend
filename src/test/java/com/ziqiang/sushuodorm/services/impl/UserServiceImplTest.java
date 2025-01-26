package com.ziqiang.sushuodorm.services.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ziqiang.sushuodorm.entity.item.RoomItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.mapper.RoomMapper;
import com.ziqiang.sushuodorm.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        // 在每个测试之前设置模拟对象
    }

    @Test
    public void getUserId_UnionIdExists_ReturnsUserName() {
        String code = "unionId123";
        UserItem userItem = new UserItem().setUserName("JohnDoe");
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(userItem);

        String result = userService.getUserId(code);

        assertEquals("JohnDoe", result);
    }

    @Test
    public void getUserId_UnionIdDoesNotExist_ReturnsNull() {
        String code = "nonExistentUnionId";
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);

        String result = userService.getUserId(code);

        assertNull(result);
    }

    @Test
    public void getRoomId_UserIdExists_ReturnsRoomId() {
        String username = "user123";
        UserItem userItem = new UserItem().setRoomId("room123");
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(userItem);

        String result = userService.getRoomId(username);

        assertEquals("room123", result);
    }

    @Test
    public void getRoomId_UserIdDoesNotExist_ReturnsNull() {
        String username = "nonExistentUser";
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);

        String result = userService.getRoomId(username);

        assertNull(result);
    }

    @Test
    public void updateUserPhone_Success_ReturnsTrue() {
        String userId = "user123";
        String phone = "1234567890";
        when(userMapper.update(any(UserItem.class), any(UpdateWrapper.class))).thenReturn(1);

        boolean result = userService.updateUserPhone(userId, phone);

        assertTrue(result);
    }

    @Test
    public void updateUserPhone_Failure_ReturnsFalse() {
        String userId = "user123";
        String phone = "1234567890";
        when(userMapper.update(any(UserItem.class), any(UpdateWrapper.class))).thenReturn(0);

        boolean result = userService.updateUserPhone(userId, phone);

        assertFalse(result);
    }

    @Test
    public void updateUserProfile_Success_ReturnsTrue() {
        String userId = "user123";
        String gender = "male";
        String avatar = "avatarUrl";
        when(userMapper.update(any(UserItem.class), any(UpdateWrapper.class))).thenReturn(1);

        boolean result = userService.updateUserProfile(userId, gender, avatar);

        assertTrue(result);
    }

    @Test
    public void updateUserProfile_Failure_ReturnsFalse() {
        String userId = "user123";
        String gender = "male";
        String avatar = "avatarUrl";
        when(userMapper.update(any(UserItem.class), any(UpdateWrapper.class))).thenReturn(0);

        boolean result = userService.updateUserProfile(userId, gender, avatar);

        assertFalse(result);
    }

    @Test
    public void updateUserName_Success_ReturnsTrue() {
        String userId = "user123";
        String name = "JohnDoe";
        when(userMapper.update(any(UserItem.class), any(UpdateWrapper.class))).thenReturn(1);

        boolean result = userService.updateUserName(userId, name);

        assertTrue(result);
    }

    @Test
    public void updateUserName_Failure_ReturnsFalse() {
        String userId = "user123";
        String name = "JohnDoe";
        when(userMapper.update(any(UserItem.class), any(UpdateWrapper.class))).thenReturn(0);

        boolean result = userService.updateUserName(userId, name);

        assertFalse(result);
    }

    @Test
    public void updateRoomId_UserAlreadyInRoom_ReturnsFalse() {
        String userId = "user123";
        String roomId = "room123";
        UserItem userItem = new UserItem().setRoomId(roomId);
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(userItem);

        boolean result = userService.updateRoomId(userId, roomId);

        assertFalse(result);
    }

    @Test
    public void updateRoomId_RoomDoesNotExist_InsertsNewRoom() {
        String userId = "user123";
        String roomId = "newRoom";
        UserItem userItem = new UserItem().setRoomId("oldRoom").setUserName("JohnDoe");
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(userItem);
        when(roomMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(roomMapper.insert(any(RoomItem.class))).thenReturn(1);

        boolean result = userService.updateRoomId(userId, roomId);

        assertTrue(result);
    }

    @Test
    public void updateRoomId_RoomFull_DoesNotUpdateUser() {
        String userId = "user123";
        String roomId = "fullRoom";
        UserItem userItem = new UserItem().setRoomId("oldRoom").setUserName("JohnDoe");
        RoomItem roomItem = new RoomItem().setRoomId(Integer.parseInt(roomId)).setCapacity(1);
        Map<String, UserItem> occupants = new HashMap<>();
        occupants.put("existingUser", new UserItem());
        roomItem.setOccupants(occupants);
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(userItem);
        when(roomMapper.selectOne(any(QueryWrapper.class))).thenReturn(roomItem);

        boolean result = userService.updateRoomId(userId, roomId);

        assertFalse(result);
    }

    @Test
    public void updateRoomId_RoomExistsAndNotFull_UpdatesUser() {
        String userId = "user123";
        String roomId = "room123";
        UserItem userItem = new UserItem().setRoomId("oldRoom").setUserName("JohnDoe");
        RoomItem roomItem = new RoomItem().setRoomId(Integer.parseInt(roomId)).setCapacity(2);
        Map<String, UserItem> occupants = new HashMap<>();
        occupants.put("existingUser", new UserItem());
        roomItem.setOccupants(occupants);
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(userItem);
        when(roomMapper.selectOne(any(QueryWrapper.class))).thenReturn(roomItem);
        when(userMapper.update(any(UserItem.class), any(UpdateWrapper.class))).thenReturn(1);
        when(roomMapper.update(any(RoomItem.class), any(UpdateWrapper.class))).thenReturn(1);

        boolean result = userService.updateRoomId(userId, roomId);

        assertTrue(result);
    }

    @Test
    public void insertUserProfile_Success_ReturnsTrue() {
        String gender = "male";
        String nickname = "JohnDoe";
        String avatar = "avatarUrl";
        when(userMapper.insert(any(UserItem.class))).thenReturn(1);

        boolean result = userService.insertUserProfile(gender, nickname, avatar);

        assertTrue(result);
    }

    @Test
    public void insertUserProfile_Failure_ReturnsFalse() {
        String gender = "male";
        String nickname = "JohnDoe";
        String avatar = "avatarUrl";
        when(userMapper.insert(any(UserItem.class))).thenReturn(0);

        boolean result = userService.insertUserProfile(gender, nickname, avatar);

        assertFalse(result);
    }
}
