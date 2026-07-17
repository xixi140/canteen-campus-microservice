<template>
  <div>
    <div class="stats-grid">
      <div class="stat-card"><div class="sc-icon">💰</div><div class="sc-val">¥368.00</div><div class="sc-lbl">今日收入</div></div>
      <div class="stat-card"><div class="sc-icon">📋</div><div class="sc-val">32</div><div class="sc-lbl">今日订单</div></div>
      <div class="stat-card"><div class="sc-icon">⭐</div><div class="sc-val">4.8</div><div class="sc-lbl">店铺评分</div></div>
      <div class="stat-card"><div class="sc-icon">👥</div><div class="sc-val">1286</div><div class="sc-lbl">月售单量</div></div>
    </div>

    <div style="display:grid;grid-template-columns:1fr 1fr;gap:16px;margin-bottom:20px">
      <div class="section-card">
        <h3>📋 今日订单</h3>
        <div class="row"><span>已完成</span><span class="green">24</span></div>
        <div class="row"><span>备餐中</span><span class="orange">5</span></div>
        <div class="row"><span>待处理</span><span class="red">3</span></div>
        <div class="row total"><span>总计</span><span>32</span></div>
      </div>
      <div class="section-card">
        <h3>📈 本周趋势</h3>
        <div class="bar-chart">
          <div v-for="(d,i) in weekData" :key="i" class="bar-item">
            <div class="bar-fill" :style="{height:d.pct+'%'}"></div>
            <span class="bar-day">{{ d.day }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="section-card">
      <h3>🕐 最近订单</h3>
      <div v-for="(o,i) in recent" :key="i" class="ro-item">
        <span class="ro-queue">A{{ o.queue }}</span>
        <span class="ro-name">{{ o.name }}</span>
        <span class="ro-time">{{ o.time }}</span>
        <span :class="['ro-status', o.status === '备餐中' ? 'preparing' : 'done']">{{ o.status }}</span>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
const weekData = [
  { day:'一',val:28,pct:60 },{ day:'二',val:32,pct:70 },{ day:'三',val:25,pct:55 },
  { day:'四',val:38,pct:80 },{ day:'五',val:45,pct:95 },{ day:'六',val:35,pct:75 },{ day:'日',val:30,pct:65 }
]
const recent = [
  { queue:34, name:'蛋炒饭×2', status:'备餐中', time:'12:05' },
  { queue:33, name:'牛肉炒饭×1', status:'已完成', time:'11:30' },
  { queue:32, name:'可乐×3', status:'已完成', time:'11:15' }
]
</script>
<style scoped>
.stats-grid { display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-bottom:20px; }
.stat-card { background:white;border-radius:12px;padding:16px;text-align:center;box-shadow:0 1px 3px rgba(0,0,0,.04); }
.sc-icon { font-size:24px; } .sc-val { font-size:22px;font-weight:700;color:#FF6B35;margin-top:4px; } .sc-lbl { font-size:12px;color:#999;margin-top:2px; }
.section-card { background:white;border-radius:12px;padding:16px;box-shadow:0 1px 3px rgba(0,0,0,.04);margin-bottom:16px; }
.section-card h3 { font-size:15px;font-weight:600;margin-bottom:12px; }
.row { display:flex;justify-content:space-between;padding:8px 0;border-bottom:1px solid #f5f5f5;font-size:14px; }
.row.total { border-top:2px solid #eee;font-weight:600;padding-top:10px;margin-top:4px; }
.green { color:#2ED573; } .orange { color:#FFA502; } .red { color:#FF4757; }
.bar-chart { display:flex;justify-content:space-between;align-items:flex-end;height:80px;padding:0 4px; }
.bar-item { display:flex;flex-direction:column;align-items:center;gap:4px;width:12%; }
.bar-fill { width:100%;max-width:24px;background:linear-gradient(180deg,#FF6B35,#FFB347);border-radius:4px 4px 2px 2px;min-height:6px; }
.bar-day { font-size:10px;color:#999; }
.ro-item { display:flex;align-items:center;gap:12px;padding:10px 0;border-bottom:1px solid #f5f5f5;font-size:14px; }
.ro-queue { background:#FF6B35;color:white;padding:2px 8px;border-radius:4px;font-weight:700;font-size:12px; }
.ro-name { flex:1; } .ro-time { font-size:12px;color:#999; }
.ro-status { font-size:11px;padding:2px 10px;border-radius:6px; }
.ro-status.preparing { background:#FFF8E1;color:#FFA000; }
.ro-status.done { background:#E8F8E8;color:#2ED573; }
</style>
