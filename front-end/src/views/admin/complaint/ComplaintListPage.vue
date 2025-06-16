<script setup>
import {onMounted, ref} from "vue";
// import { UserFilled, Document, Message, PieChart, SwitchButton } from '@element-plus/icons-vue';
import router from "@/router/index.js";
import {get, post} from "@/net/index.js"
import {ElMessage} from "element-plus";
import axios from "axios";


const complaints = ref([]); // 投诉列表数据
const pageSize = ref(20); // 每页显示条数
const currentPage = ref(1); // 当前页码
const totalLogs = ref(0); // 数据总条数
const loading = ref(false); // 加载状态
const allData = ref([]);

const generateData = () => {
  const data = []
  get('/api/complaint/total-complaint', (response)=>{
    for(const item of response){
      let formatScore = ''
      if (item.score > 0.5)
        formatScore = item.score + '  (正面情绪)'
      else if (item.score <= 0.2)
        formatScore = item.score + '  (高危情绪)'
      else
        formatScore = item.score + '  (负面情绪)'

      data.push({
        id: item.id,
        content: item.content,
        photo_url: item.photo_url,
        date: (new Date(item.date)).toLocaleString(),
        accepted: item.accepted,
        score: formatScore
      })
    }
    allData.value = data
    totalLogs.value = allData.value.length
    updatePageData()
  })
};

// 初始化数据
onMounted(() => {
  generateData()
});

// 跳转到投诉受理页面并传递参数
const goToComplaintAcceptance = (complaint) => {
  router.push({
    path: "/admin/complaintManage/complaintAcceptance",
    query: {
      id: complaint.id,
      accepted: complaint.accepted,
      date: complaint.date,
      content: complaint.content,
      photo_url: complaint.photo_url,
    },
  });
};

// 更新当前页数据
const updatePageData = () => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  complaints.value = allData.value.slice(start, end);
};

// 监听分页变化
const handleSizeChange = (size) => {
  pageSize.value = size; // 更新每页条数
  currentPage.value = 1; // 重置到第1页
  updatePageData();
};

const handleCurrentChange = (page) => {
  currentPage.value = page; // 更新当前页码
  updatePageData();
};
</script>

<template>
  <el-container style="height: 100%; width: 100%;">
    <!-- Header -->
    <el-header style="display: flex; justify-content: space-between; align-items: center;">
      <h3>食品安全投诉列表</h3>
    </el-header>

    <!-- Main Content -->
    <el-main>
      <!-- 投诉列表 -->
      <el-table :data="complaints" v-loading="loading" border style="width: 100%; height: 700px; overflow-y: auto;">
        <el-table-column prop="id" label="投诉ID" sortable width="100" show-overflow-tooltip />
        <el-table-column prop="content" label="投诉内容" sortable show-overflow-tooltip />
        <el-table-column prop="date" label="投诉时间" sortable width="180" show-overflow-tooltip />
        <el-table-column prop="score" label="情感分析评分" sortable width="180" show-overflow-tooltip />
        <el-table-column prop="accepted" label="受理状态" sortable width="120" show-overflow-tooltip />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="goToComplaintAcceptance(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页控件 -->
      <div class="pagination-container">
        <el-pagination background layout="sizes, prev, pager, next, total" 
                      :total="totalLogs" :page-sizes="[20, 30, 50]" 
                      :page-size="pageSize" :current-page="currentPage"
                      @size-change="handleSizeChange"
                      @current-change="handleCurrentChange"
                      style="margin-top: 15px; text-align: right;" />
      </div>
    </el-main>
  </el-container>
</template>

<style scoped>
.el-header {
  background-color: #f5f5f5;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
  padding: 0 20px;
}

.el-main {
  padding: 20px;
}

.pagination-container {
  display: flex;
  justify-content:flex-end;
  padding-top: 10px;
}
</style>