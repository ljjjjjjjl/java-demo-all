package com.ljl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("operation_log")
public class OperationLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;        // 操作人
    private String operation;       // 操作内容（如：新增商品）
    private String method;          // 请求方法（如：POST /api/product/add）
    private String ip;              // 操作人 IP
    private Integer status;         // 执行状态（0 成功 1 失败）
    private String errorMsg;        // 错误信息
    private Long costTime;          // 耗时（毫秒）
    private LocalDateTime createTime;
}
