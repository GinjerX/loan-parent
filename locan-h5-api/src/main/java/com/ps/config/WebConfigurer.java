package com.ps.config;

import com.ps.config.intercepors.CrossDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Autowired
    private CrossDomain crossDomain;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //相对于一个靶子,除了这个路径下的文件,其他都可以进入
        registry.addInterceptor(crossDomain).addPathPatterns("/**").excludePathPatterns("/xxxxxxxxx6666");
    }
}
