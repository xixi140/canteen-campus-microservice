<template>
  <div class="shop-detail">
    <div class="detail-topbar">
      <button class="back-btn" @click="$router.back()"><el-icon :size="20"><ArrowLeft /></el-icon></button>
      <h3>{{ shop.name }}</h3>
      <span></span>
    </div>
    <div class="shop-header-bg">
      <div class="shop-header-content">
        <div class="shop-header-left">
          <div class="shop-icon-wrap">{{ shop.icon }}</div>
          <div class="shop-meta">
            <h2>{{ shop.name }}</h2>
            <div class="shop-rating-row">
              <span class="rating-star">★</span>
              <span class="rating-num">{{ shop.rating }}</span><span class="rating-divider">|</span><span>月售{{ shop.sales }}</span>
            </div>
            <div class="shop-sub-info">
              <span>⏰ {{ shop.openTime }}</span><span class="dot">·</span><span>约{{ shop.deliveryTime }}分钟</span>
            </div>
          </div>
        </div>
        <div class="shop-status"><span class="status-dot"></span>营业中</div>
      </div>
      <div class="queue-display">
        <div class="queue-item"><span class="queue-label">当前叫号</span><span class="queue-num">A{{ currentQueue }}</span></div>
        <div class="queue-item"><span class="queue-label">我的号码</span><span class="queue-num muted">{{ myQueue ? 'A'+myQueue : '—' }}</span></div>
      </div>
    </div>
    <div class="shop-notice"><el-icon><WarningFilled /></el-icon><span>{{ shop.notice }}</span></div>

    <div class="menu-layout">
      <div class="category-sidebar">
        <button v-for="(cat, idx) in categories" :key="cat" :class="['cat-item', { active: activeCat === cat }]" @click="activeCat = cat">
          <span class="cat-dot" :style="{ background: catColors[idx] }"></span><span>{{ cat }}</span><span class="cat-count">{{ getCatCount(cat) }}</span>
        </button>
      </div>
      <div class="dish-area">
        <div v-for="d in filteredDishes" :key="d.id" class="dish-card">
          <div class="dish-info-block">
            <h4>{{ d.name }}</h4>
            <div class="dish-badges"><span class="badge badge-hot" v-if="d.hot">热销</span><span class="badge badge-signature" v-if="d.signature">招牌</span></div>
            <p class="dish-desc">{{ d.desc }}</p><div class="dish-sales">月售{{ d.sales }}</div>
            <div class="dish-price-row">
              <span class="dish-price">¥<strong>{{ d.price }}</strong></span>
              <button class="add-btn" @click.stop="addToCart(d)" :disabled="d.stock === 0"><span v-if="d.stock > 0">+</span><span v-else>售罄</span></button>
            </div>
          </div>
          <div class="dish-emoji-wrap"><span class="dish-emoji">{{ d.emoji }}</span></div>
        </div>
      </div>
    </div>

    <transition name="slide-up">
      <div v-if="cart.items.length" class="cart-bar">
        <div class="cart-bar-left" @click="showCartDetail = !showCartDetail">
          <div class="cart-icon-wrap"><el-icon :size="26"><ShoppingCart /></el-icon><span class="cart-badge-dot">{{ cart.totalCount }}</span></div>
          <div class="cart-price-info"><span class="cart-price">¥{{ cart.totalPrice }}</span><span class="cart-desc" v-if="!showCartDetail">点击查看明细</span></div>
        </div>
        <button class="cart-checkout-btn" @click="showCheckout = true">去结算</button>
      </div>
    </transition>

    <transition name="slide-up">
      <div v-if="showCartDetail && cart.items.length" class="cart-detail-panel">
        <div class="cart-detail-header"><span>已选商品</span><button class="clear-cart-btn" @click="cart.clear()">清空</button></div>
        <div v-for="item in cart.items" :key="item.id" class="cart-detail-item">
          <span>{{ item.emoji }} {{ item.name }}</span>
          <div class="cdi-right"><span class="cdi-price">¥{{ (item.price * item.quantity).toFixed(2) }}</span>
            <div class="cdi-qty"><button @click="cart.changeQuantity(item.id, -1)">−</button><span>{{ item.quantity }}</span><button @click="cart.changeQuantity(item.id, 1)">+</button></div>
          </div>
        </div>
      </div>
    </transition>

    <el-dialog v-model="showCheckout" title="确认订单" width="92%" :close-on-click-modal="false">
      <div class="checkout-body">
        <div v-for="item in cart.items" :key="item.id" class="co-item"><span>{{ item.emoji }} {{ item.name }} × {{ item.quantity }}</span><span class="co-price">¥{{ (item.price * item.quantity).toFixed(2) }}</span></div>
        <div class="co-total"><span>合计</span><span class="co-total-price">¥{{ cart.totalPrice }}</span></div>
        <div class="co-field"><label>👤 取餐人</label><el-input v-model="pickupName" placeholder="输入昵称或姓名" size="large" /></div>
        <div class="co-notice"><el-icon><InfoFilled /></el-icon><span>到店后报取餐号即可取餐</span></div>
      </div>
      <template #footer>
        <el-button @click="showCheckout = false">取消</el-button>
        <el-button type="primary" @click="placeOrder" :loading="ordering">{{ ordering ? '提交中...' : '确认下单' }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useCartStore } from '@/store/cart'
