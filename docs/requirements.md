### 2. 核心功能模块

#### 用户认证与权限管理 (Auth)
*   ✅ **JWT 登录鉴权**：实现用户登录接口，生成 Token，拦截未授权请求。
*   ✅ **RBAC 基础模型**：用户 -> 角色 -> 菜单/权限。实现简单的角色分配（如：管理员、运营员）。
*   ✅ **接口文档**：集成 Swagger 或 Knife4j，方便前后端联调。
#### 商品管理 (Product)
*   ✅ **商品 CRUD**：商品的增删改查（名称、价格、库存、状态）。
*   ✅ **分类管理**：商品分类的树形结构管理。（已完成）
*   ✅ **图片上传**：实现简单的文件上传接口（可对接本地存储或云存储）。（已完成）

#### 订单管理 (Order)
*   ✅ **订单查询**：分页查询订单列表，支持按状态（待付款、待发货、已完成）筛选。（已完成）
*   ✅ **订单详情**：查看订单包含的商品列表。（已完成）
*   ✅ **状态流转**：模拟订单状态变更（如：发货操作）。（已完成）

#### 数据统计 (Dashboard)
*   ✅ **报表统计**：今日订单量、销售额统计（简单的 SQL 聚合查询）。（已完成）

### 3. 数据库设计 (核心表)
*   ✅ **用户表 (user)**：id, username, password, real_name, phone, status(0 正常 1 禁用), create_time
*   ✅ **角色表 (role)**：id, role_name, description
*   ✅ **用户角色关联表 (user_role)**：user_id, role_id
*   ✅ **商品表 (product)**：id, product_name, price, stock, category_id, status(0 上架 1 下架), image_url, version（乐观锁版本号）
*   ✅ **商品分类表 (category)**：id, name, parent_id, sort, create_time, update_time（已创建）
*   ✅ **订单表 (order)**：id, order_no(订单号), user_id, total_price, status(0 待付款 1 待发货 2 已发货 3 已完成), create_time（已创建）
*   ✅ **订单详情表 (order_item)**：id, order_id, product_id, quantity, price, create_time（已创建）
*   ✅ **操作日志表 (operation_log)**：id, username, operation, method, ip, status, error_msg, cost_time, create_time（已创建）

### 4. 开发亮点与难点 (面试加分点)
*   ✅ **统一响应格式**：定义统一的 `Result` 返回对象，包含 code, message, data，提高接口规范性。
*   ✅ **全局异常处理**：使用 `@ControllerAdvice` 捕获全局异常（如参数校验失败、业务异常），避免代码中到处 try-catch。
*   ✅ **分页处理**：使用 MyBatis Plus 的分页插件，简化分页代码。
*   ✅ **乐观锁机制**：在商品库存扣减时使用 version 字段实现 CAS 乐观锁，防止超卖问题。（已完成）
*   ✅ **日志记录 (进阶)**：使用 AOP 切面记录用户操作日志（如：谁在什么时间修改了什么数据）。（已完成）

### 5. 部署与启动流程
1.  ✅ 创建数据库，执行建表 SQL。（所有核心表已创建完成）
2.  修改 application.yml 配置文件中的数据库连接信息。
3.  Maven 打包或直接运行启动类。
4.  浏览器访问 Swagger 地址进行接口测试。

### 6. 下一步计划
**优先级排序：**

**P1 - 功能优化**（完善现有功能）
- [ ] 图片上传支持多种格式验证（jpg/png/gif）
- [ ] 添加图片压缩功能
- [ ] 商品列表支持多条件搜索（名称、分类、状态）

---

## 📊 当前完成度：**95%**

✅ **已完成：** 所有核心业务功能 + 图片上传 + 乐观锁机制 + 操作日志系统  
⚠️ **待完成：** 功能优化（锦上添花）
