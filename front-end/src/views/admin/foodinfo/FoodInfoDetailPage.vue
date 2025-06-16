<script setup>
import {onMounted, reactive, ref} from "vue";
import {useRoute} from "vue-router";
import router from "@/router/index.js";
import {postWithToken} from "@/net/index.js";
import {ElMessage} from "element-plus";

const route = useRoute();
let form = reactive({
  food: {
    id: route.params.id,
    name: route.params.name,
    photo_url: JSON.parse(route.params.photo_url),
    dish: [],
  },
  counter: null,
  dish: null
});

const formRef = ref();

function deleteConsumer(counter) {
  postWithToken('/api/food/delete-consumer', {
    id: form.food.id,
    food_counter: counter[0],
    dish: counter[1]
  }, () => {
    router.go(0)
  })
}

function addConsumer() {
  if(form.counter!=null&&form.counter!==''&&form.dish!=null&&form.dish!==''){
    postWithToken('/api/food/add-consumer', {
      id: form.food.id,
      food_counter: form.counter,
      dish: form.dish
    }, () => {
      router.go(0)
    })
  }
  else {
    ElMessage.warning("请输入完整信息")
  }
}

function getConsumerInfo() {
  postWithToken('/api/food/consumer-info', {
    id: form.food.id
  }, (data) => {
    for (const item of data) {
      form.food.dish.push([item.food_counter, item.dish])
    }
  })
}

onMounted(() => {
  getConsumerInfo();
});

function goBack() {
  router.back()
}
</script>

<template>
  <div class="page-container">
    <div class="header">
      <h1>食品消费端信息</h1>
      <el-button type="primary" @click="goBack" class="back-btn">返回</el-button>
    </div>

    <div class="card-container">
      <div class="info-card">
        <div class="image-container">
          <el-image :src="form.food.photo_url" fit="cover" class="food-image"/>
        </div>
        <div class="info-content">
          <h2>{{ form.food.name }}</h2>

          <div class="info-section">
            <h3>供应信息</h3>
            <div v-for="(counter, index) in form.food.dish" :key="index" class="supply-item">
              <div class="supply-info">
                <p><span class="label">食堂窗口：</span>{{ counter[0] }}</p>
                <p><span class="label">供应菜品：</span>{{ counter[1] }}</p>
              </div>
              <el-button
                  type="danger"
                  @click="deleteConsumer(counter)"
                  class="delete-btn"
                  size="small"
              >
                删除
              </el-button>
            </div>
          </div>

          <div class="add-section">
            <h3>添加消费端信息</h3>
            <el-form
                ref="formRef"
                :model="form"
                label-position="top"
                class="add-form"
            >
              <el-form-item label="窗口" prop="counter">
                <el-input
                    v-model="form.counter"
                    placeholder="请输入窗口名称"
                    clearable
                />
              </el-form-item>
              <el-form-item label="菜名" prop="dish">
                <el-input
                    v-model="form.dish"
                    placeholder="请输入菜品名称"
                    clearable
                />
              </el-form-item>
              <el-button
                  type="primary"
                  @click="addConsumer"
                  class="add-btn"
              >
                新增菜品
              </el-button>
            </el-form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

h1 {
  color: #2c3e50;
  margin: 0;
}

.card-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.info-card {
  display: flex;
  flex-direction: column;
}

.image-container {
  height: 300px;
  overflow: hidden;
}

.food-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info-content {
  padding: 25px;
}

h2 {
  color: #34495e;
  margin-top: 0;
  border-bottom: 1px solid #eee;
  padding-bottom: 15px;
}

.info-section {
  margin-top: 20px;
}

h3 {
  color: #3498db;
  margin-bottom: 15px;
}

.supply-item {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 6px;
  margin-bottom: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.supply-info {
  flex: 1;
}

.supply-item p {
  margin: 5px 0;
}

.label {
  font-weight: bold;
  color: #555;
  display: inline-block;
  width: 100px;
}

.delete-btn {
  margin-left: 15px;
}

.add-section {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.add-form {
  margin-top: 20px;
}

.add-form :deep(.el-form-item__label) {
  font-weight: bold;
  color: #555;
}

.add-btn {
  width: 100%;
  margin-top: 10px;
}

.back-btn {
  background-color: #3498db;
  border-color: #3498db;
}

@media (max-width: 768px) {
  .info-card {
    flex-direction: column;
  }

  .image-container {
    height: 200px;
  }

  .supply-item {
    flex-direction: column;
    align-items: flex-start;
  }

  .delete-btn {
    margin-left: 0;
    margin-top: 10px;
    align-self: flex-end;
  }
}
</style>