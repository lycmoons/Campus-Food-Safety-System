<script setup>
import router from "@/router/index.js";
import { onMounted, reactive } from "vue";
import { get } from "@/net/index.js";

function goBack() {
  router.back();
}

function getAllNews() {
  get("/api/news/all-news", (data) => {
    for (const item of data) {
      form.news.push({
        id: item.id,
        title: item.title,
        content: item.content,
        photo_url: item.photo_url,
        date: new Date(item.date).toLocaleString(),
      });
    }
  });
}

function showNewsDetail(newsItem) {
  router.push({
    name: "news",
    params: {
      id: newsItem.id,
      title: newsItem.title,
      content: newsItem.content,
      photo_url: JSON.stringify(newsItem.photo_url),
      date: newsItem.date,
    },
  });
}

onMounted(() => {
  getAllNews();
});

let form = reactive({
  news: [],
});
</script>

<template>
  <div class="container">
    <div class="header">
      <el-button @click="goBack">返回</el-button>
    </div>

    <div class="news-list">
      <div
          class="news-item"
          v-for="newsItem in form.news"
          :key="newsItem.id"
          @click="showNewsDetail(newsItem)"
      >
        <el-image class="news-image" :src="newsItem.photo_url[0]" />
        <div class="news-info">
          <span class="title">{{ newsItem.title }}</span>
          <span class="date">{{ newsItem.date || "未指定日期" }}</span>
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

.news-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: center;
}

.news-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 200px;
  padding: 12px;
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.news-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.news-image {
  width: 100%;
  height: 120px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 8px;
}

.news-info {
  text-align: center;
}

.title {
  display: block;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.date {
  display: block;
  font-size: 14px;
  color: #999;
}
</style>
