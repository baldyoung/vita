package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao {

   // void insertProduct(@Param("product")ProductEntity productEntity);

    List<ProductEntity> selectProduct(@Param("product")ProductEntity productEntity);
}
