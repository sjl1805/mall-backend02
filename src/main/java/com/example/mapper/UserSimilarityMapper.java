package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.UserSimilarity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户相似度数据访问层接口
 */
@Mapper
public interface UserSimilarityMapper extends BaseMapper<UserSimilarity> {
    
    /**
     * 获取与指定用户相似度最高的用户列表
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 用户相似度列表
     */
    @Select("SELECT * FROM user_similarity WHERE user_id_a = #{userId} ORDER BY similarity DESC LIMIT #{limit}")
    List<UserSimilarity> findSimilarUsers(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 获取两个用户之间的相似度
     *
     * @param userIdA 用户A ID
     * @param userIdB 用户B ID
     * @return 用户相似度
     */
    @Select("SELECT * FROM user_similarity WHERE " +
            "(user_id_a = #{userIdA} AND user_id_b = #{userIdB}) OR " +
            "(user_id_a = #{userIdB} AND user_id_b = #{userIdA}) LIMIT 1")
    UserSimilarity findBetweenUsers(@Param("userIdA") Long userIdA, @Param("userIdB") Long userIdB);
} 