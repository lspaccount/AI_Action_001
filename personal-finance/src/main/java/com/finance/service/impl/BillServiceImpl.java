package com.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.entity.Bill;
import com.finance.mapper.BillMapper;
import com.finance.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
}