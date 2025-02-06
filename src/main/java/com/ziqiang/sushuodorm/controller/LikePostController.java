package com.ziqiang.sushuodorm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.common.PageRequest;
import com.ziqiang.sushuodorm.entity.item.LikePostItem;
import com.ziqiang.sushuodorm.entity.vo.ResponseBeanVo;
import com.ziqiang.sushuodorm.services.LikePostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("like")
@Slf4j
public class LikePostController {
    @Autowired
    private LikePostService likePostService;

    @Operation(summary = "点赞帖子")
    @RequestMapping("likePost")
    public ResponseBeanVo<?> likePost(@RequestParam String userId, @RequestBody Long postId) {
        return ResponseBeanVo.ok(likePostService.save(userId, postId));
    }

    @Operation(summary = "取消点赞帖子")
    @RequestMapping("unlikePost")
    public ResponseBeanVo<?> unlikePost(@RequestParam String userId, @RequestBody Long postId) {
        return ResponseBeanVo.ok(likePostService.remove(userId, postId));
    }

    @Operation(summary = "获取点赞列表")
    @RequestMapping("getLikePostList")
    public ResponseBeanVo<IPage<LikePostItem>> getLikePostList(@RequestParam String userId, @RequestBody @Validated PageRequest pageRequest) {
        return ResponseBeanVo.ok(likePostService.getPage(userId, pageRequest.getPageSize(), pageRequest.getCurrentId()));
    }
}
