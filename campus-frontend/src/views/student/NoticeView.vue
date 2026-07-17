<template>
  <div class="notice-page">
    <div class="detail-topbar">
      <button class="back-btn" @click="$router.back()"><el-icon :size="20"><ArrowLeft /></el-icon></button>
      <h3>🔔 消息中心</h3>
      <button class="clear-btn" @click="notices = []" v-if="notices.length">全部已读</button>
    </div>
    <div class="notice-list">
      <div v-for="n in notices" :key="n.id" class="notice-card" :class="{ unread: !n.isRead }" @click="n.isRead = true">
        <div class="notice-icon">{{ n.type === 'ORDER' ? '📦' : '📢' }}</div>
        <div class="notice-body">
          <div class="notice-top"><h4>{{ n.title }}</h4><span class="notice-time">{{ n.time }}</span></div>
          <p>{{ n.content }}</p>
        </div>
        <div v-if="!n.isRead" class="unread-dot"></div>
      </div>
      <el-empty v-if="notices.length === 0" description="暂无消息" />
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue'

const notices = ref([
  { id: 1, type: 'ORDER', title: '订单取餐提醒', content: '您的蛋炒饭已备好，取餐号 A34，请到老王炒饭取餐', time: '12:05', isRead: false },
  { id: 2, type: 'ORDER', title: '订单配送提醒', content: '您的牛肉炒饭正在备餐中，请稍候', time: '11:35', isRead: false },
  { id: 3, type: 'SYSTEM', title: '系统公告', content: '一食堂本周六起推出夏日清凉套餐，欢迎品尝', time: '昨天', isRead: true }
])
</script>
<style scoped>
.notice-page { padding-bottom: 30px; }
.detail-topbar { display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); position: sticky; top: 0; z-index: 10; }
.back-btn { width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border: none; border-radius: 50%; background: #f5f5f5; cursor: pointer; }
.detail-topbar h3 { font-size: 17px; font-weight: 600; }
.clear-btn { font-size: 13px; color: var(--text-light); background: none; border: none; cursor: pointer; }
.notice-list { padding: 8px 16px; display: flex; flex-direction: column; gap: 8px; }
.notice-card { display: flex; gap: 12px; padding: 14px; background: white; border-radius: 14px; cursor: pointer; box-shadow: 0 1px 4px rgba(0,0,0,0.04); align-items: flex-start; position: relative; }
.notice-card.unread { background: #FFF8F5; }
.notice-icon { font-size: 28px; }
.notice-body { flex: 1; }
.notice-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
.notice-top h4 { font-size: 14px; font-weight: 600; }
.notice-time { font-size: 11px; color: var(--text-lighter); }
.notice-body p { font-size: 13px; color: var(--text-light); line-height: 1.4; }
.unread-dot { width: 8px; height: 8px; border-radius: 50%; background: var(--accent); flex-shrink: 0; margin-top: 6px; }
</style>
