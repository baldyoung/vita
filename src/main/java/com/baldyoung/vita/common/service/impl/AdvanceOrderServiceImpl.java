package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.pojo.entity.ShoppingCartItem;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预订单服务
 */
@Service
public class AdvanceOrderServiceImpl {

    private Map<Integer, List<ShoppingCartItem>> advanceOrderMap;


    @PostConstruct
    public void init() {
        advanceOrderMap = new HashMap();
    }

    public void setAdvanceOrder(Integer roomId, List<ShoppingCartItem> itemList) {
        advanceOrderMap.put(roomId, itemList);
    }

    public List<ShoppingCartItem> getAdvanceOrder(Integer roomId) {
        return advanceOrderMap.get(roomId);
    }



}
