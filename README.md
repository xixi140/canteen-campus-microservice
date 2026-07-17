# 智慧校园餐饮服务平台 (Campus Canteen Cloud)

> 基于 **Spring Boot 3.5.15 + Spring Cloud 2025.0.3 + Spring Cloud Alibaba 2025.0.0.0** 的微服务架构全栈项目

---

## 项目简介

智慧校园餐饮服务平台是一个面向高校场景的微服务全栈项目，覆盖学生、商户、管理员三类角色的完整业务闭环：**浏览食堂/商户 → 加入购物车 → 在线下单 → 支付 → 实时通知 → 取餐履约**。

后端采用 **Spring Cloud Alibaba** 微服务技术栈，包含 6 个业务微服务 + 1 个 API 网关；前端采用 **Vue 3 + Element Plus** 构建三端独立门户。通过 Docker Compose 一键编排运行。

---

## 技术栈

| 层级 | 技术选型 | 版本 |
|------|---------|------|
| 开发语言 | Java | 17 (LTS) |
| 微服务框架 | Spring Boot / Spring Cloud | 3.5.15 / 2025.0.3 |
| 微服务治理 | Spring Cloud Alibaba | 2025.0.0.0 |
| 注册配置中心 | Nacos | 2.2.3 |
| API 网关 | Spring Cloud Gateway | 4.x (WebFlux) |
| 远程调用 | OpenFeign + LoadBalancer | 4.x |
| 熔断降级 | Sentinel | 1.8.9 |
| 分布式事务 | Seata AT 模式 | 2.0.0 |
| ORM 框架 | MyBatis-Plus | 3.5.7 |
| 数据库连接池 | Druid | 1.2.23 |
| 数据库 / 缓存 | MySQL / Redis | 8.0 / 7.x |
| 容器化 | Docker + Docker Compose | v3.8 |
| 前端框架 | Vue 3 + Element Plus | 3.4+ / 2.4+ |
| 状态管理 / 构建 | Pinia / Vite | 2.x / 5.x |
| 实时推送 | WebSocket | 原生 API |

---

## 系统架构

```
┌──────────────────────────────────────────────────────────┐
│                   Vue 3 前端 (端口 5173)                   │
│        学生端(三端分离)  /  商户端  /  管理端               │
│        Axios → localhost:5173/api/* (Vite代理→8080)       │
└──────────────────────────┬───────────────────────────────┘
                           │ HTTP
                   ┌───────▼────────┐
                   │  API Gateway   │ 端口: 8080
                   │  (Gateway)      │ 路由 / 负载均衡 / 跨域
                   └───────┬────────┘
                           │
         ┌─────────────────┼────────────────────┐
         │                 │                    │
    ┌────▼────┐     ┌──────▼──────┐     ┌───────▼───────┐
    │  Nacos  │     │   Nacos     │     │   Sentinel    │
    │Registry │     │   Config    │     │   熔断规则     │
    └────┬────┘     └──────┬──────┘     └───────┬───────┘
         │                 │                    │
    ┌────┴─────────────────┴────────────────────┴────────┐
    │                   6 个业务微服务                      │
    │                                                     │
    │  user-service      (8081) → user_db    用户认证     │
    │  canteen-service   (8082) → canteen_db  食堂菜品     │
    │  merchant-service  (8083) → merchant_db 商户菜品     │
    │  order-service     (8084) → order_db    订单(核心)   │
    │  payment-service   (8085) → payment_db  支付         │
    │  notice-service    (8086) → notice_db   通知+WS      │
    └────────────────────────────────────────────────────┘
```

### 服务间调用拓扑

```
                         ┌──────────┐
                         │ Gateway   │
                         └────┬─────┘
                              │
                    ┌─────────▼─────────┐
                    │   order-service   │ ← 核心调度节点
                    │   (核心编排层)     │
                    └──┬──────┬──────┬──┘
                       │      │      │
              ┌────────▼┐ ┌───▼────┐ ┌▼────────┐
              │  user   │ │canteen │ │merchant │
              │:8081    │ │:8082   │ │:8083    │
              └─────────┘ └───┬────┘ └─────────┘
                              │ Redis (拥挤度)
              ┌─────────┐ ┌───┴────────┐
              │ payment │ │  notice    │
              │ :8085   │ │  :8086     │
              └─────────┘ └────────────┘
```

---

## 微服务职责

