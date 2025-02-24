package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.ProductSpec;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_spec(商品规格表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:58
 * @Entity model.entity.ProductSpec
 */
public interface ProductSpecMapper extends BaseMapper<ProductSpec> {

    /**
     * 批量更新规格值
     * @param ids 规格ID列表
     * @param spec 包含更新字段的规格对象
     * @return 影响行数
     */
    int updateSpecValuesBatch(@Param("ids") List<Long> ids, 
                             @Param("spec") ProductSpec spec);

    /**
     * 更新规格状态
     * @param specId 规格ID
     * @param status 新状态
     * @return 影响行数
     */
    int updateSpecStatus(@Param("specId") Long specId, 
                        @Param("status") Integer status);

    /**
     * 调整规格排序
     * @param productId 商品ID
     * @param sortedIds 排序后的规格ID列表
     * @return 影响行数
     */
    int updateSpecSort(@Param("productId") Long productId,
                      @Param("sortedIds") List<Long> sortedIds);

}




