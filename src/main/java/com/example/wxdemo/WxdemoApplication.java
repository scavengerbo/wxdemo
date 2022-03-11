package com.example.wxdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.wxdemo.db.mapper")
public class WxdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxdemoApplication.class, args);
    }

}
