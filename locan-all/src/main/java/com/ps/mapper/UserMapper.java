package com.ps.mapper;

import com.ps.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    //查询用户信息
    UserVO queryAll(String phone);

    List<UserVO> queryUserAll();

    //根据身份证查询用户信息
    UserVO query(String idcard);

    //修改用户信息
    void update(UserVO userVO);

    void upload(UserVO userVO);

    void quota(UserVO userVO);
}
