package com.baldyoung.vita.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

//import org.springframework.data.redis.core.RedisTemplate;
@Service
public class RedisServiceImpl {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void setTestAction() {
        stringRedisTemplate.opsForValue().set("test key", "baldyoung.com", 3333, TimeUnit.MINUTES);
    }

    public void getTestAction() {
        String value = stringRedisTemplate.opsForValue().get("test key");
        out.println(value);
    }
    //@Autowired
    //private StringRedisTemplate<String,String>redisTemplate;



}