import { createOrderAPI } from '@/api'

const route = useRoute()
const router = useRouter()
const cart = useCartStore()
const shopId = Number(route.params.id)

const showCheckout = ref(false)
const showCartDetail = ref(false)
const ordering = ref(false)
const pickupName = ref('')
const activeCat = ref('全部')
const myQueue = ref<number | null>(null)
const currentQueue = ref(parseInt(localStorage.getItem('queueNo') || '32'))
const catColors = ['#FF6B35','#4CAF50','#2196F3','#9C27B0','#FF9800','#00BCD4']

const shops: Record<number, any> = {
  1: { name:'老王炒饭', icon:'🍚', rating:4.8, sales:1286, openTime:'09:00-21:00', deliveryTime:15, notice:'🔥 咖喱鸡肉饭限时特惠16元' },
  2: { name:'李记麻辣烫', icon:'🌶️', rating:4.5, sales:956, openTime:'10:00-22:00', deliveryTime:20, notice:'🌶️ 买满20送饮料' },
  3: { name:'好滋味面馆', icon:'🍜', rating:4.6, sales:823, openTime:'07:00-21:00', deliveryTime:12, notice:'🍜 每日现拉手工面' }
}

const dishData: Record<number, any[]> = {
  1: [
    { id:11, name:'蛋炒饭', emoji:'🍚', price:10, stock:30, category:'炒饭', desc:'粒粒分明', sales:523, signature:true },
    { id:12, name:'扬州炒饭', emoji:'🍚', price:15, stock:25, category:'炒饭', desc:'正宗扬州风味', sales:386, hot:true },
    { id:13, name:'牛肉炒饭', emoji:'🥩', price:18, stock:20, category:'炒饭', desc:'嫩牛肉粒', sales:298, hot:true },
    { id:14, name:'老干妈炒饭', emoji:'🌶️', price:12, stock:35, category:'炒饭', desc:'香辣过瘾', sales:245 },
    { id:115, name:'咖喱鸡肉饭', emoji:'🍛', price:16, stock:22, category:'盖饭', desc:'日式咖喱', sales:189, signature:true },
    { id:117, name:'可乐', emoji:'🥤', price:3, stock:100, category:'饮品', desc:'冰镇可乐', sales:876 },
    { id:118, name:'酸梅汤', emoji:'🧃', price:5, stock:60, category:'饮品', desc:'冰镇酸梅汤', sales:432 }
  ],
  2: [
    { id:21, name:'经典麻辣烫', emoji:'🍲', price:15, stock:50, category:'麻辣烫', desc:'自选称重', sales:678, signature:true },
    { id:22, name:'毛肚麻辣烫', emoji:'🥘', price:22, stock:20, category:'麻辣烫', desc:'加量毛肚', sales:345, hot:true },
    { id:23, name:'酸辣粉', emoji:'🍜', price:10, stock:40, category:'小吃', desc:'正宗重庆酸辣粉', sales:289 },
    { id:25, name:'冰粉', emoji:'🍮', price:6, stock:45, category:'饮品', desc:'清凉解辣', sales:567, hot:true }
  ],
  3: [
    { id:31, name:'牛肉拉面', emoji:'🍜', price:14, stock:30, category:'面食', desc:'牛骨浓汤', sales:456, signature:true },
    { id:32, name:'炸酱面', emoji:'🍝', price:10, stock:35, category:'面食', desc:'老北京炸酱面', sales:345, hot:true },
    { id:33, name:'葱油拌面', emoji:'🍝', price:8, stock:40, category:'面食', desc:'葱香四溢', sales:298 },
    { id:34, name:'红烧牛肉面', emoji:'🍜', price:18, stock:20, category:'面食', desc:'大块牛肉', sales:267, hot:true },
    { id:35, name:'三鲜水饺', emoji:'🥟', price:12, stock:25, category:'小吃', desc:'手工水饺', sales:189 }
  ]
}

