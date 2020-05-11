package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.dao.BillDao;
import com.baldyoung.vita.common.pojo.entity.BillEntity;
import com.baldyoung.vita.common.pojo.entity.DiningRoomEntity;
import com.baldyoung.vita.common.utility.UniqueCodeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baldyoung.vita.common.utility.CommonMethod.isEmpty;

@Service
public class BillServiceImpl {
    @Autowired
    private BillDao billDao;

    @Autowired
    private DiningRoomServiceImpl diningRoomService;

    private static final int BILL_NUMBER_LENGTH = 30;

    private UniqueCodeModule uniqueCodeModule;

    private Map<Integer, String> billNumberMap;

    @PostConstruct
    public void init() {
        uniqueCodeModule = UniqueCodeModule.getInstance("bill", "tt");
        billNumberMap = new HashMap();
        List<DiningRoomEntity> entities = diningRoomService.getAllDiningRoom();
        for (DiningRoomEntity entity :entities) {
            if (!isEmpty(entity.getCurrentBillNumber())) {
                billNumberMap.put(entity.getDiningRoomId(), entity.getCurrentBillNumber());
            }
        }
    }

    /**
     * 新建一个账单
     * @param ownerTypeFlag
     * @param ownerId
     * @return
     */
    private String createNewBill(Integer ownerTypeFlag, Integer ownerId) {
        BillEntity entity = new BillEntity();
        String billNumber = uniqueCodeModule.getUniqueCode(BILL_NUMBER_LENGTH);
        DiningRoomEntity diningRoomEntity = diningRoomService.getDiningRoom(ownerId);
        entity.setBillNumber(billNumber);
        entity.setBillOwnerTypeFlag(ownerTypeFlag);
        entity.setBillOwnerId(ownerId);
        entity.setBillOwnerName(diningRoomEntity.getDiningRoomName());
        billDao.insertBillEntity(entity);
        return billNumber;
    }

    public void deleteBillNumberBuffer(Integer roomId) {
        billNumberMap.put(roomId, null);
    }

    /**
     * 获取指定就餐位的当前账单编号, 如果不存在则创建
     * @param roomId
     * @return
     */
    public String getRoomBillNumber(Integer roomId) {
        String billNumber = billNumberMap.get(roomId);
        if (null == billNumber) {
            billNumber = createNewBill(0, roomId);
        }
        DiningRoomEntity entity = new DiningRoomEntity();
        entity.setDiningRoomId(roomId);
        entity.setCurrentBillNumber(billNumber);
        diningRoomService.updateDiningRoom(entity);
        billNumberMap.put(roomId, billNumber);
        return billNumber;
    }

    /**
     * 获取指定就餐位的当前账单编号
     * @param roomId
     * @return
     */
    public String getRoomBillNumberWithoutCreate(Integer roomId) {
        String billNumber = billNumberMap.get(roomId);
        return billNumber;
    }

    public BillEntity getBill(String billNumber) {
        BillEntity entity = billDao.selectBill(billNumber);
        return entity;
    }









}
