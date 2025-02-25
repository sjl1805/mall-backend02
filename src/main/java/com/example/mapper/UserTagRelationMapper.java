package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.UserTagRelation;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户-标签关联Mapper接口
 */
@Mapper
public interface UserTagRelationMapper extends BaseMapper<UserTagRelation> {
    
    /**
     * 查询用户的所有标签关联
     * @param userId 用户ID
     * @return 标签关联列表
     */
    List<UserTagRelation> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 查询与标签关联的所有用户
     * @param tagId 标签ID
     * @return 用户标签关联列表
     */
    List<UserTagRelation> selectByTagId(@Param("tagId") Long tagId);
    
    /**
     * 查询用户的高权重标签(权重大于指定值)
     * @param userId 用户ID
     * @param minWeight 最小权重值
     * @return 高权重标签关联列表
     */
    List<UserTagRelation> selectHighWeightTags(@Param("userId") Long userId, @Param("minWeight") BigDecimal minWeight);
    
    /**
     * 查询标签的高关联用户(权重大于指定值)
     * @param tagId 标签ID
     * @param minWeight 最小权重值
     * @return 高关联用户列表
     */
    List<UserTagRelation> selectHighWeightUsers(@Param("tagId") Long tagId, @Param("minWeight") BigDecimal minWeight);
    
    /**
     * 更新用户标签权重
     * @param userId 用户ID
     * @param tagId 标签ID
     * @param weight 新权重
     * @return 影响行数
     */
    int updateWeight(@Param("userId") Long userId, @Param("tagId") Long tagId, @Param("weight") BigDecimal weight);
    
    /**
     * 批量插入用户标签关联
     * @param list 用户标签关联列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<UserTagRelation> list);
    
    /**
     * 删除用户的所有标签关联
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUserId(@Param("userId") Long userId);
    
    /**
     * 删除特定的用户标签关联
     * @param userId 用户ID
     * @param tagId 标签ID
     * @return 影响行数
     */
    int deleteByUserIdAndTagId(@Param("userId") Long userId, @Param("tagId") Long tagId);
    
    /**
     * 查询标签的使用统计
     * @return 标签使用统计列表，包含tagId、tagCount（使用人数）、avgWeight（平均权重）
     */
    @MapKey("tagId")
    List<Map<String, Object>> selectTagUsageStats();
    
    /**
     * 分页查询某标签的关联用户
     * @param page 分页参数
     * @param tagId 标签ID
     * @return 分页数据
     */
    IPage<UserTagRelation> selectUsersByTagIdPage(Page<UserTagRelation> page, @Param("tagId") Long tagId);
    
    /**
     * 查询用户的标签关联数量
     * @param userId 用户ID
     * @return 关联数量
     */
    int countByUserId(@Param("userId") Long userId);
    
    /**
     * 增加标签权重
     * @param userId 用户ID
     * @param tagId 标签ID
     * @param increment 增量值
     * @return 影响行数
     */
    int incrementWeight(@Param("userId") Long userId, @Param("tagId") Long tagId, @Param("increment") BigDecimal increment);
} 