package com.ljl.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUploadUtil {

    /**
     * 上传文件到本地服务器
     * @param file 上传的文件
     * @param uploadDir 上传目录
     * @return 文件访问路径
     */
    public String uploadFile(MultipartFile file, String uploadDir) {
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        try {
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString().replace("-", "") + extension;

            // 创建上传目录
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 保存文件
            File destFile = new File(uploadDir, fileName);
            file.transferTo(destFile);

            // 返回相对路径（用于数据库存储）
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 删除文件
     * @param filePath 文件路径
     */
    public void deleteFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath.replace("/uploads/", getUploadDir()));
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private String getUploadDir() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "D:/uploads/";
        } else {
            return "/tmp/uploads/";
        }
    }
}
