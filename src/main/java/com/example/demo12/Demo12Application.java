package com.example.demo12;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//@EnableWebMvc//解决了Swagger报错
@SpringBootApplication
@MapperScan("com.example.demo12.mapper")
public class Demo12Application {

    public static void main(String[] args) {

        SpringApplication.run(Demo12Application.class, args);
    }

}
