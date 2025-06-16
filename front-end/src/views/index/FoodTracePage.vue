<script setup>
import {computed, onMounted, reactive} from "vue";
import {getWithToken, postWithToken} from "@/net/index.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

function production(food){
  router.push({
    name: 'production',
    params: {
      id: food.id,
      name: food.name,
      photo_url: JSON.stringify(food.photo_url)
    }
  })
}

function transportation(food){
  router.push({
    name: 'transportation',
    params: {
      id: food.id,
      name: food.name,
      photo_url: JSON.stringify(food.photo_url)
    }
  })
}

function consumer(food){
  router.push({
    name: 'consumer',
    params: {
      id: food.id,
      name: food.name,
      photo_url: JSON.stringify(food.photo_url)
    }
  })
}

function getAllFood(){
  getWithToken('/api/food/all-food',(data) => {
    for (const item of data){
      form.food.push({
        id: item.id,
        name: item.name,
        photo_url: item.photo_url,
      })
    }
  })
}

onMounted(() => {
  getAllFood()
})

let form = reactive({
  food: [],
  searchQuery: "", // 搜索框的输入内容
  expandedId: null, // 当前展开的食物项ID
})

// 计算属性：根据搜索框内容过滤数据
const filteredFood = computed(() => {
  if (!form.searchQuery) {
    return form.food;
  }
  return form.food.filter((item) =>
      item.name.includes(form.searchQuery)
  );
})

// 切换展开项的方法
const toggleExpand = (id) => {
  form.expandedId = form.expandedId === id ? null : id;
}
</script>

<template>
  <div class="page-container">
    <h1>食品溯源列表</h1>

    <div class="search-container">
      <el-input
          v-model="form.searchQuery"
          placeholder="搜索食物名称..."
          clearable
          class="search-input"
      >
        <template #prefix>
          <i class="el-icon-search"></i>
        </template>
      </el-input>
    </div>

    <div class="food-list">
      <div
          v-for="item in filteredFood"
          :key="item.id"
          class="food-card"
          :class="{ 'expanded': form.expandedId === item.id }"
      >
        <div class="card-header" @click="toggleExpand(item.id)">
          <h2>{{ item.name }}</h2>
          <i
              class="el-icon-arrow-down expand-icon"
              :class="{ 'expanded': form.expandedId === item.id }"
          ></i>
        </div>

        <div v-if="form.expandedId === item.id" class="card-content">
          <div class="image-container">
            <el-image
                :src="item.photo_url"
                fit="cover"
                class="food-image"
            />
          </div>

          <div class="action-buttons">
            <el-button
                type="primary"
                @click="production(item)"
                class="action-btn"
            >
              生产端信息
            </el-button>
            <el-button
                type="primary"
                @click="transportation(item)"
                class="action-btn"
            >
              运输端信息
            </el-button>
            <el-button
                type="primary"
                @click="consumer(item)"
                class="action-btn"
            >
              消费端信息
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  color: #2c3e50;
  text-align: center;
  margin-bottom: 30px;
}

.search-container {
  margin-bottom: 25px;
}

.search-input {
  width: 100%;
}

.search-input :deep(.el-input__inner) {
  border-radius: 20px;
  padding-left: 35px;
}

.food-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.food-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: all 0.3s ease;
}

.food-card.expanded {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.card-header {
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  background-color: #f8f9fa;
  transition: background-color 0.3s;
}

.card-header:hover {
  background-color: #e9ecef;
}

.card-header h2 {
  margin: 0;
  color: #34495e;
  font-size: 18px;
}

.expand-icon {
  transition: transform 0.3s;
}

.expand-icon.expanded {
  transform: rotate(180deg);
}

.card-content {
  padding: 20px;
}

.image-container {
  height: 200px;
  margin-bottom: 20px;
  border-radius: 6px;
  overflow: hidden;
}

.food-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.action-buttons {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.action-btn {
  background-color: #3498db;
  border-color: #3498db;
}

.subscription-actions {
  display: flex;
  gap: 10px;
  border-top: 1px solid #eee;
  padding-top: 15px;
}

.subscription-btn {
  flex: 1;
}

@media (max-width: 768px) {
  .page-container {
    padding: 15px;
  }

  .action-buttons {
    flex-direction: column;
  }

  .action-btn, .subscription-btn {
    width: 100%;
  }
}
</style>