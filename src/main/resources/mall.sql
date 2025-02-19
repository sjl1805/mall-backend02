-- 创建数据库
CREATE DATABASE
IF
	NOT EXISTS mall CHARACTER 
	SET utf8mb4;
USE mall;
-- ===============================
-- 第一层：基础表（无外键依赖）
-- ===============================
-- 1. 用户表
CREATE TABLE users (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
	`username` VARCHAR ( 64 ) NOT NULL COMMENT '用户名',
	`password` VARCHAR ( 68 ) NOT NULL COMMENT 'BCrypt加密后的密码哈希值',
	`nickname` VARCHAR ( 64 ) COMMENT '昵称',
	`phone` VARCHAR ( 11 ) COMMENT '手机号',
	`email` VARCHAR ( 128 ) COMMENT '邮箱',
	`avatar` VARCHAR ( 255 ) DEFAULT '/images/backend.png' COMMENT '头像',
	`gender` TINYINT DEFAULT 0 COMMENT '性别：0未知 1男 2女',
	`status` TINYINT DEFAULT 1 COMMENT '状态：0禁用 1启用',
	`role` TINYINT UNSIGNED NOT NULL DEFAULT 1 CHECK (
	`role` IN ( 0, 1, 2, 9 )),
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	PRIMARY KEY ( `id` ),
	UNIQUE INDEX `idx_username` ( `username` ),
	UNIQUE INDEX `idx_phone` ( `phone` ),
	UNIQUE INDEX `idx_email` ( `email` ) 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '用户表';-- 添加复合索引
ALTER TABLE users ADD INDEX idx_search ( username, phone, STATUS );
ALTER TABLE users ADD INDEX idx_create_time ( create_time );-- 2. 商品分类表
CREATE TABLE `category` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
	`parent_id` BIGINT COMMENT '父分类ID',
	`name` VARCHAR ( 64 ) NOT NULL COMMENT '分类名称',
	`icon` VARCHAR ( 255 ) COMMENT '分类图标',
	`level` TINYINT NOT NULL COMMENT '层级：1一级 2二级 3三级',
	`sort` INT DEFAULT 0 COMMENT '排序',
	`status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	PRIMARY KEY ( `id` ),
	INDEX `idx_parent_id` ( `parent_id` ) 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '商品分类表';-- 添加分类表索引
ALTER TABLE category ADD INDEX idx_parent_status ( parent_id, STATUS );
ALTER TABLE category ADD INDEX idx_level_sort ( LEVEL, sort );-- 3. 商品表（使用JSON类型）
CREATE TABLE products (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
	`category_id` BIGINT NOT NULL COMMENT '分类ID',
	`name` VARCHAR ( 100 ) NOT NULL COMMENT '商品名称',
	`description` TEXT COMMENT '商品描述',
	`price` DECIMAL ( 10, 2 ) NOT NULL COMMENT '价格',
	`stock` INT DEFAULT 0 COMMENT '库存',
	`images` JSON COMMENT '商品图片JSON数组',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	`status` TINYINT DEFAULT 1 COMMENT '商品状态：0-下架 1-上架',
	PRIMARY KEY ( `id` ),
	INDEX `idx_products_search` ( `name`, `category_id`, `price` ),
	FOREIGN KEY ( `category_id` ) REFERENCES category ( `id` ) 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '商品表';-- 修改商品表索引顺序
ALTER TABLE products 
    DROP INDEX idx_products_search,
    ADD INDEX idx_products_search (category_id, name, price);-- 4. 优惠券表
CREATE TABLE `coupon` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
	`name` VARCHAR ( 32 ) NOT NULL COMMENT '优惠券名称',
	`type` TINYINT NOT NULL COMMENT '优惠券类型：1-满减券 2-折扣券',
	`value` DECIMAL ( 10, 2 ) NOT NULL COMMENT '优惠券面值',
	`min_amount` DECIMAL ( 10, 2 ) NOT NULL COMMENT '使用门槛',
	`start_time` TIMESTAMP NOT NULL COMMENT '生效时间',
	`end_time` TIMESTAMP NOT NULL COMMENT '失效时间',
	`status` TINYINT DEFAULT 1 COMMENT '状态：0-失效 1-生效',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	PRIMARY KEY ( `id` ) 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '优惠券表';-- 添加优惠券表索引
ALTER TABLE coupon ADD INDEX idx_status_time ( STATUS, end_time );
ALTER TABLE coupon ADD INDEX idx_type ( type );-- 5. 用户地址表
CREATE TABLE user_address (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT NOT NULL,
	`is_default` TINYINT COMMENT '默认地址状态：0-非默认 1-默认',
	`is_default_true` TINYINT GENERATED ALWAYS AS (IF(is_default = 1, 1, NULL)) VIRTUAL,
	`receiver_name` VARCHAR ( 32 ) NOT NULL,
	`receiver_phone` VARCHAR ( 20 ) NOT NULL,
	`province` VARCHAR ( 50 ) NOT NULL,
	`city` VARCHAR ( 50 ) NOT NULL,
	`district` VARCHAR ( 50 ) NOT NULL,
	`detail_address` VARCHAR ( 200 ) NOT NULL,
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	PRIMARY KEY ( `id` ),
	FOREIGN KEY ( `user_id` ) REFERENCES users ( `id` ),
	UNIQUE INDEX `uk_user_default` ( `user_id`, `is_default_true` ),
	INDEX `idx_receiver_name` ( `receiver_name` ),
	INDEX `idx_receiver_phone` ( `receiver_phone` ),
	INDEX `idx_province` ( `province` ),
	INDEX `idx_city` ( `city` ),
	INDEX `idx_district` ( `district` ),
	INDEX `idx_detail_address` ( `detail_address` ) 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '用户收货地址表';-- 6. 收藏夹表
CREATE TABLE `favorite_folder` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏夹ID',
	`user_id` BIGINT NOT NULL COMMENT '用户ID',
	`name` VARCHAR ( 32 ) NOT NULL COMMENT '收藏夹名称',
	`description` VARCHAR ( 128 ) COMMENT '收藏夹描述',
	`is_public` TINYINT DEFAULT 0 COMMENT '公开状态：0-私密 1-公开',
	`item_count` INT DEFAULT 0 COMMENT '收藏项数量',
	`sort` INT DEFAULT 0 COMMENT '排序',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	PRIMARY KEY ( `id` ),
	INDEX `idx_user_id` ( `user_id` ),
	FOREIGN KEY ( `user_id` ) REFERENCES users ( `id` ) ON DELETE CASCADE 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '收藏夹表';-- 添加收藏夹表索引
ALTER TABLE favorite_folder ADD INDEX idx_user_public ( user_id, is_public );
ALTER TABLE favorite_folder ADD INDEX idx_sort ( sort );-- 7. 购物车表
CREATE TABLE `cart` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
	`user_id` BIGINT NOT NULL COMMENT '用户ID',
	`product_id` BIGINT NOT NULL COMMENT '商品ID',
	`quantity` INT NOT NULL COMMENT '商品数量',
	`checked` TINYINT DEFAULT 1 COMMENT '选中状态：0-未选中 1-已选中',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	PRIMARY KEY ( `id` ),
	INDEX `idx_user_id` ( `user_id` ),
	FOREIGN KEY ( `user_id` ) REFERENCES users ( `id` ),
	FOREIGN KEY ( `product_id` ) REFERENCES products ( `id` ),
	UNIQUE INDEX `uk_user_product` ( `user_id`, `product_id` ) 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '购物车表';-- 添加购物车表索引
ALTER TABLE cart ADD INDEX idx_user_checked ( user_id, checked );
ALTER TABLE cart ADD INDEX idx_product ( product_id );-- 8. 订单表（完整状态字段）
CREATE TABLE orders (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
	`order_no` VARCHAR ( 64 ) NOT NULL COMMENT '订单号',
	`user_id` BIGINT NOT NULL COMMENT '用户ID',
	`total_amount` DECIMAL ( 10, 2 ) NOT NULL COMMENT '订单总金额',
	`pay_amount` DECIMAL ( 10, 2 ) NOT NULL COMMENT '实付金额',
	`status` TINYINT COMMENT '订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消 5-退款中',
	`receiver_name` VARCHAR ( 32 ) NOT NULL COMMENT '收货人姓名',
	`receiver_phone` VARCHAR ( 11 ) NOT NULL COMMENT '收货人电话',
	`receiver_address` VARCHAR ( 255 ) NOT NULL COMMENT '收货地址',
	`payment_time` TIMESTAMP COMMENT '支付时间',
	`delivery_time` TIMESTAMP COMMENT '发货时间',
	`receive_time` TIMESTAMP COMMENT '收货时间',
	`payment_method` TINYINT COMMENT '支付方式：1-支付宝 2-微信 3-银联',
	`logistics_company` VARCHAR ( 64 ) COMMENT '物流公司',
	`tracking_number` VARCHAR ( 64 ) COMMENT '运单号',
	`comment_status` TINYINT DEFAULT 0 COMMENT '评价状态：0未评价 1已评价',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	`timezone` VARCHAR ( 50 ) DEFAULT '+08:00' COMMENT '时区信息',
	PRIMARY KEY ( `id` ),
	UNIQUE INDEX `uk_order_no` ( `order_no` ),
	INDEX `idx_orders_user_status` ( `user_id`, `status` ),
	INDEX `idx_payment_time` ( `payment_time` ),
	INDEX `idx_logistics` ( `logistics_company`, `tracking_number` ),
	FOREIGN KEY ( `user_id` ) REFERENCES users ( `id` ) 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '订单表';-- 添加订单表索引
ALTER TABLE orders ADD INDEX idx_user_status ( user_id, STATUS );
ALTER TABLE orders ADD INDEX idx_create_time ( create_time );-- 9. 订单项表（级联删除）
CREATE TABLE `order_item` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单商品ID',
	`order_id` BIGINT NOT NULL COMMENT '订单ID',
	`product_id` BIGINT NOT NULL COMMENT '商品ID',
	`product_name` VARCHAR ( 128 ) NOT NULL COMMENT '商品名称',
	`product_image` VARCHAR ( 255 ) COMMENT '商品主图URL',
	`price` DECIMAL ( 10, 2 ) NOT NULL COMMENT '商品单价',
	`quantity` INT NOT NULL COMMENT '购买数量',
	`total_amount` DECIMAL ( 10, 2 ) NOT NULL COMMENT '商品总价',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	PRIMARY KEY ( `id` ),
	INDEX `idx_order_id` ( `order_id` ),
	INDEX `idx_product_id` ( `product_id` ),
	FOREIGN KEY ( `order_id` ) REFERENCES `orders` ( `id` ) ON DELETE CASCADE,
	FOREIGN KEY ( `product_id` ) REFERENCES products ( `id` ) ON DELETE CASCADE 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '订单商品表';-- 添加订单项表索引
ALTER TABLE order_item ADD INDEX idx_order_product ( order_id, product_id );
ALTER TABLE order_item ADD INDEX idx_product_sales ( product_id );-- 10. 用户优惠券表（依赖用户、优惠券）
CREATE TABLE `user_coupon` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户优惠券ID',
	`user_id` BIGINT NOT NULL COMMENT '用户ID',
	`coupon_id` BIGINT NOT NULL COMMENT '优惠券ID',
	`status` TINYINT COMMENT '用户优惠券状态：0-未使用 1-已使用 2-已过期',
	`order_id` BIGINT COMMENT '使用的订单ID',
	`get_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
	`use_time` TIMESTAMP COMMENT '使用时间',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	PRIMARY KEY ( `id` ),
	INDEX `idx_user_id` ( `user_id` ),
	INDEX `idx_coupon_id` ( `coupon_id` ),
	FOREIGN KEY ( `user_id` ) REFERENCES users ( `id` ) ON DELETE CASCADE,
	FOREIGN KEY ( `coupon_id` ) REFERENCES coupon ( `id` ) ON DELETE CASCADE,
	FOREIGN KEY ( `order_id` ) REFERENCES `orders` ( `id` ) ON DELETE 
	SET NULL 
	) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '用户优惠券表';-- 添加用户优惠券表索引
