<script setup>
import { onMounted, reactive, ref } from "vue";
import router from "@/router/index.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRoute } from "vue-router";
import {postWithToken} from "@/net/index.js";

// 路由参数
const route = useRoute();

// 知识库文章详情数据（模拟后端返回数据）
const knowledgeDetail = reactive({
  id: route.query.id || "未知",
  title: route.query.title || "未知",
  category: route.query.category || "未知",
  content: route.query.content || "未知",
  date: route.query.date || "未知",
  photo_url: JSON.parse(route.query.photo_url) || ["未知"],
});

// 删除文章
const deleteKnowledge = () => { 
  ElMessageBox.confirm("确认删除这篇文章吗？", "提示", { 
    confirmButtonText: "确定",
    cancelButtonText: "取消", 
    type: "warning", 
  }).then(() => {
    postWithToken('/api/knowledge/delete-knowledge',{
      knowledgeIds: [knowledgeDetail.id]
    }, () => {
      ElMessage.success("推文已删除！");
      router.push('/admin/articleManage/articleList')
    })
  }).catch(() => { 
    ElMessage.info("删除已取消"); 
  }); 
};
</script>

<template>
  <el-container>
    <!-- Header -->
    <el-header style="display: flex; justify-content: space-between; align-items: center;">
      <h3>食品安全知识库详情</h3>
      <el-button type="primary" @click="$router.go(-1)">返回</el-button>
    </el-header>

    <!-- Main Content -->
    <el-main style="display: flex; flex-direction: column; align-items: center;">
      <!-- 基本信息 -->
      <el-card style="width: 80%; margin-bottom: 20px;">
        <template #header>
          <span style="font-size: 16px; font-weight: bold;">知识库文章基本信息</span>
        </template>
        <el-table :data="[knowledgeDetail]" border>
          <el-table-column prop="id" label="文章ID" width="100" show-overflow-tooltip />
          <el-table-column prop="title" label="文章标题" show-overflow-tooltip />
          <el-table-column prop="category" label="文章类别" width="180" show-overflow-tooltip />
          <el-table-column prop="date" label="文章发表时间" width="180" show-overflow-tooltip />
        </el-table>
      </el-card>

      <!-- 文章内容 -->
      <el-card style="width: 80%; margin-bottom: 20px; max-height: 600px; overflow-y: auto;">
        <template #header>
          <span style="font-size: 16px; font-weight: bold;">知识库文章内容</span>
          <el-button type="danger" style="float: right; margin-right: 10px;" @click="deleteKnowledge">删除</el-button>
        </template>
        <div>
          <p style="white-space: pre-line;">{{ knowledgeDetail.content }}</p>
          <div v-if="knowledgeDetail.photo_url" style="margin-top: 10px;">
            <el-image v-for="(url, index) in knowledgeDetail.photo_url" :key="index" :src="url.trim()" style="width: 200px; height: 200px; margin-right: 10px;" fit="cover" />
          </div>
        </div>
      </el-card>
    </el-main>
  </el-container>
</template>


<style scoped>
.el-header {
  background-color: #f5f5f5;
  font-size: 18px;
  font-weight: bold;
  padding: 0 20px;
}

.el-main {
  background-color: #ffffff;
  padding: 20px;
}

.el-card {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>
