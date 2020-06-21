package com.baldyoung.vita.common.service.impl;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static com.baldyoung.vita.common.utility.CommonMethod.isEmpty;

/**
 * 电子账单服务
 */
@Service
public class InvoiceServiceImpl {

    /**
     * 账单匹配图，key为映射URL，value为对应的账单编号
     */
    private Map<String, String> invoiceMap;

    @PostConstruct
    public void init() {
        invoiceMap = new HashMap();
    }

    public void setInvoiceKeyValue(String key, String billNumber) {
        if (isEmpty(key) || isEmpty(billNumber)) {
            return;
        }
        invoiceMap.put(key, billNumber);
    }

    public String getInvoiceValueByKey(String key) {
        return invoiceMap.get(key);
    }

    public void removeInvoiceKeyValue(String key) {
        invoiceMap.remove(key);
    }
}
