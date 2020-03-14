package com.baldyoung.vita.common.dao;


import com.baldyoung.vita.common.pojo.entity.ProductTypeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeDao {

    List<ProductTypeEntity> selectAll();
}
