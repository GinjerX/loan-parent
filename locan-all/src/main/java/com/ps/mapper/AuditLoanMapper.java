package com.ps.mapper;

import com.ps.domain.BorrowVO;
import com.ps.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Repository
public interface AuditLoanMapper {

    List<BorrowVO> queryAudit();

    BorrowVO queryBorrow(Integer id);

    //生成还款表
    void repayment(Integer borrowId, BigDecimal money, String time);

    //点击通过审核 修改用户状态
    void updateState(Integer borrowId);

    List<UserVO> queryUser();

    void updateUser(Integer id);

    void updateInterest(String interset);

    String queryInterset();

    BorrowVO queryCollection(String phone);
}
