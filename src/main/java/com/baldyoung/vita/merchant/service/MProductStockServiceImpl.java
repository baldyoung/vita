package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.ProductDao;
import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.ProductStockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baldyoung.vita.common.utility.CommonMethod.isEmptyCollection;

@Service
public class MProductStockServiceImpl {

    @Autowired
    private ProductStockServiceImpl productStockService;

    @Autowired
    private ProductDao productDao;

    /**
     * 重置商品库存
     * 将商品固定库存同步到当前的商品库存
     * @throws ServiceException
     */
    public void productStockReset() throws ServiceException {
        productStockService.cancelAllStock();
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductIsShow(1);
        List<ProductEntity> productEntityList = productDao.selectProductWithCondition(productEntity);
        Map<Integer, Integer> stockMap = new HashMap();
        if (isEmptyCollection(productEntityList)) {
            return;
        }
        for (ProductEntity entity : productEntityList) {
            if (null != entity.getProductStockFlag() && 1 == entity.getProductStockFlag().intValue() && null != entity.getProductStock()) {
                stockMap.put(entity.getProductId(), entity.getProductStock());
            }
        }
        productStockService.setStockMap(stockMap);
    }
}
