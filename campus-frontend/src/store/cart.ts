import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

export interface CartItem {
  id: number
  name: string
  emoji: string
  price: number
  quantity: number
  shopId?: number
  shopName?: string
}

export const useCartStore = defineStore('cart', () => {
  const items = ref<CartItem[]>([])

  const totalCount = computed(() => items.value.reduce((s, i) => s + i.quantity, 0))
  const totalPrice = computed(() =>
    items.value.reduce((s, i) => s + i.price * i.quantity, 0).toFixed(2)
  )

  function addItem(item: CartItem) {
    const exist = items.value.find(i => i.id === item.id)
    if (exist) { exist.quantity++ }
    else { items.value.push({ ...item, quantity: 1 }) }
    ElMessage.success(`已加入：${item.name}`)
  }

  function changeQuantity(id: number, delta: number) {
    const item = items.value.find(i => i.id === id)
    if (!item) return
    item.quantity += delta
    if (item.quantity <= 0) items.value = items.value.filter(i => i.id !== id)
  }

  function clear() { items.value = [] }

  // 按shopId分组
  const groups = computed(() => {
    const map = new Map<number, CartItem[]>()
    items.value.forEach(i => {
      const key = i.shopId || 0
      if (!map.has(key)) map.set(key, [])
      map.get(key)!.push(i)
    })
    return map
  })

  return { items, totalCount, totalPrice, groups, addItem, changeQuantity, clear }
})
