<template>
  <div>
    <div class="stats-grid">
      <div class="stat-card"><div class="s-icon">🏛️</div><div class="s-val">2</div><div class="s-lbl">食堂总数</div></div>
      <div class="stat-card"><div class="s-icon">🏪</div><div class="s-val">3</div><div class="s-lbl">入驻商户</div></div>
      <div class="stat-card"><div class="s-icon">🍽️</div><div class="s-val">86</div><div class="s-lbl">菜品总数</div></div>
      <div class="stat-card"><div class="s-icon">📋</div><div class="s-val">128</div><div class="s-lbl">今日订单</div></div>
      <div class="stat-card"><div class="s-icon">💰</div><div class="s-val">¥1,286</div><div class="s-lbl">今日收入</div></div>
      <div class="stat-card"><div class="s-icon">👤</div><div class="s-val">256</div><div class="s-lbl">活跃用户</div></div>
    </div>

    <div style="display:grid;grid-template-columns:1fr 1fr;gap:16px;margin-bottom:20px">
      <div class="section-card">
        <h3>📋 今日订单</h3>
        <div class="row"><span>已完成</span><span class="green">96</span></div>
        <div class="row"><span>备餐中</span><span class="orange">20</span></div>
        <div class="row"><span>待支付</span><span class="red">8</span></div>
        <div class="row"><span>已取消</span><span class="gray">4</span></div>
        <div class="row total"><span>总计</span><span>128</span></div>
      </div>
      <div class="section-card">
        <h3>📈 本周趋势</h3>
        <div class="bar-chart">
          <div v-for="(d,i) in weekData" :key="i" class="bar-item">
            <div class="bar-fill" :style="{height:d.pct+'%'}"></div>
            <span class="bar-day">{{ d.day }}</span>
            <span class="bar-val">{{ d.val }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="section-card">
      <h3>🕐 最近订单</h3>
      <table class="order-table">
        <thead><tr><th>订单号</th><th>用户</th><th>商品</th><th>金额</th><th>状态</th></tr></thead>
        <tbody>
          <tr v-for="o in recentOrders" :key="o.id">
            <td>#{{ o.id }}</td><td>{{ o.user }}</td><td>{{ o.items }}</td><td>¥{{ o.amount }}</td>
            <td><span :class="['tag',o.status]">{{ sText(o.status) }}</span></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
<script setup lang="ts">
const weekData = [
  { day:'一',val:28,pct:60 },{ day:'二',val:32,pct:70 },{ day:'三',val:25,pct:55 },
  { day:'四',val:38,pct:80 },{ day:'五',val:45,pct:95 },{ day:'六',val:35,pct:75 },{ day:'日',val:30,pct:65 }
]
const recentOrders = [
  { id:1001,user:'张同学',items:'🍚 蛋炒饭×2',amount:'20.00',status:'PREPARING' },
  { id:1002,user:'李同学',items:'🥩 牛肉炒饭×1',amount:'18.00',status:'COMPLETED' },
  { id:1003,user:'王同学',items:'🍚 扬州炒饭×1',amount:'15.00',status:'COMPLETED' },
  { id:1004,user:'赵同学',items:'🥤 可乐×2',amount:'6.00',status:'PAID' },
]
const sText = (s:string)=>({PAID:'待备餐',PREPARING:'备餐中',COMPLETED:'已完成'})[s]||s
</script>
<style scoped>
.stats-grid { display:grid;grid-template-columns:repeat(3,1fr);gap:12px;margin-bottom:20px; }
.stat-card { background:white;border-radius:12px;padding:16px;text-align:center;box-shadow:0 1px 3px rgba(0,0,0,0.04); }
.s-icon { font-size:24px; }
.s-val { font-size:24px;font-weight:700;color:#FF6B35;margin-top:4px; }
.s-lbl { font-size:12px;color:#999;margin-top:2px; }
.section-card { background:white;border-radius:12px;padding:16px;box-shadow:0 1px 3px rgba(0,0,0,0.04); }
.section-card h3 { font-size:15px;font-weight:600;margin-bottom:14px; }
.row { display:flex;justify-content:space-between;padding:8px 0;border-bottom:1px solid #f5f5f5;font-size:14px; }
.row.total { border-top:2px solid #eee;font-weight:600;padding-top:10px;margin-top:4px; }
.green { color:#2ED573;font-weight:600; }
.orange { color:#FFA502;font-weight:600; }
.red { color:#FF4757;font-weight:600; }
.gray { color:#999; }

.bar-chart { display:flex;justify-content:space-between;align-items:flex-end;height:100px;padding:0 4px; }
.bar-item { display:flex;flex-direction:column;align-items:center;gap:4px;width:12%; }
.bar-fill { width:100%;max-width:24px;background:linear-gradient(180deg,#FF6B35,#FFB347);border-radius:4px 4px 2px 2px;min-height:8px;transition:height 0.5s; }
.bar-day { font-size:10px;color:#999; }
.bar-val { font-size:10px;font-weight:600; }

.order-table { width:100%;border-collapse:collapse;font-size:13px; }
.order-table th { text-align:left;padding:8px;background:#fafafa;font-weight:600;color:#666;font-size:12px; }
.order-table td { padding:8px;border-bottom:1px solid #f5f5f5; }
.tag { font-size:11px;padding:2px 8px;border-radius:6px; }
.tag.PAID { background:#FFF0E8;color:#FF6B35; }
.tag.PREPARING { background:#FFF8E1;color:#FFA000; }
.tag.COMPLETED { background:#E8F8E8;color:#2ED573; }
</style>
