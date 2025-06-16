<script setup>
import {onMounted, ref} from "vue";
import {getWithToken} from "@/net/index.js";

    
const adminlogs = ref([]); // 当前页数据
const pageSize = ref(20); // 每页显示条数
const currentPage = ref(1); // 当前页码
const totalLogs = ref(0); // 数据总条数
const loading = ref(false); // 加载状态

// 模拟的总数据，写连接的时候删掉
const allData = ref([]);


const generateData = () => {
  const data = [];
  getWithToken('/api/log/all-log', (response)=>{
    for (const item of response){
      data.push({
        id: item.id,
        table_name: item.table_name,
        message: item.message,
        date: (new Date(item.date)).toLocaleString()
      })
    }
    allData.value = data
    totalLogs.value = allData.value.length
    updatePageData()
  })
};

// 更新当前页数据
const updatePageData = () => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  adminlogs.value = allData.value.slice(start, end);
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

// 初始化数据
onMounted(() => {
  generateData()
});
</script>
    
<template>
  <el-container style="height: 100%; width: 100%">
      <!-- Header -->
    <el-header style="display: flex; justify-content: space-between; align-items: center;">
      <h3>管理员修改日志</h3>
    </el-header>

    <el-main>
      <el-table :data="adminlogs" v-loading="loading" border style="width: 100%; height: 700px; overflow-y: auto;" stripe>
        <el-table-column prop="id" label="日志ID" sortable width="100" show-overflow-tooltip />
        <el-table-column prop="table_name" label="板块" sortable width="160" show-overflow-tooltip />
        <el-table-column prop="message" label="标题" sortable show-overflow-tooltip />
        <el-table-column prop="date" label="修改日期" sortable width="150" show-overflow-tooltip />
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