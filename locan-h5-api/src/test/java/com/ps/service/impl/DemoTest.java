package com.ps.service.impl;

import com.ps.domain.UserVO;
import com.ps.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DemoTest {
    @Autowired
    private UserServiceImpl userService;
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }
    @Test
    public void test(){
        String phone = "136789456";
        //userService.query(phone);
        UserVO userVO = new UserVO();
        userVO.setBankCard("789456123");
        userService.update(userVO);

    }
}
