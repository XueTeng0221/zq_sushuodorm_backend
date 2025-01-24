package com.ziqiang.sushuodorm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.entity.item.FavoriteItem;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.function.Function;

public interface FavoriteMapper extends BaseMapper<FavoriteItem> {
    IPage<PostItem> listFavoritePostByPage(IPage<PostItem> page, @Param("ew") Wrapper<PostItem> queryWrapper);

    boolean exists(Wrapper<FavoriteItem> queryWrapper);

    long count();

    long count(Wrapper<FavoriteItem> queryWrapper);

    List<FavoriteItem> list(Wrapper<FavoriteItem> queryWrapper);

    List<FavoriteItem> list(IPage<FavoriteItem> page, Wrapper<FavoriteItem> queryWrapper);

    List<FavoriteItem> list();

    List<FavoriteItem> list(IPage<FavoriteItem> page);

    List<FavoriteItem> listObjs(Wrapper<FavoriteItem> queryWrapper);

    List<FavoriteItem> listObjs(Wrapper<FavoriteItem> queryWrapper, Function<? super Object, ?> mapper);

    List<FavoriteItem> listObjs();
}
