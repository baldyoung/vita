package com.baldyoung.vita.common.pojo.dto.product;

import com.baldyoung.vita.common.pojo.entity.ProductEntity;

import java.math.BigDecimal;

public class CProductDto {
    private Integer productId;
    private Integer productTypeId;
    private Integer productAttributeTypeId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productStockFlag;
    private Integer productStock;
    private Integer productSalesVolume;
    private String productImgName;
    private String productInfo;
    private Integer productGrade;
    private Integer currentQuantity;

    @Override
    public String toString() {
        return "CProductDto{" +
                "productId=" + productId +
                ", productTypeId=" + productTypeId +
                ", productAttributeTypeId=" + productAttributeTypeId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productStockFlag=" + productStockFlag +
                ", productStock=" + productStock +
                ", productSalesVolume=" + productSalesVolume +
                ", productImgName='" + productImgName + '\'' +
                ", productInfo='" + productInfo + '\'' +
                ", productGrade=" + productGrade +
                ", currentQuantity=" + currentQuantity +
                '}';
    }



    public CProductDto() {
    }

    public CProductDto(ProductEntity entity) {
        CProductDto dto = this;
        dto.setProductId(entity.getProductId());
        dto.setProductAttributeTypeId(entity.getProductAttributeTypeId());
        dto.setProductImgName(entity.getProductImgName());
        dto.setProductInfo(entity.getProductInfo());
        dto.setProductPrice(entity.getProductPrice());
        dto.setProductName(entity.getProductName());
        dto.setProductStock(entity.getProductStock());
        dto.setProductStockFlag(entity.getProductStockFlag());
        dto.setProductTypeId(entity.getProductTypeId());
        dto.setProductSalesVolume(entity.getProductSalesVolume());
        dto.setProductGrade(entity.getProductGrade());
    }

    public CProductDto(Integer productId, Integer productTypeId, Integer productAttributeTypeId, String productName, BigDecimal productPrice, Integer productStockFlag, Integer productStock, Integer productSalesVolume, String productImgName, String productInfo, Integer productGrade, Integer currentQuantity) {
        this.productId = productId;
        this.productTypeId = productTypeId;
        this.productAttributeTypeId = productAttributeTypeId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStockFlag = productStockFlag;
        this.productStock = productStock;
        this.productSalesVolume = productSalesVolume;
        this.productImgName = productImgName;
        this.productInfo = productInfo;
        this.productGrade = productGrade;
        this.currentQuantity = currentQuantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Integer getProductAttributeTypeId() {
        return productAttributeTypeId;
    }

    public void setProductAttributeTypeId(Integer productAttributeTypeId) {
        this.productAttributeTypeId = productAttributeTypeId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductStockFlag() {
        return productStockFlag;
    }

    public void setProductStockFlag(Integer productStockFlag) {
        this.productStockFlag = productStockFlag;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }

    public Integer getProductSalesVolume() {
        return productSalesVolume;
    }

    public void setProductSalesVolume(Integer productSalesVolume) {
        this.productSalesVolume = productSalesVolume;
    }

    public String getProductImgName() {
        return productImgName;
    }

    public void setProductImgName(String productImgName) {
        this.productImgName = productImgName;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public Integer getProductGrade() {
        return productGrade;
    }

    public void setProductGrade(Integer productGrade) {
        this.productGrade = productGrade;
    }

    public Integer getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Integer currentQuantity) {
        this.currentQuantity = currentQuantity;
    }
}
