package com.ziqiang.sushuodorm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.dto.mail.MailQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.user.UserQueryRequest;
import com.ziqiang.sushuodorm.entity.item.MailItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.entity.vo.ResponseBeanVo;
import com.ziqiang.sushuodorm.entity.vo.UserVo;
import com.ziqiang.sushuodorm.services.MailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mail")
@Slf4j
public class MailController {
    @Autowired
    private MailService mailService;

    @Operation(summary = "发送邮件")
    @PostMapping("/save")
    public ResponseBeanVo<?> save(@RequestParam String userId, @RequestBody List<UserItem> receivers) {
        boolean b = mailService.save(userId, receivers.stream().collect(Collectors.toMap(
                UserItem::getUserName, userItem -> userItem, (i1, i2) -> i1, HashMap::new)));
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @Operation(summary = "回复邮件")
    @PostMapping("/reply")
    public ResponseBeanVo<?> reply(@RequestParam String userId, @RequestBody Map<String, UserItem> receivers) {
        boolean b = mailService.reply(userId, receivers.values().stream().toList());
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @Operation(summary = "删除邮件")
    @DeleteMapping("/remove")
    public ResponseBeanVo<?> remove(@RequestParam String userId) {
        boolean b = mailService.remove(userId);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @Operation(summary = "更新邮件")
    @PutMapping("/update")
    public ResponseBeanVo<?> update(@RequestParam String postId, @RequestParam String title, @RequestParam String subject) {
        boolean b = mailService.update(postId, title, subject);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @Operation(summary = "获取邮件")
    @GetMapping("/getItemByUsername")
    public ResponseBeanVo<IPage<MailItem>> getItemByUsername(@RequestParam String username, @RequestBody @Validated MailQueryRequest queryRequest) {
        return ResponseBeanVo.ok(mailService.getItemByUsername(username, queryRequest));
    }

    @Operation(summary = "获取邮件接收者")
    @GetMapping("/getReceiverByUsername")
    public ResponseBeanVo<IPage<UserVo>> getReceiverByUsername(@RequestParam String username, @RequestBody @Validated UserQueryRequest userQueryRequest) {
        return ResponseBeanVo.ok(mailService.getReceiverByUsername(username, userQueryRequest.getCurrentId(), userQueryRequest.getPageSize()));
    }
}