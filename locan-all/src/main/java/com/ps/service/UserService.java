package com.ps.service;

import com.ps.domain.UserVO;

import java.util.List;

public interface UserService {
    //查询用户所有信息(根据id用户编号查)
    UserVO query(String phone);
    /*查询所有用户*/
    List<UserVO> queryUserAll();

    //根据身份证查询个人信息
    UserVO queyId(String idcard);

    //编辑个人资料
    void update(UserVO userVO);

    //上传照片
    void upload(UserVO userVO);
    //评估金额
    void quota(UserVO userVO);

    void time();
}
