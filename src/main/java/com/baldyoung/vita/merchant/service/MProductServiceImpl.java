package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.ProductDao;
import com.baldyoung.vita.common.pojo.dto.product.NewProductDto;
import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.*;

@Service
public class MProductServiceImpl {

    private static Logger logger = LoggerFactory.getLogger(MProductServiceImpl.class);
    @Autowired
    private ProductDao productDao;

    @Autowired
    private MProductSortServiceImpl productSortService;

    public void addProduct(NewProductDto newProductDto) throws ServiceException {
        ProductEntity productEntity = toNewProductEntity(newProductDto);
        ProductEntity existsObject = productDao.findProductByProductName(productEntity.getProductName());
        if (null != existsObject && null != existsObject.getProductId()) {
            // 如果该商品名称已存在，则抛出业务异常
            throw new ServiceException(PRODUCT_NAME_EXISTS);
        }
        productEntity.setProductIsShow(0);
        productDao.insertProduct(productEntity);

    }

    /**
     * 修改商品信息
     * @param newProductDto
     * @throws ServiceException
     */
    public void updateProduct(NewProductDto newProductDto) throws ServiceException {
        ProductEntity productEntity = toNewProductEntity(newProductDto);
        ProductEntity existsObject = null;
        if (null != productEntity.getProductName()) {
            existsObject = productDao.findProductByProductName(productEntity.getProductName());
        }
        if (null != existsObject && null != existsObject.getProductId()) {
            // 如果该商品名称已存在，则抛出业务异常
            if (!existsObject.getProductId().equals(productEntity.getProductId())) {
                throw new ServiceException(PRODUCT_NAME_EXISTS);
            }
        }
        productEntity.setProductIsShow(null);
        productDao.updateProduct(productEntity);
    }

    /**
     * 获取特定的商品统计数据
     * @param productTypeId
     * @param isShow
     * @return
     */
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

    /**
     * 获取分页的商品集合
     * @param productTypeId
     * @param isShow
     * @param startIndex
     * @param maxSize
     * @return
     */
    public List<ProductEntity> getProductPagingList(Integer productTypeId, Integer isShow, Integer startIndex, Integer maxSize) {
        ProductEntity entity = new ProductEntity();
        if (productTypeId != null && productTypeId.intValue() != -1) {
            entity.setProductTypeId(productTypeId);
        }
        entity.setProductIsShow(isShow);
        return productDao.selectProduct(entity, startIndex, maxSize);
    }

    /**
     * 通过商品编号获取商品信息
     * @param productId
     * @return
     */
    public ProductEntity getProductByProductId(Integer productId) {
        ProductEntity productEntity = productDao.findProductByProductId(productId);
        return productEntity;
    }

    /**
     * 删除指定商品
     * @param productId
     */
    public void deleteProduct(Integer productId) {
        productDao.deleteProduct(productId);
    }

    /**
     * 获取所有商品，附带每个商品的排序值
     * @return
     */
    public List<ProductEntity> getAllProduct() {
        List<ProductEntity> list = productDao.selectAllProduct();
        for (ProductEntity entity : list) {
            entity.setProductGrade(productSortService.getProductGradeByProductId(entity.getProductId()));
        }
        return list;
    }

    public void updateSimpleProductList(Integer typeId, Integer isShow, List<Integer> productIdList) {
        productDao.updateSimpleProductList(typeId, isShow, productIdList);
    }

    /**
     * 获取所有上架商品
     * @return
     */
    public List<ProductEntity> getAllValidProduct() {
        ProductEntity entity = new ProductEntity();
        entity.setProductIsShow(1);
        return productDao.selectProductWithCondition(entity);
    }

    /**
     * 将NewProductDto转换为ProductEntity
     * @param newProductDto
     * @return
     */
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
