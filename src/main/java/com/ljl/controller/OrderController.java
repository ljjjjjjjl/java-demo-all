package com.ljl.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljl.annotation.RequireRole;
import com.ljl.entity.Order;
import com.ljl.service.OrderService;
import com.ljl.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @RequireRole({"运营员", "管理员"})
    @PostMapping("/create")
    public Result createOrder(@RequestBody Order order) {
        boolean success = orderService.createOrder(order);
        return success ? Result.success("创建订单成功") : Result.error("创建订单失败");
    }

    @RequireRole({"运营员", "管理员"})
    @GetMapping("/list")
    public Result listOrders(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(required = false) Integer status) {
        Page<Order> orders = orderService.listOrders(pageNum, pageSize, status);
        return Result.success(orders);
    }

    @RequireRole({"运营员", "管理员"})
    @GetMapping("/{id}")
    public Result getOrder(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return order != null ? Result.success(order) : Result.error("订单不存在");
    }

    @RequireRole("管理员")
    @PutMapping("/status/{orderId}/{status}")
    public Result updateOrderStatus(@PathVariable Long orderId, @PathVariable Integer status) {
        boolean success = orderService.updateOrderStatus(orderId, status);
        return success ? Result.success("更新订单状态成功") : Result.error("更新订单状态失败");
    }
}
