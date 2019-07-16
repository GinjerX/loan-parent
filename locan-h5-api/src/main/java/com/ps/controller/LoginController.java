package com.ps.controller;

import com.ps.domain.CodeVO;
import com.ps.domain.MessageVO;
import com.ps.domain.UserVO;
import com.ps.service.LoginService;
import com.ps.service.NoteCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private LoginService service;

    @Autowired
    private NoteCodeService codeServiceImpl;

    /**
     * 存储验证码
     * @param phone
     * @param type
     * @return
     */
    @RequestMapping("/verify")
    public MessageVO checkCode(@RequestParam String phone,@RequestParam String type){
        MessageVO messageVO = new MessageVO();
        codeServiceImpl.doGetTestWayOne(phone, type); //验证登录验证码 ,先获取电话和数据类型 存进数据库 1：是登录 2:注册 3：借款
        messageVO.setMsg("验证码已发送");
        return messageVO;
    }

    /**
     * ？：如果发送验证码时的电话和点击登录时的电话不一致怎么办？？
     * //判断用户手机号码存不存在否则验证码错误
     * @param phone     用户电话号码或者账号
     * @param password  用户密码
     * @return
     */
    @GetMapping("/userlogin")
    public MessageVO login(@RequestParam String phone, @RequestParam String password,@RequestParam String code,@RequestParam String type) {
        System.out.println("123123123123");
        MessageVO messageVO = new MessageVO();

        UserVO userVO = service.userLogin(phone, password);

        if (userVO != null ) {//账号存在
            CodeVO verify = codeServiceImpl.getCode(phone,type);//根据phone取出数据库存储的验证码,短信表找不到这个手机号码表示验证码发送错误
            if(verify==null){
                messageVO.setMsg("验证码不存在");
            }
            System.out.println("code"+code);
            System.out.println("verify:"+verify);
            if (code.equals(verify.getVerify())) {//判断验证码是否正确 （如果第二次登录时，那验证码是第一次的验证码还是第二次的验证码？怎么分辨)
                messageVO.setMsg("登录成功");
            } else {
                messageVO.setMsg("验证码输入错误");
            }
        } else {
            messageVO.setMsg("请输入正确的账号和密码");
        }
        return messageVO;
    }

    /**
     * 用户注册
     * @param phone
     * @param password
     * @param type
     * @return
     */
    @RequestMapping("/register")
    public MessageVO regiser(@RequestParam String phone, @RequestParam String password, @RequestParam String code,@RequestParam String type) {

        MessageVO messageVO = new MessageVO();
        UserVO userVO = service.userLogin(phone, password);//判断账号是否已经存在

        codeServiceImpl.doGetTestWayOne(phone, "2"); //验证注册验证码 ,先获取电话和数据类型 存进数据库 2：是注册

        if (userVO == null) {//账号不存在
            CodeVO verify = codeServiceImpl.getCode(phone,type);//根据phone获取数据库code,如果短信表没有这条手机号码表示短信发错了
            if(verify==null){
                messageVO.setMsg("找不到这个验证码");
            }
            if(code.equals(verify.getVerify())){//效验短信验证码
                service.register(phone, password);
                messageVO.setMsg("注册成功");
            }else{
                messageVO.setMsg("验证码输入错误");
            }
        } else {
            messageVO.setMsg("你注册的手机号码已存在");
        }
        return messageVO;
    }

}
