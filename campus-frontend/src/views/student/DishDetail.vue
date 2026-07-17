<template>
  <div class="dish-detail">
    <div class="detail-topbar">
      <button class="back-btn" @click="$router.back()"><el-icon :size="20"><ArrowLeft /></el-icon></button>
      <h3>菜品详情</h3>
      <span></span>
    </div>

    <div v-if="dish">
      <div class="dish-hero" :style="{ background: dish.color }">
        <span class="dish-big-emoji">{{ dish.emoji }}</span>
        <span class="dish-source-tag">
          {{ dish.sourceType === 'CANTEEN' ? '🏫 食堂菜品' : '🏪 商户菜品' }}
        </span>
      </div>

      <div class="dish-info-card">
        <div class="dish-header">
          <div>
            <h2>{{ dish.name }}</h2>
            <p class="dish-desc-text">{{ dish.desc }}</p>
          </div>
          <span class="dish-price-big">¥{{ dish.price }}</span>
        </div>
      </div>

      <!-- 食堂菜品 → 提示 -->
      <div v-if="dish.sourceType === 'CANTEEN'" class="canteen-notice-card">
        <el-icon :size="20"><InfoFilled /></el-icon>
        <div>
          <strong>本菜品为食堂官方供应</strong>
          <p class="notice-text">请前往食堂对应窗口排队购买，本平台仅提供信息查看服务</p>
        </div>
      </div>

      <!-- 商户菜品 → 可下单 -->
      <div v-if="dish.sourceType === 'MERCHANT'" class="merchant-order-section">
        <div class="quantity-row">
          <span class="label">数量</span>
          <div class="quantity-control">
            <button class="qty-btn" @click="decrease" :disabled="quantity <= 1">−</button>
            <span class="qty-value">{{ quantity }}</span>
            <button class="qty-btn" @click="increase" :disabled="quantity >= dish.stock">+</button>
          </div>
        </div>
        <div class="pickup-row">
          <span class="label">取餐人</span>
          <el-input v-model="pickupName" placeholder="输入昵称" size="large" style="max-width:200px" />
        </div>
      </div>
    </div>

    <!-- 底部下单栏（仅商户菜品） -->
    <div v-if="dish?.sourceType === 'MERCHANT'" class="order-bar">
      <div class="order-total">
        <span class="total-label">合计</span>
        <span class="total-price">¥{{ (dish.price * quantity).toFixed(2) }}</span>
      </div>
      <button class="order-btn" @click="handleOrder" :disabled="ordering">
        <span v-if="ordering">下单中...</span>
        <span v-else>下单 · 取餐号 A{{ currentQueue + 1 }}</span>
      </button>
    </div>

    <!-- 食堂菜品 → 返回按钮 -->
    <div v-if="dish?.sourceType === 'CANTEEN'" style="padding: 20px; text-align: center;">
      <el-button type="primary" round @click="$router.back()">← 返回查看其他菜品</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const sourceType = route.params.sourceType as string
const dishId = Number(route.params.dishId)

const dish = ref<any>(null)
const quantity = ref(1)
const ordering = ref(false)
const pickupName = ref('')
const currentQueue = ref(32)

function decrease() { if (quantity.value > 1) quantity.value-- }
function increase() { if (dish.value && quantity.value < dish.value.stock) quantity.value++ }

const handleOrder = async () => {
  if (!pickupName.value.trim()) {
    ElMessage.warning('请输入取餐人称呼')
    return
  }
  ordering.value = true
  setTimeout(() => {
    currentQueue.value++
    ordering.value = false
    ElMessage.success(`🎉 下单成功！取餐号 A${currentQueue.value}，请到店取餐`)
    router.push('/orders')
  }, 1000)
}

