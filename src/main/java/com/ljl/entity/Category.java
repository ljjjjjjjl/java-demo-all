package com.ljl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品分类实体类
 * 用于表示商品分类信息，支持树形结构（通过parentId实现）
 * 对应数据库表：category
 */
@Data
@TableName("category")
public class Category {
    /**
     * 主键ID，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 分类名称
     */
    private String name;
    
    /**
     * 父分类ID，用于构建树形结构
     * 顶级分类的parentId为null或0
     */
    private Long parentId;
    
    /**
     * 排序字段，数值越小越靠前
     */
    private Integer sort;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
