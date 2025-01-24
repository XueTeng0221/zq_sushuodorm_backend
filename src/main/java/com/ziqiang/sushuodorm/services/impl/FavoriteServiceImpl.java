package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.item.FavoriteItem;
import com.ziqiang.sushuodorm.mapper.FavoriteMapper;
import com.ziqiang.sushuodorm.services.FavoriteService;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, FavoriteItem> implements FavoriteService {
}
