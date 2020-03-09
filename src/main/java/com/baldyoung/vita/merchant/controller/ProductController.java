package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static java.lang.System.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("mProduct")
public class ProductController {

    @PostMapping("add")
    public Map addProduct(@RequestParam("productName")String productName,
                          @RequestParam(value = "productTypeId", required = false)Integer productTypeId,
                          @RequestParam(value = "productAttributeId", required = false)Integer productAttributeId,
                          @RequestParam("productPrice")BigDecimal productPrice,
                          @RequestParam("productStockFlag")Integer productStockFlag,
                          @RequestParam(value = "productStock", required = false)Integer productStock,
                          @RequestParam("productIsShow")Integer productIsShow,
                          @RequestParam(value = "productInfo", required = false)String productInfo,
                          @RequestParam(value = "productGrade", required = false)Integer productGrade,
                          @RequestParam("file")MultipartFile multipartFile) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(productName);
        productEntity.setProductTypeId(productTypeId);
        productEntity.setProductAttributeId(productAttributeId);
        productEntity.setProductPrice(productPrice);
        productEntity.setProductStockFlag(productStockFlag);
        productEntity.setProductStock(productStock);
        productEntity.setProductIsShow(productIsShow);
        productEntity.setProductInfo(productInfo);
        productEntity.setProductGrade(productGrade);

        out.println(productEntity);

        out.println(multipartFile.getContentType());
        out.println(multipartFile.getName() + ", " + multipartFile.getOriginalFilename());
        out.println(multipartFile.getSize());

        return null;
    }

}
