<template>
  <template v-if="$route.path === '/login'">
    <router-view :key="'login'" />
  </template>
  <template v-else-if="role === 'STUDENT'">
    <student-layout :key="'s-'+role"><router-view /></student-layout>
  </template>
  <template v-else-if="role === 'MERCHANT'">
    <merchant-layout :key="'m-'+role" :title="pageTitle"><router-view /></merchant-layout>
  </template>
  <template v-else-if="role === 'ADMIN'">
    <admin-layout :key="'a-'+role" :title="pageTitle"><router-view /></admin-layout>
  </template>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import StudentLayout from '@/layouts/StudentLayout.vue'
import MerchantLayout from '@/layouts/MerchantLayout.vue'
import AdminLayout from '@/layouts/AdminLayout.vue'

const route = useRoute()
const role = computed(() => localStorage.getItem('role') || '')
const pageTitle = computed(() => (route.meta?.title as string) || '')
</script>

<style>
:root { --primary:#FF6B35; --bg:#f5f5f5; --text:#2D3436; }
* { margin:0; padding:0; box-sizing:border-box; }
body { font-family:-apple-system,BlinkMacSystemFont,'Segoe UI','PingFang SC','Microsoft YaHei',sans-serif; background:var(--bg); color:var(--text); }
</style>
