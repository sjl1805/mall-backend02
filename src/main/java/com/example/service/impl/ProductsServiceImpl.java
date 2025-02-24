package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.entity.Products;
import com.example.service.ProductsService;
import com.example.mapper.ProductsMapper;
import org.springframework.stereotype.Service;

/**
* @author 31815
* @description 针对表【products(商品表)】的数据库操作Service实现
* @createDate 2025-02-24 12:03:56
*/
@Service
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products>
    implements ProductsService{

}




