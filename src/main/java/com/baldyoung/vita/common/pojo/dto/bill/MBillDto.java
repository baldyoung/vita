package com.baldyoung.vita.common.pojo.dto.bill;

import com.baldyoung.vita.common.pojo.dto.order.MOrderDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class MBillDto {
    private Integer billId;
    private String billNumber;
    private Integer billOwnerTypeFlag;
    private Integer billOwnerId;
    private String billOwnerName;
    private Integer billCustomerNumber;
    private String billCustomerName;
    private Integer billOrderQuantity;
    private BigDecimal billTotalAmount;
    private BigDecimal billReceivedAmount;
    private Date billReceivedDateTime;
    private String billRecentHandlerName;
    private Integer billRecentHandlerId;
    private Date billStartDateTime;
    private Date billEndDateTime;
    private String billRemarks;

    private List<MOrderDto> orderList;

    public String getBillCustomerName() {
        return billCustomerName;
    }

    public void setBillCustomerName(String billCustomerName) {
        this.billCustomerName = billCustomerName;
    }

    public List<MOrderDto> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<MOrderDto> orderList) {
        this.orderList = orderList;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
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

    public String getBillRemarks() {
        return billRemarks;
    }

    public void setBillRemarks(String billRemarks) {
        this.billRemarks = billRemarks;
    }
}
