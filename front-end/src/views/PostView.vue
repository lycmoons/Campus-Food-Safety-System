<script setup>
import { onMounted, reactive } from "vue";
import { useRoute } from "vue-router";
import router from "@/router/index.js";
import { post, postWithToken } from "@/net/index.js";
import { ElMessage } from "element-plus";

const route = useRoute();

function getAllComment() {
  post("/api/comment/get-comment", { post_id: form.post.id }, (data) => {
    for (const item of data) {
      form.comment.push({
        id: item.id,
        username: item.username,
        content: item.content,
        date: new Date(item.date).toLocaleString(),
      });
    }
  });
}

function sendComment() {
  if (form.myComment === null || form.myComment === "") {
    ElMessage.warning("评论内容不能为空");
  } else {
    postWithToken(
        "/api/comment/send-comment",
        {
          post_id: form.post.id,
          content: form.myComment,
        },
        () => {
          ElMessage.success("评论发送成功");
          form.myComment = "";

          form.comment = []
          getAllComment()
        }
    );
  }
}

onMounted(() => {
  getAllComment();
});

let form = reactive({
  post: {
    id: route.params.id,
    title: route.params.title,
    username: route.params.username,
    date: route.params.date,
    content: route.params.content,
    photo_url: JSON.parse(route.params.photo_url),
  },

  comment: [],

  myComment: "",
});

function goBack() {
  router.back();
}
</script>

<template>
  <div class="container">
    <!-- 帖子内容 -->
    <el-divider>帖子详情</el-divider>
    <el-card shadow="hover" class="post-card">
      <div class="post-header">
        <h2>{{ form.post.title }}</h2>
        <p>
          <span>作者：{{ form.post.username }}</span>
          <span class="date">发布时间：{{ form.post.date }}</span>
        </p>
      </div>
      <el-divider></el-divider>
      <div class="post-content">{{ form.post.content }}</div>
      <el-divider>配图</el-divider>
      <el-row :gutter="10" class="photo-gallery">
        <el-col
            v-for="(photo, index) in form.post.photo_url"
            :key="index"
            :span="8"
        >
          <el-image
              :src="photo"
              fit="cover"
              class="photo"
          />
        </el-col>
      </el-row>
    </el-card>



    <!-- 评论内容 -->
    <el-divider>评论区</el-divider>
    <el-card shadow="never" class="comment-card">
      <div
          v-for="comment in form.comment"
          :key="comment.id"
          class="comment-item"
      >
        <p class="comment-header">
          <span class="username">{{ comment.username }}</span>
          <span class="comment-date">{{ comment.date }}</span>
        </p>
        <p class="comment-content">{{ comment.content }}</p>
        <el-divider></el-divider>
      </div>
      <div v-if="!form.comment.length" class="no-comment">暂无评论</div>
    </el-card>


    <!-- 我的评论输入 -->
    <el-divider>我要评论</el-divider>
    <el-card shadow="never" class="comment-input-card">
      <el-form>
        <el-form-item label="评论：" label-width="80px">
          <el-input
              v-model="form.myComment"
              type="textarea"
              :rows="4"
              placeholder="请输入评论内容..."
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="sendComment" style="margin: 0 auto">发送评论</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 返回按钮 -->
    <div class="button-section">
      <el-button @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<style scoped>
/* 容器 */
.container {
  max-width: 800px;
  margin: 20px auto;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

/* 帖子卡片 */
.post-card {
  margin-bottom: 20px;
}
.post-header {
  display: flex;
  flex-direction: column;
  gap: 5px;
}
.post-header .date {
  font-size: 12px;
  color: #999;
}
.post-content {
  line-height: 1.6;
  font-size: 14px;
}

/* 图片展示 */
.photo-gallery {
  margin-top: 10px;
}
.photo {
  width: 100%;
  height: 120px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 评论卡片 */
.comment-card {
  margin-bottom: 20px;
}
.comment-item {
  margin-bottom: 10px;
}
.comment-header {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #666;
}
.comment-header .username {
  font-weight: bold;
  color: #333;
}
.comment-content {
  margin-top: 5px;
  font-size: 14px;
  color: #444;
}
.no-comment {
  text-align: center;
  color: #999;
  font-size: 14px;
}

/* 评论输入卡片 */
.comment-input-card {
  margin-bottom: 20px;
}
.comment-input-card .el-form-item {
  margin-bottom: 10px;
}

/* 返回按钮 */
.button-section {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
