package com.baldyoung.vita;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@EnableScheduling
@EnableCaching
@MapperScan("com.baldyoung.vita.common.dao")
@SpringBootApplication
// 添加该注解后才能让拦截器生效
@ServletComponentScan
public class VitaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VitaApplication.class, args);
    }
    // 添加该调用
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/","file:static/");
    }

}
