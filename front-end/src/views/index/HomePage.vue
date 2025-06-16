<script setup>
import {onMounted, reactive} from "vue";
import {get} from "@/net/index.js";
import router from "@/router/index.js";

function getTopNews() {
  get('/api/news/top-news', (data) => {
    for (const item of data) {
      form.news.push({
        id: item.id,
        title: item.title,
        content: item.content,
        photo_url: item.photo_url,
        date: (new Date(item.date)).toLocaleString(),
      });
    }
  });
}

function getTopKnowledge() {
  get('/api/knowledge/top-knowledge', (data) => {
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

function getAllNews(){
  router.push('/all-news')
}

function getAllKnowledge(){
  router.push('/all-knowledge')
}

// 页面首次加载时运行
onMounted(() => {
  getTopNews();
  getTopKnowledge();
});

function showNewsDetail(newsItem) {
  router.push({
    name: 'news',
    params: {
      id: newsItem.id,
      title: newsItem.title,
      content: newsItem.content,
      photo_url: JSON.stringify(newsItem.photo_url),
      date: newsItem.date,
    },
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

let form = reactive({
  news: [],
  knowledge: [],
});
</script>

<template>
  <div class="container">
    <div class="news">
      <div class="section-title">食品安全热点</div>
      <div class="content-container">
        <button class="content-item"
                v-for="newsItem in form.news"
                :key="newsItem.id"
                @click="showNewsDetail(newsItem)">
          <el-image class="image" :src="newsItem.photo_url[0]"/>
          <div class="item-info">
            <span class="title">{{ newsItem.title }}</span>
            <span class="date">{{ newsItem.date || '未指定日期' }}</span>
          </div>
        </button>
      </div>
      <el-button @click="getAllNews" class="view-more-btn">查看更多</el-button>
    </div>

    <el-divider></el-divider>

    <div class="knowledge">
      <div class="section-title">食品安全知识</div>
      <div class="content-container">
        <button class="content-item"
                v-for="knowledgeItem in form.knowledge"
                :key="knowledgeItem.id"
                @click="showKnowledgeDetail(knowledgeItem)">
          <el-image class="image" :src="knowledgeItem.photo_url[0]"/>
          <div class="item-info">
            <span class="title">{{ knowledgeItem.title }}</span>
            <span class="date">{{ knowledgeItem.date || '未指定日期' }}</span>
          </div>
        </button>
      </div>
      <el-button @click="getAllKnowledge" class="view-more-btn">查看更多</el-button>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  padding: 20px;
  height: 100vh;
  background-color: #f9f9f9;
}

.news,
.knowledge {
  flex: 1;
  width: 100%;
  margin-bottom: 20px;
}

.section-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.content-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.content-item {
  background-color: #fff;
  border-radius: 10px;
  width: 280px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  overflow: hidden;
  transition: transform 0.3s ease;
}

.content-item:hover {
  transform: translateY(-5px);
}

.image {
  width: 100%;
  height: 180px;
  object-fit: cover;
}

.item-info {
  padding: 10px;
  text-align: center;
}

.title {
  display: block;
  font-size: 1rem;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

.date {
  display: block;
  font-size: 0.875rem;
  color: #777;
}

.view-more-btn {
  width: 100%;
  margin-top: 20px;
  text-align: center;
  font-size: 1rem;
  background-color: #4CAF50;
  color: white;
  border-radius: 5px;
}

.view-more-btn:hover {
  background-color: #45a049;
}
</style>
