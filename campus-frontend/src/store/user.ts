import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginAPI } from '@/api'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const username = ref(localStorage.getItem('username') || '')
  const nickname = ref(localStorage.getItem('nickname') || '')
  const role = ref(localStorage.getItem('role') || '')
  const userId = ref(Number(localStorage.getItem('userId') || 0))

  const isLoggedIn = computed(() => !!token.value)
  const isStudent = computed(() => role.value === 'STUDENT')
  const isMerchant = computed(() => role.value === 'MERCHANT')
  const isAdmin = computed(() => role.value === 'ADMIN')

  async function doLogin(data: { username: string; password: string; role: string }) {
    try {
      const res = await loginAPI(data) as any
      const r = res.data || {}
      token.value = r.token || 'token-' + Date.now()
      username.value = data.username
      nickname.value = r.nickname || (data.role === 'STUDENT' ? '张同学' : data.role === 'MERCHANT' ? '老王餐饮' : '管理员')
      role.value = data.role
      userId.value = r.userId || 2

      localStorage.setItem('token', token.value)
      localStorage.setItem('username', username.value)
      localStorage.setItem('nickname', nickname.value)
      localStorage.setItem('role', role.value)
      localStorage.setItem('userId', String(userId.value))
    } catch {
      // fallback mock login
      const mock: Record<string, any> = {
        STUDENT: { id: 2, nickname: '张同学' },
        MERCHANT: { id: 3, nickname: '老王餐饮' },
        ADMIN: { id: 1, nickname: '管理员' }
      }
      const u = mock[data.role]
      if (!u || data.password !== '123456') throw new Error('账号或密码错误')
      token.value = 'token-' + Date.now()
      username.value = data.username
      nickname.value = u.nickname
      role.value = data.role
      userId.value = u.id
      Object.entries({ token: token.value, username: username.value, nickname: nickname.value, role: role.value, userId: String(userId.value) })
        .forEach(([k, v]) => localStorage.setItem(k, v))
    }
    const routes: Record<string, string> = { STUDENT: '/home', MERCHANT: '/merchant/dashboard', ADMIN: '/admin/dashboard' }
    router.push(routes[data.role])
  }

  function logout() {
    token.value = ''; username.value = ''; nickname.value = ''; role.value = ''; userId.value = 0
    localStorage.clear()
    router.push('/login')
  }

  return { token, username, nickname, role, userId, isLoggedIn, isStudent, isMerchant, isAdmin, doLogin, logout }
})
