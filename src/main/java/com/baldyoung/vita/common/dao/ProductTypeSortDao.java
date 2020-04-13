package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.ProductTypeSortEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeSortDao {

    List<ProductTypeSortEntity> selectAll();

    void insert(@Param("productTypeSort")ProductTypeSortEntity productSortEntity);

    void insertList(@Param("sortList")List<ProductTypeSortEntity> productSortEntityList);

    void deleteAll();
}
