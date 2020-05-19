package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.DiningRoomPositionEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiningRoomPositionDao {

    List<DiningRoomPositionEntity> selectAll();

    void insertPosition(@Param("position")DiningRoomPositionEntity position);

    void updatePosition(@Param("position")DiningRoomPositionEntity position);

    void deletePositionByPositionId(@Param("positionId")Integer positionId);

    void deletePositionByDiningRoomId(@Param("diningRoomId")Integer diningRoomId);


}
