package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.entity.item.MsgItem;
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
    public boolean save(@PathVariable("userId") String userId, @RequestParam String content) {
        return msgService.saveItem(userId, content);
    }

    @PostMapping("/get")
    @RequestMapping("/{userId}")
    public List<MsgItem> getMsgList(@PathVariable("userId") String userId) {
        return msgService.getMsgList(userId);
    }

    @PostMapping("/page")
    @RequestMapping("/{userId}")
    public List<MsgItem> getPage(@PathVariable("userId") String userId,
                                 @RequestParam int currentPage, @RequestParam int pageSize) {
        return msgService.getPage(userId, currentPage, pageSize).getRecords();
    }
}
