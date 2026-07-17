<template>
  <div class="m-layout" v-if="role === 'MERCHANT'">
    <aside class="m-sb">
      <div class="m-hd"><span class="m-logo">🏪</span><span class="m-tit">{{ nick }}</span></div>
      <nav class="m-nav">
        <router-link to="/merchant-dashboard" class="m-item" active-class="active">📊 营业概况</router-link>
        <router-link to="/merchant-dishes" class="m-item" active-class="active">🍽️ 菜品管理</router-link>
        <router-link to="/merchant-order" class="m-item" active-class="active">📋 订单管理</router-link>
        <router-link to="/merchant-settings" class="m-item" active-class="active">⚙️ 店铺设置</router-link>
      </nav>
      <div class="m-ft" @click="logout">🚪 退出登录</div>
    </aside>
    <div class="m-body">
      <header class="m-bar">{{ title }}</header>
      <main class="m-page"><slot /></main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()
const nick = computed(() => localStorage.getItem('nickname') || '商户')
const role = computed(() => localStorage.getItem('role'))
const props = defineProps<{ title: string }>()
function logout() { localStorage.clear(); router.push('/login') }
</script>
<style scoped>
.m-layout { display:flex;min-height:100vh; }
.m-sb { width:220px;position:fixed;left:0;top:0;bottom:0;background:#2D3436;display:flex;flex-direction:column;z-index:100; }
.m-hd { padding:20px 16px;border-bottom:1px solid rgba(255,255,255,0.1);color:white;display:flex;align-items:center;gap:10px; }
.m-logo { font-size:24px; }
.m-tit { font-size:16px;font-weight:600; }
.m-nav { flex:1;padding:12px 8px;display:flex;flex-direction:column;gap:2px; }
.m-item { padding:12px 14px;border-radius:10px;color:rgba(255,255,255,0.65);text-decoration:none;font-size:14px; }
.m-item:hover { background:rgba(255,255,255,0.1);color:white; }
.m-item.active { background:rgba(255,255,255,0.15);color:white;font-weight:600; }
.m-ft { padding:16px;border-top:1px solid rgba(255,255,255,0.1);cursor:pointer;color:rgba(255,255,255,0.65);text-align:center;font-size:13px; }
.m-ft:hover { color:white; }
.m-body { margin-left:220px;flex:1; }
.m-bar { padding:16px 24px;background:white;border-bottom:1px solid #eee;font-size:18px;font-weight:600; }
.m-page { padding:24px;background:#f5f5f5;min-height:calc(100vh - 57px); }
</style>
