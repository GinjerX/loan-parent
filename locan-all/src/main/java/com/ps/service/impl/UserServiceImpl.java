package com.ps.service.impl;

import com.ps.domain.UserVO;
import com.ps.mapper.UserMapper;
import com.ps.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;



    //根据用户id查询
    @Override
    public UserVO query(String phone) {
        UserVO userVO= userMapper.queryAll(phone);
        return userVO;
    }

    @Override
    public List<UserVO> queryUserAll() {
        List<UserVO> list = userMapper.queryUserAll();
        return list;
    }

    //根据身份证查询个人信息
    @Override
    public UserVO queyId(String idcard) {
        UserVO userVO= userMapper.query(idcard);
        return userVO;
    }

    @Override
    public void update(UserVO userVO) {
        userMapper.update(userVO);
    }

    @Override
    public void upload(UserVO userVO) {
        userMapper.upload(userVO);
    }

    @Override
    public void quota(UserVO userVO) {
        userMapper.quota(userVO);
    }

    @Override
    public void time() {
        System.out.println("hualalalalalalal");
    }


}
