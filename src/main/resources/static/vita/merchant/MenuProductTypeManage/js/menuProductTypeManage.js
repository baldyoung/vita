
$(function(){
    DataModule.init();
    ProductTypeModule.init();
});

var DataModule = {
    productTypeData : [],
    init :function () {
        DataModule.productTypeData = DataModule.requestProductTypeData();
    },
    saveAction : function () {
        swal({
                title: "您确定保存当前状态吗?",
                text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: false,
                closeOnCancel: true
            },
            function(isConfirm) {
                if (isConfirm) {
                    var data = ProductTypeModule.packageData();
                    console.log(data);
                    DataModule.sendData(data);
                } else {
                    swal("已取消保存", "未保存！", "error");
                }
            });
    },
    requestProductTypeData : function () {
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
                    targetData = sortProductTypeList(targetData);
                }
            },
            error: function() {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
        return targetData;
    },
    sendData : function (t) {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mMenu/menuProductTypeUpdate",
            type: 'POST',
            cache: false,
            dataType: 'json',
            // async: false, //设置同步
            contentType : 'application/json; charset=utf-8',
            data: JSON.stringify(t),
            success: function(data) {
                if (data.code != 0) {
                    swal('数据保存失败', data.desc, 'error');
                } else {
                    swal('保存成功', '数据已更新', 'success');
                }
            },
            error: function() {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    }
}

var ProductTypeModule = {

    init : function () {
        ProductTypeModule.loadData(DataModule.productTypeData);
    },
    packageData : function () {
        var targetOn = $('#onProductTypeArea');
        var targetOff = $('#offProductTypeArea');
        var onTypeUnitList = targetOn.children();
        var offTypeUnitList = targetOff.children();
        var onList = [], offList = [];
        for (var i=0; i<onTypeUnitList.length; i++) {
            var item = $(onTypeUnitList[i]);
            onList[onList.length] = parseInt(item.attr("producttypeid"));
        }
        for (var i=0; i<offTypeUnitList.length; i++) {
            var item = $(offTypeUnitList[i]);
            offList[offList.length] = parseInt(item.attr("producttypeid"));
        }
        var data = {
            onTypeList : onList,
            offTypeList : offList
        }
        return data;
    },
    loadData : function (data) {
        var targetOn = $('#onProductTypeArea');
        var targetOff = $('#offProductTypeArea');
        targetOn.html('');
        targetOff.html('');
        for (var i=0; i<data.length; i++) {
            var item = data[i];
            var html = ProductTypeModule.createTypeUnitHTML(item);
            if (item.isShow == 1) {
                targetOn.append(html);
            } else {
                targetOff.append(html);
            }
        }
    },
    createTypeUnitHTML : function (data) {
        var html = '<li productTypeId="'+data.productTypeId+'" class="warning-element">'+data.productTypeName+'</li>';
        return html;
    }
}