package com.baldyoung.vita.common.pojo.entity;

public class ProductTypeSortEntity {
    private Integer productTypeSortId;
    private Integer productTypeId;
    private Integer productTypeGrade;

    @Override
    public String toString() {
        return "ProductTypeSortEntity{" +
                "productSortId=" + productTypeSortId +
                ", productTypeId=" + productTypeId +
                ", productGrade=" + productTypeGrade +
                '}';
    }

    public Integer getProductTypeSortId() {
        return productTypeSortId;
    }

    public void setProductTypeSortId(Integer productTypeSortId) {
        this.productTypeSortId = productTypeSortId;
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Integer getProductTypeGrade() {
        return productTypeGrade;
    }

    public void setProductTypeGrade(Integer productTypeGrade) {
        this.productTypeGrade = productTypeGrade;
    }
}
