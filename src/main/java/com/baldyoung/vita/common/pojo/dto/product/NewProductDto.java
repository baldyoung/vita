package com.baldyoung.vita.common.pojo.dto.product;



import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * productId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]商品编号：唯一、非空',
 * 	productName VARCHAR(50) UNIQUE NOT NULL COMMENT'商品名称：唯一、非空',
 * 	productTypeId INT UNSIGNED COMMENT'商品类别编号',
 * 	productAttributeId INT UNSIGNED COMMENT'商品属性编号',
 * 	productPrice DECIMAL(10,1) NOT NULL COMMENT'商品单价：非空',
 * 	productStockFlag TINYINT NOT NULL COMMENT'商品库存标识（0/无库存限制, 1/有库存限制）：非空',
 * 	productStock INT UNSIGNED COMMENT'商品库存',
 * 	productSalesVolume INT UNSIGNED NOT NULL DEFAULT 0 COMMENT'商品销售总量：非空',
 * 	productIsShow TINYINT NOT NULL COMMENT'商品上架标识（0/未上架, 1/已上架）：非空',
 * 	productImgName VARCHAR(100) NOT NULL COMMENT'商品图片：非空',
 * 	productInfo VARCHAR(200) COMMENT'商品描述',
 * 	productGrade SMALLINT UNSIGNED NOT NULL COMMENT'商品优先级：非空，其为用于商品排序的首要标准',
 * 	isDelete TINYINT DEFAULT 0 NOT NULL COMMENT'删除标识（0/未删除, 1/已删除）：非空',
 * 	createDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT'创建时间：非空',
 * 	updateDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT'修改时间：非空',
 */

public class NewProductDto {

    private Integer productId;

    @Length(min=1, max=10, message = "商品名称不能为空，且不能超过10个字")
    private String productName;

    @NotNull(message = "必须指定商品类型")
    private Integer productTypeId;

    private Integer attributeTypeId;

    @NotNull(message = "必须指定商品单价")
    private BigDecimal productPrice;

    @NotNull(message = "必须指定商品库存标识")
    private Integer productStockFlag;

    private Integer productStock;
    // private Integer productSalesVolume;
    @NotNull(message = "必须指定上架标识")
    private Integer productIsShow;

    // @Length(max=100, message = "图片名称不能超过100个字")
    private String productImgName;

    @Length(max=200, message = "商品介绍不能超过200个字")
    private String productInfo;

    @Max(value = 65535, message = "排序超过最大值")
    private Integer productGrade;

    private MultipartFile file;


    @Override
    public String toString() {
        return "NewProductDto{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productTypeId=" + productTypeId +
                ", attributeTypeId=" + attributeTypeId +
                ", productPrice=" + productPrice +
                ", productStockFlag=" + productStockFlag +
                ", productStock=" + productStock +
                ", productIsShow=" + productIsShow +
                ", productImgName='" + productImgName + '\'' +
                ", productInfo='" + productInfo + '\'' +
                ", productGrade=" + productGrade +
                ", file=" + file +
                '}';
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Integer getAttributeTypeId() {
        return attributeTypeId;
    }

    public void setAttributeTypeId(Integer attributeTypeId) {
        this.attributeTypeId = attributeTypeId;
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

    public Integer getProductIsShow() {
        return productIsShow;
    }

    public void setProductIsShow(Integer productIsShow) {
        this.productIsShow = productIsShow;
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
}
