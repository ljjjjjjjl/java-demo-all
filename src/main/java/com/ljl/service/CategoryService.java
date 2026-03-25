package com.ljl.service;

import com.ljl.entity.Category;
import java.util.List;

public interface CategoryService {
    /**
     * 添加分类
     */
    boolean addCategory(Category category);

    /**
     * 修改分类
     */
    boolean updateCategory(Category category);

    /**
     * 删除分类
     */
    boolean deleteCategory(Long id);

    /**
     * 查询所有分类（树形结构）
     */
    List<Category> listCategories();

    /**
     * 查询一级分类
     */
    List<Category> listFirstLevelCategories();

    /**
     * 根据父 ID 查询子分类
     */
    List<Category> listCategoriesByParentId(Long parentId);
}
