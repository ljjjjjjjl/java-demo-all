package com.ljl.service;

import com.ljl.utils.Result;

/**
 * 数据统计服务接口
 */
public interface DashboardService {
    /**
     * 获取今日统计数据，包括今日订单量和今日销售额
     * @return 统一返回结果
     */
    Result getTodayStats();
}
