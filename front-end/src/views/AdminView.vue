<script setup>
import {onMounted, ref} from "vue";
import {
  UserFilled,
  List,
  Message,
  MessageBox,
  PieChart,
  SwitchButton,
  Notebook,
  Document
} from '@element-plus/icons-vue';
import router from "@/router/index.js";
import {getWithToken, removeToken} from "@/net/index.js";
import {ElMessage} from "element-plus";

const faviconSrc = ref('/src/assets/images/foodSafety.ico');

// 图片加载失败处理方法
const handleImageError = () => {
  console.error("图片加载失败，使用默认图片代替");
  faviconSrc.value = "/src/assets/images/default.png";
};
const handleImageLoad = () => {
  console.log("图片加载成功");
};

function logoutSuccess(data) {
  removeToken()
  ElMessage.success('退出登录成功')
  router.push("/")
}

function userLogout() {
  getWithToken('/api/auth/logout', logoutSuccess)
}
</script>

<template>
  <el-container class="container">
    <el-header style="display: flex; justify-content: space-between; align-items: center; height: 8vb; width: 100%">
      <el-container style="align-items: center;">
        <el-image style="width: 70px; height: 70px;" :src="faviconSrc" fit="cover" alt="图片加载失败"
                  @load="handleImageLoad" @error="handleImageError">
          <template #placeholder>
            <div style="color: #999;">加载中...</div>
          </template>
        </el-image>
        <h1 style="font-size: xx-large; font-family: KaiTi;">同济食品安全管理平台</h1>
      </el-container>
      <div>
        <!--个人中心-->
        <el-button @click="router.push('/admin/profile')" type="info" plain>
          <el-icon :size="17">
            <UserFilled/>
          </el-icon>
          个人中心
        </el-button>
      </div>
    </el-header>

    <el-container>
      <el-aside width="200px" style="background-color: cadetblue;">
        <el-menu default-active="1" class="el-menu-vertical" style="background-color: bisque">
          <el-sub-menu index="1">
            <template #title>
              <el-icon>
                <List/>
              </el-icon>
              <span>食品信息管理</span>
            </template>
            <el-menu-item index="1-1" @click="router.push('/admin/foodinfoManage/foodinfoList')">食品信息列表</el-menu-item>
            <el-menu-item index="1-2" @click="router.push('/admin/foodinfoManage/inputFoodinfo')">食品信息录入</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="2">
            <template #title>
              <el-icon>
                <MessageBox/>
              </el-icon>
              <span>投诉受理</span>
            </template>
            <el-menu-item index="2-1" @click="router.push('/admin/complaintManage/complaintList')">投诉列表</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="4">
            <template #title>
              <el-icon>
                <Notebook/>
              </el-icon>
              <span>食品安全推文</span>
            </template>
            <el-menu-item index="4-1" @click="router.push('/admin/articleManage/articleList')">推文列表</el-menu-item>
            <el-menu-item index="4-2" @click="router.push('/admin/articleManage/createArticle')">推文撰写</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="5">
            <template #title>
              <el-icon>
                <Notebook/>
              </el-icon>
              <span>食品安全知识库</span>
            </template>
            <el-menu-item index="5-1" @click="router.push('/admin/knowledgeManage/knowledgeList')">知识库列表</el-menu-item>
            <el-menu-item index="5-2" @click="router.push('/admin/knowledgeManage/createKnowledge')">知识库撰写</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="6">
            <template #title>
              <el-icon>
                <Document/>
              </el-icon>
              <span>修改日志</span>
            </template>
            <el-menu-item index="6-1" @click="router.push('/admin/log')">查看修改日志</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <el-main>
        <router-view v-slot="{ Component }">
          <transition name="el-fade-in-linear" mode="out-in">
            <component :is="Component"/>
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.container {
  min-height: 100vb;
  height: 100%;
  width: 100%;
  border: 2px solid #dcdfe6;
}

.el-header {
  background-color: aqua;
  width: 160vb;
}

.el-menu-item {
  background-color: beige;
}

h3 {
  margin: 0;
}
</style>