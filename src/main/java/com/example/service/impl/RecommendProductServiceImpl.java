package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.RecommendProductMapper;
import com.example.model.dto.RecommendProductDTO;
import com.example.model.dto.PageDTO;
import com.example.model.entity.RecommendProduct;
import com.example.service.RecommendProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 推荐商品服务实现类
 * 
 * @author 31815
 * @description 实现推荐商品核心业务逻辑，包含：
 *              1. 时间冲突校验
 *              2. 批量操作优化
 *              3. 缓存策略管理
 * @createDate 2025-02-18 23:44:00
 */
@Service
@CacheConfig(cacheNames = "recommendService")
public class RecommendProductServiceImpl extends ServiceImpl<RecommendProductMapper, RecommendProduct>
        implements RecommendProductService {

    /**
     * 创建推荐（完整校验）
     * @param recommendDTO 推荐信息
     * @return 操作结果
     * @implNote 业务逻辑：
     *           1. 校验时间冲突
     *           2. 保存推荐记录
     *           3. 清除全量缓存
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean createRecommend(RecommendProductDTO recommendDTO) {
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

    /**
     * 分页查询推荐（缓存优化）
     * @param queryDTO 分页参数
     * @return 分页结果
     * @implNote 缓存策略：
     *           1. 缓存键：page:{queryDTO.hashCode}
     *           2. 缓存时间：10分钟
     */
    @Override
    @Cacheable(key = "'page:' + #queryDTO.hashCode()")
    public IPage<RecommendProductDTO> listRecommendPage(PageDTO<RecommendProductDTO> queryDTO) {
        Page<RecommendProduct> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        IPage<RecommendProduct> recommendPage = baseMapper.selectRecommendPage(page, queryDTO.getQuery());
        return recommendPage.convert(RecommendProductDTO::fromEntity);
    }

    /**
     * 更新推荐状态（状态机）
     * @param id 推荐ID
     * @param status 新状态
     * @return 操作结果
     * @implNote 状态变更后清除全量缓存
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean updateRecommendStatus(Long id, Integer status) {
        return baseMapper.updateStatus(id, status) > 0;
    }

    /**
     * 调整推荐排序（原子操作）
     * @param id 推荐ID
     * @param sort 新排序值
     * @return 操作结果
     * @implNote 使用数据库行锁保证原子性
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean updateRecommendSort(Long id, Integer sort) {
        return baseMapper.updateSort(id, sort) > 0;
    }

    /**
     * 获取有效推荐（缓存优化）
     * @param type 推荐类型
     * @return 推荐列表
     * @implNote 缓存策略：
     *           1. 缓存键：active:{type}
     *           2. 缓存时间：30分钟
     */
    @Override
    @Cacheable(key = "'active:' + #type")
    public List<RecommendProductDTO> getActiveRecommends(Integer type) {
        return baseMapper.selectByTypeAndStatus(type, 1).stream()
                .map(RecommendProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 获取推荐统计（缓存优化）
     * @return 统计结果
     * @implNote 缓存策略：
     *           1. 缓存键：stats
     *           2. 缓存时间：2小时
     */
    @Override
    @Cacheable(key = "'stats'")
    public Map<Integer, Long> getRecommendStats() {
        return baseMapper.countActiveRecommends().stream()
                .collect(Collectors.toMap(
                        m -> (Integer) m.get("type"),
                        m -> (Long) m.get("count")
                ));
    }

    /**
     * 批量创建推荐（事务操作）
     * @param recommends 推荐列表
     * @return 操作结果
     * @implNote 执行逻辑：
     *           1. 批量校验时间冲突
     *           2. 批量插入数据
     *           3. 清除全量缓存
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean batchCreateRecommends(List<RecommendProductDTO> recommends) {
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




