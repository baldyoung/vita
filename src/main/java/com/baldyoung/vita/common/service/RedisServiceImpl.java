package com.baldyoung.vita.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    public void test() {
        //stringRedisTemplate.h
        Map<String,String> map=new HashMap<String,String>();
        map.put("key1","value1");
        map.put("key2","value2");
        map.put("key3","value3");
        map.put("key4","value4");
        map.put("key5","value5");
        stringRedisTemplate.opsForHash().putAll("map1",map);
        Map resultMap= stringRedisTemplate.opsForHash().entries("map1");
        List reslutMapList=stringRedisTemplate.opsForHash().values("map1");
        Set resultMapSet=stringRedisTemplate.opsForHash().keys("map1");
        String value=(String)stringRedisTemplate.opsForHash().get("map1","key1");

        // System.out.println("value:"+value);
        // System.out.println("resultMapSet:"+resultMapSet);
        // System.out.println("resultMap:"+resultMap);
        // System.out.println("resulreslutMapListtMap:"+reslutMapList);
    }
    //@Autowired
    //private StringRedisTemplate<String,String>redisTemplate;



}
