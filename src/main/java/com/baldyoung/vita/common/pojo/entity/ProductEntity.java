package com.baldyoung.vita.common.pojo.entity;

import com.baldyoung.vita.common.pojo.exception.EntityCheckException;
import com.baldyoung.vita.common.pojo.exception.ServiceException;
import com.baldyoung.vita.common.utility.CommonMethod;
import com.baldyoung.vita.common.utility.StringCheckOut;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/*
productId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]商品编号：唯一、非空',
	productName VARCHAR(50) UNIQUE NOT NULL COMMENT'商品名称：唯一、非空',
	productTypeId INT UNSIGNED COMMENT'商品类别编号',
	productAttributeTypeId INT UNSIGNED COMMENT'商品属性编号',
	productPrice DECIMAL(10,1) NOT NULL COMMENT'商品单价：非空',
	productStockFlag TINYINT NOT NULL COMMENT'商品库存标识（0/无库存限制, 1/有库存限制）：非空',
	productStock INT UNSIGNED COMMENT'商品库存',
	productSalesVolume INT UNSIGNED NOT NULL DEFAULT 0 COMMENT'商品销售总量：非空',
	productIsShow TINYINT NOT NULL COMMENT'商品上架标识（0/未上架, 1/已上架）：非空',
	productImgName VARCHAR(100) NOT NULL COMMENT'商品图片：非空',
	productInfo VARCHAR(200) COMMENT'商品描述',
	productGrade SMALLINT UNSIGNED NOT NULL COMMENT'商品优先级：非空，其为用于商品排序的首要标准',
	isDelete TINYINT DEFAULT 0 NOT NULL COMMENT'删除标识（0/未删除, 1/已删除）：非空',
	createDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT'创建时间：非空',
	updateDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT'修改时间：非空',
 */
public class ProductEntity {
    private Integer productId;
    private Integer productTypeId;
    private Integer productAttributeTypeId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productStockFlag;
    private Integer productStock;
    private Integer productSalesVolume;
    private Integer productIsShow;
    private String productImgName;
    private String productInfo;
    private Integer productGrade;
    private Integer isDelete;
    private Date createDateTime;
    private Date updateDateTime;

    public boolean check() throws EntityCheckException {

        throw new EntityCheckException();
    }

    public static ProductEntity createInstanceFromMap(Map<String, Object> map, String... paramNotNull) throws ServiceException{
        //
        if (paramNotNull.length > 0) {
            for (String param : paramNotNull) {
                if (Objects.isNull(map.get(param))) {
                    throw new ServiceException();
                }
            }
        }
        ProductEntity productEntity = new ProductEntity();
        String temp = CommonMethod.ObjectToString(map.get("productId"));
        if (Objects.nonNull(temp)) {
            //productEntity.productId = StringCheckOut.outForIntegerWithRange(temp, 0, Integer.MAX_VALUE);
        }
        //temp =


        return null;
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

    public void setProductAttributeTypeId(Integer productAttributeId) {
        this.productAttributeTypeId = productAttributeId;
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

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "productId=" + productId +
                ", productTypeId=" + productTypeId +
                ", productAttributeTypeId=" + productAttributeTypeId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productStockFlag=" + productStockFlag +
                ", productStock=" + productStock +
                ", productSalesVolume=" + productSalesVolume +
                ", productIsShow=" + productIsShow +
                ", productImgName='" + productImgName + '\'' +
                ", productInfo='" + productInfo + '\'' +
                ", productGrade=" + productGrade +
                ", isDelete=" + isDelete +
                ", createDateTime=" + createDateTime +
                ", updateDateTime=" + updateDateTime +
                '}';
    }
}
