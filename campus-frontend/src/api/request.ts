import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const API_BASE = 'http://localhost:8080/api'

const request = axios.create({ baseURL: API_BASE, timeout: 15000 })

request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

request.interceptors.response.use(
  res => res.data,
  error => {
    if (error.response?.status === 401) { localStorage.clear(); router.push('/login') }
    else if (error.response?.status === 429) ElMessage.warning('请求太频繁，请稍后再试')
    return Promise.reject(error)
  }
)

export default request
