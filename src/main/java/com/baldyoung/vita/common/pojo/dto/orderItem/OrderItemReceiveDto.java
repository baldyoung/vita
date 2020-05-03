package com.baldyoung.vita.common.pojo.dto.orderItem;

public class OrderItemReceiveDto {

    private Integer productId;

    private Integer productQuantity;

    private Integer productAttributeId;

    @Override
    public String toString() {
        return "OrderItemReceiveDto{" +
                "productId=" + productId +
                ", productQuantity=" + productQuantity +
                ", productAttributeId=" + productAttributeId +
                '}';
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Integer getProductAttributeId() {
        return productAttributeId;
    }

    public void setProductAttributeId(Integer productAttributeId) {
        this.productAttributeId = productAttributeId;
    }
}
