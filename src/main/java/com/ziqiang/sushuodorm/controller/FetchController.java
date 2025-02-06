package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.dto.item.FetchQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderUpdateRequest;
import com.ziqiang.sushuodorm.entity.vo.FetchVo;
import com.ziqiang.sushuodorm.entity.vo.ResponseBeanVo;
import com.ziqiang.sushuodorm.services.FetchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/fetch")
@RestController
@Slf4j
public class FetchController {
    @Autowired
    private FetchService fetchService;

    @PostMapping("/save")
    public ResponseBeanVo<?> save(@RequestParam String fetchId, @RequestParam String userId,
                               @RequestParam String roomId, @RequestParam String description) {
        boolean b = fetchService.save(fetchId, userId, roomId, description);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PostMapping("/remove")
    public ResponseBeanVo<?> remove(@RequestParam String fetchId, @RequestParam String userId) {
        boolean b = fetchService.remove(fetchId, userId);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PostMapping("/update")
    public ResponseBeanVo<?> update(@RequestParam String userId, @RequestParam String fetchId,
                          @RequestParam String description, @RequestBody OrderUpdateRequest orderUpdateRequest) {
        boolean b = fetchService.update(userId, fetchId, description, orderUpdateRequest);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PostMapping("/getFetches")
    public ResponseBeanVo<List<FetchVo>> getFetchesByUserId(@RequestParam String userId) {
        return ResponseBeanVo.ok(fetchService.getFetchesByUserId(userId));
    }

    @PostMapping("/getFetchesByRoomId")
    public ResponseBeanVo<List<FetchVo>> getFetchesByRoomId(@RequestParam String roomId,
                                            @RequestBody FetchQueryRequest fetchQueryRequest) {
        return ResponseBeanVo.ok(fetchService.getFetchesByRoomId(roomId, fetchQueryRequest).getRecords());
    }

    @PostMapping("/getFetchesByFromDorm")
    public ResponseBeanVo<List<FetchVo>> getFetchesByFromDorm(@RequestParam String fromDormId,
                                              @RequestBody FetchQueryRequest fetchQueryRequest) {
        return ResponseBeanVo.ok(fetchService.getFetchesByFromDorm(fromDormId, fetchQueryRequest).getRecords());
    }

    @PostMapping("/getFetchesByToDorm")
    public ResponseBeanVo<List<FetchVo>> getFetchesByToDorm(@RequestParam String toDormId,
                                            @RequestAttribute FetchQueryRequest fetchQueryRequest) {
        return ResponseBeanVo.ok(fetchService.getFetchesByToDorm(toDormId, fetchQueryRequest).getRecords());
    }
}
