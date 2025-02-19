package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.product.RecommendProductDTO;
import com.example.model.dto.product.RecommendProductPageDTO;
import com.example.model.entity.RecommendProduct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 推荐商品管理Mapper接口
 * 实现推荐策略管理、时间排期和智能推荐算法集成
 * 
 * @author 毕业设计学生
 */
public interface RecommendProductMapper extends BaseMapper<RecommendProduct> {

    /**
     * 分页查询推荐商品（支持多条件过滤）
     * 
     * @param page     分页参数（包含当前页、每页数量）
     * @param queryDTO 查询条件DTO（包含类型、状态、时间范围等）
     * @return 分页结果（包含推荐列表和分页信息）
     */
    IPage<RecommendProduct> selectRecommendPage(IPage<RecommendProduct> page,
                                                @Param("query") RecommendProductPageDTO queryDTO);

    /**
     * 根据类型和状态查询有效推荐（前台展示用）
     * 
     * @param type   推荐类型（1-首页推荐 2-分类推荐 3-猜你喜欢）
     * @param status 状态（1-生效 0-失效）
     * @return 按排序值升序排列的推荐列表
     */
    List<RecommendProduct> selectByTypeAndStatus(@Param("type") Integer type,
                                                 @Param("status") Integer status);

    /**
     * 更新推荐状态（定时任务调用）
     * 
     * @param id     推荐记录ID（必填）
     * @param status 目标状态（1-生效 0-失效）
     * @return 影响的行数（0表示操作失败）
     */
    @Update("UPDATE recommend_product SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 调整推荐排序（拖拽排序时调用）
     * 
     * @param id   推荐记录ID（必填）
     * @param sort 新的排序值（1-100）
     * @return 影响的行数
     */
    @Update("UPDATE recommend_product SET sort = #{sort} WHERE id = #{id}")
    int updateSort(@Param("id") Long id, @Param("sort") Integer sort);

    /**
     * 统计有效推荐数量（用于数据看板）
     * 
     * @return 各类型有效推荐数量统计
     */
    List<Map<String, Object>> countActiveRecommends();

    /**
     * 检查时间冲突（创建/修改推荐时调用）
     * 
     * @param productId 商品ID（必填）
     * @param startTime 推荐开始时间（格式：yyyy-MM-dd HH:mm:ss）
     * @param endTime   推荐结束时间（需晚于开始时间）
     * @return 存在时间冲突返回冲突数量，否则返回0
     */
    int checkTimeConflict(@Param("productId") Long productId,
                          @Param("startTime") String startTime,
                          @Param("endTime") String endTime);

    /**
     * 根据算法版本查询推荐（AB测试使用）
     * 
     * @param version 算法版本号（格式：v1.2.0）
     * @return 按创建时间倒序排列的推荐列表
     */
    List<RecommendProduct> selectByAlgorithmVersion(@Param("version") String version);

    /**
     * 根据类型和时间范围查询（定时任务调用）
     * 
     * @param type  推荐类型（必填）
     * @param start 查询开始时间
     * @param end   查询结束时间
     * @return 符合时间范围的推荐列表
     */
    List<RecommendProduct> selectByTypeAndTimeRange(@Param("type") Integer type,
                                                    @Param("start") Date start,
                                                    @Param("end") Date end);

    /**
     * 批量插入推荐记录（支持导入功能）
     * 
     * @param recommends 推荐记录DTO列表
     * @return 成功插入的记录数
     */
    int batchInsert(@Param("recommends") List<RecommendProductDTO> recommends);
}




