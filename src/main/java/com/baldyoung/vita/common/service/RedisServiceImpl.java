package com.baldyoung.vita.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.RedisTemplate;
import java.lang.reflect.Method;

//import org.springframework.data.redis.core.RedisTemplate;
public class RedisServiceImpl {
    @Autowired
    public RedisTemplate<String,Object> redisTemplate;

    //@Autowired
    //private StringRedisTemplate<String,String>redisTemplate;



}
