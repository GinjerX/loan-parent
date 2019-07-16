package com.ps.service.impl;

import com.ps.domain.BorrowVO;
import com.ps.domain.RepaymentVO;
import com.ps.mapper.AuditLoanMapper;
import com.ps.mapper.BorrowMapper;
import com.ps.service.BorrowService;
import com.ps.utils.MD5Util;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BorrowServiceImpl implements BorrowService {
    @Value("${date}")
    private String date;

    @Autowired
    private BorrowMapper borrowMapper;
    @Autowired
    private CodeServiceImpl codeService;
    @Autowired
    private AuditLoanMapper auditLoanMapper;

    @Override
    public void insertBorrow(BorrowVO borrowVO) {
        borrowMapper.borrow(borrowVO);
    }

    @Override
    public BorrowVO auditStatus(String userPhone) {
        BorrowVO state = borrowMapper.borrowStatus(userPhone);
        return state;
    }

    //查询最大额度
    @Override
    public Integer getMaxQuota(String phone) {
        Integer quota = borrowMapper.getMaxQuota(phone);
        return quota;
    }

    @Override
    public List<RepaymentVO> queryRepayment(int borrowId) {
        List<RepaymentVO> list = borrowMapper.queryRepayment(borrowId);
        return list;
    }

    /**
     * 还款第三方接口
     *
     * @param id
     */
    @Override
    public Content requsetRepayment(Integer id) throws IOException {

        RepaymentVO vo = borrowMapper.getRepayment(id);
        if(vo ==null){
            return null;
        }
        String key = "xiao20190703";
        String notice_url = "http://192.168.3.44:8081/responesRepayment";
        String orderId = codeService.getRandomNumber4();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("key", "xiao20190703");
        map.put("orderId", orderId);
        map.put("notice_url", notice_url);
        map.put("money", vo.getMoney());
        map.put("timeMillis", System.currentTimeMillis());
        map.put("secret_key", "dskfjwerqe24");

        List<String> keyset = new ArrayList<>(map.keySet());
        Collections.sort(keyset, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });
        Map<String, Object> setmap = new LinkedHashMap<>();
        for (int i = keyset.size() - 1; i >= 0; i--) {
            for (String s : map.keySet()) {
                if (keyset.get(i).equals(s)) {
                    setmap.put(keyset.get(i), map.get(s));
                }
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (String i : setmap.keySet()) {
            stringBuffer.append(setmap.get(i));
        }
        String md5 = MD5Util.getMd5Code(stringBuffer.toString());

        String url = "http://192.168.3.117:9000/prePay?key="
                + key + "&orderId=" + orderId + "&notice_url=" + notice_url + "&money=" + vo.getMoney() + "&timeMillis=" + setmap.get("timeMillis") + "&md5=" + md5;

            Content content = Request.Get(url).execute().returnContent();
            System.out.println("content: "+content);
            return content;
    }

    /**
     * 还款返回二微码
     * 还款完成
     *
     * @param id
     */
    public void respRepayment(Integer id) {
        borrowMapper.repaymentMoney(id);
    }
    @Override
    @Scheduled(cron = "0 0 10 * * * ")
    public void inform() throws ParseException {
        List<RepaymentVO> list = borrowMapper.overdue();

        String[] strings = date.split(",");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < list.size(); i++) {
            Date date = format.parse(list.get(i).getTime());
            for (int j = 0; j < strings.length; j++) {
                String time2 = format.format(changeDay(Integer.parseInt(strings[j])));
                if (time2.equals(format.format(date))) {
                    //获取用户信息发送短信通知
                    BorrowVO borrowVO = borrowMapper.informUser(list.get(i).getId());
                    //根据还款表id查询用户信息
                    shortNote(borrowVO.getUserVO().getPhone());
                }
            }
        }
    }

    /**
     * 还款通知 提前五天通知一次 提前三天通知一次 当天通知一次
     * 发送短信
     *
     * @param phone
     */
    public void shortNote(String phone) {

        //发送短信提醒还款  这是第三方提醒还款
        String inforn = "还钱还钱";
        String url = "http://v.juhe.cn/sms/send?mobile=" + phone + "&tpl_id=169056&tpl_value=" + inforn + "&key=f51ba8fb794aa38f584e9fd793364e2f";

        try {
            Content content = Request.Get(url).execute().returnContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到自己加减过后的天数
     *
     * @param day
     * @return
     */
    public static Date changeDay(int day) {
        Calendar calendar = Calendar.getInstance();//得到时间
        calendar.add(Calendar.DATE, day);//得到 当前时间的前几天
        return calendar.getTime();
    }

    public void overdueTime() {
        List<RepaymentVO> repaymentVOS = borrowMapper.overdue();
        Calendar c1 = Calendar.getInstance();//当前时间
        c1.add(Calendar.DATE, 1);
        for (int i = 0; i < repaymentVOS.size(); i++) {
            if (repaymentVOS.get(i).getTime().equals(c1)) {//已逾期
                borrowMapper.updateOverdueState(repaymentVOS.get(i).getId());//修改已逾期的状态
            }
        }
    }

    public void overdue() {
        List<RepaymentVO> repaymentVOS = borrowMapper.informstate4();//获取到所有状态都为4（已逾期） 的还款 已经返回时间差

        for (int i = 0; i < repaymentVOS.size(); i++) {
            //获取到时间差 * 5
            Integer overdue = repaymentVOS.get(i).getOverdue() * 5;//已逾期后额外每天+5 这是额外加的钱

            BigDecimal money = repaymentVOS.get(i).getMoney().add(BigDecimal.valueOf(overdue));

            //修改还款表的实际还款金额
            borrowMapper.updateMoney(money);
        }
    }
    /* public void overdue() {
        //查询所有已逾期的
        List<RepaymentVO> repaymentVOS = borrowMapper.informstate4();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c1 = Calendar.getInstance();//
        Calendar c2 = Calendar.getInstance();//当前时间

        int day1 = c2.get(Calendar.DATE);
        for (int i = 0; i < repaymentVOS.size(); i++) {

            Date date = null;//获取到具体还款时间 格式化

            try {
                date = format.parse(repaymentVOS.get(i).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            c1.setTime(date);
            int day = c1.get(Calendar.DATE);
            int overdue= day1-day;
            if(overdue>=1){
                overdue=overdue*5;//已逾期加五块
            }

            BigDecimal money = repaymentVOS.get(i).getMoney().add(BigDecimal.valueOf(overdue));
            //修改还款表的实际还款金额
            borrowMapper.updateMoney(money);
        }

    }
*/

}
