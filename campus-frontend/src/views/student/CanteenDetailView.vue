<template>
  <div class="canteen-detail">
    <div class="detail-topbar">
      <button class="back-btn" @click="$router.back()"><el-icon :size="20"><ArrowLeft /></el-icon></button>
      <h3>{{ canteen?.name || '食堂详情' }}</h3>
      <span></span>
    </div>

    <div class="canteen-hero">
      <div class="hero-top">
        <span class="hero-icon">🏛️</span>
        <div>
          <h2>{{ canteen?.name }}</h2>
          <p class="hero-location">📍 {{ canteen?.location }} | {{ canteen?.openTime }}</p>
        </div>
      </div>
      <div class="notice-banner">
        <el-icon><InfoFilled /></el-icon>
        <span>食堂菜品需到窗口排队购买，本平台仅提供查看功能</span>
      </div>
    </div>

    <!-- 餐次切换 — 控制下方菜品 -->
    <div class="meal-tabs">
      <button v-for="m in meals" :key="m.key"
              :class="['meal-tab', { active: activeMeal === m.key }]"
              @click="switchMeal(m.key)">
        {{ m.icon }} {{ m.name }}
      </button>
    </div>

    <!-- 当前餐次提示 -->
    <div class="meal-hint">
      <span>{{ currentMealHint }}</span>
      <span class="count-badge">{{ totalDishCount }}道菜品</span>
    </div>

    <!-- 档口列表 -->
    <div class="stall-list">
      <div v-for="stall in stalls" :key="stall.id" class="stall-section">
        <div class="stall-header" @click="toggleStall(stall.id)">
          <div class="stall-left">
            <span class="stall-icon">{{ stall.icon }}</span>
            <div>
              <h4>{{ stall.name }}</h4>
              <span class="stall-category">{{ stall.category }}</span>
            </div>
          </div>
          <div class="stall-right">
            <span class="stall-count">{{ getStallDishes(stall.id).length }}道</span>
            <el-icon :class="{ rotated: openStalls.has(stall.id) }"><ArrowDown /></el-icon>
          </div>
        </div>
        <transition name="slide">
          <div v-if="openStalls.has(stall.id)" class="stall-dishes">
            <div v-for="d in getStallDishes(stall.id)" :key="d.id" class="stall-dish">
              <span class="dish-emoji">{{ d.emoji }}</span>
              <div class="dish-info">
                <span class="dish-name">{{ d.name }}</span>
                <span class="dish-desc">{{ d.desc }}</span>
              </div>
              <div class="dish-price-only">
                <span class="price-num">¥{{ d.price }}</span>
              </div>
            </div>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const openStalls = ref(new Set<number>())
const activeMeal = ref('LUNCH')

const meals = [
  { key: 'BREAKFAST', name: '早餐', icon: '🌅', hint: '🌅 早餐时段：06:30-09:00，热腾腾的包子豆浆等着你' },
  { key: 'LUNCH', name: '午餐', icon: '☀️', hint: '☀️ 午餐时段：11:00-13:30，丰富多样的午餐选择' },
  { key: 'DINNER', name: '晚餐', icon: '🌙', hint: '🌙 晚餐时段：17:00-19:30，晚餐轻食健康美味' }
]

const canteen = computed(() => ({
  id: route.params.id,
  name: route.params.id === '2' ? '二食堂' : '一食堂',
  location: route.params.id === '2' ? '校园西区' : '校园东区',
  openTime: '06:30-13:00 / 16:30-19:30'
}))

const currentMealHint = computed(() => meals.find(m => m.key === activeMeal.value)?.hint || '')

