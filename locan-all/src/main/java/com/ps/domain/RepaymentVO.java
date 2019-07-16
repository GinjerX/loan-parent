package com.ps.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RepaymentVO {

    private Integer id;
    private BigDecimal money;
    private String time;
    private String state;
    private Integer borrowId;
    private BorrowVO borrowVO;
    private Integer  countMoney;
    private Integer overdue;//已逾期的天数;


}

