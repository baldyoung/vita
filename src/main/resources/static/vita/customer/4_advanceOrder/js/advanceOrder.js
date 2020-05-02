$(function() {
	init();
});

function init() {
	HeartBeatModule.init();

	// 数据处理
	var i, j;
	var typeList = ProductTypeModule.productTypeBuffer;
	var productList = ProductModule.productBuffer;
	for (i=0; i<typeList.length; i++) {
		var type = typeList[i];
		type.productList = [];
		var list = type.productList;
		for (j=0; j<productList.length; j++) {
			var product = productList[j];
			if (product.valid == undefined) {
				product.valid = true;
			}
			if (product.productTypeId == type.productTypeId) {
				list[list.length] = product;
			}
		}
	}
	AdvanceOrderModule.requestAdvanceOrderData(typeList);
}

/**
 * 心跳模块
 * @type {{init: HeartBeatModule.init, sendData: HeartBeatModule.sendData}}
 */
var HeartBeatModule = {
	init : function () {
		setInterval('HeartBeatModule.sendData()', 1000);
	},
	sendData : function () {
		$.ajax({
			url: GlobalConfig.serverAddress + "/shoppingCart/heartBeat",
			type: 'GET',
			cache: false,
			dataType: 'json',
			//async: false, //设置同步
			contentType: "application/json; charset=utf-8",
			data: null,
			success: function(data) {
				if (data.code == 0) {
					targetData = data.data;
				} else {
					layer.open({
						content: 'heart beat defeat:'+data.desc,
						skin: 'msg',
						time: 1 //3秒后自动关闭
					});
				}
			},
			error: function() {
				layer.open({
					content: '连接服务器失败，请检查网络是否通畅！',
					skin: 'msg',
					time: 2 //3秒后自动关闭
				});
			}
		});
	}
}
/**
 * 商品类型模块
 * @type {{init: ProductTypeModule.init, productTypeBuffer: Array, requestData: (function(): Array), getProductTypeMap: (function()), getProductType: ProductTypeModule.getProductType}}
 */
var ProductTypeModule = {
	productTypeBuffer : [],
	init : function() {
		ProductTypeModule.productTypeBuffer = ProductTypeModule.requestData();
	},
	getProductTypeMap : function() {
		var map = {};
		var list = ProductTypeModule.productTypeBuffer;
		for (var i=0; i<list.length; i++) {
			var item = list[i];
			map[''+item.productTypeId] = item;
		}
		return map;
	},
	getProductType : function(tProductTypeId) {
		var list = ProductTypeModule.productTypeBuffer;
		for (var i=0; i<list.length; i++) {
			var item = list[i];
			if (item.productTypeId == tProductTypeId) {
				return item;
			}
		}
		return undefined;
	},
	requestData : function() {
		var targetData = [];
		$.ajax({
			url: GlobalConfig.serverAddress + "/productType/list",
			type: 'GET',
			cache: false,
			dataType: 'json',
			async: false, //设置同步
			contentType: "application/json; charset=utf-8",
			data: null,
			success: function(data) {
				if (data.code == 0) {
					targetData = data.data;
				} else {
					layer.open({
						content: '获取品类数据失败,'+data.desc,
						skin: 'msg',
						time: 2 //3秒后自动关闭
					});
				}
			},
			error: function() {
				layer.open({
					content: '连接服务器失败，请检查网络是否通畅！',
					skin: 'msg',
					time: 2 //3秒后自动关闭
				});
			}
		});
		return targetData;
	}
}
/**
 * 商品模块
 * @type {{init: ProductModule.init, productBuffer: Array, loadData: ProductModule.loadData, requestData: ProductModule.requestData}}
 */
