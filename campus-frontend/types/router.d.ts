import 'vue-router'

declare module 'vue-router' {
  interface RouteMeta {
    title?: string
    role?: string
    noAuth?: boolean
  }
}
