
var url = "/Vita_war_exploded"

var mainIframe = window.document.getElementById("J_iframe"); 
var ACI = document.getElementById('AudioActionIcon');
var PlayAudioState = true;

var isFullScreen = false; //记录当前是否为全屏


//修改是否播放声音的参数
function changeAudioState(){
	//console.log('changeAudioState:');
			
	PlayAudioState = PlayAudioState == false ? true : false  ;
	ACI.className = PlayAudioState == false ? "glyphicon glyphicon-volume-off" : "glyphicon glyphicon-volume-up";
	mainIframe.contentWindow.updatePlayAudioState();
}
//改变全屏状态
function changeFullScreenState(){
	isFullScreen = isFullScreen == false ? true : false;
	if(true == isFullScreen) openFullScreen();
	else closeFullScreen();
}
$(document).ready(function(){
	
		
		
	
});


//用户登出操作
function logoutUser(){
    $.ajax({
        url: url+"/logout",
        type: 'POST',
        cache: false,
        //dataType:'json',
        dataType:'text',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({}),
        //data:temp,
        //processData: false,
        //contentType: false,
        beforeSend: function () {
        },
        success: function (data) {
            //将json字符串转换为json对象
            location.reload();
            return;
            data = JSON.parse(data);
            if(data.result=='succeed'){
                //alert("退出成功");
                window.href=url+"/Vita_Back/Login/login.html"
            }
            else if(data.result=='default'){
                //alert("退出失败\n"+data.inf);
                location.reload();
            }
            //console.log(data);
            //window.location.href='../../index.html'
        },
        error: function(data){

        }

    });
}



//用户修改密码操作
function changePWD(){
    var temp ={
        oldPWD:$('#oldPWD').val(),
        newPWD:$('#newPWD').val()
    }
    if(temp.newPWD=='' || temp.oldPWD==''){
        swal("请勿留空", "");
        return;
    }
    $.ajax({
        url: url+"/Vita_Back/changePWD",
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
            if(data.result=='succeed'){
                swal("修改成功", "密码已变更", "success");
            }
            else if(data.result=='default'){
                swal("修改失败", "密码未变更", "error");
            }
        },
        error: function(data){
        }
    });
    $('#oldPWD').val(''),
    $('#newPWD').val('')
}