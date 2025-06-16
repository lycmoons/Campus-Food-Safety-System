<script setup>

</script>

<template>
  <div class="container">
    <div class="left"></div>
    <div class="center">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component"/>
        </transition>
      </router-view>
    </div>
    <div class="right"></div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  height: 100vh;
}

.left, .center, .right {
  padding: 10px;
}

.left {
  flex: 2;
}

.center {
  flex: 1;
}

.right {
  flex: 2;
}
</style>