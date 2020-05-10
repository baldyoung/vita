package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.ProductSortDao;
import com.baldyoung.vita.common.pojo.entity.ProductSortEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品排序服务
 */
@Service
public class MProductSortServiceImpl {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProductSortDao productSortDao;

    private Map<Integer, Integer> productSortMap;

    public static final Integer DEFAULT_PRODUCT_GRADE = 0;

    @PostConstruct
    public void init() {
        productSortMap = getAllProductSortMapFromDB();
        productSortMap = Collections.synchronizedMap(productSortMap);
    }

    /**
     * 获取指定商品的商品等级
     * @param productId
     * @return
     */
    public Integer getProductGradeByProductId(Integer productId) {
        Map<Integer, Integer> temp = productSortMap;
        if (null == temp) {
            return DEFAULT_PRODUCT_GRADE;
        }
        return temp.get(productId);
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
        logger.warn("新增一条商品排序记录>>>"+entity.toString());
        productSortDao.insert(entity);
        productSortMap.put(productId, productGrade);
    }

    /**
     * 设置一堆的商品排序记录（这个描述非常棒！）
     * @param list
     */
    public void setProductGradeList(List<ProductSortEntity> list) {
        Map<Integer, Integer> temp = productSortMap;
        logger.warn("新增多条商品排序记录>>>"+list.toString());
        productSortDao.insertList(list);
        temp.clear();
        for (ProductSortEntity entity : list) {
            temp.put(entity.getProductId(), entity.getProductGrade());
        }
    }

}
