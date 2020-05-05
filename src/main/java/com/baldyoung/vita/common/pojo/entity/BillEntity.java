package com.baldyoung.vita.common.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

public class BillEntity {

    private Integer billId;
    private String billNumber;
    private Integer billOwnerTypeFlag;
    private Integer billOwnerId;
    private String billOwnerName;

    private Integer billCustomerNumber;
    private Integer billOrderQuantity;
    private BigDecimal billTotalAmount;

    private BigDecimal billReceivedAmount;
    private Date billReceivedDateTime;

    private String billRecentHandlerName;
    private Integer billRecentHandlerId;

    private Date billStartDateTime;
    private Date billEndDateTime;
    private Date updateDateTime;

    private String billRemarks;


    public Integer getBillId() {
        return billId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getBillOwnerTypeFlag() {
        return billOwnerTypeFlag;
    }

    public void setBillOwnerTypeFlag(Integer billOwnerTypeFlag) {
        this.billOwnerTypeFlag = billOwnerTypeFlag;
    }

    public Integer getBillOwnerId() {
        return billOwnerId;
    }

    public void setBillOwnerId(Integer billOwnerId) {
        this.billOwnerId = billOwnerId;
    }

    public String getBillOwnerName() {
        return billOwnerName;
    }

    public void setBillOwnerName(String billOwnerName) {
        this.billOwnerName = billOwnerName;
    }

    public Integer getBillCustomerNumber() {
        return billCustomerNumber;
    }

    public void setBillCustomerNumber(Integer billCustomerNumber) {
        this.billCustomerNumber = billCustomerNumber;
    }

    public Integer getBillOrderQuantity() {
        return billOrderQuantity;
    }

    public void setBillOrderQuantity(Integer billOrderQuantity) {
        this.billOrderQuantity = billOrderQuantity;
    }

    public BigDecimal getBillTotalAmount() {
        return billTotalAmount;
    }

    public void setBillTotalAmount(BigDecimal billTotalAmount) {
        this.billTotalAmount = billTotalAmount;
    }

    public BigDecimal getBillReceivedAmount() {
        return billReceivedAmount;
    }

    public void setBillReceivedAmount(BigDecimal billReceivedAmount) {
        this.billReceivedAmount = billReceivedAmount;
    }

    public Date getBillReceivedDateTime() {
        return billReceivedDateTime;
    }

    public void setBillReceivedDateTime(Date billReceivedDateTime) {
        this.billReceivedDateTime = billReceivedDateTime;
    }

    public String getBillRecentHandlerName() {
        return billRecentHandlerName;
    }

    public void setBillRecentHandlerName(String billRecentHandlerName) {
        this.billRecentHandlerName = billRecentHandlerName;
    }

    public Integer getBillRecentHandlerId() {
        return billRecentHandlerId;
    }

    public void setBillRecentHandlerId(Integer billRecentHandlerId) {
        this.billRecentHandlerId = billRecentHandlerId;
    }

    public Date getBillStartDateTime() {
        return billStartDateTime;
    }

    public void setBillStartDateTime(Date billStartDateTime) {
        this.billStartDateTime = billStartDateTime;
    }

    public Date getBillEndDateTime() {
        return billEndDateTime;
    }

    public void setBillEndDateTime(Date billEndDateTime) {
        this.billEndDateTime = billEndDateTime;
    }

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getBillRemarks() {
        return billRemarks;
    }

    public void setBillRemarks(String billRemarks) {
        this.billRemarks = billRemarks;
    }
}
