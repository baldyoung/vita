
$(function(){
    DataModule.init();
    OFFModule.init();
    ONModule.init();
});
/**
 * 数据处理模块
 * @type {{requestProductTypeList: (function(): Array), requestProductList: (function(): Array), init: DataModule.init, sendData: DataModule.sendData, packageData: (function(): {onList: (*|Array), offList: (*|Array)}), saveData: DataModule.saveData, getOffProductList: DataModule.getOffProductList, productData: Array, getOnProductList: DataModule.getOnProductList, productTypeData: Array}}
 */
var DataModule = {
    productData : [],
    productTypeData : [],
    saveData : function() {
        var data = DataModule.packageData();
        DataModule.sendData(data);
    },
    packageData : function() {
        var offData = OFFModule.packageData();
        var onData = ONModule.packageData();
        var data = {
            offList : offData,
            onList : onData
        }
        return data;
    },
    getOnProductList : function(ptId) {
        var list = DataModule.productData;
        for (var i=0; i<list.length; i++) {
            var item = list[i];
            if (item.productTypeId == ptId) {
                return item.onProductList;
            }
        }
        return [];
    },
    getOffProductList : function(ptId) {
        var list = DataModule.productData;
        for (var i=0; i<list.length; i++) {
            var item = list[i];
            if (item.productTypeId == ptId) {
                return item.offProductList;
            }
        }
        return [];
    },
    init : function() {
        var productList = DataModule.requestProductList();
        var productTypeList = DataModule.requestProductTypeList();
        DataModule.productTypeData = productTypeList;
        var data = [];
        // 将对应类型的商品分成已上架和未上架
        for(var i=0; i<productTypeList.length; i++) {
            var typeId = productTypeList[i].productTypeId;
            if ("noType" == typeId) {
                continue;
            }
            var offList = [];
            var onList = [];
            for (var j=0; j<productList.length; j++) {
                if (productList[j].productTypeId == typeId) {
                    var temp = productList[j];
                    if (temp.isShow == 1) {
                        onList[onList.length] = temp;
                    } else {
                        offList[offList.length] = temp;
                    }
                    temp.hasType = true;
                }
            }
            var cell = {
                productTypeId : typeId,
                offProductList : offList,
                onProductList : onList
            }
            data[data.length] = cell;
        }
        // 筛选出没有指定类型的商品，并指定为未上架
        var noTypeList = [];
        for(var i=0; i<productList.length; i++) {
            var temp = productList[i];
            if (temp.hasType != true) {
                temp.productTypeId = 'noType';
                noTypeList[noTypeList.length] = temp;
            }
        }
        var cell = {
            productTypeId : "noType",
            offProductList : noTypeList,
            onProductList : []
        }
        data[data.length] = cell;
        DataModule.productData = data;
        console.log(data);
    },
    requestProductTypeList : function () {
        var targetData = [];
        $.ajax({
            url: GlobalConfig.serverAddress + "/mProductType/all",
            type: 'GET',
            cache: false,
            dataType: 'json',
            async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: null,
            success: function(data) {
                if (data.code != 0) {
                    swal('获取商品类型数据失败', data.desc, 'error');
                } else {
                    targetData = data.data;
                    targetData[targetData.length] = {
                        productTypeId : "noType",
                        productTypeName : '【无类型】'
                    }
                }
            },
            error: function() {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
        return targetData;
    },
    requestProductList : function () {
        var targetData = [];
        $.ajax({
            url: GlobalConfig.serverAddress + "/mProduct/all",
            type: 'GET',
            cache: false,
            dataType: 'json',
            async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: null,
            success: function(data) {
                if (data.code != 0) {
                    swal('获取商品数据失败', data.desc, 'error');
                } else {
                    targetData = data.data;
                    for (var i=0; i<targetData.length; i++) {
                        var item = targetData[i];
                        item.productStockName = (item.productStockFlag == 1 ? ("库存:"+item.productStock) : "无库存限制");
                    }
                }
            },
            error: function() {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
        return targetData;
    },
    sendData : function(t) {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mMenu/menuUpdate",
            type: 'POST',
            cache: false,
            // dataType: 'json',
            dataType : 'json',
            // async: false, //设置同步
            // contentType: "application/x-www-form-urlencoded;charset=utf-8",
            contentType : 'application/json; charset=utf-8',
            data: JSON.stringify(t),
            success: function(data) {
                if (data.code != 0) {
                    swal('更新菜单失败', data.desc, 'error');
                } else {
                    swal('菜单更新成功', '', 'success');
                }
            },
            error: function() {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    }
}
/**
 * 下架商品模块
 * @type {{init: OFFModule.init, loadType: OFFModule.loadType, createTypeUnitHTML: (function(*): string), loadProduct: OFFModule.loadProduct, createProductUnitHTML: (function(*): string), createTypeAreaHTML: (function(*): string)}}
 */
var OFFModule = {

    init : function() {
        OFFModule.loadType(DataModule.productTypeData);
        var temp = DataModule.productTypeData;
        if (temp.length > 0) {
            $('#offProductTypeKK' + temp[0].productTypeId).click();
        }
    },
    packageData : function() {
        //console.log(">>>>");
        var data = [];
        var typeUnitList = $('.offProductFlag');
        // console.log(typeUnitList);
        for (var i=0; i<typeUnitList.length; i++) {
            var item = $(typeUnitList[i]);
            // 当前列队的商品类型编号
            var typeId = item.attr("producttypeid");
            // console.log(typeId);
            var productUnitList = item.children();
            var pIdList = [];
            for (var j=0; j<productUnitList.length; j++) {
                var cell = $(productUnitList[j]);
                var productId = cell.attr("productid");
                pIdList[pIdList.length] = productId;
            }
            var temp = {
                productTypeId : typeId,
                productIdList : pIdList
            }
            data[data.length] = temp;
            // console.log(productUnitList);
        }
        return data;
        //console.log(data);
    },
    loadType : function (data) {
        var target = $('#S_OFF_TYPE_LIST');
        var targetB = $('#S_OFF_AREA_LIST');
        var temp;
        target.html('');
        targetB.html('');
        /*var temp = {
            productTypeId : "-1",
            productTypeName : '全部商品'
        };
        target.append(OFFModule.createTypeUnitHTML(temp));
        targetB.append(OFFModule.createTypeAreaHTML(temp));*/
        for(var i=0; i<data.length; i++) {
            var item = data[i];
            target.append(OFFModule.createTypeUnitHTML(item));
            targetB.append(OFFModule.createTypeAreaHTML(item));
            OFFModule.loadProduct(item.productTypeId, DataModule.getOffProductList(item.productTypeId));
        }
    },
    loadProduct : function (ptId, productList) {
        var target = $('#offProductList'+ptId);
        for (var i=0; i<productList.length; i++) {
            var item = productList[i];
            target.append(OFFModule.createProductUnitHTML(item));
        }
    },
    createTypeUnitHTML : function (data) {
        var html = '<li class="" style="width:110px;">';
            html += '<a id="offProductTypeKK'+data.productTypeId+'" data-toggle="tab" href="#tab-off-area'+data.productTypeId+'" style="padding-left:2px; padding-right:0px; " aria-expanded="true"> '+data.productTypeName+'</a>';
            html += '</li>';
        return html;
    },
    createTypeAreaHTML : function (data) {
        var html = '<div id="tab-off-area'+data.productTypeId+'" class="tab-pane" >' +
            '<div class="panel-body" style="padding-bottom:0px;">' +
            '<div class="ibox" style="border: 0px solid; height:909px; overflow:auto;">' +
            '<ul id="offProductList'+data.productTypeId+'" productTypeId="'+data.productTypeId+'" class="sortable-list connectList agile-list productListArea offProductFlag">' +
            '</ul>' +
            '</div>' +
            '</div>' +
            '</div>';
        return html;
    },
    createProductUnitHTML : function (data) {
        var html = '<li productId="'+data.productId+'" class="info-element">' +
            '<img style="width:100px; height:100px; border: 1px solid #D3D4D3;" src="'+GlobalConfig.productImgRelativePath+data.productImgName+'" />' +
            '<div style="width:255px; padding-left:3px;  float:right; ">' +
            '<div style="width:255px; height:33px; font-size:23px; font-weight: bold; overflow: hidden;">' + data.productName +
            '</div>' +
            '<div style="width:255px; height:27px; overflow: hidden;">' +
            '<i id="pUnitStockText'+data.productId+'" style="float:left; font-style:normal;">'+data.productStockName+'</i>' +
            '<i id="pUnitPriceText'+data.productId+'" style="float:right;">'+data.productPrice+'</i>' +
            '</div>' +
            '<div style="width:255px; height:39px; overflow: hidden; padding-top:5px; ">' +
            '<a onclick="ProductEditModule.requestAndLoadData('+data.productId+')" class="pull-right btn btn-xs btn-primary" data-toggle="modal" data-target="#updateProductPanel">修改</a>' +
            '</div>' +
            '</div>' +
            '  </li>';
        return html;
    }
}
/**
 * 上架商品模块
 * @type {{init: ONModule.init, loadType: ONModule.loadType, createTypeUnitHTML: (function(*): string), loadProduct: ONModule.loadProduct, createProductUnitHTML: (function(*): string), createTypeAreaHTML: (function(*): string)}}
 */
var ONModule = {
    init : function() {
        ONModule.loadType(DataModule.productTypeData);
        var temp = DataModule.productTypeData;
        if (temp.length > 0) {
            $('#onProductTypeKK' + temp[0].productTypeId).click();
        }
    },
    packageData : function() {
        //console.log(">>>>");
        var data = [];
        var typeUnitList = $('.onProductFlag');
        // console.log(typeUnitList);
        for (var i=0; i<typeUnitList.length; i++) {
            var item = $(typeUnitList[i]);
            // 当前列队的商品类型编号
            var typeId = item.attr("producttypeid");
            // console.log(typeId);
            var productUnitList = item.children();
            var pIdList = [];
            for (var j=0; j<productUnitList.length; j++) {
                var cell = $(productUnitList[j]);
                var productId = cell.attr("productid");
                pIdList[pIdList.length] = productId;
            }
            var temp = {
                productTypeId : typeId,
                productIdList : pIdList
            }
            data[data.length] = temp;
            // console.log(productUnitList);
        }
        return data;
        //console.log(data);
    },
    loadType : function (data) {
        var target = $('#S_ON_TYPE_LIST');
        var targetB = $('#S_ON_AREA_LIST');
        var temp;
        target.html('');
        targetB.html('');
        /*var temp = {
            productTypeId : "-1",
            productTypeName : '全部商品'
        };
        target.append(OFFModule.createTypeUnitHTML(temp));
        targetB.append(OFFModule.createTypeAreaHTML(temp));*/
        for(var i=0; i<data.length; i++) {
            var item = data[i];
            if (item.productTypeId == "null") {
                continue;
            }
            target.append(ONModule.createTypeUnitHTML(item));
            targetB.append(ONModule.createTypeAreaHTML(item));
            ONModule.loadProduct(item.productTypeId, DataModule.getOnProductList(item.productTypeId));
        }
    },
    loadProduct : function (ptId, productList) {
        var target = $('#offProductList'+ptId);
        for (var i=0; i<productList.length; i++) {
            var item = productList[i];
            target.append(ONModule.createProductUnitHTML(item));
        }
    },
    createTypeUnitHTML : function (data) {
        var html = '<li class="" style="width:110px;">';
        html += '<a id="onProductTypeKK'+data.productTypeId +'" data-toggle="tab" href="#tab-on-area'+data.productTypeId+'" style="padding-left:2px; padding-right:0px; " aria-expanded="true"> '+data.productTypeName+'</a>';
        html += '</li>';
        return html;
    },
    createTypeAreaHTML : function (data) {
        var html = '<div id="tab-on-area'+data.productTypeId+'" class="tab-pane">' +
            '<div class="panel-body" style="padding-bottom:0px;">' +
            '<div class="ibox" style="border: 0px solid; height:909px; overflow:auto;">' +
            '<ul id="onProductList'+data.productTypeId+'" productTypeId="'+data.productTypeId+'" class="sortable-list connectList agile-list productListArea onProductFlag">' +
            '</ul>' +
            '</div>' +
            '</div>' +
            '</div>';
        return html;
    },
    createProductUnitHTML : function (data) {
        var html = '<li productId="'+data.productId+'" class="info-element">' +
            '<img style="width:100px; height:100px; border: 1px solid #D3D4D3;" src="'+GlobalConfig.productImgRelativePath+data.productImgName+'" />' +
            '<div style="width:255px; padding-left:3px;  float:right; ">' +
            '<div style="width:255px; height:33px; font-size:23px; font-weight: bold; overflow: hidden;">' + data.productName +
            '</div>' +
            '<div style="width:255px; height:27px; overflow: hidden;">' +
            '<i id="pUnitStockText'+data.productId+'" style="float:left; font-style:normal;">'+data.productStockName+'</i>' +
            '<i id="pUnitPriceText'+data.productId+'" style="float:right;">'+data.productPrice+'</i>' +
            '</div>' +
            '<div style="width:255px; height:39px; overflow: hidden; padding-top:5px; ">' +
            '<a onclick="ProductEditModule.requestAndLoadData('+data.productId+')" class="pull-right btn btn-xs btn-primary" data-toggle="modal" data-target="#updateProductPanel">修改</a>' +
            '</div>' +
            '</div>' +
            '  </li>';
        return html;
    }
}

var ProductEditModule = {
    currentProductId : undefined,
    init : function() {

    },
    saveData : function() {
        var data = ProductEditModule.packageData();
        console.log("修改后的商品属性：");
        console.log(data);
        if (undefined != data) {
            ProductEditModule.sendData(data);
        }
    },
    packageData : function() {
        var data = {
            productStock : $('#newProductStockText').val(),
            productId : ProductEditModule.currentProductId,
            productPrice : $('#newProductPriceText').val()
        }
        if ($('#stockSetBtn').is(':checked')) {
            if (GlobalMethod.isEmpty(data.productStock)) {
                swal('商品库存不能为空', '如不需要库存限制，请取消“库存限制”的勾选', 'error');
                return undefined;
            }
            data.productStockFlag = '1';
        } else {
            data.productStockFlag = '0';
        }
        return data;
    },
    loadData : function (data) {
        ProductEditModule.currentProductId = data.productId;
        $('#productNameText').text(data.productName);
        $('#productPriceText').text(data.productPrice);
        $('#newProductPriceText').val('');
        $('#productStockText').text(data.productStockName);
        if (data.productStockFlag == 1) {
            // 有库存限制
            $('#stockSetBtn').prop('checked', 'true');
            $('#newProductStockText').removeAttr('readonly');
            $('#newProductStockText').val(data.productStock);
        } else {
            $('#stockSetBtn').removeAttr('checked');
            $('#newProductStockText').prop('readonly', 'true');
            $('#newProductStockText').val('');
        }
    },
    requestAndLoadData : function (pId) {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mProduct/getProduct",
            type: 'GET',
            cache: false,
            dataType : 'json',
            // async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            // contentType : 'application/json; charset=utf-8',
            data: {
                productId : pId
            },
            success: function(data) {
                if (data.code != 0) {
                    swal('获取商品信息失败', data.desc, 'error');
                } else {
                    data = data.data;
                    data.productStockName = (data.productStockFlag == 1 ? data.productStock : "无库存限制");
                    ProductEditModule.loadData(data);
                }
            },
            error: function() {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    },
    sendData : function (t) {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mProduct/updateSimple",
            type: 'POST',
            cache: false,
            dataType : 'json',
            // async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            // contentType : 'application/json; charset=utf-8',
            data: t,
            success: function(data) {
                if (data.code != 0) {
                    swal('修改失败', data.desc, 'error');
                } else {
                    $('#btnCloseProductEdit').click();
                    swal('修改成功', '', 'success');
                    // --------------------------------------------------------------------- 修改本地对应的展示单元
                    t.productStockName = (t.productStockFlag == 1 ? ("库存:"+t.productStock) : "无库存限制");
                    $('#pUnitStockText'+t.productId).text(t.productStockName);
                    if (!GlobalMethod.isEmpty(t.productPrice)) {
                        $('#pUnitPriceText'+t.productId).text(t.productPrice);
                    }
                }
            },
            error: function() {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    },
    updateCheckedStatus : function () {
        if ($('#stockSetBtn').is(':checked')) {
            // 选用库存限制
            $('#newProductStockText').removeAttr('readonly');
            $('#newProductStockText').val('');
        } else {
            $('#newProductStockText').prop('readonly', 'true');
            $('#newProductStockText').val('');
        }
    }
}










