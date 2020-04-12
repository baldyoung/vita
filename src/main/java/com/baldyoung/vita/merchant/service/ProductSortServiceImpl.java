package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.ProductSortDao;
import com.baldyoung.vita.common.pojo.entity.ProductSortEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductSortServiceImpl {

    @Autowired
    ProductSortDao productSortDao;

    private Map<Integer, Integer> productSortMap;


    @PostConstruct
    public void init() {
        productSortMap = getAllProductSortMapFromDB();
    }

    /**
     * 获取指定商品的商品等级
     * @param productId
     * @return
     */
    public Integer getProductGradeByProductId(Integer productId) {
        if (null == productSortMap) {
            return 0;
        }
        return productSortMap.get(productId);
    }

    /**
     * 获取当前缓存的商品等级图
     * @return
     */
    public Map<Integer, Integer> getAllProductSortMap() {
        return productSortMap;
    }

    /**
     * 从数据库获取商品等级图
     * @return
     */
    public Map<Integer, Integer> getAllProductSortMapFromDB() {
        List<ProductSortEntity> list = productSortDao.selectAll();
        Map<Integer, Integer> map = new HashMap();
        for (ProductSortEntity entity : list) {
            map.put(entity.getProductId(), entity.getProductGrade());
        }
        return map;
    }

}
