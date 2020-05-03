package com.baldyoung.vita.customer.service;

import com.baldyoung.vita.common.dao.ProductAttributeTypeDao;
import com.baldyoung.vita.common.dao.ProductAttributeValueDao;
import com.baldyoung.vita.common.pojo.dto.ProductAttribute.ProductAttributeDto;
import com.baldyoung.vita.common.pojo.dto.ProductAttribute.ProductAttributeTypeDto;
import com.baldyoung.vita.common.pojo.entity.ProductAttributeTypeEntity;
import com.baldyoung.vita.common.pojo.entity.ProductAttributeValueEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class CProductAttributeServiceImpl {
    @Autowired
    private ProductAttributeTypeDao productAttributeTypeDao;

    @Autowired
    private ProductAttributeValueDao productAttributeValueDao;

    public List<ProductAttributeTypeDto> getAllAttributeData() {
        List<ProductAttributeTypeEntity> typeList = productAttributeTypeDao.selectAll();
        List<ProductAttributeValueEntity> valueList = productAttributeValueDao.selectAll();
        List<ProductAttributeTypeDto> result = new ArrayList(typeList.size());
        for (ProductAttributeTypeEntity type : typeList) {
            Integer typeId = type.getProductAttributeTypeId();
            ProductAttributeTypeDto typeDto = new ProductAttributeTypeDto();
            typeDto.setProductAttributeTypeId(typeId);
            List<ProductAttributeDto> attributeList = new LinkedList();
            // typeDto.setProductAttributeTypeName(type.getProductAttributeTypeName());
            for (ProductAttributeValueEntity value : valueList) {
                if (typeId.equals(value.getProductAttributeTypeId())) {
                    ProductAttributeDto valueDto = new ProductAttributeDto();
                    valueDto.setProductAttributeId(value.getProductAttributeValueId());
                    valueDto.setProductAttributeName(value.getProductAttributeValueName());
                    attributeList.add(valueDto);
                }
            }
            typeDto.setProductAttributeList(attributeList);
            result.add(typeDto);
        }
        return result;
    }

}
