package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;
import static java.lang.System.out;

@RestController
@RequestMapping("mMenu")
public class MenuController {


    @PostMapping("menuUpdate")
    public ResponseResult updateMenu(@RequestBody Map param) {
        out.println(param.toString());
        List<Map> list = (List<Map>) param.get("offList");
        out.println("\noffList >>>"+list.toString());
        Map map = list.get(6);
        out.println("\noffList -> item(0):"+map.toString());
        List<Integer> pIdList = (List<Integer>) map.get("productIdList");
        out.println("\noffList -> item(0) -> productIdList:"+pIdList.toString());
        return success();
    }


}
