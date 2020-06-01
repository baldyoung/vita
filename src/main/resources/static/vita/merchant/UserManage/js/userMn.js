

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
        console.log(data);
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





/************** ***************/

var url = "/Vita_war_exploded"

var currentUserId=undefined;


//加载用户数据表格
function  loadUserTable(t){

}
//请求用户数据，并加载到页面
function requestUserList(){
    $.ajax({
        url: url + "/Vita_Back/getTargetAllUser",
        type: 'POST',
        cache: false,
        //dataType:'json',
        dataType: 'text',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({}),
        //data:temp,
        //processData: false,
        //contentType: false,
        success: function (data) {
            //将json字符串转换为json对象
            data = JSON.parse(data);
            //console.log(data);
            if (data.result == 'succeed') {//获取成功
                var temp = JSON.parse(data.inf); //将inf字段转换为json对象
                loadUserTable(temp)//将获取到的数据交给指定函数进行加载，显示在页面上

            } else if (data.result == 'default') { //获取失败
            }
        }
    });
}
//删除指定用户
function deleteUser(t){
	swal({
			title: "您确定要删除吗？",
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
			if (!isConfirm) {
				return;
			} 
		});
    $.ajax({
        url: url + "/Vita_Back/deleteTargetUser",
        type: 'POST',
        cache: false,
        //dataType:'json',
        dataType: 'text',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({id:t}),
        //data:temp,
        //processData: false,
        //contentType: false,
        success: function (data) {
            //将json字符串转换为json对象
            data = JSON.parse(data);
            //console.log(data);
            if (data.result == 'succeed') {//获取成功
                requestUserList();
            } else if (data.result == 'default') { //获取失败
            }
        }
    });
}
//加载用户的详细详细
function loadUserInf(t){
    currentUserId=t.id;
    $('#nameText').val(t.name);
    $('#accountText').val(t.account);
    $('#passwordText').val(t.password);
    $('#gradeText').val(t.grade);
}
//重置用户详细窗口
function resetUserInf(){
    $('#nameText').val('');
    $('#accountText').val('');
    $('#passwordText').val('');
    $('#gradeText').val(3);
}
//加载指定用户的信息
function openUserInfWin(tid, tname, taccount, tpwd, tgrade){
    var temp = {
        id:tid,
        name:tname,
        account:taccount,
        password:tpwd,
        grade:(tgrade<'7'?'3':'7')
    }
    $('#myModal_title').text('用户信息-修改')
    loadUserInf(temp);
}
//准备添加新用户
function readForAddUser(){
    currentUserId=undefined;
    $('#myModal_title').text('用户信息-新增')
    resetUserInf();
}
//保存用户信息
function saveUserInf(){
    var temp = {
        name: $('#nameText').val(),
        account: $('#accountText').val(),
        password: $('#passwordText').val(),
        grade: $('#gradeText').val()
    }
    if(currentUserId!=undefined)
        temp.id=currentUserId;
    var tip = check(temp);
    if(true!=tip){//格式不正确
        showTip(tip);
    }else{
        postUserInf(temp)
    }
}
function postUserInf(t){
    $.ajax({
        url: url + "/Vita_Back/saveOrUpdateTargetUser",
        type: 'POST',
        cache: false,
        //dataType:'json',
        dataType: 'text',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(t),
        //data:temp,
        //processData: false,
        //contentType: false,
        success: function (data) {
            //将json字符串转换为json对象
            data = JSON.parse(data);
            //console.log(data);
            if (data.result == 'succeed') {//获取成功
                requestUserList();
                swal("操作成功", "");
				$('#btnCloseInfWin').trigger('click');
            } else if (data.result == 'default') { //获取失败
                swal("操作失败", data.inf);
            }
        }
    });
}
//检查指定的json对象的格式是否正确
function check(t){
    if(undefined==t || undefined==t.name || undefined==t.account || undefined==t.password || undefined==t.grade) return undefined;
    if(t.name.length>=50 ) return '用户名称需要小于50个字符';
    if(t.account.length>=20) return '登录名需要小于20个字符';
    if(t.password.length>=20) return '登录密码需要小于20个字符';
    if(t.name=='' || t.account=='' || t.password=='') return '信息不能留空';
    return true;
}
//提示信息
function showTip(t){
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "progressBar": true,
        "positionClass": "toast-top-center",
        "onclick": null,
        "showDuration": "400",
        "hideDuration": "1000",
        "timeOut": "7000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
    toastr.warning(''+t);

}

function testFun(){
    var temp = [{
        userId : "10010",
        userName : "张总",
        userAccount : "zhang",
        userGradeName : "管理员"
    },{
        userId : "10011",
        userName : "李总",
        userAccount : "li",
        userGradeName : "管理员"
    },{
        userId : "10012",
        userName : "刘阿姨",
        userAccount : "liu",
        userGradeName : "普通账户"
    }]
    loadUserTable(temp);
}