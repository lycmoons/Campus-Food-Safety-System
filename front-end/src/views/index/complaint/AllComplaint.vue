<script setup>

import {computed, onMounted, reactive} from "vue";
import {getWithToken} from "@/net/index.js";

function getAllComplaint(){
  getWithToken('/api/complaint/all-complaint', (data) => {
    for (const item of data){
      form.complaint.push({
        id: item.id,
        content: item.content,
        photo_url: item.photo_url,
        date: (new Date(item.date)).toLocaleString(),
        accepted: item.accepted === 'false' ? '未受理' : '已受理'
      })
    }
  })
}

onMounted(() => {
  getAllComplaint()
})

let form = reactive({
  complaint: []
})

</script>

<template>
  <div class="complaint-list">
    <!-- 遍历投诉信息 -->
    <div class="complaint-item" v-for="item in form.complaint" :key="item.id">
      <div class="complaint-date">日期：{{ item.date || '未指定日期'}}</div>
      <div class="complaint-date">受理：{{ item.accepted }}</div>
      <div class="complaint-content">内容：{{ item.content }}</div>
      <div class="complaint-photo"
           v-for="(photo, index) in item.photo_url"
           :key="index">
        <img :src="photo" alt="投诉图片" />
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 外层容器，流式布局 */
.complaint-list {
  display: flex;
  flex-direction: column; /* 纵向排列 */
  gap: 20px; /* 每条投诉之间的间隔 */
  padding: 20px;
  max-width: 600px; /* 可选，设置容器最大宽度 */
  margin: 0 auto; /* 居中显示 */
  background-color: #f9f9f9;
  border: 1px solid #ccc;
  border-radius: 10px;
}

/* 每条投诉的样式 */
.complaint-item {
  display: flex;
  flex-direction: column;
  gap: 10px; /* 每部分之间的间距 */
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #ffffff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

/* 投诉日期的样式 */
.complaint-date {
  font-size: 14px;
  color: #555;
  font-weight: bold;
}

/* 投诉内容的样式 */
.complaint-content {
  font-size: 16px;
  color: #333;
  line-height: 1.5;
}

/* 投诉图片的样式 */
.complaint-photo img {
  max-width: 100%; /* 图片宽度自适应 */
  height: auto;
  border-radius: 5px;
  border: 1px solid #ddd;
}
</style>