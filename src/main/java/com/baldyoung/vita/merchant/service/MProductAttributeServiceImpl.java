package com.baldyoung.vita.merchant.service;


import com.baldyoung.vita.common.dao.ProductAttributeTypeDao;
import com.baldyoung.vita.common.dao.ProductAttributeValueDao;
import com.baldyoung.vita.common.pojo.entity.ProductAttributeTypeEntity;
import com.baldyoung.vita.common.pojo.entity.ProductAttributeValueEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MProductAttributeServiceImpl {
    @Autowired
    private ProductAttributeTypeDao productAttributeTypeDao;

    @Autowired
    private ProductAttributeValueDao productAttributeValueDao;

    /**
     * 获取所有商品属性类型
     * @return
     */
    public List<ProductAttributeTypeEntity> getAllAttributeType() {
        List<ProductAttributeTypeEntity> productAttributeTypeEntityList = productAttributeTypeDao.selectAll();
        return productAttributeTypeEntityList;
    }

    public List<ProductAttributeValueEntity> getAllAttributeValue() {
        List<ProductAttributeValueEntity> productAttributeValueEntityList = productAttributeValueDao.selectAll();
        return productAttributeValueEntityList;
    }

    public List<ProductAttributeValueEntity> getAttributeValueListByAttributeTypeId(Integer attributeTypeId) {
        return productAttributeValueDao.selectByProductAttributeTypeId(attributeTypeId);
    }

    /**
     * 新增属性类别
     * @param attributeTypeName
     */
    public void addAttributeType(String attributeTypeName) {
        ProductAttributeTypeEntity entity = new ProductAttributeTypeEntity();
        entity.setProductAttributeTypeName(attributeTypeName);
        productAttributeTypeDao.insertEntity(entity);
    }

    /**
     * 新增属性值
     * @param attributeValueName
     * @param attributeTypeId
     */
    public void addAttributeValue(String attributeValueName, Integer attributeTypeId) {
        ProductAttributeValueEntity entity = new ProductAttributeValueEntity();
        entity.setProductAttributeTypeId(attributeTypeId);
        entity.setProductAttributeValueName(attributeValueName);
        productAttributeValueDao.insertEntity(entity);
    }

    /**
     * 删除属性类别
     * @param typeId
     */
    public void deleteAttributeType(Integer typeId) {
        productAttributeTypeDao.deleteEntity(typeId);
        productAttributeValueDao.deleteEntityByTypeId(typeId);
    }

    /**
     * 删除属性值
     * @param valueId
     */
    public void deleteAttributeValue(Integer valueId) {
        productAttributeValueDao.deleteEntity(valueId);
    }

}
