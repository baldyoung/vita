var OrderModule = {
	orderDisplayAreaId: "#orderDisplayArea",
	currentOrderTypeFlag : 0,
	allItem : 0,
	unfinishedItem : 0,
	finishedItem : 0,
	init : function() {
		var data = OrderModule.requestOrderList();
		OrderModule.loadOrderList(data);
	},
	filterData : function(data) {
		for (var i=0; i<data.length; i++) {
			var order = data[i];
			var itemList = order.itemList;
			for (var j=0; j<itemList.length; j++) {
				var cell = itemList[j];
				if (cell.orderProductItemStatusFlag == 2) {
					OrderModule.unfinishedItem ++;
				} else if (cell.orderProductItemStatusFlag == 3) {
					OrderModule.finishedItem ++;
				}
				if (cell.orderProductItemStatusFlag != -1) {
					OrderModule.allItem ++;
				}
				if (OrderModule.currentOrderTypeFlag == 0) {
					// 所有项
				} else if (cell.orderProductItemStatusFlag != OrderModule.currentOrderTypeFlag) {
					itemList.splice(j, 1);
					j--;
					continue;
				}
			}
			if (itemList.length == 0) {
				data.splice(i, 1);
				i--;
			}
		}
		return data;
	},
	requestOrderList: function() {
		var targetData = [];
		$.ajax({
			url: GlobalConfig.serverAddress + "/order/orderList",
			type: 'GET',
			cache: false,
			dataType: 'json',
			async: false, //设置同步
			contentType: "application/json; charset=utf-8",
			data: null,
			success: function(data) {
				if (data.code == 0) {
					targetData = OrderModule.filterData(data.data);
				} else {
					layer.open({
						content: '获取订单数据失败：'+(undefined == data.desc ? '【无效访问】' : data.desc),
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
	loadOrderList: function(orderList) {
		$('#totalAmount').text(OrderModule.allItem);
		$('#unfinishedAmount').text(OrderModule.unfinishedItem);
		$('#finishedAmount').text(OrderModule.finishedItem);
		var i,
			temp,
			target = $(OrderModule.orderDisplayAreaId);
		target.html('');
		var amount = 0.0;
		for (i = 0; i < orderList.length; i++) {
			temp = orderList[i];
			temp.diningTypeName = temp.orderTypeFlag == 0 ? '堂食' : '打包';
			temp.diningTime = temp.orderPresetTime;
			target.append(OrderModule.createOrderUnitHTML(temp, i + 1));
			amount += temp.amount;
		}
		if (OrderModule.currentOrderTypeFlag == 0) {
			$('#totalOrderAmount').text(amount);
			$('#totalOrderQuantity').text('订单数量：'+orderList.length);
		}
	},
	createOrderUnitHTML: function(orderData, index) {
		var html = '<div class="zjzz-buylist-goods1">';
		html += '<div class="zjzz-buylist-gtime">';
		html += '<span class="zjzz-buylist-gtime1">订单' + index + '</span>';
		html += '<span class="zjzz-buylist-gtime2" style="font-size:13px; padding-top:0px;">就餐时间:' + orderData.diningTime + '</span>';
		html += '<div >';
		html += '<span class=" " style="font-size:14px;">&nbsp;(' + orderData.diningTypeName + ')</span>';
		html += '</div>';
		html += '</div>';
		var amount = 0.0;
		OrderModule.allOrder += orderData.itemList.length;
		for (var i = 0; i < orderData.itemList.length; i++) {
			var temp = orderData.itemList[i];
			amount += temp.orderProductPrice * temp.orderProductQuantity;
			html += OrderModule.createItemUnitHTML(temp);
		}
		orderData.createTime = GlobalMethod.toDateString(orderData.orderCreateDateTime);
		orderData.amount = amount;
		html += '<span class="zjzz-buylist-goodsm">';
		html += '<span class="zjzz-buylist-gtime1" style="color:#787878; font-size:13px; ">' + orderData.createTime + '</span>';
		html += '<i class="zjzz-buylist-gm3">共' + orderData.itemList.length + '件</i>';
		html += '<i>应付总额：<i class="zjzz-buylist-gm4">' + orderData.amount + '</i></i>';
		html += '</span>';
		html += '</div>';
		return html;
	},
	createItemUnitHTML: function(itemData) {
		var html = '<div class="zjzz-buylist-det" ' + (-1 == itemData.orderProductItemStatusFlag ? 'style="background:#e5e8e7;"' : '') + '>';
		html += '<img src="' + GlobalConfig.productImgRelativePath + itemData.orderProductImg + '"/>';
		html += '<div class="zjzz-buylist-gdetail">';
		html += '<span class="zjzz-buylist-gtit1" style=" ">' + itemData.orderProductName + '</span>';
		html += '<span class="zjzz-buylist-gmoney">';
		html += '<i class="zjzz-buylist-gm1">' + itemData.orderProductPrice + '</i>';
		html += '<i class="zjzz-buylist-gm2">x' + itemData.orderProductQuantity + '</i>';
		html += '</span>';
		if (!GlobalMethod.isEmpty(itemData.orderProductRemarks)) {
			html += '<span class="zjzz-buylist-gtit1" style="margin-top:5px; font-size:12px; " >口味：<i style="height:20px; background: #' + '' + '; border-radius: 5px; padding:0 5px 0 5px;">' + itemData.orderProductRemarks + '</i></span>';
		}
		html += '</div>';
		if (!GlobalMethod.isEmpty(itemData.orderProductItemStatusDesc)) {
			html += '<span class="zjzz-buylist-gtit1" style="font-size:10px; width:100%; margin-top:0px; color:#ff9c8f;" >' + itemData.orderProductItemStatusDesc + '</span>';
		}
		html += '</div>';
		return html;
	}
}

$(function() {
	OrderModule.init();
});

var test_orderList = [{
			orderId: "k00101",
			diningTypeName: "堂食",
			diningTime: "现在",
			itemNumber: "10",
			orderAmount: "300",
			discount: "0.8",
			receivableAmount: "240",
			createTime: "2020-02-05 12:33:32",
			itemList: [{
				itemId: "1001",
				productName: "赣南脐橙",
				productPrice: "30.00",
				productPicture: "3.png",
				productQuantity: "1",
				labelValueColor: "009F95",
				labelValueName: "正常",
				isValid: true
			}, {
				itemId: "1002",
				productName: "赣北苹果",
				productPrice: "25.00",
				productPicture: "1.png",
				productQuantity: "2",
				labelValueColor: "009F95",
				labelValueName: "正常",
				isValid: true
			}, {
				itemId: "1003",
				productName: "失效商品测试",
				productPrice: "888.00",
				productPicture: "6.png",
				productQuantity: "1",
				labelValueColor: "009F95",
				labelValueName: "正常",
				invalidMsg: "已替换为红烧鱼",
				isValid: false
			}, {
				itemId: "1004",
				productName: "赣北苹果",
				productPrice: "25.00",
				productPicture: "1.png",
				productQuantity: "1",
				isValid: true
			}]
		}, {
			orderId: "k00101",
			diningTypeName: "堂食",
			diningTime: "现在",
			itemNumber: "10",
			orderAmount: "300",
			discount: "0.8",
			receivableAmount: "240",
			createTime: "2020-02-05 12:33:32",
			itemList: [{
				itemId: "1001",
				productName: "赣南脐橙",
				productPrice: "30.00",
				productPicture: "3.png",
				productQuantity: "1",
				labelValueColor: "009F95",
				labelValueName: "正常",
				isValid: true
			}, {
				itemId: "1002",
				productName: "赣北苹果",
				productPrice: "25.00",
				productPicture: "1.png",
				productQuantity: "2",
				labelValueColor: "009F95",
				labelValueName: "正常",
				isValid: true
			}, {
				itemId: "1003",
				productName: "失效商品测试",
				productPrice: "888.00",
				productPicture: "6.png",
				productQuantity: "1",
				labelValueColor: "009F95",
				labelValueName: "正常",
				invalidMsg: "已替换为红烧鱼",
				isValid: false
			}, {
				itemId: "1004",
				productName: "赣北苹果",
				productPrice: "25.00",
				productPicture: "1.png",
				productQuantity: "1",
				isValid: true
			}]}];