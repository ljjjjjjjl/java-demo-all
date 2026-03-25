// 登录接口地址（根据后端实际接口调整）
const LOGIN_API = "/api/login";

// 页面加载完成后绑定事件
window.onload = function() {
    // 检查是否已登录，若已登录直接跳转到首页
    if (isLoggedIn()) {
        window.location.href = "index.html";
    }

    // 绑定登录表单提交事件
    bindLoginForm();

    // 填充记住的用户名（如果有）
    fillRememberedUsername();
};

/**
 * 检查是否已登录（通过localStorage中的token判断）
 */
function isLoggedIn() {
    const token = localStorage.getItem("token");
    return !!token;
}

/**
 * 绑定登录表单提交事件
 */
function bindLoginForm() {
    const form = document.getElementById("login-form");
    const errorTip = document.getElementById("error-tip");

    form.onsubmit = async function(e) {
        e.preventDefault(); // 阻止默认提交

        // 获取表单数据
        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();
        const rememberMe = document.getElementById("remember-me").checked;

        // 基础校验
        if (!username) {
            showError("请输入用户名");
            return;
        }
        if (!password) {
            showError("请输入密码");
            return;
        }

        try {
            // 发起登录请求
            const result = await loginRequest(username, password);

            // 登录成功处理
            handleLoginSuccess(result, rememberMe, username);

            // 跳转到分类管理页面
            window.location.href = "index.html";
        } catch (error) {
            showError(error.message || "登录失败，请重试");
        }
    };

    // 显示错误提示
    function showError(message) {
        errorTip.textContent = message;
        // 3秒后清空错误提示
        setTimeout(() => {
            errorTip.textContent = "";
        }, 3000);
    }
}

/**
 * 登录请求（对接后端接口）
 * @param {string} username 用户名
 * @param {string} password 密码
 * @returns {Promise} 登录结果
 */
async function loginRequest(username, password) {
    const response = await fetch(LOGIN_API, {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=utf-8"
        },
        body: JSON.stringify({ username, password })
    });

    const result = await response.json();

    // 处理接口返回的错误
    if (!response.ok || !result.success) {
        throw new Error(result.message || "用户名或密码错误");
    }

    return result;
}

/**
 * 登录成功后的处理逻辑
 * @param {object} result 登录接口返回结果
 * @param {boolean} rememberMe 是否记住我
 * @param {string} username 用户名
 */
function handleLoginSuccess(result, rememberMe, username) {
    // 1. 存储token（核心：鉴权凭证）
    if (result.data && result.data.token) {
        localStorage.setItem("token", result.data.token);
    }

    // 2. 存储用户信息（可选）
    if (result.data && result.data.user) {
        localStorage.setItem("userInfo", JSON.stringify(result.data.user));
    }

    // 3. 处理记住我功能
    if (rememberMe) {
        localStorage.setItem("rememberedUsername", username);
    } else {
        localStorage.removeItem("rememberedUsername");
    }

    // 4. 提示登录成功
    alert("登录成功！即将跳转到管理页面");
}

/**
 * 填充记住的用户名
 */
function fillRememberedUsername() {
    const rememberedUsername = localStorage.getItem("rememberedUsername");
    if (rememberedUsername) {
        document.getElementById("username").value = rememberedUsername;
        document.getElementById("remember-me").checked = true;
    }
}