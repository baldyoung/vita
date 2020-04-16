package com.baldyoung.vita;

import com.baldyoung.vita.common.dao.Z_TestDao;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VitaApplication.class)
class VitaApplicationTests {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    Z_TestDao z_testDao;

    @Test
    void contextLoads() {
        //stringRedisTemplate.
        if (null != z_testDao) {
            out.println(z_testDao.showDataBaseCreateSQL().toString());
        } else {
            out.println("z_testDao is null");
        }
        if (null == stringRedisTemplate) {
            out.println("null is it");
            return;
        }
        stringRedisTemplate.opsForValue().set("test key", "baldyoung.com", 3333, TimeUnit.MINUTES);


    }

    @Test
    void requestDataFromRedis() {
        if (null == stringRedisTemplate) {
            out.println("null is it");
            return;
        }
        String value = stringRedisTemplate.opsForValue().get("test key");
        out.println(value);
    }

}
