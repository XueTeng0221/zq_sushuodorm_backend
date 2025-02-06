package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import com.ziqiang.sushuodorm.entity.vo.ResponseBeanVo;
import com.ziqiang.sushuodorm.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequestMapping("/post")
@RestController
@Slf4j
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/insert")
    public ResponseBeanVo<?> insert(@RequestParam String title) {
        boolean b = postService.insertPost(title);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PostMapping("/insertWithTags")
    public ResponseBeanVo<?> insertWithTags(@RequestParam String title, @RequestParam List<String> tags) {
        boolean b = postService.insertPost(title, tags);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PostMapping("/update")
    public ResponseBeanVo<?> update(@RequestBody @NotNull PostItem postItem) {
        boolean b = postService.updatePost(postItem);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PostMapping("/remove")
    public ResponseBeanVo<?> remove(@RequestBody @NotNull PostItem postItem) {
        boolean b = postService.removePost(postItem);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }
}
