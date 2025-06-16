<template>
  <div class="container">
    <div class="card">
      <div class="item">
        <span class="label">用户名：</span>{{ form.username }}
      </div>

      <div class="item">
        <span class="label">邮箱：</span>{{ form.email }}
      </div>

      <div class="item">
        <el-button type="danger" @click="userLogout" plain class="logout-btn">退出登录</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, reactive} from "vue";
import {getWithToken, postWithToken, removeToken} from "@/net/index.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

function logoutSuccess(data) {
  removeToken()
  sessionStorage.removeItem("aiChatMessages")
  ElMessage.success('退出登录成功')
  router.push("/")
}

function userLogout() {
  getWithToken('/api/auth/logout', logoutSuccess)
}

function getPersonalInfo() {
  getWithToken('/api/account/person-info', (data) => {
    form.username = data.username
    form.email = data.email
  })
}

onMounted(() => {
  getPersonalInfo()
})

let form = reactive({
  username: '',
  email: ''
})

</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f4f7fc;
}

.card {
  width: 100%;
  max-width: 400px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 30px;
  text-align: left;
}

.item {
  margin-bottom: 20px;
  font-size: 16px;
  color: #555;
}

.label {
  font-weight: bold;
  color: #333;
}

.logout-btn {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border-radius: 5px;
  border: none;
  cursor: pointer;
  background-color: #ff4d4f;
  color: white;
}

.logout-btn:hover {
  background-color: #f5222d;
}
</style>
