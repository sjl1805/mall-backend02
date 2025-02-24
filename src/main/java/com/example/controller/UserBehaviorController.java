package com.example.controller;

import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.UserBehavior;
import com.example.service.UserBehaviorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "UserBehavior", description = "用户行为记录的增删改查")
@RestController
@RequestMapping("/userBehavior")
public class UserBehaviorController {

    @Autowired
    private UserBehaviorService userBehaviorService;

    @Operation(summary = "根据用户ID查询用户行为记录")
    @GetMapping("/user/{userId}")
    public CommonResult<List<UserBehavior>> getBehaviorsByUserId(@PathVariable Long userId) {
        List<UserBehavior> behaviors = userBehaviorService.selectByUserId(userId);
        return CommonResult.success(behaviors);
    }

    @Operation(summary = "新增用户行为记录")
    @PostMapping("/add")
    public CommonResult<Boolean> addUserBehavior(@Valid @RequestBody UserBehavior userBehavior) {
        boolean result = userBehaviorService.insertUserBehavior(userBehavior);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新用户行为记录")
    @PutMapping("/update")
    public CommonResult<Boolean> updateUserBehavior(@Valid @RequestBody UserBehavior userBehavior) {
        boolean result = userBehaviorService.updateUserBehavior(userBehavior);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除用户行为记录")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteUserBehavior(@PathVariable Long id) {
        boolean result = userBehaviorService.deleteUserBehavior(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 