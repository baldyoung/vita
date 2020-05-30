package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.CommonConfig;
import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.entity.MerchantUserEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.merchant.service.MMerchantUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.*;
import static com.baldyoung.vita.common.utility.CommonMethod.isAnyEmpty;

@RestController
@RequestMapping("mUser")
public class MMerchantUserController {
    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private MMerchantUserServiceImpl mMerchantUserService;

    @PostMapping("login")
    public ResponseResult loginCheck(@RequestParam("a")String account,
                                     @RequestParam("p")String password,
                                     HttpSession session) throws ServiceException {
        if (isAnyEmpty(account, password)) {
            return defeat("账号和密码不能为空");
        }
        if (account.length() > 10) {
            return defeat("账号名称过长");
        }
        if (password.length() > 20) {
            return defeat("密码过长");
        }
        MerchantUserEntity entity = mMerchantUserService.loginCheck(account, password);
        session.setAttribute("merchantUserId", entity.getMerchantUserId());
        session.setAttribute("grade", entity.getMerchantUserGrade());
        session.setMaxInactiveInterval(commonConfig.merchantSessionTime);
        return success("/ls");
    }

    @PostMapping("logout")
    public ResponseResult loginOut(HttpSession session) {
        session.removeAttribute("merchantUserId");
        session.removeAttribute("grade");
        return success();
    }
}
