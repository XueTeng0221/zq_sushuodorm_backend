package com.ziqiang.sushuodorm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.entity.item.LikeCommentItem;
import com.ziqiang.sushuodorm.services.LikeCommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("点赞评论")
@Slf4j
public class LikeCommentController {
    @Autowired
    private LikeCommentService likeCommentService;

    @PostMapping("点赞")
    public boolean likeComment(@RequestParam String userId, @RequestParam Long commentId) {
        return likeCommentService.save(userId, commentId);
    }

    @PostMapping("取消点赞")
    public boolean unlikeComment(@RequestParam String userId, @RequestParam Long commentId) {
        return likeCommentService.remove(userId, commentId);
    }

    @PostMapping("获取点赞列表")
    public IPage<LikeCommentItem> getLikeCommentList(@RequestParam String userId,
                                                     @RequestParam int pageNum, @RequestParam int pageId) {
        return likeCommentService.getPage(userId, pageNum, pageId);
    }
}
