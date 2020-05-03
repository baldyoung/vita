package com.baldyoung.vita.common.pojo.dto.ProductAttribute;

import java.util.List;

public class ProductAttributeTypeDto {

    private String productAttributeTypeName;

    private Integer productAttributeTypeId;

    private List<ProductAttributeDto> productAttributeList;

    public String getProductAttributeTypeName() {
        return productAttributeTypeName;
    }

    public void setProductAttributeTypeName(String productAttributeTypeName) {
        this.productAttributeTypeName = productAttributeTypeName;
    }

    public Integer getProductAttributeTypeId() {
        return productAttributeTypeId;
    }

    public void setProductAttributeTypeId(Integer productAttributeTypeId) {
        this.productAttributeTypeId = productAttributeTypeId;
    }

    public List<ProductAttributeDto> getProductAttributeList() {
        return productAttributeList;
    }

    public void setProductAttributeList(List<ProductAttributeDto> productAttributeList) {
        this.productAttributeList = productAttributeList;
    }
}
