package com.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_bill")
public class Bill {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Integer type;
    
    private BigDecimal amount;
    
    private String category;
    
    private String remark;
    
    private Date billDate;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}