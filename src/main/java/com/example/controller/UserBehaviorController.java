package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common. Result;
import com.example.common.ResultCode;
import com.example.model.entity.Products;
import com.example.model.entity.UserBehavior;
import com.example.model.entity.Users;
import com.example.service.UserBehaviorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户行为控制器
 * <p>
 * 提供用户行为记录相关的API接口，包括行为数据的记录、查询和分析
 */
@Tag(name = "UserBehavior", description = "用户行为记录及数据分析接口")
@RestController
@RequestMapping("/userBehavior")
@Validated
@Slf4j
public class UserBehaviorController {

    @Autowired
    private UserBehaviorService userBehaviorService;

    @Operation(summary = "根据用户ID查询用户行为记录", description = "获取指定用户的所有行为数据")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<List<UserBehavior>> getBehaviorsByUserId(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("获取用户行为记录请求: userId={}", userId);
        List<UserBehavior> behaviors = userBehaviorService.selectByUserId(userId);
        log.info("获取用户行为记录成功: userId={}, count={}", userId, behaviors.size());
        return  Result.success(behaviors);
    }

    @Operation(summary = "新增用户行为记录", description = "记录用户行为数据，如浏览商品、收藏等")
    @PostMapping("/add")
    public  Result<Boolean> addUserBehavior(@Valid @RequestBody UserBehavior userBehavior) {
        log.info("新增用户行为记录请求: userId={}, productId={}, behaviorType={}", 
                userBehavior.getUserId(), userBehavior.getProductId(), userBehavior.getBehaviorType());
        
        // 验证用户只能添加自己的行为记录，除非是管理员
        if (!isAdminOrSelf(userBehavior.getUserId())) {
            log.warn("新增用户行为记录失败: 权限不足, userId={}", userBehavior.getUserId());
            return  Result.failed(ResultCode.FORBIDDEN, "无权为其他用户添加行为记录");
        }
        
        boolean result = userBehaviorService.insertUserBehavior(userBehavior);
        if (result) {
            log.info("新增用户行为记录成功: userId={}, productId={}, behaviorType={}", 
                    userBehavior.getUserId(), userBehavior.getProductId(), userBehavior.getBehaviorType());
            return  Result.success(true);
        } else {
            log.warn("新增用户行为记录失败: userId={}, productId={}, behaviorType={}", 
                    userBehavior.getUserId(), userBehavior.getProductId(), userBehavior.getBehaviorType());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "更新用户行为记录", description = "更新用户行为数据，需要管理员权限")
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> updateUserBehavior(@Valid @RequestBody UserBehavior userBehavior) {
        log.info("更新用户行为记录请求: id={}", userBehavior.getId());
        boolean result = userBehaviorService.updateUserBehavior(userBehavior);
        if (result) {
            log.info("更新用户行为记录成功: id={}", userBehavior.getId());
            return  Result.success(true);
        } else {
            log.warn("更新用户行为记录失败: id={}", userBehavior.getId());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "根据ID删除用户行为记录", description = "删除用户行为数据，需要管理员权限")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> deleteUserBehavior(
            @Parameter(description = "行为记录ID", required = true) @PathVariable Long id) {
        log.info("删除用户行为记录请求: id={}", id);
        boolean result = userBehaviorService.deleteUserBehavior(id);
        if (result) {
            log.info("删除用户行为记录成功: id={}", id);
            return  Result.success(true);
        } else {
            log.warn("删除用户行为记录失败: id={}", id);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "根据行为类型查询用户行为", description = "查询特定类型的所有行为记录，需要管理员权限")
    @GetMapping("/type/{behaviorType}")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<List<UserBehavior>> getBehaviorsByType(
            @Parameter(description = "行为类型", required = true) @PathVariable Integer behaviorType) {
        log.info("根据行为类型查询用户行为请求: behaviorType={}", behaviorType);
        
        // 验证行为类型有效性
        if (behaviorType < 0) {
            log.warn("根据行为类型查询用户行为失败: 行为类型无效, behaviorType={}", behaviorType);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "行为类型无效");
        }
        
        List<UserBehavior> behaviors = userBehaviorService.selectByBehaviorType(behaviorType);
        log.info("根据行为类型查询用户行为成功: behaviorType={}, count={}", behaviorType, behaviors.size());
        return  Result.success(behaviors);
    }
    
    @Operation(summary = "根据用户ID和行为类型查询", description = "获取用户特定类型的行为记录")
    @GetMapping("/user/{userId}/type/{behaviorType}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<List<UserBehavior>> getBehaviorsByUserIdAndType(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "行为类型", required = true) @PathVariable Integer behaviorType) {
        log.info("根据用户ID和行为类型查询行为记录请求: userId={}, behaviorType={}", userId, behaviorType);
        
        // 验证行为类型有效性
        if (behaviorType < 0) {
            log.warn("根据用户ID和行为类型查询行为记录失败: 行为类型无效, behaviorType={}", behaviorType);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "行为类型无效");
        }
        
        List<UserBehavior> behaviors = userBehaviorService.selectByUserIdAndType(userId, behaviorType);
        log.info("根据用户ID和行为类型查询行为记录成功: userId={}, behaviorType={}, count={}", 
                userId, behaviorType, behaviors.size());
        return  Result.success(behaviors);
    }
    
    @Operation(summary = "根据时间范围查询用户行为", description = "查询指定时间范围内的所有行为记录，需要管理员权限")
    @GetMapping("/time")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<List<UserBehavior>> getBehaviorsByTimeRange(
            @Parameter(description = "开始时间", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @Parameter(description = "结束时间", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        log.info("根据时间范围查询用户行为请求: startTime={}, endTime={}", startTime, endTime);
        
        // 验证时间范围有效性
        if (startTime.after(endTime)) {
            log.warn("根据时间范围查询用户行为失败: 时间范围无效, startTime={}, endTime={}", startTime, endTime);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "开始时间不能晚于结束时间");
        }
        
        List<UserBehavior> behaviors = userBehaviorService.selectByTimeRange(startTime, endTime);
        log.info("根据时间范围查询用户行为成功: startTime={}, endTime={}, count={}", 
                startTime, endTime, behaviors.size());
        return  Result.success(behaviors);
    }
    
    @Operation(summary = "查询热门商品", description = "根据用户行为数据分析热门商品")
    @GetMapping("/popular")
    public  Result<List<Products>> getPopularProducts(
            @Parameter(description = "行为类型") @RequestParam(required = false) Integer behaviorType,
            @Parameter(description = "开始时间") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @Parameter(description = "结束时间") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("查询热门商品请求: behaviorType={}, startTime={}, endTime={}, limit={}", 
                behaviorType, startTime, endTime, limit);
        
        // 默认行为类型为浏览行为
        if (behaviorType == null) {
            behaviorType = 1; // 假设1表示浏览行为
        }
        
        // 设置默认时间范围为过去30天
        if (startTime == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -30);
            startTime = calendar.getTime();
        }
        
        if (endTime == null) {
            endTime = new Date();
        }
        
        // 验证时间范围有效性
        if (startTime.after(endTime)) {
            log.warn("查询热门商品失败: 时间范围无效, startTime={}, endTime={}", startTime, endTime);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "开始时间不能晚于结束时间");
        }
        
        List<Products> products = userBehaviorService.selectPopularProducts(
                behaviorType, startTime, endTime, limit);
        log.info("查询热门商品成功: behaviorType={}, count={}", behaviorType, products.size());
        return  Result.success(products);
    }
    
