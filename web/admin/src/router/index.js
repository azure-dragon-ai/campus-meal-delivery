import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'system/admin',
        name: 'SystemAdmin',
        component: () => import('@/views/system/Admin.vue'),
        meta: { title: '管理员管理' }
      },
      {
        path: 'system/role',
        name: 'SystemRole',
        component: () => import('@/views/system/Role.vue'),
        meta: { title: '角色管理' }
      },
      {
        path: 'system/menu',
        name: 'SystemMenu',
        component: () => import('@/views/system/Menu.vue'),
        meta: { title: '菜单管理' }
      },
      {
        path: 'school/list',
        name: 'SchoolList',
        component: () => import('@/views/school/SchoolList.vue'),
        meta: { title: '学校管理' }
      },
      {
        path: 'school/class',
        name: 'SchoolClass',
        component: () => import('@/views/school/ClassList.vue'),
        meta: { title: '班级管理' }
      },
      {
        path: 'person/teacher',
        name: 'PersonTeacher',
        component: () => import('@/views/person/Teacher.vue'),
        meta: { title: '教师管理' }
      },
      {
        path: 'person/parent',
        name: 'PersonParent',
        component: () => import('@/views/person/Parent.vue'),
        meta: { title: '家长管理' }
      },
      {
        path: 'person/diner',
        name: 'PersonDiner',
        component: () => import('@/views/person/Diner.vue'),
        meta: { title: '就餐人管理' }
      },
      {
        path: 'leave/list',
        name: 'LeaveList',
        component: () => import('@/views/leave/LeaveList.vue'),
        meta: { title: '请假管理' }
      },
      {
        path: 'semester/list',
        name: 'SemesterList',
        component: () => import('@/views/semester/SemesterList.vue'),
        meta: { title: '学期管理' }
      },
      {
        path: 'meal-reg/list',
        name: 'MealRegList',
        component: () => import('@/views/meal-reg/MealRegList.vue'),
        meta: { title: '配餐登记管理' }
      },
      {
        path: 'recipe/list',
        name: 'RecipeList',
        component: () => import('@/views/recipe/RecipeList.vue'),
        meta: { title: '食谱管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (to.path !== '/login' && !userStore.token) {
    next('/login')
  } else if (to.path === '/login' && userStore.token) {
    next('/')
  } else {
    next()
  }
})

export default router
