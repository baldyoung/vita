package com.baldyoung.vita.customer.service;

import com.baldyoung.vita.common.dao.ProductDao;
import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import com.baldyoung.vita.merchant.service.ProductSortServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.baldyoung.vita.common.utility.CommonMethod.isEmptyCollection;

@Service
public class CProductServiceImpl {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductSortServiceImpl productSortService;

    public List<ProductEntity> getValidProductForProductType(Integer productTypeId) {
        ProductEntity entity = new ProductEntity();
        entity.setProductTypeId(productTypeId);
        entity.setProductIsShow(1);
        List<ProductEntity> list = productDao.selectProductWithCondition(entity);
        if (null != list) {
            for (ProductEntity entity1 : list) {
                entity.setProductGrade(productSortService.getProductGradeByProductId(entity.getProductId()));
            }
        }
        return list;
    }
}
