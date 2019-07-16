package com.ps.service.impl;

import com.ps.domain.BorrowVO;
import com.ps.domain.UserVO;
import com.ps.mapper.AuditLoanMapper;
import com.ps.mapper.BorrowMapper;
import com.ps.service.AuditLoansServicse;
import com.ps.utils.MD5Util;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AuditLoansServiceImpl implements AuditLoansServicse {

    @Autowired
    private AuditLoanMapper auditLoanMapper;

    @Override
    public List<BorrowVO> queryAudit() {
        List<BorrowVO> list =auditLoanMapper.queryAudit();
        return list;
    }

    /**
     * 生成还款计划
     * @param borrowId
     */
    public void repayment(Integer borrowId){

        //1.根据借钱的id 查询借钱信息
        BorrowVO borrowVO = auditLoanMapper.queryBorrow(borrowId);

        //2.算出每期还款
        Integer money = borrowVO.getBorrowMoney();//借款的金额
        Integer number = borrowVO.getBorrowNumber();//还款总期数

        int totalInterest = borrowVO.getTotalInterest();//还款总利息
        BigDecimal sum= BigDecimal.valueOf((money+totalInterest)/number);//每期还款金

        //时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //还款表生成
        for (int i = 0; i < number; i++) {
            calendar.add(Calendar.MONTH, 1);//增加一个月
            Date dates = calendar.getTime();
            String startDateString = sdf.format(dates);
            auditLoanMapper.repayment(borrowId, sum, startDateString);//插入还款数据
        }
        //修改借款表审核状态
        auditLoanMapper.updateState(borrowId);
    }

    /**
     * 查询用户
     * @return
     */
    @Override
    public List<UserVO> queryUser() {
        List<UserVO> list=auditLoanMapper.queryUser();
        if(list!=null){
            return list;
        }
        return null;
    }

    @Override
    public void updateUser(Integer id) {
        auditLoanMapper.updateUser(id);
    }

    @Override
    public String getInterest() {
        String inerest = auditLoanMapper.queryInterset();
        return inerest;
    }

    @Override
    public void updateInterest(String interset) {
        auditLoanMapper.updateInterest(interset);
    }

    //随机生成4为验证码
    public String getRandomNumber4(){
        String code = "";
        for(int i=0;i<4;i++){
            int random = (int)(Math.random()*10);
            code += String.valueOf(random);
        }
        System.out.println("验证码为："+code);
        return code;
    }

    /**
     * 催收
     * 第三方接口
     * @param phone
     */
    public void getCollection(String phone){
        String orderId =getRandomNumber4();

        BorrowVO borrowVO =auditLoanMapper.queryCollection(phone);

        BigDecimal money = borrowVO.getRepaymentVO().getMoney();//需还款金额
        String idcard = borrowVO.getUserVO().getIdentityCard();//用户身份证
        String key ="xiao20190703";

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("key",key);
        map.put("orderId",orderId);
        map.put("idcard",idcard);
        map.put("money",money);
        map.put("phone",phone);
        map.put("secret_key","gwewerer1er3g");

        List<String> keyset = new ArrayList<>(map.keySet());
        Collections.sort(keyset, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });
        Map<String,Object> setmap = new LinkedHashMap<>();
        for (int i = keyset.size()-1; i >=0 ; i--) {
            for (String s:map.keySet()) {
                if(keyset.get(i).equals(s)){
                    setmap.put(keyset.get(i),map.get(s));
                }
            }
        }
        System.out.println("setmap: "+setmap);
        StringBuffer stringBuffer = new StringBuffer();
        for (String i: setmap.keySet()) {
            stringBuffer.append(setmap.get(i));
        }
        String md5 = MD5Util.getMd5Code(stringBuffer.toString());

        String url = "http://192.168.3.117:9000/debt?key="
                +key+"&orderId="+orderId+"&idcard="+idcard+"&money="+money+"&phone="+phone+"&md5="+md5;

        try {
            Content content = Request.Get(url).execute().returnContent();
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
