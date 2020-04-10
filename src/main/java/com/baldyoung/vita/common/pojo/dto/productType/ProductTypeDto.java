package com.baldyoung.vita.common.pojo.dto.productType;

import com.baldyoung.vita.common.pojo.entity.ProductTypeEntity;

public class ProductTypeDto {
    private Integer productTypeId;
    private String productTypeName;
    private Integer productTypeGrade;
    private Integer isShow;

    @Override
    public String toString() {
        return "ProductTypeEntity{" +
                "productTypeId=" + productTypeId +
                ", productTypeName='" + productTypeName + '\'' +
                ", productTypeGrade=" + productTypeGrade +
                ", isShow=" + isShow +
                '}';
    }
    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
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

    public static ProductTypeEntity to(ProductTypeDto productTypeDto) {
        if (null == productTypeDto) {
            return null;
        }
        ProductTypeEntity productTypeEntity = new ProductTypeEntity();
        productTypeEntity.setProductTypeId(productTypeDto.getProductTypeId());
        productTypeEntity.setProductTypeName(productTypeDto.getProductTypeName());
        productTypeEntity.setProductTypeGrade(productTypeDto.getProductTypeGrade());
        productTypeEntity.setIsShow(productTypeDto.getIsShow());
        return productTypeEntity;
    }
}