| 服务 | 角色 | 核心职责 | 数据库 | 依赖 |
|------|------|---------|--------|------|
| **user-service** | 能力提供者 | 用户注册/登录/JWT签发/身份校验 | user_db | 无 |
| **canteen-service** | 能力提供者 | 食堂/档口/官方菜品/拥挤度计算 | canteen_db | Redis |
| **merchant-service** | 能力提供者 | 商户入驻/店铺管理/菜品上架 | merchant_db | 无 |
| **order-service** | **核心调度者** | 订单全生命周期/跨服务编排/幂等 | order_db | 依赖4个下游 |
| **payment-service** | 能力提供者 | 支付单创建/状态管理（模拟支付） | payment_db | 无 |
| **notice-service** | 能力提供者 | 通知创建/已读管理/WebSocket推送 | notice_db | 无 |

---

## 核心业务流程

### 下单全链路（跨 5 个服务）

```
前端 → Gateway → order-service
  │
  ├─ ① 幂等性校验：SELECT by requestId（唯一索引）
  ├─ ② Feign → user-service：验证用户身份
  ├─ ③ Feign → canteen/merchant-service：查菜品 + 乐观锁扣库存
  ├─ ④ 本地事务 → 写入 order_db
  ├─ ⑤ Feign → payment-service：创建支付单
  └─ ⑥ RestTemplate → notice-service：异步发送通知
```

### 订单状态机

```
PENDING_PAYMENT → PAID → PREPARING → COMPLETED
       ↓
   CANCELLED（仅待支付可取消）
```

---

## 数据库设计

6 个微服务对应 6 个独立数据库，严格禁止跨库 JOIN，服务间数据交互通过 Feign API 完成。

| 数据库 | 服务 | 核心表 | 关键字段 |
|--------|------|--------|---------|
| user_db | user-service | user | username(UNIQUE), password(MD5), role |
| canteen_db | canteen-service | canteen, stall, canteen_dish | version(乐观锁), meal_type(餐次) |
| merchant_db | merchant-service | merchant, shop, merchant_dish | version(乐观锁), category(分类) |
| order_db | order-service | order | request_id(UNIQUE幂等键), status(状态机) |
| payment_db | payment-service | payment | request_id(UNIQUE幂等键), status |
| notice_db | notice-service | notice | user_id(INDEX), is_read |

---

## 快速启动

### 前置要求

- Java 17+
- Maven 3.9+
- Docker & Docker Compose
- Node.js 18+

### 1. 启动基础设施

```bash
docker compose up -d nacos mysql redis
```

### 2. 编译后端

```bash
mvn clean package -DskipTests -pl campus-common/common-core,campus-common/common-feign-api,campus-gateway,campus-user-service,campus-canteen-service,campus-merchant-service,campus-order-service,campus-payment-service,campus-notice-service -am
```

### 3. 启动后端服务

按顺序启动（推荐在 IDEA 中逐个运行）：

1. GatewayApplication（:8080）
2. UserApplication（:8081）
3. CanteenApplication（:8082）
4. MerchantApplication（:8083）
5. OrderApplication（:8084）
6. PaymentApplication（:8085）
7. NoticeApplication（:8086）

### 4. 启动前端

```bash
cd campus-frontend
npm install
npm run dev
```

访问 http://localhost:5173

### 5. 测试账号

| 角色 | 账号 | 密码 | 说明 |
|------|------|------|------|
| 学生 | student1 | 123456 | 浏览食堂/商户，在线下单 |
| 商户 | merchant1 | 123456 | 管理菜品，处理订单 |
| 管理员 | admin | 123456 | 管理食堂/商户/平台数据 |

---

## 项目结构

```
campus-canteen-cloud/
├── pom.xml                          # 父 POM（版本管理）
├── docker-compose.yml               # Docker 集群编排
├── sql/init.sql                     # 6 个数据库初始化 + Seata undo_log
├── docker-data/                     # Docker 持久化数据
│   └── seata/registry.conf          # Seata 注册配置
│
├── campus-common/                   # 公共模块
│   ├── common-core/                 # R<T> 响应体、BizException、JwtUtils
│   └── common-feign-api/            # Feign 接口契约 + DTO（4个VO）
│
├── campus-gateway/                  # API 网关（Spring Cloud Gateway）
├── campus-user-service/             # 用户服务
├── campus-canteen-service/          # 食堂菜品服务
├── campus-merchant-service/         # 商户菜品服务
├── campus-order-service/            # 订单服务（核心编排层）
├── campus-payment-service/          # 支付服务
├── campus-notice-service/           # 通知 + WebSocket 服务
│
└── campus-frontend/                 # Vue 3 前端
    ├── src/
    │   ├── api/                     # Axios 统一请求层
    │   ├── layouts/                 # 三端独立布局
    │   ├── router/                  # 路由守卫（角色控制）
    │   ├── store/                   # Pinia 状态管理
    │   └── views/                   # 学生/商户/管理员三端页面
    └── ...
```
