package com.ps.service;

import com.ps.domain.BorrowVO;
import com.ps.domain.UserVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

public interface AuditLoansServicse {
    //后台审核贷款
    List<BorrowVO> queryAudit();
    //后台生成还款计划表
    void repayment(Integer borrowId);

    //void updateAudit(Integer borrowId);

    List<UserVO> queryUser();
    //修改用户状态
    void updateUser(Integer id);

    String getInterest();

    void updateInterest(String interset);
    //催收
    void getCollection(String phone);
}
