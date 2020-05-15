package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.DiningRoomDao;
import com.baldyoung.vita.common.dao.DiningRoomReservationDao;
import com.baldyoung.vita.common.pojo.dto.diningRoom.RoomInfoDto;
import com.baldyoung.vita.common.pojo.entity.DiningRoomEntity;
import com.baldyoung.vita.common.pojo.entity.DiningRoomReservationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MDiningRoomServiceImpl {
    @Autowired
    private DiningRoomDao diningRoomDao;

    @Autowired
    private DiningRoomReservationDao diningRoomReservationDao;

    /**
     * 获取就餐位的静态数据
     * @return
     */
    public List<RoomInfoDto> getAllDiningRoom() {
        List<DiningRoomEntity> entities = diningRoomDao.selectAll();
        List<Integer> roomIds = entities.stream().map(item -> item.getDiningRoomId()).collect(Collectors.toList());
        List<DiningRoomReservationEntity> reservationList = diningRoomReservationDao.selectTargetByRoomIds(roomIds);
        Map<Integer, DiningRoomReservationEntity> reservationMap = new HashMap();
        for (DiningRoomReservationEntity reservation : reservationList) {
            reservationMap.put(reservation.getDiningRoomId(), reservation);
        }
        List<RoomInfoDto> list = new ArrayList(entities.size());
        for (DiningRoomEntity entity :entities) {
            RoomInfoDto dto = new RoomInfoDto();
            dto.setDiningRoomId(entity.getDiningRoomId());
            dto.setDiningRoomName(entity.getDiningRoomName());
            dto.setDiningRoomGrade(entity.getDiningRoomGrade());
            dto.setDiningRoomStatus(entity.getDiningRoomStatus());
            dto.setDiningRoomInfo(entity.getDiningRoomInfo());
            DiningRoomReservationEntity reservation = reservationMap.get(entity.getDiningRoomId());
            dto.setReservationData(toReservationData(reservation));
            list.add(dto);
        }
        return list;
    }

    public void updateDiningRoom(DiningRoomEntity entity) {
        diningRoomDao.updateDiningRoom(entity);
    }


    private String toReservationData(DiningRoomReservationEntity entity) {
        if (null == entity) {
            return "";
        }
        return entity.getDiningDate() + "【" + entity.getDiningTime() + "】 " + entity.getCustomerName();
    }

}
