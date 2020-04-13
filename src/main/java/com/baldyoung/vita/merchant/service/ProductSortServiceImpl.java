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

    public static final Integer DEFAULT_PRODUCT_GRADE = 0;

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
            return DEFAULT_PRODUCT_GRADE;
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

    /**
     * 新增一个商品的排序记录
     * @param productId
     */
    public void setProductGrade(Integer productId) {
        setProductGrade(productId, DEFAULT_PRODUCT_GRADE);
    }

    /**
     * 新增一个商品的排序记录
     * @param productId
     * @param productGrade
     */
    public void setProductGrade(Integer productId, Integer productGrade) {
        ProductSortEntity entity = new ProductSortEntity();
        entity.setProductId(productId);
        entity.setProductGrade(productGrade);
        productSortDao.insert(entity);
        productSortMap.put(productId, productGrade);
    }

    /**
     * 设置一堆的商品排序记录（这个描述非常棒！）
     * @param list
     */
    public void setProductGradeList(List<ProductSortEntity> list) {
        productSortDao.insertList(list);
        productSortMap.clear();
        for (ProductSortEntity entity : list) {
            productSortMap.put(entity.getProductId(), entity.getProductGrade());
        }
    }

}
