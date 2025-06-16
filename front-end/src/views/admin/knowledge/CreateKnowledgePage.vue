<script setup>
import { reactive, ref } from "vue";
import { Close } from '@element-plus/icons-vue';
import { ElMessage } from "element-plus";
import router from "@/router/index.js";
import {post, postWithMultipart} from "@/net/index.js";

// 表单数据
const form = reactive({
  title: "",
  content: "",
  category: "",
  photo_url: [],
});

const categories = ref(["商超", "食品信息化", "健康", "餐饮", "零食"]);

const formRef = ref();
const previewImages = ref([]);
const uploadedFiles = ref([]);

const rule = {
  title: [{ required: true, message: '请输入标题', trigger: ['blur', 'change'] }],
  content: [{ required: true, message: '请输入内容', trigger: ['blur', 'change'] }],
  category: [{ required: true, message: '请输入类别', trigger: ['blur', 'change'] }],
};

// 图片上传前的处理
const handleBeforeUpload = (file) => {
  const isImage = file.type.startsWith("image/");
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isImage) {
    ElMessage.error("只能上传图片文件！");
    return false;
  }
  if (!isLt2M) {
    ElMessage.error("图片大小不能超过 2MB！");
    return false;
  }

  // 生成预览 URL 并存储到数组
  const reader = new FileReader();
  reader.onload = () => {
    previewImages.value.push(reader.result);
    uploadedFiles.value.push(file);
    updatePhotoUrl();
  };
  reader.readAsDataURL(file);
  return false; // 取消自动上传
};

// 移除图片
const handleRemoveImage = (index) => {
  previewImages.value.splice(index, 1);
  uploadedFiles.value.splice(index, 1);
  updatePhotoUrl();
};

// 更新 photo_url 字符串
const updatePhotoUrl = () => {
  const fileNames = uploadedFiles.value.map((file) => file.name);
  form.photo_url = fileNames.join(",");
};

// 提交表单
const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      const formData = new FormData();
      formData.append("title", form.title);
      formData.append("content", form.content);
      formData.append("category", form.category);
      uploadedFiles.value.forEach((file) => {
        formData.append('image', file);
      })

      postWithMultipart('/api/knowledge/add-knowledge', formData, () => {
        ElMessage.success('知识录入成功')
        router.push('/admin/knowledgeManage/knowledgeList')
      })


    } else {
      ElMessage.error("请填写完整的表单信息");
    }
  });
};

// 重置表单
const resetForm = () => {
  form.title = "";
  form.content = "";
  form.photo_url = "";
  previewImages.value = [];
  uploadedFiles.value = [];
};
</script>

<template>
  <el-container style="height: 100%; width: 100%">
    <!-- 页面标题 -->
    <el-header style="display: flex; justify-content: space-between; align-items: center;">
      <h3>食品安全知识库列表文章撰写</h3>
    </el-header>

    <el-main>
      <el-form ref="formRef" :model="form" label-position="top" :rules="rule">
        <el-card class="title-card">
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入文章标题" size="large" clearable />
          </el-form-item>
        </el-card>

        <el-card class="content-card">
          <el-form-item label="正文内容" prop="content">
            <el-input v-model="form.content" type="textarea" placeholder="请输入文章内容" :autosize="{ minRows: 8, maxRows: 13}" clearable style="resize: none; max-height: 300px; overflow: auto;" />
          </el-form-item>
        </el-card>
        
        <el-card class="category-card">
          <el-form-item label="选择类别" prop="category">
            <el-select v-model="form.category" placeholder="请选择类别" clearable size="large">
              <el-option v-for="category in categories" :key="category" :label="category" :value="category" /> 
            </el-select>
          </el-form-item>
        </el-card>

        <el-card class="upload-card">
          <el-form-item label="上传图片">
            <div class="upload-container">
              <el-upload class="upload-btn" drag :multiple="true" :show-file-list="false" :before-upload="handleBeforeUpload" accept="image/*" >
                <el-button size="small" type="primary">点击上传图片</el-button>
              </el-upload>
            </div>
            <div v-if="previewImages.length" class="image-preview-container">
              <div
                v-for="(image, index) in previewImages" :key="index" class="image-preview-box" >
                <img :src="image" alt="图片预览" class="image-preview" />
                <el-icon class="remove-icon" @click.stop="handleRemoveImage(index)">
                  <Close />
                </el-icon>
              </div>
            </div>
          </el-form-item>
        </el-card>

        <el-card class="action-card">
          <div class="button-group">
            <el-button type="primary" @click="submitForm">提交</el-button>
            <el-button type="warning" @click="resetForm">重置</el-button>
          </div>
        </el-card>
      </el-form>
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

.el-card {
  margin: 10px auto;
  max-width: 1000px;
  box-shadow: 0 4px 12px rgba(53, 4, 4, 0.1);
  background-color: #f9fafc;
}

.upload-card {
  max-height: 180px;
  display: flex;
  flex-wrap: nowrap;
  gap: 20px; 
  background-color: #f9fafc;
  margin-top: 20px;
  overflow-y: auto;
  overflow-x: auto;
}

.upload-container {
  flex: 0 0 auto; 
  display: flex;
  flex-wrap: nowrap;
  align-items: center;
  justify-content: center;
  margin-right: 50px;
}

.upload-placeholder {
  padding: 10px 10px;
  font-size: 14px;
  color: #909399;
}

.image-preview-container {
  display: flex;
  flex-wrap: nowrap;
  gap: 10px;
  padding: 10px;
  background-color: #f4f4f4;
  border: 1px solid #dcdcdc;
}

.image-preview-box {
  position: relative;
  width: 120px;
  height: 120px;
  flex-shrink: 0; 
  border: 1px solid #ccc;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fff;
}

.image-preview {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

.remove-icon {
  position: absolute;
  top: 5px;
  right: 5px;
  font-size: 18px;
  cursor: pointer;
  color: #f56c6c;
  background-color: #fff;
  border-radius: 50%;
  padding: 2px;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 100px;
  background-color: #f9fafc;
}
</style>