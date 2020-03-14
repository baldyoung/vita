package com.baldyoung.vita.common.pojo.dto.productType;

public class ProductTypeDto {
    private Integer productTypeId;
    private String productTypeName;
    private Integer productTypeGrade;

    @Override
    public String toString() {
        return "ProductTypeEntity{" +
                "productTypeId=" + productTypeId +
                ", productTypeName='" + productTypeName + '\'' +
                ", productTypeGrade=" + productTypeGrade +
                '}';
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public Integer getProductTypeGrade() {
        return productTypeGrade;
    }

    public void setProductTypeGrade(Integer productTypeGrade) {
        this.productTypeGrade = productTypeGrade;
    }
}
