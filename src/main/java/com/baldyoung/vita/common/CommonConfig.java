package com.baldyoung.vita.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 系统配置类
 */
@Component
@PropertySource("classpath:system.properties")
public class CommonConfig {

    @Value("${productImgPath}")
    public String productImgPath;

    @Value("${qrcodeImgPath}")
    public String qrcodeImgPath;

    @Value("${serveAddress}")
    public String serveAddress;

    @Value("${positionAddress}")
    public String positionAddress;

}
