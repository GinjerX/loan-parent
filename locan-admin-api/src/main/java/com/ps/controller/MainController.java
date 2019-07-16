package com.ps.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @RequestMapping("/test")
    public String demo(){
        System.out.println("测试成功");
        return "成功";
    }
}
