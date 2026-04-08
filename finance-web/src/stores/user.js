import { defineStore } from 'pinia'
import router from '../router'

export const useUserStore = defineStore('user', {
  state: () => ({
    // token
    token: localStorage.getItem('token') || '',
    // userInfo
    userInfo: JSON.parse(localStorage.getItem('userInfo') || 'null')
  }),
  actions: {
    // 设置token
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },
    // 设置userInfo
    setUserInfo(userInfo) {
      this.userInfo = userInfo
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    },
    // 退出登录
    logout() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      router.push('/login')
    },
    // 检查是否登录
    isLogin() {
      return !!this.token
    }
  }
})