ALTER TABLE user_coupon ADD INDEX idx_user_status ( user_id, STATUS );
ALTER TABLE user_coupon ADD INDEX idx_coupon_valid ( coupon_id, STATUS, get_time );-- 11. 商品规格表（依赖商品）
CREATE TABLE `product_spec` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '规格ID',
	`product_id` BIGINT NOT NULL COMMENT '商品ID',
	`spec_name` VARCHAR ( 64 ) NOT NULL COMMENT '规格名称',
	`spec_values` TEXT NOT NULL COMMENT '规格值，JSON格式',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	PRIMARY KEY ( `id` ),
	INDEX `idx_product_id` ( `product_id` ),
	FOREIGN KEY ( `product_id` ) REFERENCES products ( `id` ) ON DELETE CASCADE 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '商品规格表';-- 添加商品规格表索引
ALTER TABLE product_spec ADD INDEX idx_product_spec ( product_id, spec_name );
ALTER TABLE product_spec ADD INDEX idx_spec_name ( spec_name );-- 12. 商品SKU表（带默认主图）
CREATE TABLE `product_sku` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
	`product_id` BIGINT NOT NULL COMMENT '商品ID',
	`spec_values` VARCHAR ( 255 ) NOT NULL COMMENT '规格值组合（JSON）',
	`price` DECIMAL ( 10, 2 ) NOT NULL COMMENT '价格',
	`stock` INT NOT NULL COMMENT '库存',
	`sales` INT DEFAULT 0 COMMENT '销量',
	`main_image` VARCHAR ( 255 ) DEFAULT '/images/backend.png' COMMENT 'SKU主图',
	`status` TINYINT COMMENT 'SKU状态：0-下架 1-上架',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	PRIMARY KEY ( `id` ),
	INDEX `idx_product_id` ( `product_id` ),
	INDEX `idx_status_price` ( `status`, `price` ),
	INDEX `idx_sales` ( `sales` ),
	INDEX `idx_price_stock_status` ( `price`, `stock`, `status` ),
	FOREIGN KEY ( `product_id` ) REFERENCES products ( `id` ) ON DELETE CASCADE 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '商品SKU表';-- 修改商品SKU表索引定义
