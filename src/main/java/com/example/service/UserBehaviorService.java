package com.example.service;

import com.example.model.entity.UserBehavior;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.users.UserBehaviorDTO;

import java.util.List;
import java.util.Map;

/**
* @author 31815
* @description 针对表【user_behavior(用户行为记录表)】的数据库操作Service
* @createDate 2025-02-18 23:43:52
*/
public interface UserBehaviorService extends IService<UserBehavior> {
    boolean recordBehavior(UserBehaviorDTO behaviorDTO);
    List<UserBehavior> getRecentBehaviors(Long userId, Integer limit);
    Map<String, Object> getUserWeight(Long userId);
    List<Map<String, Object>> getBehaviorDistribution(Integer days);
    boolean batchRecordBehaviors(List<UserBehaviorDTO> behaviors);
   // void analyzeBehaviors();
}
