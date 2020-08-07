package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.CommonConfig;
import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.entity.MerchantUserEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;
import com.baldyoung.vita.merchant.serverEndpoint.MerchantSystemMessageServerPoint;
import com.baldyoung.vita.merchant.service.MMerchantUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.io.IOException;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.*;
import static com.baldyoung.vita.common.utility.CommonMethod.*;

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
    public ResponseResult loginOut(HttpSession session) throws UtilityException, IOException {
        session.removeAttribute("merchantUserId");
        session.removeAttribute("grade");
        Integer userId = getMerchantUserIdFromSession(session);
        if (null != userId) {
            MerchantSystemMessageServerPoint.closeSocket(userId);
        }
        return success();
    }

    @GetMapping("merchantUserList")
    public ResponseResult getReadableMerchantUser(HttpSession session) throws UtilityException {
        Integer grade = getMerchantUserGradeFromSession(session);
        return success(mMerchantUserService.getReadableMerchantUser(grade));
    }

    @PostMapping("addMerchantUser")
    public ResponseResult addMerchantUser(@RequestParam("userName")String userName,
                                          @RequestParam("userAccount")String userAccount,
                                          @RequestParam("userPassword")String userPassword,
                                          @RequestParam("userGrade")Integer userGrade,
                                          HttpSession session) throws UtilityException, ServiceException {
        if (isAnyEmpty(userName, userAccount, userPassword)) {
            return defeat("请补全信息");
        }
        if (userName.length() > 20) {
            return defeat("名称不能超过20个字");
        }
        if (userAccount.length() > 10) {
            return defeat("登录名不能超过10个字");
        }
        if (userPassword.length() > 10) {
            return defeat("登录密码不能超过20个字");
        }
        Integer grade = getMerchantUserGradeFromSession(session);
        mMerchantUserService.addMerchantUser(userName, userAccount, userPassword, userGrade, grade);
        return success();
    }

    @PostMapping("deleteMerchantUser")
    public ResponseResult deleteMerchantUser(@RequestParam("userId")Integer userId,
                                             HttpSession session) throws UtilityException, ServiceException {
        Integer grade = getMerchantUserGradeFromSession(session);
        mMerchantUserService.deleteMerchantUser(userId, grade);
        return success();
    }

    @PostMapping("updateMerchantUser")
    public ResponseResult updateMerchantUser(@RequestParam("userId")Integer userId,
                                             @RequestParam(value = "userName", required = false)String userName,
                                             @RequestParam(value = "userAccount", required = false)String userAccount,
                                             @RequestParam(value = "userPassword", required = false)String userPassword,
                                             @RequestParam("userGrade")Integer userGrade,
                                             HttpSession session) throws UtilityException, ServiceException {
        if (null != userName && isEmpty(userName)) {
            return defeat("用户名称不能全由空格组成");
        }
        if (null != userAccount && isEmpty(userAccount)) {
            return defeat("登录名不能全由空格组成");
        }
        if (null != userPassword && isEmpty(userPassword)) {
            return defeat("密码不能全由空格组成");
        }
        if (!isEmpty(userName) && userName.length() > 20) {
            return defeat("名称不能超过20个字");
        }
        if (!isEmpty(userAccount) && userAccount.length() > 10) {
            return defeat("登录名不能超过10个字");
        }
        if (!isEmpty(userPassword) && userPassword.length() > 10) {
            return defeat("登录密码不能超过20个字");
        }
        Integer grade = getMerchantUserGradeFromSession(session);
        MerchantUserEntity entity = new MerchantUserEntity();
        entity.setMerchantUserId(userId);
        entity.setMerchantUserName(userName);
        entity.setMerchantUserAccount(userAccount);
        entity.setMerchantUserPassword(userPassword);
        entity.setMerchantUserGrade(userGrade);
        mMerchantUserService.updateMerchantUser(entity, grade);
        return success();
    }

    @GetMapping("currentUserInfo")
    public ResponseResult getCurrentUserInfo(HttpSession session) throws UtilityException {
        Integer userId = getMerchantUserIdFromSession(session);
        return success(mMerchantUserService.getTargetUserInfo(userId));

    }

    @PostMapping("updateCurrentUser")
    public ResponseResult updateCurrentUserInfo(@RequestParam(value = "userName", required = false)String userName,
                                                @RequestParam(value = "userAccount", required = false)String userAccount,
                                                @RequestParam(value = "userNewPassword", required = false)String userNewPassword,
                                                @RequestParam(value = "userOldPassword", required = false)String userOldPassword,
                                                //@RequestParam(value = "userGrade", required = false)Integer userGrade,
                                                HttpSession session) throws UtilityException, ServiceException {
        if (null != userName && isEmpty(userName)) {
            return defeat("用户名称不能全由空格组成");
        }
        if (null != userAccount && isEmpty(userAccount)) {
            return defeat("登录名不能全由空格组成");
        }
        if (null != userNewPassword && isEmpty(userNewPassword)) {
            return defeat("新密码不能全由空格组成");
        }
        if (null != userOldPassword && isEmpty(userOldPassword)) {
            return defeat("旧密码不能全由空格组成");
        }
        if (!isEmpty(userName) && userName.length() > 20) {
            return defeat("名称不能超过20个字");
        }
        if (!isEmpty(userAccount) && userAccount.length() > 10) {
            return defeat("登录名不能超过10个字");
        }
        if (!isEmpty(userNewPassword) && userNewPassword.length() > 10) {
            return defeat("新密码不能超过20个字");
        }
        if (null != userOldPassword && isEmpty(userOldPassword)) {
            return defeat("旧密码不能全由空格组成");
        }
        Integer userId = getMerchantUserIdFromSession(session);
        Integer userGrade = getMerchantUserGradeFromSession(session);
        MerchantUserEntity entity = new MerchantUserEntity();
        entity.setMerchantUserId(userId);
        entity.setMerchantUserName(userName);
        entity.setMerchantUserAccount(userAccount);
        entity.setMerchantUserPassword(userNewPassword);
        // entity.setMerchantUserGrade(userGrade);
        mMerchantUserService.updateCurrentMerchantUser(entity, userOldPassword, userGrade);
        return success();
    }

    


}
