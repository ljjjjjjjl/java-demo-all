// ========== 新增：权限拦截 ==========
// 页面加载前先检查登录状态
window.onload = function() {
    // 1. 检查是否已登录，未登录则跳转到登录页
    if (!checkLoginStatus()) {
        window.location.href = "login.html";
        return;
    }

    // 2. 正常初始化
    getCategoryList();
    bindFormSubmit();
};

/**
 * 检查登录状态
 * @returns {boolean} 是否已登录
 */
function checkLoginStatus() {
    const token = localStorage.getItem("token");
    // 简单校验：存在token即认为已登录（可扩展：校验token有效期）
    return !!token;
}

/**
 * 退出登录功能（新增）
 */
function logout() {
    if (confirm("确定要退出登录吗？")) {
        // 清除登录状态
        localStorage.removeItem("token");
        localStorage.removeItem("userInfo");

        // 跳转到登录页
        window.location.href = "login.html";
    }
}

// ========== 核心业务逻辑 ==========
// 接口基础路径（请根据后端API的实际地址调整）
const BASE_URL = "/api/categories";

/**
 * 通用请求函数（修改：添加token请求头和错误处理）
 * @param {string} url 请求地址
 * @param {string} method 请求方式 GET/POST/PUT/DELETE
 * @param {object} data 请求体数据
 * @returns {Promise} 响应数据
 */
async function request(url, method = "GET", data = null) {
    const options = {
        method: method,
        headers: {
            "Content-Type": "application/json;charset=utf-8",
            // 添加token到请求头（后端鉴权用）
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    };

    if (data && (method === "POST" || method === "PUT")) {
        options.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(url, options);

        // 处理401未授权（token过期/无效）
        if (response.status === 401) {
            alert("登录已过期，请重新登录");
            localStorage.removeItem("token");
            window.location.href = "login.html";
            throw new Error("未授权");
        }

        const result = await response.json();
        if (!response.ok) {
            throw new Error(result.message || "接口请求失败");
        }
        return result;
    } catch (error) {
        alert("请求异常：" + error.message);
        throw error;
    }
}

// 1. 获取分类列表（从后端接口拉取）
async function getCategoryList() {
    try {
        const result = await request(BASE_URL, "GET");
        // 假设后端返回 {success:true, data:[...]}，取data作为列表数据
        const categoryList = result.data || [];

        // 渲染列表
        renderCategoryList(categoryList);
    } catch (error) {
        console.error("获取列表失败：", error);
    }
}

// 2. 渲染分类列表（抽离为独立函数，接收列表数据）
function renderCategoryList(categoryList) {
    const tbody = document.querySelector("#category-table tbody");
    tbody.innerHTML = ""; // 清空原有内容

    // 按排序序号降序排列
    const sortedList = [...categoryList].sort((a, b) => b.sort - a.sort);

    if (sortedList.length === 0) {
        const tr = document.createElement("tr");
        tr.innerHTML = `<td colspan="4" style="color:#999;">暂无分类数据</td>`;
        tbody.appendChild(tr);
        return;
    }

    sortedList.forEach(category => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${category.id}</td>
            <td>${category.name}</td>
            <td>${category.sort}</td>
            <td>
                <button class="btn edit-btn" onclick="editCategory(${category.id})">编辑</button>
                <button class="btn delete-btn" onclick="deleteCategory(${category.id})">删除</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// 3. 绑定表单提交事件（新增/编辑）
function bindFormSubmit() {
    const form = document.getElementById("category-form");
    form.onsubmit = async function(e) {
        e.preventDefault(); // 阻止默认提交

        const id = document.getElementById("category-id").value;
        const name = document.getElementById("category-name").value.trim();
        const sort = parseInt(document.getElementById("category-sort").value);

        // 数据校验
        if (!name) {
            alert("分类名称不能为空！");
            return;
        }
        if (isNaN(sort) || sort < 0) {
            alert("排序序号必须为非负数字！");
            return;
        }

        try {
            if (id) {
                // 编辑分类：PUT请求 /api/categories/{id}
                await request(`${BASE_URL}/${id}`, "PUT", { name, sort });
                alert("编辑分类成功！");
            } else {
                // 新增分类：POST请求 /api/categories
                await request(BASE_URL, "POST", { name, sort });
                alert("新增分类成功！");
            }

            resetForm();        // 重置表单
            getCategoryList();  // 重新拉取列表
        } catch (error) {
            console.error("提交失败：", error);
        }
    };
}

// 4. 编辑分类（先查询详情，再填充表单）
async function editCategory(id) {
    try {
        // 获取单条分类详情：GET /api/categories/{id}
        const result = await request(`${BASE_URL}/${id}`, "GET");
        const category = result.data;

        document.getElementById("form-title").textContent = "编辑分类";
        document.getElementById("category-id").value = category.id;
        document.getElementById("category-name").value = category.name;
        document.getElementById("category-sort").value = category.sort;
    } catch (error) {
        console.error("获取分类详情失败：", error);
    }
}

// 5. 删除分类
async function deleteCategory(id) {
    if (!confirm("确定要删除该分类吗？删除后不可恢复！")) {
        return;
    }

    try {
        // 删除分类：DELETE /api/categories/{id}
        await request(`${BASE_URL}/${id}`, "DELETE");
        alert("删除分类成功！");
        getCategoryList(); // 重新拉取列表
    } catch (error) {
        console.error("删除分类失败：", error);
    }
}

// 6. 重置表单
function resetForm() {
    document.getElementById("form-title").textContent = "新增分类";
    document.getElementById("category-form").reset();
    document.getElementById("category-id").value = "";
}
