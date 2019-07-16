package com.ps.controller;

import com.ps.domain.BorrowVO;
import com.ps.domain.MessageVO;
import com.ps.domain.UserVO;
import com.ps.service.AuditLoansServicse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class AuditLoansController {
    @Autowired
    private AuditLoansServicse auditLoansServicse;


    /**
     * 审核贷款
     * 1.查询出所有用户的待审核贷款
     * 2.点击通过审核 生成还款计划表
     *
     * @return
     */
    @ApiOperation(value = "chaxun")
    @PostMapping("/auditloansQuery")
    public MessageVO auditloansQuery() {
        MessageVO message = new MessageVO();
        List<BorrowVO> list = auditLoansServicse.queryAudit();
        message.setData(list);
        message.setMsg("chauxnchenggon");
        System.out.println("list:" + list);
        return message;
    }

    @RequestMapping("/auditloans")
    public MessageVO auditloans(Integer borrowId) {
        MessageVO message = new MessageVO();
        //点击通过审核 生成还款计划表 修改状态为通过审核
        auditLoansServicse.repayment(borrowId);
        message.setMsg("用户通过审核");
        return message;
    }

    /**
     * 审核用户信息
     * 1.查询出所有的用户
     * 2.修改用户状态W为已通过审核
     *
     * @return
     */
    @RequestMapping("/auditUserQuery")
    public MessageVO auditUserQuery() {
        MessageVO message = new MessageVO();
        List<UserVO> list = auditLoansServicse.queryUser();
        message.setMsg("查询用户信息");
        message.setData(list);

        return message;
    }

    /**
     * 点击通过用户审核
     */
    @RequestMapping("/aduitUser")
    public MessageVO aduitUser(@RequestParam Integer id) {
        MessageVO message = new MessageVO();
        auditLoansServicse.updateUser(id);
        message.setMsg("编辑成功");
        return message;
    }

    /**
     * 设置利息
     */
    @RequestMapping("/inerest")
    public MessageVO getInerest(@RequestParam String interset) {
        MessageVO message = new MessageVO();
        auditLoansServicse.updateInterest(interset);
        message.setMsg("修改利息成功");
        return message;
    }

    @RequestMapping("/inerestQuery")
    public MessageVO inerestQuery() {
        MessageVO message = new MessageVO();
        String inerest = auditLoansServicse.getInterest();
        message.setMsg("");
        message.setData(inerest);
        return message;
    }

    /**
     * 点击给用户发送催收信息
     *
     * @param phone
     * @return
     */
    @RequestMapping("/collection")
    public MessageVO collection(String phone) {
        MessageVO message = new MessageVO();
        auditLoansServicse.getCollection(phone);
        message.setMsg("已催收");
        return message;
    }


}
