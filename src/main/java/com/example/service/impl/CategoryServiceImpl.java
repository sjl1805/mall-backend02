package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.CategoryService;
import com.example.model.entity.Category;
import com.example.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 31815
* @description 针对表【category(商品分类表)】的数据库操作Service实现
* @createDate 2025-02-18 23:44:29
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {

}




