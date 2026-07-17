<template>
  <div class="login-page">
    <!-- 背景装饰 -->
    <div class="bg-shapes">
      <div class="shape shape-1">🍽️</div>
      <div class="shape shape-2">🥗</div>
      <div class="shape shape-3">🍜</div>
      <div class="shape shape-4">🥟</div>
      <div class="shape shape-5">🍛</div>
      <div class="shape shape-6">🧋</div>
    </div>

    <div class="login-card">
      <!-- 顶部 -->
      <div class="card-top">
        <div class="logo-circle">🍽️</div>
        <h1>智慧校园餐饮</h1>
        <p class="subtitle">智慧点餐 · 便捷校园生活</p>
      </div>

      <!-- 表单 -->
      <el-form @submit.prevent="handleLogin" class="login-form">
        <div class="input-group">
          <label>账号</label>
          <el-input v-model="form.username" placeholder="请输入账号" size="large" class="login-input">
            <template #prefix><el-icon><User /></el-icon></template>
          </el-input>
        </div>
        <div class="input-group">
          <label>密码</label>
          <el-input v-model="form.password" type="password" placeholder="请输入密码" size="large" class="login-input" show-password>
            <template #prefix><el-icon><Lock /></el-icon></template>
          </el-input>
        </div>
        <el-button type="primary" size="large" class="login-btn" @click="handleLogin" :loading="loading">
          {{ loading ? '登录中...' : '登 录' }}
        </el-button>
      </el-form>

      <!-- 测试账号 -->
      <div class="test-section">
        <p class="test-title">📝 体验账号</p>
        <div class="test-grid">
          <div class="test-item" @click="fill('student1','123456')">
            <span class="ti-avatar" style="background:linear-gradient(135deg,#FF6B35,#FFB347)">学</span>
            <div><span class="ti-name">学生账号</span><span class="ti-info">student1 / 123456</span></div>
          </div>
          <div class="test-item" @click="fill('merchant1','123456')">
            <span class="ti-avatar" style="background:linear-gradient(135deg,#4CAF50,#8BC34A)">商</span>
            <div><span class="ti-name">商户账号</span><span class="ti-info">merchant1 / 123456</span></div>
          </div>
          <div class="test-item" @click="fill('admin','123456')">
            <span class="ti-avatar" style="background:linear-gradient(135deg,#2196F3,#03A9F4)">管</span>
            <div><span class="ti-name">管理员账号</span><span class="ti-info">admin / 123456</span></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const form = ref({ username: '', password: '' })

const accounts: Record<string, { role: string; nickname: string }> = {
  student1: { role: 'STUDENT', nickname: '张同学' },
  merchant1: { role: 'MERCHANT', nickname: '老王餐饮' },
  admin: { role: 'ADMIN', nickname: '管理员' }
}
const roleHome: Record<string, string> = {
  STUDENT: '/home', MERCHANT: '/merchant-dashboard', ADMIN: '/admin-dashboard'
}

function fill(u: string, p: string) { form.value.username = u; form.value.password = p }

function handleLogin() {
  const u = form.value.username.trim(), p = form.value.password.trim()
  if (!u || !p) { ElMessage.warning('请输入账号和密码'); return }
  const acct = accounts[u]
  if (!acct || p !== '123456') { ElMessage.error('账号或密码错误'); return }
  loading.value = true
  setTimeout(() => {
    loading.value = false
    localStorage.setItem('token', 't-'+Date.now())
    localStorage.setItem('role', acct.role)
    localStorage.setItem('nickname', acct.nickname)
    ElMessage.success('欢迎回来，' + acct.nickname + '！')
    router.push(roleHome[acct.role])
  }, 600)
}

onMounted(() => localStorage.clear())
</script>

<style scoped>
.login-page {
  min-height:100vh;display:flex;align-items:center;justify-content:center;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8ecf1 100%);
  position:relative;overflow:hidden;padding:20px;
}

/* 浮动餐具背景 - 浅色模式 */
.bg-shapes { position:absolute;inset:0;pointer-events:none;overflow:hidden; }
.shape { position:absolute;font-size:40px;opacity:0.08;animation:float 8s ease-in-out infinite; }
.shape-1 { top:10%;left:10%;font-size:60px;animation-delay:0s; }
.shape-2 { top:20%;right:15%;font-size:50px;animation-delay:1s; }
.shape-3 { bottom:25%;left:8%;font-size:45px;animation-delay:2s; }
.shape-4 { bottom:15%;right:10%;font-size:55px;animation-delay:3s; }
.shape-5 { top:40%;left:5%;font-size:35px;animation-delay:4s; }
.shape-6 { top:50%;right:5%;font-size:40px;animation-delay:5s; }
@keyframes float { 0%,100%{transform:translateY(0) rotate(0deg)} 50%{transform:translateY(-20px) rotate(10deg)} }

/* 登录卡片 */
.login-card {
  background:rgba(255,255,255,0.95);backdrop-filter:blur(20px);
  border-radius:24px;padding:36px 32px;width:100%;max-width:400px;
  box-shadow:0 25px 80px rgba(0,0,0,0.2);position:relative;z-index:1;
}
.card-top { text-align:center;margin-bottom:28px; }
.logo-circle {
  width:72px;height:72px;border-radius:50%;margin:0 auto 14px;
  background:linear-gradient(135deg,#FF6B35,#FFB347);
  display:flex;align-items:center;justify-content:center;
  font-size:36px;box-shadow:0 8px 24px rgba(255,107,53,0.3);
}
.card-top h1 { font-size:24px;font-weight:700;color:#2D3436;margin-bottom:4px; }
.subtitle { font-size:14px;color:#999; }

.input-group { margin-bottom:16px; }
.input-group label { display:block;font-size:14px;font-weight:500;margin-bottom:6px;color:#444; }
.login-input :deep(.el-input__wrapper) { border-radius:12px;padding:4px 12px;box-shadow:0 0 0 1px #e8e8e8 inset; }
.login-input :deep(.el-input__wrapper):hover { box-shadow:0 0 0 1px #667eea inset; }

.login-btn {
  width:100%;padding:14px;border-radius:12px;font-size:16px;font-weight:600;
  background:linear-gradient(135deg,#FF6B35,#FFB347);border:none;margin-top:8px;
  letter-spacing:2px;
}
.login-btn:hover { opacity:0.9; }

/* 测试账号 */
.test-section { margin-top:24px;padding-top:20px;border-top:1px solid #f0f0f0; }
.test-title { font-size:13px;color:#999;text-align:center;margin-bottom:12px; }
.test-grid { display:flex;flex-direction:column;gap:8px; }
.test-item { display:flex;align-items:center;gap:10px;padding:10px 12px;border-radius:12px;background:#FAFAFA;cursor:pointer;transition:all 0.2s; }
.test-item:hover { background:#F0EEFF;transform:translateX(4px); }
.ti-avatar { width:36px;height:36px;border-radius:50%;display:flex;align-items:center;justify-content:center;color:white;font-size:14px;font-weight:600;flex-shrink:0; }
.ti-name { display:block;font-size:13px;font-weight:500; }
.ti-info { display:block;font-size:11px;color:#999;margin-top:2px; }
</style>
