package com.baldyoung.vita;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@MapperScan("com.baldyoung.vita.common.dao")
@SpringBootApplication
// 添加该注解后才能让拦截器生效
@ServletComponentScan
public class VitaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VitaApplication.class, args);
    }

}
