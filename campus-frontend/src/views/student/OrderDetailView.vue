<template>
  <div class="order-detail-page">
    <div class="detail-topbar">
      <button class="back-btn" @click="$router.back()"><el-icon :size="20"><ArrowLeft /></el-icon></button>
      <h3>订单详情</h3>
      <span></span>
    </div>
    <div class="od-status-bar" :class="order.status">
      <span class="od-status-icon">{{ statusIcon }}</span>
      <div class="od-status-text">
        <h3>{{ statusTitle }}</h3>
        <p>{{ statusDesc }}</p>
      </div>
    </div>
    <div class="od-queue" v-if="order.queueNo">
      <span class="oq-label">取餐号</span>
      <span class="oq-number">A{{ order.queueNo }}</span>
    </div>
    <div class="od-shop">
      <span class="od-shop-icon">{{ order.emoji }}</span>
      <div><h4>{{ order.shopName }}</h4><p class="od-shop-meta">{{ order.createTime }}</p></div>
    </div>
    <div class="od-dish">
      <div class="od-dish-row"><span>{{ order.emoji }} {{ order.dishName }}</span><span>× {{ order.quantity }}</span></div>
      <div class="od-dish-row total"><span>合计</span><span class="od-price">¥{{ order.totalAmount }}</span></div>
    </div>
    <div class="od-actions" v-if="order.status === 'PENDING_PAYMENT'">
      <el-button type="danger" plain round @click="cancelOrder">取消订单</el-button>
      <el-button type="primary" round>去支付</el-button>
    </div>
    <div class="od-actions" v-if="order.status === 'PREPARING' || order.status === 'PAID'">
      <el-button type="primary" round @click="confirmPickup">确认取餐</el-button>
    </div>
    <div class="od-actions" v-if="order.status === 'COMPLETED'">
      <el-button round @click="showReview = true">去评价</el-button>
    </div>
    <el-dialog v-model="showReview" title="评价" width="90%">
      <div style="text-align:center;padding:16px 0">
        <div style="margin-bottom:12px"><el-rate v-model="reviewStar" :colors="['#FF6B35','#FFB347','#FFD93D']" /></div>
        <el-input v-model="reviewText" type="textarea" :rows="3" placeholder="说说你的评价..." />
      </div>
      <template #footer>
        <el-button @click="showReview = false">取消</el-button>
        <el-button type="primary" @click="submitReview">提交评价</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const showReview = ref(false)
const reviewStar = ref(5)
const reviewText = ref('')

const order = ref({
  id: Number(route.params.id), queueNo: 34, pickupName: '张同学',
  dishName: '蛋炒饭', emoji: '🍚', quantity: 2, totalAmount: '20.00',
  status: 'PREPARING', createTime: '2026-06-25 12:05', shopName: '老王炒饭', shopId: 1
})

const statusIcon = computed(() => ({ PENDING_PAYMENT: '⏳', PAID: '✅', PREPARING: '👨‍🍳', COMPLETED: '🎉', CANCELLED: '❌' })[order.value.status] || '📋')
const statusTitle = computed(() => ({ PENDING_PAYMENT: '等待支付', PAID: '已支付，等待备餐', PREPARING: '正在备餐中', COMPLETED: '已完成', CANCELLED: '已取消' })[order.value.status] || '')
const statusDesc = computed(() => ({ PENDING_PAYMENT: '请尽快完成支付', PAID: '商户正在处理您的订单', PREPARING: '预计几分钟后完成', COMPLETED: '感谢您的光临', CANCELLED: '订单已取消' })[order.value.status] || '')

function cancelOrder() { order.value.status = 'CANCELLED'; ElMessage.success('订单已取消') }
function confirmPickup() { order.value.status = 'COMPLETED'; ElMessage.success('🎉 取餐成功') }
function submitReview() { showReview.value = false; ElMessage.success('评价提交成功！') }
</script>
<style scoped>
.order-detail-page { padding-bottom: 30px; }
.detail-topbar { display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); position: sticky; top: 0; z-index: 10; }
.back-btn { width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border: none; border-radius: 50%; background: #f5f5f5; cursor: pointer; }
.detail-topbar h3 { font-size: 17px; font-weight: 600; }
.od-status-bar { display: flex; gap: 14px; align-items: center; padding: 24px 20px; }
.od-status-bar.PREPARING { background: #FFF8E1; }
.od-status-bar.COMPLETED { background: #E8F8E8; }
.od-status-bar.PAID { background: #FFF0E8; }
.od-status-icon { font-size: 36px; }
.od-status-text h3 { font-size: 18px; font-weight: 700; }
.od-status-text p { font-size: 13px; color: var(--text-light); margin-top: 4px; }
.od-queue { display: flex; align-items: center; justify-content: center; gap: 12px; padding: 16px; background: white; margin: 0 16px; border-radius: 14px; }
.oq-label { font-size: 14px; color: var(--text-light); }
.oq-number { font-size: 32px; font-weight: 800; color: var(--primary); }
.od-shop { display: flex; gap: 12px; padding: 16px; background: white; margin: 10px 16px; border-radius: 14px; }
.od-shop-icon { font-size: 32px; }
.od-shop h4 { font-size: 15px; font-weight: 600; }
.od-shop-meta { font-size: 12px; color: var(--text-lighter); margin-top: 2px; }
.od-dish { background: white; margin: 0 16px; border-radius: 14px; padding: 16px; }
.od-dish-row { display: flex; justify-content: space-between; padding: 8px 0; font-size: 14px; }
.od-dish-row.total { border-top: 1px solid #f5f5f5; font-size: 15px; font-weight: 600; margin-top: 4px; padding-top: 12px; }
.od-price { color: var(--accent); font-size: 20px; font-weight: 700; }
.od-actions { display: flex; gap: 12px; justify-content: center; padding: 20px 16px; }
</style>
