<script setup>
import { reactive, ref } from "vue";
import { Close } from '@element-plus/icons-vue';
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import axios from "axios";
import router from "@/router/index.js";
import {postWithMultipart} from "@/net/index.js";

// 表单数据
const form = reactive({
  name: null,
  manufacturer: null,
  batch_num: null,
  product_date: null,
  shelf_life: null,
  carrier: null,
  start_time: null,
  end_time: null,
  image: null,
  temperature: null,
  humidity: null
});

// 表单校验规则
const rules = {
  name: [{ required: true, message: '请输入食品名称', trigger: 'blur' }],
  manufacturer: [{ required: true, message: '请输入生产商', trigger: 'blur' }],
  batch_num: [{ required: true, message: '请输入批次号', trigger: 'blur' }],
  product_date: [{ required: true, message: '请输入生产日期', trigger: 'blur' }],
  shelf_life: [{ required: true, message: '请输入保质期（月）', trigger: 'blur' }],
  carrier: [{ required: true, message: '请输入承运商', trigger: 'blur' }],
  start_time: [{ required: true, message: '请输入运输起始时间', trigger: 'blur' }],
  end_time: [{ required: true, message: '请输入运输结束时间', trigger: 'blur' }],
  image: [{ required: true, message: '请上传食品图片', trigger: 'blur' }],
  temperature: [{ required: true, message: '请输入运输温度（°C）', trigger: 'blur' }],
  humidity: [{ required: true, message: '请输入运输湿度（%）', trigger: 'blur' }],

};

const formRef = ref();

// 状态选项
const statusOptions = [
  { label: "high-level", value: "high-level" },
  { label: "low-level", value: "low-level" },
];

// 提交表单
const submitForm = () => {

  formRef.value.validate((valid) => {
    if (valid){
      const formData = new FormData()
      formData.append('name', form.name)
      formData.append('manufacturer', form.manufacturer)
      formData.append('batch_num', form.batch_num)
      formData.append('product_date', form.product_date.toLocaleString())
      formData.append('shelf_life', form.shelf_life)
      formData.append('carrier', form.carrier)
      formData.append('start_time', form.start_time.toLocaleString())
      formData.append('end_time', form.end_time.toLocaleString())
      formData.append('image', form.image)
      formData.append('temperature', form.temperature)
      formData.append('humidity', form.humidity)


      postWithMultipart('/api/food/add-food', formData, ()=>{
        ElMessage.success('食品信息录入成功')
        router.push('/admin/foodinfoManage/foodinfoList')
      })
    }
    else {
      ElMessage.warning('请完善表单内容')
    }
  })
};

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields();
    // 手动清空文件输入框的值
    const fileInput = document.querySelector('.file-input');
    if (fileInput) {
      fileInput.value = '';  // 清空文件输入框
    }
    ElMessage.success("表单已重置");
  } else {
    ElMessage.error("表单未绑定");
  }
};

// 处理图片选择
const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    form.image = file
  }
}
</script>

<template>
  <el-container style="height: 100%; width: 100%">
    <!-- Header -->
    <el-header style="display: flex; justify-content: space-between; align-items: center;">
      <h3>食品信息录入</h3>
    </el-header>

    <!-- Main Content -->
    <el-main>
      <el-card style="width: 80%; margin-bottom: 20px;">
        <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" style="width: 60%; margin: 0 auto;">

          <el-form-item label="食品名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入食品名称" />
          </el-form-item>


          <el-form-item label="制造商" prop="manufacturer">
            <el-input v-model="form.manufacturer" placeholder="请输入制造商" />
          </el-form-item>


          <el-form-item label="批次号" prop="batch_num">
            <el-input v-model="form.batch_num" placeholder="请输入批次号" />
          </el-form-item>


          <el-form-item label="生产日期" prop="product_date">
            <el-date-picker v-model="form.product_date" type="datetime" placeholder="请选择日期" style="width: 100%;" />
          </el-form-item>


          <el-form-item label="保质期" prop="shelf_life">
            <el-input v-model="form.shelf_life" type="number" placeholder="请输入保质期（月）"/>
          </el-form-item>


          <el-form-item label="承运商" prop="carrier">
            <el-input v-model="form.carrier" placeholder="请输入承运商"/>
          </el-form-item>


          <el-form-item label="运输起始时间" prop="start_time">
            <el-date-picker v-model="form.start_time" type="datetime" placeholder="请选择日期" style="width: 100%;" />
          </el-form-item>


          <el-form-item label="运输结束时间" prop="end_time">
            <el-date-picker v-model="form.end_time" type="datetime" placeholder="请选择日期" style="width: 100%;" />
          </el-form-item>


          <el-form-item label="运输温度" prop="temperature">
            <el-input v-model="form.temperature" type="number" placeholder="请输入运输温度"/>
          </el-form-item>


          <el-form-item label="运输湿度" prop="humidity">
            <el-input v-model="form.humidity" type="number" placeholder="请输入运输湿度"/>
          </el-form-item>


          <el-form-item label="图片上传" prop="image">
            <input type="file" @change="handleFileChange" accept="image/*" class="file-input" />
          </el-form-item>


          <el-form-item>
            <el-button type="primary" @click="submitForm">提交</el-button>
            <el-button type="warning" @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
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

.el-card {
  margin: 10px auto;
  max-width: 1000px;
  box-shadow: 0 4px 12px rgba(53, 4, 4, 0.1);
  background-color: #f9fafc;
}

.input,
.textarea,
.file-input {
  width: 95%;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
</style>
