<script setup>
import router from "@/router/index.js";
import { onMounted, reactive } from "vue";
import { get } from "@/net/index.js";

function goBack() {
  router.back();
}

function getAllKnowledge() {
  get('/api/knowledge/all-knowledge', (data) => {
    for (const item of data) {
      form.knowledge.push({
        id: item.id,
        title: item.title,
        content: item.content,
        category: item.category,
        photo_url: item.photo_url,
        date: (new Date(item.date)).toLocaleString(),
      });
    }
  });
}

function showKnowledgeDetail(knowledgeItem) {
  router.push({
    name: 'knowledge',
    params: {
      id: knowledgeItem.id,
      category: knowledgeItem.category,
      title: knowledgeItem.title,
      content: knowledgeItem.content,
      photo_url: JSON.stringify(knowledgeItem.photo_url),
      date: knowledgeItem.date,
    },
  });
}

onMounted(() => {
  getAllKnowledge();
});

let form = reactive({
  knowledge: [],
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
          v-for="knowledgeItem in form.knowledge"
          :key="knowledgeItem.id"
          @click="showKnowledgeDetail(knowledgeItem)"
      >
        <el-image class="news-image" :src="knowledgeItem.photo_url[0]" />
        <div class="news-info">
          <span class="title">{{ knowledgeItem.title }}</span>
          <span class="date">{{ knowledgeItem.date || "未指定日期" }}</span>
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
