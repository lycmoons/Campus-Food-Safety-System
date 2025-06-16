<script setup>
import {reactive, ref} from "vue";
import { User, Message, Lock, Clock } from "@element-plus/icons-vue"
import router from "@/router/index.js";
import {postWithToken} from "@/net/index.js"
import {ElMessage} from "element-plus";
import axios from "axios";
import {validateCode, validateEmail, validatePassword, validateUsername} from "@/validate/index.js";

const formRef = ref()
const form = reactive({
  username: '',
  password: '',
  password_repeat: '',
  email: '',
  code: ''
})

const validatePassword_repeat = (rule, value, callback) => {
  if (value === ''){
    callback(new Error('请再次输入密码进行确认'))
  }
  else if (value !== form.password){
    callback(new Error('两次输入的密码不一致'))
  }
  else {
    callback()
  }
}


const rule = {
  username: [
    { validator: validateUsername, trigger: ['blur', 'change'] },
  ],
  password: [
    { validator: validatePassword, trigger: ['blur', 'change'] },
  ],
  password_repeat: [
    { validator: validatePassword_repeat, trigger: ['blur', 'change'] },
  ],
  email: [
    { validator: validateEmail, trigger: ['blur', 'change'] },
  ],
  code: [
    { validator: validateCode, trigger: ['blur', 'change'] },
  ]
}

function askForCode() {
  if(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(form.email)){
    axios.post('/api/email/ask-code',{
      email: form.email,
      type: 'register'
    }).then(({data}) => {
      if (data.code === 200){
        ElMessage.success(`验证码已发送至邮箱地址：${form.email}`)
      }
      else {
        console.warn(`请求地址：/api/email/ask-code，状态码：${data.code}，错误信息：${data.message}`)
        ElMessage.warning(data.message)
      }
    }).catch(err => {
      console.warn(err)
      ElMessage.warning('发生了一些错误，请联系管理员')
    })
  }else {
    ElMessage.warning('请输入正确的电子邮件')
  }
}

function register() {
  formRef.value.validate((valid) => {
    if(valid){
      axios.post('/api/account/register', {
        username: form.username,
        password: form.password,
        email: form.email,
        code: form.code
      }).then(({data}) => {
        if (data.code === 200){
          ElMessage.success('注册成功，请尝试登录')
          router.push('/')
        }
        else {
          console.warn(`请求地址：/api/account/register，状态码：${data.code}，错误信息：${data.message}`)
          ElMessage.warning(data.message)
        }
      }).catch(err => {
        console.warn(err)
        ElMessage.warning('发生了一些错误，请联系管理员')
      })
    }
    else{
      ElMessage.warning('请完善表单内容')
    }
  })
}

</script>

<template>
  <div style="text-align: center;margin: 0 20px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px;font-weight: bold">注册</div>
    </div>
    <div style="margin-top: 50px">
      <el-form :model="form" :rules="rule" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" maxlength="10" type="text" placeholder="用户名">
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

        <el-form-item prop="password_repeat">
          <el-input v-model="form.password_repeat" maxlength="20" type="password" placeholder="再次输入确认密码">
            <template #prefix>
              <el-icon><Lock/></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="email">
          <el-input v-model="form.email" type="text" placeholder="电子邮件地址">
            <template #prefix>
              <el-icon><Message/></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="code">
          <el-row gutter="10" style="width: 100%">
            <el-col :span="17">
              <el-input v-model="form.code" maxlength="6" type="text" placeholder="请输入验证码">
                <template #prefix>
                  <el-icon><Clock/></el-icon>
                </template>
              </el-input>
            </el-col>

            <el-col :span="5">
              <el-button @click="askForCode" type="success" plain>获取验证码</el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <div style="margin-top: 50px">
      <el-button  @click="register" style="width: 250px" type="warning" plain>立即注册</el-button>
    </div>
    <div style="margin-top: 20px">
      <span style="font-size: 14px;line-height: 15px;color: grey">已有账号？</span>
      <el-link style="translate: 0 -1px" @click="router.push('/')">立即登录</el-link>
    </div>
  </div>
</template>

<style scoped>

</style>