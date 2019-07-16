package com.ps.service.impl;

import com.ps.domain.BorrowVO;
import com.ps.domain.CodeVO;
import com.ps.domain.UserVO;
import com.ps.mapper.BorrowMapper;
import com.ps.mapper.LoginMapper;
import com.ps.service.NoteCodeService;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class CodeServiceImpl implements NoteCodeService {
    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private BorrowMapper borrowMapper;

    /*public static void main(String[] args) {//测试
        new CodeService().getRandomNumber4();
    }*/


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
     * GET---有参测试 (方式一:手动在url后面加上参数)
     *
     * @date 2018年7月13日 下午4:19:23
     */
    public void doGetTestWayOne(String phone,String type) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        /*获取随机数*/
        String verify=getRandomNumber4();

        String url="http://v.juhe.cn/sms/send?mobile="+phone+"&tpl_id=169056&tpl_value="+verify+"&key=f51ba8fb794aa38f584e9fd793364e2f";
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url);

       loginMapper.loginCode(phone,verify,type);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间(单位毫秒)
                    .setConnectTimeout(5000)
                    // 设置请求超时时间(单位毫秒)
                    .setConnectionRequestTimeout(5000)
                    // socket读写超时时间(单位毫秒)
                    .setSocketTimeout(5000)
                    // 设置是否允许重定向(默认为true)
                    .setRedirectsEnabled(true).build();

            // 将上面的配置信息 运用到这个Get请求里
            httpGet.setConfig(requestConfig);

            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);

            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取数据库中的验证码
     * @param
     * @return
     */
    public CodeVO getCode(String userPhone,String type) {
        CodeVO code = loginMapper.getcode(userPhone,type);
        System.out.println("code: "+code);
        return code;
    }

}
