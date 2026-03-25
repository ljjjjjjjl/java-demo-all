package com.ljl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljl.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    // 无需自定义方法，MyBatis Plus 已提供 CRUD
}


