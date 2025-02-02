package com.ziqiang.sushuodorm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.entity.item.LikePostItem;
import com.ziqiang.sushuodorm.services.LikePostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("点赞")
@Slf4j
public class LikePostController {
    @Autowired
    private LikePostService likePostService;

    @RequestMapping("点赞帖子")
    public boolean likePost(@RequestParam String userId, @RequestBody Long postId) {
        return likePostService.save(userId, postId);
    }

    @RequestMapping("取消点赞帖子")
    public boolean unlikePost(@RequestParam String userId, @RequestBody Long postId) {
        return likePostService.remove(userId, postId);
    }

    @RequestMapping("获取点赞列表")
    public IPage<LikePostItem> getLikePostList(@RequestParam String userId, int pageNum, int pageSize) {
        return likePostService.getPage(userId, pageNum, pageSize);
    }
}
