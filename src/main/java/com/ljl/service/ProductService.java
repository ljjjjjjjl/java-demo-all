package com.ljl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljl.dto.ProductDTO;
import com.ljl.entity.Product;

import java.util.List;
public interface ProductService extends IService<Product> {
    // 新增商品
    boolean addProduct(ProductDTO productDTO);
    // 修改商品
    boolean updateProduct(ProductDTO productDTO);
    // 删除商品
    boolean deleteProduct(Long id);
    // 分页查询商品
    List<Product> listProducts(Integer pageNum, Integer pageSize);
    // 根据ID查询商品
    Product getProductById(Long id);
}

