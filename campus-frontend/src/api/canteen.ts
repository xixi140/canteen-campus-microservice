import request from './request'

/** 获取所有食堂列表 */
export const getCanteens = () => request.get('/canteen/list')

/** 获取食堂详情 */
export const getCanteenDetail = (id: number) => request.get(`/canteen/${id}`)

/** 获取档口列表 */
export const getStalls = (canteenId: number) =>
  request.get(`/canteen/${canteenId}/stalls`)

/** 获取食堂菜品列表（支持按餐次筛选） */
export const getCanteenDishes = (params: {
  canteenId?: number
  stallId?: number
  mealType?: string
}) => request.get('/canteen/dishes', { params })