// ===== 所有菜品数据（按餐次、档头组织）=====
const mealDishMap = {
  BREAKFAST: {
    stallDishes: {
      3: [
        { id: 601, name: '煎饼果子', emoji: '🥞', price: 6.00, desc: '现做现卖，香脆可口' },
        { id: 602, name: '豆浆油条', emoji: '🥛', price: 4.00, desc: '现磨豆浆配手工油条' },
        { id: 603, name: '小笼包', emoji: '🥟', price: 8.00, desc: '鲜肉小笼，汤汁鲜美' },
        { id: 604, name: '茶叶蛋', emoji: '🥚', price: 1.50, desc: '卤香入味' }
      ],
      4: [
        { id: 605, name: '皮蛋瘦肉粥', emoji: '🥣', price: 5.00, desc: '文火慢熬，暖心暖胃' },
        { id: 606, name: '八宝粥', emoji: '🥣', price: 4.00, desc: '营养丰富' }
      ]
    }
  },
  LUNCH: {
    stallDishes: {
      1: [
        { id: 101, name: '红烧肉', emoji: '🥩', price: 8.00, desc: '肥而不腻，入口即化' },
        { id: 102, name: '番茄炒蛋', emoji: '🍳', price: 4.00, desc: '经典下饭菜' },
        { id: 103, name: '青椒肉丝', emoji: '🫑', price: 6.00, desc: '嫩滑肉丝配青椒' },
        { id: 104, name: '麻婆豆腐', emoji: '🥘', price: 5.00, desc: '麻辣下饭' }
      ],
      2: [
        { id: 201, name: '牛肉面', emoji: '🍜', price: 12.00, desc: '手工拉面配红烧牛肉' },
        { id: 202, name: '炸酱面', emoji: '🍝', price: 8.00, desc: '老北京炸酱面' },
        { id: 203, name: '打卤面', emoji: '🍜', price: 9.00, desc: '传统打卤面' }
      ],
      3: [
        { id: 301, name: '酸辣粉', emoji: '🍜', price: 7.00, desc: '酸辣开胃' }
      ],
      4: [
        { id: 401, name: '宫保鸡丁', emoji: '🍗', price: 10.00, desc: '正宗川味，麻辣鲜香' },
        { id: 402, name: '鱼香肉丝', emoji: '🥗', price: 8.00, desc: '经典川菜，酸甜开胃' }
      ],
      5: [
        { id: 501, name: '过桥米线', emoji: '🍜', price: 15.00, desc: '云南过桥米线，汤鲜味美' },
        { id: 502, name: '小锅米线', emoji: '🍜', price: 10.00, desc: '昆明小锅米线' }
      ]
    }
  },
  DINNER: {
    stallDishes: {
      1: [
        { id: 701, name: '蒜蓉空心菜', emoji: '🥬', price: 5.00, desc: '清淡健康' },
        { id: 702, name: '清蒸鲈鱼', emoji: '🐟', price: 12.00, desc: '鲜嫩可口' }
      ],
      2: [
        { id: 703, name: '三鲜面', emoji: '🍜', price: 10.00, desc: '鲜味十足' }
      ],
      4: [
        { id: 704, name: '回锅肉', emoji: '🥩', price: 9.00, desc: '经典川菜' },
        { id: 705, name: '水煮肉片', emoji: '🥘', price: 11.00, desc: '麻辣鲜香' }
      ],
      5: [
        { id: 706, name: '砂锅米线', emoji: '🍜', price: 12.00, desc: '热腾腾的砂锅米线' }
      ]
    }
  }
}

// 档口（固定不变）
const stalls = [
  { id: 1, name: '1号窗口·自选快餐', icon: '🍱', category: '炒菜 ｜ 套餐' },
  { id: 2, name: '2号窗口·面食坊', icon: '🍜', category: '面食 ｜ 米线' },
  { id: 3, name: '3号窗口·早餐小食', icon: '🥟', category: '早点 ｜ 小吃' },
  { id: 4, name: '4号窗口·精品小炒', icon: '🥘', category: '川菜 ｜ 湘菜' },
  { id: 5, name: '5号窗口·云南味道', icon: '🍜', category: '米线 ｜ 饵丝' }
]

