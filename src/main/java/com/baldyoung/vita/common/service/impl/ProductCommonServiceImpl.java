package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.dao.ProductAttributeTypeDao;
import com.baldyoung.vita.common.dao.ProductAttributeValueDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCommonServiceImpl {

    @Autowired
    private ProductAttributeTypeDao productAttributeTypeDao;

    @Autowired
    private ProductAttributeValueDao productAttributeValueDao;


}
