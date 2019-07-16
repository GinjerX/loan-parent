package com.ps.mapper;


import com.ps.domain.CodeVO;
import com.ps.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LoginMapper {

    /**
     * 用户登录
     * @param phone
     * @param password
     * @return
     */
    UserVO userLogin(String phone, String password);

    /**
     * 用户注册
     * @param phone
     * @param password
     * @return
     */
    UserVO register(String phone, String password);

    /**
     * 存储验证码
     */
    void loginCode(String phone,String verify,String type);

    /**
     * 取出验证码
     */
    CodeVO getcode(String userPhone,String type);


}
