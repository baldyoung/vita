package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductDao {

   // void insertProduct(@Param("product")ProductEntity productEntity);
    Map<String, Object> countProductTargetInfo();

    List<ProductEntity> selectProduct(@Param("product")ProductEntity productEntity, @Param("startIndex")Integer startIndex, @Param("maxSize")Integer maxSize);
}
