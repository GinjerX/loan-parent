package com.ps.domain;

import lombok.Data;

@Data
public class UserVO {
    private Integer id;//用户编号
    private String phone;//手机号码/用户账号
    private String password;//账号密码
    private String username;//用户真实姓名
    private String identityCard;//身份证号
    private String registerTime;//注册时间
    private String opengingBank;//开户银行
    private String bankCard;//银行卡号
    private String identityCardardF;//身份证照片 (正面) 存储的URL地址
    private String identityCardardR;//身份证照片 (反面) 存储的URL地址
    private String bankImg;//银行卡照片
    private Integer quota;//额度
    private Integer auditStatus;//审核状态


}
