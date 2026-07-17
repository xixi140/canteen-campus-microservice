<template>
  <div>
    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px">
      <el-button type="primary" round @click="showDialog=true;form={name:'',emoji:'🍚',price:10,stock:30,category:'炒饭'}">+ 新增菜品</el-button>
    </div>
    <div class="stats-bar">
      <div class="stat"><strong>{{ dishes.length }}</strong><span>全部</span></div>
      <div class="stat"><strong>{{ onCount }}</strong><span>已上架</span></div>
      <div class="stat"><strong>{{ offCount }}</strong><span>已下架</span></div>
    </div>
    <div class="list">
      <div v-for="d in dishes" :key="d.id" class="item">
        <span class="emoji">{{ d.emoji }}</span>
        <div class="info"><h4>{{ d.name }}</h4><span>¥{{ d.price }} · 月售{{ d.sales }}</span></div>
        <el-switch v-model="d.status" :active-value="1" :inactive-value="0" />
        <el-button text size="small" @click="editDish(d)">编辑</el-button>
      </div>
    </div>

    <el-dialog v-model="showDialog" :title="editing?'编辑菜品':'新增菜品'" width="90%">
      <el-form label-position="top">
        <el-form-item label="菜品名称"><el-input v-model="form.name" /></el-form-item>
        <el-row :gutter="12">
          <el-col :span="12"><el-form-item label="价格"><el-input-number v-model="form.price" :min="1" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="库存"><el-input-number v-model="form.stock" :min="0" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="分类">
          <el-select v-model="form.category" style="width:100%">
            <el-option label="炒饭" value="炒饭" /><el-option label="盖饭" value="盖饭" />
            <el-option label="面食" value="面食" /><el-option label="小吃" value="小吃" /><el-option label="饮品" value="饮品" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述"><el-input v-model="form.desc" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog=false">取消</el-button>
        <el-button type="primary" @click="saveDish">{{ editing?'保存':'上架' }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, computed, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const showDialog = ref(false)
const editing = ref(false)
const form = reactive<any>({ name:'', emoji:'🍚', price:10, stock:30, category:'炒饭', desc:'' })

const dishes = ref([
  { id:1,name:'蛋炒饭',emoji:'🍚',price:10,stock:30,status:1,sales:1286,desc:'',category:'炒饭' },
  { id:2,name:'扬州炒饭',emoji:'🍚',price:15,stock:25,status:1,sales:956,desc:'',category:'炒饭' },
  { id:3,name:'牛肉炒饭',emoji:'🥩',price:18,stock:20,status:1,sales:823,desc:'',category:'炒饭' },
  { id:4,name:'老干妈炒饭',emoji:'🌶️',price:12,stock:35,status:0,sales:534,desc:'',category:'炒饭' }
])
const onCount = computed(() => dishes.value.filter(d=>d.status).length)
const offCount = computed(() => dishes.value.filter(d=>!d.status).length)

function editDish(d:any) { Object.assign(form, d); editing.value = true; showDialog.value = true }
function saveDish() {
  if (editing.value) { Object.assign(dishes.value.find(d=>d.id===form.id)||{}, form); ElMessage.success('已更新') }
  else { dishes.value.unshift({ id:Date.now(), ...form, status:1, sales:0 }); ElMessage.success('已上架') }
  showDialog.value = false; editing.value = false
}
</script>
<style scoped>
.stats-bar { display:flex;gap:12px;margin-bottom:16px; }
.stat { flex:1;background:white;border-radius:10px;padding:12px;text-align:center; }
.stat strong { display:block;font-size:20px;color:#FF6B35; }
.stat span { font-size:12px;color:#999; }
.list { display:flex;flex-direction:column;gap:8px; }
.item { display:flex;align-items:center;gap:12px;padding:12px;background:white;border-radius:12px; }
.emoji { font-size:28px; }
.info { flex:1; }
.info h4 { font-size:14px;font-weight:600; }
.info span { font-size:12px;color:#999; }
</style>
