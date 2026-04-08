import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

// 创建axios实例
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 从localStorage读取token
    const token = localStorage.getItem('token')
    // 自动带上Token
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    // code=200 直接返回 data
    if (res.code === 200) {
      return res.data
    } else {
      // code=401 跳转登录页并清除 Token
      if (res.code === 401) {
        ElMessage.error('未授权，请重新登录')
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        router.push('/login')
      } else {
        // 其他 code 弹出错误提示
        ElMessage.error(res.message || '请求失败')
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  error => {
    console.error('响应错误:', error)
    ElMessage.error('网络错误，请稍后重试')
    return Promise.reject(error)
  }
)

export default request
