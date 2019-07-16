package com.ps.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ps.domain.*;
import com.ps.service.BorrowService;
import com.ps.service.NoteCodeService;
import org.apache.http.client.fluent.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
public class BorrowController {

    @Autowired
    private NoteCodeService noteCodeService;

    @Autowired
    private BorrowService borrowService;

    /**
     * 发送借款验证码
     *
     * @param phone
     * @param type
     * @return
     */
    @RequestMapping("/borrowVerify")
    public MessageVO checkCode(@RequestParam String phone, @RequestParam String type) {
        MessageVO messageVO = new MessageVO();
        noteCodeService.doGetTestWayOne(phone, type); //验证登录验证码 ,先获取电话和数据类型 存进数据库 1：是登录 2:注册 3：借款
        messageVO.setCode(200);
        messageVO.setMsg("验证码已发送");
        return messageVO;
    }

    /**
     * 借款请求
     *
     * @param borrowVO
     * @return
     */
    @RequestMapping("/borrow")
    public MessageVO borrow(@RequestBody BorrowVO borrowVO) {
        MessageVO<Object> messageVO = new MessageVO<>();
        System.out.println(borrowVO);
        //获取短信验证码
        CodeVO code = noteCodeService.getCode(borrowVO.getUserPhone(), borrowVO.getType());
        System.out.println("codeborrow:" + code);
        //判断贷款是否超出最大额度
        Integer maxQuota = borrowService.getMaxQuota(borrowVO.getUserPhone());
        if (maxQuota < borrowVO.getBorrowMoney()) {
            messageVO.setData("当前最大申请额度为：" + maxQuota);
            messageVO.setMsg("你申请的贷款超过了最大额度请重新申请");
            return messageVO;
        }
        if (code.getVerify() == null) {
            messageVO.setMsg("验证码不存在");
            return messageVO;
        }
        if (code.getVerify().equals(borrowVO.getVerify())) {//验证码正确
            borrowService.insertBorrow(borrowVO);//加入借款信息
            messageVO.setMsg("请等待审核...");
        }else{
            messageVO.setMsg("验证码不正确...");
        }
        return messageVO;
    }

    /**
     * 查询还款
     *
     * @return
     */
    @RequestMapping("/borrowCheck")
    public MessageVO check(@RequestParam String userPhone) {
        MessageVO message = new MessageVO();
        System.out.println("userphone:"+userPhone);

        BorrowVO queryState = borrowService.auditStatus(userPhone);//借款审核状态

        System.out.println(queryState);
        if(queryState==null){
            message.setMsg("queryState为null");
            return message;
        }

        if (queryState.getAuditStatus().equals("0")) {//0 :待审核
            message.setMsg("贷款正在审核中。。。");

        } else if (queryState.getAuditStatus().equals("2")) {//2： 已驳回
            message.setMsg("贷款已被驳回");

        } else if (queryState.getAuditStatus().equals("1")) {//1：借款已通过审核  进入查询还款计划
            message.setMsg("查询还款计划");
            //在管理员点击通过审核的时候生成还款表,所以生成还款表是管理员后台生成的
            //borrowService.repayment(queryState.getBorrowId());
            //这里只需要查询还款详情
            List<RepaymentVO> list = borrowService.queryRepayment(queryState.getBorrowId());
            message.setCode(200);
            message.setData(list);
            message.setMsg("还钱还钱还钱");
        }
        return message;
    }

    /**
     * 第三方接口还款
     *
     * @param id
     * @return
     */
    @RequestMapping("/requsetRepayment")
    public String requsetRepayment(@RequestParam Integer id) throws IOException {
        //MessageVO messageVo = new MessageVO();
        Content content = borrowService.requsetRepayment(id);
        System.out.println("requsetRe: "+content);
        //messageVo.setData(content);
        return content.toString();
    }

    /**
     * result {orderId code message}
     *
     * @return
     */
    @RequestMapping("/responesRepayment")
    public MessageVO respRepayment(@RequestParam("result") String result) {
        System.out.println("whahaohaoahao");
        MessageVO messageVO = new MessageVO();

        JSONObject json = JSONObject.parseObject(result);

        String results = json.getString("result");

        System.out.println("r.results:"+results);

        //修改状态为已还款
        borrowService.respRepayment(Integer.valueOf(results));

        messageVO.setData(results);

        messageVO.setMsg("已还款");

        return messageVO;
    }

    /**
     * 定时发送通知
     *
     * @return
     * @throws ParseException
     */
    @RequestMapping("/informTime")
    public MessageVO inform() throws ParseException {
        MessageVO message = new MessageVO();
        borrowService.inform();
        return message;
    }


}
