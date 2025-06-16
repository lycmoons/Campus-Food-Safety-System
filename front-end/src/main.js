import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router/index.js"
import 'element-plus/dist/index.css'
import ElementPlus from 'element-plus'
import axios from "axios"
import zhCn from 'element-plus/es/locale/lang/zh-cn';


axios.defaults.baseURL = 'http://81.68.71.52:8080' // 本地测试：'http://localhost:8080'
const app = createApp(App)
app.use(router)
app.use(ElementPlus)
app.use(ElementPlus, {locale: zhCn})
app.mount('#app')