function getStallDishes(stallId: number) {
  const mealDishes = (mealDishMap as any)[activeMeal.value]?.stallDishes
  return mealDishes?.[stallId] || []
}

const totalDishCount = computed(() => {
  const mealDishes = (mealDishMap as any)[activeMeal.value]?.stallDishes
  if (!mealDishes) return 0
  return Object.values(mealDishes).reduce((sum: number, arr: any) => sum + arr.length, 0)
})

function switchMeal(key: string) {
  activeMeal.value = key
  // 切换餐次时自动展开第一个有菜品的档口
  openStalls.value.clear()
  for (const stall of stalls) {
    if (getStallDishes(stall.id).length > 0) {
      openStalls.value.add(stall.id)
      break
    }
  }
}

function toggleStall(id: number) {
  if (openStalls.value.has(id)) openStalls.value.delete(id)
  else openStalls.value.add(id)
}

onMounted(() => {
  switchMeal('LUNCH')
})
</script>

<style scoped>
.canteen-detail { padding-bottom: 30px; }
.detail-topbar { display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); position: sticky; top: 0; z-index: 10; }
.back-btn { width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border: none; border-radius: 50%; background: #f5f5f5; cursor: pointer; }
.detail-topbar h3 { font-size: 17px; font-weight: 600; }
.canteen-hero { background: linear-gradient(135deg, #FFF0E8, #FFE8CC); padding: 20px; }
.hero-top { display: flex; gap: 12px; margin-bottom: 12px; }
.hero-icon { font-size: 40px; }
.canteen-hero h2 { font-size: 20px; font-weight: 700; }
.hero-location { font-size: 12px; color: var(--text-light); margin-top: 4px; }
.notice-banner { display: flex; align-items: center; gap: 8px; padding: 10px 14px; background: #FFF3E0; border-radius: 10px; font-size: 13px; color: #E65100; }
.meal-tabs { display: flex; gap: 8px; padding: 12px 16px; }
.meal-tab { flex: 1; padding: 10px; border: 2px solid #eee; border-radius: 10px; background: white; cursor: pointer; font-size: 14px; font-weight: 500; transition: all 0.3s; }
.meal-tab.active { border-color: var(--primary); background: var(--primary-light); color: var(--primary); }
.meal-hint { display: flex; justify-content: space-between; align-items: center; padding: 0 16px 12px; font-size: 13px; color: var(--text-light); }
.count-badge { background: #f5f5f5; padding: 2px 10px; border-radius: 8px; font-size: 11px; }
.stall-list { padding: 0 16px; display: flex; flex-direction: column; gap: 10px; }
.stall-section { background: white; border-radius: 14px; overflow: hidden; box-shadow: 0 1px 4px rgba(0,0,0,0.04); }
.stall-header { display: flex; justify-content: space-between; align-items: center; padding: 14px 16px; cursor: pointer; }
.stall-header:hover { background: #FAFAFA; }
.stall-left { display: flex; align-items: center; gap: 12px; }
.stall-icon { font-size: 28px; }
.stall-left h4 { font-size: 15px; font-weight: 600; }
.stall-category { font-size: 12px; color: var(--text-lighter); }
.stall-right { display: flex; align-items: center; gap: 8px; }
.stall-count { font-size: 12px; color: var(--text-light); }
.rotated { transform: rotate(180deg); }
.stall-dishes { border-top: 1px solid #f5f5f5; }
.stall-dish { display: flex; align-items: center; gap: 12px; padding: 12px 16px; }
.dish-emoji { font-size: 28px; }
.dish-info { flex: 1; }
.dish-name { font-size: 14px; font-weight: 500; display: block; }
.dish-desc { font-size: 11px; color: var(--text-lighter); margin-top: 2px; }
.dish-price-only { text-align: right; }
.price-num { font-size: 16px; font-weight: 700; color: var(--text); }
.slide-enter-active, .slide-leave-active { transition: all 0.3s; }
.slide-enter-from, .slide-leave-to { opacity: 0; max-height: 0; }
</style>
