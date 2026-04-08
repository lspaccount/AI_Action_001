<template>
  <el-container class="main-container">
    <!-- 顶部导航栏 -->
    <el-header class="header">
      <div class="logo">
        <span class="logo-text">个人记账本</span>
      </div>
      <div class="user-info">
        <span class="username">{{ userStore.userInfo?.username || '用户' }}</span>
        <el-button link @click="handleLogout">退出登录</el-button>
      </div>
    </el-header>
    
    <el-container>
      <!-- 左侧菜单 -->
      <el-aside width="200px" class="aside">
        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical-demo"
          @select="handleMenuSelect"
        >
          <el-menu-item index="/main/bill">
            <el-icon><Money /></el-icon>
            <span>记账</span>
          </el-menu-item>
          <el-menu-item index="/main/stats">
            <el-icon><DataAnalysis /></el-icon>
            <span>统计</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <!-- 主内容区 -->
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { Money, DataAnalysis } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 计算当前激活的菜单
const activeMenu = computed(() => {
  return route.fullPath
})

// 处理菜单选择
const handleMenuSelect = (key) => {
  router.push(key)
}

// 处理退出登录
const handleLogout = () => {
  userStore.logout()
}

// 页面挂载时检查登录状态
onMounted(() => {
  if (!userStore.isLogin()) {
    router.push('/')
  }
})
</script>

<style scoped>
.main-container {
  height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background-color: #409EFF;
  color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.logo {
  font-size: 20px;
  font-weight: bold;
}

.logo-text {
  margin-left: 10px;
}

.user-info {
  display: flex;
  align-items: center;
}

.username {
  margin-right: 20px;
}

.aside {
  background-color: #f5f7fa;
  border-right: 1px solid #e4e7ed;
}

.el-menu-vertical-demo {
  height: 100%;
  border-right: none;
}

.main {
  padding: 20px;
  background-color: #f5f7fa;
  overflow-y: auto;
}
</style>
