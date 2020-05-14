package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.CustomerMessageEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CustomerMessageDao {

    List<CustomerMessageEntity> selectByDiningRoomId(@Param("diningRoomId")Integer diningRoomId);

    CustomerMessageEntity selectByRecordId(@Param("recordId")Integer reocrdId);

    void insert(@Param("entity")CustomerMessageEntity entity);

    void deleteByRecordId(@Param("recordId")Integer recordId);

    void update(@Param("entity")CustomerMessageEntity entity);

    void deleteRecordForRoomId(@Param("roomId")Integer roomId);



}
