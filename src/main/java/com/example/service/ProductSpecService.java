package com.example.service;

import com.example.model.entity.ProductSpec;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.product.ProductSpecDTO;

import java.util.List;

/**
* @author 31815
* @description 针对表【product_spec(商品规格表)】的数据库操作Service
* @createDate 2025-02-18 23:44:05
*/
public interface ProductSpecService extends IService<ProductSpec> {
    boolean batchCreateSpecs(Long productId, List<ProductSpecDTO> specs);
    List<ProductSpec> getSpecsByProductId(Long productId);
    boolean updateSpecValues(Long productId, Long specId, String specValues);
    Integer getSpecCount(Long productId);
}
