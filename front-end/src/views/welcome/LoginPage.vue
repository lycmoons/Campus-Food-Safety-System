<script setup>
  import {User, Lock} from '@element-plus/icons-vue'
  import {reactive, ref} from "vue";
  import router from "@/router/index.js";
  import axios from "axios";
  import {saveToken} from "@/net/index.js";
  import {ElMessage} from "element-plus";

  const form = reactive({
    username: '',
    password: '',
    remember: false
  })

  const formRef = ref()

  const rule = {
    username: [
      {required: true, message: '请输入用户名/邮箱', trigger: ['blur', 'change']}
    ],
    password: [
      {required: true, message: '请输入密码', trigger: ['blur', 'change']}
    ]
  }

  function loginSuccess(data){
    // 存储 token
    saveToken(data.token, data.expire, data.role, form.remember)

    // 进入主界面
    if (data.role === 'user') {
      ElMessage.success(`登录成功，欢迎 ${data.username}`)
      router.push('/index')
    }
    else if (data.role === 'admin') {
      ElMessage.success('管理员登录成功')
      router.push('/admin')
    }
  }

  function userLogin(){
    formRef.value.validate((valid) => {
      if(valid){
        axios.post('/api/auth/login',{
          username: form.username,
          password: form.password
        },{
          headers: { "Content-Type": "application/x-www-form-urlencoded" }
        }).then(({data}) => {
          if(data.code === 200){
            loginSuccess(data.data)
          }else{
            console.warn(`请求地址：/api/auth/login，状态码：${data.code}，错误信息：${data.message}`)
            ElMessage.warning(data.message)
          }
        }).catch(err => {
          console.warn(err)
          ElMessage.warning('发生了一些错误，请联系管理员')
        })
      }
    })
  }
</script>

<template>
  <div style="text-align: center;margin: 0 20px">
   <div style="margin-top: 150px">
     <div style="font-size: 25px;font-weight: bold">登录</div>
   </div>
   <div style="margin-top: 50px">
     <el-form :model="form" :rules="rule" ref="formRef">

       <el-form-item prop="username">
         <el-input v-model="form.username" type="text" placeholder="用户名/邮箱">
           <template #prefix>
             <el-icon><User/></el-icon>
           </template>
         </el-input>
       </el-form-item>

       <el-form-item prop="password">
         <el-input v-model="form.password" maxlength="20" type="password" placeholder="密码">
           <template #prefix>
             <el-icon><Lock/></el-icon>
           </template>
         </el-input>
       </el-form-item>

       <el-row>
         <el-col :span="12" style="text-align: left">
           <el-form-item prop="remember">
             <el-checkbox v-model="form.remember" label="记住密码"/>
           </el-form-item>
         </el-col>

         <el-col :span="12" style="text-align: right">
             <el-link>忘记密码？</el-link>
         </el-col>
       </el-row>
     </el-form>
   </div>
   <div style="margin-top: 40px">
     <el-button @click="userLogin" style="width: 270px" type="success" plain>登录</el-button>
   </div>
   <el-divider>
     <span style="font-size: 13px;color: grey">没有账号</span>
   </el-divider>
   <div>
     <el-button style="width: 270px" type="warning" plain>注册</el-button>
   </div>
 </div>
</template>

<style scoped>

</style>