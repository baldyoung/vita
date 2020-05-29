package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.ProductAttributeValueEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAttributeValueDao {

    List<ProductAttributeValueEntity> selectAll();

    List<ProductAttributeValueEntity> selectByProductAttributeTypeId(@Param("attributeTypeId")Integer attributeTypeId);

    void insertEntity(@Param("entity")ProductAttributeValueEntity entity);

    void deleteEntity(@Param("entityId")Integer entityId);

    void deleteEntityByTypeId(@Param("typeId")Integer typeId);
}
