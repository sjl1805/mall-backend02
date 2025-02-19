package com.example.service;

import com.example.model.entity.RecommendProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.product.RecommendProductDTO;
import com.example.model.dto.product.RecommendProductPageDTO;
import java.util.List;
import java.util.Map;

/**
* @author 31815
* @description 针对表【recommend_product(推荐商品表)】的数据库操作Service
* @createDate 2025-02-18 23:44:00
*/
public interface RecommendProductService extends IService<RecommendProduct> {
    boolean createRecommend(RecommendProductDTO recommendDTO);
    IPage<RecommendProduct> listRecommendPage(RecommendProductPageDTO queryDTO);
    boolean updateRecommendStatus(Long id, Integer status);
    boolean updateRecommendSort(Long id, Integer sort);
    List<RecommendProduct> getActiveRecommends(Integer type);
    Map<Integer, Long> getRecommendStats();
    boolean batchCreateRecommends(List<RecommendProductDTO> recommends);
}
