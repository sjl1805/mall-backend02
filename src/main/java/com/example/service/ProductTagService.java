package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.ProductTag;

import java.util.List;
import java.util.Map;

/**
 * 商品标签服务接口
 */
public interface ProductTagService extends IService<ProductTag> {

    /**
     * 根据标签名称查询标签
     *
     * @param name 标签名称
     * @return 标签对象
     */
    ProductTag getByName(String name);

    /**
     * 根据标签类型查询标签列表
     *
     * @param type 标签类型
     * @return 标签列表
     */
    List<ProductTag> getListByType(Integer type);

    /**
     * 分页查询标签
     *
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页标签列表
     */
    IPage<ProductTag> getTagPage(Page<ProductTag> page, Map<String, Object> params);

    /**
     * 创建标签
     *
     * @param productTag 标签信息
     * @return 创建的标签
     */
    ProductTag createTag(ProductTag productTag);

    /**
     * 更新标签
     *
     * @param productTag 标签信息
     * @return 是否更新成功
     */
    boolean updateTag(ProductTag productTag);

    /**
     * 删除标签
     *
     * @param id 标签ID
     * @return 是否删除成功
     */
    boolean deleteTag(Long id);

    /**
     * 批量创建标签
     *
     * @param tagList 标签列表
     * @return 是否创建成功
     */
    boolean batchCreateTags(List<ProductTag> tagList);

    /**
     * 批量删除标签
     *
     * @param ids 标签ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteTags(List<Long> ids);

    /**
     * 根据关键词搜索标签
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 标签列表
     */
    List<ProductTag> searchByKeyword(String keyword, Integer limit);

    /**
     * 获取热门标签
     *
     * @param limit 限制数量
     * @return 热门标签列表
     */
    List<ProductTag> getHotTags(Integer limit);

    /**
     * 统计各类型标签数量
     *
     * @return 统计结果
     */
    List<Map<String, Object>> countTagByType();

    /**
     * 检查标签名称是否存在
     *
     * @param name 标签名称
     * @return 是否存在
     */
    boolean checkNameExists(String name);

    /**
     * 根据商品ID获取关联的标签
     *
     * @param productId 商品ID
     * @return 标签列表
     */
    List<ProductTag> getTagsByProductId(Long productId);
} 