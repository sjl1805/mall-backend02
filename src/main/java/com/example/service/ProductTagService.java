package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.ProductTag;

import java.util.List;

/**
 * 商品标签服务接口
 */
public interface ProductTagService extends IService<ProductTag> {
    
    /**
     * 根据标签类型获取标签列表
     *
     * @param type 标签类型
     * @return 标签列表
     */
    List<ProductTag> getTagsByType(Integer type);
    
    /**
     * 添加标签
     *
     * @param tag 标签信息
     * @return 添加成功的标签
     */
    ProductTag addTag(ProductTag tag);
    
    /**
     * 更新标签
     *
     * @param tag 标签信息
     * @return 是否更新成功
     */
    boolean updateTag(ProductTag tag);
    
    /**
     * 删除标签
     *
     * @param tagId 标签ID
     * @return 是否删除成功
     */
    boolean deleteTag(Long tagId);
} 