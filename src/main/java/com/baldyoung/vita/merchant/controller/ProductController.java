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

    /**
     * 获取商品的统计数据
     * @return
     */
    @GetMapping("targetCountInfo")
    public ResponseResult getTargetCountInfo(@RequestParam(value = "productTypeId", required = false)Integer productTypeId,
                                             @RequestParam(value = "productIsShow", required = false)Integer isShow) {
        return success(productService.getProductTargetCountInfo(productTypeId, isShow));
    }

    /**
     * 分页的方式获取商品数据集
     * @param productTypeId
     * @param isShow
     * @param pageIndex
     * @param maxSize
     * @return
     */
    @PostMapping("pagingList")
    public ResponseResult getProductPagingList(@RequestParam(value = "productTypeId", required = false)Integer productTypeId,
                                               @RequestParam(value = "productIsShow", required = false)Integer isShow,
                                               @RequestParam(value = "pageIndex")Integer pageIndex,
                                               @RequestParam(value = "maxSize")Integer maxSize) {
        if (pageIndex.intValue() < 0 || maxSize.intValue() < 0) {
            return defeat("参数格式不正确");
        }
        Integer startIndex = (pageIndex - 1) * maxSize;
        return success(productService.getProductPagingList(productTypeId, isShow, startIndex, maxSize));
    }

    /**
     * 新增或修改指定商品信息
     * @param newProductDto
     * @param bindingResult
     * @return
     */
    @PostMapping("addOrUpdate")
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
    @GetMapping("getProduct")
    public ResponseResult getProduct(@RequestParam("productId")Integer productId) {
        ProductEntity productEntity = productService.getProductByProductId(productId);
        if (null == productEntity) {
            return defeat();
        }
        return success(productEntity);
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
