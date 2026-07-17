<template>
  <div>
    <div class="stats-bar">
      <div class="stat"><strong>{{ merchants.length }}</strong><span>总商户</span></div>
      <div class="stat"><strong>{{ activeCount }}</strong><span>营业中</span></div>
      <div class="stat"><strong>{{ inactiveCount }}</strong><span>已停用</span></div>
    </div>
    <div class="search-bar">
      <el-input v-model="search" placeholder="搜索商户名称..." prefix-icon="Search" clearable />
    </div>
    <div v-for="m in filteredMerchants" :key="m.id" class="mc">
      <div class="mc-top">
        <span class="mc-avatar" :style="{background:m.color}">{{ m.initial }}</span>
        <div class="mc-info">
          <h4>{{ m.name }}</h4>
          <div class="mc-tags">
            <span :class="['mc-status',m.status?'on':'off']">{{ m.status ? '营业中' : '已停用' }}</span>
            <span class="mc-dish">{{ m.dishCount }}道菜品</span>
          </div>
        </div>
        <el-switch v-model="m.status" :active-value="1" :inactive-value="0" />
      </div>
      <div class="mc-meta">
        <span>📞 {{ m.contact }}</span>
        <span>⭐ {{ m.rating }}</span>
        <span>📅 入驻 {{ m.joinDate }}</span>
      </div>
      <div class="mc-acts">
        <el-button size="small" round @click="viewDetail(m)">查看详情</el-button>
        <el-button size="small" round type="danger" plain @click="delMerchant(m)">移除</el-button>
      </div>
    </div>
    <el-empty v-if="filteredMerchants.length === 0" description="无匹配商户" />
  </div>
</template>
<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const search = ref('')
const merchants = ref([
  { id:1,name:'老王炒饭',initial:'老',color:'#FFF0E8',status:1,contact:'138****0001',dishCount:12,rating:4.8,joinDate:'2026-03-01' },
  { id:2,name:'李记麻辣烫',initial:'李',color:'#FFE8E8',status:1,contact:'138****0002',dishCount:8,rating:4.5,joinDate:'2026-03-15' },
  { id:3,name:'好滋味面馆',initial:'好',color:'#E8F8E8',status:0,contact:'138****0003',dishCount:10,rating:4.6,joinDate:'2026-04-01' },
])

const activeCount = computed(() => merchants.value.filter(m=>m.status).length)
const inactiveCount = computed(() => merchants.value.filter(m=>!m.status).length)
const filteredMerchants = computed(() =>
  merchants.value.filter(m => m.name.includes(search.value))
)

function viewDetail(m:any) { ElMessage.info(`查看「${m.name}」详情`) }
function delMerchant(m:any) {
  ElMessageBox.confirm(`确定移除「${m.name}」？`).then(()=>{
    merchants.value = merchants.value.filter(x=>x.id!==m.id)
    ElMessage.success('已移除')
  }).catch(()=>{})
}
</script>
<style scoped>
.stats-bar { display:flex;gap:12px;margin-bottom:16px; }
.stat { flex:1;background:white;border-radius:10px;padding:12px;text-align:center; }
.stat strong { display:block;font-size:20px;color:#FF6B35; }
.stat span { font-size:12px;color:#999; }
.search-bar { margin-bottom:12px; }
.mc { background:white;border-radius:12px;padding:14px;margin-bottom:10px; }
.mc-top { display:flex;align-items:center;gap:12px;margin-bottom:8px; }
.mc-avatar { width:44px;height:44px;border-radius:12px;display:flex;align-items:center;justify-content:center;font-size:18px;font-weight:700;color:#FF6B35;flex-shrink:0; }
.mc-info { flex:1; }
.mc-info h4 { font-size:15px;font-weight:600; }
.mc-tags { display:flex;gap:6px;margin-top:4px; }
.mc-status { font-size:11px;padding:1px 8px;border-radius:6px; }
.mc-status.on { background:#E8F8E8;color:#2ED573; }
.mc-status.off { background:#f5f5f5;color:#999; }
.mc-dish { font-size:11px;color:#999; }
.mc-meta { display:flex;gap:16px;font-size:12px;color:#999;margin-left:56px;margin-bottom:8px; }
.mc-acts { margin-left:56px;display:flex;gap:8px; }
</style>
