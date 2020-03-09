package com.baldyoung.vita;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.baldyoung.vita.common.dao")
@SpringBootApplication
public class VitaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VitaApplication.class, args);
    }

}
