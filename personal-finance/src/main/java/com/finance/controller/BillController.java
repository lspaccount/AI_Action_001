package com.finance.controller;

import com.finance.entity.Bill;
import com.finance.service.BillService;
import com.finance.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bill")
@SecurityRequirement(name = "Bearer Authentication")
public class BillController {

    @Autowired
    private BillService billService;

    @Operation(summary = "添加账单", description = "添加新的账单记录")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public Result<?> addBill(@Parameter(description = "账单信息") @RequestBody Bill bill, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error("未授权");
        }
        token = token.substring(7);
        Long userId = JwtUtil.getUserIdFromToken(token);
        bill.setUserId(userId);
        boolean success = billService.addBill(bill);
        if (success) {
            return Result.success("添加成功", null);
        }
        return Result.error("添加失败");
    }

    @Operation(summary = "获取账单列表", description = "获取用户的账单列表")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/list")
    public Result<List<Bill>> getBillList(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error("未授权");
        }
        token = token.substring(7);
        Long userId = JwtUtil.getUserIdFromToken(token);
        List<Bill> bills = billService.getBillList(userId);
        return Result.success("查询成功", bills);
    }

    @Operation(summary = "获取月度统计", description = "获取用户的月度收支统计")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/stats")
    public Result<Map<String, Object>> getMonthlyStats(@Parameter(description = "年月，格式：YYYY-MM") @RequestParam String month, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error("未授权");
        }
        token = token.substring(7);
        Long userId = JwtUtil.getUserIdFromToken(token);
        Map<String, Object> stats = billService.getDetailedMonthlyStats(userId, month);
        return Result.success("查询成功", stats);
    }

    @Operation(summary = "账单分页查询", description = "分页查询账单，支持按分类和月份筛选")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/page")
    public Result<Page<Bill>> getBillPage(
            @Parameter(description = "页码，默认1") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小，默认10") @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "分类，可选") @RequestParam(required = false) String category,
            @Parameter(description = "月份，格式：YYYY-MM，可选") @RequestParam(required = false) String month,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error("未授权");
        }
        token = token.substring(7);
        Long userId = JwtUtil.getUserIdFromToken(token);
        Page<Bill> page = billService.getBillPage(userId, pageNum, pageSize, category, month);
        return Result.success("查询成功", page);
    }
}