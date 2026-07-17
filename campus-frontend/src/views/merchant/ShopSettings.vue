<template>
  <div>
    <el-form label-position="top" @submit.prevent="saveShop">
      <div class="fs"><h4>基本信息</h4>
        <el-form-item label="店铺名称"><el-input v-model="shop.name" size="large" /></el-form-item>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="营业开始"><el-time-select v-model="shop.openStart" :start="'06:00'" step="00:30" end="'12:00'" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="营业结束"><el-time-select v-model="shop.openEnd" :start="'12:00'" step="00:30" end="'23:00'" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="店铺公告"><el-input v-model="shop.notice" type="textarea" :rows="2" /></el-form-item>
      </div>
      <div class="fs"><h4>营业状态</h4>
        <div style="display:flex;align-items:center;gap:12px">
          <el-switch v-model="shop.isOpen" :active-value="1" :inactive-value="0" size="large" />
          <span>{{ shop.isOpen ? '营业中' : '已打烊' }}</span>
        </div>
      </div>
      <el-button native-type="submit" type="primary" size="large" round style="width:100%;margin-top:16px">保存设置</el-button>
    </el-form>
  </div>
</template>
<script setup lang="ts">
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'

const shop = reactive({ name:'老王炒饭', openStart:'09:00', openEnd:'21:00', notice:'🔥 新品尝鲜：咖喱鸡肉饭限时特惠16元！', isOpen:1 })
function saveShop() { ElMessage.success('店铺设置已保存！') }
</script>
<style scoped>
.fs { background:white;border-radius:12px;padding:16px;margin-bottom:12px; }
.fs h4 { font-size:15px;font-weight:600;margin-bottom:12px;padding-bottom:8px;border-bottom:1px solid #f5f5f5; }
</style>