var ProductModule = {
	productBuffer : [],
	init : function () {
		ProductModule.productBuffer = ProductModule.requestData();
	},
	loadData : function(data) {

	},
	requestData : function () {
		var targetList = [];
		$.ajax({
			url: GlobalConfig.serverAddress + "/shoppingCart/itemList",
			type: 'GET',
			cache: false,
			async: false, //设置同步
			dataType: 'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {},
			success: function(data) {
				if (data.code != '0') {
					layer.open({
						content: '获取购物车数据失败！'+data.desc,
						skin: 'msg',
						time: 2 //3秒后自动关闭
					});
				} else {
					targetList = data.data;
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
		return targetList;
	},
	getProduct : function(tProductId) {
		var list = ProductModule.productBuffer;
		for (var i=0; i<list.length; i++) {
			var item = list[i];
			if (item.productId == tProductId) {
				return item;
			}
		}
	}
}
/**
 * 商品属性模块   ----------------------------------------------- 数据获取未编写
 * @type {{createValidItemHTML: (function(*): string), createInvalidItemHTML: (function(*): string), getLabelValueList: ProductAttributeModule.getLabelValueList, productAttributeBuffer: Array, getItem: ProductAttributeModule.getItem, getLabelValue: ProductAttributeModule.getLabelValue, selectLabel: ProductAttributeModule.selectLabel, updateItemLabel: ProductAttributeModule.updateItemLabel}}
 */
var ProductAttributeModule = {
	productAttributeBuffer : [],
	init : function() {
		ProductAttributeModule.productAttributeBuffer = ProductAttributeModule.requestData();
	},
	requestData : function() {
		var targetList = [];

		return targetList;
	},
	getAttributeList: function(tProductId) {
		var product = ProductModule.getProduct(tProductId);
		if (undefined == product || undefined == product.attributeTypeId) {
			return undefined;
		}
		var attributeTypeId = product.attributeTypeId;
		var list = ProductAttributeModule.productAttributeBuffer;
		for (var i=0; i<list.length; i++) {
			var item = list[i];
			if (item.attributeTypeId == attributeTypeId) {
				return item;
			}
		}
		return undefined;
	},
	getAttribute: function(tAttributeId) {
		if (undefined == tAttributeId) {
			return undefined;
		}
		var list = ProductAttributeModule.productAttributeBuffer;
		for (var i=0; i<list.length; i++) {
			var item = list[i];
			var array = item.attributeList;
			for (var j=0; j<array.length; j++) {
				var cell = array[j];
				if (cell.attributeId == tAttributeId) {
					return cell;
				}
			}
		}
		return undefined;
	},
	selectAttribute : function(productId) {
		var attributeList = ProductAttributeModule.getAttributeList(productId);
		var html = '<div style="width:100%; background:#e5be6b; height:100px; border-top-left-radius: 15px; border-top-right-radius: 15px;">';
		html += '<div style="width:100%; background: #ff8a0c; border-top-left-radius: 15px; border-top-right-radius: 15px; padding: 5px 0 5px 0; text-align: center;">';
		html += '<i style="height:30px;">请选择该商品属性</i>';
		html += '<a onclick="layer.closeAll()" style="top:0px; width:30px;height:30px;background:url(/vita/customer/0_common/img/arrow-bottom.png) no-repeat center center;background-size:50%; float:right; margin-right:8px; "></a>';
		html += '</div>';
		html += '<div style="width:100%; padding: 3px 20px 10px 20px;">';
		for (var i = 0; i < attributeList.length; i++) {
			var item = attributeList[i];
			html += '<input onclick="ProductAttributeModule.updateItemLabel(' + itemId + ', ' + item.attributeId + ')" type="button" style="height:30px; padding:2px 10px 2px 10px; margin: 6px 5px 5px 3px; border-radius:5px; background:#' + temp.labelValueColor + '; float:left; " value="' + temp.labelValueName + '" />';
		}
		html += '</div>';
		html += '</div>';
		layer.open({
			type: 1,
			content: html,
			anim: 'up',
			style: 'position:fixed; bottom:0; left:0; width: 100%; height: 200px; border:none; border-top-left-radius: 15px; border-top-right-radius: 15px; background:#e5be6b; '
		});
	},
	updateItemLabel : function(tItemId, tNewLabelValueId) {
		var item = AdvanceOrderModule.getItem(tItemId);
		item.labelTypeValueId = tNewLabelValueId;
		var label = AdvanceOrderModule.getLabelValue(tNewLabelValueId);
		var target = $('#itemLabel'+tItemId);
		target.css("background", "#"+label.labelValueColor);
		target.text(label.labelValueName);
		layer.closeAll();
		console.log("修改商品项标签:");
		console.log(AdvanceOrderModule.orderData);
	}
}

var AdvanceOrderModule = {
	displayAreaId: "#itemDisplayArea",
	orderData: undefined,
	productLabelData: undefined,
	requestAdvanceOrderData: function() {
	},
	loadAdvanceOrderData: function(data) {
		var target = $(AdvanceOrderModule.displayAreaId);
		var temp;
		var html = '';
		for (var i = 0; i < data.length; i++) {
			temp = data[i];
			productList = temp.productList;
			if (productList.length <= 0) {
				html += '<div style="width:100%; color:#EF4F4F; font-size:10px; text-align: left;"> '+temp.productTypeName+'</div>';
			}
			for (var j=0; j<productList.length; j++) {
				html += AdvanceOrderModule.createValidItemHTML(productList[j]);
			}
		}
		target.html('');
		target.append(html);
		data = AdvanceOrderModule.orderData;
		$('#diningTypeName').text(data.diningTypeName);
		$('#diningTime').text(data.diningTime);
		$('#itemNumber').text(data.itemNumber);
		$('#orderAmount').text(data.orderAmount);
		$('#discount').text(data.discount);
		$('#discountAmount').text(parseFloat(data.orderAmount) - parseFloat(data.receivableAmount));
		$('#receivableAmount').text(data.receivableAmount);
	},
	requestProductLabelData: function() {

	},
	submitAdvanceOrderData: function() {

	},
	getItem: function(tItemId) {
		var data = AdvanceOrderModule.orderData.itemList,
			i,
			temp;
		for (i = 0; i < data.length; i++) {
			temp = data[i];
			if (temp.itemId == tItemId) {
				return temp;
			}
		}
		return undefined;
	},
	getLabelValueList: function(tLabelTypeId) {
		if (undefined == tLabelTypeId) {
			return undefined;
		}
		var i,
			productLabelData = AdvanceOrderModule.productLabelData,
			temp;
		for (i = 0; i < productLabelData.length; i++) {
			temp = productLabelData[i];
			if (tLabelTypeId == temp.labelTypeId) {
				return temp.labelValueList;
			}
		}
		return undefined;
	},
	getLabelValue: function(tLabelValueCellId) {
		if (undefined == tLabelValueCellId) {
			return undefined;
		}
		var i, j,
			productLabelData = AdvanceOrderModule.productLabelData,
			temp, cell;
		for (i = 0; i < productLabelData.length; i++) {
			temp = productLabelData[i].labelValueList;
			for (j = 0; j < temp.length; j++) {
				cell = temp[j];
				if (cell.labelValueId == tLabelValueCellId) {
					return cell;
				}
			}
		}
		return undefined;
	},
	createValidItemHTML: function(itemData) {
		var html = '<div class="zjzz-buylist-det" style="margin-bottom: 1px;">';
		html += '<img src="' + GlobalConfig.productImgRelativePath + itemData.productPicture + '" />';
		html += '<div class="zjzz-buylist-gdetail">';
		html += '<span class="zjzz-buylist-gtit1">' + itemData.productName + '</span>';
		html += '<span class="zjzz-buylist-gmoney">';
		html += '<i class="zjzz-buylist-gm1">' + itemData.productPrice + '</i>';
		html += '<i class="zjzz-buylist-gm2">x' + itemData.quantity + '</i>';
		html += '</span>';
		if (undefined != itemData.labelTypeValueId) {
			var temp = AdvanceOrderModule.getLabelValue(itemData.labelTypeValueId);
			html += '<span class="zjzz-buylist-gtit1" style="font-size:13px; " onclick="AdvanceOrderModule.selectLabel(' + itemData.itemId + ')">口味：<i id="itemLabel' + itemData.itemId + '" style=" height:20px; background: #' + temp.labelValueColor + '; border-radius: 5px; padding:0 5px 0 5px;">' + temp.labelValueName + '</i></span>';
		}
		html += '</div>';
		html += '</div>';
		return html;
	},
	createInvalidItemHTML: function(itemData) {
		var html = '<div class="zjzz-buylist-det" style="background:#FF685E; margin-bottom: 1px ;">';
		html += '<img src="' + GlobalConfig.productImgRelativePath + itemData.productPicture + '" />';
		html += '<div class="zjzz-buylist-gdetail">';
		html += '<span class="zjzz-buylist-gtit1">' + itemData.productName + '</span>';
		html += '<span class="zjzz-buylist-gmoney">';
		html += '<i class="zjzz-buylist-gm1">' + itemData.productPrice + '</i>';
		html += '<i class="zjzz-buylist-gm2">x' + itemData.quantity + '</i>';
		html += '</span>';
		html += '</div>';
		html += '</div>';
		return html;
	},
	selectLabel : function(itemId) {
		var item = AdvanceOrderModule.getItem(itemId);
		var labelList = AdvanceOrderModule.getLabelValueList(item.labelTypeId); 
		var html = '<div style="width:100%; background:#e5be6b; height:100px; border-top-left-radius: 15px; border-top-right-radius: 15px;">';
		html += '<div style="width:100%; background: #ff8a0c; border-top-left-radius: 15px; border-top-right-radius: 15px; padding: 5px 0 5px 0; text-align: center;">';
		html += '<i style="height:30px;">请选择该菜品口味</i>';
		html += '<a onclick="layer.closeAll()" style="top:0px; width:30px;height:30px;background:url(/vita/customer/0_common/img/arrow-bottom.png) no-repeat center center;background-size:50%; float:right; margin-right:8px; "></a>';
		html += '</div>';
		html += '<div style="width:100%; padding: 3px 20px 10px 20px;">';
		for (var i = 0; i < labelList.length; i++) {
			var temp = labelList[i];
			html += '<input onclick="AdvanceOrderModule.updateItemLabel(' + itemId + ', ' + temp.labelValueId + ')" type="button" style="height:30px; padding:2px 10px 2px 10px; margin: 6px 5px 5px 3px; border-radius:5px; background:#' + temp.labelValueColor + '; float:left; " value="' + temp.labelValueName + '" />';
		}
		html += '</div>';
		html += '</div>';
		layer.open({
			type: 1,
			content: html,
			anim: 'up',
			style: 'position:fixed; bottom:0; left:0; width: 100%; height: 200px; border:none; border-top-left-radius: 15px; border-top-right-radius: 15px; background:#e5be6b; '
		});
	},
	updateItemLabel : function(tItemId, tNewLabelValueId) {
		var item = AdvanceOrderModule.getItem(tItemId);
		item.labelTypeValueId = tNewLabelValueId;
		var label = AdvanceOrderModule.getLabelValue(tNewLabelValueId);
		var target = $('#itemLabel'+tItemId);
		target.css("background", "#"+label.labelValueColor);
		target.text(label.labelValueName);
		layer.closeAll();
		console.log("修改商品项标签:");
		console.log(AdvanceOrderModule.orderData);
	}
};

function test() {
	selectLabel(1);
}

var test_data = {
	productLabelData: [{
		labelTypeId: "1",
		labelValueList: [{
			labelValueId: "101",
			labelValueName: "正常",
			labelValueColor: "009F95"
		}, {
			labelValueId: "102",
			labelValueName: "非常辣",
			labelValueColor: "FC746C"
		}, {
			labelValueId: "103",
			labelValueName: "不辣",
			labelValueColor: "8CB2C5"
		}]
	},{
		labelTypeId: "2",
		labelValueList: [{
			labelValueId: "201",
			labelValueName: "201",
			labelValueColor: "009F95"
		}, {
			labelValueId: "202",
			labelValueName: "202",
			labelValueColor: "FC746C"
		}, {
			labelValueId: "203",
			labelValueName: "203",
			labelValueColor: "8CB2C5"
		}]
	}],
	advanceOrderData: {
		orderId: "k00101",
		diningTypeName: "堂食",
		diningTime: "现在",
		itemNumber: "10",
		orderAmount: "300",
		discount: "0.8",
		receivableAmount: "240",
		receivedAmount: "240",
		itemList: [{
			itemId: "1001",
			productName: "赣南脐橙",
			productPrice: "30.00",
			productPicture: "3.png",
			quantity: "1",
			labelTypeId: "1",
			labelTypeValueId: "101",
			isValid: true
		}, {
			itemId: "1002",
			productName: "赣北苹果",
			productPrice: "25.00",
			productPicture: "1.png",
			quantity: "2",
			labelTypeId: "2",
			labelTypeValueId: "201",
			isValid: true
		}, {
			itemId: "1003",
			productName: "失效商品测试",
			productPrice: "888.00",
			productPicture: "6.png",
			quantity: "1",
			labelTypeId: "1",
			labelTypeValueId: "101",
			isValid: false
		}, {
			itemId: "1004",
			productName: "赣北苹果",
			productPrice: "25.00",
			productPicture: "1.png",
			quantity: "1",
			isValid: true
		}]
	}

}