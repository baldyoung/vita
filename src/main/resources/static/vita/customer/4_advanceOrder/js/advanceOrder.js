$(function() {
	init();
});
var AdvanceOrderData = [];
function init() {
	HeartBeatModule.init();
	ProductAttributeModule.init();
	ProductTypeModule.init();
	ProductModule.init();

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
			if (undefined == ProductAttributeModule.getAttributeList(product.productId)) {
				product.productAttributeTypeId = undefined;
			}
		}
	}
	console.log(typeList);
	AdvanceOrderData = typeList;
	AdvanceOrderModule.loadAdvanceOrderData(typeList);
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
		var list = ProductAttributeModule.requestData();
		for (var i=0; i<list.length; i++) {
			var array = list[i].productAttributeList;
			for (var j=0; j<array.length; j++) {
				array[j].productAttributeColor = ColorModule.getTargetColor(j);
			}
		}
		ProductAttributeModule.productAttributeBuffer = list;
	},
	requestData : function() {
		var targetList = [];
		$.ajax({
			url: GlobalConfig.serverAddress + "/attribute/all",
			type: 'GET',
			cache: false,
			async: false, //设置同步
			dataType: 'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {},
			success: function(data) {
				if (data.code != '0') {
					layer.open({
						content: '获取商品属性数据失败！'+data.desc,
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
	getAttributeList: function(tProductId) {
		var product = ProductModule.getProduct(tProductId);
		//console.log('kkk');
		//console.log(product);
		if (undefined == product || undefined == product.productAttributeTypeId) {
			return undefined;
		}
		var attributeTypeId = product.productAttributeTypeId;
		var list = ProductAttributeModule.productAttributeBuffer;
		for (var i=0; i<list.length; i++) {
			var item = list[i];
			if (item.productAttributeTypeId == attributeTypeId) {
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
			var array = item.productAttributeList;
			for (var j=0; j<array.length; j++) {
				var cell = array[j];
				if (cell.productAttributeId == tAttributeId) {
					return cell;
				}
			}
		}
		return undefined;
	},
	selectAttribute : function(productId) {
		var attributeList = ProductAttributeModule.getAttributeList(productId);
		if (undefined == attributeList) {
			return;
		}
		attributeList = attributeList.productAttributeList;
		var html = '<div style="width:100%; background:#e5be6b; height:100px; border-top-left-radius: 15px; border-top-right-radius: 15px;">';
		html += '<div style="width:100%; background: #ff8a0c; border-top-left-radius: 15px; border-top-right-radius: 15px; padding: 5px 0 5px 0; text-align: center;">';
		html += '<i style="height:30px;">请选择该商品属性</i>';
		html += '<a onclick="layer.closeAll()" style="top:0px; width:30px;height:30px;background:url('+'/vita/customer/0_common/img/arrow-bottom.png'+') no-repeat center center;background-size:50%; float:right; margin-right:8px; "></a>';
		html += '</div>';
		html += '<div style="width:100%; padding: 3px 20px 10px 20px;">';
		for (var i = 0; i < attributeList.length; i++) {
			var item = attributeList[i];
			html += '<input onclick="ProductAttributeModule.updateItemLabel(' + productId + ', ' + item.productAttributeId + ')" type="button" style="height:30px; padding:2px 10px 2px 10px; margin: 6px 5px 5px 3px; border-radius:5px; background:#' + item.productAttributeColor + '; float:left; " value="' + item.productAttributeName + '" />';
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
	updateItemLabel : function(tProductId, tAttributeId) {
		var item = ProductModule.getProduct(tProductId);
		item.attributeId = tAttributeId;
		var attribute = ProductAttributeModule.getAttribute(tAttributeId);
		var target = $('#itemLabel'+tProductId);
		target.css("background", "#"+attribute.productAttributeColor);
		target.text(attribute.productAttributeName);
		layer.closeAll();
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
			var productList = temp.productList;
			if (productList.length > 0) {
				html += '<div class="indent-details-box2" style="border-top:solid 5px #F5F5F4;">\n' +
					'<span class="indent-details-text4 tcolor-grey">'+temp.productTypeName+'</span>\n' +
					'<span id="diningTypeName" class="indent-details-text5 tcolor-black">'+productList.length+'</span>\n' +
					'</div>';
			}
			for (var j=0; j<productList.length; j++) {
				html += AdvanceOrderModule.createValidItemHTML(productList[j]);
			}
		}
		//console.log(html);
		target.html('');
		target.append(html);
		return;
		data = AdvanceOrderModule.orderData;
		$('#diningTypeName').text(data.diningTypeName);
		$('#diningTime').text(data.diningTime);
		$('#itemNumber').text(data.itemNumber);
		$('#orderAmount').text(data.orderAmount);
		$('#discount').text(data.discount);
		$('#discountAmount').text(parseFloat(data.orderAmount) - parseFloat(data.receivableAmount));
		$('#receivableAmount').text(data.receivableAmount);
	},
	submitAction : function() {
		layer.open({
			content: '确定提交吗？',
			btn: ['确定', '取消'],
			yes: function() {
				layer.closeAll();
				AdvanceOrderModule.submitAdvanceOrderData();
			},
			no: function() {
			}
		});
	},
	submitAdvanceOrderData: function() {
		var data = AdvanceOrderModule.packageData();
		console.log(data);
		//var href="../4_orderResult/orderResult.html";
		$.ajax({
			url: GlobalConfig.serverAddress + "/order/do",
			type: 'POST',
			cache: false,
			dataType: 'json',
			async: false, //设置同步
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(data),
			success: function(data) {
				if (data.code == 0) {

				} else {
					layer.open({
						content: '提交订单失败：'+data.desc,
						skin: 'msg',
						time: 3 //3秒后自动关闭
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

	},
	packageData : function() {
		var list = AdvanceOrderData;
		console.log("###未处理数据：")
		console.log(list);
		var data = [];
		var targetData = [];
		for (var i=0; i<list.length; i++) {
			var item = list[i];
			var array = item.productList;
			if (array.length == 0) {
				continue;
			}
			var pList = [];
			var productItem = {
				productTypeId : item.productTypeId,
				productList : pList
			};
			for (var j=0; j<array.length; j++) {
				var cell = {
					productId : array[j].productId,
					productQuantity : array[j].currentQuantity
				}
				if (undefined != array[j].attributeId) {
					cell.productAttributeId = array[j].attributeId;
				}
				pList[pList.length] = cell;
				targetData[targetData.length] = cell;
			}
			data[data.length] = productItem;
		}
		console.log("###处理后数据：")
		console.log(data);
		console.log(targetData);
		return targetData;
	},
	createValidItemHTML: function(itemData) {
		var html = '<div class="zjzz-buylist-det" style="margin-bottom: 1px;">';
		html += '<img src="' + GlobalConfig.productImgRelativePath + itemData.productImgName + '" />';
		html += '<div class="zjzz-buylist-gdetail">';
		html += '<span class="zjzz-buylist-gtit1">' + itemData.productName + '</span>';
		html += '<span class="zjzz-buylist-gmoney">';
		html += '<i class="zjzz-buylist-gm1">' + itemData.productPrice + '</i>';
		html += '<i class="zjzz-buylist-gm2">x' + itemData.currentQuantity + '</i>';
		html += '</span>';
		if (undefined != itemData.productAttributeTypeId) {
			var temp = ProductAttributeModule.getAttributeList(itemData.productAttributeTypeId);
			html += '<span class="zjzz-buylist-gtit1" style="font-size:13px; " onclick="ProductAttributeModule.selectAttribute(' + itemData.productId + ')">口味：<i id="itemLabel' + itemData.productId + '" style=" height:20px; background: #' + '66cccc' + '; border-radius: 5px; padding:0 5px 0 5px;">' + '默认' + '</i></span>';
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
	}
};

var ColorModule = {
	DefaultColor : ['009F95', 'FC746C', '8CB2C5'],
	getColor : function () {
		var i = Math.floor(Math.random() * ColorModule.DefaultColor.length);
		return ColorModule.DefaultColor[i];
	},
	getTargetColor : function (k) {
		var list = ColorModule.DefaultColor;
		if (k >= list.length || k < 0) {
			return list[list.length - 1];
		}
		return list[k];
	}
}




