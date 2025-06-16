<script setup>

import {ref} from "vue";
import router from "@/router/index.js";

let activeMenu = ref('/index/complaint')

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
        <el-menu-item index="/index/complaint">我的投诉</el-menu-item>
        <el-menu-item index="/index/complaint/new-complaint">新建投诉</el-menu-item>
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
}

.navigation{
  flex: 1;

}

.content{
  flex: 7;
}
</style>