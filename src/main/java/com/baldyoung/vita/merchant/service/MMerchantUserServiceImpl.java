package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.MerchantUserDao;
import com.baldyoung.vita.common.pojo.entity.MerchantUserEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.*;
@Service
public class MMerchantUserServiceImpl {
    @Autowired
    private MerchantUserDao merchantUserDao;

    /**
     * 新增商家账号
     * @param userName
     * @param account
     * @param password
     * @param grade
     * @param currentUserGrade
     * @throws ServiceException
     */
    public void addMerchantUser(String userName, String account, String password, Integer grade, Integer currentUserGrade) throws ServiceException {
        if (grade.intValue() >= currentUserGrade.intValue()) {
            throw new ServiceException(MERCHANT_GRADE_FORBIDDEN);
        }
        MerchantUserEntity entity = new MerchantUserEntity();
        entity.setMerchantUserName(userName);
        entity.setMerchantUserAccount(account);
        entity.setMerchantUserPassword(password);
        entity.setMerchantUserGrade(grade);
        merchantUserDao.insertMerchantUserEntity(entity);
    }

    /**
     * 删除商家账号
     * @param targetUserId
     * @param currentUserGrade
     * @throws ServiceException
     */
    public void deleteMerchantUser(Integer targetUserId, Integer currentUserGrade) throws ServiceException {
        MerchantUserEntity entity = merchantUserDao.selectByMerchantUserId(targetUserId);
        if (null == entity || null == entity.getMerchantUserId()) {
            return ;
        }
        if (entity.getMerchantUserGrade().intValue() >= currentUserGrade.intValue()) {
            throw new ServiceException(MERCHANT_GRADE_FORBIDDEN);
        }
        merchantUserDao.deleteMerchantUserEntity(targetUserId);
    }

    /**
     * 修改商家账号
     * @param entity
     * @throws ServiceException
     */
    public void updateMerchantUser(MerchantUserEntity entity) throws ServiceException {
        if (null != entity.getMerchantUserAccount()) {
            MerchantUserEntity existsUser = merchantUserDao.selectByMerchantUserAccount(entity.getMerchantUserAccount());
            if (entity.getMerchantUserId().intValue() != existsUser.getMerchantUserId().intValue()) {
                throw new ServiceException(MERCHANT_ACCOUNT_EXISTS);
            }
        }
        merchantUserDao.updateMerchantUserEntity(entity);
    }

    /**
     * 商家账号登录核查
     * @param userAccount
     * @param password
     * @return
     * @throws ServiceException
     */
    public MerchantUserEntity loginCheck(String userAccount, String password) throws ServiceException {
        MerchantUserEntity entity = merchantUserDao.selectByMerchantUserAccount(userAccount);
        if (null == entity || null == entity.getMerchantUserId()) {
            throw new ServiceException(MERCHANT_LOGIN_FILED);
        }
        if (!entity.getMerchantUserPassword().equals(password)) {
            throw new ServiceException(MERCHANT_LOGIN_FILED);
        }
        return entity;
    }
}
