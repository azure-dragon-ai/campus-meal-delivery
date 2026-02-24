<template>
  <div class="layout-container">
    <el-container>
      <el-aside :width="isCollapse ? '64px' : '200px'" class="sidebar">
        <div class="logo">
          <span v-if="!isCollapse">校园配餐</span>
          <span v-else>餐</span>
        </div>
        <el-menu
          :default-active="activeMenu"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          :collapse="isCollapse"
          :collapse-transition="false"
          router
        >
          <el-menu-item index="/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-sub-menu index="system">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/system/admin">管理员管理</el-menu-item>
            <el-menu-item index="/system/role">角色管理</el-menu-item>
            <el-menu-item index="/system/menu">菜单管理</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="school">
            <template #title>
              <el-icon><School /></el-icon>
              <span>学校管理</span>
            </template>
            <el-menu-item index="/school/list">学校列表</el-menu-item>
            <el-menu-item index="/school/class">班级管理</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="person">
            <template #title>
              <el-icon><User /></el-icon>
              <span>人员管理</span>
            </template>
            <el-menu-item index="/person/teacher">教师管理</el-menu-item>
            <el-menu-item index="/person/parent">家长管理</el-menu-item>
            <el-menu-item index="/person/diner">就餐人管理</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="leave">
            <template #title>
              <el-icon><Calendar /></el-icon>
              <span>请假管理</span>
            </template>
            <el-menu-item index="/leave/list">请假列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="semester">
            <template #title>
              <el-icon><Calendar /></el-icon>
              <span>学期管理</span>
            </template>
            <el-menu-item index="/semester/list">学期列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="meal-reg">
            <template #title>
              <el-icon><Food /></el-icon>
              <span>配餐登记</span>
            </template>
            <el-menu-item index="/meal-reg/list">配餐登记列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="recipe">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>食谱管理</span>
            </template>
            <el-menu-item index="/recipe/list">食谱列表</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div class="header-left">
            <el-icon class="collapse-icon" @click="toggleCollapse">
              <Fold v-if="!isCollapse" />
              <Expand v-else />
            </el-icon>
          </div>
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <el-avatar :size="30" :icon="UserFilled" />
                <span class="username">{{ userStore.userInfo.name || userStore.userInfo.username }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)
const activeMenu = computed(() => route.path)

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确认退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      userStore.logout()
      router.push('/login')
    })
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.el-container {
  height: 100%;
}

.sidebar {
  background-color: #304156;
  transition: width 0.3s;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #2b3a4b;
}

.el-menu {
  border-right: none;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
}

.collapse-icon {
  font-size: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.collapse-icon:hover {
  color: #409EFF;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin-left: 10px;
}

.main-content {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
