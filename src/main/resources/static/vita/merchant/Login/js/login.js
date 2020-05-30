/**
 * 
 */
function forgetPWD() {
    swal("请联系客服或工作人员", "", "success");
}

function login(){
	var temp = {
			a:$('#userAccount').val(),
			p:$('#userPassword').val()
	}
	if(GlobalMethod.isEmpty(temp.a) || GlobalMethod.isEmpty(temp.p)){
        swal('登录失败', "账号名和密码不能为空", 'error');
	    return;
    }
    $.ajax({
        url: GlobalConfig.serverAddress + "/mUser/login",
        type: 'POST',
        cache: false,
        dataType: 'json',
        //async: false, //设置同步
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        data: temp,
        success: function(data) {
            if (data.code != 0) {
                swal('登录失败', data.desc, 'error');
            } else {
                GlobalMethod.replaceURL(data.data);
            }
        },
        error: function() {
            swal('服务器连接失败', '请检查网络是否通畅', 'warning');
        }
    });
}

//监听回车键
/*$("object").bind("keydown",function(e){
    var key = e.which;
    if(key == 13){
        login();
    }
});*/

$(document).keyup(function(event){
    if(event.keyCode ==13){
        login();
    }
});