

$(document).ready(function(){

    MerchantUserModule.init();
});


var MerchantUserModule = {
    userBuffer : [],
    init : function () {
        MerchantUserModule.requestAndLoadData();
    },
    getUserInfo : function(userId) {
        var list = MerchantUserModule.userBuffer;
        for (var i=0; i<list.length; i++) {
            if (list[i].merchantUserId == userId) {
                return list[i];
            }
        }
        return undefined;
    },
    loadData : function (data) {
        MerchantUserModule.userBuffer = data;
        var dataTable = $('#userTable');
        if ($.fn.dataTable.isDataTable(dataTable)) {
            dataTable.DataTable().destroy();
        }
        for(var i=0;i<data.length;i++){
            if(data[i].merchantUserGrade >= 3) data[i].userGradeName='管理员';
            else data[i].userGradeName='普通'
        }
        dataTable.DataTable({
            'searching':true, //去掉搜索框
            'bLengthChange': true, //去掉每页显示多少条数据方法
            "serverSide": false, //关闭分页操作，默认就是关闭
            "autoWidth" : false, //
            "bSort": false, //打开排序功能
            //"order": [[ 0, "desc" ]], //默认排序的指标
            'data':data, //表格的数据源
            'columns':[{
                data:'merchantUserName'
            },{
                data:'merchantUserAccount'
            },{
                data:'userGradeName'
            }],
            "columnDefs": [{
                "render" : function(data, type, row) {
                    var a = "";
                    a+="<button class=\"btn btn-primary \" style='margin-top:3px;' type=\"button\" onclick=\"DeleteMerchantUserModule.deleteAction('"+row.merchantUserId+"', '"+row.merchantUserName+"')\"><i class=\"fa fa-building-o\"></i>删除"
                    a+="</button>"
                    a+="<button class=\"btn btn-primary \" style='margin-top:3px; margin-left:5px; ' type=\"button\" data-toggle=\"modal\" data-target=\"#alertOrAddUserInfoPanel\" onclick=\"updateOrAddMerchantUserModule.loadTargetUserInfo('"+row.merchantUserId+"')\"><i class=\"fa fa-building-o\"></i>修改"
                    a+="</button>"
                    return a;
                },
                "targets" :3
            }]
        })
    },
    requestAndLoadData : function () {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mUser/merchantUserList",
            type: 'GET',
            cache: false,
            dataType: 'json',
            //async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: null,
            success: function(data) {
                if (data.code != 0) {
                    swal('获取数据失败', data.desc, 'error');
                } else {
                    MerchantUserModule.loadData(data.data);
                }
            },
            error: function() {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    }
}

var updateOrAddMerchantUserModule = {
    currentUserId : undefined,
    readyToAdd : function() {
        updateOrAddMerchantUserModule.currentUserId = undefined;
        $('#myModal_title').val("账号新增");
        $('#nameText').val('');
        $('#accountText').val('');
        $('#passwordText').val('');
        $('#gradeText').val(-1);
    },
    loadTargetUserInfo : function (userId) {
        updateOrAddMerchantUserModule.currentUserId = userId;
        var userInfo = MerchantUserModule.getUserInfo(userId);
        $('#myModal_title').val("账号修改");
        $('#nameText').val(userInfo.merchantUserName);
        $('#accountText').val(userInfo.merchantUserAccount);
        $('#passwordText').val('');
        $('#gradeText').val(userInfo.merchantUserGrade);
    },
    packageData : function() {
        var data = {
            userName : $('#nameText').val(),
            userAccount : $('#accountText').val(),
            userGrade : $('#gradeText').val(),
            userId : updateOrAddMerchantUserModule.currentUserId
        }
        var up = $('#passwordText').val();
        if (!GlobalMethod.isEmpty(up)) {
            data.userPassword = up;
        }
        if (GlobalMethod.isEmpty(data.userName)) {
            data.userName = '';
        }
        return data;
    },
    sendAction : function() {
        var data = updateOrAddMerchantUserModule.packageData();
        if (data.userGrade == undefined || -1 == data.userGrade) {
            swal('请补全信息', '账号等级不能为空', 'error');
            return;
        }
        updateOrAddMerchantUserModule.sendData(data);
    },
    sendData : function(data) {
        var address = "/mUser/updateMerchantUser";
        if (data.userId == undefined) {
            if (GlobalMethod.isEmpty(data.userAccount)) {
                swal('请补全信息', '账号登录名不能为空', 'error');
                return;
            }
            if (GlobalMethod.isEmpty(data.userPassword)) {
                swal('请补全信息', '密码不能为空', 'error');
                return;
            }
            address = "/mUser/addMerchantUser";
        }
        $.ajax({
            url: GlobalConfig.serverAddress + address,
            type: 'POST',
            cache: false,
            dataType: 'json',
            //async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: data,
            success: function(data) {
                if (data.code != 0) {
                    swal('操作失败', data.desc, 'error');
                } else {
                    MerchantUserModule.init();
                    swal('保存成功', '', 'success');
                    $('#btnCloseInfWin').trigger('click');

                }
            },
            error: function() {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    }
}

var DeleteMerchantUserModule = {
    deleteAction : function(userId, userName) {
        swal({
                title: "您确定要删除 " + userName + " 吗?",
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
                    DeleteMerchantUserModule.sendData(userId);
                } else {
                    swal("已取消", "您取消了删除操作！", "error");
                }
            });
    },
    sendData : function(tId) {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mUser/deleteMerchantUser",
            type: 'POST',
            cache: false,
            dataType: 'json',
            //async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: {
                userId : tId
            },
            success: function(data) {
                if (data.code != 0) {
                    swal('删除失败', data.desc, 'error');
                } else {
                    MerchantUserModule.init();
                }
            },
            error: function() {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    }
}



