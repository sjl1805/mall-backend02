package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.UserTag;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户标签Mapper接口
 */
@Mapper
public interface UserTagMapper extends BaseMapper<UserTag> {

    /**
     * 根据标签名称查询用户标签
     *
     * @param name 标签名称
     * @return 标签对象
     */
    UserTag selectByName(String name);

    /**
     * 根据标签类型查询用户标签列表
     *
     * @param type 标签类型：1-兴趣 2-行为 3-人口特征 4-其他
     * @return 标签列表
     */
    List<UserTag> selectListByType(Integer type);

    /**
     * 分页查询用户标签（支持多条件筛选）
     *
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页标签列表
     */
    IPage<UserTag> selectTagPage(Page<UserTag> page, @Param("params") Map<String, Object> params);

    /**
     * 批量插入用户标签
     *
     * @param tagList 标签列表
     * @return 影响行数
     */
    int batchInsert(@Param("tagList") List<UserTag> tagList);

    /**
     * 根据名称模糊查询用户标签
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 标签列表
     */
    List<UserTag> searchByKeyword(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 获取常用用户标签（根据关联用户数量）
     *
     * @param limit 限制数量
     * @return 热门标签列表
     */
    List<UserTag> selectPopularTags(@Param("limit") Integer limit);

    /**
     * 统计各类型用户标签数量
     *
     * @return 统计结果
     */
    @MapKey("type")
    List<Map<String, Object>> countTagByType();

    /**
     * 查询用户标签是否存在
     *
     * @param name 标签名称
     * @return 存在数量
     */
    int existsByName(String name);
    
    /**
     * 根据用户ID获取关联的标签
     *
     * @param userId 用户ID
     * @return 标签列表
     */
    List<UserTag> selectTagsByUserId(@Param("userId") Long userId);
    
    /**
     * 批量删除用户标签
     *
     * @param ids 标签ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("ids") List<Long> ids);
    
    /**
     * 获取系统推荐的用户标签
     * 
     * @param limit 限制数量
     * @return 标签列表
     */
    List<UserTag> selectRecommendedTags(@Param("limit") Integer limit);
    
    /**
     * 按使用频率统计标签
     * 
     * @param limit 限制数量
     * @return 统计结果
     */
    @MapKey("tagId")
    List<Map<String, Object>> countTagUsage(@Param("limit") Integer limit);
} 