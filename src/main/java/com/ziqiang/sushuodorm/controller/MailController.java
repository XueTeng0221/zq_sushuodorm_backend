package com.ziqiang.sushuodorm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.entity.dto.mail.MailQueryRequest;
import com.ziqiang.sushuodorm.entity.item.MailItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.entity.vo.UserVo;
import com.ziqiang.sushuodorm.services.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mail")
@Slf4j
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/save")
    public boolean save(@RequestParam String userId, @RequestBody Map<String, UserItem> receivers) {
        return mailService.save(userId, receivers);
    }

    @PostMapping("/reply")
    public boolean reply(@RequestParam String userId, @RequestBody Map<String, UserItem> receivers) {
        return mailService.reply(userId, receivers.values().stream().toList());
    }

    @DeleteMapping("/remove")
    public boolean remove(@RequestParam String userId) {
        return mailService.remove(userId);
    }

    @PutMapping("/update")
    public boolean update(@RequestParam String postId, @RequestParam String title, @RequestParam String subject) {
        return mailService.update(postId, title, subject);
    }

    @GetMapping("/getItemByUsername")
    public IPage<MailItem> getItemByUsername(@RequestParam String username, @RequestBody MailQueryRequest queryRequest) {
        return mailService.getItemByUsername(username, queryRequest);
    }

    @GetMapping("/getReceiverByUsername")
    public IPage<UserVo> getReceiverByUsername(@RequestParam String username, @RequestParam int currentSize, @RequestParam int pageSize) {
        return mailService.getReceiverByUsername(username, currentSize, pageSize);
    }
}