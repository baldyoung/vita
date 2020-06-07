package com.baldyoung.vita.common.pojo.entity;

import java.math.BigDecimal;

public class BillCountInfoEntity {

    /**
     * 账单总数
     */
    private Integer billTotalNumber;
    /**
     * 零金额账单数
     */
    private Integer zeroBillNumber;
    /**
     * 未结账单数
     */
    private Integer unPayBillNumber;
    /**
     * 总的营业额
     */
    private BigDecimal totalSales;
    /**
     * 未结款项总额
     */
    private BigDecimal totalUnReceive;

    public Integer getBillTotalNumber() {
        return billTotalNumber;
    }

    public void setBillTotalNumber(Integer billTotalNumber) {
        this.billTotalNumber = billTotalNumber;
    }

    public Integer getZeroBillNumber() {
        return zeroBillNumber;
    }

    public void setZeroBillNumber(Integer zeroBillNumber) {
        this.zeroBillNumber = zeroBillNumber;
    }

    public Integer getUnPayBillNumber() {
        return unPayBillNumber;
    }

    public void setUnPayBillNumber(Integer unPayBillNumber) {
        this.unPayBillNumber = unPayBillNumber;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public BigDecimal getTotalUnReceive() {
        return totalUnReceive;
    }

    public void setTotalUnReceive(BigDecimal totalUnReceive) {
        this.totalUnReceive = totalUnReceive;
    }
}
