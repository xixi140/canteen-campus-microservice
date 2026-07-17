<template>
  <div>
    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px">
      <div style="display:flex;gap:8px">
        <el-button type="primary" round @click="showAddCanteen = true">+ 新增食堂</el-button>
        <el-button round @click="showAddStall = true" v-if="canteens.length">+ 新增档口</el-button>
      </div>
    </div>

    <div v-for="c in canteens" :key="c.id" class="cs">
      <div class="cs-hd">
        <div class="cs-hd-left">
          <h3>{{ c.name }}</h3>
          <span class="cs-loc">{{ c.location }}</span>
        </div>
        <div class="cs-hd-right">
          <el-tag>{{ c.stallCount }}个档口</el-tag>
          <el-button text type="primary" size="small" @click="editCanteen(c)">编辑</el-button>
          <el-button text type="danger" size="small" @click="delCanteen(c)">删除</el-button>
        </div>
      </div>
      <div class="cs-stalls">
        <div v-for="s in c.stalls" :key="s.id" class="stall-row">
          <div class="stall-info">
            <span class="stall-name">{{ s.name }}</span>
            <span class="stall-cat">{{ s.category }}</span>
          </div>
          <div class="stall-acts">
            <el-button text size="small" @click="editStall(s)">编辑</el-button>
            <el-button text size="small" type="primary" @click="addDish(s)">菜品</el-button>
            <el-button text size="small" type="danger">删除</el-button>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="showAddCanteen" :title="editCanteenMode ? '编辑食堂' : '新增食堂'" width="90%">
      <el-form label-position="top">
        <el-form-item label="食堂名称"><el-input v-model="canteenForm.name" size="large" /></el-form-item>
        <el-form-item label="位置"><el-input v-model="canteenForm.location" size="large" /></el-form-item>
        <el-form-item label="营业时间"><el-input v-model="canteenForm.openTime" placeholder="例：06:30-13:00 / 16:30-19:30" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddCanteen = false">取消</el-button>
        <el-button type="primary" @click="saveCanteen">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const showAddCanteen = ref(false)
const editCanteenMode = ref(false)
const canteenForm = reactive({ name:'', location:'', openTime:'06:30-13:00 / 16:30-19:30' })

const canteens = ref([
  { id:1,name:'一食堂',location:'校园东区',stallCount:8,openTime:'06:30-13:00 / 16:30-19:30',
    stalls:[
      { id:1,name:'1号窗口·自选快餐',category:'炒菜' },
      { id:2,name:'2号窗口·面食坊',category:'面食' },
      { id:3,name:'3号窗口·早餐小食',category:'早餐' }
    ]},
  { id:2,name:'二食堂',location:'校园西区',stallCount:6,openTime:'06:30-13:00 / 16:30-19:30',
    stalls:[
      { id:4,name:'1号窗口·精品小炒',category:'炒菜' },
      { id:5,name:'2号窗口·云南味道',category:'米线' }
    ]}
])

function saveCanteen() {
  if (!canteenForm.name) { ElMessage.warning('请输入食堂名称'); return }
  canteens.value.push({
    id:Date.now(),...canteenForm,stallCount:0,stalls:[]
  })
  showAddCanteen.value = false
  canteenForm.name = ''; canteenForm.location = ''
  ElMessage.success('新增成功')
}
function editCanteen(c:any) { ElMessage.info('编辑功能开发中') }
function delCanteen(c:any) {
  ElMessageBox.confirm(`确定删除「${c.name}」？`).then(()=>{
    canteens.value = canteens.value.filter(x=>x.id!==c.id)
    ElMessage.success('已删除')
  }).catch(()=>{})
}
function editStall(s:any) { ElMessage.info('编辑档口') }
function addDish(s:any) { ElMessage.info('为「'+s.name+'」添加菜品') }
</script>
<style scoped>
.cs { margin-bottom:16px;border-radius:12px;overflow:hidden;box-shadow:0 1px 3px rgba(0,0,0,0.04); }
.cs-hd { background:linear-gradient(135deg,#FFF0E8,#FFE8CC);padding:14px 16px;display:flex;justify-content:space-between;align-items:center; }
.cs-hd-left { display:flex;align-items:center;gap:8px; }
.cs-hd-left h3 { font-size:16px;font-weight:600; }
.cs-loc { font-size:12px;color:#666; }
.cs-hd-right { display:flex;align-items:center;gap:8px; }
.cs-stalls { }
.stall-row { display:flex;justify-content:space-between;align-items:center;padding:10px 16px;background:white;border-bottom:1px solid #f5f5f5; }
.stall-info { display:flex;gap:8px;align-items:center; }
.stall-name { font-size:14px; }
.stall-cat { font-size:11px;padding:2px 8px;border-radius:6px;background:#f5f5f5;color:#666; }
.stall-acts { display:flex;gap:4px; }
</style>
