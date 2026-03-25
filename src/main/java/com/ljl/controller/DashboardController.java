package com.ljl.controller;

import com.ljl.service.DashboardService;
import com.ljl.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据统计报表控制器
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取今日统计数据
     * GET /dashboard/today-stats
     */
    @GetMapping("/today-stats")
    public Result getTodayStats() {
        return dashboardService.getTodayStats();
    }
}
