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
CREATE TABLE users
(
    `id`          BIGINT           NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`    VARCHAR(64)      NOT NULL COMMENT '用户名',
    `password`    VARCHAR(68)      NOT NULL COMMENT 'BCrypt加密后的密码哈希值',
    `nickname`    VARCHAR(64) COMMENT '昵称',
    `phone`       VARCHAR(11) COMMENT '手机号',
    `email`       VARCHAR(128) COMMENT '邮箱',
    `avatar`      VARCHAR(255)              DEFAULT '/images/backend.png' COMMENT '头像',
    `gender`      TINYINT                   DEFAULT 0 COMMENT '性别：0未知 1男 2女',
    `status`      TINYINT                   DEFAULT 1 COMMENT '状态：0禁用 1启用',
    `role`        TINYINT UNSIGNED NOT NULL DEFAULT 1 CHECK (
        `role` IN (0, 1, 2, 9)),
    `create_time` TIMESTAMP                 DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time` TIMESTAMP                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_username` (`username`),
    UNIQUE INDEX `idx_phone` (`phone`),
    UNIQUE INDEX `idx_email` (`email`),
    INDEX idx_search (username, phone, STATUS),
    INDEX idx_create_time (create_time)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户表';
-- 2. 商品分类表
CREATE TABLE `category`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `parent_id`   BIGINT COMMENT '父分类ID',
    `name`        VARCHAR(64) NOT NULL COMMENT '分类名称',
    `icon`        VARCHAR(255) COMMENT '分类图标',
    `level`       TINYINT     NOT NULL COMMENT '层级：1一级 2二级 3三级',
    `sort`        INT       DEFAULT 0 COMMENT '排序',
    `status`      TINYINT   DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    PRIMARY KEY (`id`),
    INDEX `idx_parent_id` (`parent_id`),
    INDEX idx_parent_status (parent_id, STATUS),
    INDEX idx_level_sort (LEVEL, sort)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '商品分类表';
-- 3. 商品表（使用JSON类型）
CREATE TABLE products
(
    `id`          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `category_id` BIGINT         NOT NULL COMMENT '分类ID',
    `name`        VARCHAR(100)   NOT NULL COMMENT '商品名称',
    `description` TEXT COMMENT '商品描述',
    `price`       DECIMAL(10, 2) NOT NULL COMMENT '价格',
    `stock`       INT       DEFAULT 0 COMMENT '库存',
    `images`      JSON COMMENT '商品图片JSON数组',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    `status`      TINYINT   DEFAULT 1 COMMENT '商品状态：0-下架 1-上架',
    PRIMARY KEY (`id`),
    INDEX idx_products_search (category_id, NAME, price),
    FOREIGN KEY (`category_id`) REFERENCES category (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '商品表';
-- 4. 优惠券表
CREATE TABLE `coupon`
(
    `id`          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
    `name`        VARCHAR(32)    NOT NULL COMMENT '优惠券名称',
    `type`        TINYINT        NOT NULL COMMENT '优惠券类型：1-满减券 2-折扣券',
    `value`       DECIMAL(10, 2) NOT NULL COMMENT '优惠券面值',
    `min_amount`  DECIMAL(10, 2) NOT NULL COMMENT '使用门槛',
    `start_time`  TIMESTAMP      NOT NULL COMMENT '生效时间',
    `end_time`    TIMESTAMP      NOT NULL COMMENT '失效时间',
    `status`      TINYINT   DEFAULT 1 COMMENT '状态：0-失效 1-生效',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    PRIMARY KEY (`id`),
    INDEX idx_status_time (STATUS, end_time),
    INDEX idx_type (type)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '优惠券表';
-- 5. 用户地址表
CREATE TABLE user_address
(
    `id`              BIGINT       NOT NULL AUTO_INCREMENT,
    `user_id`         BIGINT       NOT NULL,
    `is_default`      TINYINT COMMENT '默认地址状态：0-非默认 1-默认',
    `is_default_true` TINYINT GENERATED ALWAYS AS (
                          IF
                          (is_default = 1, 1, NULL)) VIRTUAL,
    `receiver_name`   VARCHAR(32)  NOT NULL,
    `receiver_phone`  VARCHAR(20)  NOT NULL,
    `province`        VARCHAR(50)  NOT NULL,
    `city`            VARCHAR(50)  NOT NULL,
    `district`        VARCHAR(50)  NOT NULL,
    `detail_address`  VARCHAR(200) NOT NULL,
    `create_time`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`),
    UNIQUE INDEX `uk_user_default` (`user_id`, `is_default_true`),
    INDEX `idx_receiver_name` (`receiver_name`),
    INDEX `idx_receiver_phone` (`receiver_phone`),
    INDEX `idx_province` (`province`),
    INDEX `idx_city` (`city`),
    INDEX `idx_district` (`district`),
    INDEX `idx_detail_address` (`detail_address`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户收货地址表';
-- 6. 收藏夹表
CREATE TABLE `favorite_folder`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '收藏夹ID',
    `user_id`     BIGINT      NOT NULL COMMENT '用户ID',
    `name`        VARCHAR(32) NOT NULL COMMENT '收藏夹名称',
    `description` VARCHAR(128) COMMENT '收藏夹描述',
    `is_public`   TINYINT   DEFAULT 0 COMMENT '公开状态：0-私密 1-公开',
    `item_count`  INT       DEFAULT 0 COMMENT '收藏项数量',
    `sort`        INT       DEFAULT 0 COMMENT '排序',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX idx_user_public (user_id, is_public),
    INDEX idx_sort (sort),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`) ON DELETE CASCADE
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '收藏夹表';
-- 7. 购物车表
CREATE TABLE `cart`
(
    `id`          BIGINT NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
    `user_id`     BIGINT NOT NULL COMMENT '用户ID',
    `product_id`  BIGINT NOT NULL COMMENT '商品ID',
    `quantity`    INT    NOT NULL COMMENT '商品数量',
    `checked`     TINYINT   DEFAULT 1 COMMENT '选中状态：0-未选中 1-已选中',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX idx_user_checked (user_id, checked),
    INDEX idx_product (product_id),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`),
    FOREIGN KEY (`product_id`) REFERENCES products (`id`),
    UNIQUE INDEX `uk_user_product` (`user_id`, `product_id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '购物车表';
-- 8. 订单表（完整状态字段）
CREATE TABLE orders
(
    `id`                BIGINT         NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no`          VARCHAR(64)    NOT NULL COMMENT '订单号',
    `user_id`           BIGINT         NOT NULL COMMENT '用户ID',
    `total_amount`      DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
    `pay_amount`        DECIMAL(10, 2) NOT NULL COMMENT '实付金额',
    `status`            TINYINT COMMENT '订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消 5-退款中',
    `receiver_name`     VARCHAR(32)    NOT NULL COMMENT '收货人姓名',
    `receiver_phone`    VARCHAR(11)    NOT NULL COMMENT '收货人电话',
    `receiver_address`  VARCHAR(255)   NOT NULL COMMENT '收货地址',
    `payment_time`      TIMESTAMP COMMENT '支付时间',
    `delivery_time`     TIMESTAMP COMMENT '发货时间',
    `receive_time`      TIMESTAMP COMMENT '收货时间',
    `payment_method`    TINYINT COMMENT '支付方式：1-支付宝 2-微信 3-银联',
    `logistics_company` VARCHAR(64) COMMENT '物流公司',
    `tracking_number`   VARCHAR(64) COMMENT '运单号',
    `comment_status`    TINYINT     DEFAULT 0 COMMENT '评价状态：0未评价 1已评价',
    `create_time`       TIMESTAMP   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time`       TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    `timezone`          VARCHAR(50) DEFAULT '+08:00' COMMENT '时区信息',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_order_no` (`order_no`),
    INDEX idx_orders_user_status (`user_id`, `status`),
    INDEX idx_payment_time (`payment_time`),
    INDEX idx_logistics (`logistics_company`, `tracking_number`),
    INDEX idx_user_status (user_id, STATUS),
    INDEX idx_create_time (create_time),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '订单表';
-- 9. 订单项表（级联删除）
CREATE TABLE `order_item`
(
    `id`            BIGINT         NOT NULL AUTO_INCREMENT COMMENT '订单商品ID',
    `order_id`      BIGINT         NOT NULL COMMENT '订单ID',
    `product_id`    BIGINT         NOT NULL COMMENT '商品ID',
    `product_name`  VARCHAR(128)   NOT NULL COMMENT '商品名称',
    `product_image` VARCHAR(255) COMMENT '商品主图URL',
    `price`         DECIMAL(10, 2) NOT NULL COMMENT '商品单价',
    `quantity`      INT            NOT NULL COMMENT '购买数量',
    `total_amount`  DECIMAL(10, 2) NOT NULL COMMENT '商品总价',
    `create_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    PRIMARY KEY (`id`),
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_product_id` (`product_id`),
    FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES products (`id`) ON DELETE CASCADE
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '订单商品表';
-- 10. 用户优惠券表（依赖用户、优惠券）
CREATE TABLE `user_coupon`
(
    `id`          BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户优惠券ID',
    `user_id`     BIGINT NOT NULL COMMENT '用户ID',
    `coupon_id`   BIGINT NOT NULL COMMENT '优惠券ID',
    `status`      TINYINT COMMENT '用户优惠券状态：0-未使用 1-已使用 2-已过期',
    `order_id`    BIGINT COMMENT '使用的订单ID',
    `get_time`    TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
    `use_time`    TIMESTAMP COMMENT '使用时间',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_coupon_id` (`coupon_id`),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`coupon_id`) REFERENCES coupon (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE
        SET NULL
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户优惠券表';
-- 11. 商品规格表（依赖商品）
CREATE TABLE `product_spec`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '规格ID',
    `product_id`  BIGINT      NOT NULL COMMENT '商品ID',
    `spec_name`   VARCHAR(64) NOT NULL COMMENT '规格名称',
    `spec_values` TEXT        NOT NULL COMMENT '规格值，JSON格式',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    PRIMARY KEY (`id`),
    INDEX `idx_product_id` (`product_id`),
    FOREIGN KEY (`product_id`) REFERENCES products (`id`) ON DELETE CASCADE
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '商品规格表';
-- 12. 商品SKU表（带默认主图）
CREATE TABLE `product_sku`
(
    `id`          BIGINT         NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
    `product_id`  BIGINT         NOT NULL COMMENT '商品ID',
    `spec_values` VARCHAR(255)   NOT NULL COMMENT '规格值组合（JSON）',
    `price`       DECIMAL(10, 2) NOT NULL COMMENT '价格',
    `stock`       INT            NOT NULL COMMENT '库存',
    `sales`       INT          DEFAULT 0 COMMENT '销量',
    `main_image`  VARCHAR(255) DEFAULT '/images/backend.png' COMMENT 'SKU主图',
    `status`      TINYINT COMMENT 'SKU状态：0-下架 1-上架',
    `create_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    PRIMARY KEY (`id`),
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_status_price` (`status`, `price`),
    INDEX `idx_sales` (`sales`),
    INDEX `idx_price_stock_status` (`price`, `stock`, `status`),
    FOREIGN KEY (`product_id`) REFERENCES products (`id`) ON DELETE CASCADE
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '商品SKU表';
-- 13. 商品评价表（依赖订单、用户、商品）
CREATE TABLE `product_review`
(
    `id`          BIGINT  NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    `order_id`    BIGINT  NOT NULL COMMENT '订单ID',
    `user_id`     BIGINT  NOT NULL COMMENT '用户ID',
    `product_id`  BIGINT  NOT NULL COMMENT '商品ID',
    `rating`      TINYINT NOT NULL COMMENT '评分：1-5分',
    `content`     TEXT COMMENT '评价内容',
    `images`      TEXT COMMENT '评价图片，JSON格式',
    `status`      TINYINT COMMENT '审核状态：0-待审核 1-已通过 2-已拒绝',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    PRIMARY KEY (`id`),
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_product_id` (`product_id`),
    FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`),
    FOREIGN KEY (`product_id`) REFERENCES products (`id`),
    UNIQUE INDEX `uk_order_product` (`order_id`, `product_id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '商品评价表';-- 14. 商品收藏表
CREATE TABLE `product_favorite`
(
    `id`          BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `user_id`     BIGINT NOT NULL COMMENT '用户ID',
    `product_id`  BIGINT NOT NULL COMMENT '商品ID',
    `folder_id`   BIGINT COMMENT '收藏夹ID（NULL表示未分类）',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    FOREIGN KEY (`folder_id`) REFERENCES favorite_folder (`id`) ON DELETE
        SET NULL,
    FOREIGN KEY (`user_id`) REFERENCES users (`id`) ON DELETE CASCADE
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '商品收藏表';-- 15. 推荐商品表
CREATE TABLE `recommend_product`
(
    `id`                BIGINT      NOT NULL AUTO_INCREMENT COMMENT '推荐商品ID',
    `product_id`        BIGINT      NOT NULL COMMENT '商品ID',
    `type`              TINYINT     NOT NULL COMMENT '推荐类型：1-热门商品 2-新品推荐 3-算法生成',
    `sort`              INT         NOT NULL COMMENT '排序',
    `status`            TINYINT     NOT NULL COMMENT '推荐状态：0-未生效 1-生效中',
    `start_time`        TIMESTAMP   NOT NULL COMMENT '开始时间',
    `end_time`          TIMESTAMP   NOT NULL COMMENT '结束时间',
    `algorithm_version` VARCHAR(64) NOT NULL COMMENT '算法版本',
    `recommend_reason`  TEXT COMMENT '推荐理由',
    `create_time`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    PRIMARY KEY (`id`),
    INDEX `idx_product_id` (`product_id`),
    FOREIGN KEY (`product_id`) REFERENCES products (`id`) ON DELETE CASCADE
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '推荐商品表';-- 16. 用户行为表
CREATE TABLE user_behavior
(
    `id`            BIGINT        NOT NULL AUTO_INCREMENT COMMENT '行为记录ID',
    `user_id`       BIGINT        NOT NULL COMMENT '用户ID',
    `product_id`    BIGINT        NOT NULL COMMENT '商品ID',
    `behavior_type` TINYINT       NOT NULL COMMENT '行为类型：1-浏览 2-收藏 3-购买',
    `behavior_time` TIMESTAMP              DEFAULT CURRENT_TIMESTAMP COMMENT '行为时间',
    `duration`      INT                    DEFAULT 0 COMMENT '停留时长（秒）',
    `create_time`   TIMESTAMP              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（带时区）',
    `update_time`   TIMESTAMP              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（带时区）',
    `weight`        DECIMAL(3, 1) NOT NULL DEFAULT 0.5 COMMENT '行为权重',
    PRIMARY KEY (`id`),
    INDEX `idx_user_behavior` (`user_id`),
    INDEX `idx_behavior_time` (`behavior_time`),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`),
    FOREIGN KEY (`product_id`) REFERENCES products (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户行为记录表';