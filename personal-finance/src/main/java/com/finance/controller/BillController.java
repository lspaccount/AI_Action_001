package com.finance.controller;

import com.finance.entity.Bill;
import com.finance.service.BillService;
import com.finance.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping
    public Result addBill(@RequestBody Bill bill, @RequestHeader("Authorization") String token) {
        Long userId = JwtUtil.getUserIdFromToken(token);
        bill.setUserId(userId);
        boolean success = billService.addBill(bill);
        if (success) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @GetMapping("/list")
    public Result getBillList(@RequestHeader("Authorization") String token) {
        Long userId = JwtUtil.getUserIdFromToken(token);
        List<Bill> bills = billService.getBillList(userId);
        return Result.success("查询成功", bills);
    }

    @GetMapping("/stats")
    public Result getMonthlyStats(@RequestParam String yearMonth, @RequestHeader("Authorization") String token) {
        Long userId = JwtUtil.getUserIdFromToken(token);
        Map<String, BigDecimal> stats = billService.getMonthlyStats(userId, yearMonth);
        return Result.success("查询成功", stats);
    }
}