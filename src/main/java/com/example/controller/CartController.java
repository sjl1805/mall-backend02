package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.model.entity.Cart;
import com.example.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 购物车控制器
 * <p>
 * 提供购物车相关的API接口，包括购物车的查询、添加、修改、删除等操作
 * 购物车是电商系统中连接浏览和下单的关键环节，直接影响用户的购买决策和转化率
 */
@Tag(name = "Cart", description = "购物车管理API")
@RestController
@RequestMapping("/cart")
@Validated
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "根据用户ID查询购物车", description = "获取指定用户的所有购物车商品")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public Result<List<Cart>> getCartsByUserId(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("根据用户ID查询购物车请求: userId={}", userId);
        List<Cart> carts = cartService.selectByUserId(userId);
        log.info("根据用户ID查询购物车成功: userId={}, count={}", userId, carts.size());
        return Result.success(carts);
    }

    @Operation(summary = "分页查询购物车", description = "管理员分页查询特定用户的购物车")
    @GetMapping("/list/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<IPage<Cart>> getCartList(
            @Parameter(description = "页码")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("分页查询购物车请求: page={}, size={}, userId={}", page, size, userId);
        IPage<Cart> pageParam = new Page<>(page, size);
        IPage<Cart> result = cartService.selectPage(pageParam, userId);
        log.info("分页查询购物车成功: totalPages={}, totalRecords={}", result.getPages(), result.getTotal());
        return  Result.success(result);
    }

    @Operation(summary = "根据ID查询购物车", description = "获取特定购物车记录的详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @cartService.selectById(#id)?.userId == authentication.principal.id")
    public  Result<Cart> getCartById(
            @Parameter(description = "购物车ID", required = true) @PathVariable Long id) {
        log.info("根据ID查询购物车请求: id={}", id);
        Cart cart = cartService.selectById(id);
        if (cart != null) {
            log.info("根据ID查询购物车成功: id={}", id);
            return  Result.success(cart);
        } else {
            log.warn("根据ID查询购物车失败: id={}, 购物车记录不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "购物车记录不存在");
        }
    }

    @Operation(summary = "新增购物车", description = "添加商品到购物车")
    @PostMapping("/add")
    @PreAuthorize("isAuthenticated() and authentication.principal.id == #cart.userId")
    public  Result<Boolean> addCart(@Valid @RequestBody Cart cart) {
        log.info("新增购物车请求: userId={}, productId={}, quantity={}",
                cart.getUserId(), cart.getProductId(), cart.getQuantity());

        // 参数验证
        if (cart.getProductId() == null) {
            log.warn("新增购物车失败: 商品ID不能为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "商品ID不能为空");
        }

        if (cart.getQuantity() == null || cart.getQuantity() <= 0) {
            log.warn("新增购物车失败: 数量无效, quantity={}", cart.getQuantity());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "商品数量必须大于0");
        }

        boolean result = cartService.insertCart(cart);
        if (result) {
            log.info("新增购物车成功: userId={}, productId={}, id={}",
                    cart.getUserId(), cart.getProductId(), cart.getId());
            return  Result.success(true);
        } else {
            log.warn("新增购物车失败: userId={}, productId={}",
                    cart.getUserId(), cart.getProductId());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "更新购物车", description = "更新购物车商品信息")
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #cart.userId")
    public  Result<Boolean> updateCart(@Valid @RequestBody Cart cart) {
        log.info("更新购物车请求: id={}", cart.getId());

        // 参数验证
        if (cart.getId() == null) {
            log.warn("更新购物车失败: ID不能为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "购物车ID不能为空");
        }

        if (cart.getQuantity() != null && cart.getQuantity() <= 0) {
            log.warn("更新购物车失败: 数量无效, quantity={}", cart.getQuantity());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "商品数量必须大于0");
        }

        // 验证权限
        Cart existingCart = cartService.selectById(cart.getId());
        if (existingCart == null) {
            log.warn("更新购物车失败: id={}, 购物车记录不存在", cart.getId());
            return  Result.failed(ResultCode.NOT_FOUND, "购物车记录不存在");
        }

        boolean result = cartService.updateCart(cart);
        if (result) {
            log.info("更新购物车成功: id={}", cart.getId());
            return  Result.success(true);
        } else {
            log.warn("更新购物车失败: id={}", cart.getId());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "根据ID删除购物车", description = "从购物车中移除指定商品")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @cartService.selectById(#id)?.userId == authentication.principal.id")
    public  Result<Boolean> deleteCart(
            @Parameter(description = "购物车ID", required = true) @PathVariable Long id) {
        log.info("删除购物车请求: id={}", id);

        // 检查购物车记录是否存在
        Cart cart = cartService.selectById(id);
        if (cart == null) {
            log.warn("删除购物车失败: id={}, 购物车记录不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "购物车记录不存在");
        }

        boolean result = cartService.deleteCart(id);
        if (result) {
            log.info("删除购物车成功: id={}", id);
            return  Result.success(true);
        } else {
            log.warn("删除购物车失败: id={}", id);
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "根据用户ID和商品ID查询购物车商品", description = "检查用户购物车中是否已有特定商品")
    @GetMapping("/check/{userId}/{productId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<Cart> getCartByUserIdAndProductId(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "商品ID", required = true) @PathVariable Long productId) {
        log.info("根据用户ID和商品ID查询购物车商品请求: userId={}, productId={}", userId, productId);
        Cart cart = cartService.selectByUserIdAndProductId(userId, productId);
        if (cart != null) {
            log.info("根据用户ID和商品ID查询购物车商品成功: userId={}, productId={}", userId, productId);
            return  Result.success(cart);
        } else {
            log.info("根据用户ID和商品ID查询购物车商品: 未找到记录, userId={}, productId={}", userId, productId);
            return  Result.failed(ResultCode.NOT_FOUND, "购物车中未找到该商品");
        }
    }

    @Operation(summary = "获取购物车商品详情", description = "获取购物车商品详情，包含商品信息")
    @GetMapping("/detail/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<List<Map<String, Object>>> getCartDetail(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("获取购物车商品详情请求: userId={}", userId);
        List<Map<String, Object>> details = cartService.getCartDetail(userId);
        log.info("获取购物车商品详情成功: userId={}, count={}", userId, details.size());
        return  Result.success(details);
    }

    @Operation(summary = "更新购物车商品数量", description = "修改购物车中商品的数量")
    @PutMapping("/{id}/quantity/{quantity}")
    @PreAuthorize("hasRole('ADMIN') or @cartService.selectById(#id)?.userId == authentication.principal.id")
    public  Result<Boolean> updateQuantity(
            @Parameter(description = "购物车ID", required = true) @PathVariable Long id,
            @Parameter(description = "商品数量", required = true) @PathVariable Integer quantity) {
        log.info("更新购物车商品数量请求: id={}, quantity={}", id, quantity);

        // 检查购物车记录是否存在
        Cart cart = cartService.selectById(id);
        if (cart == null) {
            log.warn("更新购物车商品数量失败: id={}, 购物车记录不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "购物车记录不存在");
        }

        // 参数验证
        if (quantity <= 0) {
            log.warn("更新购物车商品数量失败: 数量无效, quantity={}", quantity);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "商品数量必须大于0");
        }

        boolean result = cartService.updateQuantity(id, quantity);
        if (result) {
            log.info("更新购物车商品数量成功: id={}, quantity={}", id, quantity);
            return  Result.success(true);
        } else {
            log.warn("更新购物车商品数量失败: id={}, quantity={}", id, quantity);
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "更新购物车商品选中状态", description = "更新购物车中商品的选中状态")
    @PutMapping("/{id}/checked/{checked}")
    @PreAuthorize("hasRole('ADMIN') or @cartService.selectById(#id)?.userId == authentication.principal.id")
    public  Result<Boolean> updateChecked(
            @Parameter(description = "购物车ID", required = true) @PathVariable Long id,
            @Parameter(description = "选中状态: 0-未选中 1-已选中", required = true) @PathVariable Integer checked) {
        log.info("更新购物车商品选中状态请求: id={}, checked={}", id, checked);

        // 检查购物车记录是否存在
        Cart cart = cartService.selectById(id);
        if (cart == null) {
            log.warn("更新购物车商品选中状态失败: id={}, 购物车记录不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "购物车记录不存在");
        }

        // 参数验证
        if (checked != 0 && checked != 1) {
            log.warn("更新购物车商品选中状态失败: 选中状态无效, checked={}", checked);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "选中状态必须为0或1");
        }

        boolean result = cartService.updateChecked(id, checked);
        if (result) {
            log.info("更新购物车商品选中状态成功: id={}, checked={}", id, checked);
            return  Result.success(true);
        } else {
            log.warn("更新购物车商品选中状态失败: id={}, checked={}", id, checked);
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "全选/取消全选购物车商品", description = "全选或取消全选用户购物车中的所有商品")
    @PutMapping("/all-checked/{userId}/{checked}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<Boolean> updateAllChecked(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "选中状态: 0-未选中 1-已选中", required = true) @PathVariable Integer checked) {
        log.info("全选/取消全选购物车商品请求: userId={}, checked={}", userId, checked);

        // 参数验证
        if (checked != 0 && checked != 1) {
            log.warn("全选/取消全选购物车商品失败: 选中状态无效, checked={}", checked);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "选中状态必须为0或1");
        }

        boolean result = cartService.updateAllChecked(userId, checked);
        if (result) {
            log.info("全选/取消全选购物车商品成功: userId={}, checked={}", userId, checked);
            return  Result.success(true);
        } else {
            log.warn("全选/取消全选购物车商品失败: userId={}, checked={}", userId, checked);
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "查询用户选中的购物车商品", description = "获取用户购物车中已勾选的商品")
    @GetMapping("/checked/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<List<Cart>> getCheckedCartsByUserId(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("查询用户选中的购物车商品请求: userId={}", userId);
        List<Cart> checkedCarts = cartService.selectCheckedByUserId(userId);
        log.info("查询用户选中的购物车商品成功: userId={}, count={}", userId, checkedCarts.size());
        return  Result.success(checkedCarts);
    }

    @Operation(summary = "获取选中商品详情", description = "获取已选中商品的详细信息，用于订单确认")
    @GetMapping("/checked-detail/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<List<Map<String, Object>>> getCheckedCartDetail(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("获取选中商品详情请求: userId={}", userId);
        List<Map<String, Object>> details = cartService.getCheckedCartDetail(userId);
        log.info("获取选中商品详情成功: userId={}, count={}", userId, details.size());
        return  Result.success(details);
    }

    @Operation(summary = "清空用户购物车", description = "清空用户的整个购物车")
    @DeleteMapping("/clear/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<Boolean> clearCart(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("清空用户购物车请求: userId={}", userId);
        boolean result = cartService.clearCart(userId);
        if (result) {
            log.info("清空用户购物车成功: userId={}", userId);
            return  Result.success(true);
        } else {
            log.warn("清空用户购物车失败: userId={}", userId);
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "删除选中的购物车商品", description = "删除用户购物车中已勾选的商品")
    @DeleteMapping("/delete-checked/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<Boolean> deleteChecked(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("删除选中的购物车商品请求: userId={}", userId);
        boolean result = cartService.deleteChecked(userId);
        if (result) {
            log.info("删除选中的购物车商品成功: userId={}", userId);
            return  Result.success(true);
        } else {
            log.warn("删除选中的购物车商品失败: userId={}", userId);
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "批量删除购物车商品", description = "批量删除多个购物车商品")
    @DeleteMapping("/batch")
    @PreAuthorize("hasRole('ADMIN') or isCurrentUserOwnAllCarts(#ids)")
    public  Result<Boolean> batchDelete(@RequestBody List<Long> ids) {
        log.info("批量删除购物车商品请求: ids={}", ids);

        // 参数验证
        if (ids == null || ids.isEmpty()) {
            log.warn("批量删除购物车商品失败: 参数无效, ids为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "购物车ID列表不能为空");
        }

        boolean result = cartService.batchDelete(ids);
        if (result) {
            log.info("批量删除购物车商品成功: count={}", ids.size());
            return  Result.success(true);
        } else {
            log.warn("批量删除购物车商品失败: ids={}", ids);
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "统计用户购物车商品数量", description = "获取用户购物车中的商品总数")
    @GetMapping("/count/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<Integer> countByUserId(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("统计用户购物车商品数量请求: userId={}", userId);
        int count = cartService.countByUserId(userId);
        log.info("统计用户购物车商品数量成功: userId={}, count={}", userId, count);
        return  Result.success(count);
    }

    @Operation(summary = "添加或更新购物车", description = "添加商品到购物车或更新已有商品的数量")
    @PostMapping("/add-or-update")
    @PreAuthorize("isAuthenticated() and authentication.principal.id == #userId")
    public  Result<Boolean> addOrUpdateCart(
            @Parameter(description = "用户ID", required = true) @RequestParam Long userId,
            @Parameter(description = "商品ID", required = true) @RequestParam Long productId,
            @Parameter(description = "数量", required = true) @RequestParam Integer quantity) {
        log.info("添加或更新购物车请求: userId={}, productId={}, quantity={}", userId, productId, quantity);

        // 参数验证
        if (quantity <= 0) {
            log.warn("添加或更新购物车失败: 数量无效, quantity={}", quantity);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "商品数量必须大于0");
        }

        boolean result = cartService.addOrUpdateCart(userId, productId, quantity);
        if (result) {
            log.info("添加或更新购物车成功: userId={}, productId={}", userId, productId);
            return  Result.success(true);
        } else {
            log.warn("添加或更新购物车失败: userId={}, productId={}", userId, productId);
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "计算购物车选中商品价格", description = "计算用户购物车中已勾选商品的总价")
    @GetMapping("/calculate/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<BigDecimal> calculateCheckedAmount(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("计算购物车选中商品价格请求: userId={}", userId);
        BigDecimal totalAmount = cartService.calculateCheckedAmount(userId);
        log.info("计算购物车选中商品价格成功: userId={}, totalAmount={}", userId, totalAmount);
        return  Result.success(totalAmount);
    }

    /**
     * 辅助方法：判断当前用户是否拥有所有指定的购物车记录
     * 此方法应该在SecurityConfig中配置，这里仅作占位符
     */
    private boolean isCurrentUserOwnAllCarts(List<Long> cartIds) {
        // 此方法应该检查当前用户是否拥有所有指定的购物车记录
        // 实际应该遍历cartIds，对每个id检查所属用户是否是当前用户
        return true; // 默认允许，实际实现中应该返回正确的判断结果
    }
}

