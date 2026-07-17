<template>
  <div class="home">
    <div class="meal-banner" :style="{ background: bannerStyle }">
      <div><h2>{{ mealGreeting }}</h2><p>{{ mealSlogan }}</p></div>
      <div class="banner-emoji">{{ mealEmoji }}</div>
    </div>
    <div class="meal-bar">
      <button v-for="m in meals" :key="m.key" :class="['mb',{active:currentMeal===m.key}]" @click="switchMeal(m.key)">{{ m.icon }} {{ m.name }}</button>
    </div>
    <div class="quick-grid">
      <router-link to="/canteens" class="q-card"><span>🏛️</span><span>食堂</span><small>查看菜品</small></router-link>
      <router-link to="/merchant-shops" class="q-card"><span>🏪</span><span>商户</span><small>在线点餐</small></router-link>
      <router-link to="/orders" class="q-card"><span>📋</span><span>订单</span><small>{{ orderCount }}笔</small></router-link>
    </div>
    <section class="sec">
      <div class="sec-hd"><h3>🏫 食堂拥挤度</h3><span class="upd">每30秒更新</span></div>
      <div class="cg-grid">
        <div v-for="c in congestion" :key="c.id" class="cg-card" :class="{hot:c.level==='拥挤'||c.level==='爆满'}" @click="$router.push(`/canteen/${c.id}`)">
          <div class="cg-top"><b>{{ c.name }}</b><span class="cg-emoji">{{ c.emoji }}</span></div>
          <div class="cg-lv">{{ c.level }}</div>
          <div class="cg-info">⏳ 排队约{{ c.waitMinutes }}分钟</div>
          <div v-if="c.level==='拥挤'||c.level==='爆满'" class="cg-tip">💡 建议去{{ c.id===1?'二食堂':'一食堂' }}</div>
        </div>
      </div>
    </section>
    <section class="sec">
      <div class="sec-hd"><h3>🔥 {{ dishTitle }}</h3></div>
      <div class="food-row">
        <div v-for="d in dishes" :key="d.id" class="f-card" @click="goDish(d)">
          <div class="f-img" :style="{background:d.color||'#f5f5f5'}"><span>{{ d.emoji }}</span></div>
          <div class="f-bd"><h4>{{ d.name }}</h4><span class="fp">¥{{ d.price }}</span></div>
        </div>
      </div>
    </section>
    <section class="sec">
      <div class="sec-hd"><h3>🥗 今日营养</h3></div>
      <div class="nu-card">
        <div class="nu-it"><span class="nu-v">{{ nutrition.cal }}</span><span>卡路里</span></div>
        <div class="nu-it"><span class="nu-v">{{ nutrition.protein }}g</span><span>蛋白质</span></div>
        <div class="nu-it"><span class="nu-v">{{ nutrition.carbs }}g</span><span>碳水</span></div>
        <div class="nu-it"><span class="nu-v">{{ nutrition.fat }}g</span><span>脂肪</span></div>
      </div>
    </section>
  </div>
</template>
<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCongestion, getRecommend, getOrders } from '@/api'
const router = useRouter()
const currentMeal = ref('LUNCH')
const orderCount = ref(0)
const meals = [
  {key:'BREAKFAST',name:'早餐',icon:'🌅',bg:'linear-gradient(135deg,#FF9A9E,#FAD0C4)',emoji:'🌅',greeting:'早安！',slogan:'煎饼果子6元'},
  {key:'LUNCH',name:'午餐',icon:'☀️',bg:'linear-gradient(135deg,#FF6B35,#FFA502)',emoji:'🍱',greeting:'中午好！',slogan:'红烧肉8元'},
  {key:'DINNER',name:'晚餐',icon:'🌙',bg:'linear-gradient(135deg,#667eea,#764ba2)',emoji:'🌙',greeting:'辛苦一天',slogan:'回锅肉9元'}
]
const bannerStyle=ref(meals[1].bg),mealEmoji=ref(meals[1].emoji),mealGreeting=ref(meals[1].greeting),mealSlogan=ref(meals[1].slogan)
function switchMeal(k:string){const m=meals.find(x=>x.key===k)!;bannerStyle.value=m.bg;mealEmoji.value=m.emoji;mealGreeting.value=m.greeting;mealSlogan.value=m.slogan}
const dishTitle=computed(()=>['今日早餐推荐','今日午餐推荐','今日晚餐推荐'][['BREAKFAST','LUNCH','DINNER'].indexOf(currentMeal.value)])

