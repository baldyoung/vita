var GlobalConfig = {
	shopInfo: {
		shopName: 'vita门店',
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
	toDateString : function(date) {
		var d = new Date(date);
		var dateString=d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate() + ' ' + d.getHours() + ':' + d.getMinutes() + ':' + d.getSeconds();
		return dateString;
	}

}

/**
 * 按商品等级排序商品集
 * @param list
 * @returns {Array}
 */
function sortProductList(list) {
	var data = [], k = 0;
	while (list.length > 0) {
		var t = 0, cell = list[t];
		for (var i=t+1; i<list.length; i++) {
			var item = list[i];
			if (cell.productGrade < item.productGrade) {
				t = i;
				cell = item;
			}
		}
		data[k++] = cell;
		list.splice(t, 1);
	}
	return data;
}

/**
 * 按品类等级排序品类集合
 * @param list
 * @returns {Array}
 */
function sortProductTypeList(list) {
	var data = [], k = 0;
	while (list.length > 0) {
		var t = 0, cell = list[t];
		for (var i=t+1; i<list.length; i++) {
			var item = list[i];
			if (cell.productTypeGrade < item.productTypeGrade) {
				t = i;
				cell = item;
			}
		}
		data[k++] = cell;
		list.splice(t, 1);
	}
	return data;
}

function  toDiningTypeName(diningTypeId) {
	if (0 == diningTypeId) {
		return "堂食";
	}
	if (1 == diningTypeId) {
		return "打包";
	}
	return "";
}
function toOrderItemStatusName(statusId) {
	var temp = '';
	switch(statusId) {
		case 0 : temp = '待确定'; break;
		case 1 : temp = '缺货中'; break
		case 2 : temp = '备货中'; break
		case 3 : temp = '已完成'; break
		case 4 : temp = '已删除'; break
	}
	return temp;
}

function toOrderItemStatusNameStyle(statusId) {
	var html = '';
	var temp = '';
	switch(statusId) {
		case 0 : temp = '<span style="background:#FFCCCC; color:#000000;">待确定</span>'; break;
		case 1 : temp = '<span style="background:#FFFF66; color:#000000;">缺货中</span>'; break
		case 2 : temp = '<span style="background:#99CCFF; color:#000000;">备货中</span>'; break
		case 3 : temp = '<span style="background:#99CC99; color:#000000;">已完成</span>'; break
		case 4 : temp = '<span style="background:#000000; color:#FFFFFF;">已删除</span>'; break
	}
	html += temp;
	html += "";
	return html;
}


