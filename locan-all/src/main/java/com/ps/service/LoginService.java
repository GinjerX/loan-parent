package com.ps.service;

import com.ps.domain.UserVO;
import org.springframework.stereotype.Service;


public interface LoginService {

    UserVO userLogin(String phone,String password);

    void register(String phone,String password);

}
