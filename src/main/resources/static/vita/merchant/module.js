
function requetsAndLoadData () {
    $.ajax({
        url: GlobalConfig.serverAddress + "",
        type: 'GET',
        cache: false,
        dataType: 'json',
        //async: false, //设置同步
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        data: filterData,
        success: function(data) {
            if (data.code != 0) {
                swal('操作失败', data.desc, 'error');
            } else {

            }
        },
        error: function() {
            swal('服务器连接失败', '请检查网络是否通畅', 'warning');
        }
    });
}

function requestTargetData () {
    var targetData = undefined;
    $.ajax({
        url: GlobalConfig.serverAddress + "",
        type: 'GET',
        cache: false,
        dataType: 'json',
        async: false, //设置同步
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        data: filterData,
        success: function(data) {
            if (data.code != 0) {
                swal('获取数据失败', data.desc, 'error');
            } else {
                targetData = data.data;
            }
        },
        error: function() {
            swal('服务器连接失败', '请检查网络是否通畅', 'warning');
        }
    });
    return targetData;
}

function deleteAction(productName) {
    swal({
            title: "您确定要删除 " + productName + " 吗?",
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

            } else {
                swal("已取消", "您取消了删除操作！", "error");
            }
        });
}