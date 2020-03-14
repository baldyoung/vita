package com.baldyoung.vita.common.pojo.entity;


/**
 * productTypeId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]商品类型编号：唯一、非空',
 * 	productTypeName VARCHAR(30) UNIQUE NOT NULL COMMENT'商品类型名称：唯一、非空',
 * 	productTypeGrade SMALLINT UNSIGNED NOT NULL COMMENT'商品类型的优先级：非空，其为用于类型排序的首要标准',
 * 	PRIMARY KEY (productTypeId)
 */
public class ProductTypeEntity {
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
