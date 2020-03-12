

//浏览器全屏相关方法

//打开全屏
function openFullScreen(){
	var ele = document.documentElement;
	var temp = ele.requestFullScreen || ele.webkitRequestFullScreen || ele.mozRequestFullScreen || ele.msRequestFullscreen;
	if(undefined != temp ){
		temp.call(ele);
	}
}

//关闭全屏
function closeFullScreen(){
	if(document.exitFullscreen){
		document.exitFullscreen();
	}else if(document.mozCancelFullScreen){
		document.mozCancelFullScreen();
	}else if(document.webkitCancelFullScreen){
		document.webkitCancelFullScreen();
	}else if(document.msExitFullscreen){
		document.msExitFullscreen();
	}else if(undefined != window.ActiveXObject){
		var wscript = new ActiveXObject("WScript.Shell");
		if(wscript != null)
			wscript.SendKeys("{F11}");
	}
}
	