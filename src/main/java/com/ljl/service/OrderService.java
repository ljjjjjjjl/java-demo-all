package com.ljl.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljl.entity.Order;

public interface OrderService {
    /**
     * 创建订单
     */
    boolean createOrder(Order order);

    /**
     * 分页查询订单列表
     */
    Page<Order> listOrders(Integer pageNum, Integer pageSize, Integer status);

    /**
     * 查询订单详情
     */
    Order getOrderById(Long id);

    /**
     * 更新订单状态
     */
    boolean updateOrderStatus(Long orderId, Integer status);
}
