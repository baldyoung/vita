package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.ProductDao;
import com.baldyoung.vita.common.pojo.dto.product.NewProductDto;
import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import com.baldyoung.vita.merchant.controller.ProductController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl {

    private static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductDao productDao;

    public void addProduct(NewProductDto newProductDto) {



    }

    public Map<String, Object> getProductTargetCountInfo() {
        return productDao.countProductTargetInfo();
    }

    public List<ProductEntity> getProductPagingList(Integer productTypeId, Integer isShow, Integer startIndex, Integer maxSize) {
        ProductEntity entity = new ProductEntity();
        if (productTypeId != null && productTypeId.intValue() != -1) {
            entity.setProductTypeId(productTypeId);
        }
        entity.setProductIsShow(isShow);
        return productDao.selectProduct(entity, startIndex, maxSize);
    }

}
