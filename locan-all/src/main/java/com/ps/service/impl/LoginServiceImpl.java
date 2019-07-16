package com.ps.service.impl;

import com.ps.domain.UserVO;
import com.ps.mapper.LoginMapper;
import com.ps.service.LoginService;
import com.ps.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public UserVO userLogin(String phone,String password) {
        UserVO userVO = loginMapper.userLogin(phone, password);
        return userVO;
    }
    @Override
    public void register(String phone, String password) {
        loginMapper.userLogin(phone,password);

    }



}
