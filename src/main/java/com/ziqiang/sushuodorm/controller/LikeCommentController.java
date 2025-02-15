package com.ziqiang.sushuodorm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.common.PageRequest;
import com.ziqiang.sushuodorm.entity.item.LikeCommentItem;
import com.ziqiang.sushuodorm.entity.vo.ResponseBeanVo;
import com.ziqiang.sushuodorm.services.LikeCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("likeComment")
@Slf4j
public class LikeCommentController {
    @Autowired
    private LikeCommentService likeCommentService;

    @PostMapping("likeComment")
    public ResponseBeanVo<?> likeComment(@RequestParam String userId, @RequestParam Long commentId) {
        boolean b = likeCommentService.save(userId, commentId);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PostMapping("unlikeComment")
    public ResponseBeanVo<?> unlikeComment(@RequestParam String userId, @RequestParam Long commentId) {
        boolean b = likeCommentService.remove(userId, commentId);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PostMapping("getLikeList")
    public ResponseBeanVo<IPage<LikeCommentItem>> getLikeCommentList(@RequestParam String userId,
                                                     @RequestBody @Validated PageRequest pageRequest) {
        return ResponseBeanVo.ok(likeCommentService.getPage(userId, pageRequest.getCurrentId(), pageRequest.getPageSize()));
    }
}
