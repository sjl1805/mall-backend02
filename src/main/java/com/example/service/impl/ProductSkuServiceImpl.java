package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProductSkuMapper;
import com.example.model.entity.ProductSku;
import com.example.service.ProductSkuService;
import org.springframework.stereotype.Service;

/**
 * @author 31815
 * @description 针对表【product_sku(商品SKU表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:01
 */
@Service
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku>
        implements ProductSkuService {

}




