<template>
  <div class="chat-room">
    <div class="messages" ref="messagesContainer">
      <div
          v-for="(message, index) in messages"
          :key="index"
          :class="['message', message.type]"
      >
        <div class="message-header">
          <span class="avatar" :class="message.type">
            {{ message.type === 'user' ? '👤' : '🤖' }}
          </span>
          <span class="sender-name">
            {{ message.type === 'user' ? '你' : 'AI助手' }}
          </span>
        </div>
        <div class="content" v-html="renderMarkdown(message.content)"></div>
        <div class="timestamp">{{ formatTime(message.timestamp) }}</div>
      </div>
    </div>

    <div class="input-area">
      <input
          v-model="userInput"
          @keyup.enter="sendMessage"
          placeholder="输入你的问题..."
      />
      <button @click="sendMessage">发送</button>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, onUpdated, nextTick} from 'vue';
import {postWithToken} from "@/net/index.js";
import {marked} from 'marked'; // 引入marked库
import DOMPurify from 'dompurify'; // 引入DOMPurify用于安全过滤

// 响应式数据
const userInput = ref('');
const messages = ref([]);
const messagesContainer = ref(null);

// 配置marked
marked.setOptions({
  breaks: true, // 自动换行
  gfm: true, // 支持GitHub风格的Markdown
});

// 安全渲染Markdown
const renderMarkdown = (content) => {
  return DOMPurify.sanitize(marked.parse(content || ''));
};

// 格式化时间显示
const formatTime = (timestamp) => {
  const date = new Date(timestamp);
  return date.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
};

// 从本地存储加载聊天记录
const loadMessages = () => {
  const savedMessages = sessionStorage.getItem('aiChatMessages');
  if (savedMessages) {
    messages.value = JSON.parse(savedMessages);
  }

  // 如果没有消息，添加欢迎消息
  if (messages.value.length === 0) {
    messages.value.push({
      type: 'ai',
      content: '你好！我是AI助手，请问有什么可以帮你的吗？',
      timestamp: Date.now()
    });
  }
};

// 保存聊天记录到本地存储
const saveMessages = () => {
  sessionStorage.setItem('aiChatMessages', JSON.stringify(messages.value));
};

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
    }
  });
};

// 发送消息
const sendMessage = () => {
  if (!userInput.value.trim()) return;

  // 添加用户消息
  messages.value.push({
    type: 'user',
    content: userInput.value,
    timestamp: Date.now()
  });
  userInput.value = '';

  // 添加AI思考中的提示
  messages.value.push({
    type: 'ai',
    content: '思考中...',
    timestamp: Date.now()
  });

  // 获取AI回复
  function getAnswer(question, callback) {
    postWithToken('/api/deepseek/ask', {
      question: question
    }, (data) => {
      callback(data.answer)
    })
  }

  // 调用getAnswer函数
  getAnswer(messages.value[messages.value.length - 2].content, (answer) => {
    // 移除"思考中"消息
    messages.value.pop();

    // 添加AI回复
    messages.value.push({
      type: 'ai',
      content: answer,
      timestamp: Date.now()
    });

    // 保存到本地存储
    saveMessages();
  });
};

// 生命周期钩子
onMounted(() => {
  loadMessages();
  scrollToBottom();
});

onUpdated(() => {
  scrollToBottom();
});
</script>

<style scoped>
.chat-room {
  display: flex;
  flex-direction: column;
  height: 85vh;
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  margin-bottom: 20px;
  border: 1px solid #eee;
  border-radius: 8px;
}

.message {
  margin-bottom: 15px;
  padding: 12px 15px;
  border-radius: 12px;
  max-width: 80%;
  word-wrap: break-word;
  position: relative;
}

.message-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.avatar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  margin-right: 8px;
  font-size: 14px;
}

.avatar.user {
  background-color: #e3f2fd;
}

.avatar.ai {
  background-color: #f3e5f5;
}

.sender-name {
  font-weight: bold;
  font-size: 0.85rem;
  color: #555;
}

.message.user {
  margin-left: auto;
  background-color: #dcf8c6;
  border-bottom-right-radius: 5px;
}

.message.ai {
  margin-right: auto;
  background-color: #f1f0f0;
  border-bottom-left-radius: 5px;
}

/* Markdown内容样式 */
.content {
  font-size: 0.95rem;
  line-height: 1.6;
}

.content :deep() h1,
.content :deep() h2,
.content :deep() h3 {
  margin: 0.5em 0;
  line-height: 1.2;
}

.content :deep() p {
  margin: 0.5em 0;
}

.content :deep() ul,
.content :deep() ol {
  padding-left: 1.5em;
  margin: 0.5em 0;
}

.content :deep() li {
  margin: 0.25em 0;
}

.content :deep() code {
  background-color: rgba(175, 184, 193, 0.2);
  border-radius: 4px;
  padding: 0.2em 0.4em;
  font-family: monospace;
}

.content :deep() pre {
  background-color: #f6f8fa;
  border-radius: 6px;
  padding: 12px;
  overflow: auto;
}

.content :deep() pre code {
  background-color: transparent;
  padding: 0;
}

.content :deep() blockquote {
  border-left: 4px solid #dfe2e5;
  color: #6a737d;
  padding-left: 12px;
  margin: 0.5em 0;
}

.content :deep() a {
  color: #0969da;
  text-decoration: none;
}

.content :deep() a:hover {
  text-decoration: underline;
}

.content :deep() strong {
  font-weight: bold;
}

.content :deep() em {
  font-style: italic;
}

.timestamp {
  font-size: 0.75rem;
  color: #666;
  text-align: right;
  margin-top: 4px;
}

.input-area {
  display: flex;
  gap: 10px;
}

.input-area input {
  flex: 1;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 20px;
  outline: none;
}

.input-area button {
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
}

.input-area button:hover {
  background-color: #45a049;
}
</style>