ALTER TABLE product_sku 
    DROP INDEX idx_price_stock_status,
    ADD INDEX idx_price_stock_status (price, stock, status);-- 13. 商品评价表（依赖订单、用户、商品）
CREATE TABLE `product_review` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评价ID',
	`order_id` BIGINT NOT NULL COMMENT '订单ID',
	`user_id` BIGINT NOT NULL COMMENT '用户ID',
	`product_id` BIGINT NOT NULL COMMENT '商品ID',
	`rating` TINYINT NOT NULL COMMENT '评分：1-5分',
	`content` TEXT COMMENT '评价内容',
	`images` TEXT COMMENT '评价图片，JSON格式',
	`status` TINYINT COMMENT '审核状态：0-待审核 1-已通过 2-已拒绝',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	PRIMARY KEY ( `id` ),
	INDEX `idx_order_id` ( `order_id` ),
	INDEX `idx_product_id` ( `product_id` ),
	FOREIGN KEY ( `order_id` ) REFERENCES `orders` ( `id` ),
	FOREIGN KEY ( `user_id` ) REFERENCES users ( `id` ),
	FOREIGN KEY ( `product_id` ) REFERENCES products ( `id` ),
	UNIQUE INDEX `uk_order_product` ( `order_id`, `product_id` ) 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '商品评价表';-- 添加评价表索引
