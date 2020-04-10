package com.baldyoung.vita.merchant.service;


import com.baldyoung.vita.common.dao.ProductTypeDao;
import com.baldyoung.vita.common.pojo.dto.productType.ProductTypeDto;
import com.baldyoung.vita.common.pojo.entity.ProductTypeEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.PRODUCT_TYPE_NAME_EXISTS;

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
            productTypeDto.setIsShow(entity.getIsShow());
            result.add(productTypeDto);
        }
        return result;
    }

    /**
     * 新增或修改商品类型
     * @param productTypeDto
     * @throws ServiceException
     */
    public void updateOrAddProductType(ProductTypeDto productTypeDto) throws ServiceException {
        ProductTypeEntity productTypeEntity = ProductTypeDto.to(productTypeDto);
        ProductTypeEntity existsProductType = productTypeDao.selectByProductTypeName(productTypeEntity.getProductTypeName());
        if (existsProductType != null && null != existsProductType.getProductTypeId()) {
            if (!existsProductType.getProductTypeId().equals(productTypeEntity.getProductTypeId())) {
                // 已存在该商品类型名称，且不是当前对象
                throw new ServiceException(PRODUCT_TYPE_NAME_EXISTS);
            }
        }
        if (null == productTypeEntity.getProductTypeId()) {
            productTypeDao.insertProductType(productTypeEntity);
        } else {
            productTypeDao.updateProductType(productTypeEntity);
        }
    }

    public void deleteProductType(Integer productTypeId) {
        productTypeDao.deleteProductType(productTypeId);
    }

    public ProductTypeEntity findByProductTypeId(Integer productTypeId) {
        return productTypeDao.selectByProductTypeId(productTypeId);
    }
}
