<template>
  <div class="profile-page">
    <div class="profile-header">
      <div class="ph-avatar-wrap">
        <el-avatar :size="72" class="ph-avatar" style="background:linear-gradient(135deg,var(--primary),var(--secondary))">
          {{ nickname[0] }}
        </el-avatar>
      </div>
      <h2>{{ nickname }}</h2>
      <p class="ph-tag">学生用户</p>
    </div>
    <div class="profile-stats">
      <div class="ps-item"><span class="ps-num">{{ orders.length }}</span><span>全部订单</span></div>
      <div class="ps-item"><span class="ps-num">0</span><span>待评价</span></div>
      <div class="ps-item"><span class="ps-num">★</span><span>积分</span></div>
    </div>
    <div class="profile-menu">
      <div class="pm-item" @click="$router.push('/orders')"><el-icon><Document /></el-icon><span>我的订单</span><el-icon><ArrowRight /></el-icon></div>
      <div class="pm-item" @click="$router.push('/notices')"><el-icon><Bell /></el-icon><span>消息中心</span><el-icon><ArrowRight /></el-icon></div>
      <div class="pm-item"><el-icon><Location /></el-icon><span>收货地址</span><el-icon><ArrowRight /></el-icon></div>
      <div class="pm-item"><el-icon><Setting /></el-icon><span>账户设置</span><el-icon><ArrowRight /></el-icon></div>
    </div>
    <div class="profile-logout">
      <el-button type="danger" plain round @click="handleLogout">退出登录</el-button>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const nickname = computed(() => localStorage.getItem('nickname') || '用户')
const role = computed(() => localStorage.getItem('role') || 'STUDENT')
const orders: any[] = []
function handleLogout() { localStorage.clear(); router.push('/login') }
</script>
<style scoped>
.profile-page { padding-bottom: 30px; }
.profile-header { text-align: center; padding: 30px 16px 20px; background: linear-gradient(135deg, #FF6B3520, #FFB34720); }
.ph-avatar-wrap { margin-bottom: 12px; }
.profile-header h2 { font-size: 20px; font-weight: 700; }
.ph-tag { font-size: 13px; color: var(--text-light); margin-top: 4px; }
.profile-stats { display: flex; gap: 0; background: white; margin: 0; border-radius: 0; }
.ps-item { flex: 1; text-align: center; padding: 16px 0; border-right: 1px solid #f5f5f5; }
.ps-item:last-child { border-right: none; }
.ps-num { font-size: 22px; font-weight: 700; color: var(--primary); display: block; }
.ps-item span:last-child { font-size: 12px; color: var(--text-light); margin-top: 4px; display: block; }
.profile-menu { background: white; margin-top: 10px; }
.pm-item { display: flex; align-items: center; gap: 12px; padding: 16px; border-bottom: 1px solid #f5f5f5; cursor: pointer; font-size: 14px; }
.pm-item span { flex: 1; }
.pm-item .el-icon:last-child { color: var(--text-lighter); }
.profile-logout { text-align: center; padding: 24px; }
</style>
