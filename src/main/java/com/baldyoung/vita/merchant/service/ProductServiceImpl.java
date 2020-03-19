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
        ProductEntity productEntity = toNewProductEntity(newProductDto);
        productEntity.setProductIsShow(0);
        productDao.insertProduct(productEntity);

    }

    public void updateProduct(NewProductDto newProductDto) {
        ProductEntity productEntity = toNewProductEntity(newProductDto);
        productEntity.setProductIsShow(null);
        productDao.updateProduct(productEntity);
    }

    public Map<String, Object> getProductTargetCountInfo(Integer productTypeId, Integer isShow) {
        ProductEntity entity = new ProductEntity();
        if (productTypeId != null && productTypeId.intValue() != -1) {
            entity.setProductTypeId(productTypeId);
        }
        entity.setProductIsShow(isShow);
        Map<String, Object> result = new HashMap();
        result.put("total", productDao.countProductTargetAmount(entity));
        entity.setProductIsShow(1);
        result.put("productIsShow", productDao.countProductTargetAmount(entity));
        entity.setProductIsShow(isShow);
        entity.setProductStock(0);
        result.put("productStock", productDao.countProductTargetAmount(entity));
        entity.setProductStock(null);
        entity.setProductImgName("default.gif");
        result.put("productImgName", productDao.countProductTargetAmount(entity));
        return result;
    }

    public List<ProductEntity> getProductPagingList(Integer productTypeId, Integer isShow, Integer startIndex, Integer maxSize) {
        ProductEntity entity = new ProductEntity();
        if (productTypeId != null && productTypeId.intValue() != -1) {
            entity.setProductTypeId(productTypeId);
        }
        entity.setProductIsShow(isShow);
        return productDao.selectProduct(entity, startIndex, maxSize);
    }

    public ProductEntity getProductByProductId(Integer productId) {
        ProductEntity productEntity = productDao.findProductByProductId(productId);
        return productEntity;
    }



    private ProductEntity toNewProductEntity(NewProductDto newProductDto) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(newProductDto.getProductId());
        productEntity.setProductName(newProductDto.getProductName());
        productEntity.setProductTypeId(newProductDto.getProductTypeId());
        productEntity.setProductAttributeTypeId(newProductDto.getProductAttributeTypeId());
        productEntity.setProductInfo(newProductDto.getProductInfo());
        productEntity.setProductPrice(newProductDto.getProductPrice());
        productEntity.setProductStockFlag(newProductDto.getProductStockFlag());
        productEntity.setProductStock(newProductDto.getProductStock());
        productEntity.setProductImgName(newProductDto.getProductImgName());
        // productEntity.setProductIsShow(newProductDto.getProductIsShow());
        return productEntity;
    }

}
