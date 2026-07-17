<template>
  <div class="canteen-list-page">
    <div class="detail-topbar">
      <button class="back-btn" @click="$router.back()"><el-icon :size="20"><ArrowLeft /></el-icon></button>
      <h3>🏫 全部食堂</h3>
      <span></span>
    </div>
    <div class="list-intro">
      <p>查看食堂今日菜品信息，到窗口排队购买</p>
    </div>
    <div class="canteen-grid">
      <div v-for="c in canteens" :key="c.id" class="canteen-card-lg" @click="$router.push(`/canteen/${c.id}`)">
        <div class="cc-cover" :style="{ background: c.color }"><span class="cc-icon">{{ c.icon }}</span></div>
        <div class="cc-body">
          <h3>{{ c.name }}</h3>
          <p class="cc-location">📍 {{ c.location }}</p>
          <div class="cc-meta">
            <span>{{ c.stallCount }}个档口</span><span>·</span><span>{{ c.dishCount }}+道菜品</span>
          </div>
          <p class="cc-time">⏰ {{ c.openTime }}</p>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getCanteens } from '@/api'

const canteens = ref<any[]>([])

onMounted(async () => {
  try {
    const res = await getCanteens() as any
    if (res.data && res.data.length) {
      canteens.value = res.data.map((c: any) => ({
        ...c,
        icon: '🏛️',
        color: c.id === 1 ? '#FFF0E8' : '#E8F4FD',
        stallCount: c.stallCount || 0,
        dishCount: c.dishCount || 0,
        openTime: c.openTime || '06:30-13:00 / 16:30-19:30'
      }))
    }
  } catch {}
  if (!canteens.value.length) {
    canteens.value = [
      { id:1, name:'一食堂', icon:'🏛️', color:'#FFF0E8', location:'校园东区', stallCount:8, dishCount:32, openTime:'06:30-13:00 / 16:30-19:30' },
      { id:2, name:'二食堂', icon:'🏛️', color:'#E8F4FD', location:'校园西区', stallCount:6, dishCount:28, openTime:'06:30-13:00 / 16:30-19:30' }
    ]
  }
})
</script>
<style scoped>
.canteen-list-page { padding-bottom: 30px; }
.detail-topbar { display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); position: sticky; top: 0; z-index: 10; }
.back-btn { width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border: none; border-radius: 50%; background: #f5f5f5; cursor: pointer; }
.detail-topbar h3 { font-size: 17px; font-weight: 600; }
.list-intro { padding: 16px; font-size: 14px; color: #666; }
.canteen-grid { padding: 0 16px; display: flex; flex-direction: column; gap: 14px; }
.canteen-card-lg { display: flex; gap: 16px; padding: 16px; background: white; border-radius: 16px; cursor: pointer; transition: all 0.3s; box-shadow: 0 1px 4px rgba(0,0,0,0.04); }
.canteen-card-lg:hover { transform: translateY(-3px); box-shadow: 0 6px 20px rgba(0,0,0,0.1); }
.cc-cover { width: 80px; height: 80px; border-radius: 14px; display: flex; align-items: center; justify-content: center; font-size: 36px; flex-shrink: 0; }
.cc-body h3 { font-size: 18px; font-weight: 700; margin-bottom: 4px; }
.cc-location { font-size: 12px; color: #666; margin-bottom: 4px; }
.cc-meta { font-size: 12px; color: #999; margin-bottom: 4px; display: flex; gap: 4px; }
.cc-time { font-size: 12px; color: #999; }
</style>
