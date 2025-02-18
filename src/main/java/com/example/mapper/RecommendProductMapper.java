package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.RecommendProduct;
import com.example.model.query.RecommendQuery;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
* @author 31815
* @description 针对表【recommend_product(推荐商品表)】的数据库操作Mapper
* @createDate 2025-02-18 23:44:00
* @Entity model.entity.RecommendProduct
*/
public interface RecommendProductMapper extends BaseMapper<RecommendProduct> {

    /**
     * 分页查询推荐商品
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<RecommendProduct> selectRecommendPage(IPage<RecommendProduct> page,
                                               @Param("query") RecommendQuery query);

    /**
     * 根据类型和状态查询推荐
     * @param type 推荐类型
     * @param status 推荐状态
     * @return 推荐列表
     */
    List<RecommendProduct> selectByTypeAndStatus(@Param("type") Integer type,
                                              @Param("status") Integer status);

    /**
     * 更新推荐状态
     * @param id 推荐ID
     * @param status 新状态
     * @return 影响行数
     */
    int updateRecommendStatus(@Param("id") Long id,
                            @Param("status") Integer status);

    /**
     * 统计有效推荐数量
     * @return 各类型推荐数量
     */
    List<Map<String, Object>> countActiveRecommends();

    /**
     * 检查时间冲突
     * @param productId 商品ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 冲突数量
     */
    int checkTimeConflict(@Param("productId") Long productId,
                        @Param("startTime") String startTime,
                        @Param("endTime") String endTime);

    /**
     * 根据算法版本查询推荐
     * @param version 算法版本
     * @return 推荐列表
     */
    List<RecommendProduct> selectByAlgorithmVersion(@Param("version") String version);
}




