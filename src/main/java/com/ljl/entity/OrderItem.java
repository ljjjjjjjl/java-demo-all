package com.ljl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情实体类
 * 用于表示订单中的单个商品项，包含商品信息、数量和价格
 * 对应数据库表：order_item
 */
@Data
@TableName("order_item")
public class OrderItem {
    /**
     * 主键ID，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 订单ID，关联所属订单
     */
    private Long orderId;
    
    /**
     * 商品ID，关联具体商品
     */
    private Long productId;
    
    /**
     * 购买数量
     */
    private Integer quantity;
    
    /**
     * 商品单价，以购买时的价格为准
     */
    private BigDecimal price;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}

