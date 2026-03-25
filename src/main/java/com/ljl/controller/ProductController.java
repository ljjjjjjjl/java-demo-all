package com.ljl.controller;

import com.ljl.annotation.RequireRole;
import com.ljl.dto.ProductDTO;
import com.ljl.entity.Product;
import com.ljl.service.ProductService;
import com.ljl.utils.FileUploadUtil;
import com.ljl.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "商品管理")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private FileUploadUtil fileUploadUtil;

    // 👉 管理员专属：新增商品（需要"管理员"角色）
    @RequireRole("管理员")
    @PostMapping("/add")
    public Result addProduct(@Validated @RequestBody ProductDTO productDTO) {
        boolean success = productService.addProduct(productDTO);
        return success ? Result.success("新增商品成功") : Result.error("新增商品失败");
    }

    // 👉 管理员专属：修改商品（需要"管理员"角色）
    @RequireRole("管理员")
    @PutMapping("/update")
    public Result updateProduct(@Validated @RequestBody ProductDTO productDTO) {
        boolean success = productService.updateProduct(productDTO);
        return success ? Result.success("修改商品成功") : Result.error("修改商品失败");
    }

    // 👉 管理员专属：删除商品（需要"管理员"角色）
    @RequireRole("管理员")
    @DeleteMapping("/delete/{id}")
    public Result deleteProduct(@PathVariable Long id) {
        boolean success = productService.deleteProduct(id);
        return success ? Result.success("删除商品成功") : Result.error("删除商品失败");
    }

    // 👉 运营员/管理员都能访问：查询商品列表（需要"运营员"或"管理员"角色）
    @RequireRole({"运营员", "管理员"})
    @GetMapping("/list")
    public Result listProducts(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Product> products = productService.listProducts(pageNum, pageSize);
        return Result.success(products);
    }

    // 👉 运营员/管理员都能访问：查询单个商品
    @RequireRole({"运营员", "管理员"})
    @GetMapping("/{id}")
    public Result getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return product != null ? Result.success(product) : Result.error("商品不存在");
    }

    // 👉 上传商品图片
    @RequireRole({"运营员", "管理员"})
    @PostMapping("/upload")
    @ApiOperation("上传商品图片")
    public Result uploadProductImage(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = System.getProperty("os.name").toLowerCase().contains("win") 
                ? "D:/uploads/" : "/tmp/uploads/";
            String filePath = fileUploadUtil.uploadFile(file, uploadDir);
            return Result.success(filePath);
        } catch (Exception e) {
            return Result.error("上传失败：" + e.getMessage());
        }
    }
}
