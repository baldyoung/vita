// 公用方法组件

// --- 浏览器全屏调用
var FullScreenModule = {
	// 打开全屏
	openFullScreen: function() {
		var ele = document.documentElement;
		var temp = ele.requestFullScreen || ele.webkitRequestFullScreen || ele.mozRequestFullScreen || ele.msRequestFullscreen;
		if (undefined != temp) {
			temp.call(ele);
		}
	},
	// 关闭全屏
	closeFullScreen: function() {
		if (document.exitFullscreen) {
			document.exitFullscreen();
		} else if (document.mozCancelFullScreen) {
			document.mozCancelFullScreen();
		} else if (document.webkitCancelFullScreen) {
			document.webkitCancelFullScreen();
		} else if (document.msExitFullscreen) {
			document.msExitFullscreen();
		} else if (undefined != window.ActiveXObject) {
			var wscript = new ActiveXObject("WScript.Shell");
			if (wscript != null) {
				wscript.SendKeys("{F11}");
			}
		}
	}
}
