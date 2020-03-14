package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.dto.product.NewProductDto;
import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import com.baldyoung.vita.merchant.service.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.*;
import static java.lang.System.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("mProduct")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("pagingList")
    public ResponseResult getProductPagingList() {
        return success(productService.getProductPagingList());
    }

    @PostMapping("add")
    public ResponseResult addProduct(@Valid NewProductDto newProductDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return defeat(bindingResult.getFieldError().getDefaultMessage());
        }
        MultipartFile multipartFile = newProductDto.getFile();
        if (null == multipartFile && null == newProductDto.getProductImgName()) {
            return defeat("图片为空");
        }
        if (null != multipartFile) {
            String str = multipartFile.getName() + ", " + multipartFile.getSize() + ", " + multipartFile.getContentType() + ", " + multipartFile.getOriginalFilename();
            out.println(str);
        }
        return success();
        /*out.println(multipartFile.getContentType());
        out.println(multipartFile.getName() + ", " + multipartFile.getOriginalFilename());
        out.println(multipartFile.getSize());
        return success(newProductDto);*/
    }



    public ResponseResult addProduct2(@RequestParam("newProductInfo") @Valid NewProductDto newProductDto, @RequestParam(value = "file", required = false)MultipartFile multipartFile) {
        out.println(newProductDto );
        out.println(multipartFile.getContentType());
        out.println(multipartFile.getName() + ", " + multipartFile.getOriginalFilename());
        out.println(multipartFile.getSize());
        return success(newProductDto);
    }

    @PostMapping("add2")
    public Map addProduct2(@RequestParam("productName")String productName,
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
        productEntity.setProductAttributeTypeId(productAttributeId);
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
