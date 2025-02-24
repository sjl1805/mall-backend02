package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.entity.RecommendProduct;
import com.example.service.RecommendProductService;
import com.example.mapper.RecommendProductMapper;
import org.springframework.stereotype.Service;

/**
* @author 31815
* @description 针对表【recommend_product(推荐商品表)】的数据库操作Service实现
* @createDate 2025-02-24 12:03:53
*/
@Service
public class RecommendProductServiceImpl extends ServiceImpl<RecommendProductMapper, RecommendProduct>
    implements RecommendProductService{

}




