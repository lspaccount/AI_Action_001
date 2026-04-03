package com.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.entity.Bill;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BillService extends IService<Bill> {
    boolean addBill(Bill bill);
    
    List<Bill> getBillList(Long userId);
    
    Map<String, BigDecimal> getMonthlyStats(Long userId, String yearMonth);
}