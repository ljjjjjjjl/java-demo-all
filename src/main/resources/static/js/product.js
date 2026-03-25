// ========== 权限拦截 ==========
window.onload = function() {
    // 检查登录状态
    if (!checkLoginStatus()) {
        window.location.href = "login.html";
        return;
    }

    // 初始化
    initPage();
};

/**
 * 检查登录状态
 */
function checkLoginStatus() {
    const token = localStorage.getItem("token");
    return !!token;
}

/**
 * 退出登录
 */
function logout() {
    if (confirm("确定要退出登录吗？")) {
        localStorage.removeItem("token");
        localStorage.removeItem("userInfo");
        window.location.href = "login.html";
    }
}

// ========== 接口配置 ==========
const PRODUCT_BASE_URL = "/api/products"; // 商品接口前缀
const CATEGORY_BASE_URL = "/api/categories"; // 分类接口前缀

// 全局变量：商品列表、分类列表、搜索关键词
let productList = [];
let categoryList = [];
let searchKeyword = "";

/**
 * 页面初始化
 */
async function initPage() {
    try {
        // 1. 加载分类列表（用于商品所属分类下拉框）
        await loadCategoryList();
        // 2. 加载商品列表
        await loadProductList();
        // 3. 绑定表单提交事件
        bindProductForm();
    } catch (error) {
        console.error("页面初始化失败：", error);
        alert("页面加载失败，请刷新重试");
    }
}

/**
 * 加载分类列表（填充下拉框）
 */
async function loadCategoryList() {
    const result = await request(CATEGORY_BASE_URL, "GET");
    categoryList = result.data || [];

    const categorySelect = document.getElementById("product-category");
    categorySelect.innerHTML = '<option value="">请选择分类</option>';

    categoryList.forEach(category => {
        const option = document.createElement("option");
        option.value = category.id;
        option.textContent = category.name;
        categorySelect.appendChild(option);
    });
}

/**
 * 加载商品列表
 */
async function loadProductList() {
    const result = await request(PRODUCT_BASE_URL, "GET");
    productList = result.data || [];
    renderProductList();
}

/**
 * 渲染商品列表（支持搜索过滤）
 */
function renderProductList() {
    const tbody = document.querySelector("#product-table tbody");
    tbody.innerHTML = "";

    // 搜索过滤
    let filteredList = [...productList];
    if (searchKeyword) {
        filteredList = filteredList.filter(product =>
            product.name.toLowerCase().includes(searchKeyword.toLowerCase())
        );
    }

    if (filteredList.length === 0) {
        const tr = document.createElement("tr");
        tr.innerHTML = `<td colspan="7" style="color:#999;">暂无商品数据</td>`;
        tbody.appendChild(tr);
        return;
    }

    filteredList.forEach(product => {
        // 获取分类名称
        const category = categoryList.find(c => c.id === product.categoryId);
        const categoryName = category ? category.name : "未知分类";

        // 状态文本
        const statusText = product.status === 1 ? "上架" : "下架";
        const statusClass = product.status === 1 ? "text-green" : "text-red";

        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${product.id}</td>
            <td>${product.name}</td>
            <td>¥${product.price.toFixed(2)}</td>
            <td>${product.stock}</td>
            <td class="${statusClass}">${statusText}</td>
            <td>${categoryName}</td>
            <td>
                <button class="btn edit-btn" onclick="editProduct(${product.id})">编辑</button>
                <button class="btn delete-btn" onclick="deleteProduct(${product.id})">删除</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

/**
 * 绑定商品表单提交事件
 */
function bindProductForm() {
    const form = document.getElementById("product-form");

    form.onsubmit = async function(e) {
        e.preventDefault();

        // 获取表单数据
        const id = document.getElementById("product-id").value;
        const name = document.getElementById("product-name").value.trim();
        const price = parseFloat(document.getElementById("product-price").value);
        const stock = parseInt(document.getElementById("product-stock").value);
        const status = parseInt(document.getElementById("product-status").value);
        const categoryId = parseInt(document.getElementById("product-category").value);

        // 数据校验
        if (!name) {
            alert("商品名称不能为空！");
            return;
        }
        if (isNaN(price) || price < 0) {
            alert("商品价格必须为非负数！");
            return;
        }
        if (isNaN(stock) || stock < 0) {
            alert("库存数量必须为非负整数！");
            return;
        }
        if (isNaN(categoryId)) {
            alert("请选择所属分类！");
            return;
        }

        const productData = { name, price, stock, status, categoryId };

        try {
            if (id) {
                // 编辑商品
                await request(`${PRODUCT_BASE_URL}/${id}`, "PUT", productData);
                alert("商品编辑成功！");
            } else {
                // 新增商品
                await request(PRODUCT_BASE_URL, "POST", productData);
                alert("商品新增成功！");
            }

            resetForm();
            loadProductList(); // 重新加载列表
        } catch (error) {
            console.error("提交商品失败：", error);
            alert("操作失败：" + error.message);
        }
    };
}

/**
 * 编辑商品
 */
async function editProduct(id) {
    try {
        // 获取商品详情
        const result = await request(`${PRODUCT_BASE_URL}/${id}`, "GET");
        const product = result.data;

        // 填充表单
        document.getElementById("form-title").textContent = "编辑商品";
        document.getElementById("product-id").value = product.id;
        document.getElementById("product-name").value = product.name;
        document.getElementById("product-price").value = product.price;
        document.getElementById("product-stock").value = product.stock;
        document.getElementById("product-status").value = product.status;
        document.getElementById("product-category").value = product.categoryId;
    } catch (error) {
        console.error("获取商品详情失败：", error);
        alert("编辑失败：" + error.message);
    }
}

/**
 * 删除商品
 */
async function deleteProduct(id) {
    if (!confirm("确定要删除该商品吗？删除后不可恢复！")) {
        return;
    }

    try {
        await request(`${PRODUCT_BASE_URL}/${id}`, "DELETE");
        alert("商品删除成功！");
        loadProductList();
    } catch (error) {
        console.error("删除商品失败：", error);
        alert("删除失败：" + error.message);
    }
}

/**
 * 重置表单
 */
function resetForm() {
    document.getElementById("form-title").textContent = "新增商品";
    document.getElementById("product-form").reset();
    document.getElementById("product-id").value = "";
}

/**
 * 搜索商品
 */
function searchProduct() {
    searchKeyword = document.getElementById("search-input").value.trim();
    renderProductList();
}

/**
 * 重置搜索
 */
function resetSearch() {
    document.getElementById("search-input").value = "";
    searchKeyword = "";
    renderProductList();
}

/**
 * 通用请求函数（和分类管理的request函数一致）
 */
async function request(url, method = "GET", data = null) {
    const options = {
        method: method,
        headers: {
            "Content-Type": "application/json;charset=utf-8",
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    };

    if (data && (method === "POST" || method === "PUT")) {
        options.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(url, options);

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

// ========== 新增样式（状态文字颜色） ==========
// 动态添加样式，避免修改CSS文件
const style = document.createElement('style');
style.textContent = `
    .text-green { color: #67c23a; font-weight: bold; }
    .text-red { color: #f56c6c; font-weight: bold; }
`;
document.head.appendChild(style);