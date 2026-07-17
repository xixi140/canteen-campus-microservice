<template>
  <div class="order-page">
    <div class="detail-topbar">
      <button class="back-btn" @click="$router.back()"><el-icon :size="20"><ArrowLeft /></el-icon></button>
      <h3>📋 我的订单</h3>
      <span></span>
    </div>
    <div class="order-tabs">
      <button v-for="t in tabs" :key="t.key" :class="['otab', { active: activeTab === t.key }]" @click="activeTab = t.key">{{ t.name }}</button>
    </div>
    <div class="order-list">
      <div v-for="o in filteredOrders" :key="o.id" class="order-card" @click="$router.push(`/order/${o.id}`)">
        <div class="order-top">
          <div class="order-queue-wrap"><span class="oq-badge">A{{ o.queueNo }}</span><span class="oq-pickup">{{ o.pickupName }}</span></div>
          <span :class="['order-status-tag', o.status]">{{ statusText(o.status) }}</span>
        </div>
        <div class="order-body">
          <span class="order-emoji">{{ o.emoji }}</span>
          <div class="order-info"><p>{{ o.dishName }}</p><p class="o-qty">×{{ o.quantity }} · {{ o.shopName }}</p></div>
          <span class="order-amount">¥{{ o.totalAmount }}</span>
        </div>
        <div class="order-footer">
          <span class="order-time">{{ o.createTime }}</span>
          <span class="order-detail-link">查看详情 →</span>
        </div>
      </div>
      <el-empty v-if="filteredOrders.length === 0" description="暂无订单" />
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, computed } from 'vue'

const activeTab = ref('ALL')
const tabs = [{ key: 'ALL', name: '全部' }, { key: 'PREPARING', name: '备餐中' }, { key: 'PAID', name: '待取餐' }, { key: 'COMPLETED', name: '已完成' }]

const orders = ref([
  { id: 1002, queueNo: 34, pickupName: '张同学', emoji: '🍚', dishName: '蛋炒饭', quantity: 2, totalAmount: '20.00', status: 'PREPARING', createTime: '12:05', shopName: '老王炒饭' },
  { id: 1001, queueNo: 30, pickupName: '张同学', emoji: '🥩', dishName: '牛肉炒饭', quantity: 1, totalAmount: '18.00', status: 'COMPLETED', createTime: '11:30', shopName: '老王炒饭' }
])

const filteredOrders = computed(() => activeTab.value === 'ALL' ? orders.value : orders.value.filter(o => o.status === activeTab.value))
const statusText = (s: string) => ({ PAID: '待取餐', PREPARING: '备餐中', COMPLETED: '已完成', CANCELLED: '已取消' })[s] || s
</script>
<style scoped>
.order-page { padding-bottom: 30px; }
.detail-topbar { display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); position: sticky; top: 0; z-index: 10; }
.back-btn { width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border: none; border-radius: 50%; background: #f5f5f5; cursor: pointer; }
.detail-topbar h3 { font-size: 17px; font-weight: 600; }
.order-tabs { display: flex; gap: 0; background: white; padding: 0 16px; }
.otab { flex: 1; padding: 12px 0; border: none; border-bottom: 2px solid transparent; background: white; font-size: 13px; cursor: pointer; color: var(--text-light); transition: all 0.2s; }
.otab.active { color: var(--primary); border-bottom-color: var(--primary); font-weight: 600; }
.order-list { padding: 8px 16px; display: flex; flex-direction: column; gap: 10px; }
.order-card { background: white; border-radius: 14px; padding: 14px; cursor: pointer; box-shadow: 0 1px 4px rgba(0,0,0,0.04); transition: all 0.3s; }
.order-card:hover { box-shadow: var(--shadow-hover); }
.order-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
.order-queue-wrap { display: flex; align-items: center; gap: 8px; }
.oq-badge { background: var(--primary); color: white; font-weight: 700; padding: 2px 10px; border-radius: 6px; font-size: 14px; }
.oq-pickup { font-size: 13px; color: var(--text-light); }
.order-status-tag { font-size: 11px; padding: 3px 10px; border-radius: 8px; font-weight: 500; }
.order-status-tag.PAID { background: #FFF0E8; color: var(--primary); }
.order-status-tag.PREPARING { background: #FFF8E1; color: #FFA000; }
.order-status-tag.COMPLETED { background: #E8F8E8; color: #2ED573; }
.order-body { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
.order-emoji { font-size: 28px; }
.order-info { flex: 1; }
.order-info p { font-size: 14px; font-weight: 500; }
.o-qty { font-size: 12px; color: var(--text-lighter); margin-top: 2px; }
.order-amount { font-size: 18px; font-weight: 700; color: var(--accent); }
.order-footer { display: flex; justify-content: space-between; font-size: 12px; color: var(--text-lighter); border-top: 1px solid #f5f5f5; padding-top: 8px; }
.order-detail-link { color: var(--primary); }
</style>
