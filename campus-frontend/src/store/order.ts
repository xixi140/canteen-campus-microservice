import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getOrders as getOrdersAPI } from '@/api'

export interface OrderItem {
  id: number
  queueNo: number
  pickupName: string
  dishName: string
  emoji: string
  quantity: number
  totalAmount: string
  status: string
  createTime: string
  shopName: string
  shopId: number
  sourceType: string
}

export const useOrderStore = defineStore('order', () => {
  const orders = ref<OrderItem[]>([])

  function addOrder(order: OrderItem) { orders.value.unshift(order) }

  function updateStatus(id: number, status: string) {
    const o = orders.value.find(o => o.id === id)
    if (o) o.status = status
  }

  async function fetchOrders(userId?: number, status?: string) {
    try {
      const res = await getOrdersAPI({ userId, status }) as any
      orders.value = (res.data || []).map((o: any) => ({
        id: o.id,
        queueNo: 0,
        pickupName: '',
        dishName: o.dishName || '',
        emoji: '🍽️',
        quantity: o.quantity || 1,
        totalAmount: o.totalAmount || '0',
        status: o.status || 'PENDING_PAYMENT',
        createTime: o.createTime || '',
        shopName: '',
        shopId: 0,
        sourceType: o.sourceType || '',
      }))
      return orders.value
    } catch { return [] }
  }

  return { orders, addOrder, updateStatus, fetchOrders }
})
