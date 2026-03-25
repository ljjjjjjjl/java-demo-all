package com.ljl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljl.dto.ProductDTO;
import com.ljl.entity.Product;
import com.ljl.mapper.ProductMapper;
import com.ljl.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

    @Service
    public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

        @Override
        public boolean addProduct(ProductDTO productDTO) {
            Product product = new Product();
            BeanUtils.copyProperties(productDTO, product);
            return this.save(product);
        }

        @Override
        public boolean updateProduct(ProductDTO productDTO) {
            Product product = new Product();
            BeanUtils.copyProperties(productDTO, product);
            return this.updateById(product);
        }

        @Override
        public boolean deleteProduct(Long id) {
            return this.removeById(id);
        }

        @Override
        public List<Product> listProducts(Integer pageNum, Integer pageSize) {
            // 简单分页（后续可升级为 MyBatis Plus 分页插件）
            return this.lambdaQuery()
                    .page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize))
                    .getRecords();
        }

        @Override
        public Product getProductById(Long id) {
            return this.getById(id);
        }

        /**
         * 扣减库存（带乐观锁）
         * @param productId 商品 ID
         * @param quantity 扣减数量
         * @return true-扣减成功，false-库存不足或版本冲突
         */
        public boolean deductStock(Long productId, Integer quantity) {
            Product product = this.getById(productId);
            if (product == null || product.getStock() < quantity) {
                return false;
            }
            
            product.setStock(product.getStock() - quantity);
            // MP 会自动比较 version 并 +1，如果 version 被其他事务修改则更新失败
            return this.updateById(product);
        }
    }
