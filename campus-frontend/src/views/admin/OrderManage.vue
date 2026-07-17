<template>
  <div>
    <div class="stats-bar">
      <div class="stat"><strong>{{ orders.length }}</strong><span>全部订单</span></div>
      <div class="stat"><strong>{{ paidCount }}</strong><span>待备餐</span></div>
      <div class="stat"><strong>{{ prepCount }}</strong><span>备餐中</span></div>
      <div class="stat"><strong>{{ doneCount }}</strong><span>已完成</span></div>
    </div>
    <div class="filter-bar">
      <button v-for="f in filters" :key="f" :class="['ft',{active:activeFilter===f}]" @click="activeFilter=f">{{ f }}</button>
    </div>
    <div class="order-table">
      <div v-for="o in filteredOrders" :key="o.id" class="oc">
        <div class="oc-top">
          <span class="oc-id">#{{ o.id }}</span>
          <span class="oc-queue">A{{ o.queue }}</span>
          <span class="oc-user">{{ o.user }}</span>
          <span class="oc-time">{{ o.time }}</span>
        </div>
        <div class="oc-body">
          <span class="oc-dish">{{ o.items }}</span>
          <span class="oc-shop">{{ o.shop }}</span>
        </div>
        <div class="oc-bot">
          <span class="oc-amount">¥{{ o.amount }}</span>
          <span :class="['tag',o.status]">{{ sText(o.status) }}</span>
        </div>
        <div v-if="o.status==='PAID'" class="oc-acts">
          <el-button size="small" type="primary" round @click="o.status='PREPARING'">开始备餐</el-button>
        </div>
        <div v-else-if="o.status==='PREPARING'" class="oc-acts">
          <el-button size="small" type="success" round @click="o.status='COMPLETED'">确认取餐</el-button>
        </div>
      </div>
      <el-empty v-if="filteredOrders.length===0" description="暂无订单" />
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, computed } from 'vue'

const activeFilter = ref('全部')
const filters = ['全部','待备餐','备餐中','已完成']

const orders = ref([
  { id:1001,queue:33,user:'张同学',items:'🍚 蛋炒饭×2',shop:'老王炒饭',amount:'20.00',status:'PAID',time:'12:05' },
  { id:1002,queue:34,user:'李同学',items:'🥩 牛肉炒饭×1',shop:'老王炒饭',amount:'18.00',status:'PREPARING',time:'11:55' },
  { id:1003,queue:30,user:'王同学',items:'🍚 扬州炒饭×1',shop:'老王炒饭',amount:'15.00',status:'COMPLETED',time:'11:30' },
  { id:1004,queue:35,user:'赵同学',items:'🌶️ 麻辣烫×1',shop:'李记麻辣烫',amount:'22.00',status:'PAID',time:'12:10' },
  { id:1005,queue:31,user:'刘同学',items:'🍜 牛肉拉面×1',shop:'好滋味面馆',amount:'14.00',status:'COMPLETED',time:'11:25' },
])

const paidCount = computed(() => orders.value.filter(o=>o.status==='PAID').length)
const prepCount = computed(() => orders.value.filter(o=>o.status==='PREPARING').length)
const doneCount = computed(() => orders.value.filter(o=>o.status==='COMPLETED').length)
const filteredOrders = computed(() => {
  if (activeFilter.value === '全部') return orders.value
  const map: Record<string,string> = {'待备餐':'PAID','备餐中':'PREPARING','已完成':'COMPLETED'}
  return orders.value.filter(o => o.status === map[activeFilter.value])
})
const sText = (s:string)=>({PAID:'待备餐',PREPARING:'备餐中',COMPLETED:'已完成'})[s]||s
</script>
<style scoped>
.stats-bar { display:flex;gap:12px;margin-bottom:16px; }
.stat { flex:1;background:white;border-radius:10px;padding:12px;text-align:center; }
.stat strong { display:block;font-size:20px;color:#FF6B35; }
.stat span { font-size:12px;color:#999; }
.filter-bar { display:flex;gap:8px;margin-bottom:16px; }
.ft { padding:6px 14px;border:none;border-radius:8px;background:white;cursor:pointer;font-size:13px; }
.ft.active { background:#FF6B35;color:white; }
.order-table { display:flex;flex-direction:column;gap:8px; }
.oc { background:white;border-radius:12px;padding:14px; }
.oc-top { display:flex;align-items:center;gap:8px;font-size:13px;color:#666;margin-bottom:6px; }
.oc-queue { background:#FF6B35;color:white;font-weight:700;padding:2px 8px;border-radius:4px;font-size:12px; }
.oc-user { flex:1; }
.oc-time { font-size:11px;color:#999; }
.oc-body { display:flex;justify-content:space-between;font-size:14px;margin-bottom:6px; }
.oc-shop { font-size:12px;color:#999; }
.oc-bot { display:flex;justify-content:space-between;align-items:center; }
.oc-amount { font-size:16px;font-weight:700;color:#FF6B35; }
.tag { font-size:11px;padding:2px 8px;border-radius:6px; }
.tag.PAID { background:#FFF0E8;color:#FF6B35; }
.tag.PREPARING { background:#FFF8E1;color:#FFA000; }
.tag.COMPLETED { background:#E8F8E8;color:#2ED573; }
.oc-acts { margin-top:8px; }
</style>
