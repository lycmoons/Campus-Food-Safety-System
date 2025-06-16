<script setup>
import {onMounted, reactive} from "vue";
import { useRoute } from "vue-router";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";
import {postWithToken} from "@/net/index.js";


// 路由参数
const route = useRoute();

// 投诉详情数据（模拟后端返回数据）
const complaintDetail = reactive({
  id: route.query.id || "未知",
  accepted: route.query.accepted || "未知",
  date: route.query.date || "未知",
  content: route.query.content || "未知",
  photo_url: route.query.photo_url || ["未知"],
});

// 处理反馈数据
const form = reactive({
  id: route.query.id,
  content: '',
});


function goBack(){
  router.back()
}

// 发送反馈
const submitForm = () => {
  if (form.content === ''){
    ElMessage.warning('请完善表单内容')
  }
  else{
    postWithToken('/api/complaint/accept-complaint', {
      id: form.id,
      content: form.content
    }, ()=>{
      ElMessage.success('投诉受理成功')
      router.push('/admin/complaintManage/complaintList')
    })
  }
};
</script>

<template>
  <el-container>
    <!-- Header -->
    <el-header style="display: flex; justify-content: space-between; align-items: center;">
      <h3>投诉详情</h3>
      <el-button type="primary" @click="goBack">返回</el-button>
    </el-header>

    <el-main style="display: flex; flex-direction: column; align-items: center;">
      <!-- Basic Info -->
      <el-card style="width: 80%; margin-bottom: 20px;">
        <template #header>
          <span style="font-size: 16px; font-weight: bold;">投诉基本信息</span>
        </template>
        <el-table :data="[complaintDetail]" border>
          <el-table-column prop="id" label="投诉ID" />
          <el-table-column prop="accepted" label="状态" />
          <el-table-column prop="date" label="投诉时间" />
        </el-table>
      </el-card>

      <el-card :data="[complaintDetail]" style="width: 80%; margin-bottom: 20px; max-height: 400px; overflow-y: auto;">
        <template #header>
          <span style="font-size: 16px; font-weight: bold;">投诉内容</span>
        </template>
        <p style="white-space: pre-line;">{{ complaintDetail.content }}</p>
        <div v-if="complaintDetail.photo_url" style="margin-top: 10px;">
          <el-image v-for="(url, index) in complaintDetail.photo_url" :key="index" :src="url" 
                    style="width: 200px; height: 200px; margin-right: 10px; cursor: pointer;" fit="cover" 
                    @click="() => $router.push(url)" />
        </div>
      </el-card>

      <el-card v-if="complaintDetail.accepted === 'false'" style="width: 80%;">
        <template #header>
          <span style="font-size: 16px; font-weight: bold;">处理反馈</span>
        </template>
        <el-form label-width="100px">
          <el-form-item label="反馈详情">
            <el-input v-model="form.content" type="textarea" placeholder="请输入处理反馈的具体内容" :rows="4" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm">提交反馈</el-button>
          </el-form-item>
        </el-form>
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
