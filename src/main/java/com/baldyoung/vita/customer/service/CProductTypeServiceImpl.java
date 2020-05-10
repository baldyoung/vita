package com.baldyoung.vita.customer.service;

import com.baldyoung.vita.common.dao.ProductTypeDao;
import com.baldyoung.vita.common.pojo.entity.ProductTypeEntity;
import com.baldyoung.vita.merchant.service.MProductTypeSortServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CProductTypeServiceImpl {
    @Autowired
    private ProductTypeDao productTypeDao;

    @Autowired
    private MProductTypeSortServiceImpl productTypeSortService;

    /**
     * 获取有效品类集
     * @return
     */
    public List<ProductTypeEntity> getAllValidProductType() {
        ProductTypeEntity entity = new ProductTypeEntity();
        entity.setIsShow(1);
        List<ProductTypeEntity> list = productTypeDao.selectWithCondition(entity);
        if (null != list) {
            for (ProductTypeEntity item : list) {
                item.setProductTypeGrade(productTypeSortService.getProductTypeGradeByProductTypeId(item.getProductTypeId()));
            }
        }
        return list;
    }

}
