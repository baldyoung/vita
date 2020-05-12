package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.dto.product.NewProductDto;
import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import com.baldyoung.vita.common.pojo.exception.ExceptionBase;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.utility.FileDataSaveModule;
import com.baldyoung.vita.merchant.service.MProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.defeat;
import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;
import static java.lang.System.out;

/**
 * 商品管理-后端接口
 */
@RestController
@RequestMapping("mProduct")
public class MProductController {

    // 商品图片存储路径
    private static String ProductImgPath = createProductImgPath();

    @Autowired
    private MProductServiceImpl productService;

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
    public ResponseResult addProduct(@Valid NewProductDto newProductDto, BindingResult bindingResult) throws IOException, ExceptionBase {
        String desc = "";
        if (bindingResult.hasErrors()) {
            return defeat(bindingResult.getFieldError().getDefaultMessage());
        }
        MultipartFile multipartFile = newProductDto.getFile();
        if (null == newProductDto.getProductId() && null == multipartFile && null == newProductDto.getProductImgName()) {
            return defeat("图片为空");
        }
        if (null != multipartFile) {
            // 判读图片的长宽
            BufferedImage bi= ImageIO.read(multipartFile.getInputStream());
            Integer width = bi.getWidth();
            Integer height = bi.getHeight();
            out.println("(W:"+width+", H:"+height+")");
            if (Math.abs(width - height) > 5) {
                desc = "图片长宽不等，展示效果可能不佳！";
            }
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
        if (0 == newProductDto.getProductStockFlag().intValue()) {
            newProductDto.setProductStock(0);
        }
        if (null == newProductDto.getProductId()) {
            // 新增
            productService.addProduct(newProductDto);
        } else {
            // 修改
            productService.updateProduct(newProductDto);
        }
        return success(null, desc);
    }

    /**
     * 获取指定商品信息
     * @param productId
     * @return
     */
    @GetMapping("getProduct")
    public ResponseResult getProduct(@RequestParam("productId")Integer productId) {
        ProductEntity productEntity = productService.getProductByProductId(productId);
        if (null == productEntity) {
            return defeat();
        }
        return success(productEntity);
    }

    /**
     * 删除指定商品
     * @param productId
     * @return
     */
    @GetMapping("deleteProduct")
    public ResponseResult deleteProduct(@RequestParam("productId")Integer productId) {
        productService.deleteProduct(productId);
        return success();
    }

    /**
     * 获取所有商品
     * @return
     */
    @GetMapping("all")
    public ResponseResult getAllProduct() {
        return success(productService.getAllProduct());
    }

    /**
     * 为菜单编辑时提供商品的简要属性（价格、库存）修改
     * @param productId
     * @param productStockFlag
     * @param productStock
     * @param productPrice
     * @return
     * @throws ServiceException
     */
    @PostMapping("updateSimple")
    public ResponseResult updateSimple(@RequestParam("productId")Integer productId,
                                       @RequestParam("productStockFlag")Integer productStockFlag,
                                       @RequestParam(value = "productStock", required = false)Integer productStock,
                                       @RequestParam(value = "productPrice", required = false)BigDecimal productPrice
                                       ) throws ServiceException {
        boolean invalid = (0 != productStockFlag.intValue() && 1 != productStockFlag.intValue()) || (1 == productStockFlag.intValue() && null == productStock);
        if (invalid) {
            return defeat("非法数据");
        }
        NewProductDto newProductDto = new NewProductDto();
        newProductDto.setProductId(productId);
        newProductDto.setProductStock(productStock);
        newProductDto.setProductStockFlag(productStockFlag);
        newProductDto.setProductPrice(productPrice);
        productService.updateProduct(newProductDto);
        return success();
    }

    /**
     * 获取所有有效的商品
     * @return
     */
    @GetMapping("allValidProduct")
    public ResponseResult getAllValidProduct() {

        return success(productService.getAllValidProduct());
    }























    static private String createProductImgPath() {
        String staticPath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        return FileDataSaveModule.adjustPathNameSeparator(staticPath+"/vita/resource/productImg/");
    }



}
