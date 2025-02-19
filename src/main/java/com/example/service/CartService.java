package com.example.service;

import com.example.model.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.cart.CartDTO;
import java.util.List;

/**
* @author 31815
* @description 针对表【cart(购物车表)】的数据库操作Service
* @createDate 2025-02-18 23:44:32
*/
public interface CartService extends IService<Cart> {
    boolean addToCart(Long userId, CartDTO cartDTO);
    boolean updateQuantity(Long userId, Long cartId, Integer delta);
    boolean batchCheckItems(Long userId, List<Long> productIds, Integer checked);
    boolean clearChecked(Long userId);
    List<Cart> getUserCart(Long userId);
    int getCartItemCount(Long userId);
    boolean removeCartItems(Long userId, List<Long> cartIds);
}
