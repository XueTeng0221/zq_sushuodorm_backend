package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.item.LikePostItem;
import com.ziqiang.sushuodorm.mapper.LikePostMapper;
import com.ziqiang.sushuodorm.services.LikePostService;
import org.springframework.stereotype.Service;

@Service
public class LikePostServiceImpl extends ServiceImpl<LikePostMapper, LikePostItem> implements LikePostService {
}
