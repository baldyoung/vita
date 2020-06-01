package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.DiningRoomEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiningRoomDao {


    List<DiningRoomEntity> selectAll();

    DiningRoomEntity selectByDiningRoomId(Integer diningRoomId);

    void updateDiningRoom(@Param("diningRoom")DiningRoomEntity diningRoomEntity);

    void deleteDiningRoom(@Param("diningRoomId")Integer diningRoomId);

    void insertDiningRoom(@Param("diningRoom")DiningRoomEntity diningRoomEntity);

}
