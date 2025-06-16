<script setup>

import router from "@/router/index.js";
import { get } from "@/net/index.js";
import { onMounted, reactive } from "vue";

function goBack() {
  router.back();
}

function getAllPost() {
  get("/api/post/all-post", (data) => {
    for (const item of data) {
      form.userPost.push({
        id: item.id,
        username: item.username,
        title: item.title,
        content: item.content,
        photo_url: item.photo_url,
        date: new Date(item.date).toLocaleString(),
      });
    }
  });
}

function showPostDetail(post) {
  router.push({
    name: "postDetail",
    params: {
      id: post.id,
      title: post.title,
      username: post.username,
      date: post.date,
      content: post.content,
      photo_url: JSON.stringify(post.photo_url),
    },
  });
}

onMounted(() => {
  getAllPost();
});

let form = reactive({
  userPost: [],
});

</script>

<template>
  <div class="container">
    <div class="header">
      <el-button @click="goBack">返回</el-button>
    </div>

    <div class="post-list">
      <div
          class="post-item"
          v-for="post in form.userPost"
          :key="post.id"
          @click="showPostDetail(post)"
      >
        <el-image class="post-image" :src="post.photo_url[0]" />
        <div class="post-info">
          <span class="post-title">{{ post.title }}</span>
          <span class="post-author">作者：{{ post.username }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px;
  background-color: #f9f9f9;
  min-height: 100vh;
}

.header {
  width: 100%;
  display: flex;
  justify-content: flex-start;
  margin-bottom: 16px;
}

.post-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: center;
}

.post-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 220px;
  padding: 12px;
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.post-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.post-image {
  width: 100%;
  height: 140px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 8px;
}

.post-info {
  text-align: center;
}

.post-title {
  display: block;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.post-author {
  display: block;
  font-size: 14px;
  color: #999;
}
</style>
