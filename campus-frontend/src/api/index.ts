import request from './request'

// ===== 认证 =====
export const login = (data: { username: string; password: string }) =>
  request.post('/user/login', data)

export const getUserInfo = (id: number) => request.get(`/user/${id}`)

// ===== 食堂 =====
export const getCanteens = () => request.get('/canteen/list')
export const getCanteenDetail = (id: number) => request.get(`/canteen/${id}`)
export const getStalls = (canteenId: number) => request.get(`/canteen/${canteenId}/stalls`)
export const getCanteenDishes = (params: any) => request.get('/canteen/dishes', { params })
export const getCongestion = () => request.get('/canteen/congestion')
export const getRecommend = (mealType?: string) => request.get('/canteen/recommend', { params: { mealType } })
export const getNutrition = (id: number) => request.get(`/canteen/dishes/${id}/nutrition`)
export const batchNutrition = (dishIds: number[]) => request.post('/canteen/nutrition/batch', dishIds)

// ===== 商户 =====
export const getMerchants = () => request.get('/merchant/list')
export const getShopDetail = (id: number) => request.get(`/merchant/shop/${id}`)
export const getShopDishes = (shopId: number, category?: string) =>
  request.get('/merchant/dishes', { params: { shopId, category } })

// ===== 订单 =====
export const createOrderAPI = (data: any) => request.post('/order/create', data)
export const getOrders = (params?: any) => request.get('/order/list', { params })
export const getOrderDetail = (id: number) => request.get(`/order/${id}`)
export const cancelOrderAPI = (id: number) => request.put(`/order/${id}/cancel`)
export const confirmPickupAPI = (id: number) => request.put(`/order/${id}/pickup`)

// ===== 支付 =====
export const createPayment = (data: any) => request.post('/payment/create', data)

// ===== 通知 =====
export const getNotices = (userId: number) => request.get('/notice/list', { params: { userId } })
export const readNotice = (id: number) => request.put(`/notice/${id}/read`)

// ===== 用户订单历史 =====
export const getUserOrders = (userId: number) => request.get(`/order/user/${userId}`)
