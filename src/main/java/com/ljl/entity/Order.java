package com.ljl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 用于表示订单基本信息，包含订单号、用户信息、总价和状态等
 * 对应数据库表：order
 */
@Data
@TableName("order")
public class Order {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 订单编号，唯一标识一个订单
     */
    private String orderNo;
    
    /**
     * 用户ID，关联下单用户
     */
    private Long userId;
    
    /**
     * 订单总金额，精确到小数点后两位
     */
    private BigDecimal totalPrice;
    
    /**
     * 订单状态
     * 0: 待付款, 1: 待发货, 2: 已发货, 3: 已完成
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
