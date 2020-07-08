var ServiceModule = {
	displayAreaId: 'serviceDisplayArea',
	serviceData: [],
	informMerchantStatus: false,
	timerId: undefined,
	init : function() {
		var data = ServiceModule.requestData();
		ServiceModule.serviceData = data;
		ServiceModule.loadData(data);
	},
	getServiceItem: function(serviceId) {
		var i, temp;
		for (i = 0; i < ServiceModule.serviceData.length; i++) {
			temp = ServiceModule.serviceData[i];
			if (temp.customerMessageTypeId == serviceId) {
				return temp;
			}
		}
		return undefined;
	},
	requestData: function() { // 获取商家下的服务信息
		var targetData = [];
		$.ajax({
			url: GlobalConfig.serverAddress + "/system/messageType",
			type: 'GET',
			cache: false,
			dataType: 'json',
			async: false, //设置同步
			contentType: "application/json; charset=utf-8",
			data: null,
			success: function(data) {
				if (data.code == 0) {
					targetData = data.data
				} else {
					layer.open({
						content: '获取系统消息数据失败：'+(undefined == data.desc ? '【无效访问】' : data.desc),
						skin: 'msg',
						time: 2 //3秒后自动关闭
					});
				}
			},
			error: function() {
				layer.open({
					content: '连接服务器失败，请检查网络是否通畅！',
					skin: 'msg',
					time: 3 //3秒后自动关闭
				});
			}
		});
		return targetData;
	},
	postServiceMsg: function(serviceId) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/system/sendMessage",
			type: 'POST',
			cache: false,
			dataType: 'json',
			async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				messageTypeId : serviceId
			},
			success: function(data) {
				if (data.code == 0) {
					layer.open({
						content: '您的请求已送达商家！',
						skin: 'msg',
						time: 2 //2秒后自动关闭
					});
				} else {
					layer.open({
						content: '消息发送失败：'+data.desc,
						skin: 'msg',
						time: 2 //3秒后自动关闭
					});
				}
			},
			error: function() {
				layer.open({
					content: '连接服务器失败，请检查网络是否通畅！',
					skin: 'msg',
					time: 3 //3秒后自动关闭
				});
			}
		});
		// console.log("发送给商家" + serviceId + "服务信息");
	},
	cancelServiceRequest: function() {
		// 取消当前桌的服务请求
		// 同步请求

		// console.log("已取消当前的服务请求");
	},
	requestServiceResult: function() { // 请求当前服务的结果
		if (testTimes-- > 0) {
			console.log("轮询获取请求反馈...");
			return;
		}
		console.log("商家已经确定");	
		ServiceModule.informMerchantStatus = true;
		layer.closeAll();
		layer.open({
			content: '商家已经确定',
			skin: 'msg',
			time: 3,
			end: function() {
			}
		});
	},
	loadData: function(data) {
		var serviceData = data;
		var target = $('#' + ServiceModule.displayAreaId);
		target.html('');
		var i,
			serviceItem,
			html;
		for (i = 0; i < serviceData.length; i++) {
			serviceItem = serviceData[i];
			serviceItem.serviceTypeId = 1;
			html = '<span class="personal-box5-text2" onclick="ServiceModule.';
			if (2 == serviceItem.serviceTypeId) {
				html += 'informMerchant(' + serviceItem.customerMessageTypeId;
			} else if (1 == serviceItem.serviceTypeId) {
				html += 'sendMsgToMerchant(' + serviceItem.customerMessageTypeId;
			}
			html += ')">' + serviceItem.customerMessageTypeName + '</span>'
			target.append(html);
		}
	},
	informMerchant: function(serviceId) { // 需要等待商家回应的消息类型
		var service = ServiceModule.getServiceItem(serviceId);
		layer.open({
			content: '您确定要' + service.serviceName + '吗？',
			btn: ['确定', '取消'],
			yes: function(index) {
				ServiceModule.informMerchantStatus = false;
				ServiceModule.timerId = setInterval('ServiceModule.requestServiceResult()', 3000);
				layer.open({
					type: 2,
					content: '正在' + service.serviceName + '...',
					end: function() {
						clearInterval(ServiceModule.timerId);
						if (ServiceModule.informMerchantStatus == false) {
							layer.closeAll();
							layer.open({
								content: '已取消请求',
								skin: 'msg',
								time: 2,
								end: function() {
									
								}
							});
						}
						ServiceModule.informMerchantStatus = false;
					}
				});
			}
		});
	},
	sendMsgToMerchant: function(serviceId) { // 只是通知商家，并不需要得到回复的消息类型
		var service = ServiceModule.getServiceItem(serviceId);
		layer.open({
			content: '您确定要' + service.customerMessageTypeName + '吗？',
			btn: ['确定', '取消'],
			yes: function(index) {
				ServiceModule.postServiceMsg(serviceId);
			}
		});
	}
};


var DiningRoomModule = {

	init : function () {
		var data = DiningRoomModule.requestData();
		DiningRoomModule.loadData(data);
	},
	requestData : function () {
		var targetData = [];
		$.ajax({
			url: GlobalConfig.serverAddress + "/order/diningRoomInfo",
			type: 'GET',
			cache: false,
			dataType: 'json',
			async: false, //设置同步
			contentType: "application/json; charset=utf-8",
			data: null,
			success: function(data) {
				if (data.code == 0) {
					targetData = data.data
				} else {
					if (undefined == data.code) {
						GlobalMethod.replaceURL("../3_system/invalidPage.html")
					}
					layer.open({
						content: '获取就餐位数据失败：'+data.desc,
						skin: 'msg',
						time: 2 //3秒后自动关闭
					});
				}
			},
			error: function() {
				layer.open({
					content: '连接服务器失败，请检查网络是否通畅！',
					skin: 'msg',
					time: 3 //3秒后自动关闭
				});
			}
		});
		return targetData;
	},
	loadData : function (data) {
		if (undefined == data.allItem) {
			data.allItem = '';
		}
		if (undefined == data.finishItem) {
			data.finishItem = '';
		}
		if (undefined == data.unfinishItem) {
			data.unfinishItem = '';
		}
		$('#diningRoomText').text(data.diningRoomName);
		$('#allItemText').text(data.allItem);
		$('#unfinishItemText').text(data.unfinishItem);
		$('#finishItemText').text(data.finishItem);


	}
}

var MessageModule = {

}


var testTimes = 1;

$(function() {
	DiningRoomModule.init();
	ServiceModule.init();
})