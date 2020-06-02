package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.DiningRoomDao;
import com.baldyoung.vita.common.dao.DiningRoomReservationDao;
import com.baldyoung.vita.common.pojo.dto.diningRoom.RoomInfoDto;
import com.baldyoung.vita.common.pojo.entity.DiningRoomEntity;
import com.baldyoung.vita.common.pojo.entity.DiningRoomReservationEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.DiningRoomServiceImpl;
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

    @Autowired
    private DiningRoomServiceImpl diningRoomService;

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

    /**
     * 修改指定就餐位的数据
     * @param entity
     */
    public void updateDiningRoom(DiningRoomEntity entity) {
        diningRoomDao.updateDiningRoom(entity);
    }

    /**
     * 获取所有就餐位数据
     * @return
     */
    public List<DiningRoomEntity> getDiningRoomList() {
        return diningRoomDao.selectAll();
    }

    /**
     * 获取指定编号的就餐位信息
     * @param roomId
     * @return
     */
    public DiningRoomEntity getDiningRoom(Integer roomId) {
        return diningRoomDao.selectByDiningRoomId(roomId);
    }

    /**
     * 新增就餐位
     * @param roomName
     * @param roomGrade
     * @param roomInfo
     */
    public void addDiningRoom(String roomName, Integer roomGrade, String roomInfo) {
        DiningRoomEntity entity = new DiningRoomEntity();
        entity.setDiningRoomName(roomName);
        entity.setDiningRoomGrade(roomGrade);
        entity.setDiningRoomInfo(roomInfo);
        entity.setDiningRoomStatus(0);
        // diningRoomDao.insertDiningRoom(entity);
        diningRoomService.addDiningRoom(entity);
    }

    /**
     * 删除指定就餐位
     * @param roomId
     */
    public void deleteDiningRoom(Integer roomId) throws ServiceException {
        diningRoomDao.deleteDiningRoom(roomId);
        diningRoomService.deleteDiningRoom(roomId);
    }



    /**
     * 设置预定消息
     * @param entity
     * @return
     */
    private String toReservationData(DiningRoomReservationEntity entity) {
        if (null == entity) {
            return "";
        }
        return entity.getDiningDate() + "【" + entity.getDiningTime() + "】 " + entity.getCustomerName();
    }



}
