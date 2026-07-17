-- ============================================================
-- 智慧校园餐饮服务平台 — 数据库初始化脚本
-- Docker 启动时自动执行，创建所有微服务的独立数据库
-- ============================================================

-- ==================== 用户服务数据库 ====================
CREATE DATABASE IF NOT EXISTS user_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE user_db;

CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码(MD5加密)',
    nickname VARCHAR(50) COMMENT '昵称',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT' COMMENT '角色:STUDENT/MERCHANT/ADMIN',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username)
) ENGINE=InnoDB COMMENT='用户表';

-- 测试数据
INSERT INTO `user` (username, password, nickname, role) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 'ADMIN'),
('student1', 'e10adc3949ba59abbe56e057f20f883e', '张同学', 'STUDENT'),
('merchant1', 'e10adc3949ba59abbe56e057f20f883e', '老王炒饭', 'MERCHANT');

-- ==================== 食堂服务数据库 ====================
CREATE DATABASE IF NOT EXISTS canteen_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE canteen_db;

CREATE TABLE IF NOT EXISTS `canteen` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '食堂名称',
    location VARCHAR(255) COMMENT '位置描述',
    open_time VARCHAR(100) COMMENT '营业时间',
    image_url VARCHAR(255) COMMENT '封面图',
    stall_count INT DEFAULT 0 COMMENT '档口数',
    deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='食堂表';

CREATE TABLE IF NOT EXISTS `stall` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    canteen_id BIGINT NOT NULL COMMENT '所属食堂ID',
    name VARCHAR(100) NOT NULL COMMENT '档口名称',
    category VARCHAR(50) COMMENT '分类',
    sort_order INT DEFAULT 0 COMMENT '排序',
    deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_canteen_id (canteen_id)
) ENGINE=InnoDB COMMENT='档口表';

CREATE TABLE IF NOT EXISTS `canteen_dish` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    stall_id BIGINT NOT NULL COMMENT '所属档口ID',
    name VARCHAR(100) NOT NULL COMMENT '菜品名称',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT DEFAULT 0 COMMENT '库存(份)',
    meal_type VARCHAR(20) NOT NULL COMMENT '餐次:BREAKFAST/LUNCH/DINNER',
    supply_date DATE COMMENT '供应日期',
    image_url VARCHAR(255) COMMENT '图片',
    description VARCHAR(500) COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态:1-供应中 0-售罄',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_stall_id (stall_id),
    INDEX idx_meal_type (meal_type)
) ENGINE=InnoDB COMMENT='食堂菜品表';

-- 测试数据
INSERT INTO canteen (name, location, open_time, stall_count) VALUES
('一食堂', '校园东区', '06:30-13:00 / 16:30-19:30', 8),
('二食堂', '校园西区', '06:30-13:00 / 16:30-19:30', 6);

INSERT INTO stall (canteen_id, name, category, sort_order) VALUES
(1, '1号窗口-自选快餐', '炒菜', 1),
(1, '2号窗口-面食', '面食', 2),
(1, '3号窗口-小吃', '小吃', 3),
(2, '1号窗口-精品小炒', '炒菜', 1),
(2, '2号窗口-米线', '米线', 2);

INSERT INTO canteen_dish (stall_id, name, price, stock, meal_type, supply_date, description, status) VALUES
(1, '红烧肉', 8.00, 50, 'LUNCH', CURDATE(), '经典红烧肉，肥而不腻', 1),
(1, '番茄炒蛋', 4.00, 80, 'LUNCH', CURDATE(), '新鲜番茄配鸡蛋', 1),
(1, '青椒肉丝', 6.00, 60, 'LUNCH', CURDATE(), '嫩滑肉丝配青椒', 1),
(2, '牛肉面', 12.00, 30, 'LUNCH', CURDATE(), '手工拉面配红烧牛肉', 1),
(2, '炸酱面', 8.00, 40, 'LUNCH', CURDATE(), '老北京炸酱面', 1),
(3, '煎饼果子', 6.00, 20, 'BREAKFAST', CURDATE(), '天津煎饼果子', 1),
(3, '豆浆油条', 4.00, 100, 'BREAKFAST', CURDATE(), '现磨豆浆配油条', 1),
(4, '宫保鸡丁', 10.00, 40, 'LUNCH', CURDATE(), '正宗川味宫保鸡丁', 1),
(4, '鱼香肉丝', 8.00, 50, 'LUNCH', CURDATE(), '经典鱼香肉丝', 1),
(5, '过桥米线', 15.00, 25, 'LUNCH', CURDATE(), '云南过桥米线', 1),
(5, '小锅米线', 10.00, 35, 'LUNCH', CURDATE(), '昆明小锅米线', 1);

-- ==================== 商户服务数据库 ====================
CREATE DATABASE IF NOT EXISTS merchant_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE merchant_db;

CREATE TABLE IF NOT EXISTS `merchant` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '关联用户ID',
    name VARCHAR(100) NOT NULL COMMENT '商户名称',
    phone VARCHAR(20) COMMENT '联系电话',
    status TINYINT DEFAULT 1 COMMENT '状态:1-正常 0-禁用',
    deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='商户表';

