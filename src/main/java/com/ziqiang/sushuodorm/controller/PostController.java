package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.entity.item.PostItem;
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
    public boolean insert(@RequestParam String title) {
        return postService.insertPost(title);
    }

    @PostMapping("/insertWithTags")
    public boolean insertWithTags(@RequestParam String title, @RequestParam List<String> tags) {
        return postService.insertPost(title, tags);
    }

    @PostMapping("/update")
    public boolean update(@RequestBody @NotNull PostItem postItem) {
        return postService.updatePost(postItem);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestBody @NotNull PostItem postItem) {
        return postService.removePost(postItem);
    }
}
