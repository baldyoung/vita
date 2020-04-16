package com.baldyoung.vita;


import com.baldyoung.vita.common.dao.Z_TestDao;
import com.baldyoung.vita.common.service.RedisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequestMapping("/")
public class Z_DefaultController {
    @Autowired
    Z_TestDao dbTestDao;

    @Autowired
    RedisServiceImpl redisService;


    @GetMapping
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("vita/index.html");
    }

    @GetMapping("db-test")
    public String dbTest(HttpServletResponse response, HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1.服务部署成功");
        try {
            stringBuilder.append("2.数据库访问成功" + dbTestDao.showDataBaseCreateSQL());
        } catch (Exception e) {
            stringBuilder.append("2.数据库访问失败"+e);
        }
        String str = stringBuilder.toString();
        PrintWriter writer = response.getWriter();
        writer.println(str);
        return null;
    }

    @GetMapping("redis-test")
    public String redistTest(HttpServletResponse response) throws IOException {
        redisService.setTestAction();
        redisService.getTestAction();
        PrintWriter writer = response.getWriter();
        writer.println("当你看到这串字符时，代表redis服务正常");
        return null;
    }

}
