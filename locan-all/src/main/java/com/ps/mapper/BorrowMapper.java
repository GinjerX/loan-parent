package com.ps.mapper;

import com.ps.domain.BorrowVO;
import com.ps.domain.RepaymentVO;
import com.ps.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Repository
public interface BorrowMapper {

    /*插入接款信息*/
    void borrow(BorrowVO borrowVO);

    Integer getMaxQuota(String phone);

    BorrowVO borrowStatus(String userPhone);

    List<RepaymentVO> queryRepayment(int borrowId);

    void repaymentMoney(Integer id);

    //获取需本期还款的贷款
    RepaymentVO getRepayment(Integer borrowId);

    List<RepaymentVO> overdue();

    BorrowVO informUser(Integer id);
    /*informstate4 查询状态为4*/
    List<RepaymentVO> informstate4();

    void updateMoney(BigDecimal money);

    void updateOverdueState(Integer id);
}
