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

-- 13. 用户相似度表
CREATE TABLE user_similarity (
    user_id_a BIGINT NOT NULL,
    user_id_b BIGINT NOT NULL,
    similarity DECIMAL(8,7) NOT NULL,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id_a, user_id_b),
    FOREIGN KEY (user_id_a) REFERENCES users(id),
    FOREIGN KEY (user_id_b) REFERENCES users(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='用户相似度表';

-- 14. 商品相似度表
CREATE TABLE product_similarity (
    product_id_a BIGINT NOT NULL,
    product_id_b BIGINT NOT NULL,
    similarity DECIMAL(8,7) NOT NULL,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (product_id_a, product_id_b),
    FOREIGN KEY (product_id_a) REFERENCES products(id),
    FOREIGN KEY (product_id_b) REFERENCES products(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='商品相似度表';

-- 15. 推荐结果表
CREATE TABLE recommendation_result (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    score DECIMAL(10,6) NOT NULL,
    algorithm_type TINYINT COMMENT '1-基于用户CF 2-基于物品CF 3-混合CF',
    expire_time TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_product (user_id, product_id),
    INDEX idx_user_score (user_id, score DESC),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='推荐结果表';

-- 清空测试数据（如有必要）
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE recommendation_result;
TRUNCATE TABLE product_similarity;
TRUNCATE TABLE user_similarity;
TRUNCATE TABLE user_coupon;
TRUNCATE TABLE coupon;
TRUNCATE TABLE order_item;
TRUNCATE TABLE orders;
TRUNCATE TABLE cart;
TRUNCATE TABLE user_behavior;
TRUNCATE TABLE user_address;
TRUNCATE TABLE product_sku;
TRUNCATE TABLE products;
TRUNCATE TABLE product_tag;
TRUNCATE TABLE category;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- 插入用户数据
INSERT INTO users (username, password, nickname, phone, email, avatar, role, status, age_range, gender, consumption_level, activity_level, preferred_categories, tags)
VALUES 
('admin', '$2a$10$EofL0s8I9c0.XPOpsSH.7.radgKnjnZn4XDXs1.Ej9LtUAm.lo9re', '系统管理员', '13900000000', 'admin@mall.com', '/images/backend.png', 0, 1, '25-35', 1, 3, 3, '["电子产品","办公用品"]', '["技术","管理"]'),
('zhangwei', '$2a$10$EofL0s8I9c0.XPOpsSH.7.radgKnjnZn4XDXs1.Ej9LtUAm.lo9re', '张伟', '13801234567', 'zhangwei@example.com', '/images/backend.png', 1, 1, '18-24', 1, 2, 3, '["手机","电脑","游戏"]', '["学生","游戏玩家"]'),
('liyun', '$2a$10$EofL0s8I9c0.XPOpsSH.7.radgKnjnZn4XDXs1.Ej9LtUAm.lo9re', '李云', '13912345678', 'liyun@example.com', '/images/backend.png', 1, 1, '25-35', 2, 3, 2, '["女装","美妆","家居"]', '["职场","时尚"]'),
('store01', '$2a$10$EofL0s8I9c0.XPOpsSH.7.radgKnjnZn4XDXs1.Ej9LtUAm.lo9re', '数码旗舰店', '13923456789', 'store01@example.com', '/images/backend.png', 2, 1, '35-45', 1, 3, 3, '["数码","电子"]', '["商家","品牌"]');

-- 插入商品分类数据
INSERT INTO category (parent_id, name, image, level, status)
VALUES 
(NULL, '数码电子', '/images/backend.png', 1, 1),
(NULL, '服装鞋包', '/images/backend.png', 1, 1),
(NULL, '家用电器', '/images/backend.png', 1, 1),
(1, '手机', '/images/backend.png', 2, 1),
(1, '电脑办公', '/images/backend.png', 2, 1),
(2, '女装', '/images/backend.png', 2, 1),
(2, '男装', '/images/backend.png', 2, 1),
(3, '厨房电器', '/images/backend.png', 2, 1),
(3, '生活电器', '/images/backend.png', 2, 1),
(4, '智能手机', '/images/backend.png', 3, 1);

-- 插入商品标签数据
INSERT INTO product_tag (name, type)
VALUES 
('热销爆款', 1),
('新品上市', 1),
('限时优惠', 1),
('高端精选', 1),
('性价比高', 1),
('办公必备', 2),
('娱乐游戏', 2),
('日常生活', 2),
('户外活动', 2),
('节能环保', 3);

-- 插入商品数据
INSERT INTO products (category_id, name, description, price, stock, image_main, images, tags, status)
VALUES 
(10, 'X23 Pro智能手机', '全新一代旗舰手机，8K高清摄像，AI智能助手，快速充电技术', 5999.00, 500, '/images/backend.png', '["images/backend.png", "images/backend.png", "images/backend.png"]', '[{"id": 2, "weight": 0.9}, {"id": 4, "weight": 0.8}]', 1),
(10, 'Note12 轻薄手机', '6.7英寸全面屏，4800万像素四摄，5000mAh超大电池', 3299.00, 800, '/images/backend.png', '["images/backend.png", "images/backend.png"]', '[{"id": 1, "weight": 0.8}, {"id": 5, "weight": 0.9}]', 1),
(5, 'BookAir 轻薄笔记本', '14英寸全面屏，11代酷睿处理器，16G内存，512G固态硬盘', 5899.00, 300, '/images/backend.png', '["images/backend.png", "images/backend.png"]', '[{"id": 2, "weight": 0.8}, {"id": 6, "weight": 0.9}]', 1),
(5, 'GameBook Pro游戏本', '17.3英寸电竞屏，RTX3060显卡，32G内存，1TB固态硬盘', 8799.00, 200, '/images/backend.png', '["images/backend.png", "images/backend.png"]', '[{"id": 7, "weight": 0.9}, {"id": 4, "weight": 0.7}]', 1),
(6, '春季新款连衣裙', '2023春季新款，法式复古风，收腰显瘦，多色可选', 299.00, 1000, '/images/backend.png', '["images/backend.png", "images/backend.png"]', '[{"id": 2, "weight": 0.9}, {"id": 8, "weight": 0.7}]', 1),
(7, '男士商务休闲西装', '商务正装，修身剪裁，舒适面料，多色可选', 699.00, 500, '/images/backend.png', '["images/backend.png", "images/backend.png"]', '[{"id": 1, "weight": 0.7}, {"id": 6, "weight": 0.8}]', 1),
(8, '智能多功能电饭煲', '智能控温，多功能菜单，3L容量，不粘内胆', 399.00, 600, '/images/backend.png', '["images/backend.png", "images/backend.png"]', '[{"id": 5, "weight": 0.8}, {"id": 8, "weight": 0.9}]', 1),
(9, '无线吸尘器', '强劲吸力，60分钟续航，多种吸头，墙面收纳', 1299.00, 400, '/images/backend.png', '["images/backend.png", "images/backend.png"]', '[{"id": 5, "weight": 0.7}, {"id": 10, "weight": 0.9}]', 1),
(10, 'Y7 青春版手机', '6.5英寸水滴屏，后置三摄，大电池，人脸识别', 1699.00, 1000, '/images/backend.png', '["images/backend.png", "images/backend.png"]', '[{"id": 1, "weight": 0.7}, {"id": 5, "weight": 0.9}]', 1),
(5, '超薄平板电脑Pro', '11英寸视网膜屏，高性能处理器，支持手写笔，长续航', 3899.00, 400, '/images/backend.png', '["images/backend.png", "images/backend.png"]', '[{"id": 2, "weight": 0.8}, {"id": 6, "weight": 0.7}]', 1);

-- 插入产品SKU数据
INSERT INTO product_sku (product_id, sku_code, specifications, price, stock, image)
VALUES 
(1, 'X23-BLK-256', '{"颜色":"星空黑","存储":"256GB"}', 5999.00, 200, '/images/backend.png'),
(1, 'X23-BLK-512', '{"颜色":"星空黑","存储":"512GB"}', 6499.00, 150, '/images/backend.png'),
(1, 'X23-WHT-256', '{"颜色":"珍珠白","存储":"256GB"}', 5999.00, 100, '/images/backend.png'),
(1, 'X23-WHT-512', '{"颜色":"珍珠白","存储":"512GB"}', 6499.00, 50, '/images/backend.png'),
(2, 'N12-BLU-128', '{"颜色":"极光蓝","存储":"128GB"}', 3299.00, 300, '/images/backend.png'),
(2, 'N12-BLU-256', '{"颜色":"极光蓝","存储":"256GB"}', 3699.00, 200, '/images/backend.png'),
(2, 'N12-GRN-128', '{"颜色":"翡翠绿","存储":"128GB"}', 3299.00, 200, '/images/backend.png'),
(2, 'N12-GRN-256', '{"颜色":"翡翠绿","存储":"256GB"}', 3699.00, 100, '/images/backend.png'),
(3, 'BA-SLV-8-512', '{"颜色":"银色","内存":"8GB","硬盘":"512GB"}', 5499.00, 150, '/images/backend.png'),
(3, 'BA-SLV-16-512', '{"颜色":"银色","内存":"16GB","硬盘":"512GB"}', 5899.00, 150, '/images/backend.png');

-- 插入用户地址数据
INSERT INTO user_address (user_id, is_default, receiver_name, receiver_phone, province, city, district, detail_address)
VALUES 
(2, 1, '张伟', '13801234567', '北京市', '北京市', '海淀区', '中关村科技园区8号楼606室'),
(2, 0, '张伟', '13801234567', '广东省', '深圳市', '南山区', '科技园南区15栋201室'),
(2, 0, '张伟亲属', '13709876543', '北京市', '北京市', '朝阳区', '朝阳公园南路12号2单元502室'),
(3, 1, '李云', '13912345678', '上海市', '上海市', '浦东新区', '陆家嘴金融中心3号楼1503室'),
(3, 0, '李云', '13912345678', '浙江省', '杭州市', '西湖区', '文三西路52号西溪诚园3幢2单元301室');

-- 插入用户行为数据
INSERT INTO user_behavior (user_id, product_id, category_id, behavior_type, stay_time, rating, review_content, view_count, cart_count, ip_address, user_agent)
VALUES 
(2, 1, 10, 1, 180, NULL, NULL, 8, 0, '192.168.0.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'),
(2, 3, 5, 1, 240, NULL, NULL, 5, 0, '192.168.0.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'),
(2, 4, 5, 1, 320, NULL, NULL, 6, 0, '192.168.0.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'),
(2, 1, 10, 3, NULL, NULL, NULL, 8, 1, '192.168.0.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'),
(2, 4, 5, 4, NULL, NULL, NULL, 6, 0, '192.168.0.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'),
(2, 9, 10, 5, 120, NULL, NULL, 3, 0, '192.168.0.101', 'Mozilla/5.0 (Mobile)'),
(2, 4, 5, 6, NULL, 4.8, '这款游戏本性能非常强劲，吃鸡和大型游戏都很流畅，散热也不错，推荐购买！', 6, 0, '192.168.0.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'),
(3, 5, 6, 1, 150, NULL, NULL, 7, 0, '192.168.0.102', 'Mozilla/5.0 (Mobile)'),
(3, 7, 8, 1, 90, NULL, NULL, 3, 0, '192.168.0.102', 'Mozilla/5.0 (Mobile)'),
(3, 5, 6, 3, NULL, NULL, NULL, 7, 1, '192.168.0.102', 'Mozilla/5.0 (Mobile)');

-- 插入购物车数据
INSERT INTO cart (user_id, product_id, sku_id, quantity, checked)
VALUES 
(2, 1, 1, 1, 1),
(2, 4, NULL, 1, 1),
(3, 5, NULL, 2, 1),
(3, 7, NULL, 1, 0),
(3, 8, NULL, 1, 0);

-- 插入订单数据
INSERT INTO orders (order_no, user_id, total_amount, status, receiver_name, receiver_phone, receiver_address, coupon_id, discount_amount, payment_time)
VALUES 
('2023052912345601', 2, 14298.00, 3, '张伟', '13801234567', '北京市海淀区中关村科技园区8号楼606室', 3, 500.00, '2023-05-29 14:30:00'),
('2023060112345602', 2, 3299.00, 2, '张伟', '13801234567', '北京市海淀区中关村科技园区8号楼606室', 9, 20.00, '2023-06-01 10:45:00'),
('2023060512345603', 3, 998.00, 1, '李云', '13912345678', '上海市浦东新区陆家嘴金融中心3号楼1503室', 1, 0.00, '2023-06-05 16:20:00'),
('2023060812345604', 3, 1299.00, 0, '李云', '13912345678', '上海市浦东新区陆家嘴金融中心3号楼1503室', NULL, 0.00, NULL);

-- 插入订单项数据
INSERT INTO order_item (order_id, product_id, sku_id, product_name, product_image, specifications, price, quantity, total_amount)
VALUES 
(1, 1, 1, 'X23 Pro智能手机', '/images/backend.png', '{"颜色":"星空黑","存储":"256GB"}', 5999.00, 1, 5999.00),
(1, 4, NULL, 'GameBook Pro游戏本', '/images/backend.png', NULL, 8799.00, 1, 8799.00),
(2, 2, 5, 'Note12 轻薄手机', '/images/backend.png', '{"颜色":"极光蓝","存储":"128GB"}', 3299.00, 1, 3299.00),
(3, 5, NULL, '春季新款连衣裙', '/images/backend.png', NULL, 299.00, 2, 598.00),
(3, 7, NULL, '智能多功能电饭煲', '/images/backend.png', NULL, 399.00, 1, 399.00),
(4, 8, NULL, '无线吸尘器', '/images/backend.png', NULL, 1299.00, 1, 1299.00);

-- 插入优惠券数据
INSERT INTO coupon (name, type, amount, threshold, start_time, end_time, total, remain, category_limit, product_limit, status)
VALUES 
('满500减50', 1, 50.00, 500.00, '2023-05-01 00:00:00', '2023-12-31 23:59:59', 5000, 4500, NULL, NULL, 1),
('满1000减100', 1, 100.00, 1000.00, '2023-05-01 00:00:00', '2023-12-31 23:59:59', 3000, 2700, NULL, NULL, 1),
('满5000减500', 1, 500.00, 5000.00, '2023-05-01 00:00:00', '2023-12-31 23:59:59', 1000, 850, NULL, NULL, 1),
('数码电子9折', 2, 0.90, 0.00, '2023-05-01 00:00:00', '2023-08-31 23:59:59', 2000, 1800, '[1]', NULL, 1),
('服装8.5折', 2, 0.85, 0.00, '2023-05-01 00:00:00', '2023-08-31 23:59:59', 2000, 1700, '[2]', NULL, 1),
('家电满1000减150', 1, 150.00, 1000.00, '2023-05-01 00:00:00', '2023-12-31 23:59:59', 1500, 1350, '[3]', NULL, 1),
('新用户专享50元券', 3, 50.00, 0.00, '2023-05-01 00:00:00', '2023-12-31 23:59:59', 10000, 9500, NULL, NULL, 1),
('618活动满1000减150', 1, 150.00, 1000.00, '2023-06-01 00:00:00', '2023-06-20 23:59:59', 5000, 4500, NULL, NULL, 1),
('新人首单立减20', 3, 20.00, 0.00, '2023-05-01 00:00:00', '2023-12-31 23:59:59', 8000, 7000, NULL, NULL, 1),
('手机品类满3000减300', 1, 300.00, 3000.00, '2023-05-01 00:00:00', '2023-12-31 23:59:59', 2000, 1800, NULL, '[1, 2, 9]', 1);

-- 插入用户优惠券数据
INSERT INTO user_coupon (user_id, coupon_id, status, order_id, get_time, use_time)
VALUES 
(2, 1, 0, NULL, '2023-05-20 09:15:00', NULL),
(2, 3, 1, 1, '2023-05-25 14:20:00', '2023-05-29 14:30:00'),
(2, 9, 1, 2, '2023-05-30 08:30:00', '2023-06-01 10:45:00'),
(2, 4, 0, NULL, '2023-05-25 14:20:00', NULL),
(3, 1, 0, NULL, '2023-06-01 10:30:00', NULL),
(3, 5, 0, NULL, '2023-06-01 10:30:00', NULL),
(3, 7, 0, NULL, '2023-06-01 10:30:00', NULL),
(3, 8, 0, NULL, '2023-06-02 16:45:00', NULL);

-- 插入用户相似度数据
INSERT INTO user_similarity (user_id_a, user_id_b, similarity)
VALUES 
(2, 3, 0.6745231),
(3, 2, 0.6745231),
(2, 4, 0.4231567),
(4, 2, 0.4231567),
(3, 4, 0.3567812),
(4, 3, 0.3567812);

-- 插入商品相似度数据
INSERT INTO product_similarity (product_id_a, product_id_b, similarity)
VALUES 
(1, 2, 0.8956742),
(2, 1, 0.8956742),
(1, 9, 0.7865321),
(9, 1, 0.7865321),
(3, 4, 0.7654321),
(4, 3, 0.7654321),
(3, 10, 0.6542387),
(10, 3, 0.6542387),
(5, 6, 0.3456789),
(6, 5, 0.3456789);

-- 插入推荐结果数据
INSERT INTO recommendation_result (user_id, product_id, score, algorithm_type, expire_time)
VALUES 
(2, 2, 0.9245678, 1, '2023-07-01 00:00:00'),
(2, 9, 0.8765432, 1, '2023-07-01 00:00:00'),
(2, 10, 0.7891234, 2, '2023-07-01 00:00:00'),
(2, 8, 0.6789543, 3, '2023-07-01 00:00:00'),
(3, 1, 0.8976543, 1, '2023-07-01 00:00:00'),
(3, 8, 0.8345678, 2, '2023-07-01 00:00:00'),
(3, 10, 0.7456789, 2, '2023-07-01 00:00:00'),
(3, 3, 0.6987654, 3, '2023-07-01 00:00:00');

-- 更新用户头像
UPDATE users SET avatar = '/images/backend.png';

-- 更新商品分类图片
UPDATE category SET image = '/images/backend.png';

-- 更新商品主图
UPDATE products SET image_main = '/images/backend.png';

-- 更新商品图片集合
UPDATE products SET images = '["images/backend.png", "images/backend.png", "images/backend.png"]';

-- 更新SKU图片
UPDATE product_sku SET image = '/images/backend.png';

-- 更新订单项商品图片
UPDATE order_item SET product_image = '/images/backend.png';