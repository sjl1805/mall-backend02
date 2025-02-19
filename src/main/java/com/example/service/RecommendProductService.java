package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.product.RecommendProductDTO;
import com.example.model.dto.product.RecommendProductPageDTO;
import com.example.model.entity.RecommendProduct;

import java.util.List;
import java.util.Map;

/**
 * 推荐商品服务接口
 * 
 * @author 31815
 * @description 提供推荐商品管理功能，包含：
 *              1. 推荐位管理
 *              2. 推荐时间控制
 *              3. 推荐排序与统计
 * @createDate 2025-02-18 23:44:00
 */
public interface RecommendProductService extends IService<RecommendProduct> {

    /**
     * 创建推荐（带时间校验）
     * @param recommendDTO 推荐信息，包含：
     *                      - productId: 商品ID（必须）
     *                      - type: 推荐类型（必须）
     *                      - startTime: 开始时间
     *                      - endTime: 结束时间
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当时间冲突时抛出
     */
    boolean createRecommend(RecommendProductDTO recommendDTO);

    /**
     * 分页查询推荐（带缓存）
     * @param queryDTO 分页参数：
     *                 - type: 推荐类型过滤
     *                 - status: 状态过滤
     * @return 分页结果（包含推荐详情）
     * @implNote 结果缓存优化，有效期10分钟
     */
    IPage<RecommendProduct> listRecommendPage(RecommendProductPageDTO queryDTO);

    /**
     * 更新推荐状态（管理端）
     * @param id 推荐记录ID
     * @param status 新状态（0-禁用，1-启用）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当推荐不存在时抛出
     */
    boolean updateRecommendStatus(Long id, Integer status);

    /**
     * 调整推荐排序（原子操作）
     * @param id 推荐记录ID
     * @param sort 新排序值（越大越靠前）
     * @return 操作是否成功
     */
    boolean updateRecommendSort(Long id, Integer sort);

    /**
     * 获取有效推荐（带缓存）
     * @param type 推荐类型
     * @return 推荐列表（按排序值倒序）
     * @implNote 结果缓存优化，有效期30分钟
     */
    List<RecommendProduct> getActiveRecommends(Integer type);

    /**
     * 获取推荐统计（带缓存）
     * @return 各类型推荐数量统计
     * @implNote 结果缓存优化，有效期2小时
     */
    Map<Integer, Long> getRecommendStats();

    /**
     * 批量创建推荐（事务操作）
     * @param recommends 推荐列表
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当存在时间冲突时抛出
     */
    boolean batchCreateRecommends(List<RecommendProductDTO> recommends);
}
