<script setup>
import {onMounted, reactive} from "vue";
import {useRoute} from "vue-router";
import router from "@/router/index.js";
import {postWithToken} from "@/net/index.js";

const route = useRoute();
let form = reactive({
  food: {
    id: route.params.id,
    name: route.params.name,
    photo_url: JSON.parse(route.params.photo_url),
    carrier: "",
    start_time: "",
    end_time: "",
    temperature: "",
    humidity: ""
  },
});

function getTransportationInfo() {
  postWithToken('/api/food/transportation-info', {
    id: form.food.id
  }, (data) => {
    form.food.carrier = data.carrier
    form.food.start_time = new Date(data.start_time).toLocaleString()
    form.food.end_time = new Date(data.end_time).toLocaleString()
    form.food.temperature = data.temperature
    form.food.humidity = data.humidity
  })
}

onMounted(() => {
  getTransportationInfo();
});

function goBack() {
  router.back()
}
</script>

<template>
  <div class="page-container">
    <div class="header">
      <h1>食品运输端信息</h1>
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
            <h3>运输信息</h3>
            <div class="info-grid">
              <div class="info-item">
                <span class="label">承运商家：</span>
                <span>{{ form.food.carrier }}</span>
              </div>
              <div class="info-item">
                <span class="label">运输起始时间：</span>
                <span>{{ form.food.start_time }}</span>
              </div>
              <div class="info-item">
                <span class="label">抵达时间：</span>
                <span>{{ form.food.end_time }}</span>
              </div>
              <div class="info-item">
                <span class="label">运输温度：</span>
                <span>{{ form.food.temperature }}°C</span>
              </div>
              <div class="info-item">
                <span class="label">运输湿度：</span>
                <span>{{ form.food.humidity }}%</span>
              </div>
            </div>
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

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 15px;
}

.info-item {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 6px;
}

.label {
  font-weight: bold;
  color: #555;
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

  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>