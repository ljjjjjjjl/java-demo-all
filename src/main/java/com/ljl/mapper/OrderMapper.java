package com.ljl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljl.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 查询指定时间范围内的订单数量
     */
    @Select("SELECT COUNT(*) FROM `order` WHERE create_time BETWEEN #{start} AND #{end}")
    Integer selectTodayOrderCount(LocalDateTime start, LocalDateTime end);

    /**
     * 查询指定时间范围内的销售额
     */
    @Select("SELECT SUM(total_price) FROM `order` WHERE create_time BETWEEN #{start} AND #{end}")
    BigDecimal selectTodaySales(LocalDateTime start, LocalDateTime end);
}
