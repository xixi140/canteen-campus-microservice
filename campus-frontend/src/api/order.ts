import request from './request'

/** 创建订单 */
export const createOrder = (data: {
  userId: number
  sourceType: string   // CANTEEN 或 MERCHANT
  dishId: number
  quantity: number
  requestId: string    // 前端生成UUID，用于幂等性
}) => request.post('/order/create', data)

/** 查询订单详情 */
export const getOrderDetail = (id: number) => request.get(`/order/${id}`)

/** 查询用户订单列表 */
export const getUserOrders = (userId: number) => request.get(`/order/user/${userId}`)
