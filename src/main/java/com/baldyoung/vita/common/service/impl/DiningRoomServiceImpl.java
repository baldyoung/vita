package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.dao.DiningRoomDao;
import com.baldyoung.vita.common.pojo.entity.DiningRoomEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiningRoomServiceImpl {
    @Autowired
    private DiningRoomDao diningRoomDao;

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    @Autowired
    private BillServiceImpl billService;

    public List<DiningRoomEntity> getAllDiningRoom() {
        return diningRoomDao.selectAll();
    }

    public DiningRoomEntity getDiningRoom(Integer diningRoomId) {
        return diningRoomDao.selectByDiningRoomId(diningRoomId);
    }

    public void updateDiningRoom(DiningRoomEntity entity) {
        diningRoomDao.updateDiningRoom(entity);
    }

    public void deleteDiningRoom(Integer diningRoomId) throws ServiceException {
        diningRoomDao.deleteDiningRoom(diningRoomId);
        shoppingCartService.deleteShoppingCart(diningRoomId);
        billService.deleteBillNumberBuffer(diningRoomId);
    }

    public void addDiningRoom(DiningRoomEntity entity) {
        diningRoomDao.insertDiningRoom(entity);
        if (entity.getDiningRoomStatus() != null && entity.getDiningRoomStatus().intValue() != -1) {
            shoppingCartService.addShoppingCart(entity.getDiningRoomId());
        }
    }



    /**
     * 获取有效的就餐位编号
     * @return
     */
    public List<Integer> getValidDiningRoomIdList() {
        List<DiningRoomEntity> roomList = getAllDiningRoom();
        List<Integer> roomIds = new ArrayList(roomList.size());
        for (DiningRoomEntity entity : roomList) {
            if (entity.getDiningRoomStatus().intValue() == -1) {
                continue;
            }
            roomIds.add(entity.getDiningRoomId());
        }
        return roomIds;
    }




}
