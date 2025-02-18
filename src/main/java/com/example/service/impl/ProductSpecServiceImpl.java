package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.ProductSpecService;
import com.example.model.entity.ProductSpec;
import com.example.mapper.ProductSpecMapper;
import org.springframework.stereotype.Service;

/**
* @author 31815
* @description 针对表【product_spec(商品规格表)】的数据库操作Service实现
* @createDate 2025-02-18 23:44:05
*/
@Service
public class ProductSpecServiceImpl extends ServiceImpl<ProductSpecMapper, ProductSpec>
    implements ProductSpecService {

}




