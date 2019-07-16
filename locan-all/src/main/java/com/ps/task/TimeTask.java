package com.ps.task;

import com.ps.service.BorrowService;
import com.ps.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class TimeTask {
    @Autowired
    private UserService userService;
    @Autowired
    private BorrowService borrowService;

    @Scheduled(cron = "0/6 * * * * * ")
    public void checktime(){
        userService.time();
    }

}
