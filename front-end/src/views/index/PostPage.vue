<script setup>
import {onMounted, reactive} from "vue";
import {get, getToken, postWithMultipart} from "@/net/index.js";
import {ElMessage} from "element-plus";
import axios from "axios";
import router from "@/router/index.js";

function getTopPost(){
  get('/api/post/top-post', (data) => {
    for(const item of data){
      form.userPost.push({
        id: item.id,
        username: item.username,
        title: item.title,
        content: item.content,
        photo_url: item.photo_url,
        date: (new Date(item.date)).toLocaleString()
      })
    }
  })
}

function getAllPost(){
  router.push('/all-post')
}

function sendPost(){
  if (form.title === '' || form.title === null){
    ElMessage.warning('标题不能为空')
  }
  else if (form.content === '' || form.content === null){
    ElMessage.warning('内容不能为空')
  }
  else if (form.selectedPhoto.length === 0){
    ElMessage.warning('必须附加至少一张图片')
  }
  else {
    const formData = new FormData()
    form.selectedPhoto.forEach((file) => {
      formData.append('files', file)
    })
    formData.append('title', form.title)
    formData.append('content', form.content)

    postWithMultipart('/api/post/send-post', formData, () => {
      handleSuccess()
    })
  }
}

function handleSuccess(){
  ElMessage.success('帖子发布成功')

  // 输入框重置
  form.selectedPhoto = []
  form.title = ''
  form.content = ''

  // 刷新帖子推送
  form.userPost = []
  getTopPost()
}

// 页面首次加载时运行
onMounted(() => {
  getTopPost()
})

let form = reactive({
  selectedPhoto: [],
  title: '',
  content: '',

  userPost: []
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

// 显示帖子详情
function showPostDetail(post){
  router.push({
    name: 'postDetail',
    params: {
      id: post.id,
      title: post.title,
      username: post.username,
      date: post.date,
      content: post.content,
      photo_url: JSON.stringify(post.photo_url)
    }
  })
}
</script>

<template>
  <div class="container">
    <div class="title">食品安全热帖</div>
    <div class="post-view">
      <div
          v-for="post in form.userPost"
          :key="post.id"
          @click="showPostDetail(post)"
          class="postItem">
        <el-image class="image" :src="post.photo_url[0]" />
        <div class="post-info">
          <span class="post-title">{{ post.title }}</span>
          <span class="post-author">作者：{{ post.username }}</span>
        </div>
      </div>
    </div>
    <el-button @click="getAllPost" style="margin-top: 20px" class="view-more">查看更多</el-button>

    <el-divider style="margin-top: 50px;margin-bottom: 50px">我要发帖</el-divider>

    <div class="user-input">
      <!-- 帖子标题输入框 -->
      <div class="form-item">
        <label for="title">标题：</label>
        <input
            id="title"
            type="text"
            v-model="form.title"
            placeholder="请输入标题"
            class="input"
        />
      </div>

      <!-- 帖子内容输入框 -->
      <div class="form-item">
        <label for="content">内容：</label>
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

      <!-- 发送按钮 -->
      <div class="form-item">
        <button @click="sendPost" class="submit-btn">发送</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100vh;
  width: 100%;
}

.title {
  font-size: 24px;
  font-weight: bold;
  margin-top: 20px;
  margin-bottom: 30px;
  color: #333;
}

.post-view {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  width: 90%;
  margin-bottom: 50px;
}

.postItem {
  background-color: #fff;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  transition: transform 0.3s;
}

.postItem:hover {
  transform: translateY(-10px);
}

.image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.post-info {
  padding: 15px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.post-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  text-align: center;
}

.post-author {
  font-size: 14px;
  color: #777;
  text-align: center;
}

.view-more {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s;
}

.view-more:hover {
  background-color: #0056b3;
}

.user-input {
  width: 90%;
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
}

label {
  font-weight: bold;
  font-size: 16px;
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