CREATE TABLE IF NOT EXISTS `shop` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id BIGINT NOT NULL COMMENT '所属商户ID',
    name VARCHAR(100) NOT NULL COMMENT '店铺名称',
    logo_url VARCHAR(255) COMMENT '店铺LOGO',
    open_time VARCHAR(100) COMMENT '营业时间',
    rating DECIMAL(2,1) DEFAULT 5.0 COMMENT '评分',
    deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_merchant_id (merchant_id)
) ENGINE=InnoDB COMMENT='店铺表';

CREATE TABLE IF NOT EXISTS `merchant_dish` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    shop_id BIGINT NOT NULL COMMENT '所属店铺ID',
    merchant_id BIGINT NOT NULL COMMENT '所属商户ID',
    name VARCHAR(100) NOT NULL COMMENT '菜品名称',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT DEFAULT 0 COMMENT '库存',
    category VARCHAR(50) COMMENT '分类',
    image_url VARCHAR(255) COMMENT '图片',
    description VARCHAR(500) COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态:1-上架 0-下架',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_shop_id (shop_id),
    INDEX idx_merchant_id (merchant_id)
) ENGINE=InnoDB COMMENT='商户菜品表';

-- 测试数据
INSERT INTO merchant (user_id, name, phone) VALUES (3, '老王餐饮', '13888880001');
INSERT INTO shop (merchant_id, name, rating) VALUES (1, '老王炒饭', 4.8);
INSERT INTO merchant_dish (shop_id, merchant_id, name, price, stock, category, description, status) VALUES
(1, 1, '蛋炒饭', 10.00, 30, '炒饭', '金黄蛋炒饭', 1),
(1, 1, '扬州炒饭', 15.00, 25, '炒饭', '正宗扬州炒饭', 1),
(1, 1, '牛肉炒饭', 18.00, 20, '炒饭', '嫩牛肉粒炒饭', 1);

-- ==================== 订单服务数据库 ====================
CREATE DATABASE IF NOT EXISTS order_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE order_db;

CREATE TABLE IF NOT EXISTS `order` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id VARCHAR(64) NOT NULL UNIQUE COMMENT '幂等键:唯一请求ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    source_type VARCHAR(20) NOT NULL COMMENT '菜品来源:CANTEEN/MERCHANT',
    dish_id BIGINT NOT NULL COMMENT '菜品ID',
    dish_name VARCHAR(100) COMMENT '菜品名称(冗余)',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    unit_price DECIMAL(10,2) NOT NULL COMMENT '单价',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING_PAYMENT' COMMENT '订单状态',
    payment_id BIGINT COMMENT '关联支付单ID',
    remark VARCHAR(255) COMMENT '备注',
    deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_request_id (request_id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
) ENGINE=InnoDB COMMENT='订单表';

-- ==================== 支付服务数据库 ====================
CREATE DATABASE IF NOT EXISTS payment_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE payment_db;

CREATE TABLE IF NOT EXISTS `payment` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id VARCHAR(64) NOT NULL UNIQUE COMMENT '幂等键',
    order_id BIGINT NOT NULL COMMENT '关联订单ID',
    user_id BIGINT COMMENT '用户ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态:PENDING/SUCCESS/FAILED/REFUNDED',
    pay_method VARCHAR(30) COMMENT '支付方式:WECHAT/ALIPAY/CAMPUS_CARD',
    transaction_id VARCHAR(100) COMMENT '第三方交易号',
    pay_time DATETIME COMMENT '支付时间',
    deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_request_id (request_id),
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB COMMENT='支付表';

-- ==================== 通知服务数据库 ====================
CREATE DATABASE IF NOT EXISTS notice_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE notice_db;

CREATE TABLE IF NOT EXISTS `notice` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '接收用户ID',
    title VARCHAR(200) NOT NULL COMMENT '通知标题',
    content TEXT COMMENT '通知内容',
    type VARCHAR(30) COMMENT '类型:ORDER/SYSTEM',
    is_read TINYINT DEFAULT 0 COMMENT '已读:0-未读 1-已读',
    deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB COMMENT='通知表';

-- ==================== Seata AT 事务回滚表 ====================
-- order_db 需要 Seata undo_log 表（如果启用分布式事务）
CREATE TABLE IF NOT EXISTS order_db.undo_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    branch_id BIGINT NOT NULL,
    xid VARCHAR(128) NOT NULL,
    context VARCHAR(128) NOT NULL,
    rollback_info LONGBLOB NOT NULL,
    log_status INT NOT NULL,
    log_created DATETIME NOT NULL,
    log_modified DATETIME NOT NULL,
    UNIQUE KEY ux_undo_log (xid, branch_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Seata AT事务回滚表';

CREATE TABLE IF NOT EXISTS canteen_db.undo_log LIKE order_db.undo_log;
CREATE TABLE IF NOT EXISTS merchant_db.undo_log LIKE order_db.undo_log;
CREATE TABLE IF NOT EXISTS payment_db.undo_log LIKE order_db.undo_log;
