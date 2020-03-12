var OrderModule = {
	orderDisplayAreaId: "#orderDisplayArea",
	requestOrderList: function() {
		OrderModule.loadOrderList(test_orderList);
	},
	loadOrderList: function(orderList) {
		var i,
			temp,
			target = $(OrderModule.orderDisplayAreaId);
		target.html('');
		for (i = 0; i < orderList.length; i++) {
			temp = orderList[i];
			target.append(OrderModule.createOrderUnitHTML(temp, i + 1));
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
		for (var i = 0; i < orderData.itemList.length; i++) {
			var temp = orderData.itemList[i];
			html += OrderModule.createItemUnitHTML(temp);
		}
		
		html += '<span class="zjzz-buylist-goodsm">';
		html += '<span class="zjzz-buylist-gtime1" style="color:#787878; font-size:13px; ">' + orderData.createTime + '</span>';
		html += '<i class="zjzz-buylist-gm3">共' + orderData.itemNumber + '件</i>';
		html += '<i>应付总额：<i class="zjzz-buylist-gm4">' + orderData.receivableAmount + '</i></i>';
		html += '</span>';
		html += '</div>';
		return html;
	},
	createItemUnitHTML: function(itemData) {
		var html = '<div class="zjzz-buylist-det" ' + (undefined != itemData.invalidMsg ? 'style="background:#e5e8e7;"' : '') + '>';
		html += '<img src="' + GlobalConfig.productImgRelativePath + itemData.productPicture + '"/>';
		html += '<div class="zjzz-buylist-gdetail">';
		html += '<span class="zjzz-buylist-gtit1" style=" ">' + itemData.productName + '</span>';
		html += '<span class="zjzz-buylist-gmoney">';
		html += '<i class="zjzz-buylist-gm1">' + itemData.productPrice + '</i>';
		html += '<i class="zjzz-buylist-gm2">x' + itemData.productQuantity + '</i>';
		html += '</span>';
		if (undefined != itemData.labelValueName) {
			html += '<span class="zjzz-buylist-gtit1" style="margin-top:5px; font-size:12px; " >口味：<i style="height:20px; background: #' + itemData.labelValueColor + '; border-radius: 5px; padding:0 5px 0 5px;">' + itemData.labelValueName + '</i></span>';
		}
		html += '</div>';
		if (undefined != itemData.invalidMsg) {
			html += '<span class="zjzz-buylist-gtit1" style="font-size:10px; width:100%; margin-top:0px; color:#ff9c8f;" >' + itemData.invalidMsg + '</span>';
		}
		html += '</div>';
		return html;
	}
}

$(function() {
	OrderModule.requestOrderList();
	console.log(test_orderList);
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