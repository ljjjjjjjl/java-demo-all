package com.ljl.service.impl;

import com.ljl.mapper.OrderMapper;
import com.ljl.service.DashboardService;
import com.ljl.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Result getTodayStats() {
        // 获取今日零点
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        // 获取当前时间
        LocalDateTime endOfDay = LocalDateTime.now();

        // 查询今日订单数量
        Integer todayOrderCount = orderMapper.selectTodayOrderCount(startOfDay, endOfDay);
        // 查询今日销售额
        BigDecimal todaySales = orderMapper.selectTodaySales(startOfDay, endOfDay);

        // 构建返回数据
        var data = new java.util.HashMap<String, Object>();
        data.put("todayOrderCount", todayOrderCount != null ? todayOrderCount : 0);
        data.put("todaySales", todaySales != null ? todaySales : BigDecimal.ZERO);

        return Result.success(data);
    }
}
