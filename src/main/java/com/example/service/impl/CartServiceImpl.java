package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.entity.Cart;
import com.example.service.CartService;
import com.example.mapper.CartMapper;
import org.springframework.stereotype.Service;

/**
* @author 31815
* @description 针对表【cart(购物车表)】的数据库操作Service实现
* @createDate 2025-02-24 12:04:25
*/
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart>
    implements CartService{

}




