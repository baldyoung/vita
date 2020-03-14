package com.baldyoung.vita.merchant.service;


import com.baldyoung.vita.common.dao.ProductTypeDao;
import com.baldyoung.vita.common.pojo.dto.productType.ProductTypeDto;
import com.baldyoung.vita.common.pojo.entity.ProductTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductTypeServiceImpl {

    @Autowired
    private ProductTypeDao productTypeDao;

    public List<ProductTypeDto> getAllProductType() {
        List<ProductTypeEntity> productTypeEntityList = productTypeDao.selectAll();
        List<ProductTypeDto> result = new ArrayList(productTypeEntityList.size());
        ProductTypeDto productTypeDto;
        for(ProductTypeEntity entity : productTypeEntityList) {
            productTypeDto = new ProductTypeDto();
            productTypeDto.setProductTypeId(entity.getProductTypeId());
            productTypeDto.setProductTypeName(entity.getProductTypeName());
            productTypeDto.setProductTypeGrade(entity.getProductTypeGrade());
            result.add(productTypeDto);
        }
        return result;
    }
}
