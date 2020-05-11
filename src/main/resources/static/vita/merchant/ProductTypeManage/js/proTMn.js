

$(function(){
    PTModule.init();
    EditPTModule.init();
});
// 商品类型主模块
var PTModule = {
    init : function() {
        PTModule.requestAndLoad();
    },
    loadData : function(data) {
        var dataTable = $('#ProTypeTable');
        if ($.fn.dataTable.isDataTable(dataTable)) {
            dataTable.DataTable().destroy();
        }
        dataTable.DataTable({
            'searching':true, //去掉搜索框
            'bLengthChange': true, //去掉每页显示多少条数据方法
            "serverSide": false, //关闭分页操作，默认就是关闭
            "autoWidth" : false, //
            "bSort": true, //打开排序功能
            "order": [[ 1, "desc"], [0, 'asc' ]], //默认排序的指标
            "pageLength": 25, //默认每页显示的数据条数
            'data':data, //表格的数据源
            'columns':[{
                data:'productTypeId'
            },{
                data:'productTypeName'
            },{
                data:'isShow'
            }
            ],
            "columnDefs": [{
                "render" : function(data, type, row) {
                    var a = "";
                    a+="<button class=\"btn btn-primary \" style='margin-top:3px;' type=\"button\" data-toggle=\"modal\" data-target=\"#OrderFormWindow\" onclick=\"EditPTModule.deleteAction('"+row.productTypeId+"','"+row.productTypeName+"')\"><i class=\"fa fa-building-o\"></i>删除"
                    a+="</button>"
                    a+="<button class=\"btn btn-primary \" style='margin-top:3px; margin-left:5px; ' type=\"button\" data-toggle=\"modal\" data-target=\"#myModal\" onclick=\"EditPTModule.loadDataAction('"+row.productTypeId+"','"+row.productTypeGrade+"','"+row.productTypeName+"',  '"+row.isShowText+"' )\"><i class=\"fa fa-building-o\"></i>修改"
                    a+="</button>"
                    return a;
                },
                "targets" :3
            }]
        });
    },
    requestAndLoad : function() {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mProductType/all",
            type: 'GET',
            cache: false,
            dataType:'json',
            contentType: "application/json; charset=utf-8",
            data: {},
            //processData: false,
            //contentType: false,
            success: function (data) {
                if (data.code == 0) {
                    data = data.data;
                    for(var i=0; i<data.length; i++) {
                        var item = data[i];
                        item.isShowText = item.isShow == 1 ? '已上架': '未上架';
                    }
                    PTModule.loadData(data);
                } else {
                    swal("获取数据失败", data.desc, "error");
                }
            }
        });
    }
}

// 编辑模块
var EditPTModule = {
    currentProductTypeId : undefined,
    init : function() {
        EditPTModule.resetData();
    },
    loadDataAction : function(PTId, PTGrade, PTName, isShowFlag) {
        var data = {
            productTypeId : PTId,
            productTypeName : PTName,
            productTypeGrade : PTGrade,
            isShowText : isShowFlag
        }
        EditPTModule.loadData(data);
    },
    deleteAction : function(PTId, PTName) {
        swal({
                title: "您确定要删除 " + PTName + " 吗?",
                text: "删除后将无法恢复，请谨慎操作！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function(isConfirm) {
                if (isConfirm) {
                    EditPTModule.deleteData(PTId);
                } else {
                    swal("已取消", "您取消了删除操作！", "error");
                }
            });
    },
    packageData : function() {
        var newData = {
            productTypeName : $('#NewPTNameText').val()
        };
        var updateData = {
            productTypeName : $('#PTNameText').val(),
            productTypeId : EditPTModule.currentProductTypeId
        };
        var data = EditPTModule.currentProductTypeId == undefined ? newData : updateData;
        if (GlobalMethod.isEmpty(data.productTypeName)) {
            swal("类型名称不能为空", "", "error");
            data = undefined;
        }
        return data;
    },
    loadData : function(data) {
        EditPTModule.currentProductTypeId = data.productTypeId;
        $('#PTNameText').val(data.productTypeName);
        $('#PTGradeText').val(data.productTypeGrade);
        $('#PTIsShowText').val(data.isShowText);
    },
    resetData : function() {
        EditPTModule.currentProductTypeId = undefined;
        $('#NewPTNameText').val('');
    },
    requestAndLoad : function(t) {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mProductType/details",
            type: 'GET',
            cache: false,
            dataType:'json',
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: {productTypeId : t},
            success: function (data) {
                if (data.code == 0) {
                    data = data.data;
                    data.isShowText = data.isShow == 1 ? '已上架': '未上架';
                    EditPTModule.loadData(data);
                } else {
                    swal("获取数据失败", data.desc, "error");
                }
            }
        });
    },
    sendData : function() {
        var targetData = EditPTModule.packageData();
        if (undefined == targetData) {
            return;
        }
        $.ajax({
            url: GlobalConfig.serverAddress + "/mProductType/updateOrAdd",
            type: 'POST',
            cache: false,
            dataType:'json',
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: targetData,
            success: function (data) {
                $('#btnCloseAddWin').click();
                $('#btnSaveInfWin').click();
                var title = targetData.productTypeId == undefined ? "新增" : "修改";
                title += " " + targetData.productTypeName + " ";
                if (data.code == 0) {
                    swal(title+"成功", "", "success");
                    PTModule.init();
                } else {
                    swal(title+"失败", data.desc, "error");
                }
            }
        });
    },
    deleteData : function(PTId) {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mProductType/delete",
            type: 'POST',
            cache: false,
            dataType:'json',
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: {
                productTypeId : PTId
            },
            success: function (data) {
                if (data.code == 0) {
                    swal("删除成功", "", "success");
                    PTModule.init();
                } else {
                    swal("删除失败", data.desc, "error");
                }
            }
        });
    }
}
