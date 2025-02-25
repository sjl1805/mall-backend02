package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.ProductTag;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品标签Mapper接口
 */
@Mapper
public interface ProductTagMapper extends BaseMapper<ProductTag> {

    /**
     * 根据标签名称查询标签
     *
     * @param name 标签名称
     * @return 标签对象
     */
    ProductTag selectByName(String name);

    /**
     * 根据标签类型查询标签列表
     *
     * @param type 标签类型
     * @return 标签列表
     */
    List<ProductTag> selectListByType(Integer type);

    /**
     * 分页查询标签（支持多条件筛选）
     *
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页标签列表
     */
    IPage<ProductTag> selectTagPage(Page<ProductTag> page, @Param("params") Map<String, Object> params);

    /**
     * 批量插入标签
     *
     * @param tagList 标签列表
     * @return 影响行数
     */
    int batchInsert(@Param("tagList") List<ProductTag> tagList);

    /**
     * 根据名称模糊查询标签
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 标签列表
     */
    List<ProductTag> searchByKeyword(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 获取热门标签（根据使用频率）
     *
     * @param limit 限制数量
     * @return 热门标签列表
     */
    List<ProductTag> selectHotTags(@Param("limit") Integer limit);

    /**
     * 统计各类型标签数量
     *
     * @return 统计结果
     */
    @MapKey("type")
    List<Map<String, Object>> countTagByType();

    /**
     * 查询标签是否存在
     *
     * @param name 标签名称
     * @return 存在数量
     */
    int existsByName(String name);
    
    /**
     * 根据商品ID获取关联的标签
     *
     * @param productId 商品ID
     * @return 标签列表
     */
    List<ProductTag> selectTagsByProductId(@Param("productId") Long productId);
    
    /**
     * 批量删除标签
     *
     * @param ids 标签ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("ids") List<Long> ids);
} 