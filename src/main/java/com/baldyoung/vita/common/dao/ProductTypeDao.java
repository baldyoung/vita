package com.baldyoung.vita.common.dao;


import com.baldyoung.vita.common.pojo.entity.ProductTypeEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeDao {
    /**
     * 获取所有商品类型
     * @return
     */
    List<ProductTypeEntity> selectAll();

    /**
     * 新增商品类型
     */
    void insertProductType(@Param("productType")ProductTypeEntity productTypeEntity);

    /**
     * 修改商品类型
     */
    void updateProductType(@Param("productType")ProductTypeEntity productTypeEntity);

    /**
     * 删除指定商品类型
     */
    void deleteProductType(@Param("productTypeId")Integer productTypeId);

    /**
     * 获取依据条件搜索商品类型
     */
    List<ProductTypeEntity> selectWithCondition(@Param("productType")ProductTypeEntity productTypeEntity);

    ProductTypeEntity selectByProductTypeName(@Param("productTypeName")String productTypeName);

    ProductTypeEntity selectByProductTypeId(@Param("productTypeId")Integer productTypeId);


}