const shop = shops[shopId] || shops[1]
const allDishes = dishData[shopId] || dishData[1]
const categories = computed(() => { const s = new Set(allDishes.map((d:any) => d.category)); return ['全部', ...Array.from(s)] as string[] })
const filteredDishes = computed(() => activeCat.value === '全部' ? allDishes : allDishes.filter((d:any) => d.category === activeCat.value))
function getCatCount(cat: string) { return cat === '全部' ? allDishes.length : allDishes.filter((d:any) => d.category === cat).length }

function addToCart(d: any) { cart.addItem({ id: d.id, name: d.name, emoji: d.emoji, price: d.price, quantity: 1, shopId }) }

async function placeOrder() {
  if (!pickupName.value.trim()) { ElMessage.warning('请输入取餐人称呼'); return }
  ordering.value = true
  try {
    const userId = Number(localStorage.getItem('userId') || 2)
    for (const item of cart.items) {
      await createOrderAPI({ userId, sourceType: 'MERCHANT', dishId: item.id, quantity: item.quantity,
        requestId: Date.now() + '-' + Math.random().toString(36).substr(2,9) })
    }
    currentQueue.value++
    myQueue.value = currentQueue.value
    localStorage.setItem('queueNo', String(currentQueue.value))
    cart.clear(); pickupName.value = ''
    ElMessage.success(`🎉 下单成功！取餐号 A${currentQueue.value}`)
    router.push('/orders')
  } catch { ElMessage.error('下单失败，请重试') }
  finally { ordering.value = false; showCheckout.value = false }
}
</script>

