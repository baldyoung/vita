package com.baldyoung.vita.common.pojo.entity;

public class ProductAttributeValueEntity {

    private Integer productAttributeValueId;

    private Integer productAttributeTypeId;

    private String productAttributeValueName;

    @Override
    public String toString() {
        return "ProductAttributeValueEntity{" +
                "productAttributeValueId=" + productAttributeValueId +
                ", productAttributeTypeId=" + productAttributeTypeId +
                ", productAttributeValueName='" + productAttributeValueName + '\'' +
                '}';
    }

    public Integer getProductAttributeValueId() {
        return productAttributeValueId;
    }

    public void setProductAttributeValueId(Integer productAttributeValueId) {
        this.productAttributeValueId = productAttributeValueId;
    }

    public Integer getProductAttributeTypeId() {
        return productAttributeTypeId;
    }

    public void setProductAttributeTypeId(Integer productAttributeTypeId) {
        this.productAttributeTypeId = productAttributeTypeId;
    }

    public String getProductAttributeValueName() {
        return productAttributeValueName;
    }

    public void setProductAttributeValueName(String productAttributeValueName) {
        this.productAttributeValueName = productAttributeValueName;
    }
}
