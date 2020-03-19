package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.ProductAttributeValueEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAttributeValueDao {

    List<ProductAttributeValueEntity> selectAll();

    List<ProductAttributeValueEntity> selectByProductAttributeTypeId(@Param("attributeTypeId")Integer attributeTypeId);
}
