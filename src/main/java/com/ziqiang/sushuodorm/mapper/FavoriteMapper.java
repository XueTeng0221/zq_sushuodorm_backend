package com.ziqiang.sushuodorm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.entity.item.FavoriteItem;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.function.Function;

@Mapper
public interface FavoriteMapper extends BaseMapper<FavoriteItem> {
    boolean exists(Wrapper<FavoriteItem> queryWrapper);

    IPage<PostItem> listFavoritePostByPage(IPage<PostItem> page, @Param("ew") Wrapper<PostItem> queryWrapper);

    List<FavoriteItem> listObjs();

    List<FavoriteItem> listObjs(@Param("ew") Wrapper<FavoriteItem> queryWrapper);

    List<FavoriteItem> listObjs(@Param("ew") Wrapper<FavoriteItem> queryWrapper, Function<? super Object, ?> mapper);
}
