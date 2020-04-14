package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductDao {

   // void insertProduct(@Param("product")ProductEntity productEntity);
    Integer countProductTargetAmount(@Param("product")ProductEntity product);

    List<ProductEntity> selectProduct(@Param("product")ProductEntity product, @Param("startIndex")Integer startIndex, @Param("maxSize")Integer maxSize);

    ProductEntity findProductByProductId(@Param("productId")Integer productId);

    ProductEntity findProductByProductName(@Param("productName")String productName);

    void insertProduct(@Param("product")ProductEntity productEntity);

    void updateProduct(@Param("product")ProductEntity productEntity);

    void deleteProduct(@Param("productId")Integer productId);

    List<ProductEntity> selectAllProduct();

    /**
     * 统一修改商品的类型和上下架标识
     * @param typeId
     * @param productIsShow
     * @param productIdList
     */
    void updateSimpleProductList(@Param("productTypeId")Integer typeId, @Param("productIsShow")Integer productIsShow, @Param("productIdList")List<Integer> productIdList);

    List<ProductEntity> selectProductWithCondition(@Param("condition")ProductEntity entity);

}