<style scoped>
.shop-detail { padding-bottom: 80px; background: #f5f5f5; min-height: 100vh; }
.detail-topbar { display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); position: sticky; top: 0; z-index: 50; }
.back-btn { width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border: none; border-radius: 50%; background: #f5f5f5; cursor: pointer; }
.detail-topbar h3 { font-size: 17px; font-weight: 600; }
.shop-header-bg { background: linear-gradient(135deg, var(--primary), var(--secondary)); padding: 16px 16px 0; color: white; }
.shop-header-content { display: flex; justify-content: space-between; align-items: flex-start; }
.shop-header-left { display: flex; gap: 14px; flex:1; }
.shop-icon-wrap { width: 60px; height: 60px; background: rgba(255,255,255,0.2); border-radius: 14px; display: flex; align-items: center; justify-content: center; font-size: 32px; flex-shrink:0; }
.shop-meta h2 { font-size: 20px; font-weight: 700; }
.shop-rating-row { display: flex; align-items: center; gap: 4px; font-size: 13px; margin: 4px 0; }
.rating-star { color: #FFD93D; font-size: 16px; }
.rating-divider { opacity: 0.4; }
.shop-sub-info { display: flex; align-items: center; gap: 6px; font-size: 12px; opacity: 0.85; }
.shop-status { display: flex; align-items: center; gap: 6px; font-size: 13px; background: rgba(255,255,255,0.2); padding: 6px 14px; border-radius: 20px; white-space:nowrap; }
.status-dot { width: 8px; height: 8px; border-radius: 50%; background: #2ED573; }
.shop-notice { display: flex; align-items: center; gap: 8px; padding: 10px 16px; background: #FFF8E1; font-size: 12px; color: #E65100; }
.queue-display { display: flex; gap: 12px; margin-top: 14px; padding: 12px 0; }
.queue-item { flex: 1; background: rgba(255,255,255,0.15); border-radius: 10px; padding: 10px; text-align: center; }
.queue-label { display: block; font-size: 11px; opacity: 0.8; }
.queue-num { font-size: 24px; font-weight: 700; margin-top: 2px; display: block; }
.queue-num.muted { opacity: 0.6; }
.menu-layout { display: flex; background: white; }
.category-sidebar { width: 90px; flex-shrink: 0; background: #FAFAFA; padding: 8px 0; }
.cat-item { width: 100%; display: flex; flex-direction: column; align-items: center; gap: 4px; padding: 12px 8px; border: none; background: transparent; cursor: pointer; font-size: 12px; color: #666; position: relative; }
.cat-item.active { background: white; color: var(--text); font-weight: 600; }
.cat-item.active::before { content: ''; position: absolute; left: 0; top: 50%; transform: translateY(-50%); width: 3px; height: 20px; background: var(--primary); border-radius: 0 3px 3px 0; }
.cat-count { font-size: 10px; color: #999; }
.dish-area { flex: 1; padding: 12px; }
.dish-card { display: flex; justify-content: space-between; gap: 12px; padding: 14px 0; border-bottom: 1px solid #f5f5f5; }
.dish-card:last-child { border-bottom: none; }
.dish-info-block { flex: 1; }
.dish-info-block h4 { font-size: 15px; font-weight: 600; margin-bottom: 4px; }
.dish-badges { display: flex; gap: 4px; margin-bottom: 4px; }
.badge { font-size: 10px; padding: 1px 6px; border-radius: 4px; }
.badge-hot { background: #FFF0F0; color: #FF4757; }
.badge-signature { background: #FFF0E8; color: #FF6B35; }
.dish-desc { font-size: 12px; color: #999; margin-bottom: 2px; }
.dish-sales { font-size: 11px; color: #999; margin-bottom: 6px; }
.dish-price-row { display: flex; align-items: center; justify-content: space-between; }
.dish-price { font-size: 14px; color: #FF4757; }
.dish-price strong { font-size: 18px; }
.add-btn { width: 32px; height: 32px; border-radius: 50%; border: none; background: #FF6B35; color: white; font-size: 20px; cursor: pointer; display: flex; align-items: center; justify-content: center; }
.add-btn:disabled { background: #ddd; color: #999; }
.dish-emoji-wrap { width: 80px; height: 80px; background: #FAFAFA; border-radius: 10px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.dish-emoji { font-size: 36px; }
.cart-bar { position: fixed; bottom: 0; left: 0; right: 0; z-index: 100; display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; background: #2D3436; max-width: 1200px; margin: 0 auto; }
.cart-bar-left { display: flex; align-items: center; gap: 12px; flex: 1; cursor: pointer; }
.cart-icon-wrap { position: relative; }
.cart-badge-dot { position: absolute; top: -8px; right: -8px; background: #FF4757; color: white; font-size: 11px; width: 20px; height: 20px; display: flex; align-items: center; justify-content: center; border-radius: 50%; font-weight: 700; }
.cart-price { font-size: 20px; font-weight: 700; color: white; }
.cart-desc { font-size: 11px; color: rgba(255,255,255,0.6); }
.cart-checkout-btn { padding: 12px 28px; border: none; border-radius: 30px; background: linear-gradient(135deg, #FF6B35, #FFB347); color: white; font-size: 15px; font-weight: 600; cursor: pointer; }
.cart-detail-panel { position: fixed; bottom: 60px; left: 0; right: 0; z-index: 99; background: white; border-radius: 16px 16px 0 0; max-width: 1200px; margin: 0 auto; box-shadow: 0 -4px 20px rgba(0,0,0,0.1); padding: 16px; max-height: 50vh; overflow-y: auto; }
.cdi-qty { display: flex; align-items: center; gap: 10px; }
.cdi-qty button { width: 24px; height: 24px; border-radius: 50%; border: 1px solid #ddd; background: white; font-size: 14px; cursor: pointer; }
.cdi-qty span { font-size: 15px; font-weight: 600; min-width: 16px; text-align: center; }
.slide-up-enter-active, .slide-up-leave-active { transition: all 0.3s; }
.slide-up-enter-from, .slide-up-leave-to { transform: translateY(100%); opacity: 0; }
.co-item { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #f5f5f5; font-size: 14px; }
.co-total { display: flex; justify-content: space-between; padding: 12px 0; font-size: 16px; font-weight: 600; }
.co-total-price { font-size: 22px; font-weight: 700; color: #FF4757; }
.co-field { margin: 12px 0; }
.co-field label { font-size: 14px; font-weight: 500; display: block; margin-bottom: 8px; }
.co-notice { display: flex; align-items: center; gap: 6px; padding: 10px; background: #E8F4FD; border-radius: 8px; font-size: 12px; color: #1565C0; }
</style>
