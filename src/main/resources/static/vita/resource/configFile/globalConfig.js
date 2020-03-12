var GlobalConfig = {
	shopInfo: {
		shopName: '天天农庄',
		shopLogo: ''
	},
	productImgRelativePath: "/vita/resource/productImg/",
	currentModel: "test", // dev、test
	//currentModel: "dev", // dev、test
	serverAddress: "",
	currentServiceType: 'take-out' // take-out为外卖类型
};

var GlobalMethod = {
	isEmpty: function(str) {
		if (str == null) return true;
		return str.replace(/\s/g, '').length == 0;
	},
	goBack: function() { // 返回上一页面
		history.back();
	},
	redirectURL: function(url) { // 跳转到指定URL，可以通过浏览器back回到上一页面
		window.location.href = url;
	},
	replaceURL: function(url) { // 替换当前URL，其导致浏览器的back无效
		window.location.replace(url);
	},
	isPC: function() { // 判断当前浏览器的的状态是否为PC端
		var userAgent = navigator.userAgent.toLowerCase();
		var temp = userAgent.match(/mobile/i);
		if (userAgent.match(/mobile/i) == "mobile") {
			return false;
		}
		if (userAgent.match(/android/i) == "android") {
			return false;
		}
		if (userAgent.match(/iphone/i)) {
			return false;
		}
		return true;
	},
	getArgsFromLocationHref: function(name) { // 获取当前URL中的指定参数值，没有则返回undefined
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
		var r = window.location.search.substr(1).match(reg);
		if (r != undefined) return unescape(r[2]);
		return undefined;
	},
	getAgeByDateTime: function(datetime) { // 获取格式为YYYY*MM*DD形式的时间到当前年份的差值
		var currentDate = new Date();
		var currentYear = currentDate.getFullYear();
		var birthYear = datetime.substr(0, 4);
		var age = currentYear - parseInt(birthYear) + 1;
		return age;
	},
	requestLocation: function(successRun, defaultRun) {
		if (typeof successRun != "function") {
			console.warn("parameter 'successRun' is not a function !");
		}
		if (typeof defaultRun != "function") {
			console.warn("parameter 'defaultRun' is not a function ! ")
		}
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(
				function(position) {
					var longitude = position.coords.longitude;
					var latitude = position.coords.latitude;
					console.log(longitude)
					console.log(latitude)
					if (typeof successRun == "function") {
						successRun(longitude, latitude);
					}
				},
				function(e) {
					var msg = e.code;
					var dd = e.message;
					console.log(msg)
					console.log(dd)
					if (typeof defaultRun == "function") {
						defaultRun(msg, dd);
					}
				}
			)
		}
	},
	base64ToImg : function(dataurl, filename) {

		var arr = dataurl.split(',')
		var mime = arr[0].match(/:(.*?);/)[1]
		var suffix = mime.split('/')[1]
		var bstr = atob(arr[1])
		var n = bstr.length
		var u8arr = new Uint8Array(n)
		while (n--) {
			u8arr[n] = bstr.charCodeAt(n)
		}
		return new File([u8arr], `${filename}.${suffix}`, {
			type: mime
		})
	}
}