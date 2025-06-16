<script setup>
import {ref} from "vue";
import router from "@/router/index.js";

let activeMenu = ref('/index')

function handleSelect(index){
  router.push(index)
  activeMenu = index
}
</script>



<template>
  <div class="container">
    <div class="navigation">
      <el-menu :default-active="activeMenu"
               @select="handleSelect"
               mode="horizontal"
               style="display: flex; justify-content: space-evenly"
               router>
        <el-menu-item index="/index">首页</el-menu-item>
        <el-menu-item index="/index/post">留言板</el-menu-item>
        <el-menu-item index="/index/trace">食品溯源</el-menu-item>
        <el-menu-item index="/index/complaint">投诉</el-menu-item>
        <el-menu-item index="/index/account">我的</el-menu-item>
        <el-menu-item index="/index/assistant">智能助手</el-menu-item>
      </el-menu>
    </div>

    <div class="content">
      <router-view style="margin-top: 50px"></router-view>
    </div>
  </div>
</template>



<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.navigation {
  flex: 1;
  width: 100%;
}

.content {
  flex: 14;
  width: 100%;
}
</style>