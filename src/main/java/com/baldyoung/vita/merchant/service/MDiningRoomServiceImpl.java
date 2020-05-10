package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.DiningRoomDao;
import com.baldyoung.vita.common.pojo.dto.diningRoom.RoomInfoDto;
import com.baldyoung.vita.common.pojo.entity.DiningRoomEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MDiningRoomServiceImpl {
    @Autowired
    private DiningRoomDao diningRoomDao;

    /**
     * 获取就餐位的静态数据
     * @return
     */
    public List<RoomInfoDto> getAllDiningRoom() {
        List<DiningRoomEntity> entities = diningRoomDao.selectAll();
        List<RoomInfoDto> list = new ArrayList(entities.size());
        for (DiningRoomEntity entity :entities) {
            RoomInfoDto dto = new RoomInfoDto();
            dto.setDiningRoomId(entity.getDiningRoomId());
            dto.setDiningRoomName(entity.getDiningRoomName());
            dto.setDiningRoomGrade(entity.getDiningRoomGrade());
            dto.setDiningRoomStatus(entity.getDiningRoomStatus());
            dto.setDiningRoomInfo(entity.getDiningRoomInfo());
            list.add(dto);
        }
        return list;
    }



}
