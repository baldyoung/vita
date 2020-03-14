package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.ProductDao;
import com.baldyoung.vita.common.pojo.dto.product.NewProductDto;
import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import com.baldyoung.vita.merchant.controller.ProductController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl {

    private static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductDao productDao;

    public void addProduct(NewProductDto newProductDto) {



    }

    public List<ProductEntity> getProductPagingList() {
        ProductEntity entity = new ProductEntity();
        entity.setIsDelete(0);
        return productDao.selectProduct(entity);
    }

}
