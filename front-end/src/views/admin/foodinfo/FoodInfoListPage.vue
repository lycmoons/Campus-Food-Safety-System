<script setup>
import {onMounted, ref} from "vue";
// import { UserFilled, Document, Message, PieChart, SwitchButton } from '@element-plus/icons-vue';
import router from "@/router/index.js";
import {get, getWithToken, post, postWithToken} from "@/net/index.js"
import {ElMessageBox, ElMessage} from "element-plus";
import axios from "axios";
    
const foodInfos = ref([]);
const selectedFoodInfos = ref([]);
const pageSize = ref(20); // 每页显示条数
const currentPage = ref(1); // 当前页码
const totalLogs = ref(0); // 数据总条数
const loading = ref(false); // 加载状态
const allData = ref([]);


const generateData = () => {
  const data = [];
  getWithToken('/api/food/all-food',(response) => {
    for(const item of response) {
      data.push({
        id: item.id,
        name: item.name,
        photo_url: item.photo_url,
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

// 跳转到食品信息详细页面并传递参数
const goToFoodInfoDetail = (foodInfo) => {
  router.push({
    name: "foodinfoDetail",
    params: {
      id: foodInfo.id,
      name: foodInfo.name,
      photo_url: JSON.stringify(foodInfo.photo_url),
    },
  });
};

function showProduction(food) {
  router.push({
    name: "foodproduct",
    params: {
      id: food.id,
      name: food.name,
      photo_url: JSON.stringify(food.photo_url),
    },
  })
}

function showTransportation(food) {
  router.push({
    name: "foodtrans",
    params: {
      id: food.id,
      name: food.name,
      photo_url: JSON.stringify(food.photo_url),
    },
  })
}

// 更新当前页数据
const updatePageData = () => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  foodInfos.value = allData.value.slice(start, end);
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

function deleteSelectedFoodInfos(){
  ElMessageBox.confirm("确认删除选中的食品信息吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "danger",
  }).then(() => {

    postWithToken('/api/food/delete-food',{
      foodIds: selectedFoodInfos.value
    }, () => {
      ElMessage.success("选中的食品信息已删除！");
      selectedFoodInfos.value = []
      generateData()
    })

  }).catch(() => {
    ElMessage.info("删除已取消");
  });

}

</script>


<template>
  <el-container style="height: 100%; width: 100%">
    <!-- Header -->
      <el-header style="display: flex; justify-content: space-between; align-items: center;">
        <h3>食品信息列表</h3>
      </el-header>
  
    <el-main>
      <el-container class="deleteButton-container">
        <el-button type="danger" :disabled="!selectedFoodInfos.length" @click="deleteSelectedFoodInfos">删除选中食品信息</el-button>
      </el-container>
        <el-table v-loading="loading" :data="foodInfos" border style="width: 100%; height: 680px;" @selection-change="selectedFoodInfos = $event.map(item => item.id)">
          <el-table-column type="selection" width="55" sortable show-overflow-tooltip />
          <el-table-column prop="id" label="食品ID" width="100" sortable show-overflow-tooltip />
          <el-table-column prop="name" label="食品名称" sortable show-overflow-tooltip />
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button type="primary" size="small" @click="showProduction(scope.row)">生产端信息</el-button>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button type="primary" size="small" @click="showTransportation(scope.row)">运输端信息</el-button>
            </template>
          </el-table-column><el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="goToFoodInfoDetail(scope.row)">消费端信息</el-button>
          </template>
        </el-table-column>
        </el-table>

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

.deleteButton-container{
  display: flex;
  justify-content: flex-end;
  margin-bottom: 5px;
}

.pagination-container {
  display: flex;
  justify-content:flex-end;
  padding-top: 10px;
}
</style>