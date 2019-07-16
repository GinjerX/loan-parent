package com.ps;

import static org.junit.Assert.assertTrue;

import com.ps.domain.UserVO;
import com.ps.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple H5App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
   // @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }
    //@Test
    public void test(){
        String phone = "136789456";
        //userService.query(phone);
        UserVO userVO = new UserVO();
        userVO.setBankCard("789456123");

    }
}
