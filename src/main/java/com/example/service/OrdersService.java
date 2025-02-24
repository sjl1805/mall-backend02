package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Orders;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【orders(订单表)】的数据库操作Service
 * @createDate 2025-02-24 12:04:10
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 根据用户ID查询订单
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Orders> selectByUserId(Long userId);

    /**
     * 分页查询订单
     * @param page 分页信息
     * @return 订单列表
     */
    IPage<Orders> selectPage(IPage<Orders> page);

    /**
     * 根据ID查询订单
     * @param id 订单ID
     * @return 订单信息
     */
    Orders selectById(Long id);

    /**
     * 新增订单
     * @param orders 订单信息
     * @return 插入结果
     */
    boolean insertOrder(Orders orders);

    /**
     * 更新订单信息
     * @param orders 订单信息
     * @return 更新结果
     */
    boolean updateOrder(Orders orders);

    /**
     * 根据ID删除订单
     * @param id 订单ID
     * @return 删除结果
     */
    boolean deleteOrder(Long id);
}
