package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.entity.ProductSortEntity;
import com.baldyoung.vita.common.pojo.entity.ProductTypeSortEntity;
import com.baldyoung.vita.merchant.service.MProductServiceImpl;
import com.baldyoung.vita.merchant.service.MProductSortServiceImpl;
import com.baldyoung.vita.merchant.service.MProductTypeServiceImpl;
import com.baldyoung.vita.merchant.service.MProductTypeSortServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.defeat;
import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmptyCollection;
import static com.baldyoung.vita.common.utility.CommonMethod.toInteger;
import static java.lang.System.out;

@RestController
@RequestMapping("mMenu")
public class MMenuController {

    @Autowired
    private MProductServiceImpl productService;

    @Autowired
    private MProductTypeServiceImpl productTypeService;

    @Autowired
    private MProductSortServiceImpl productSortService;

    @Autowired
    private MProductTypeSortServiceImpl productTypeSortService;

    @PostMapping("menuProductUpdate")
    public ResponseResult updateMenu(@RequestBody Map param) {
        out.println(param.toString());
        List<Map> offList = (List<Map>) param.get("offList");
        List<Map> onList = (List<Map>) param.get("onList");
        if (null == offList || null == onList) {
            return defeat("非法数据");
        }
        List<Integer> productIdList = new LinkedList();
        // 更新下架商品集的 类型 和 上下架标识
        for (Map item : offList) {
            Integer typeId = toInteger(item.get("productTypeId"));
            List<Integer> pIdList = (List<Integer>) item.get("productIdList");
            if (!isEmptyCollection(pIdList)) {
                productIdList.addAll(pIdList);
                productService.updateSimpleProductList(typeId, 0, pIdList);
            }
        }
        // 更新上架商品集的 类型 和 上下架标识
        for (Map item : onList) {
            Integer typeId = toInteger(item.get("productTypeId"));
            List<Integer> pIdList = (List<Integer>) item.get("productIdList");
            if (!isEmptyCollection(pIdList)) {
                productIdList.addAll(pIdList);
                productService.updateSimpleProductList(typeId, 1, pIdList);
            }
        }
        // 更新商品的排序数据
        List<ProductSortEntity> sortList = new ArrayList(productIdList.size());
        int grade = productIdList.size();
        for (int i=0; i<productIdList.size(); i++) {
            ProductSortEntity entity = new ProductSortEntity();
            entity.setProductId(productIdList.get(i));
            entity.setProductGrade(grade--);
            sortList.add(entity);
        }
        productSortService.setProductGradeList(sortList);
        return success();
    }

    @PostMapping("menuProductTypeUpdate")
    public ResponseResult updateMenuProductType(@RequestBody Map param) {
        out.println(param.toString());
        List<Integer> onTypeList = (List<Integer>) param.get("onTypeList");
        List<Integer> offTypeList = (List<Integer>) param.get("offTypeList");
        // 修改品类上下架标识
        if (!isEmptyCollection(offTypeList)) {
            productTypeService.updateSimpleProductTypeList( offTypeList, 0);
        }
        if (!isEmptyCollection(onTypeList)) {
            productTypeService.updateSimpleProductTypeList(onTypeList, 1);
        }
        // 修改品类排序记录
        Integer grade = 0;
        List<ProductTypeSortEntity> list = new ArrayList(onTypeList.size() + offTypeList.size());
        for (int i=offTypeList.size()-1; i>=0; i--) {
            ProductTypeSortEntity entity = new ProductTypeSortEntity();
            entity.setProductTypeId(offTypeList.get(i));
            entity.setProductTypeGrade(grade++);
            list.add(entity);
        }
        for (int i=onTypeList.size()-1; i>=0; i--) {
            ProductTypeSortEntity entity = new ProductTypeSortEntity();
            entity.setProductTypeId(onTypeList.get(i));
            entity.setProductTypeGrade(grade++);
            list.add(entity);
        }
        productTypeSortService.setProductTypeGradeList(list);
        return success();
    }


}
