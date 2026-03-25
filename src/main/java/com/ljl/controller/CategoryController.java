package com.ljl.controller;

import com.ljl.annotation.RequireRole;
import com.ljl.entity.Category;
import com.ljl.service.CategoryService;
import com.ljl.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @RequireRole("管理员")
    @PostMapping("/add")
    public Result addCategory(@RequestBody Category category) {
        boolean success = categoryService.addCategory(category);
        return success ? Result.success("添加分类成功") : Result.error("添加分类失败");
    }

    @RequireRole("管理员")
    @PutMapping("/update")
    public Result updateCategory(@RequestBody Category category) {
        boolean success = categoryService.updateCategory(category);
        return success ? Result.success("修改分类成功") : Result.error("修改分类失败");
    }

    @RequireRole("管理员")
    @DeleteMapping("/delete/{id}")
    public Result deleteCategory(@PathVariable Long id) {
        boolean success = categoryService.deleteCategory(id);
        return success ? Result.success("删除分类成功") : Result.error("删除分类失败");
    }

    @RequireRole({"运营员", "管理员"})
    @GetMapping("/list")
    public Result listCategories() {
        List<Category> categories = categoryService.listCategories();
        return Result.success(categories);
    }

    @RequireRole({"运营员", "管理员"})
    @GetMapping("/first-level")
    public Result listFirstLevelCategories() {
        List<Category> categories = categoryService.listFirstLevelCategories();
        return Result.success(categories);
    }

    @RequireRole({"运营员", "管理员"})
    @GetMapping("/children/{parentId}")
    public Result listCategoriesByParentId(@PathVariable Long parentId) {
        List<Category> categories = categoryService.listCategoriesByParentId(parentId);
        return Result.success(categories);
    }
}
