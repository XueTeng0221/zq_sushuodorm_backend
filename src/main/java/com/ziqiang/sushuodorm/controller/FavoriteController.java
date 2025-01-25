package com.ziqiang.sushuodorm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.entity.item.FavoriteItem;
import com.ziqiang.sushuodorm.services.FavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
@Slf4j
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/save")
    public boolean save(@RequestParam String userId, @RequestParam Long postId) {
        return favoriteService.save(userId, postId);
    }

    @DeleteMapping("/remove")
    public boolean remove(@RequestParam String userId, @RequestParam Long postId) {
        return favoriteService.remove(userId, postId);
    }

    @PutMapping("/update")
    public boolean update(@RequestParam String userId, @RequestParam Long postId) {
        return favoriteService.update(userId, postId);
    }

    @GetMapping("/getPage")
    public IPage<FavoriteItem> getPage(@RequestParam String userId, @RequestParam int pageNum, @RequestParam int pageId) {
        return favoriteService.getPage(userId, pageNum, pageId);
    }

    @GetMapping("/searchByPostId")
    public IPage<FavoriteItem> searchByPostId(@RequestParam String userId, @RequestParam Long postId,
                                              @RequestParam int pageNum, @RequestParam int pageId) {
        return favoriteService.searchByPostId(userId, postId, pageNum, pageId);
    }
}