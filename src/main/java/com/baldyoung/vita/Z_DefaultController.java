package com.baldyoung.vita;


import com.baldyoung.vita.common.dao.Z_TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Z_DefaultController {
    @Autowired
    Z_TestDao z_testDao;

    @GetMapping
    public String defaultMethod() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1.服务部署成功");
        try {
            stringBuilder.append("2.数据库访问成功" + z_testDao.showDataBaseCreateSQL());
        } catch (Exception e) {
            stringBuilder.append("2.数据库访问失败"+e);
        }
        return stringBuilder.toString();
    }

}
