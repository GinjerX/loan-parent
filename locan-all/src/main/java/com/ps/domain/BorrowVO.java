package com.ps.domain;

import lombok.Data;

@Data
public class BorrowVO {
    private String type; //短信类型 借款
    private int borrowId;
    private int borrowMoney;
    private int borrowNumber;//借款期数
    private String borrowTime;
    private String userPhone;
    private int totalInterest;//总利息
    private String auditStatus;//审核状态  0 待审核 1 以通过审核 2 驳回  默认0
    private String verify; //验证码
    private RepaymentVO repaymentVO;
    private UserVO userVO;



}
