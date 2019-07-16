package com.ps.service;

import com.ps.domain.BorrowVO;
import com.ps.domain.RepaymentVO;
import org.apache.http.client.fluent.Content;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface BorrowService {
    //插入一条借款数据
    void insertBorrow(BorrowVO borrowVO);

    BorrowVO auditStatus(String userPhone);

    Integer getMaxQuota(String phone);

    List<RepaymentVO> queryRepayment(int borrowId);

    void inform() throws ParseException;

    void respRepayment(Integer id);

    Content requsetRepayment(Integer id) throws IOException;

    void overdue();

}
