package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.RecommendProductService;
import com.example.model.entity.RecommendProduct;
import com.example.mapper.RecommendProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.model.dto.product.RecommendProductDTO;
import com.example.model.dto.product.RecommendProductPageDTO;
import com.example.exception.BusinessException;
import com.example.common.ResultCode;

/**
* @author 31815
* @description 针对表【recommend_product(推荐商品表)】的数据库操作Service实现
* @createDate 2025-02-18 23:44:00
*/
@Service
@CacheConfig(cacheNames = "recommendService")
public class RecommendProductServiceImpl extends ServiceImpl<RecommendProductMapper, RecommendProduct>
    implements RecommendProductService {

    //private static final Logger logger = LoggerFactory.getLogger(RecommendProductServiceImpl.class);

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean createRecommend(RecommendProductDTO recommendDTO) {
        // 校验时间冲突
        if (baseMapper.checkTimeConflict(
            recommendDTO.getProductId(),
            recommendDTO.getStartTime(),
            recommendDTO.getEndTime()
        ) > 0) {
            throw new BusinessException(ResultCode.RECOMMEND_TIME_CONFLICT);
        }

        RecommendProduct recommend = new RecommendProduct();
        BeanUtils.copyProperties(recommendDTO, recommend);
        return save(recommend);
    }

    @Override
    @Cacheable(key = "'page:' + #queryDTO.hashCode()")
    public IPage<RecommendProduct> listRecommendPage(RecommendProductPageDTO queryDTO) {
        Page<RecommendProduct> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return baseMapper.selectRecommendPage(page, queryDTO);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean updateRecommendStatus(Long id, Integer status) {
        return baseMapper.updateStatus(id, status) > 0;
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean updateRecommendSort(Long id, Integer sort) {
        return baseMapper.updateSort(id, sort) > 0;
    }

    @Override
    @Cacheable(key = "'active:' + #type")
    public List<RecommendProduct> getActiveRecommends(Integer type) {
        return baseMapper.selectByTypeAndStatus(type, 1);
    }

    @Override
    @Cacheable(key = "'stats'")
    public Map<Integer, Long> getRecommendStats() {
        return baseMapper.countActiveRecommends().stream()
                .collect(Collectors.toMap(
                        m -> (Integer) m.get("type"),
                        m -> (Long) m.get("count")
                ));
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean batchCreateRecommends(List<RecommendProductDTO> recommends) {
        // 批量校验时间冲突
        recommends.forEach(recommend -> {
            if (baseMapper.checkTimeConflict(
                recommend.getProductId(),
                recommend.getStartTime(),
                recommend.getEndTime()
            ) > 0) {
                throw new BusinessException(ResultCode.RECOMMEND_TIME_CONFLICT);
            }
        });
        return baseMapper.batchInsert(recommends) > 0;
    }
}




