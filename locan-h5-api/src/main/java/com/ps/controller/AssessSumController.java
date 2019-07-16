package com.ps.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ps.domain.MessageVO;
import com.ps.domain.Result;
import com.ps.domain.UserVO;
import com.ps.mapper.UserMapper;
import com.ps.service.UserService;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class AssessSumController {
    @Autowired
    private UserService userService;


    /**
     * 贷款金额评估
     * @param key
     * @param idcard
     * @return
     * @throws IOException
     */
    @RequestMapping("/assess")
    public MessageVO doRequset(@RequestParam String key, @RequestParam String idcard) throws IOException {
        MessageVO messageVO = new MessageVO();

        UserVO userVO = userService.queyId(idcard);
        if(userVO!=null){//判断身份证是否存在

        }else{
            messageVO.setMsg("身份证号码不存在请完善个人信息");
        }

        String informUrl = "http://192.168.3.44:8081/getrequset";
        String url ="http://192.168.3.117:9000/valuation?key="+key+"&idcard="+idcard+"&notice_url="+informUrl;
        Content content = Request.Get(url).execute().returnContent();

        messageVO.setMsg("评估信息已发送");
        messageVO.setMsg(content.toString());
        return messageVO;

    }

    @RequestMapping("/getrequset")
    public String getRequset(@RequestParam("idcard") String idcard, @RequestParam("data") String data){

        JSON json = JSONObject.parseObject(data);
        Result result = JSONObject.toJavaObject(json,Result.class);

        UserVO userVO = new UserVO();
        userVO.setIdentityCard(idcard);
        userVO.setQuota(result.getMoney());

        userService.quota(userVO);

        return result.toString();
    }
}
