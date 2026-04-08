import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/bill',
      redirect: '/main/bill'
    },
    {
      path: '/main',
      name: 'main',
      component: () => import('../views/MainView.vue'),
      meta: {
        requiresAuth: true
      },
      children: [
        {
          path: 'bill',
          name: 'bill',
          component: () => import('../views/BillView.vue')
        },
        {
          path: 'stats',
          name: 'stats',
          component: () => import('../views/StatsView.vue')
        },
        {
          path: '',
          name: 'default',
          redirect: 'bill'
        }
      ]
    }
  ],
})

// 路由守卫
router.beforeEach((to, from) => {
  const userStore = useUserStore()
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  
  if (requiresAuth && !userStore.isLogin()) {
    // 需要登录但未登录，跳转到登录页
    return '/'
  }
  // 不需要登录或已登录，正常跳转
  return true
})

export default router
