package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.merchant.service.MProductAttributeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;
@RestController
@RequestMapping("mProductAttribute")
public class MProductAttributeController {

    @Autowired
    private MProductAttributeServiceImpl productAttributeService;

    @GetMapping("attributeTypeList")
    public ResponseResult getAllAttributeType() {
        return success(productAttributeService.getAllAttributeType());
    }

    @GetMapping("attributeValueList")
    public ResponseResult getAllAttributeValue() {
        return success(productAttributeService.getAllAttributeValue());
    }

    @PostMapping("addType")
    public ResponseResult addAttributeType(@RequestParam("typeName")String typeName) {
        productAttributeService.addAttributeType(typeName);
        return success();
    }

    @PostMapping("addValue")
    public ResponseResult addAttributeValue(@RequestParam("valueName")String valueName,
                                            @RequestParam("typeId")Integer typeId) {
        productAttributeService.addAttributeValue(valueName, typeId);
        return success();
    }

    @PostMapping("deleteType")
    public ResponseResult deleteAttributeType(@RequestParam("typeId")Integer typeId) {
        productAttributeService.deleteAttributeType(typeId);
        return success();
    }

    @PostMapping("deleteValue")
    public ResponseResult deleteAttributeValue(@RequestParam("valueId")Integer valueId) {
        productAttributeService.deleteAttributeValue(valueId);
        return success();
    }
}
