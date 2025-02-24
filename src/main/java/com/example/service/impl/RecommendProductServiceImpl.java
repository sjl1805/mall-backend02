package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.RecommendProductMapper;
import com.example.model.entity.RecommendProduct;
import com.example.service.RecommendProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【recommend_product(推荐商品表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:53
 */
@Service
public class RecommendProductServiceImpl extends ServiceImpl<RecommendProductMapper, RecommendProduct>
        implements RecommendProductService {

    @Autowired
    private RecommendProductMapper recommendProductMapper;

    @Override
    public List<RecommendProduct> selectByUserId(Long userId) {
        return recommendProductMapper.selectByUserId(userId);
    }

    @Override
    public IPage<RecommendProduct> selectPage(IPage<RecommendProduct> page) {
        return recommendProductMapper.selectPage(page);
    }

    @Override
    public RecommendProduct selectById(Long id) {
        return recommendProductMapper.selectById(id);
    }

    @Override
    public boolean insertRecommendProduct(RecommendProduct recommendProduct) {
        return recommendProductMapper.insert(recommendProduct) > 0;
    }

    @Override
    public boolean updateRecommendProduct(RecommendProduct recommendProduct) {
        return recommendProductMapper.updateById(recommendProduct) > 0;
    }

    @Override
    public boolean deleteRecommendProduct(Long id) {
        return recommendProductMapper.deleteById(id) > 0;
    }
}




