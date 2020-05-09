package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.CustomerMessageTypeEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CustomerMessageTypeDao {

    List<CustomerMessageTypeEntity> selectAll();

    CustomerMessageTypeEntity selectByCustomerMessageTypeId(@Param("id")Integer id);

    void insertCustomerMessageType(@Param("entity")CustomerMessageTypeEntity entity);

    void delete(@Param("id")Integer id);

    void updateCustomerMessageType(@Param("entity")CustomerMessageTypeEntity entity);

}
