<template>
  <div class="a-layout" v-if="role === 'ADMIN'">
    <aside class="a-sb">
      <div class="a-hd"><span class="a-logo">🔧</span><span class="a-tit">管理后台</span></div>
      <nav class="a-nav">
        <router-link to="/admin-dashboard" class="a-item" active-class="active">📊 数据看板</router-link>
        <router-link to="/admin-canteens" class="a-item" active-class="active">🏫 食堂管理</router-link>
        <router-link to="/admin-merchants" class="a-item" active-class="active">🏪 商户管理</router-link>
        <router-link to="/admin-order" class="a-item" active-class="active">📋 订单管理</router-link>
      </nav>
      <div class="a-ft" @click="logout">🚪 退出登录</div>
    </aside>
    <div class="a-body">
      <header class="a-bar">{{ title }}</header>
      <main class="a-page"><slot /></main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()
const role = computed(() => localStorage.getItem('role'))
defineProps<{ title: string }>()
function logout() { localStorage.clear(); router.push('/login') }
</script>
<style scoped>
.a-layout { display:flex;min-height:100vh; }
.a-sb { width:220px;position:fixed;left:0;top:0;bottom:0;background:#1a237e;display:flex;flex-direction:column;z-index:100; }
.a-hd { padding:20px 16px;border-bottom:1px solid rgba(255,255,255,0.1);color:white;display:flex;align-items:center;gap:10px; }
.a-logo { font-size:24px; }
.a-tit { font-size:16px;font-weight:600; }
.a-nav { flex:1;padding:12px 8px;display:flex;flex-direction:column;gap:2px; }
.a-item { padding:12px 14px;border-radius:10px;color:rgba(255,255,255,0.65);text-decoration:none;font-size:14px; }
.a-item:hover { background:rgba(255,255,255,0.1);color:white; }
.a-item.active { background:rgba(255,255,255,0.15);color:white;font-weight:600; }
.a-ft { padding:16px;border-top:1px solid rgba(255,255,255,0.1);cursor:pointer;color:rgba(255,255,255,0.65);text-align:center;font-size:13px; }
.a-ft:hover { color:white; }
.a-body { margin-left:220px;flex:1; }
.a-bar { padding:16px 24px;background:white;border-bottom:1px solid #eee;font-size:18px;font-weight:600; }
.a-page { padding:24px;background:#f5f5f5;min-height:calc(100vh - 57px); }
</style>
