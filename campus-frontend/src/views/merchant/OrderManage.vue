<template>
  <div><div class="tabs" style="display:flex;gap:8px;margin-bottom:16px">
    <button v-for="f in filters" :key="f" :class="['tab',{active:active===f}]" @click="active=f" style="padding:6px 14px;border:none;border-radius:8px;background:white;cursor:pointer;font-size:13px">{{ f }}</button>
  </div>
  <div class="list">
    <div v-for="o in orders" :key="o.id" class="card">
      <div class="card-top"><span class="qno">A{{ o.queue }}</span><span>{{ o.name }}</span></div>
      <div class="card-body">{{ o.items }}</div>
      <div class="card-actions">
        <el-button v-if="o.status==='PAID'" type="primary" size="small" round @click="o.status='PREPARING'">开始备餐</el-button>
        <el-button v-if="o.status==='PREPARING'" type="success" size="small" round @click="o.status='COMPLETED'">完成取餐</el-button>
      </div>
    </div>
  </div></div>
</template>
<script setup lang="ts">
import { ref } from 'vue'
const active = ref('待处理')
const filters = ['全部','待处理','备餐中','已完成']
const orders = ref([
  { id:1,queue:33,name:'张同学',items:'🍚 蛋炒饭×2',status:'PAID' },
  { id:2,queue:34,name:'李同学',items:'🥩 牛肉炒饭×1',status:'PREPARING' },
  { id:3,queue:30,name:'王同学',items:'🍚 扬州炒饭×1',status:'COMPLETED' },
])
</script>
<style scoped>
.tab.active { background:#FF6B35;color:white; }
.list { display:flex;flex-direction:column;gap:8px; }
.card { background:white;border-radius:12px;padding:14px; }
.card-top { display:flex;align-items:center;gap:8px;margin-bottom:6px; }
.qno { background:#FF6B35;color:white;font-weight:700;padding:2px 8px;border-radius:4px;font-size:12px; }
.card-body { font-size:14px;margin-bottom:8px; }
</style>
