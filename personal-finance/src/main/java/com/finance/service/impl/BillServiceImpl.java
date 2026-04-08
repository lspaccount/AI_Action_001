package com.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.entity.Bill;
import com.finance.mapper.BillMapper;
import com.finance.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    @Autowired
    private BillMapper billMapper;

    @Override
    public boolean addBill(Bill bill) {
        bill.setDate(new Date());
        bill.setCreateTime(new Date());
        bill.setUpdateTime(new Date());
        return billMapper.insert(bill) > 0;
    }

    @Override
    public List<Bill> getBillList(Long userId) {
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("date");
        return billMapper.selectList(wrapper);
    }

    @Override
    public Map<String, BigDecimal> getMonthlyStats(Long userId, String yearMonth) {
        Map<String, BigDecimal> stats = new ConcurrentHashMap<>();
        stats.put("income", BigDecimal.ZERO);
        stats.put("expense", BigDecimal.ZERO);
        
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.likeRight("date", yearMonth);
        
        List<Bill> bills = billMapper.selectList(wrapper);
        
        for (Bill bill : bills) {
            if ("income".equals(bill.getType())) {
                stats.put("income", stats.get("income").add(bill.getAmount()));
            } else {
                stats.put("expense", stats.get("expense").add(bill.getAmount()));
            }
        }
        
        return stats;
    }

    @Override
    public Page<Bill> getBillPage(Long userId, int pageNum, int pageSize, String category, String month) {
        Page<Bill> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        
        if (category != null && !category.isEmpty()) {
            wrapper.eq("category", category);
        }
        
        if (month != null && !month.isEmpty()) {
            wrapper.likeRight("date", month);
        }
        
        wrapper.orderByDesc("date");
        return billMapper.selectPage(page, wrapper);
    }

    @Override
    public Map<String, Object> getDetailedMonthlyStats(Long userId, String month) {
        Map<String, Object> result = new ConcurrentHashMap<>();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        Map<String, BigDecimal> categoryStats = new ConcurrentHashMap<>();
        
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.likeRight("date", month);
        
        List<Bill> bills = billMapper.selectList(wrapper);
        
        for (Bill bill : bills) {
            if ("income".equals(bill.getType())) {
                totalIncome = totalIncome.add(bill.getAmount());
            } else {
                totalExpense = totalExpense.add(bill.getAmount());
                // 统计各分类支出
                String category = bill.getCategory();
                categoryStats.put(category, categoryStats.getOrDefault(category, BigDecimal.ZERO).add(bill.getAmount()));
            }
        }
        
        // 构建分类统计列表
        List<Map<String, Object>> categoryStatsList = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : categoryStats.entrySet()) {
            Map<String, Object> categoryStat = new ConcurrentHashMap<>();
            categoryStat.put("category", entry.getKey());
            categoryStat.put("amount", entry.getValue());
            categoryStatsList.add(categoryStat);
        }
        
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("balance", totalIncome.subtract(totalExpense));
        result.put("categoryStats", categoryStatsList);
        
        return result;
    }
}