package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.dto.item.FetchQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderAddRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.item.OrderUpdateRequest;
import com.ziqiang.sushuodorm.entity.vo.FetchVo;
import com.ziqiang.sushuodorm.entity.vo.OrderVo;
import com.ziqiang.sushuodorm.entity.vo.ResponseBeanVo;
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
    public ResponseBeanVo<?> save(@RequestBody OrderAddRequest orderAddRequest) {
        boolean b = orderService.save(orderAddRequest.getOrderId(),
                orderAddRequest.getUserId(),
                orderAddRequest.getRoomId(),
                orderAddRequest.getToDormId(),
                orderAddRequest.getTitle(),
                orderAddRequest.getDescription());
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PostMapping("/remove")
    public ResponseBeanVo<?> remove(@RequestParam String orderId, @RequestParam String userId) {
        boolean b = orderService.remove(orderId, userId);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PostMapping("/update")
    @RequestMapping("/{userId}")
    public ResponseBeanVo<?> update(@PathVariable("userId") String userId, @RequestParam String orderId,
                          @RequestParam String title, @RequestParam String description,
                          @RequestBody OrderUpdateRequest orderUpdateRequest) {
        boolean b = orderService.update(userId, orderId, title, description, orderUpdateRequest);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @PostMapping("/get")
    public ResponseBeanVo<List<OrderVo>> getOrdersByUserId(@RequestParam @NotNull String userId) {
        return ResponseBeanVo.ok(orderService.getOrdersByUserId(userId));
    }

    @PostMapping("/getOrdersByRoomId")
    @RequestMapping("/{roomId}")
    public ResponseBeanVo<List<OrderVo>> getOrdersByRoomId(@PathVariable("roomId") String roomId,
                                           @RequestBody OrderQueryRequest orderQueryRequest) {
        return ResponseBeanVo.ok(orderService.getOrdersByRoomId(roomId, orderQueryRequest).getRecords());
    }

    @PostMapping("/getOrdersByFromDorm")
    @RequestMapping("/{fromDormId}")
    public ResponseBeanVo<List<OrderVo>> getOrdersByFromDorm(@PathVariable("fromDormId") String fromDormId,
                                             @RequestBody OrderQueryRequest orderQueryRequest) {
        return ResponseBeanVo.ok(orderService.getOrdersByFromDorm(fromDormId, orderQueryRequest).getRecords());
    }

    @PostMapping("/getOrdersByToDorm")
    @RequestMapping("/{toDormId}")
    public ResponseBeanVo<List<OrderVo>> getOrdersByToDorm(@PathVariable("toDormId") String toDormId,
                                           @RequestBody OrderQueryRequest orderQueryRequest) {
        return ResponseBeanVo.ok(orderService.getOrdersByToDorm(toDormId, orderQueryRequest).getRecords());
    }

    @PostMapping("/getOrdersByKeywords")
    public ResponseBeanVo<List<OrderVo>> getOrdersByKeywords(@RequestParam @NotNull List<String> keywords,
                                             @RequestBody OrderQueryRequest orderQueryRequest) {
        return ResponseBeanVo.ok(orderService.getOrdersByKeywords(keywords, orderQueryRequest).getRecords());
    }

    @PostMapping("/getOrdersByOccupants")
    public ResponseBeanVo<List<OrderVo>> getOrdersByOccupants(@RequestParam @NotNull List<String> occupants,
                                              @RequestBody OrderQueryRequest orderQueryRequest) {
        return ResponseBeanVo.ok(orderService.getOrdersByOccupants(occupants, orderQueryRequest).getRecords());
    }

    @PostMapping("/getFetchesByOrderId")
    @RequestMapping("/{orderId}")
    public ResponseBeanVo<List<FetchVo>> getFetchesByOrderId(@PathVariable("orderId") String orderId,
                                             @RequestAttribute FetchQueryRequest orderQueryRequest) {
        return ResponseBeanVo.ok(orderService.getFetchesByOrderId(orderId, orderQueryRequest).getRecords());
    }
}
