package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.MerchantUserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MerchantUserDao {

    void insertMerchantUserEntity(@Param("user")MerchantUserEntity user);

    void deleteMerchantUserEntity(@Param("userId")Integer userId);

    void updateMerchantUserEntity(@Param("user")MerchantUserEntity user);

    MerchantUserEntity selectByMerchantUserId(@Param("userId")Integer userId);

    MerchantUserEntity selectByMerchantUserAccount(@Param("account")String account);

    List<MerchantUserEntity> selectWithMaxGrade(@Param("maxGrade")Integer maxGrade);

}
