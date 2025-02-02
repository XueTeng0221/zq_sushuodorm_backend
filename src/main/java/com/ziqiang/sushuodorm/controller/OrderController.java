package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.entity.dto.item.FetchQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderAddRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderUpdateRequest;
import com.ziqiang.sushuodorm.entity.vo.FetchVo;
import com.ziqiang.sushuodorm.entity.vo.OrderVo;
import com.ziqiang.sushuodorm.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequestMapping("/order")
@RestController
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public boolean save(@RequestBody OrderAddRequest orderAddRequest) {
        return orderService.save(orderAddRequest.getOrderId(),
                orderAddRequest.getUserId(),
                orderAddRequest.getRoomId(),
                orderAddRequest.getToDormId(),
                orderAddRequest.getTitle(),
                orderAddRequest.getDescription());
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam String orderId, @RequestParam String userId) {
        return orderService.remove(orderId, userId);
    }

    @PostMapping("/update")
    @RequestMapping("/{userId}")
    public boolean update(@PathVariable("userId") String userId,
                          String orderId, String title, String description, @RequestBody OrderUpdateRequest orderUpdateRequest) {
        return orderService.update(userId, orderId, title, description, orderUpdateRequest);
    }

    @PostMapping("/get")
    public List<OrderVo> getOrdersByUserId(@RequestParam @NotNull String userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @PostMapping("/getOrdersByRoomId")
    @RequestMapping("/{roomId}")
    public List<OrderVo> getOrdersByRoomId(@PathVariable("roomId") String roomId, @RequestBody OrderQueryRequest orderQueryRequest) {
        return orderService.getOrdersByRoomId(roomId, orderQueryRequest).getRecords();
    }

    @PostMapping("/getOrdersByFromDorm")
    @RequestMapping("/{fromDormId}")
    public List<OrderVo> getOrdersByFromDorm(@PathVariable("fromDormId") String fromDormId, @RequestBody OrderQueryRequest orderQueryRequest) {
        return orderService.getOrdersByFromDorm(fromDormId, orderQueryRequest).getRecords();
    }

    @PostMapping("/getOrdersByToDorm")
    @RequestMapping("/{toDormId}")
    public List<OrderVo> getOrdersByToDorm(@PathVariable("toDormId") String toDormId, @RequestBody OrderQueryRequest orderQueryRequest) {
        return orderService.getOrdersByToDorm(toDormId, orderQueryRequest).getRecords();
    }

    @PostMapping("/getOrdersByKeywords")
    public List<OrderVo> getOrdersByKeywords(@RequestParam @NotNull List<String> keywords, @RequestBody OrderQueryRequest orderQueryRequest) {
        return orderService.getOrdersByKeywords(keywords, orderQueryRequest).getRecords();
    }

    @PostMapping("/getOrdersByOccupants")
    public List<OrderVo> getOrdersByOccupants(@RequestParam @NotNull List<String> occupants, @RequestBody OrderQueryRequest orderQueryRequest) {
        return orderService.getOrdersByOccupants(occupants, orderQueryRequest).getRecords();
    }

    @PostMapping("/getFetchesByOrderId")
    @RequestMapping("/{orderId}")
    public List<FetchVo> getFetchesByOrderId(@PathVariable("orderId") String orderId, @RequestAttribute FetchQueryRequest orderQueryRequest) {
        return orderService.getFetchesByOrderId(orderId, orderQueryRequest).getRecords();
    }
}
