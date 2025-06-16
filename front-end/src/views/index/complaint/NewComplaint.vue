<script setup>
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import {postWithMultipart} from "@/net/index.js";

function sendComplaint(){
  if(form.content===''||form.content===null){
    ElMessage.warning('投诉内容不能为空')
  }
  else if(form.selectedPhoto.length === 0){
    ElMessage.warning('必须上传投诉的图片')
  }
  else {
    const formData = new FormData()
    form.selectedPhoto.forEach((file) => {
      formData.append('files', file)
    })
    formData.append('content', form.content)

    postWithMultipart('api/complaint/send-complaint', formData, () => {
      handleSuccess()
    })
  }
}

function handleSuccess(){
  ElMessage.success('投诉成功')

  // 重置用户输入
  form.selectedPhoto = []
  form.content = ''
}

let form = reactive({
  selectedPhoto: [],
  content:''
})

// 处理图片选择
const handleFileChange = (event) => {
  const files = event.target.files;
  if (files) {
    for (let i = 0; i < files.length; i++) {
      form.selectedPhoto.push(files[i]);
    }
  }
}

</script>

<template>
  <div class="container">
    <!-- 投诉内容输入框 -->
    <div class="form-item">
      <label for="content">投诉内容：</label>
      <textarea
          id="content"
          v-model="form.content"
          placeholder="请输入内容"
          rows="10"
          class="textarea"
      ></textarea>
    </div>

    <!-- 图片上传部分 -->
    <div class="form-item">
      <label for="photo">图片上传：</label>
      <input
          id="photo"
          type="file"
          multiple
          @change="handleFileChange"
          accept="image/*"
          class="file-input"
      />
    </div>

    <!-- 投诉按钮 -->
    <div class="form-item">
      <button @click="sendComplaint" class="submit-btn">投诉</button>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 90%;
  height: auto;
  margin: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #f9f9f9;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 95%;
}

label {
  font-weight: bold;
  font-size: 16px;
}

.textarea,
.file-input {
  width: 100%;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.textarea {
  resize: none;
}

.submit-btn {
  background-color: #007bff;
  color: white;
  padding: 10px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.submit-btn:hover {
  background-color: #0056b3;
}
</style>