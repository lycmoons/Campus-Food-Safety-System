<script setup>
import { useRoute } from "vue-router";
import router from "@/router/index.js";
import { reactive } from "vue";

const route = useRoute();

function goBack() {
  router.back();
}

let form = reactive({
  id: route.params.id,
  category: route.params.category,
  title: route.params.title,
  content: route.params.content,
  photo_url: JSON.parse(route.params.photo_url),
  date: route.params.date,
});
</script>

<template>
  <div class="container">
    <!-- 标题和类型 -->
    <div class="header">
      <h1 class="title">{{ form.title }}</h1>
      <div class="sub-info">
        <span class="category">类型：{{ form.category }}</span>
        <span class="date">日期：{{ form.date }}</span>
      </div>
    </div>

    <!-- 内容 -->
    <div class="content">
      <el-divider>内容</el-divider>
      <p>{{ form.content }}</p>
    </div>

    <!-- 配图 -->
    <div class="photos">
      <el-divider>配图</el-divider>
      <el-row :gutter="20">
        <el-col
            v-for="(photo, index) in form.photo_url"
            :key="index"
            :span="8"
            class="photo-item"
        >
          <el-image :src="photo" fit="cover" class="image"/>
        </el-col>
      </el-row>
    </div>

    <!-- 返回按钮 -->
    <div class="footer">
      <el-button type="primary" @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<style scoped>
/* 容器 */
.container {
  max-width: 900px;
  margin: 40px auto;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

/* 头部区域 */
.header {
  text-align: center;
  margin-bottom: 20px;
}

.title {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.sub-info {
  font-size: 14px;
  color: #888;
  display: flex;
  justify-content: center;
  gap: 20px;
}

/* 内容区域 */
.content {
  font-size: 16px;
  line-height: 1.8;
  color: #555;
  text-align: justify;
  margin-bottom: 20px;
}

/* 图片区域 */
.photos {
  margin-bottom: 20px;
}

.photo-item {
  display: flex;
  justify-content: center;
  margin-bottom: 15px;
}

.image {
  width: 100%;
  height: 200px;
  border-radius: 4px;
  object-fit: cover;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 返回按钮区域 */
.footer {
  text-align: center;
  margin-top: 20px;
}

.el-button {
  font-size: 14px;
  padding: 10px 30px;
}
</style>
