package com.ziqiang.sushuodorm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserItem> {
    UserItem getUserById(String userId);

    List<UserItem> getOccupantsByRoomId(String roomId);

    List<UserItem> getUsersByDormId(String dormName);
}
