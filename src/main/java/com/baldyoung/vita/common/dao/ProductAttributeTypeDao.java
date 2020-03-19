package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.ProductAttributeTypeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAttributeTypeDao {

    List<ProductAttributeTypeEntity> selectAll();
}
