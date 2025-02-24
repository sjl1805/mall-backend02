package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProductFavoriteMapper;
import com.example.model.entity.ProductFavorite;
import com.example.service.ProductFavoriteService;
import org.springframework.stereotype.Service;

/**
 * @author 31815
 * @description 针对表【product_favorite(商品收藏表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:07
 */
@Service
public class ProductFavoriteServiceImpl extends ServiceImpl<ProductFavoriteMapper, ProductFavorite>
        implements ProductFavoriteService {

}




