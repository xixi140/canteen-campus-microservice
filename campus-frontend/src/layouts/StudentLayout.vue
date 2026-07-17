<template>
  <div class="s-layout" v-if="role === 'STUDENT'">
    <header class="s-hdr">
      <router-link to="/home" class="s-logo">🍽️ 校园美食</router-link>
      <nav class="s-nav">
        <router-link to="/home" class="s-nl" exact-active-class="active">首页</router-link>
        <router-link to="/canteens" class="s-nl" active-class="active">食堂</router-link>
        <router-link to="/merchant-shops" class="s-nl" active-class="active">商户</router-link>
        <router-link to="/orders" class="s-nl" active-class="active">订单</router-link>
      </nav>
      <div class="s-acts">
        <router-link to="/notices" class="s-ic">🔔</router-link>
        <router-link to="/cart" class="s-ic">🛒</router-link>
        <el-dropdown trigger="click" @command="go">
          <span class="s-ut">
            <el-avatar :size="30" style="background:#FF6B35">{{ nick[0] }}</el-avatar>
            <span class="s-un">{{ nick }}</span>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="/profile">个人中心</el-dropdown-item>
              <el-dropdown-item command="/orders">我的订单</el-dropdown-item>
              <el-dropdown-item command="/notices">消息中心</el-dropdown-item>
              <el-dropdown-item command="/cart">购物车</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>
    <main class="s-main"><slot /></main>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()
const nick = computed(() => localStorage.getItem('nickname') || '用户')
const role = computed(() => localStorage.getItem('role'))
function go(cmd: string) {
  if (cmd === 'logout') { localStorage.clear(); router.push('/login') }
  else router.push(cmd)
}
</script>

<style scoped>
.s-layout { }
.s-hdr { display:flex;align-items:center;position:fixed;top:0;left:0;right:0;height:56px;background:white;border-bottom:1px solid #eee;padding:0 20px;z-index:1000;gap:24px; }
.s-logo { font-size:18px;font-weight:700;color:#FF6B35;text-decoration:none; }
.s-nav { flex:1;display:flex;gap:4px; }
.s-nl { padding:6px 16px;border-radius:8px;text-decoration:none;font-size:14px;color:#666; }
.s-nl:hover,.s-nl.active { background:#FFF0E8;color:#FF6B35;font-weight:600; }
.s-acts { display:flex;align-items:center;gap:14px; }
.s-ic { font-size:20px;cursor:pointer;text-decoration:none; }
.s-ut { display:flex;align-items:center;gap:6px;cursor:pointer; }
.s-un { font-size:13px;color:#666;max-width:60px;overflow:hidden;text-overflow:ellipsis; }
.s-main { max-width:1200px;margin:0 auto;padding:72px 16px 20px;min-height:100vh; }
</style>
