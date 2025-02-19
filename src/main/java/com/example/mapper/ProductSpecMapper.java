package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.product.ProductSpecDTO;
import com.example.model.dto.product.ProductSpecPageDTO;
import com.example.model.entity.ProductSpec;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 商品规格管理Mapper接口
 * 实现规格的批量操作、动态查询和版本控制
 * 
 * @author 毕业设计学生
 */
public interface ProductSpecMapper extends BaseMapper<ProductSpec> {

    /**
     * 批量插入规格（商品发布时使用）
     * 
     * @param specs 规格DTO列表（包含规格名称和值）
     * @return 成功插入的记录数
     */
    int batchInsert(@Param("specs") List<ProductSpecDTO> specs);

    /**
     * 根据商品ID查询规格（按创建时间排序）
     * 
     * @param productId 商品ID（必填）
     * @return 规格列表（包含规格名称和可选值）
     */
    List<ProductSpec> selectByProductId(@Param("productId") Long productId);

    /**
     * 更新规格值（管理员操作）
     * 
     * @param specId     规格ID（必填）
     * @param specValues 新规格值（JSON数组格式）
     * @return 影响的行数
     */
    int updateSpecValues(@Param("specId") Long specId,
                        @Param("specValues") String specValues);

    /**
     * 统计商品规格数量（用于商品管理页）
     * 
     * @param productId 商品ID（必填）
     * @return 该商品的规格总数
     */
    int countByProductId(@Param("productId") Long productId);

    /**
     * 分页查询规格（支持多条件过滤）
     * 
     * @param page     分页参数（包含当前页、每页数量）
     * @param queryDTO 查询条件DTO（包含规格名称、时间范围等）
     * @return 分页结果（包含规格列表和分页信息）
     */
    IPage<ProductSpec> selectSpecPage(IPage<ProductSpec> page,
                                     @Param("query") ProductSpecPageDTO queryDTO);

    /**
     * 安全更新规格值（带商品ID校验）
     * 
     * @param productId 商品ID（用于权限校验）
     * @param specId    规格ID（必填）
     * @param specValues 新规格值（JSON数组格式）
     * @return 影响的行数（0表示操作失败）
     */
    @Update("UPDATE product_spec SET spec_values = #{specValues} " +
            "WHERE id = #{specId} AND product_id = #{productId}")
    int updateSpecValuesSafely(@Param("productId") Long productId,
                              @Param("specId") Long specId,
                              @Param("specValues") String specValues);
}