    @Operation(summary = "获取用户兴趣分类", description = "分析用户对不同类别的兴趣程度")
    @GetMapping("/interests/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<List<Users>> getUserInterests(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "开始时间") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @Parameter(description = "结束时间") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        log.info("获取用户兴趣分类请求: userId={}, startTime={}, endTime={}", userId, startTime, endTime);
        
        // 设置默认时间范围为过去30天
        if (startTime == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -30);
            startTime = calendar.getTime();
        }
        
        if (endTime == null) {
            endTime = new Date();
        }
        
        // 验证时间范围有效性
        if (startTime.after(endTime)) {
            log.warn("获取用户兴趣分类失败: 时间范围无效, startTime={}, endTime={}", startTime, endTime);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "开始时间不能晚于结束时间");
        }
        
        List<Users> interests = userBehaviorService.selectUserInterests(userId, startTime, endTime);
        log.info("获取用户兴趣分类成功: userId={}, count={}", userId, interests.size());
        return  Result.success(interests);
    }
    
    @Operation(summary = "批量插入用户行为", description = "批量添加用户行为记录，需要管理员权限")
    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> batchInsertBehaviors(@Valid @RequestBody List<UserBehavior> behaviors) {
        log.info("批量插入用户行为请求: count={}", behaviors.size());
        
        if (behaviors.isEmpty()) {
            log.warn("批量插入用户行为失败: 行为列表为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "行为列表不能为空");
        }
        
        boolean result = userBehaviorService.batchInsertBehaviors(behaviors);
        if (result) {
            log.info("批量插入用户行为成功: count={}", behaviors.size());
            return  Result.success(true);
        } else {
            log.warn("批量插入用户行为失败: count={}", behaviors.size());
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "高级条件查询用户行为", description = "根据多种条件组合查询用户行为")
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or (authentication.principal.id == #userId and #userId != null)")
    public  Result<IPage<UserBehavior>> searchBehaviors(
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "商品ID") @RequestParam(required = false) Long productId,
            @Parameter(description = "行为类型") @RequestParam(required = false) Integer behaviorType,
            @Parameter(description = "开始时间") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @Parameter(description = "结束时间") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        log.info("高级条件查询用户行为请求: userId={}, productId={}, behaviorType={}, startTime={}, endTime={}, page={}, size={}", 
                userId, productId, behaviorType, startTime, endTime, page, size);
        
        // 验证时间范围有效性
        if (startTime != null && endTime != null && startTime.after(endTime)) {
            log.warn("高级条件查询用户行为失败: 时间范围无效, startTime={}, endTime={}", startTime, endTime);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "开始时间不能晚于结束时间");
        }
        
        Page<UserBehavior> pageParam = new Page<>(page, size);
        IPage<UserBehavior> result = userBehaviorService.selectBehaviorsByCondition(
                userId, productId, behaviorType, startTime, endTime, pageParam);
        
        log.info("高级条件查询用户行为成功: totalPages={}, totalRecords={}", result.getPages(), result.getTotal());
        return  Result.success(result);
    }
    
    @Operation(summary = "统计用户行为数量", description = "统计用户特定类型行为的数量")
    @GetMapping("/count/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<Integer> countUserBehaviors(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "行为类型") @RequestParam(required = false) Integer behaviorType) {
        log.info("统计用户行为数量请求: userId={}, behaviorType={}", userId, behaviorType);
        
        // 默认统计所有类型行为
        if (behaviorType == null) {
            behaviorType = 0; // 假设0表示所有类型
        }
        
        int count = userBehaviorService.countUserBehaviors(userId, behaviorType);
        log.info("统计用户行为数量成功: userId={}, behaviorType={}, count={}", userId, behaviorType, count);
        return  Result.success(count);
    }
    
    @Operation(summary = "分析用户行为时间分布", description = "按小时统计用户行为频率")
    @GetMapping("/time-distribution/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<Map<Integer, Integer>> analyzeUserBehaviorTimeDistribution(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "行为类型") @RequestParam(required = false) Integer behaviorType) {
        log.info("分析用户行为时间分布请求: userId={}, behaviorType={}", userId, behaviorType);
        
        // 默认分析所有类型行为
        if (behaviorType == null) {
            behaviorType = 0; // 假设0表示所有类型
        }
        
        Map<Integer, Integer> distribution = userBehaviorService.analyzeUserBehaviorTimeDistribution(
                userId, behaviorType);
        log.info("分析用户行为时间分布成功: userId={}, behaviorType={}", userId, behaviorType);
        return  Result.success(distribution);
    }
    
    @Operation(summary = "分析用户行为路径", description = "跟踪用户从浏览到购买的行为路径")
    @GetMapping("/path/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<List<Map<String, Object>>> analyzeUserBehaviorPath(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "开始时间") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @Parameter(description = "结束时间") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        log.info("分析用户行为路径请求: userId={}, startTime={}, endTime={}", userId, startTime, endTime);
        
        // 设置默认时间范围为过去7天
        if (startTime == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            startTime = calendar.getTime();
        }
        
        if (endTime == null) {
            endTime = new Date();
        }
        
        // 验证时间范围有效性
        if (startTime.after(endTime)) {
            log.warn("分析用户行为路径失败: 时间范围无效, startTime={}, endTime={}", startTime, endTime);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "开始时间不能晚于结束时间");
        }
        
        List<Map<String, Object>> path = userBehaviorService.analyzeUserBehaviorPath(userId, startTime, endTime);
        log.info("分析用户行为路径成功: userId={}, pathLength={}", userId, path.size());
        return  Result.success(path);
    }
    
    @Operation(summary = "计算用户活跃度", description = "基于行为频率、多样性、权重等因素计算活跃度")
    @GetMapping("/activity/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<BigDecimal> calculateUserActivityScore(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "天数，计算最近几天的活跃度") @RequestParam(defaultValue = "30") Integer days) {
        log.info("计算用户活跃度请求: userId={}, days={}", userId, days);
        
        // 验证天数有效性
        if (days <= 0) {
            log.warn("计算用户活跃度失败: 天数无效, days={}", days);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "天数必须大于0");
        }
        
        BigDecimal score = userBehaviorService.calculateUserActivityScore(userId, days);
        log.info("计算用户活跃度成功: userId={}, days={}, score={}", userId, days, score);
        return  Result.success(score);
    }
    
    @Operation(summary = "预测用户下一步可能的行为", description = "基于历史行为模式预测用户行为")
    @GetMapping("/predict/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<Map<Integer, BigDecimal>> predictNextBehavior(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("预测用户下一步可能的行为请求: userId={}", userId);
        
        Map<Integer, BigDecimal> prediction = userBehaviorService.predictNextBehavior(userId);
        log.info("预测用户下一步可能的行为成功: userId={}, predictionCount={}", userId, prediction.size());
        return  Result.success(prediction);
    }
    
    @Operation(summary = "识别用户异常行为", description = "检测与用户正常行为模式偏离较大的行为")
    @GetMapping("/abnormal/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<List<UserBehavior>> detectAbnormalBehaviors(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "天数，检测最近几天的行为") @RequestParam(defaultValue = "30") Integer days) {
        log.info("识别用户异常行为请求: userId={}, days={}", userId, days);
        
        // 验证天数有效性
        if (days <= 0) {
            log.warn("识别用户异常行为失败: 天数无效, days={}", days);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "天数必须大于0");
        }
        
        List<UserBehavior> abnormalBehaviors = userBehaviorService.detectAbnormalBehaviors(userId, days);
        log.info("识别用户异常行为成功: userId={}, days={}, abnormalCount={}", userId, days, abnormalBehaviors.size());
        return  Result.success(abnormalBehaviors);
    }
    
    @Operation(summary = "分析用户行为转化", description = "计算从一种行为到另一种行为的转化率")
    @GetMapping("/conversion")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<BigDecimal> analyzeBehaviorConversion(
            @Parameter(description = "源行为类型", required = true) @RequestParam Integer sourceType,
            @Parameter(description = "目标行为类型", required = true) @RequestParam Integer targetType,
            @Parameter(description = "天数") @RequestParam(defaultValue = "30") Integer days) {
        log.info("分析用户行为转化请求: sourceType={}, targetType={}, days={}", sourceType, targetType, days);
        
        // 验证行为类型和天数有效性
        if (sourceType < 0 || targetType < 0) {
            log.warn("分析用户行为转化失败: 行为类型无效, sourceType={}, targetType={}", sourceType, targetType);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "行为类型必须大于或等于0");
        }
        
        if (days <= 0) {
            log.warn("分析用户行为转化失败: 天数无效, days={}", days);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "天数必须大于0");
        }
        
        BigDecimal conversionRate = userBehaviorService.analyzeBehaviorConversion(sourceType, targetType, days);
        log.info("分析用户行为转化成功: sourceType={}, targetType={}, days={}, conversionRate={}", 
                sourceType, targetType, days, conversionRate);
        return  Result.success(conversionRate);
    }
    
    /**
     * 辅助方法：判断当前用户是否为管理员或操作用户本人
     */
    private boolean isAdminOrSelf(Long userId) {
        // 此处需要根据您的安全框架实现具体逻辑
        // 简化处理，实际应从SecurityContext中获取用户信息进行判断
        return true; // 默认允许，实际实现中应该返回正确的判断结果
    }
} 

