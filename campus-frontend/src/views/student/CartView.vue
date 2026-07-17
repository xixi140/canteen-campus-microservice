<template>
  <div class="cart-page">
    <div class="detail-topbar">
      <button class="back-btn" @click="$router.back()"><el-icon :size="20"><ArrowLeft /></el-icon></button>
      <h3>🛒 购物车</h3>
      <button class="clear-btn" @click="handleClear" v-if="cart.items.length">清空</button>
    </div>

    <div v-if="cart.items.length === 0" class="empty-cart">
      <span class="empty-icon">🛒</span>
      <p>购物车是空的</p>
      <el-button type="primary" round @click="$router.push('/merchant-shops')">去商户点餐</el-button>
    </div>

    <template v-else>
      <div class="cart-list">
        <div v-for="item in cart.items" :key="item.id" class="cart-item">
          <span class="item-emoji">{{ item.emoji }}</span>
          <div class="item-info">
            <h4>{{ item.name }}</h4>
            <span class="item-price-label">¥{{ item.price }}</span>
          </div>
          <div class="item-qty">
            <button class="qty-btn-sm" @click="cart.changeQuantity(item.id, -1)">−</button>
            <span>{{ item.quantity }}</span>
            <button class="qty-btn-sm" @click="cart.changeQuantity(item.id, 1)">+</button>
          </div>
        </div>
      </div>

      <div class="cart-summary-card">
        <div class="cs-row">
          <span>小计 ({{ cart.totalCount }}件)</span>
          <span class="cs-price">¥{{ cart.totalPrice }}</span>
        </div>
        <div class="cs-row">
          <span>配送方式</span>
          <span>到店取餐</span>
        </div>
      </div>

      <div class="cart-footer-bar">
        <div class="footer-left">
          <span class="total-label">合计</span>
          <span class="total-amount">¥{{ cart.totalPrice }}</span>
        </div>
        <button class="checkout-btn" @click="showCheckout = true">去结算 ({{ cart.totalCount }})</button>
      </div>
    </template>

    <!-- 结算弹窗 -->
    <el-dialog v-model="showCheckout" title="确认订单" width="92%" :close-on-click-modal="false">
      <div class="checkout-body">
        <div class="checkout-items">
          <div v-for="item in cart.items" :key="item.id" class="co-item">
            <span>{{ item.emoji }} {{ item.name }}</span>
            <span>¥{{ item.price }} × {{ item.quantity }}</span>
          </div>
        </div>
        <div class="co-total">
          <span>合计</span><span class="co-total-price">¥{{ cart.totalPrice }}</span>
        </div>
        <div class="co-field">
          <label>👤 取餐人</label>
          <el-input v-model="pickupName" placeholder="输入您的昵称或姓名" size="large" />
        </div>
        <div class="co-notice">
          <el-icon><InfoFilled /></el-icon>
          <span>下单后将获得取餐号，到店出示即可取餐</span>
        </div>
      </div>
      <template #footer>
        <el-button @click="showCheckout = false">取消</el-button>
        <el-button type="primary" @click="placeOrder" :loading="ordering">
          {{ ordering ? '提交中...' : `提交订单` }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useCartStore } from '@/store/cart'

const router = useRouter()
const cart = useCartStore()
const showCheckout = ref(false)
const ordering = ref(false)
const pickupName = ref('')

function handleClear() {
  ElMessageBox.confirm('清空购物车？', '提示').then(() => { cart.clear(); ElMessage.success('已清空') }).catch(() => {})
}

function placeOrder() {
  if (!pickupName.value.trim()) { ElMessage.warning('请输入取餐人名称'); return }
  ordering.value = true
  setTimeout(() => {
    const queueNo = parseInt(localStorage.getItem('queueNo') || '32') + 1
    localStorage.setItem('queueNo', String(queueNo))
    ordering.value = false
    showCheckout.value = false
    cart.clear()
    pickupName.value = ''
    ElMessage.success(`🎉 下单成功！取餐号 A${queueNo}，请到店取餐`)
    router.push('/orders')
  }, 1200)
}
</script>

<style scoped>
.cart-page { padding-bottom: 100px; }
.detail-topbar { display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); position: sticky; top: 0; z-index: 10; }
.back-btn { width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border: none; border-radius: 50%; background: #f5f5f5; cursor: pointer; }
.detail-topbar h3 { font-size: 17px; font-weight: 600; }
.clear-btn { font-size: 13px; color: #999; background: none; border: none; cursor: pointer; }
.empty-cart { text-align: center; padding: 80px 0; }
.empty-icon { font-size: 64px; display: block; margin-bottom: 16px; }
.empty-cart p { font-size: 16px; color: var(--text-light); margin-bottom: 20px; }
.cart-list { padding: 12px 16px; display: flex; flex-direction: column; gap: 10px; }
.cart-item { display: flex; align-items: center; gap: 12px; padding: 14px; background: white; border-radius: 14px; box-shadow: 0 1px 4px rgba(0,0,0,0.04); }
.item-emoji { font-size: 32px; }
.item-info { flex: 1; }
.item-info h4 { font-size: 15px; font-weight: 500; }
.item-price-label { font-size: 14px; color: var(--accent); font-weight: 600; }
.item-qty { display: flex; align-items: center; gap: 8px; }
.qty-btn-sm { width: 28px; height: 28px; border-radius: 50%; border: 1.5px solid #eee; background: white; font-size: 16px; cursor: pointer; display: flex; align-items: center; justify-content: center; }
.qty-btn-sm:hover { border-color: var(--primary); color: var(--primary); }
.item-qty span { font-size: 16px; font-weight: 600; min-width: 16px; text-align: center; }
.cart-summary-card { margin: 0 16px; padding: 14px; background: white; border-radius: 14px; }
.cs-row { display: flex; justify-content: space-between; padding: 6px 0; font-size: 14px; color: var(--text-light); }
.cs-price { font-weight: 600; color: var(--text); }
.cart-footer-bar { position: fixed; bottom: 0; left: 0; right: 0; display: flex; align-items: center; justify-content: space-between; padding: 12px 20px; background: white; border-top: 1px solid #f0f0f0; max-width: 1200px; margin: 0 auto; z-index: 100; }
.total-label { font-size: 14px; color: var(--text-light); }
.total-amount { font-size: 24px; font-weight: 700; color: var(--accent); }
.checkout-btn { padding: 12px 36px; border: none; border-radius: 12px; background: linear-gradient(135deg, var(--primary), var(--secondary)); color: white; font-size: 16px; font-weight: 600; cursor: pointer; box-shadow: 0 4px 12px rgba(255,107,53,0.3); }
.checkout-body { padding: 0 4px; }
.co-item { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #f5f5f5; font-size: 14px; }
.co-total { display: flex; justify-content: space-between; padding: 12px 0; font-size: 16px; font-weight: 600; }
.co-total-price { font-size: 22px; font-weight: 700; color: var(--accent); }
.co-field { margin: 12px 0; }
.co-field label { font-size: 14px; font-weight: 500; display: block; margin-bottom: 8px; }
.co-notice { display: flex; align-items: center; gap: 6px; padding: 10px; background: #FFF8E1; border-radius: 8px; font-size: 12px; color: #E65100; }
</style>
