package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.entity.dto.item.FetchQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderUpdateRequest;
import com.ziqiang.sushuodorm.entity.vo.FetchVo;
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
    public boolean save(@RequestParam String fetchId, @RequestParam String userId,
                        @RequestParam String roomId, @RequestParam String description) {
        return fetchService.save(fetchId, userId, roomId, description);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam String fetchId, @RequestParam String userId) {
        return fetchService.remove(fetchId, userId);
    }

    @PostMapping("/update")
    public boolean update(@RequestParam String userId, @RequestParam String fetchId,
                          @RequestParam String description, @RequestBody OrderUpdateRequest orderUpdateRequest) {
        return fetchService.update(userId, fetchId, description, orderUpdateRequest);
    }

    @PostMapping("/getFetches")
    public List<FetchVo> getFetchesByUserId(@RequestParam String userId) {
        return fetchService.getFetchesByUserId(userId);
    }

    @PostMapping("/getFetchesByRoomId")
    public List<FetchVo> getFetchesByRoomId(@RequestParam String roomId,
                                            @RequestAttribute FetchQueryRequest fetchQueryRequest) {
        return fetchService.getFetchesByRoomId(roomId, fetchQueryRequest).getRecords();
    }

    @PostMapping("/getFetchesByFromDorm")
    public List<FetchVo> getFetchesByFromDorm(@RequestParam String fromDormId,
                                              @RequestAttribute FetchQueryRequest fetchQueryRequest) {
        return fetchService.getFetchesByFromDorm(fromDormId, fetchQueryRequest).getRecords();
    }

    @PostMapping("/getFetchesByToDorm")
    public List<FetchVo> getFetchesByToDorm(@RequestParam String toDormId,
                                            @RequestAttribute FetchQueryRequest fetchQueryRequest) {
        return fetchService.getFetchesByToDorm(toDormId, fetchQueryRequest).getRecords();
    }
}
