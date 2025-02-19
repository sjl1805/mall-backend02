package com.example.service;

import com.example.model.entity.Products;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.product.ProductsPageDTO;
import com.example.model.dto.product.ProductsDTO;
import java.util.List;

/**
* @author 31815
* @description 针对表【products(商品表)】的数据库操作Service
* @createDate 2025-02-18 23:44:03
*/
public interface ProductsService extends IService<Products> {
    IPage<Products> listProductsPage(ProductsPageDTO queryDTO);
    boolean addProduct(ProductsDTO productsDTO);
    boolean updateProduct(ProductsDTO productsDTO);
    boolean adjustStock(Long productId, Integer delta);
    boolean switchStatus(Long productId, Integer status);
    List<Products> getNewArrivals(Long categoryId, Integer limit);
}
