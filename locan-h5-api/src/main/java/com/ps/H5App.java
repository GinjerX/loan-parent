package com.ps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class H5App
{
    public static void main( String[] args )
    {
        SpringApplication.run(H5App.class, args);
    }
}
