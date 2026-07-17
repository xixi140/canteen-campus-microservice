<template>
  <div class="shop-list-page">
    <div class="detail-topbar">
      <button class="back-btn" @click="$router.back()"><el-icon :size="20"><ArrowLeft /></el-icon></button>
      <h3>🏪 商户美食</h3>
      <span></span>
    </div>

    <div class="list-intro">
      <p>在线下单，到店取餐</p>
    </div>

    <div class="category-tabs">
      <button v-for="cat in categories" :key="cat"
              :class="['cat-tab', { active: activeCat === cat }]"
              @click="activeCat = cat">{{ cat }}</button>
    </div>

    <div class="shop-grid">
      <div v-for="shop in filteredShops" :key="shop.id" class="shop-card" @click="$router.push(`/shop/${shop.id}`)">
        <div class="shop-banner" :style="{ background: shop.color }">
          <span class="shop-big-icon">{{ shop.icon }}</span>
        </div>
        <div class="shop-body">
          <div class="shop-header-row">
            <h3>{{ shop.name }}</h3>
            <el-tag size="small" type="warning" effect="plain">在线点餐</el-tag>
          </div>
          <div class="shop-rate">
            <el-rate v-model="shop.rating" disabled show-score score-template="{value}" size="small" />
            <span>月售 {{ shop.sales }}</span>
          </div>
          <div class="shop-dishes-tags">
            <span v-for="d in shop.hotDishes" :key="d" class="hot-dish-tag">{{ d }}</span>
          </div>
          <button class="visit-btn">进店点餐 →</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const activeCat = ref('全部')
const categories = ['全部', '炒饭', '麻辣烫', '面食', '小吃']

const shops = ref([
  { id: 1, name: '老王炒饭', icon: '🍚', color: '#FFF0E8', rating: 4.8, sales: 1286, hotDishes: ['蛋炒饭', '扬州炒饭', '牛肉炒饭'], category: '炒饭' },
  { id: 2, name: '李记麻辣烫', icon: '🌶️', color: '#FFE8E8', rating: 4.5, sales: 956, hotDishes: ['麻辣烫', '冒菜', '麻辣拌'], category: '麻辣烫' },
  { id: 3, name: '好滋味面馆', icon: '🍜', color: '#E8F8E8', rating: 4.6, sales: 823, hotDishes: ['牛肉拉面', '炸酱面', '葱油拌面'], category: '面食' }
])

const filteredShops = computed(() => {
  if (activeCat.value === '全部') return shops.value
  return shops.value.filter(s => s.category === activeCat.value)
})
</script>

<style scoped>
.shop-list-page { padding-bottom: 30px; }
.detail-topbar { display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); position: sticky; top: 0; z-index: 10; }
.back-btn { width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border: none; border-radius: 50%; background: #f5f5f5; cursor: pointer; }
.detail-topbar h3 { font-size: 17px; font-weight: 600; }
.list-intro { padding: 8px 16px 4px; font-size: 13px; color: var(--text-light); }
.category-tabs { display: flex; gap: 8px; padding: 12px 16px; overflow-x: auto; }
.cat-tab { white-space: nowrap; padding: 6px 16px; border: none; border-radius: 20px; background: white; font-size: 13px; cursor: pointer; transition: all 0.3s; box-shadow: 0 1px 4px rgba(0,0,0,0.04); }
.cat-tab.active { background: var(--primary); color: white; }
.shop-grid { padding: 0 16px; display: flex; flex-direction: column; gap: 14px; }
.shop-card { background: white; border-radius: 16px; overflow: hidden; cursor: pointer; transition: all 0.3s; box-shadow: 0 1px 4px rgba(0,0,0,0.04); }
.shop-card:hover { transform: translateY(-3px); box-shadow: var(--shadow-hover); }
.shop-banner { height: 70px; display: flex; align-items: center; justify-content: center; }
.shop-big-icon { font-size: 36px; }
.shop-body { padding: 12px 16px 16px; }
.shop-header-row { display: flex; align-items: center; justify-content: space-between; margin-bottom: 6px; }
.shop-header-row h3 { font-size: 17px; font-weight: 700; }
.shop-rate { display: flex; align-items: center; gap: 8px; margin-bottom: 8px; }
.shop-rate span { font-size: 12px; color: var(--text-light); }
.shop-dishes-tags { display: flex; gap: 6px; margin-bottom: 12px; }
.hot-dish-tag { font-size: 12px; padding: 3px 10px; border-radius: 6px; background: var(--primary-light); color: var(--primary); }
.visit-btn { width: 100%; padding: 10px; border: none; border-radius: 10px; background: linear-gradient(135deg, var(--primary), var(--secondary)); color: white; font-size: 14px; font-weight: 600; cursor: pointer; }
</style>
