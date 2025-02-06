package com.ziqiang.sushuodorm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.dto.favorite.FavoriteQueryRequest;
import com.ziqiang.sushuodorm.entity.item.FavoriteItem;
import com.ziqiang.sushuodorm.entity.vo.ResponseBeanVo;
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
    public ResponseBeanVo<?> save(@RequestParam String userId, @RequestParam Long postId) {
        boolean b = favoriteService.save(userId, postId);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @DeleteMapping("/remove")
    public ResponseBeanVo<?> remove(@RequestParam String userId, @RequestParam Long postId) {
        boolean b = favoriteService.remove(userId, postId);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PutMapping("/update")
    public ResponseBeanVo<?> update(@RequestParam String userId, @RequestParam Long postId) {
        boolean b = favoriteService.update(userId, postId);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @GetMapping("/getPage")
    public ResponseBeanVo<IPage<FavoriteItem>> getPage(@RequestParam String userId,
                                                       @RequestBody FavoriteQueryRequest favoriteQueryRequest) {
        return ResponseBeanVo.ok(favoriteService.getPage(userId,
                favoriteQueryRequest.getCurrentId(), favoriteQueryRequest.getPageSize()));
    }

    @GetMapping("/searchByPostId")
    public ResponseBeanVo<IPage<FavoriteItem>> searchByPostId(@RequestParam String userId, @RequestParam Long postId,
                                              @RequestBody FavoriteQueryRequest favoriteQueryRequest) {
        return ResponseBeanVo.ok(favoriteService.searchByPostId(userId, postId,
                favoriteQueryRequest.getCurrentId(), favoriteQueryRequest.getPageSize()));
    }
}