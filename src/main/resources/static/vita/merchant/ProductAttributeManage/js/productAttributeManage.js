$(function () {
    AttributeTypeModule.init();
    AttributeValueModule.init();
    var list = AttributeTypeModule.attributeTypeBuffer;
    if (undefined != list && list.length > 0) {
        AttributeTypeModule.selectType(list[0].productAttributeTypeId);
    }
});

/**
 * 属性类别模块
 * @type {{init: AttributeTypeModule.init, requestAndLoadData: AttributeTypeModule.requestAndLoadData, loadData: AttributeTypeModule.loadData, createDisplayUnitHTML: (function(*): string)}}
 */
var AttributeTypeModule = {
    attributeTypeBuffer: [],
    currentTypeId: undefined,
    init: function () {
        AttributeTypeModule.requestAndLoadData();
    },
    reloadCurrentData: function () {
        AttributeTypeModule.init();
        AttributeValueModule.init();
        var temp = AttributeTypeModule.currentTypeId
        var list = AttributeTypeModule.attributeTypeBuffer;
        if (undefined == temp) {
            if (undefined != list && list.length > 0) {
                temp = list[0].productAttributeTypeId;
            } else {
                $('#attributeTypeDisplayArea').html('');
                $('#attributeValueDisplayArea').html('');
                return;
            }
        }
        AttributeTypeModule.selectType(temp);
    },
    selectType: function (tId) {
        if (undefined != AttributeTypeModule.currentTypeId) {
            $('#attributeTypeUnit' + AttributeTypeModule.currentTypeId).removeClass('selectStatus');
        }
        AttributeTypeModule.currentTypeId = tId;
        var target = $('#attributeTypeUnit' + tId);
        target.addClass('selectStatus');
        AttributeValueModule.loadTargetTypeValue(tId);
    },
    loadData: function (data) {
        AttributeTypeModule.attributeTypeBuffer = data;
        var target = $('#attributeTypeDisplayArea');
        target.html('');
        for (var i = 0; i < data.length; i++) {
            target.append(AttributeTypeModule.createDisplayUnitHTML(data[i]));
        }
    },
    createDisplayUnitHTML: function (item) {
        var html = '<li class="dd-item " >' +
            '<div onclick="AttributeTypeModule.selectType(' + item.productAttributeTypeId + ')" id="attributeTypeUnit' + item.productAttributeTypeId + '" class="dd-handle">' +
            item.productAttributeTypeName +
            '<span onclick="DeleteAttributeTypeModule.deleteAction(' + item.productAttributeTypeId + ', \'' + item.productAttributeTypeName + '\')" class="label label-danger pull-right"><i class="fa fa-trash"></i></span>' +
            '</div>' +
            '</li>';
        return html;
    },
    requestAndLoadData: function () {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mProductAttribute/attributeTypeList",
            type: 'GET',
            cache: false,
            dataType: 'json',
            async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: null,
            success: function (data) {
                if (data.code != 0) {
                    swal('数据获取失败', data.desc, 'error');
                } else {
                    AttributeTypeModule.loadData(data.data);
                }
            },
            error: function () {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    }
}
/**
 * 属性值模块
 * @type {{init: AttributeValueModule.init, loadTargetTypeValue: AttributeValueModule.loadTargetTypeValue, loadData: AttributeValueModule.loadData, createDisplayUnitHTML: AttributeValueModule.createDisplayUnitHTML}}
 */
var AttributeValueModule = {
    init: function () {
        AttributeValueModule.requestAndLoadData();
    },
    loadTargetTypeValue: function (tTypeId) {
        var list = AttributeTypeModule.attributeTypeBuffer;
        var targetList = [];
        for (var i = 0; i < list.length; i++) {
            var cell = list[i];
            if (cell.productAttributeTypeId == tTypeId) {
                targetList = cell.valueList;
                break;
            }
        }
        var target = $('#attributeValueDisplayArea');
        target.html('');
        for (var i = 0; i < targetList.length; i++) {
            target.append(AttributeValueModule.createDisplayUnitHTML(targetList[i]));
        }
    },
    loadData: function (data) {
        var list = AttributeTypeModule.attributeTypeBuffer;
        for (var i = 0; i < list.length; i++) {
            var cell = list[i];
            cell.valueList = [];
            for (var j = 0; j < data.length; j++) {
                var temp = data[j];
                if (cell.productAttributeTypeId == temp.productAttributeTypeId) {
                    cell.valueList[cell.valueList.length] = temp;
                }
            }
        }
    },
    createDisplayUnitHTML: function (item) {
        var html = '<li class="dd-item" ><div class="dd-handle">' +
            item.productAttributeValueName +
            '<span class="label label-warning pull-right"><i onclick="DeleteAttributeValueModule.deleteAction(' + item.productAttributeValueId + ',\'' + item.productAttributeValueName + '\')" class="fa fa-trash"></i></span></div>' +
            '</li>';
        return html;
    },
    requestAndLoadData: function () {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mProductAttribute/attributeValueList",
            type: 'GET',
            cache: false,
            dataType: 'json',
            async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: null,
            success: function (data) {
                if (data.code != 0) {
                    swal('数据获取失败', data.desc, 'error');
                } else {
                    AttributeValueModule.loadData(data.data);
                }
            },
            error: function () {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    }
}

/**
 * 属性值的修改
 */
var AddAttributeTypeModule = {
    readyToAdd: function () {
        $('#addAttributeTypeBtn').hide();
        $('#attributeTypeNameText').val('');
        $('#addAttributeTypeModule').show();
    },
    cancelAction: function () {
        $('#addAttributeTypeModule').hide();
        $('#addAttributeTypeBtn').show();
    },
    addAction: function () {
        var newAttributeTypeName = $('#attributeTypeNameText').val();
        console.log('新添加的属性类型名为：' + newAttributeTypeName);
        AddAttributeTypeModule.sendData(newAttributeTypeName);
        AddAttributeTypeModule.cancelAction();
    },
    sendData: function (name) {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mProductAttribute/addType",
            type: 'POST',
            cache: false,
            dataType: 'json',
            async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: {
                typeName: name
            },
            success: function (data) {
                if (data.code != 0) {
                    swal('操作失败', data.desc, 'error');
                } else {
                    AttributeTypeModule.reloadCurrentData();
                }
            },
            error: function () {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    }
}
/**
 * 属性值的新增
 */
var AddAttributeValueModule = {
    readyToAdd: function () {
        $('#addAttributeValueBtn').hide();
        $('#attributeValueNameText').val('');
        $('#addAttributeValueModule').show();
    },
    cancelAction: function () {
        $('#addAttributeValueModule').hide();
        $('#addAttributeValueBtn').show();
    },
    addAction: function () {
        var newAttributeValueName = $('#attributeValueNameText').val();
        console.log('新添加的属性类型名为：' + newAttributeValueName);
        AddAttributeValueModule.sendData(newAttributeValueName);
        AddAttributeValueModule.cancelAction();
    },
    sendData: function (name) {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mProductAttribute/addValue",
            type: 'POST',
            cache: false,
            dataType: 'json',
            async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: {
                valueName: name,
                typeId: AttributeTypeModule.currentTypeId
            },
            success: function (data) {
                if (data.code != 0) {
                    swal('操作失败', data.desc, 'error');
                } else {
                    AttributeTypeModule.reloadCurrentData();
                }
            },
            error: function () {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    }
}

var DeleteAttributeTypeModule = {
    readyToDelete: function () {
    },
    deleteAction: function (tId, tName) {
        swal({
                title: "您确定要删除 " + tName + " 吗?",
                text: "删除后将无法恢复，请谨慎操作！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    DeleteAttributeTypeModule.sendData(tId);
                } else {
                    swal("已取消", "您取消了删除操作！", "error");
                }
            });
    },
    sendData: function (id) {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mProductAttribute/deleteType",
            type: 'POST',
            cache: false,
            dataType: 'json',
            async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: {
                typeId: id
            },
            success: function (data) {
                if (data.code != 0) {
                    swal('操作失败', data.desc, 'error');
                } else {
                    if (id == AttributeTypeModule.currentTypeId) {
                        AttributeTypeModule.currentTypeId = undefined;
                    }
                    AttributeTypeModule.reloadCurrentData();
                }
            },
            error: function () {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    }
}

var DeleteAttributeValueModule = {
    readyToDelete: function () {
    },
    deleteAction: function (tId, tName) {
        swal({
                title: "您确定要删除 " + tName + " 吗?",
                text: "删除后将无法恢复，请谨慎操作！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    DeleteAttributeValueModule.sendData(tId);
                } else {
                    swal("已取消", "您取消了删除操作！", "error");
                }
            });
    },
    sendData: function (id) {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mProductAttribute/deleteValue",
            type: 'POST',
            cache: false,
            dataType: 'json',
            async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: {
                valueId: id
            },
            success: function (data) {
                if (data.code != 0) {
                    swal('操作失败', data.desc, 'error');
                } else {
                    AttributeTypeModule.reloadCurrentData();
                }
            },
            error: function () {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    }
}