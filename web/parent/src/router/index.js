import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  { path: '/login', name: 'Login', component: () => import('@/views/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('@/views/Register.vue') },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/Dashboard.vue'), meta: { title: '首页' } },
      { path: 'diners', name: 'Diners', component: () => import('@/views/Diners.vue'), meta: { title: '就餐人管理' } },
      { path: 'leaves', name: 'Leaves', component: () => import('@/views/Leaves.vue'), meta: { title: '请假记录' } },
      { path: 'meal-reg', name: 'MealReg', component: () => import('@/views/MealReg.vue'), meta: { title: '配餐登记' } }
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  if (to.path !== '/login' && to.path !== '/register' && !userStore.token) {
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register') && userStore.token) {
    next('/')
  } else {
    next()
  }
})

export default router
