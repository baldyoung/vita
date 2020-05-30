package com.baldyoung.vita.common.pojo.entity;

import java.util.Date;

public class MerchantUserEntity {
    private Integer merchantUserId;
    private Integer merchantUserGrade;
    private String merchantUserName;
    private String merchantUserAccount;
    private String merchantUserPassword;
    private Date createDateTime;
    private Date lastModifiedDateTime;

    public Integer getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(Integer merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    public Integer getMerchantUserGrade() {
        return merchantUserGrade;
    }

    public void setMerchantUserGrade(Integer merchantUserGrade) {
        this.merchantUserGrade = merchantUserGrade;
    }

    public String getMerchantUserName() {
        return merchantUserName;
    }

    public void setMerchantUserName(String merchantUserName) {
        this.merchantUserName = merchantUserName;
    }

    public String getMerchantUserAccount() {
        return merchantUserAccount;
    }

    public void setMerchantUserAccount(String merchantUserAccount) {
        this.merchantUserAccount = merchantUserAccount;
    }

    public String getMerchantUserPassword() {
        return merchantUserPassword;
    }

    public void setMerchantUserPassword(String merchantUserPassword) {
        this.merchantUserPassword = merchantUserPassword;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Date getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(Date lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }
}
