package com.baldyoung.vita.common.pojo.entity;

public class ProductSortEntity {
    private Integer productSortId;
    private Integer productId;
    private Integer productGrade;

    @Override
    public String toString() {
        return "ProductSortEntity{" +
                "productSortId=" + productSortId +
                ", productId=" + productId +
                ", productGrade=" + productGrade +
                '}';
    }

    public Integer getProductSortId() {
        return productSortId;
    }

    public void setProductSortId(Integer productSortId) {
        this.productSortId = productSortId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductGrade() {
        return productGrade;
    }

    public void setProductGrade(Integer productGrade) {
        this.productGrade = productGrade;
    }
}
