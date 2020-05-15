package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.DiningRoomReservationEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiningRoomReservationDao {

    List<DiningRoomReservationEntity> selectByRoomId(@Param("roomId")Integer roomId);

    List<DiningRoomReservationEntity> selectTargetByRoomIds(@Param("roomIds")List<Integer> roomIds);

    void insertReservationEntity(@Param("entity")DiningRoomReservationEntity entity);

    void updateReservationEntity(@Param("entity")DiningRoomReservationEntity entity);

    void deleteReservationEntity(@Param("recordId")Integer recordId);

    DiningRoomReservationEntity selectByRecordId(@Param("recordId")Integer reocrdId);

    void updateReservationOffTipStatus(@Param("roomId")Integer roomId);


}
