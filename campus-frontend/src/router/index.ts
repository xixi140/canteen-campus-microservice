import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/login', name: 'Login', component: () => import('@/views/LoginView.vue') },

  // ===== 👤 学生端 =====
  { path: '/home', name: 'Home', component: () => import('@/views/student/HomeView.vue'), meta: { role: 'STUDENT', title: '校园美食' } },
  { path: '/canteens', name: 'CanteenList', component: () => import('@/views/student/CanteenListView.vue'), meta: { role: 'STUDENT', title: '全部食堂' } },
  { path: '/canteen/:id', name: 'CanteenDetail', component: () => import('@/views/student/CanteenDetailView.vue'), meta: { role: 'STUDENT', title: '食堂详情' } },
  { path: '/merchant-shops', name: 'MerchantList', component: () => import('@/views/student/MerchantListView.vue'), meta: { role: 'STUDENT', title: '商户美食' } },
  { path: '/shop/:id', name: 'ShopDetail', component: () => import('@/views/student/ShopDetailView.vue'), meta: { role: 'STUDENT', title: '店铺详情' } },
  { path: '/cart', name: 'Cart', component: () => import('@/views/student/CartView.vue'), meta: { role: 'STUDENT', title: '购物车' } },
  { path: '/orders', name: 'Orders', component: () => import('@/views/student/OrderView.vue'), meta: { role: 'STUDENT', title: '我的订单' } },
  { path: '/order/:id', name: 'OrderDetail', component: () => import('@/views/student/OrderDetailView.vue'), meta: { role: 'STUDENT', title: '订单详情' } },
  { path: '/profile', name: 'Profile', component: () => import('@/views/student/ProfileView.vue'), meta: { role: 'STUDENT', title: '个人中心' } },
  { path: '/notices', name: 'Notices', component: () => import('@/views/student/NoticeView.vue'), meta: { role: 'STUDENT', title: '消息中心' } },

  // ===== 🏪 商户端 =====
  { path: '/merchant-dashboard', name: 'MerchantDashboard', component: () => import('@/views/merchant/DashboardView.vue'), meta: { role: 'MERCHANT', title: '营业概况' } },
  { path: '/merchant-dishes', name: 'MerchantDishes', component: () => import('@/views/merchant/DishManage.vue'), meta: { role: 'MERCHANT', title: '菜品管理' } },
  { path: '/merchant-order', name: 'MerchantOrders', component: () => import('@/views/merchant/OrderManage.vue'), meta: { role: 'MERCHANT', title: '订单管理' } },
  { path: '/merchant-settings', name: 'MerchantSettings', component: () => import('@/views/merchant/ShopSettings.vue'), meta: { role: 'MERCHANT', title: '店铺设置' } },

  // ===== 🔧 管理端 =====
  { path: '/admin-dashboard', name: 'AdminDashboard', component: () => import('@/views/admin/DashboardView.vue'), meta: { role: 'ADMIN', title: '数据看板' } },
  { path: '/admin-canteens', name: 'AdminCanteens', component: () => import('@/views/admin/CanteenManage.vue'), meta: { role: 'ADMIN', title: '食堂管理' } },
  { path: '/admin-merchants', name: 'AdminMerchants', component: () => import('@/views/admin/MerchantManage.vue'), meta: { role: 'ADMIN', title: '商户管理' } },
  { path: '/admin-order', name: 'AdminOrders', component: () => import('@/views/admin/OrderManage.vue'), meta: { role: 'ADMIN', title: '订单管理' } },
]

const router = createRouter({ history: createWebHistory(), routes })

const roleHome: Record<string, string> = {
  STUDENT: '/home', MERCHANT: '/merchant-dashboard', ADMIN: '/admin-dashboard'
}

router.beforeEach((to, _from, next) => {
  if (to.path === '/') { localStorage.clear(); next('/login'); return }
  if (to.path === '/login') { next(); return }

  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')
  if (!token || !role) { localStorage.clear(); next('/login'); return }

  // 检测路径前缀
  const path = to.path
  const isStudentPath = path.startsWith('/home') || path.startsWith('/canteen') || path.startsWith('/merchant-shop') || path.startsWith('/shop/') || path.startsWith('/cart') || path.startsWith('/order') || path.startsWith('/orders') || path.startsWith('/profile') || path.startsWith('/notices')
  const isMerchantPath = path.startsWith('/merchant-dash') || path.startsWith('/merchant-dish') || path.startsWith('/merchant-order') || path.startsWith('/merchant-setting')
  const isAdminPath = path.startsWith('/admin-')

  if (isStudentPath && role !== 'STUDENT') { next(roleHome[role] || '/login'); return }
  if (isMerchantPath && role !== 'MERCHANT') { next(roleHome[role] || '/login'); return }
  if (isAdminPath && role !== 'ADMIN') { next(roleHome[role] || '/login'); return }

  document.title = (to.meta?.title as string) || '校园美食'
  next()
})

export default router