ALTER TABLE product_review ADD INDEX idx_product_rating ( product_id, rating );
ALTER TABLE product_review ADD INDEX idx_user_order ( user_id, order_id );-- 14. 商品收藏表
CREATE TABLE `product_favorite` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
	`user_id` BIGINT NOT NULL COMMENT '用户ID',
	`product_id` BIGINT NOT NULL COMMENT '商品ID',
	`folder_id` BIGINT COMMENT '收藏夹ID（NULL表示未分类）',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	PRIMARY KEY ( `id` ),
	INDEX `idx_user_id` ( `user_id` ),
	FOREIGN KEY ( `folder_id` ) REFERENCES favorite_folder ( `id` ) ON DELETE 
	SET NULL,
	FOREIGN KEY ( `user_id` ) REFERENCES users ( `id` ) ON DELETE CASCADE 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '商品收藏表';-- 添加收藏表索引
ALTER TABLE product_favorite ADD INDEX idx_user_product ( user_id, product_id );
ALTER TABLE product_favorite ADD INDEX idx_folder ( folder_id );-- 15. 推荐商品表
CREATE TABLE `recommend_product` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '推荐商品ID',
	`product_id` BIGINT NOT NULL COMMENT '商品ID',
	`type` TINYINT NOT NULL COMMENT '推荐类型：1-热门商品 2-新品推荐',
	`sort` INT NOT NULL COMMENT '排序',
	`status` TINYINT NOT NULL COMMENT '推荐状态：0-未生效 1-生效中',
	`start_time` TIMESTAMP NOT NULL COMMENT '开始时间',
	`end_time` TIMESTAMP NOT NULL COMMENT '结束时间',
	`algorithm_version` VARCHAR ( 64 ) NOT NULL COMMENT '算法版本',
	`recommend_reason` TEXT COMMENT '推荐理由',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	PRIMARY KEY ( `id` ),
	INDEX `idx_product_id` ( `product_id` ),
	FOREIGN KEY ( `product_id` ) REFERENCES products ( `id` ) ON DELETE CASCADE 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '推荐商品表';-- 添加推荐商品表索引
