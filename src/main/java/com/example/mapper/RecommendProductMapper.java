package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.RecommendProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【recommend_product(推荐商品表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:53
 * @Entity model.entity.RecommendProduct
 */
public interface RecommendProductMapper extends BaseMapper<RecommendProduct> {

    /**
     * 分页查询推荐商品
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果（包含商品信息）
     */
    Page<RecommendProduct> selectRecommendPage(Page<RecommendProduct> page, 
                                            @Param("query") RecommendQuery query);
    
    /**
     * 批量更新推荐状态
     * @param ids 推荐记录ID列表
     * @param status 新状态
     * @return 影响行数
     */
    int updateStatusBatch(@Param("ids") List<Long> ids, 
                        @Param("status") Integer status);
    
    /**
     * 查询有效推荐商品
     * @param type 推荐类型（可选）
     * @param limit 数量限制
     * @return 有效推荐列表
     */
    List<RecommendProduct> selectValidRecommends(@Param("type") Integer type,
                                                @Param("limit") int limit);
    
    /**
     * 统计推荐效果
     * @return 按类型分组的统计结果
     */
    List<Map<String, Object>> selectRecommendStats();
    
    /**
     * 清理过期推荐记录
     * @return 删除行数
     */
    int cleanExpiredRecommends();

    class RecommendQuery {
        private Integer type;
        private Integer status;
        private String algorithmVersion;
    }
}




