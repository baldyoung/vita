package com.baldyoung.vita.common.pojo.entity;

public class ProductAttributeTypeEntity {

    private Integer productAttributeTypeId;

    private String productAttributeTypeName;

    @Override
    public String toString() {
        return "ProductAttributeValueEntity{" +
                "productAttributeTypeId=" + productAttributeTypeId +
                ", productAttributeTypeName='" + productAttributeTypeName + '\'' +
                '}';
    }

    public Integer getProductAttributeTypeId() {
        return productAttributeTypeId;
    }

    public void setProductAttributeTypeId(Integer productAttributeTypeId) {
        this.productAttributeTypeId = productAttributeTypeId;
    }

    public String getProductAttributeTypeName() {
        return productAttributeTypeName;
    }

    public void setProductAttributeTypeName(String productAttributeTypeName) {
        this.productAttributeTypeName = productAttributeTypeName;
    }
}