ALTER TABLE recommend_product ADD INDEX idx_type_status ( type, STATUS );
ALTER TABLE recommend_product ADD INDEX idx_time_range ( start_time, end_time );
ALTER TABLE recommend_product ADD INDEX idx_algorithm ( algorithm_version );-- 16. 用户行为表
CREATE TABLE user_behavior (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '行为记录ID',
	`user_id` BIGINT NOT NULL COMMENT '用户ID',
	`product_id` BIGINT NOT NULL COMMENT '商品ID',
	`behavior_type` TINYINT NOT NULL COMMENT '行为类型：1-浏览 2-收藏 3-购买',
	`behavior_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '行为时间',
	`duration` INT DEFAULT 0 COMMENT '停留时长（秒）',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
	`weight` DECIMAL ( 3, 1 ) NOT NULL DEFAULT 0.5 COMMENT '行为权重',
	PRIMARY KEY ( `id` ),
	INDEX `idx_user_behavior` ( `user_id` ),
	INDEX `idx_behavior_time` ( `behavior_time` ),
	FOREIGN KEY ( `user_id` ) REFERENCES users ( `id` ),
	FOREIGN KEY ( `product_id` ) REFERENCES products ( `id` ) 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '用户行为记录表';-- 在用户行为表创建语句后添加索引
ALTER TABLE user_behavior ADD INDEX idx_user_behavior_type ( user_id, behavior_type );
ALTER TABLE user_behavior ADD INDEX idx_behavior_type_time ( behavior_type, behavior_time );
ALTER TABLE user_behavior ADD INDEX idx_product_behavior ( product_id, behavior_type );-- ===============================
-- 测试数据（按依赖顺序插入）
-- ===============================
-- 1. 用户表（4条）
INSERT INTO users ( username, PASSWORD, nickname, phone, email, gender, role )
VALUES
	( 'user', '$2a$10$k6n948le92ccts2LWj4mrubCqe4XaS3Yi8K6EiiBmAuMIy5m/B5zq', '张三', '13800000001', 'user1@mall.com', 1, 1 ),
	( 'user2', '$2a$10$k6n948le92ccts2LWj4mrubCqe4XaS3Yi8K6EiiBmAuMIy5m/B5zq', '李四', '13800000002', 'user2@mall.com', 2, 1 ),
	( 'user3', '$2a$10$k6n948le92ccts2LWj4mrubCqe4XaS3Yi8K6EiiBmAuMIy5m/B5zq', '王五', '13800000003', 'user3@mall.com', 1, 2 ),
	( 'admin', '$2a$10$k6n948le92ccts2LWj4mrubCqe4XaS3Yi8K6EiiBmAuMIy5m/B5zq', '管理员', '13800000004', 'admin@mall.com', 0, 9 );-- 2. 商品分类表（10条）
INSERT INTO category ( parent_id, NAME, LEVEL, sort )
VALUES
	( NULL, '手机数码', 1, 1 ),
	( 1, '智能手机', 2, 1 ),
	( 2, '5G手机', 3, 1 ),
	( NULL, '电脑办公', 1, 2 ),
	( 4, '笔记本', 2, 1 ),
	( 5, '游戏本', 3, 1 ),
	( NULL, '家用电器', 1, 3 ),
	( 7, '空调', 2, 1 ),
	( 7, '洗衣机', 2, 2 ),
	( 7, '冰箱', 2, 3 );-- 3. 商品表（10条）
INSERT INTO products ( category_id, NAME, description, price, stock, STATUS, images )
VALUES
	( 3, '华为Mate50', '5G旗舰手机', 5999.00, 100, 1, '[
		"/images/backend.png"
	]' ),
	( 3, 'iPhone15', 'A16芯片', 8999.00, 80, 1, '[
		"/images/backend.png"
	]' ),
	( 6, '联想拯救者', 'RTX4060显卡', 9999.00, 30, 1, '[
		"/images/backend.png"
	]' ),
	( 8, '格力空调', '一级能效', 3999.00, 50, 1, '[
		"/images/backend.png"
	]' ),
	( 9, '海尔洗衣机', '10公斤洗烘一体', 2999.00, 40, 1, '[
		"/images/backend.png"
	]' ),
	( 10, '西门子冰箱', '对开门', 5999.00, 20, 1, '[
		"/images/backend.png"
	]' ),
	( 5, 'MacBook Pro', 'M2芯片', 12999.00, 15, 1, '[
		"/images/backend.png"
	]' ),
	( 3, '小米13', '徕卡影像', 4299.00, 60, 1, '[
		"/images/backend.png"
	]' ),
	( 6, 'ROG枪神7', 'i9-13980HX', 19999.00, 10, 1, '[
		"/images/backend.png"
	]' ),
	( 8, '美的空调', '新一级能效', 3599.00, 45, 1, '[
		"/images/backend.png"
	]' );-- 4. 优惠券表（10条）
INSERT INTO coupon ( NAME, type, VALUE, min_amount, start_time, end_time )
VALUES
	( '新人满1000减200', 1, 200.00, 1000.00, '2025-01-01', '2025-12-31' ),
	( '大家电9折券', 2, 0.90, 3000.00, '2025-01-01', '2025-12-31' ),
	( '手机专用券', 1, 500.00, 4000.00, '2025-01-01', '2025-12-31' ),
	( '全品类8折', 2, 0.80, 2000.00, '2025-06-01', '2025-12-31' ),
	( '暑促优惠券', 1, 300.00, 1500.00, '2025-07-01', '2025-08-31' ),
	( '冰箱专属券', 1, 800.00, 5000.00, '2025-01-01', '2025-12-31' ),
	( '笔记本立减', 1, 1000.00, 8000.00, '2025-09-01', '2025-12-31' ),
	( '空调折扣券', 2, 0.85, 3000.00, '2025-01-01', '2025-12-31' ),
	( '年货节通用券', 1, 150.00, 1000.00, '2025-12-01', '2026-02-10' ),
	( '会员专属券', 2, 0.75, 5000.00, '2025-01-01', '2025-12-31' );-- 5. 用户地址表（10条）
INSERT INTO user_address ( user_id, is_default, receiver_name, receiver_phone, province, city, district, detail_address )
VALUES
	( 1, 1, '张三', '13800000001', '北京市', '北京市', '海淀区', '中关村大街1号' ),
	( 1, 0, '张三公司', '13800000001', '北京市', '北京市', '朝阳区', '国贸大厦A座' ),
	( 2, 1, '李四', '13800000002', '上海市', '上海市', '浦东新区', '陆家嘴环路100号' ),
	( 3, 1, '王五', '13800000003', '广东省', '深圳市', '南山区', '科技园科兴科学园' ),
	( 2, 0, '李四父母家', '13512345678', '江苏省', '南京市', '鼓楼区', '中山北路100号' ),
	( 3, 0, '王五学校', '13987654321', '湖北省', '武汉市', '洪山区', '珞喻路1037号' ),
	( 1, 0, '张三老家', '010-1234567', '浙江省', '杭州市', '余杭区', '文一西路969号' ),
	( 2, 0, '李四度假屋', '13800000002', '海南省', '三亚市', '天涯区', '滨海路88号' ),
	( 3, 0, '王五工作室', '13800000003', '四川省', '成都市', '高新区', '天府三街' ),
	( 1, 0, '张三仓库', '13800000001', '广东省', '广州市', '天河区', '珠江新城' );-- 6. 订单表（10条）
INSERT INTO orders ( user_id, order_no, total_amount, pay_amount, STATUS, receiver_name, receiver_phone, receiver_address )
VALUES
	( 1, '202309010001', 11998.00, 11798.00, 3, '张三', '13800000001', '北京市海淀区中关村大街1号' ),
	( 2, '202309010002', 8999.00, 8999.00, 1, '李四', '13800000002', '上海市浦东新区陆家嘴环路100号' ),
	( 3, '202309010003', 19999.00, 17999.00, 2, '王五', '13800000003', '深圳市南山区科技园' ),
	( 1, '202309010004', 2999.00, 2999.00, 0, '张三', '13800000001', '北京市朝阳区国贸大厦A座' ),
	( 2, '202309010005', 5999.00, 5999.00, 4, '李四', '13800000002', '南京市鼓楼区中山北路100号' ),
	( 3, '202309010006', 12999.00, 11699.00, 3, '王五', '13800000003', '武汉市洪山区珞喻路1037号' ),
	( 1, '202309010007', 3599.00, 3059.00, 1, '张三', '13800000001', '杭州市余杭区文一西路969号' ),
	( 2, '202309010008', 9999.00, 9499.00, 2, '李四', '13800000002', '三亚市天涯区滨海路88号' ),
	( 3, '202309010009', 4299.00, 4299.00, 3, '王五', '13800000003', '成都市高新区天府三街' ),
	( 1, '202309010010', 5999.00, 5999.00, 4, '张三', '13800000001', '广州市天河区珠江新城' );-- 7. 订单项表（10条）
INSERT INTO order_item ( order_id, product_id, product_name, product_image, price, quantity, total_amount )
VALUES
	( 1, 1, '华为Mate50', '/images/backend.png', 5999.00, 2, 11998.00 ),
	( 2, 2, 'iPhone15', '/images/backend.png', 8999.00, 1, 8999.00 ),
	( 3, 9, 'ROG枪神7', '/images/backend.png', 19999.00, 1, 19999.00 ),
	( 4, 5, '海尔洗衣机', '/images/backend.png', 2999.00, 1, 2999.00 ),
	( 5, 6, '西门子冰箱', '/images/backend.png', 5999.00, 1, 5999.00 ),
	( 6, 7, 'MacBook Pro', '/images/backend.png', 12999.00, 1, 12999.00 ),
	( 7, 10, '美的空调', '/images/backend.png', 3599.00, 1, 3599.00 ),
	( 8, 3, '联想拯救者', '/images/backend.png', 9999.00, 1, 9999.00 ),
	( 9, 8, '小米13', '/images/backend.png', 4299.00, 1, 4299.00 ),
	( 10, 6, '西门子冰箱', '/images/backend.png', 5999.00, 1, 5999.00 );-- 8. 收藏夹表（10条）
INSERT INTO favorite_folder ( user_id, NAME, description, is_public )
VALUES
	( 1, '电子产品', '手机电脑收藏', 1 ),
	( 1, '家电清单', '家用电器收藏', 0 ),
	( 2, '数码好物', '最新科技产品', 1 ),
	( 2, '办公设备', '办公用品收藏', 0 ),
	( 3, '游戏装备', '电竞设备合集', 1 ),
	( 1, '节日礼物', '送礼备选清单', 0 ),
	( 2, '厨房电器', '厨用设备收藏', 1 ),
	( 3, '智能家居', '智能设备集合', 1 ),
	( 1, '限时优惠', '特价商品关注', 0 ),
	( 2, '品牌专区', '品牌直营商品', 1 );-- 9. 购物车表（10条）
INSERT INTO cart ( user_id, product_id, quantity, checked )
VALUES
	( 1, 1, 2, 1 ),
	( 1, 3, 1, 0 ),
	( 2, 2, 1, 1 ),
	( 2, 5, 3, 1 ),
	( 3, 7, 1, 1 ),
	( 1, 4, 1, 0 ),
	( 2, 6, 2, 1 ),
	( 3, 8, 1, 1 ),
	( 1, 9, 1, 1 ),
	( 2, 10, 2, 0 );-- 10. 用户优惠券表（10条）
INSERT INTO user_coupon ( user_id, coupon_id, STATUS, order_id )
VALUES
	( 1, 1, 0, NULL ),
	( 1, 3, 1, 1 ),
	( 2, 2, 0, NULL ),
	( 2, 5, 2, NULL ),
	( 3, 4, 0, NULL ),
	( 1, 7, 1, 7 ),
	( 2, 6, 0, NULL ),
	( 3, 8, 1, 3 ),
	( 1, 9, 0, NULL ),
	( 2, 10, 1, 8 );-- 11. 商品规格表（10条）
INSERT INTO product_spec ( product_id, spec_name, spec_values )
VALUES
	( 1, '颜色', '["黑色","白色","银色"]' ),
	( 1, '存储', '["128GB","256GB","512GB"]' ),
	( 2, '版本', '["标准版","Pro版"]' ),
	( 3, '显卡配置', '["RTX4060","RTX4070"]' ),
	( 4, '匹数', '["1匹","1.5匹","2匹"]' ),
	( 5, '容量', '["8kg","10kg"]' ),
	( 6, '门体结构', '["对开门","十字对开门"]' ),
	( 7, '芯片类型', '["M1","M2"]' ),
	( 8, '内存组合', '["8+128","12+256"]' ),
	( 9, '屏幕尺寸', '["16英寸","18英寸"]' );-- 12. 商品SKU表（10条）
INSERT INTO product_sku ( product_id, spec_values, price, stock )
VALUES
	( 1, '{"颜色":"黑色","存储":"128GB"}', 5999.00, 50 ),
	( 1, '{"颜色":"白色","存储":"256GB"}', 6499.00, 30 ),
	( 2, '{"版本":"Pro版"}', 7999.00, 40 ),
	( 3, '{"显卡配置":"RTX4070"}', 10999.00, 15 ),
	( 4, '{"匹数":"1.5匹"}', 4199.00, 20 ),
	( 5, '{"容量":"10kg"}', 3299.00, 25 ),
	( 6, '{"门体结构":"十字对开门"}', 6599.00, 10 ),
	( 7, '{"芯片类型":"M2"}', 12999.00, 8 ),
	( 8, '{"内存组合":"12+256"}', 4699.00, 35 ),
	( 9, '{"屏幕尺寸":"18英寸"}', 21999.00, 5 );-- 13. 商品收藏表（10条）
INSERT INTO product_favorite ( user_id, product_id, folder_id )
VALUES
	( 1, 1, 1 ),
	( 1, 3, 2 ),
	( 2, 2, 3 ),
	( 2, 5, NULL ),
	( 3, 7, 5 ),
	( 1, 4, 6 ),
	( 2, 6, 7 ),
	( 3, 8, 8 ),
	( 1, 9, 9 ),
	( 2, 10, NULL );-- 14. 推荐商品表（10条）
INSERT INTO recommend_product ( product_id, type, sort, STATUS, start_time, end_time, algorithm_version )
VALUES
	( 1, 1, 1, 1, '2025-01-01', '2025-12-31', 'v2.1' ),
	( 2, 2, 2, 1, '2025-06-01', '2025-12-31', 'v2.0' ),
	( 3, 1, 3, 0, '2025-03-01', '2025-05-31', 'v1.5' ),
	( 4, 2, 4, 1, '2025-07-01', '2025-08-31', 'v2.2' ),
	( 5, 1, 5, 1, '2025-01-01', '2025-12-31', 'v2.1' ),
	( 6, 2, 6, 0, '2025-09-01', '2025-11-30', 'v1.8' ),
	( 7, 1, 7, 1, '2025-02-01', '2025-12-31', 'v2.3' ),
	( 8, 2, 8, 1, '2025-04-01', '2025-10-31', 'v2.0' ),
	( 9, 1, 9, 0, '2025-05-01', '2025-07-31', 'v1.9' ),
	( 10, 2, 10, 1, '2025-08-01', '2025-01-31', 'v2.1' );-- 15. 用户行为表（10条）
INSERT INTO user_behavior ( user_id, product_id, behavior_type, weight )
VALUES
	( 1, 1, 1, 0.8 ),
	( 1, 3, 1, 0.6 ),
	( 2, 2, 1, 0.9 ),
	( 2, 5, 2, 0.7 ),
	( 3, 7, 1, 0.5 ),
	( 1, 4, 1, 0.8 ),
	( 2, 6, 3, 0.6 ),
	( 3, 8, 1, 0.7 ),
	( 1, 9, 2, 0.9 ),
	( 2, 10, 1, 0.8 );-- 16. 商品评价表（10条）
INSERT INTO product_review ( order_id, user_id, product_id, rating, content )
VALUES
	( 1, 1, 1, 5, '手机运行流畅，拍照效果很棒！' ),
	( 3, 3, 9, 4, '性能强劲，散热效果好' ),
	( 6, 3, 7, 5, '电脑运行速度超快，屏幕显示细腻' ),
	( 7, 1, 10, 4, '制冷效果不错，安装服务好' ),
	( 8, 2, 3, 3, '显卡性能符合预期，但机身较重' ),
	( 9, 3, 8, 5, '性价比很高的手机，系统流畅' ),
	( 2, 2, 2, 2, '电池续航不如预期' ),
	( 4, 1, 5, 4, '洗衣效果满意，噪音小' ),
	( 5, 2, 6, 5, '冰箱容量大，节能效果好' ),
	( 10, 1, 6, 3, '制冷速度一般，空间布局合理' );