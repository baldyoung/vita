var SelectDiningTimeModule = {
	baseTime : 0,
	html: undefined,
	selectValue : 0,
	packageData : function() {
		var nowDate = new Date();
		nowDate.setMinutes(nowDate.getMinutes() + SelectDiningTimeModule.baseTime + parseInt(SelectDiningTimeModule.selectValue));
		var h = nowDate.getHours();
		h = h > 9 ? h : ('0'+h);
		var m = nowDate.getMinutes();
		m = m > 9 ? m : ('0'+m);
		var selectTime = h + ":" + m;
		return selectTime;
	},
	selectAction: function(diningTimeId, diningTimeValue) {
		SelectDiningTimeModule.selectValue = diningTimeValue;
		var selectTime = SelectDiningTimeModule.packageData() + '左右';
		$('#presetTimeResult').text(selectTime);
		layer.closeAll();
	},
	init: function() {
		if (SelectDiningTimeModule.html == undefined) {
			var targetData = SelectDiningTimeModule.requestData();
			SelectDiningTimeModule.loadData(targetData);
		}
		layer.open({
			type: 1,
			content: SelectDiningTimeModule.html,
			anim: 'down',
			style: 'position:fixed; top:0; left:0; width: 100%; height: 200px; border:none; border-bottom-left-radius: 15px; border-bottom-right-radius: 15px; background:#fffdf9; padding-bottom:17px; '
		});
	},
	createDisplayItemHTML: function() {
	},
	loadData: function(data) {
		var str = '<div style="height:200px;" ><div ><h5 style="text-align:center; font-weight:bold; margin-top:5px; ">请选择就餐时间</h5></div><div style="height:168px; padding-top:5px; overflow:auto;">';
		var i, item;
		for(i=0; i<data.length; i++) {
			item = data[i];
			str += '<div class="pay-a1" onclick="SelectDiningTimeModule.selectAction(\''+item.diningTimeId+'\', \''+item.diningTimeValue+'\')"><div class="pay-a1-b1" data-click="false">';
			str += '<span class="pay-a1-t1">'+item.diningTimeName+'</span>';
			str += '<span class="pay-a1-t1" style="float:right; font-size:12px; "></span>';
			str += '</div></div>';
		}
		str += '</div></div>';
		SelectDiningTimeModule.html = str;
	},
	requestData: function() {
		var targetData = [];
		if ("test" == GlobalConfig.currentModel) {
			return test_diningTimeList;
		}
		$.ajax({
			url: GlobalConfig.serverAddress + "",
			type: 'GET',
			cache: false,
			//async: false, //设置同步
			dataType: 'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {},
			success: function(data) {
				if (data.code == '0') {
					targetData = data.data;
				} else {
					layer.open({
						content: '获取就餐时间失败！',
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
		return targetData;
	}
}

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

var ShopingCartModule = {
	displayAreaId: '#itemDisplayArea',
	productListBuffer: [],
	invalidProductListBuffer: [],
	diningType : undefined,
	requestAndLoadData: function() {
		$.ajax({
			url: GlobalConfig.serverAddress + "/shoppingCart/itemList",
			type: 'GET',
			cache: false,
			// async: false, //设置同步
			dataType: 'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {},
			success: function(data) {
				if (data.code != '0') {
					layer.open({
						content: '获取购物车数据失败！'+(undefined == data.desc ? '【无效访问】' : data.desc),
						skin: 'msg',
						time: 2 //3秒后自动关闭
					});
				} else {
					ShopingCartModule.loadData(data.data);
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
	loadData : function(data) {
		if (undefined == data || 0 == data.length) {
			var target = $(ShopingCartModule.displayAreaId);
			var html = '<div style="text-align:center;">当前购物车是空的哟</div>';
			html += '<div style="text-align:center; "><a href="../3_menu/menu.html" style="color:#f1a417;">前往选购</a></div>';
			html += '<div style="text-align:center;">^-^</div>';
			target.html(html);
			return;
		}
		var i, j;
		var typeList = ProductTypeModule.productTypeBuffer;
		var productList = data;
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
		var k = 0, d = 0;
		for (i = 0; i < typeList.length; i++) {
			var pList = typeList[i].productList;
			for (j = 0; j < pList.length; j++) {
				pList[j].selectedStatus = false;
				if (pList[j].valid == false) {
					ShopingCartModule.invalidProductListBuffer[d++] = pList[j];
					pList.splice(j, 1);
					j--;
					continue;
				}
				ShopingCartModule.productListBuffer[k++] = pList[j];
			}
			if (pList.length == 0) {
				typeList.splice(i, 1);
				i--;
			}
		}
		ShopingCartModule.showItemList(typeList);
		$("#ckAll").click();
	},
	showItemList: function(itemList) {
		var target = $(ShopingCartModule.displayAreaId);
		target.html('');
		var i;
		for (i = 0; i < itemList.length; i++) {
			target.append(ShopingCartModule.createItemDisplayUnitHTML(itemList[i]));
		}
		console.log("无效商品");
		console.log(ShopingCartModule.invalidProductListBuffer);
		if (undefined != ShopingCartModule.invalidProductListBuffer && 0 != ShopingCartModule.invalidProductListBuffer.length) {
			var invalidItem = {
				productTypeName: "无效商品",
				invalid: true,
				productList: ShopingCartModule.invalidProductListBuffer
			}
			target.append(ShopingCartModule.createItemDisplayUnitHTML(invalidItem));
		}
	},
	createItemDisplayUnitHTML: function(itemInfo) {
		var str = '<div class="shop-cart-listbox1">';
		str += '<div class="shop-cart-box2" style="border-bottom: 0px solid #DCDCDC;" >';
		if (itemInfo.invalid != true) {
			str += '<input type="checkbox" name="sub2" class="btn1 ckAllA typeId' + itemInfo.productTypeId + '" onclick="ShopingCartModule.updateItemCheckBox(' + itemInfo.productTypeId + ', 2)">';
		}
		str += '<span class="shop-cart-ltext1">&nbsp;' + itemInfo.productTypeName + '</span>';
		str += '</div>';
		var i, productList = itemInfo.productList;
		for (i = 0; i < productList.length; i++) {
			str += ShopingCartModule.createProductDisplayUnitHTML(productList[i]);
		}
		str += '</div>';
		return str;
	},
	createProductDisplayUnitHTML: function(productInfo) {
		var str = '<div class="index-goods goodsUnit' + productInfo.productId + '" style="' + (productInfo.valid != true ? 'background:#E1E1E1;' : '') + '" >';
		if (productInfo.valid == true) {
			str += '<span onclick="ShopingCartModule.alterProductStatus(' + productInfo.productId + ')" class="shop-cart-check2"><input type="checkbox" id="pCheckBoxId' + productInfo.productId + '" name="sub2" class="shopcart-input1 btn2 ckAllA productIsNeed' + productInfo.productId + ' pType' + productInfo.productTypeId + '" style="pointer-events: none;"></span>';
		}
		str += '<span class="index-goods-img"><img src="' + GlobalConfig.productImgRelativePath + productInfo.productImgName + '"></span>';
		str += '<div class="index-goods-textbox">';
		str += '<span class="index-goods-text1">' + productInfo.productName + '</span>';
		str += '<div class="index-goods-text2">￥<i class="priceJs">' + productInfo.productPrice + '</i></div>';
		str += '<div class="shop-cart-box3">';
		str += '<span class="shop-cart-subtract" onclick="ShopingCartModule.alterProductQuantity(' + productInfo.productId + ', -1)"></span>';
		str += '<input type="tel" id="proQuantityText' + productInfo.productId + '" size="4" value="' + productInfo.currentQuantity + '" id="tb_count" class="shop-cart-numer" readonly="true" style="' + (productInfo.valid != true ? 'background:#E1E1E1;' : '') + '">';
		str += '<span class="shop-cart-add" onclick="ShopingCartModule.alterProductQuantity(' + productInfo.productId + ', 1)"></span>';
		str += '</div>';
		str += '</div>';
		str += '</div>';
		return str;
	},
	readySubmitOrder: function() { // 准备生成预订单
		var productList = ShopingCartModule.packageData();
		console.log(JSON.stringify(productList));
		if (false && GlobalConfig.currentServiceType == 'take-out') {
			layer.open({
				content : '当前为外卖服务，稍后需要您提供位置信息！',
				btn : ['好的', '拒绝'],
				yes : function() {
					// GlobalMethod.redirectURL('../3_login/login.html');
					GlobalMethod.redirectURL('../4_takeOutPreInfo/takeOutPreInfo.html');
				},
				no : function() {
					layer.open({
						content : '亲，无法提供位置信息将无法为您提供外卖服务！',
						btn : ['知道了']
					})
				}
			});
			return;
		}
		layer.open({
			content: '请选择就餐方式！',
			btn: ['堂食', '打包'],
			yes: function() {
				ShopingCartModule.diningType = 0; // 堂食
				ShopingCartModule.trySubmit();
				// window.location.href = '../4_advanceOrder/advanceOrder.html';
			},
			no: function() {
				ShopingCartModule.diningType = 1; // 外卖
				ShopingCartModule.trySubmit();
			}
		});
	},
	submitOrder: function() {

	},
	getProductInfoByProductId: function(tProductId) { // 通过商品id获取商品对象
		var i, productList = ShopingCartModule.productListBuffer;
		for (i = 0; i < productList.length; i++) {
			if (productList[i].productId == tProductId) {
				return productList[i];
			}
		}
		return undefined;
	},
	alterProductQuantity: function(tProductId, tQuantity) { // 修改商品数量
		var productInfo = ShopingCartModule.getProductInfoByProductId(tProductId);
		if (undefined == productInfo) {
			return;
		}
		var temp = productInfo.currentQuantity + parseInt(tQuantity);
		if (temp < 1 || temp > 99) {
			layer.open({
				content: temp < 1 ? '若要删除，请操作右上角删除按钮' : '商品单次加入购物车的数量上限为99',
				skin: 'msg',
				time: 2 //2秒后自动关闭
			});
			return;
		}
		$.ajax({
			url: GlobalConfig.serverAddress + "/shoppingCart/addProduct",
			type: 'GET',
			cache: false,
			dataType : 'json',
			async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			// contentType : 'application/json; charset=utf-8',
			data: {
				productId : tProductId,
				quantity : tQuantity
			},
			success: function(data) {
				if (data.code != 0) {
					layer.open({
						content: ''+data.desc,
						skin: 'msg',
						time: 2
					});
				} else {
					productInfo.currentQuantity = temp;
					$('#proQuantityText' + tProductId).val(temp);
					ShopingCartModule.countAllProductAmount();
				}
			},
			error: function() {
				//swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
	},
	alterProductStatus: function(tProductId) { // 修改商品状态
		var productInfo = ShopingCartModule.getProductInfoByProductId(tProductId);
		var lastStatus = productInfo.selectedStatus;
		productInfo.selectedStatus = !lastStatus;
		$('.productIsNeed' + tProductId).prop("checked", productInfo.selectedStatus);
		ShopingCartModule.updateItemCheckBox(productInfo.productTypeId, 1);
		// ShopingCartModule.countAllProductAmount();
	},
	updateItemCheckBox: function(tProductTypeId, arg) { // 更新类目相关的状态
		if (arg == 1) { // 更新状态
			var targetList = $('.pType' + tProductTypeId);
			var i;
			if (targetList.length == 0) {
				$('.typeId' + tProductTypeId).parent().parent().remove();
			} else {
				for (i = 0; i < targetList.length; i++) {
					if (targetList[i].checked == false) {
						$('.typeId' + tProductTypeId).prop('checked', false);
						break;
					}
				}
				if (i == targetList.length) {
					$('.typeId' + tProductTypeId).prop('checked', true);
				}
			}

		} else if (arg == 2) { // 批量选择
			var target = $('.typeId' + tProductTypeId);
			var targetList = $('.pType' + tProductTypeId);
			var newStatus = target[0].checked;
			for (var i = 0; i < targetList.length; i++) {
				var productId = targetList[i].id.split("Id")[1];
				var productInfo = ShopingCartModule.getProductInfoByProductId(productId);
				productInfo.selectedStatus = newStatus;
			}
			$('.typeId' + tProductTypeId).prop('checked', newStatus);
			$('.pType' + tProductTypeId).prop('checked', newStatus);
		}
		ShopingCartModule.countAllProductAmount();
	},
	selectItemAllProduct: function() {
		var newStatus = $("#ckAll")[0].checked;
		$('.ckAllA').prop("checked", newStatus);
		var targetList = ShopingCartModule.productListBuffer;
		for (var i = 0; i < targetList.length; i++) {
			targetList[i].selectedStatus = newStatus;
		}
		ShopingCartModule.countAllProductAmount();
		if (targetList.length == 0) {
			layer.open({
				content: "当前购物车无有效商品！",
				skin: 'msg',
				time: 3 //3秒后自动关闭
			});
		}
	},
	countAllProductAmount: function() { // 统计商品总额，并刷新全选按钮的状态
		var temp = 0,
			flag = true;
		var productList = ShopingCartModule.productListBuffer;
		if (productList.length == 0) {
			$('#ckAll').prop('checked', false);
			$('#AllTotal').text('0.00');
			return;
		}
		for (var i = 0; i < productList.length; i++) {
			if (productList[i].selectedStatus == true) {
				temp += parseFloat(productList[i].productPrice) * parseInt(productList[i].currentQuantity);
				continue;
			}
			flag = false;
		}
		$('#AllTotal').text(temp.toFixed(2));
		$("#ckAll").prop("checked", flag);
	},
	deleteProduct: function(tProductId) {
		var productList = ShopingCartModule.productListBuffer;
		for (var i = 0; i < productList.length; i++) {
			if (productList[i].productId == tProductId) {
				$('.goodsUnit' + productList[i].productId).remove();
				productList.splice(i, 1);
				console.log(tProductId);
				return;
			}
		}
	},
	deleteSelectedProduct: function() {
		layer.open({
			content: '确定删除所选商品？',
			btn: ['确定', '取消'],
			yes: function() {
				var deleteProductIds = [];
				var targetList = $('.ckAllA');
				for (var i = 0; i < targetList.length; i++) {
					var item = targetList[i];
					if (item.id == undefined || item.id == "") {
						continue;
					}
					var productId = item.id.split("Id")[1];
					var productInfo = ShopingCartModule.getProductInfoByProductId(productId);
					if (productInfo.selectedStatus == true) {
						deleteProductIds[deleteProductIds.length] = productId;
					}
				}
				if (0 == deleteProductIds.length) {
					return;
				}
				$.ajax({
					url: GlobalConfig.serverAddress + "/shoppingCart/itemDelete",
					type: 'POST',
					cache: false,
					dataType : 'json',
					async: false, //设置同步
					contentType: "application/x-www-form-urlencoded;charset=utf-8",
					// contentType : 'application/json; charset=utf-8',
					data: {
						productIdList : deleteProductIds
					},
					success: function(data) {
						if (data.code != 0) {
							layer.open({
								content: ''+data.desc,
								skin: 'msg',
								time: 2
							});
						} else {
							for (var t=0; t<deleteProductIds.length; t++) {
								var productInfo = ShopingCartModule.getProductInfoByProductId(deleteProductIds[t]);
								ShopingCartModule.deleteProduct(deleteProductIds[t]);
								ShopingCartModule.updateItemCheckBox(productInfo.productTypeId, 1);
							}
						}
						layer.closeAll();
					},
					error: function() {
						layer.closeAll();
					}
				});
			},
			no: function() {
			}
		});
	},
	packageData : function () {
		var productList = ShopingCartModule.productListBuffer;
		console.log("购物车商品数据：");
		console.log(productList);
		var result = [];
		for (var i=0; i<productList.length; i++) {
			var cell = productList[i];
			if (cell.selectedStatus == false) {
				continue;
			}
			var item = {
				productId : cell.productId,
				productQuantity : cell.currentQuantity
			}
			result[result.length] = item;
		}
		return result;
	},
	trySubmit : function() {
		var data = {
			diningTime : SelectDiningTimeModule.packageData(),
			diningType : ShopingCartModule.diningType,
			itemList : ShopingCartModule.packageData()
		};
		$.ajax({
			url: GlobalConfig.serverAddress + "/shoppingCart/readySubmit",
			type: 'POST',
			cache: false,
			// async: false, //设置同步
			dataType: 'json',
			// contentType : "application/x-www-form-urlencoded;charset=utf-8",
			contentType : "application/json; charset=utf-8",
			data: JSON.stringify(data),
			success: function(data) {
				if (data.code != '0') {
					layer.open({
						content: '提交失败（'+(undefined == data.desc ? '【无效访问】' : data.desc)+'）',
						skin: 'msg',
						time: 3 //3秒后自动关闭
					});
				} else {
					// --------------------------------------------- 跳转到预订单页面
					GlobalMethod.redirectURL("../4_advanceOrder/advanceOrder.html");
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
	}

};

function init() {
	return;
	$(".shop-cart-add").click(function() {
		var multi = 0;
		var vall = $(this).prev().val();
		vall++;
		$(this).prev().val(vall);
		TotalPrice();
	});
	$(".shop-cart-subtract").click(function() {
		var multi = 0;
		var vall = $(this).next().val();
		vall--;
		if (vall <= 0) {
			vall = 1;
		}
		$(this).next().val(vall);
		TotalPrice();
	});

	$(".btn1").click(function() {
		var $btn2 = $(this).parent(".shop-cart-box2").siblings(".index-goods").children(".shop-cart-check2").children(".btn2");
		if ($(this).is(':checked')) {
			$btn2.prop("checked", this.checked);
			TotalPrice();
		} else {
			$btn2.removeAttr("checked");
			TotalPrice();
		}
	});

	$(".btn2").click(function() {
		var goods = $(this).closest(".shop-cart-listbox1").find(".btn2"); //获取本店铺的所有商品
		var goodsC = $(this).closest(".shop-cart-listbox1").find(".btn2:checked"); //获取本店铺所有被选中的商品
		var Shops = $(this).closest(".shop-cart-listbox1").find(".btn1"); //获取本店铺的全选按钮
		if (goods.length == goodsC.length) { //如果选中的商品等于所有商品
			Shops.prop('checked', true); //店铺全选按钮被选中
			TotalPrice();
		} else { //如果选中的商品不等于所有商品
			Shops.prop('checked', false); //店铺全选按钮不被选中
			TotalPrice();
		}
	});

	$("#ckAll").click(function() {
		$("input[name='sub2']").prop("checked", this.checked);
		TotalPrice();
	});
	$("input[name='sub2']").click(function() {
		var $subs = $("input[name='sub2']");
		$("#ckAll").prop("checked", $subs.length == $subs.filter(":checked").length ? true : false);
		TotalPrice();
	});

	$(".shop-cart-htext1").click(function() {
		$(".scart-total-text2").toggleClass("hide");
		$(".scart-total-text3").toggleClass("hide");
		$(".scart-total-text4").toggleClass("hide");
		$(".delete").toggleClass("hide");
		TotalPrice();
	});

	$(".delete").click(function() {
		if ($("#ckAll").is(':checked')) {
			$(".shop-cart-listbox1").remove();
			$("#ckAll").prop('checked', false);
			TotalPrice();
		}
		if ($(".btn1").is(':checked')) {
			$(".btn1:checked").closest(".shop-cart-listbox1").remove();
			TotalPrice();
		}
		if ($(".btn2").is(':checked')) {
			$(".btn2:checked").parent(".shop-cart-check2").parent(".index-goods").remove();
			TotalPrice();
		}
	});

	function TotalPrice() {
		var allprice = 0; //总价
		$(".shop-cart-listbox1").each(function() { //循环每个店铺
			var oprice = 0; //店铺总价
			$(this).find(".btn2").each(function() { //循环店铺里面的商品
				if ($(this).is(":checked")) { //如果该商品被选中
					var num = $(this).parents(".index-goods").find(".shop-cart-numer").val(); //得到商品的数量
					var price = parseFloat($(this).parents(".index-goods").find(".priceJs").text()); //得到商品的单价
					var total = price * num; //计算单个商品的总价
					oprice += total; //计算该店铺的总价
				}
				$(this).closest(".shop-cart-listbox1").find(".ShopTotal").text(oprice.toFixed(2)); //显示被选中商品的店铺总价
			});
			var oneprice = parseFloat($(this).find(".ShopTotal").text()); //得到每个店铺的总价
			allprice += oneprice; //计算所有店铺的总价
		});
		$("#AllTotal").text(allprice.toFixed(2)); //输出全部总价
	}
};

$(function() {
	//init();
	ProductTypeModule.init();
	ShopingCartModule.requestAndLoadData();
	SelectDiningTimeModule.selectAction(0, 0);
	//$("#ckAll").prop("checked", true);
	//$("input[name='sub2']").prop("checked", this.checked);

});

/*

 后端接口拟定返回数据格式 ：
{
	presetTimeLength : 预定时间,
	submitFlag : 当前购物车是否已经有预定单,
	itemList : [
		{
			productTypeName : ,
			productList : [
				{
					productId : 商品Id,
					productName : ,
					productPicture : ,
					productPrice : ,
					productQuantity : 
				}...]
		}...]
		
}


 */

// 测试数据
var test_shopingCartInfo = {
	presetTimeText: '现在',
	submitFlag: 'yes',
	itemList: [{
		productTypeName: '水果',
		productTypeId: 1,
		productList: [{
			productId: 1,
			productName: '有效1',
			productPicture: '1.jpg',
			productPrice: 12.30,
			productQuantity: 3,
			valid: true
		}, {
			productId: 2,
			productName: '无效2',
			productPicture: '3.png',
			productPrice: 32.30,
			productQuantity: 1,
			valid: false
		}, {
			productId: 3,
			productName: '有效3',
			productPicture: '3.png',
			productPrice: 32.30,
			productQuantity: 3,
			valid: true
		}]
	}, {
		productTypeName: '其它',
		productTypeId: 2,
		productList: [{
			productId: 4,
			productName: '测试商品4',
			productPicture: '4.png',
			productPrice: 12.30,
			productQuantity: 2,
			valid: false
		}, {
			productId: 5,
			productName: '测试商品5',
			productPicture: '5.png',
			productPrice: 22.30,
			productQuantity: 5,
			valid: true
		}, {
			productId: 6,
			productName: '测试商品6',
			productPicture: '6.png',
			productPrice: 72.30,
			productQuantity: 1,
			valid: true
		}]
	}, {
		productTypeName: '失效测试',
		productTypeId: 3,
		productList: [{
			productId: 4,
			productName: '测试商品4',
			productPicture: '4.png',
			productPrice: 12.30,
			productQuantity: 2,
			valid: false
		}]
	}]
};