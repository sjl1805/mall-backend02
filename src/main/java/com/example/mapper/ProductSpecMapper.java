package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.ProductSpec;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* @author 31815
* @description 针对表【product_spec(商品规格表)】的数据库操作Mapper
* @createDate 2025-02-18 23:44:05
* @Entity model.entity.ProductSpec
*/
public interface ProductSpecMapper extends BaseMapper<ProductSpec> {

    /**
     * 批量插入商品规格
     * @param specs 规格列表
     * @return 插入数量
     */
    int batchInsert(@Param("specs") List<ProductSpec> specs);

    /**
     * 根据商品ID查询规格
     * @param productId 商品ID
     * @return 规格列表
     */
    List<ProductSpec> selectByProductId(@Param("productId") Long productId);

    /**
     * 更新规格值
     * @param specId 规格ID
     * @param specValues 新规格值
     * @return 影响行数
     */
    int updateSpecValues(@Param("specId") Long specId, 
                        @Param("specValues") String specValues);

    /**
     * 统计商品规格数量
     * @param productId 商品ID
     * @return 规格数量
     */
    int countByProductId(@Param("productId") Long productId);
}




