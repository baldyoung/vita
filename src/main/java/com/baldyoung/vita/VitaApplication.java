package com.baldyoung.vita;

import com.baldyoung.vita.merchant.serverEndpoint.MerchantSystemMessageServerPoint;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@EnableScheduling
@EnableCaching
@MapperScan("com.baldyoung.vita.common.dao")
@SpringBootApplication
// 添加该注解后才能让拦截器生效
@ServletComponentScan
// @EnableTransactionManagement
public class VitaApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(VitaApplication.class);
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
        //解决WebSocket不能注入的问题
        MerchantSystemMessageServerPoint.setApplicationContext(configurableApplicationContext);
    }
    // 添加该调用
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/","file:static/");
    }

}
