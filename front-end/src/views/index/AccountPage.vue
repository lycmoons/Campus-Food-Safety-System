<script setup>
import router from "@/router/index.js";
import {ref} from "vue";

let activeMenu = ref('/index/account')
function handleSelect(index){
  router.push(index)
  activeMenu = index
}
</script>


<template>
  <div class="container">
    <div class="navigation">
      <el-menu :default-active="activeMenu"
               mode="vertical"
               @select="handleSelect"
               router>
        <el-menu-item index="/index/account">我的账号</el-menu-item>
        <el-menu-item index="/index/account/my-message">我的消息</el-menu-item>
      </el-menu>
    </div>

    <div class="content">
      <router-view></router-view>
    </div>
  </div>
</template>

<style scoped>
.container{
  display: flex;
  flex-direction: row;
  height: 100vh;
  gap: 20px;
}

.navigation{
  flex: 1;

}

.content{
  flex: 7;
}
</style>