// ★ 从API加载拥挤度
const congestion=ref([{id:1,name:'一食堂',level:'加载中...',emoji:'⏳',waitMinutes:0},{id:2,name:'二食堂',level:'加载中...',emoji:'⏳',waitMinutes:0}])
onMounted(async()=>{
  try{const r=await getCongestion() as any;if(r.data)congestion.value=r.data}catch{}
  try{const r=await getRecommend(currentMeal.value)as any;if(r.data&&r.data.length)dishes.value=r.data}catch{}
  try{const r=await getOrders()as any;if(r.data)orderCount.value=r.data.length}catch{}
})

const dishes=ref([{id:11,name:'蛋炒饭',emoji:'🍚',price:10,sourceType:'MERCHANT',color:'#FFF8E1'},{id:101,name:'红烧肉',emoji:'🥩',price:8,sourceType:'CANTEEN',color:'#FFEBEE'},{id:401,name:'宫保鸡丁',emoji:'🍗',price:10,sourceType:'CANTEEN',color:'#FFEBEE'},{id:13,name:'牛肉炒饭',emoji:'🥩',price:18,sourceType:'MERCHANT',color:'#FFF0E8'}])
const nutrition=ref({cal:1250,protein:48,carbs:152,fat:38})
function goDish(d:any){d.sourceType==='CANTEEN'?router.push('/canteens'):router.push('/shop/1')}
</script>
<style scoped>
.meal-banner{border-radius:0;padding:20px 24px;color:white;display:flex;justify-content:space-between;align-items:center}
.meal-banner h2{font-size:18px;font-weight:700}.meal-banner p{font-size:12px;opacity:.9;margin-top:4px}.banner-emoji{font-size:40px}
.meal-bar{display:flex;background:white}.mb{flex:1;padding:12px;border:none;border-bottom:2px solid transparent;background:white;cursor:pointer;font-size:14px;color:#666}.mb.active{color:#FF6B35;border-bottom-color:#FF6B35;font-weight:600}
.quick-grid{display:flex;gap:10px;padding:12px 16px}.q-card{flex:1;display:flex;flex-direction:column;align-items:center;gap:4px;padding:14px 8px;background:white;border-radius:12px;text-decoration:none;color:var(--text);box-shadow:0 1px 3px rgba(0,0,0,.04)}.q-card small{font-size:10px;color:#999}
.sec{margin-bottom:20px;padding:0 16px}.sec-hd{display:flex;justify-content:space-between;align-items:center;margin-bottom:10px}.sec-hd h3{font-size:16px;font-weight:700}.upd{font-size:11px;color:#999}
.cg-grid{display:flex;gap:12px}.cg-card{flex:1;background:white;border-radius:14px;padding:16px;cursor:pointer;box-shadow:0 1px 3px rgba(0,0,0,.04)}.cg-card.hot{border:2px solid #FF6B35;background:#FFF0E8}.cg-top{display:flex;justify-content:space-between;margin-bottom:6px}.cg-emoji{font-size:28px}.cg-lv{font-size:18px;font-weight:700;margin-bottom:4px}.cg-info{font-size:12px;color:#666}.cg-tip{font-size:11px;color:#FF6B35;margin-top:4px}
.food-row{display:flex;gap:10px;overflow-x:auto}.f-card{min-width:120px;background:white;border-radius:12px;overflow:hidden;cursor:pointer}.f-img{height:80px;display:flex;align-items:center;justify-content:center;font-size:28px}.f-bd{padding:8px 10px}.f-bd h4{font-size:13px;font-weight:500}.fp{font-size:14px;font-weight:700;color:#FF4757}
.nu-card{display:flex;background:linear-gradient(135deg,#FFF0E8,#FFE8CC);border-radius:14px;padding:16px}.nu-it{flex:1;text-align:center;border-right:1px solid rgba(0,0,0,.06)}.nu-it:last-child{border-right:none}.nu-v{display:block;font-size:18px;font-weight:700;color:#FF6B35}.nu-it span:last-child{font-size:11px;color:#666;margin-top:2px;display:block}
</style>
