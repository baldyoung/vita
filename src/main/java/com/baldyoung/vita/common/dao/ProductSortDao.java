package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.ProductSortEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSortDao {

    List<ProductSortEntity> selectAll();

    void insertList(@Param("sortList")List<ProductSortEntity> productSortEntityList);

    void deleteAll();

}
