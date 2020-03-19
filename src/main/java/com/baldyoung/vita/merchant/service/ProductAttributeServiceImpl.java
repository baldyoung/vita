package com.baldyoung.vita.merchant.service;


import com.baldyoung.vita.common.dao.ProductAttributeTypeDao;
import com.baldyoung.vita.common.dao.ProductAttributeValueDao;
import com.baldyoung.vita.common.pojo.entity.ProductAttributeTypeEntity;
import com.baldyoung.vita.common.pojo.entity.ProductAttributeValueEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductAttributeServiceImpl {
    @Autowired
    private ProductAttributeTypeDao productAttributeTypeDao;

    @Autowired
    private ProductAttributeValueDao productAttributeValueDao;

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

}
