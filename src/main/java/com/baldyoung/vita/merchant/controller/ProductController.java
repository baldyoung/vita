package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.dto.product.NewProductDto;
import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import com.baldyoung.vita.common.utility.FileDataSaveModule;
import com.baldyoung.vita.merchant.service.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.*;
import static java.lang.System.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("mProduct")
public class ProductController {

    // 商品图片存储路径
    private static String ProductImgPath = createProductImgPath();

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
    public ResponseResult addProduct(@Valid NewProductDto newProductDto, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return defeat(bindingResult.getFieldError().getDefaultMessage());
        }
        MultipartFile multipartFile = newProductDto.getFile();
        if (null == newProductDto.getProductId() && null == multipartFile && null == newProductDto.getProductImgName()) {
            return defeat("图片为空");
        }
        if (null != multipartFile) {
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            out.println(fileType);
            out.println(ProductImgPath);
            FileDataSaveModule fileDataSaveModule = FileDataSaveModule.createTargetInstance(multipartFile.getInputStream(), ProductImgPath, fileType);
            out.println(fileDataSaveModule.getPathName());
            Boolean result = fileDataSaveModule.save();
            if (!result) {
                return defeat("商品图片保存失败！");
            }
            String temp = fileDataSaveModule.getPathName();
            fileName = temp.substring(temp.lastIndexOf(File.separator) + 1);
            newProductDto.setProductImgName(fileName);
        }
        if (null == newProductDto.getProductId()) {
            // 新增
            productService.addProduct(newProductDto);
        } else {
            // 修改
            productService.updateProduct(newProductDto);
        }
        return success();
    }
    @GetMapping("getProduct")
    public ResponseResult getProduct(@RequestParam("productId")Integer productId) {
        ProductEntity productEntity = productService.getProductByProductId(productId);
        if (null == productEntity) {
            return defeat();
        }
        return success(productEntity);
    }























    static private String createProductImgPath() {
        String staticPath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        return FileDataSaveModule.adjustPathNameSeparator(staticPath+"/vita/resource/productImg/");
    }



}
