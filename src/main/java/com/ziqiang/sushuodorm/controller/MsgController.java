package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.item.MsgItem;
import com.ziqiang.sushuodorm.entity.vo.ResponseBeanVo;
import com.ziqiang.sushuodorm.services.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/msg")
@Slf4j
public class MsgController {
    @Autowired
    private MsgService msgService;

    @PostMapping("/save")
    @RequestMapping("/{userId}")
    public ResponseBeanVo<?> save(@PathVariable("userId") String userId, @RequestParam String content) {
        boolean b = msgService.saveItem(userId, content);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PostMapping("/get")
    @RequestMapping("/{userId}")
    public ResponseBeanVo<List<MsgItem>> getMsgList(@PathVariable("userId") String userId) {
        return ResponseBeanVo.ok(msgService.getMsgList(userId));
    }

    @PostMapping("/page")
    @RequestMapping("/{userId}")
    public ResponseBeanVo<List<MsgItem>> getPage(@PathVariable("userId") String userId,
                                 @RequestParam int currentPage, @RequestParam int pageSize) {
        return ResponseBeanVo.ok(msgService.getPage(userId, currentPage, pageSize).getRecords());
    }
}