onMounted(() => {
  const allDishes: Record<string, any> = {
    // 🏫 食堂菜品
    '101': { id: 101, name: '红烧肉', emoji: '🥩', price: 8.00, stock: 50, sourceType: 'CANTEEN', desc: '肥而不腻，入口即化。精选五花肉慢火炖煮2小时，色泽红亮。', color: '#FFEBEE' },
    '102': { id: 102, name: '番茄炒蛋', emoji: '🍳', price: 4.00, stock: 80, sourceType: 'CANTEEN', desc: '经典下饭菜，酸甜可口。', color: '#FFF8E1' },
    '103': { id: 103, name: '青椒肉丝', emoji: '🫑', price: 6.00, stock: 60, sourceType: 'CANTEEN', desc: '嫩滑肉丝配青椒，家常美味。', color: '#E8F5E9' },
    '104': { id: 104, name: '麻婆豆腐', emoji: '🥘', price: 5.00, stock: 50, sourceType: 'CANTEEN', desc: '麻辣下饭，正宗川味。', color: '#FFEBEE' },
    '201': { id: 201, name: '牛肉面', emoji: '🍜', price: 12.00, stock: 30, sourceType: 'CANTEEN', desc: '手工拉面配红烧牛肉，汤鲜味美。', color: '#FFF3E0' },
    '202': { id: 202, name: '炸酱面', emoji: '🍝', price: 8.00, stock: 40, sourceType: 'CANTEEN', desc: '老北京炸酱面，酱香浓郁。', color: '#FFF8E1' },
    '401': { id: 401, name: '宫保鸡丁', emoji: '🍗', price: 10.00, stock: 40, sourceType: 'CANTEEN', desc: '正宗川味，麻辣鲜香。', color: '#FFEBEE' },
    '402': { id: 402, name: '鱼香肉丝', emoji: '🥗', price: 8.00, stock: 50, sourceType: 'CANTEEN', desc: '经典川菜，酸甜开胃。', color: '#FFF8E1' },
    '501': { id: 501, name: '过桥米线', emoji: '🍜', price: 15.00, stock: 25, sourceType: 'CANTEEN', desc: '云南过桥米线，汤鲜味美。', color: '#FFF3E0' },
    '502': { id: 502, name: '小锅米线', emoji: '🍜', price: 10.00, stock: 35, sourceType: 'CANTEEN', desc: '昆明小锅米线。', color: '#FFF8E1' },
    '601': { id: 601, name: '煎饼果子', emoji: '🥞', price: 6.00, stock: 20, sourceType: 'CANTEEN', desc: '现做现卖，香脆可口。', color: '#FFF8E1' },
    '602': { id: 602, name: '豆浆油条', emoji: '🥛', price: 4.00, stock: 100, sourceType: 'CANTEEN', desc: '现磨豆浆配手工油条。', color: '#FFF3E0' },
    '603': { id: 603, name: '小笼包', emoji: '🥟', price: 8.00, stock: 15, sourceType: 'CANTEEN', desc: '鲜肉小笼，汤汁鲜美。', color: '#FFEBEE' },
    '704': { id: 704, name: '回锅肉', emoji: '🥩', price: 9.00, stock: 30, sourceType: 'CANTEEN', desc: '经典川菜，肥而不腻。', color: '#FFEBEE' },
    '706': { id: 706, name: '砂锅米线', emoji: '🍜', price: 12.00, stock: 20, sourceType: 'CANTEEN', desc: '热腾腾的砂锅米线。', color: '#FFF3E0' },
    // 🏪 商户菜品
    '11': { id: 11, name: '蛋炒饭', emoji: '🍚', price: 10.00, stock: 30, sourceType: 'MERCHANT', desc: '金黄蛋炒饭，粒粒分明。', color: '#FFF8E1' },
    '12': { id: 12, name: '扬州炒饭', emoji: '🍚', price: 15.00, stock: 25, sourceType: 'MERCHANT', desc: '正宗扬州炒饭，火腿虾仁。', color: '#FFF8E1' },
    '13': { id: 13, name: '牛肉炒饭', emoji: '🥩', price: 18.00, stock: 20, sourceType: 'MERCHANT', desc: '嫩牛肉粒炒饭，黑椒风味。', color: '#FFF0E8' },
    '14': { id: 14, name: '老干妈炒饭', emoji: '🌶️', price: 12.00, stock: 35, sourceType: 'MERCHANT', desc: '香辣过瘾，经典口味。', color: '#FFF8E1' },
    '15': { id: 15, name: '咖喱鸡肉饭', emoji: '🍛', price: 16.00, stock: 22, sourceType: 'MERCHANT', desc: '日式咖喱，浓郁可口。', color: '#FFF8E1' },
    '16': { id: 16, name: '可乐', emoji: '🥤', price: 3.00, stock: 100, sourceType: 'MERCHANT', desc: '冰镇可口可乐。', color: '#E8F5E9' }
  }
  dish.value = allDishes[String(dishId)] || { id: dishId, name: '菜品详情', emoji: '🍽️', price: 0, stock: 0, sourceType, desc: '暂无信息', color: '#f5f5f5' }
})
</script>

