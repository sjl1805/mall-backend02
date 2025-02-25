-- 创建数据库
CREATE DATABASE IF NOT EXISTS mall CHARACTER SET utf8mb4;
USE mall;

-- 1. 用户表（简化字段）
CREATE TABLE users (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `password` VARCHAR(68) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(64) COMMENT '昵称',
    `phone` VARCHAR(11) COMMENT '手机号',
    `email` VARCHAR(128) COMMENT '邮箱',
    `avatar` VARCHAR(255) COMMENT '头像',
    `role` TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '角色：0管理员 1普通用户 2商家',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0禁用 1启用',
    `age_range` VARCHAR(20) COMMENT '年龄段',
    `gender` TINYINT COMMENT '性别：0-未知 1-男 2-女',
    `consumption_level` TINYINT COMMENT '消费能力：1-低 2-中 3-高',
    `activity_level` TINYINT COMMENT '活跃度：1-低 2-中 3-高',
    `preferred_categories` JSON COMMENT '偏好分类JSON',
    `tags` JSON COMMENT '用户标签JSON',
    `last_active_time` TIMESTAMP COMMENT '最后活跃时间',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_username` (`username`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '用户表';

-- 2. 商品分类表（简化字段和索引）
CREATE TABLE `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `parent_id` BIGINT COMMENT '父分类ID',
    `name` VARCHAR(64) NOT NULL COMMENT '分类名称',
    `image` VARCHAR(255) COMMENT '分类图片',
    `level` TINYINT NOT NULL COMMENT '层级：1一级 2二级 3三级',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_parent_id` (`parent_id`),
    FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '商品分类表';

-- 3. 商品标签表
CREATE TABLE `product_tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    `name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `type` TINYINT DEFAULT 1 COMMENT '标签类型：1-风格 2-场景 3-功能 4-其他',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_name` (`name`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '商品标签表';

-- 4. 商品表（集成标签关联）
CREATE TABLE products (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `category_id` BIGINT NOT NULL COMMENT '分类ID',
    `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
    `description` TEXT COMMENT '商品描述',
    `price` DECIMAL(10, 2) NOT NULL COMMENT '价格',
    `stock` INT DEFAULT 0 COMMENT '库存',
    `image_main` VARCHAR(255) COMMENT '主图URL',
    `images` JSON COMMENT '商品图片JSON数组',
    `tags` JSON COMMENT '商品标签JSON，包含标签ID和权重',
    `status` TINYINT DEFAULT 1 COMMENT '商品状态：0-下架 1-上架',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_category` (`category_id`),
    FOREIGN KEY (`category_id`) REFERENCES category (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '商品表';

-- 5. 商品SKU表
CREATE TABLE `product_sku` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `sku_code` VARCHAR(64) NOT NULL COMMENT 'SKU编码',
    `specifications` JSON NOT NULL COMMENT '规格JSON，如：{"颜色":"红色","尺寸":"XL"}',
    `price` DECIMAL(10, 2) NOT NULL COMMENT 'SKU价格',
    `stock` INT NOT NULL DEFAULT 0 COMMENT 'SKU库存',
    `image` VARCHAR(255) COMMENT 'SKU图片',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_sku_code` (`sku_code`),
    INDEX `idx_product_id` (`product_id`),
    FOREIGN KEY (`product_id`) REFERENCES products (`id`) ON DELETE CASCADE
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '商品SKU表';

-- 6. 用户地址表（简化字段和索引）
CREATE TABLE user_address (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '地址ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `is_default` TINYINT DEFAULT 0 COMMENT '默认地址：0-否 1-是',
    `receiver_name` VARCHAR(32) NOT NULL COMMENT '收货人',
    `receiver_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `province` VARCHAR(50) NOT NULL COMMENT '省份',
    `city` VARCHAR(50) NOT NULL COMMENT '城市',
    `district` VARCHAR(50) NOT NULL COMMENT '区/县',
    `detail_address` VARCHAR(200) NOT NULL COMMENT '详细地址',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`) ON DELETE CASCADE
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '用户收货地址表';

-- 7. 用户行为与交互表（合并行为表和交互表）
CREATE TABLE `user_behavior` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '行为ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `product_id` BIGINT COMMENT '商品ID',
    `category_id` BIGINT COMMENT '分类ID',
    `behavior_type` TINYINT NOT NULL COMMENT '行为类型：1-浏览 2-点击 3-加入购物车 4-收藏 5-搜索 6-评分 7-评价',
    `search_keyword` VARCHAR(255) COMMENT '搜索关键词',
    `stay_time` INT COMMENT '停留时间(秒)',
    `rating` DECIMAL(2,1) COMMENT '评分：1-5分',
    `review_content` TEXT COMMENT '评价内容',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `cart_count` INT DEFAULT 0 COMMENT '加购次数',
    `buy_count` INT DEFAULT 0 COMMENT '购买次数',
    `ip_address` VARCHAR(64) COMMENT 'IP地址',
    `user_agent` VARCHAR(512) COMMENT '用户设备信息',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_product` (`user_id`, `product_id`),
    INDEX `idx_product_behavior` (`product_id`, `behavior_type`),
    INDEX `idx_behavior_time` (`behavior_type`, `create_time`),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES products (`id`) ON DELETE SET NULL,
    FOREIGN KEY (`category_id`) REFERENCES category (`id`) ON DELETE SET NULL
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '用户行为与交互表';

-- 8. 购物车表
CREATE TABLE `cart` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `sku_id` BIGINT COMMENT 'SKU ID',
    `quantity` INT NOT NULL COMMENT '商品数量',
    `checked` TINYINT DEFAULT 1 COMMENT '选中状态：0-未选中 1-已选中',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_user_product_sku` (`user_id`, `product_id`, `sku_id`),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES products (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`sku_id`) REFERENCES product_sku (`id`) ON DELETE CASCADE
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '购物车表';

-- 9. 订单表
CREATE TABLE orders (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `total_amount` DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
    `status` TINYINT COMMENT '订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消',
    `receiver_name` VARCHAR(32) NOT NULL COMMENT '收货人姓名',
    `receiver_phone` VARCHAR(11) NOT NULL COMMENT '收货人电话',
    `receiver_address` VARCHAR(255) NOT NULL COMMENT '收货地址',
    `coupon_id` BIGINT COMMENT '使用的优惠券ID',
    `discount_amount` DECIMAL(10, 2) DEFAULT 0.00 COMMENT '优惠金额',
    `payment_time` TIMESTAMP NULL COMMENT '支付时间',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_order_no` (`order_no`),
    INDEX `idx_user_id` (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`) ON DELETE RESTRICT
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '订单表';

-- 10. 订单项表
CREATE TABLE `order_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单商品ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `sku_id` BIGINT COMMENT 'SKU ID',
    `product_name` VARCHAR(128) NOT NULL COMMENT '商品名称',
    `product_image` VARCHAR(255) COMMENT '商品主图URL',
    `specifications` JSON COMMENT '商品规格JSON',
    `price` DECIMAL(10, 2) NOT NULL COMMENT '商品单价',
    `quantity` INT NOT NULL COMMENT '购买数量',
    `total_amount` DECIMAL(10, 2) NOT NULL COMMENT '商品总价',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_order_id` (`order_id`),
    FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES products (`id`) ON DELETE RESTRICT,
    FOREIGN KEY (`sku_id`) REFERENCES product_sku (`id`) ON DELETE SET NULL
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '订单商品表';

-- 11. 优惠券表
CREATE TABLE `coupon` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
    `name` VARCHAR(100) NOT NULL COMMENT '优惠券名称',
    `type` TINYINT NOT NULL COMMENT '类型：1-满减 2-折扣 3-无门槛',
    `amount` DECIMAL(10, 2) COMMENT '优惠金额/折扣率',
    `threshold` DECIMAL(10, 2) DEFAULT 0 COMMENT '使用门槛金额',
    `start_time` TIMESTAMP NOT NULL COMMENT '开始时间',
    `end_time` TIMESTAMP NOT NULL COMMENT '结束时间',
    `total` INT NOT NULL COMMENT '总数量',
    `remain` INT NOT NULL COMMENT '剩余数量',
    `category_limit` JSON COMMENT '分类限制JSON',
    `product_limit` JSON COMMENT '商品限制JSON',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_status_time` (`status`, `start_time`, `end_time`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '优惠券表';

-- 12. 用户优惠券表
CREATE TABLE `user_coupon` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户优惠券ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `coupon_id` BIGINT NOT NULL COMMENT '优惠券ID',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-未使用 1-已使用 2-已过期',
    `order_id` BIGINT COMMENT '使用订单ID',
    `get_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '获取时间',
    `use_time` TIMESTAMP COMMENT '使用时间',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_status` (`user_id`, `status`),
    INDEX `idx_coupon_id` (`coupon_id`),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`coupon_id`) REFERENCES coupon (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`order_id`) REFERENCES orders (`id`) ON DELETE SET NULL
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '用户优惠券表';

-- 13. 用户相似度表（添加完整字段注释）
CREATE TABLE user_similarity (
    user_id_a BIGINT NOT NULL COMMENT '用户A ID',
    user_id_b BIGINT NOT NULL COMMENT '用户B ID',
    similarity DECIMAL(8,7) NOT NULL COMMENT '相似度分数（0.0-1.0，余弦相似度计算）',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '最后计算时间',
    PRIMARY KEY (user_id_a, user_id_b),
    FOREIGN KEY (user_id_a) REFERENCES users(id),
    FOREIGN KEY (user_id_b) REFERENCES users(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='用户相似度表（用于协同过滤推荐，存储用户间行为相似度）';

-- 14. 商品相似度表（添加完整字段注释）
CREATE TABLE product_similarity (
    product_id_a BIGINT NOT NULL COMMENT '商品A ID',
    product_id_b BIGINT NOT NULL COMMENT '商品B ID',
    similarity DECIMAL(8,7) NOT NULL COMMENT '相似度分数（0.0-1.0，基于共同购买/浏览行为计算）',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '最后计算时间',
    PRIMARY KEY (product_id_a, product_id_b),
    FOREIGN KEY (product_id_a) REFERENCES products(id),
    FOREIGN KEY (product_id_b) REFERENCES products(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='商品相似度表（用于商品协同过滤推荐，存储商品间关联度）';

-- 15. 推荐结果表（添加完整字段注释）
CREATE TABLE recommendation_result (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '推荐记录ID',
    user_id BIGINT NOT NULL COMMENT '被推荐用户ID',
    product_id BIGINT NOT NULL COMMENT '推荐商品ID',
    score DECIMAL(10,6) NOT NULL COMMENT '推荐分数（根据算法模型生成）',
    algorithm_type TINYINT COMMENT '算法类型：1-基于用户的CF 2-基于物品的CF 3-混合CF 4-热门推荐 5-新品推荐',
    expire_time TIMESTAMP COMMENT '推荐结果过期时间',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_product (user_id, product_id),
    INDEX idx_user_score (user_id, score DESC),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='推荐结果表（存储实时推荐结果，包含算法类型和有效期）';
