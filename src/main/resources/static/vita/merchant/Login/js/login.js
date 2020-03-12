/**
 * 
 */
var url = "/Vita_war_exploded"

function login(){
	var temp = {
			account:$('#userAccount').val(),
			pwd:$('#userPassword').val()
	}
	if(temp.account=='' || temp.pwd==''){
	    alert("内容不能留空");
	    return;
    }
	//console.log(temp);
	$.ajax({
        url: url+"/Vita_Back/login",
        type: 'POST',
        cache: false,
        //dataType:'json',
        dataType:'text',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(temp),
        //data:temp,
        //processData: false,
        //contentType: false,
        beforeSend: function () {
        },
        success: function (data) {
        	//将json字符串转换为json对象
        	data = JSON.parse(data);
        	if(data.result=='succeed') {
        	    location.href=url+"/Vita_Back/BackHomePage.html"
            }
        	else if(data.result=='default') alert("登录失败\n"+data.inf);
        	console.log(data);
        	
        	//window.location.href='../../index.html'
        },
        error: function(data){
        	
        	console.log("登录失败\n浏览器异常")
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