<style scoped>
.dish-detail { padding-bottom: 80px; }
.detail-topbar {
  display: flex; align-items: center; justify-content: space-between;
  padding: 12px 16px; background: rgba(255,255,255,0.95);
  backdrop-filter: blur(10px); position: sticky; top: 0; z-index: 10;
}
.back-btn { width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border: none; border-radius: 50%; background: #f5f5f5; cursor: pointer; }
.detail-topbar h3 { font-size: 17px; font-weight: 600; }
.dish-hero { height: 200px; display: flex; align-items: center; justify-content: center; position: relative; }
.dish-big-emoji { font-size: 72px; }
.dish-source-tag { position: absolute; top: 16px; left: 16px; padding: 6px 14px; border-radius: 20px; background: rgba(255,255,255,0.9); font-size: 13px; }
.dish-info-card { background: white; border-radius: 20px 20px 0 0; margin-top: -20px; padding: 20px; }
.dish-header { display: flex; justify-content: space-between; }
.dish-header h2 { font-size: 22px; font-weight: 700; }
.dish-desc-text { font-size: 13px; color: var(--text-light); margin-top: 6px; line-height: 1.5; }
.dish-price-big { font-size: 28px; font-weight: 700; color: var(--accent); }

.canteen-notice-card {
  display: flex; gap: 12px; margin: 16px; padding: 16px;
  background: #FFF8E1; border-radius: 14px; align-items: flex-start;
}
.canteen-notice-card strong { font-size: 14px; display: block; margin-bottom: 4px; }
.notice-text { font-size: 13px; color: #E65100; }

.merchant-order-section { padding: 16px 20px; }
.quantity-row, .pickup-row { display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; }
.label { font-weight: 600; color: var(--text-light); font-size: 14px; }
.quantity-control { display: flex; align-items: center; gap: 16px; }
.qty-btn { width: 36px; height: 36px; border-radius: 50%; border: 2px solid #eee; background: white; font-size: 18px; font-weight: 600; cursor: pointer; }
.qty-btn:hover { border-color: var(--primary); color: var(--primary); }
.qty-btn:disabled { opacity: 0.4; }
.qty-value { font-size: 20px; font-weight: 700; min-width: 24px; text-align: center; }
.queue-preview { display: flex; align-items: center; gap: 8px; padding: 12px 16px; background: var(--primary-light); border-radius: 10px; font-size: 13px; color: var(--primary); }

.order-bar {
  position: fixed; bottom: 0; left: 0; right: 0; display: flex; align-items: center; justify-content: space-between;
  padding: 12px 20px; background: white; border-top: 1px solid #f0f0f0;
  box-shadow: 0 -2px 12px rgba(0,0,0,0.06); max-width: 1200px; margin: 0 auto;
}
.order-total { display: flex; align-items: baseline; gap: 8px; }
.total-label { font-size: 14px; color: var(--text-light); }
.total-price { font-size: 24px; font-weight: 700; color: var(--accent); }
.order-btn {
  padding: 12px 40px; border: none; border-radius: 12px;
  background: linear-gradient(135deg, var(--primary), var(--secondary));
  color: white; font-size: 16px; font-weight: 600; cursor: pointer;
  box-shadow: 0 4px 12px rgba(255,107,53,0.3);
}
.order-btn:disabled { opacity: 0.6; cursor: not-allowed; }
</style>
