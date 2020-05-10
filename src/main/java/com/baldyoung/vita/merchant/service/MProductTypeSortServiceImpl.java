package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.ProductTypeSortDao;
import com.baldyoung.vita.common.pojo.entity.ProductTypeSortEntity;
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
 * 品类排序服务
 */
@Service
public class MProductTypeSortServiceImpl {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductTypeSortDao productTypeSortDao;

    private Map<Integer, Integer> productTypeSortMap;

    public static final Integer DEFAULT_PRODUCT_TYPE_GRADE = 0;

    @PostConstruct
    public void init() {
        productTypeSortMap = getAllProductTypeSortMapFromDB();
        productTypeSortMap = Collections.synchronizedMap(productTypeSortMap);
    }

    /**
     * 获取指定品类的商品等级
     * @param productTypeId
     * @return
     */
    public Integer getProductTypeGradeByProductTypeId(Integer productTypeId) {
        Map<Integer, Integer> temp = productTypeSortMap;
        if (null == temp) {
            return DEFAULT_PRODUCT_TYPE_GRADE;
        }
        return temp.get(productTypeId);
    }

    /**
     * 获取当前缓存的品类等级图
     * @return
     */
    public Map<Integer, Integer> getAllProductTypeSortMap() {
        return productTypeSortMap;
    }

    /**
     * 从数据库获取品类等级图
     * @return
     */
    public Map<Integer, Integer> getAllProductTypeSortMapFromDB() {
        List<ProductTypeSortEntity> list = productTypeSortDao.selectAll();
        Map<Integer, Integer> map = new HashMap();
        for (ProductTypeSortEntity entity : list) {
            map.put(entity.getProductTypeId(), entity.getProductTypeGrade());
        }
        return map;
    }

    /**
     * 新增一个品类的排序记录
     * @param productId
     */
    public void setProductTypeGrade(Integer productId) {
        setProductTypeGrade(productId, DEFAULT_PRODUCT_TYPE_GRADE);
    }

    /**
     * 新增一个品类的排序记录
     * @param productTypeId
     * @param productTypeGrade
     */
    public void setProductTypeGrade(Integer productTypeId, Integer productTypeGrade) {
        ProductTypeSortEntity entity = new ProductTypeSortEntity();
        entity.setProductTypeId(productTypeId);
        entity.setProductTypeGrade(productTypeGrade);
        logger.warn("新增一条品类排序记录>>>"+entity.toString());
        productTypeSortDao.insert(entity);
        productTypeSortMap.put(productTypeId, productTypeGrade);
    }

    /**
     * 设置一堆的品类排序记录（这个描述非常棒！）
     * @param list
     */
    public void setProductTypeGradeList(List<ProductTypeSortEntity> list) {
        Map<Integer, Integer> temp = productTypeSortMap;
        logger.warn("新增多条品类排序记录>>>"+list.toString());
        productTypeSortDao.insertList(list);
        temp.clear();
        for (ProductTypeSortEntity entity : list) {
            temp.put(entity.getProductTypeId(), entity.getProductTypeGrade());
        }
    }

}
