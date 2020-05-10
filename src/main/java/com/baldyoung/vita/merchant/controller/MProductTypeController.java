package com.baldyoung.vita.merchant.controller;


import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.dto.productType.ProductTypeDto;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.merchant.service.MProductTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.*;
import static com.baldyoung.vita.common.utility.CommonMethod.isAnyEmpty;

@RestController
@RequestMapping("mProductType")
public class MProductTypeController {

    @Autowired
    private MProductTypeServiceImpl productTypeService;

    /**
     * 获取所有商品类型
     * @return
     */
    @GetMapping("all")
    public ResponseResult getAllType() {
        return success(productTypeService.getAllProductType());
    }

    @GetMapping("details")
    public ResponseResult getProductTypeInfo(@RequestParam("productTypeId")Integer productTypeId) {
        return success(productTypeService.findByProductTypeId(productTypeId));
    }

    /**
     * 新增或修改商品类型
     * @param productTypeName
     * @param productTypeId
     */
    @PostMapping("updateOrAdd")
    public ResponseResult updateOrAddProductType(@RequestParam("productTypeName")String productTypeName,
                                  //@RequestParam(value = "isShow", required = false)Integer isShow,
                                  //@RequestParam(value = "productTypeGrade", required = false)Integer productTypeGrade,
                                  @RequestParam(value = "productTypeId", required = false)Integer productTypeId
                                  ) throws ServiceException {
         ProductTypeDto productTypeDto = new ProductTypeDto();
         productTypeDto.setProductTypeId(productTypeId);
         productTypeDto.setProductTypeName(productTypeName);
         if (null != productTypeId) {
             // 修改商品类型
             if (isAnyEmpty(productTypeName)) {
                 return defeat("请补全信息");
             }
         } else {
             // 新增商品类型
             productTypeDto.setProductTypeGrade(66);
             productTypeDto.setIsShow(1);
         }
         if (productTypeName.length() > 20) {
            return defeat("名称不能超过20个字");
         }
         productTypeService.updateOrAddProductType(productTypeDto);
         return success();
    }

    /**
     * 删除商品类型
     * @param productTypeId
     * @return
     */
    @PostMapping("delete")
    public ResponseResult deleteProductType(@RequestParam("productTypeId")Integer productTypeId) {
        productTypeService.deleteProductType(productTypeId);
        return success();
    }


}
