package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.dao.BillDao;
import com.baldyoung.vita.common.pojo.entity.BillEntity;
import com.baldyoung.vita.common.utility.UniqueCodeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class BillServiceImpl {
    @Autowired
    private BillDao billDao;

    private static final int BILL_NUMBER_LENGTH = 30;

    private UniqueCodeModule uniqueCodeModule;

    private Map<Integer, String> billNumberMap;

    @PostConstruct
    public void init() {
        uniqueCodeModule = UniqueCodeModule.getInstance("bill", "tt");
        billNumberMap = new HashMap();
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
        entity.setBillNumber(billNumber);
        entity.setBillOwnerTypeFlag(ownerTypeFlag);
        entity.setBillOwnerId(ownerId);
        billDao.insertBillEntity(entity);
        return billNumber;
    }

    /**
     * 获取指定就餐位的当前账单编号
     * @param roomId
     * @return
     */
    public String getRoomBillNumber(Integer roomId) {
        String billNumber = billNumberMap.get(roomId);
        if (null == billNumber) {
            billNumber = createNewBill(0, roomId);
        }
        billNumberMap.put(roomId, billNumber);
        return billNumber;
    }







}
