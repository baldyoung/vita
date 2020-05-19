

$(function(){
	console.log("workingL.js content...");
	registerMonitor();
	RoomModule.init();
	ItemModule.init();
	BillSettleAccountModule.init();
	DiningRoomStatusModule.init();
});
function registerMonitor() {
	$('.roomStatusLabel').bind("click", function(){
		var id = $(this).attr("id");
		console.log(id);
	});
	$(".orderPanelInfoType").bind("click", function(){
		var id = $(this).children("a").attr("data-toggle");
		console.log(id);
		$(".tab-pane").removeClass("active");
		$("#"+id).addClass("active");
		$(".orderPanelInfoType").removeClass("active");
		$(this).addClass("active");
	})
};
/**
 * 就餐位模块
 * @type {{init: RoomModule.init, requestAndLoadData: RoomModule.requestAndLoadData, createDisplayUnitHtml: (function(*): string), loadData: RoomModule.loadData}}
 */
var RoomModule = {
	init : function () {
		RoomModule.requestAndLoadData();
	},
	requestAndLoadData : function () {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mDiningRoom/list",
			type: 'GET',
			cache: false,
			dataType:'json',
			contentType: "application/json; charset=utf-8",
			data: {},
			success: function (data) {
				if (data.code == 0) {
					data = data.data;
					RoomModule.loadData(data);
				} else {
					swal("获取数据失败", data.desc, "error");
				}
			}
		});
	},
	loadData : function (data) {
		var target = $('#RoomList');
		target.html('');
		for (var i=0; i<data.length; i++) {
			var item = data[i];
			var html = RoomModule.createDisplayUnitHtml(item);
			target.append(html);
		}
	},
	createDisplayUnitHtml : function (item) {
		if (!GlobalMethod.isEmpty(item.reservationData)) {
			item.reservationData = '预约: ' + item.reservationData;
		} else {
			item.reservationData = '无预约';
		}
		var  html = '<div id="Room3" class="col-sm-4 roomPanel" style="width:480px; height:300px; ">' +
			'<div class="ibox roomPanel" style=" ">' +
			'<div class="ibox-title roomPanelTop" style="height:50px;">' +
			'<span  class="pull-left" style="cursor:pointer; font-size:10px;">' +
			'<span onclick="QRCodeModule.requestRoomCodeImgName('+item.diningRoomId+')" class="glyphicon glyphicon-qrcode" aria-hidden="true" data-toggle="modal" data-target="#qrCodePanel"></span>' +
			'</span>' +
			'<span id="roomStatusStyleArea'+item.diningRoomId+'" class=" pull-right roomStatusLabel">'+DiningRoomStatusModule.toDiningRoomStatusNameStyle(item.diningRoomStatus)+'</span>' +
			'<h5>' +
			'<span class="pull-left">&nbsp;&nbsp;'+item.diningRoomName+'&nbsp;&nbsp;-&nbsp;&nbsp;</span>' +
			'</h5>' +
			'</div>' +
			'<div class="ibox-content roomPanelBottom">' +
			'<div class="col-sm-12 roomOptionalArea2">' +
			'<div class="col-sm-5" style="padding: 0 0 0 0;">' +
			'<div class="team-members roomOptionalArea">' +
			'<button onclick="BillModule.requestAndLoadData('+item.diningRoomId+')" class="btn btn-info " type="button" data-toggle="modal" data-target="#OrderFormWindow" style="margin-bottom:5px; " >' +
			'<i class="fa fa-paste"></i> 订单详情' +
			'<span id="orderItemNewsTip'+item.diningRoomId+'" class="badge badge-danger newsTip" style="background:#ED5565; color:black;  display:none;    "></span>' +
			'</button>' +
			'<button class="btn btn-primary " data-toggle="modal" data-target="#customerMsgPanel" type="button" style="margin-bottom:5px; " onclick="MessageModule.loadCurrentRoomMessage('+item.diningRoomId+', \''+item.diningRoomName+'\')">' +
			'<i class="glyphicon glyphicon-envelope"></i> 客户消息' +
			'<span id="messageNewsTip'+item.diningRoomId+'" class="badge badge-danger newsTip" style="background:#ED5565; color:black; display:none;    "></span>' +
			'</button>' +
			'<button onclick="DiningRoomReservationModule.loadRoomReservationInfo('+item.diningRoomId+')" class="btn btn-primary " data-toggle="modal" data-target="#roomPreOrderHistoryPanel" type="button" style="margin-bottom:5px; background:#e0a9f9; " >' +
			'<i class="glyphicon glyphicon-tag"></i> 预定记录' +
			'<span class="badge badge-danger" style="background:#ED5565; color:black;  display:none;    "></span>' +
			'</button>' +
			'<button onclick="DiningRoomStatusModule.readyToSelect('+item.diningRoomId+')" class="btn btn-primary " data-toggle="modal" data-target="#roomStatusPanel" type="button" style="margin-bottom:5px; " >' +
			'<i class="glyphicon glyphicon-edit"></i> 修改状态' +
			'</button>' +
			'</div>' +
			'</div>' +
			'<div class="col-sm-7" style="padding: 0 0 0 0;">' +
			'<textarea style="resize:none; height:150px; width:100%; border:0px solid; margin-bottom: 5px; cursor:pointer; " readonly="true">'+item.diningRoomInfo+'</textarea>' +
			'</div>' +
			'</div>' +
			'<div class="row  m-t-sm">' +
			'<span class=" pull-left " style=" width:100%; ">' +
			'<div ondblclick="DiningRoomReservationModule.requestRemoveRoomTip('+item.diningRoomId+', \''+item.diningRoomName+'\')" id="reservationTip'+item.diningRoomId+'" class="roomPanelPreTip" style="cursor:default;">'+item.reservationData+'</div>' +
			'</span>' +
			'</div>' +
			'</div>' +
			'</div>' +
			'</div>' +
			'</div>';
		return html;
	}
}

/**
 * 账单模块
 * @type {{currentLoadRoomId: undefined, createBillItemUnitHTML: (function(*, *): string), createBillTopTitleHTML: (function(): string), createOrderItemUnitHTML: (function(*): string), createOrderUnitHTML: (function(*, *): string), requestAndLoadData: BillModule.requestAndLoadData, reloadCurrentRoomBill: BillModule.reloadCurrentRoomBill, loadData: BillModule.loadData, createBillOrderTitleHTML: (function(*, *): string)}}
 */
var BillModule = {
	currentLoadRoomId : undefined,
	customerName : undefined,
	customerNumber : undefined,
	billId : undefined,
	billAmount : undefined,
	billNumber : undefined,
	billOwnerId : undefined,
	billOwnerName : undefined,
	reloadCurrentRoomBill : function() {
		if (undefined != BillModule.currentLoadRoomId) {
			BillModule.requestAndLoadData(BillModule.currentLoadRoomId);
		}
	},
	requestAndLoadData : function (roomId) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mBill/billInfo",
			type: 'GET',
			cache: false,
			dataType:'json',
			contentType: "application/json; charset=utf-8",
			data: {
				roomId : roomId
			},
			success: function (data) {
				if (data.code == 0) {
					data = data.data;
					BillModule.currentLoadRoomId = roomId;
					BillModule.customerName = data.billCustomerName;
					BillModule.customerNumber = data.billCustomerNumber;
					BillModule.billId = data.billId;
					BillModule.billNumber = data.billNumber;
					BillModule.billOwnerId = data.billOwnerId;
					BillModule.billOwnerName = data.billOwnerName;
					BillModule.loadData(data);
				} else {
					swal("获取数据失败", data.desc, "error");
					$("#closeOrderFormWindowBtn").trigger("click"); //模拟点击关闭按钮
				}
			}
		});
	},
	loadData : function (data) {
		var amount = 0.0;
		// 加载就餐位信息
		$('#diningRoomNameText').text(data.billOwnerName);
		$('#billNumberText').text(data.billNumber);
		$('#customerNameText').text(data.billCustomerName);
		$('#createDateTimeText').text(GlobalMethod.toDateString(data.billStartDateTime));
		$('#customerNumberText').text(data.billCustomerNumber);
		$('#orderNumberText').text(data.billOrderQuantity);
		$('#billAmount').text();
		// 加载订单统览
		var target = $('#tab-orderList');
		target.html('');
		var orderList = data.orderList;
		for (var i=0; i<orderList.length; i++) {
			var order = orderList[i];
			var html = BillModule.createOrderUnitHTML(order, i+1);
			target.append(html);
		}
		// 加载账单统览
		var target = $('#tab-billItemList');
		target.html(BillModule.createBillTopTitleHTML());
		var orderList = data.orderList;
		var t=1;
		for (var i=0; i<orderList.length; i++) {
			var order = orderList[i];
			amount += order.amount;
			var html = BillModule.createBillOrderTitleHTML(order, i+1);
			var itemList = order.itemList;
			for (var j=0; j<itemList.length; j++) {
				var orderItem = itemList[j];
				if (4 == orderItem.orderProductItemStatusFlag) {
					continue;
				}
				html += BillModule.createBillItemUnitHTML(itemList[j], t++);
			}
			target.append(html);
		}
		// 账单总价
		BillModule.billAmount = amount;
		$('#billAmount').text(amount);
	},
	createBillOrderTitleHTML : function(order, index) {
		var html = '<div class="feed-element" style="margin-top:0px; padding-bottom: 0px; background:#D0E9C6; border-bottom: 1px solid;">' +
			'<i class="orderItemUnit" style="">#&nbsp;订单'+index+'&nbsp;('+order.diningTypeName+'&nbsp;'+order.orderPresetTime+')</i>' +
			'<i class="orderItemUnit" style="float:right; margin-right:5px;">总额:'+order.amount+'</i>' +
			'<i class="orderItemUnit" style="float:right; margin-right:10px;">'+GlobalMethod.toDateString(order.orderCreateDateTime)+'</i>' +
			'</div>';
		return html;
	},
	createBillItemUnitHTML : function (item, index) {
		var temp = (GlobalMethod.isEmpty(item.orderProductRemarks)? '' : ('(' + item.orderProductRemarks +')'));
		item.itemStatusName = toOrderItemStatusName(item.orderProductItemStatusFlag);
		item.itemStatusStyle = toOrderItemStatusNameStyle(item.orderProductItemStatusFlag);
		var html = '<div class="feed-element">' +
			'<i class="orderItemUnit" style="width:5%;">'+index+'</i>' +
			'<i class="orderItemUnit" style="width:7%;font-style: normal;">'+item.itemStatusStyle+'</i>' +
			'<i class="orderItemUnit" style="width:20%; font-weight:bolder; color:black; font-style: normal;">'+item.orderProductName+'&nbsp;'+temp+'</i>' +
			'<i class="orderItemUnit" style="width:7%;">'+item.orderProductPrice+'</i>' +
			'<i class="orderItemUnit" style="width:6%;">x'+item.orderProductQuantity+'</i>' +
			'<i class="orderItemUnit" style="width:10%;">'+item.amount+'</i>' +
			'<i class="orderItemUnit" style="width:45%;">' +
			'<i onclick="ItemStatusModule.readyToChangeStatus('+item.orderProductItemId+')" class="btn btn-white btn-sm" data-toggle="modal" data-target="#itemStatusPanel" ><i class="fa fa-tags"></i> 状态</i>' +
			'<i onclick="ItemModule.requestDelete('+item.orderProductItemId+', \''+item.orderProductName+'\')" class="btn btn-white btn-sm"><i class="fa fa-remove"></i> 删除</i>' +
			'<i onclick="ItemModule.readyChangeProduct('+item.orderProductItemId+')" class="btn btn-white btn-sm"  data-toggle="modal" data-target="#alertProductPanel"><i class="fa fa-retweet"></i> 替换</i>' +
			'<i onclick="ItemInfoUpdateModule.readyToUpdate('+item.orderProductItemId+', \''+item.orderProductName+'\', \''+item.itemStatusName+'\', \''+temp+'\', '+item.orderProductPrice+', '+item.orderProductQuantity+', '+item.amount+')" class="btn btn-white btn-sm" data-toggle="modal" data-target="#orderItemUpdatePanel"><i class="fa fa-pencil-square"></i> 修改</i>' +
			'</i>' +
			'</div>'
		return html;
	},
	createOrderUnitHTML : function (order, index) {
		order.diningTypeName = (order.orderTypeFlag == 0 ? '堂食' : '打包');
		order.fromTypeName = (order.orderInitiatorFlag == 0 ? '商家' : '顾客');
		var itemHtml = '';
		var list = order.itemList;
		var amount = 0.0;
		for (var i=0; i<list.length; i++) {
			itemHtml += BillModule.createOrderItemUnitHTML(list[i]);
			amount += list[i].amount;
		}
		order.amount = amount;
		var html = '<div style="border:1px solid; border-radius:10px; margin-bottom: 5px;">' +
			'<div>' +
			'<i class="orderItemUnit2" style="">订单'+index+'</i>' +
			'<i class="orderItemUnit2" style="">'+order.diningTypeName+' &nbsp; '+order.orderPresetTime+'</i>' +
			'<i class="orderItemUnit2" style="float:right; margin-right:15px;">总额:'+amount+'</i>' +
			'<i class="orderItemUnit2" style="float:right; margin-right:10px;">（'+order.fromTypeName+'）'+GlobalMethod.toDateString(order.orderCreateDateTime)+'</i>' +
			'</div>' +
			'<table class="table invoice-table" style="border-top:1px solid; margin-bottom: 0px;">' +
			'<thead>' +
			'<tr>' +
			'<th width="35%">名称</th>' +
			'<th width="20%">口味</th>' +
			'<th width="15%">数量</th>' +
			'<th width="15%">单价</th>' +
			'<th width="15%">小计</th>' +
			'</tr>' +
			'</thead>' +
			'<tbody>' + itemHtml +
			'</tbody>' +
			'</table>' +
			'</div>';
		return html;
	},
	createOrderItemUnitHTML : function (item) {
		var amount = item.orderProductPrice * item.orderProductQuantity;
		item.orderProductItemStatusDesc = toOrderItemStatusNameStyle(item.orderProductItemStatusFlag);
		item.orderProductRemarks = (item.orderProductRemarks == undefined ? '' : item.orderProductRemarks);
		item.amount = amount;
		var html = '<tr>' +
			'<td>' +
			'<strong>'+item.orderProductName+' &nbsp;</strong>' + '<span style="font-size:9px; ">'+item.orderProductItemStatusDesc+'</span>' +
			'</td>' +
			'<td>'+item.orderProductRemarks+'</td>' +
			'<td>'+item.orderProductQuantity+'</td>' +
			'<td>'+item.orderProductPrice+'</td>' +
			'<td>'+amount+'</td>' +
			'</tr>';
		return html;
	},
	createBillTopTitleHTML : function() {
		return '<div class="feed-element">\n' +
			'\t\t\t\t\t\t\t\t\t\t\t\t\t<i class="orderItemUnit" style="width:5%;">序号</i>\n' +
			'\t\t\t\t\t\t\t\t\t\t\t\t\t<i class="orderItemUnit" style="width:7%;">状态</i>\n' +
			'\t\t\t\t\t\t\t\t\t\t\t\t\t<i class="orderItemUnit" style="width:20%;">名称</i>\n' +
			'\t\t\t\t\t\t\t\t\t\t\t\t\t<i class="orderItemUnit" style="width:7%;">单价</i>\n' +
			'\t\t\t\t\t\t\t\t\t\t\t\t\t<i class="orderItemUnit" style="width:6%;">数量</i>\n' +
			'\t\t\t\t\t\t\t\t\t\t\t\t\t<i class="orderItemUnit" style="width:10%;">小计</i>\n' +
			'\t\t\t\t\t\t\t\t\t\t\t\t\t<i class="orderItemUnit" style="width:45%;">\n' +
			'\t\t\t\t\t\t\t\t\t\t\t\t\t\t操作&nbsp;&nbsp;\n' +
			'\t\t\t\t\t\t\t\t\t\t\t\t\t\t<i onclick="ItemAddModule.readyToAddItem()" class="btn btn-white btn-sm"  data-toggle="modal" data-target="#orderItemAddPanel" style="float:right;"><i class="fa fa-plus"></i> 新增 </i>\n' +
			'\t\t\t\t\t\t\t\t\t\t\t\t\t</i>\n' +
			'\t\t\t\t\t\t\t\t\t\t\t\t</div>';
	}
}

/**
 *
 * @type {{readyChangeProduct: ItemModule.readyChangeProduct, init: ItemModule.init, requestProductTypeData: (function(): Array), requestSetRead: ItemModule.requestSetRead, requestSetFinish: ItemModule.requestSetFinish, loadData: ItemModule.loadData, changeToProductId: undefined, requestDelete: ItemModule.requestDelete, requestProductData: (function(*): Array), changeProductAction: ItemModule.changeProductAction, createProductTypeUnitHTML: (function(*, *): string), createProductListUnitHTML: (function(*, *, *): string), requestChange: ItemModule.requestChange, readyChangeItemId: undefined, selectProduct: ItemModule.selectProduct}}
 */
var ItemModule = {
	changeToProductId : undefined,
	readyChangeItemId : undefined,
	init : function() {
		var typeList = ItemModule.requestProductTypeData();
		var productList = ItemModule.requestProductData();
		for (var i=0; i<typeList.length; i++) {
			var type = typeList[i];
			type.productList = [];
			for (var j=0; j<productList.length; j++) {
				var product = productList[j];
				if (product.productTypeId == type.productTypeId) {
					type.productList[type.productList.length] = product;
				}
			}
		}
		ItemModule.loadData(typeList);
		ItemSelectModule.loadData(typeList);
	},
	readyChangeProduct : function(itemId) {
		ItemModule.readyChangeItemId = itemId;
		if (undefined != ItemModule.changeToProductId) {
			$('#changeProductUnit'+ItemModule.changeToProductId).removeClass('selectColor');
			ItemModule.changeToProductId = undefined;
			$('#productSelectTip').text('');
		}
	},
	changeProductAction : function() {
		ItemModule.requestChange(ItemModule.readyChangeItemId, ItemModule.changeToProductId);
	},
	selectProduct : function(productId, productName) {
		$('#productSelectTip').text('已选择  '+productName);
		if (undefined != ItemModule.changeToProductId) {
			$('#changeProductUnit'+ItemModule.changeToProductId).removeClass('selectColor');
		}
		ItemModule.changeToProductId = productId;
		$('#changeProductUnit'+productId).addClass('selectColor');
	},
	requestSetRead : function () {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mOrder/read",
			type: 'POST',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				itemId : itemId
			},
			success: function (data) {
				if (data.code == 0) {
					//swal("替换成功", '', 'success');
				} else {
					swal("获取数据失败", data.desc, "error");
				}
			}
		});
	},
	requestSetFinish : function () {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mOrder/finish",
			type: 'POST',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				itemId : itemId
			},
			success: function (data) {
				if (data.code == 0) {
					//swal("替换成功", '', 'success');
				} else {
					swal("获取数据失败", data.desc, "error");
				}
			}
		});
	},
	requestDelete : function (itemId, productName) {
		swal({
				title: "您确定要删除 "+productName+" 吗?",
				text: "删除后将无法恢复，请谨慎操作！",
				type: "warning",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true
			},
			function(isConfirm) {
				if (isConfirm) {
					$.ajax({
						url: GlobalConfig.serverAddress + "/mOrder/delete",
						type: 'POST',
						cache: false,
						dataType:'json',
						contentType: "application/x-www-form-urlencoded;charset=utf-8",
						data: {
							itemId : itemId
						},
						success: function (data) {
							if (data.code == 0) {
								BillModule.reloadCurrentRoomBill();
								swal("删除成功", '', 'success');
							} else {
								swal("获取数据失败", data.desc, "error");
							}
						}
					});
				} else {
					swal("已取消", "您取消了删除操作！", "error");
				}
			});
	},
	requestChange : function (itemId, productId) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mOrder/change",
			type: 'POST',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				itemId : itemId,
				newProductId : productId
			},
			success: function (data) {
				if (data.code == 0) {
					BillModule.reloadCurrentRoomBill();
					swal("替换成功", '', 'success');
					$('#closeAlterProductPanelBtn').trigger('click');
				} else {
					swal("获取数据失败", data.desc, "error");
				}
			}
		});
	},
	loadData : function(data) {
		var typeDisplay = $('#productTypeDisplay');
		var productDisplay = $('#productDisplay');
		typeDisplay.html('');
		productDisplay.html('');
		for (var i=0; i<data.length; i++) {
			var productList = data[i].productList;
			var type = (i == 0 ? 'active' : '');
			typeDisplay.append(ItemModule.createProductTypeUnitHTML(data[i], type));
			productDisplay.append(ItemModule.createProductListUnitHTML(productList, data[i].productTypeId, type));
		}

	},
	createProductTypeUnitHTML : function (data, type) {
		var html = '<li class="'+type+'"><a data-toggle="tab" href="#tab-'+data.productTypeId+'" aria-expanded="true">'+data.productTypeName+'</a></li>';
		return html;
	},
	createProductListUnitHTML : function (productList, typeId, type) {
		var itemHtml = '';
		for (var i=0; i<productList.length; i++) {
			var product = productList[i];
			var temp = '<div id="changeProductUnit'+product.productId+'" onclick="ItemModule.selectProduct('+product.productId+', \''+product.productName+'\')" class="widget style1 lazur-bg selectProductUnit ">' +
			'<div class="row" style="height:17px;"><div class="col-xs-8">'+product.productName+'</div>' +
			'<div class="col-xs-4 text-right">'+product.productPrice+'</div></div></div>';
			itemHtml += temp;
		}
		var html = '<div id="tab-'+typeId+'" class="tab-pane '+type+'">' +
			'<div class="panel-body">' +
			itemHtml +
			'</div>' +
			'</div>';
		return html;
	},
	requestProductTypeData : function() {
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
					targetData = sortProductTypeList(targetData);
				} else {
					swal('获取品类数据失败', data.desc, 'error');
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
		return targetData;
	},
	requestProductData : function(tProductTypeId) {
		var targetData = [];
		$.ajax({
			url: GlobalConfig.serverAddress + "/mProduct/allValidProduct",
			type: 'GET',
			cache: false,
			dataType : 'json',
			async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			// contentType : 'application/json; charset=utf-8',
			data: {
			},
			success: function(data) {
				if (data.code != 0) {
					swal('获取商品信息失败', data.desc, 'error');
				} else {
					targetData = data.data;
					targetData = sortProductList(targetData);
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
		return targetData;
	},
}
/**
 * 商品选择模块
 * @type {{init: ItemSelectModule.init, createProductTypeUnitHTML: (function(*, *): string), loadProduct: ItemSelectModule.loadProduct, createProductListUnitHTML: (function(*, *, *): string), readyToSelectProduct: ItemSelectModule.readyToSelectProduct, productImgName: undefined, loadData: ItemSelectModule.loadData, selectedProductId: undefined, productName: undefined, productPrice: undefined, selectProduct: ItemSelectModule.selectProduct}}
 */
var ItemSelectModule = {
	selectedProductId : undefined,
	productName : undefined,
	productPrice : undefined,
	productImgName : undefined,
	init : function() {

	},
	loadProduct : function() {
		$('#closeAlterProductPanelBtn2').trigger('click');
		ItemAddModule.loadProduct(ItemSelectModule.selectedProductId, ItemSelectModule.productName, ItemSelectModule.productPrice);
	},
	loadData : function(data) {
		var typeDisplay = $('#productTypeDisplay2');
		var productDisplay = $('#productDisplay2');
		typeDisplay.html('');
		productDisplay.html('');
		for (var i=0; i<data.length; i++) {
			var productList = data[i].productList;
			var type = (i == 0 ? 'active' : '');
			typeDisplay.append(ItemSelectModule.createProductTypeUnitHTML(data[i], type));
			productDisplay.append(ItemSelectModule.createProductListUnitHTML(productList, data[i].productTypeId, type));
		}
	},
	readyToSelectProduct : function() {
		if (undefined != ItemSelectModule.selectedProductId) {
			$('#selectProductUnit'+ItemSelectModule.selectedProductId).removeClass('selectColor');
			$('#productSelectTip2').text('');
			ItemSelectModule.selectedProductId = undefined;
		}
	},
	selectProduct : function(productId, productName, productPrice) {
		$('#productSelectTip2').text('已选择  '+productName);
		if (undefined != ItemSelectModule.selectedProductId) {
			$('#selectProductUnit'+ItemSelectModule.selectedProductId).removeClass('selectColor');
		}
		ItemSelectModule.selectedProductId = productId;
		$('#selectProductUnit'+productId).addClass('selectColor');
		ItemSelectModule.productName = productName;
		ItemSelectModule.productPrice = productPrice;
		ItemSelectModule.productImgName = $('#selectProductUnit'+productId).attr('imgName');
	},
	createProductTypeUnitHTML : function (data, type) {
		var html = '<li class="'+type+'"><a data-toggle="tab" href="#tab2-'+data.productTypeId+'" aria-expanded="true">'+data.productTypeName+'</a></li>';
		return html;
	},
	createProductListUnitHTML : function (productList, typeId, type) {
		var itemHtml = '';
		for (var i=0; i<productList.length; i++) {
			var product = productList[i];
			var temp = '<div imgName="'+product.productImgName+'" id="selectProductUnit'+product.productId+'" onclick="ItemSelectModule.selectProduct('+product.productId+', \''+product.productName+'\', '+product.productPrice+')" class="widget style1 lazur-bg selectProductUnit ">' +
				'<div class="row" style="height:17px;"><div class="col-xs-8">'+product.productName+'</div>' +
				'<div class="col-xs-4 text-right">'+product.productPrice+'</div></div></div>';
			itemHtml += temp;
		}
		var html = '<div id="tab2-'+typeId+'" class="tab-pane '+type+'">' +
			'<div class="panel-body">' +
			itemHtml +
			'</div>' +
			'</div>';
		return html;
	}
}
/**
 * 订单项新增模块
 * @type {{addItem: ItemAddModule.addItem, readyToAddItem: ItemAddModule.readyToAddItem, loadProduct: ItemAddModule.loadProduct, packageData: ItemAddModule.packageData, requestAddItem: ItemAddModule.requestAddItem}}
 */
var ItemAddModule = {
	readyToAddItem : function () {
		ItemSelectModule.readyToSelectProduct();
		$('#addItemProductNameText').val('');
		$('#addItemProductAttributeNameText').val('');
		$('#addItemProductPriceText').val('');
		$('#addItemProductQuantityText').val('');
		$('#diningType1').removeAttr('checked');
		$('#diningType2').removeAttr('checked');
	},
	loadProduct : function (productId, productName, productPrice) {
		$('#addItemProductNameText').val(productName);
		$('#addItemProductAttributeNameText').val('');
		$('#addItemProductPriceText').val(productPrice);
		$('#addItemProductQuantityText').val('1');
		// $('#addItemAmountText').val();
	},
	packageData : function () {
		var data = {
			roomId : BillModule.currentLoadRoomId,
			productId : ItemSelectModule.selectedProductId,
			productName : $('#addItemProductNameText').val(),
			productPrice : $('#addItemProductPriceText').val(),
			productQuantity : $('#addItemProductQuantityText').val(),
			productRemarks : $('#addItemProductAttributeNameText').val(),
			productImgName : ItemSelectModule.productImgName
		}
		if ($('#diningType1').is(':checked')) {
			data.diningType = 0;
		} else if ($('#diningType2').is(':checked')) {
			data.diningType = 1;
		} else {
			swal('请选择就餐类型', '', 'error');
			return undefined;
		}
		if (GlobalMethod.isEmpty(data.productName)) {
			swal('商品名称不能为空', '', 'error');
			return undefined;
		}
		if (GlobalMethod.isEmpty(data.productPrice)) {
			swal('商品单价不能为空', '', 'error');
			return undefined;
		}
		if (GlobalMethod.isEmpty(data.productQuantity)) {
			swal('商品数量不能为空', '', 'error');
			return undefined;
		}
		return data;
	},
	addItem : function() {
		var data = ItemAddModule.packageData();
		console.log(data);
		if (undefined == data) {
			return;
		}
		ItemAddModule.requestAddItem(data);
		$('#closeAddItemPanelBtn').trigger('click');
	},
	requestAddItem : function (data) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mOrder/addItem",
			type: 'POST',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: data,
			success: function (data) {
				if (data.code == 0) {
					BillModule.reloadCurrentRoomBill();
					swal("新增成功", '', 'success');
				} else {
					swal("获取数据失败", data.desc, "error");
				}
			}
		});
	}
}
/**
 * 订单项修改模块
 * @type {{init: ItemInfoUpdateModule.init, updateItemInfo: ItemInfoUpdateModule.updateItemInfo, currentItemId: undefined, requestUpdate: ItemInfoUpdateModule.requestUpdate, readyToUpdate: ItemInfoUpdateModule.readyToUpdate}}
 */
var ItemInfoUpdateModule = {
	currentItemId : undefined,
	init : function() {
		
	},
	readyToUpdate : function (itemId, productName, itemStatus, productAttribute, productPrice, productQuantity, itemAmount) {
		ItemInfoUpdateModule.currentItemId = itemId;
		$('#updateItemInfo-productNameText').text(productName);
		$('#updateItemInfo-productAttributeNameText').text(productAttribute);
		$('#updateItemInfo-itemStatusName').text(itemStatus);
		$('#updateItemInfo-productPrice').text(productPrice);
		$('#updateItemInfo-newProductPrice').val('');
		$('#updateItemInfo-newProductPrice').attr('placeholder', productPrice);
		$('#updateItemInfo-productQuantity').text(productQuantity);
		$('#updateItemInfo-newProductQuantity').val('');
		$('#updateItemInfo-newProductQuantity').attr('placeholder', productQuantity);
		$('#updateItemInfo-itemAmount').text(itemAmount);
	},
	updateItemInfo : function() {
		var data = {
			itemId : ItemInfoUpdateModule.currentItemId,
			itemProductPrice : $('#updateItemInfo-newProductPrice').val(),
			itemProductQuantity : $('#updateItemInfo-newProductQuantity').val()
		}
		if (GlobalMethod.isEmpty(data.itemProductPrice) && GlobalMethod.isEmpty(data.itemProductQuantity)) {
			$('#closeOrderItemUpdatePanelBtn').trigger('click');
			return;
		}
		ItemInfoUpdateModule.requestUpdate(data.itemId, data.itemProductPrice, data.itemProductQuantity);
	},
	requestUpdate : function (itemId, price, quantity) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mOrder/update",
			type: 'POST',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				itemId : itemId,
				itemProductPrice : price,
				itemProductQuantity : quantity
			},
			success: function (data) {
				if (data.code == 0) {
					BillModule.reloadCurrentRoomBill();
					swal("修改成功", '', 'success');
					$('#closeOrderItemUpdatePanelBtn').trigger('click');
				} else {
					swal("获取数据失败", data.desc, "error");
				}
			}
		});
	}
}

/**
 * 订单项状态修改模块
 * @type {{init: ItemStatusModule.init, requestNewStatus: ItemStatusModule.requestNewStatus, readyToChangeStatus: ItemStatusModule.readyToChangeStatus, currentItemId: undefined, selectNewStatus: ItemStatusModule.selectNewStatus}}
 */
var ItemStatusModule = {
	currentItemId : undefined,
	init : function() {

	},
	readyToChangeStatus : function(itemId) {
		ItemStatusModule.currentItemId = itemId;
	},
	selectNewStatus : function(newStatus) {
		$('#closeItemStatusPanelBtn').trigger('click');
		switch(newStatus) {
			case 0 : newStatus = 'unRead'; break;
			case 1 : newStatus = 'failed'; break;
			case 2 : newStatus = 'read'; break;
			case 3 : newStatus = 'finish'; break;
			case 4 : newStatus = 'delete'; break;
			default : return;
		}
		ItemStatusModule.requestNewStatus(ItemStatusModule.currentItemId, newStatus);
	},
	requestNewStatus : function(itemId, newStatus) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mOrder/"+newStatus,
			type: 'POST',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				itemId : itemId
			},
			success: function (data) {
				if (data.code == 0) {
					BillModule.reloadCurrentRoomBill();
					swal("操作成功", '', 'success');
				} else {
					swal("获取数据失败", data.desc, "error");
				}
			}
		});
	}

}

/**
 * 账单简要信息修改模块
 * @type {{updateInfo: BillSimpleInfoModule.updateInfo, packageData: (function(): {billId: (undefined|*), customerNumber: (*|jQuery|*|*), customerName: (*|jQuery|*|*)}), requestUpdateInfo: BillSimpleInfoModule.requestUpdateInfo, loadCurrentBillInfo: BillSimpleInfoModule.loadCurrentBillInfo}}
 */
var BillSimpleInfoModule = {
	loadCurrentBillInfo : function () {
		var temp = (BillModule.customerName == undefined ? '' : BillModule.customerName);
		$('#updateCustomerNameText').val(temp);
		temp = (BillModule.customerNumber == undefined ? '' : BillModule.customerNumber);
		$('#updateCustomerNumberText').val(temp);
	},
	packageData : function() {
		var data = {
			customerName : $('#updateCustomerNameText').val(),
			customerNumber : $('#updateCustomerNumberText').val(),
			billId : BillModule.billId
		}
		return data;
	},
	updateInfo : function() {
		var data = BillSimpleInfoModule.packageData();
		BillSimpleInfoModule.requestUpdateInfo(data);
	},
	requestUpdateInfo : function (data) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mBill/updateSimpleInfo",
			type: 'POST',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: data,
			success: function (data) {
				if (data.code == 0) {
					BillModule.reloadCurrentRoomBill();
					$('#closeOrderInfUpdatePanelBtn').trigger('click');
					//swal("操作成功", '', 'success');
				} else {
					swal("获取数据失败", data.desc, "error");
				}
			}
		});
	}
}

/**
 * 账单结账模块
 * @type {{defaultRemarks: string[], init: BillSettleAccountModule.init, loadCurrentBill: BillSettleAccountModule.loadCurrentBill, selectDefaultRemarks: BillSettleAccountModule.selectDefaultRemarks, requestSettleAccount: BillSettleAccountModule.requestSettleAccount, remarks: string[], settleAccount: BillSettleAccountModule.settleAccount, loadDefaultRemarks: BillSettleAccountModule.loadDefaultRemarks}}
 */
var BillSettleAccountModule = {
	defaultRemarks : [
		'<a onclick="BillSettleAccountModule.selectDefaultRemarks(0)" class="btn btn-info btn-rounded" >商家请客</a>',
		'<a onclick="BillSettleAccountModule.selectDefaultRemarks(1)" class="btn btn-danger btn-rounded" >活动免单</a>'
	],
	remarks : [
		'商家请客',
		'活动免单'
	],
	init : function() {
		BillSettleAccountModule.loadDefaultRemarks();
	},
	loadCurrentBill : function() {
		$('#shouldPay').val(BillModule.billAmount);
		$('#actuallyPay').val('');
		$('#billRemarksArea').val('');
	},
	selectDefaultRemarks : function(index) {
		var temp = BillSettleAccountModule.remarks[index];
		$('#billRemarksArea').val(temp);
	},
	settleAccount : function(type) {
		var data = {
			billNumber : BillModule.billNumber,
			totalAmount : $('#shouldPay').val(),
			remarks : $('#billRemarksArea').val()
		}
		if (type == 0) {
			data.receiveAmount = $('#actuallyPay').val();
			if (GlobalMethod.isEmpty(data.receiveAmount)) {
				swal('实收金额不能为空', '', 'error');
				return;
			}
		}
		BillSettleAccountModule.requestSettleAccount(data);
	},
	requestSettleAccount : function (data) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mBill/settleAccount",
			type: 'POST',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: data,
			success: function (data) {
				if (data.code == 0) {
					$('#closeSettleAccountPanelBtn').trigger('click');
					//swal("", '', 'success');
				} else {
					swal("获取数据失败", data.desc, "error");
				}
			}
		});
	},
	loadDefaultRemarks : function () {
		var target = $('#defaultRemarksArea');
		var html = '';
		var list = BillSettleAccountModule.defaultRemarks;
		for (var i=0; i<list.length; i++) {
			html += list[i];
		}
		target.html(html);
	}
}

/**
 * 顾客消息模块
 * @type {{loadCurrentRoomMessage: MessageModule.loadCurrentRoomMessage, createMessageUnitHTML: (function(*): string), requestAndLoadData: MessageModule.requestAndLoadData, loadData: MessageModule.loadData}}
 */
var MessageModule = {
	currentRoomId : undefined,
	reloadCurrentRoomInfo : function() {
		MessageModule.requestAndLoadData(MessageModule.currentRoomId);
	},
	loadCurrentRoomMessage : function (t, n) {
		MessageModule.currentRoomId = t;
		MessageModule.requestAndLoadData(t);
		var temp = n + ' - 客戶消息';
		$('#customerMsgPanel-title').text(temp);
	},
	updateMessage : function(recordId) {
		MessageModule.requestSetStatus(recordId, 1);
	},
	loadData : function(data) {
		var target = $('#customerMessageArea');
		target.html('<thead >\n' +
			'\t\t\t\t\t\t\t\t<tr>\n' +
			'\t\t\t\t\t\t\t\t\t<td width="40%">类型</td>\n' +
			'\t\t\t\t\t\t\t\t\t<td width="20%">时间</td>\n' +
			'\t\t\t\t\t\t\t\t\t<td width="30%">操作</td>\n' +
			'\t\t\t\t\t\t\t\t</tr>\n' +
			'\t\t\t\t\t\t\t</thead>')
		for (var i=0; i<data.length; i++) {
			target.append(MessageModule.createMessageUnitHTML(data[i]));
		}
	},
	createMessageUnitHTML : function (item) {
		var html = '<tr class="gradeA even" style="background:white;">\n' +
			'\t\t\t\t\t\t\t\t<td class="sorting_1">'+item.customerMessageTypeName+'</td>\n' +
			'\t\t\t\t\t\t\t\t<td class=" ">'+GlobalMethod.toDateString(item.createDateTime)+'</td>\n' +
			'\t\t\t\t\t\t\t\t<td class=" ">\n' ;
			if (item.customerMessageStatus == 0) {
				html += '\t\t\t\t\t\t\t\t\t<i onclick="MessageModule.updateMessage('+item.customerMessageId+')" class="btn btn-white btn-sm"><i class="fa fa-dot-circle-o"></i> 确定 </i>\n';
			} else {
				html += ' 已确定 ';
			}
			html += '</td></tr>';
		return html;
	},
	requestAndLoadData : function (roomId) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mSystem/message",
			type: 'GET',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				roomId : roomId
			},
			success: function (data) {
				if (data.code == 0) {
					MessageModule.loadData(data.data);
				} else {
					swal("操作失败", data.desc, "error");
				}
			}
		});
	},
	requestSetStatus : function (recordId, status) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mSystem/set",
			type: 'POST',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				recordId : recordId,
				status : status
			},
			success: function (data) {
				if (data.code == 0) {
					MessageModule.reloadCurrentRoomInfo();
				} else {
					swal("操作失败", data.desc, "error");
				}
			}
		});
	},
	requestDeleteAllMessage : function () {
		swal({
				title: "您确定要清空所有客户消息吗?",
				text: "清空后将无法恢复，请谨慎操作！",
				type: "warning",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true
			},
			function(isConfirm) {
				if (isConfirm) {
					$.ajax({
						url: GlobalConfig.serverAddress + "/mSystem/delete",
						type: 'POST',
						cache: false,
						dataType:'json',
						contentType: "application/x-www-form-urlencoded;charset=utf-8",
						data: {
							roomId : MessageModule.currentRoomId
						},
						success: function (data) {
							if (data.code == 0) {
								MessageModule.reloadCurrentRoomInfo();
							} else {
								swal("操作失败", data.desc, "error");
							}
						}
					});
				} else {
					swal("已取消", "您取消了删除操作！", "error");
				}
			});

	}
}

/**
 * 就餐位状态编辑模块
 * @type {{loadData: DiningRoomStatusModule.loadData}}
 */
var DiningRoomStatusModule = {
	defaultStatus : [
		'<button class="btn btn-primary "  type="button" style="border: 0px solid;  background:#1DA02B; color:black !important;" > 空闲</button>',
		'<button class="btn btn-primary "  type="button" style="border: 0px solid;  background:#ECBA52; color:black !important;" > 使用中</button>',
		'<button class="btn btn-primary "  type="button" style="border: 0px solid;  background:#7266BA; color:black !important;" > 清理中</button>',
		'<button class="btn btn-primary "  type="button" style="border: 0px solid;  background:#ee162d; color:black !important;" > 禁用</button>'
	],
	selectBtnStyle : [
		'<button onclick="DiningRoomStatusModule.selectStatus(0)" class="btn btn-primary "  type="button" style="border: 0px solid;  background:#1DA02B; color:black !important; font-weight: bolder; width:60%; margin-bottom:8px; " > 空闲</button>',
		'<button onclick="DiningRoomStatusModule.selectStatus(1)" class="btn btn-primary "  type="button" style="border: 0px solid;  background:#ECBA52; color:black !important; font-weight: bolder; width:60%; margin-bottom:8px; " > 使用中</button>',
		'<button onclick="DiningRoomStatusModule.selectStatus(2)" class="btn btn-primary "  type="button" style="border: 0px solid;  background:#7266BA; color:black !important; font-weight: bolder; width:60%; margin-bottom:8px; " > 清理中</button>',
		'<button onclick="DiningRoomStatusModule.selectStatus(3)" class="btn btn-primary "  type="button" style="border: 0px solid;  background:#ee162d; color:black !important; font-weight: bolder; width:60%; margin-bottom:8px; " > 禁用</button>'
	],
	currentRoomId : undefined,
	init : function() {
		var target = $('#diningRoomStatusSelectArea');
		target.html('');
		var list = DiningRoomStatusModule.selectBtnStyle;
		for (var i=0; i<list.length; i++) {
			var html =  list[i] ;
			target.append(html);
		}
	},
	toDiningRoomStatusNameStyle : function(statusId) {
		if (statusId < 0 || statusId >= DiningRoomStatusModule.defaultStatus.length) {
			statusId = 3;
		}
		return DiningRoomStatusModule.defaultStatus[statusId];
	},
	readyToSelect : function (roomId) {
		DiningRoomStatusModule.currentRoomId = roomId;
	},
	selectStatus : function (statusId) {
		DiningRoomStatusModule.requestSetStatus(DiningRoomStatusModule.currentRoomId, statusId);
	},
	requestSetStatus : function (roomId, statusId) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mDiningRoom/changeStatus",
			type: 'POST',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				roomId : roomId,
				newStatusId : statusId
			},
			success: function (data) {
				if (data.code == 0) {
					$('#roomStatusStyleArea'+DiningRoomStatusModule.currentRoomId).html(DiningRoomStatusModule.toDiningRoomStatusNameStyle(statusId));
					$('#closeRoomStatusPanelBtn').trigger('click');
				} else {
					swal("获取数据失败", data.desc, "error");
				}
			}
		});
	}
}
/**
 * 就餐位的预定记录模块
 * @type {{loadRoomReservationInfo: DiningRoomReservationModule.loadRoomReservationInfo, readyToAdd: DiningRoomReservationModule.readyToAdd, reloadCurrentRoomInfo: DiningRoomReservationModule.reloadCurrentRoomInfo, requestDeleteReservation: DiningRoomReservationModule.requestDeleteReservation, requestRemoveRoomTip: DiningRoomReservationModule.requestRemoveRoomTip, currentRoomId: undefined, packageData: DiningRoomReservationModule.packageData, requestAddReservation: DiningRoomReservationModule.requestAddReservation, requestAndLoadData: DiningRoomReservationModule.requestAndLoadData, loadData: DiningRoomReservationModule.loadData, selectRecordOnTip: DiningRoomReservationModule.selectRecordOnTip}}
 */
var DiningRoomReservationModule = {
	currentRoomId : undefined,
	reloadCurrentRoomInfo : function() {
		if (undefined == DiningRoomReservationModule.currentRoomId) {
			return;
		}
		DiningRoomReservationModule.requestAndLoadData(DiningRoomReservationModule.currentRoomId);
	},
	loadRoomReservationInfo : function(roomId) {
		DiningRoomReservationModule.currentRoomId = roomId;
		DiningRoomReservationModule.requestAndLoadData(roomId);
	},
	loadData : function(data) {
		var html = '<thead><tr>' +
			'<td width="10%">序号</td>' +
			'<td width="15%">日期</td>' +
			'<td width="10%">餐点</td>' +
			'<td width="20%">预定人</td>' +
			'<td width="25%">' +
			'操作 ' +
			'<i onclick="DiningRoomReservationModule.readyToAdd()" class="btn btn-white btn-sm"  data-toggle="modal" data-target="#roomPreOrderAddPanel" style="float:right;"><i class="fa fa-plus-square-o"></i> 新增 </i>' +
			'</td></tr></thead>';
		var target = $('#reservationDisplayArea');
		target.html(html);
		for (var i=0; i<data.length; i++) {
			var item = data[i];
			html = '<tr class="gradeA" style="background:white;">' +
				'<td class="sorting_1">'+(i+1)+'</td>' +
				'<td class=" ">'+item.diningDate+'</td>' +
				'<td >'+item.diningTime+'</td>' +
				'<td >'+item.customerName+'</td>' +
				'<td class=" ">' +
				'<i onclick="DiningRoomReservationModule.selectRecordOnTip('+item.reservationId+', \''+item.diningDate+'\', \''+item.diningTime+'\', \''+item.customerName+'\')" class="btn btn-white btn-sm"><i class="fa fa-thumb-tack"></i> 标注 </i>' +
				'<i onclick="DiningRoomReservationModule.requestDeleteReservation('+item.reservationId+', \''+item.customerName+'\')" class="btn btn-white btn-sm"><i class="fa fa-trash-o"></i> 删除 </i>' +
				'</td></tr>';
			target.append(html);
		}
	},
	requestAndLoadData : function(roomId) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mSystem/reservationList",
			type: 'GET',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				roomId : roomId
			},
			success: function (data) {
				if (data.code == 0) {
					DiningRoomReservationModule.loadData(data.data);
				} else {
					swal("获取数据失败", data.desc, "error");
				}
			}
		});
	},
	readyToAdd : function () {
		//DiningRoomReservationModule.currentRoomId = roomId;
		$('#hello1').val('');
		$('#reservationDiningTimeArea').val(-1);
		$('#reservationCustomerNameArea').val('');
		$('#reservationCustomerInfoArea').val('');
	},
	packageData : function() {
		if ( -1 == $('#reservationDiningTimeArea').val()) {
			swal('请选择就餐时间', '', 'error');
			return undefined;
		}
		var data = {
			roomId : DiningRoomReservationModule.currentRoomId,
			diningDate : $('#hello1').val(),
			diningTime : $('#reservationDiningTimeArea option:selected').text(),
			customerName : $('#reservationCustomerNameArea').val(),
			reservationInfo : $('#reservationCustomerInfoArea').val()
		}
		if (GlobalMethod.isEmpty(data.reservationInfo)) {
			data.reservationInfo = ' ';
		}
		return data;
	},
	selectRecordOnTip : function(recordId, diningDate, diningTime, customerName) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mSystem/setOnTip",
			type: 'POST',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				recordId : recordId
			},
			success: function (data) {
				if (data.code == 0) {
					$('#reservationTip'+DiningRoomReservationModule.currentRoomId).text('预约: '+diningDate+'【'+diningTime+'】 '+customerName);
					swal('标记成功', '', 'success');
				} else {
					swal("操作失败", data.desc, "error");
				}
			}
		});
	},
	requestAddReservation : function () {
		var data = DiningRoomReservationModule.packageData();
		if (undefined == data) {
			return ;
		}
		$.ajax({
			url: GlobalConfig.serverAddress + "/mSystem/addReservation",
			type: 'POST',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: data,
			success: function (data) {
				if (data.code == 0) {
					$('#closeReservationAddPanelBtn').trigger('click');
					DiningRoomReservationModule.reloadCurrentRoomInfo();
					swal('新增预约成功', '', 'success');
				} else {
					swal("新增数据失败", data.desc, "error");
				}
			}
		});
	},
	requestDeleteReservation : function(recordId, customerName) {
		swal({
				title: "您确定要删除 "+customerName+" 的该条预约记录吗?",
				text: "删除后将无法恢复，请谨慎操作！",
				type: "warning",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true
			},
			function(isConfirm) {
				if (isConfirm) {
					$.ajax({
						url: GlobalConfig.serverAddress + "/mSystem/reservationRemove",
						type: 'POST',
						cache: false,
						dataType:'json',
						contentType: "application/x-www-form-urlencoded;charset=utf-8",
						data: {
							recordId : recordId
						},
						success: function (data) {
							if (data.code == 0) {
								swal('新增预约成功', '', 'success');
								DiningRoomReservationModule.reloadCurrentRoomInfo();
							} else {
								swal("删除失败", data.desc, "error");
							}
						}
					});
				} else {
					swal("已取消", "您取消了删除操作！", "error");
				}
			});
	},
	requestRemoveRoomTip : function(roomId, roomName) {
		swal({
				title: "您确定要删除 "+roomName+" 的预约标识吗?",
				text: "删除后将无法恢复，请谨慎操作！",
				type: "warning",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true
			},
			function(isConfirm) {
				if (isConfirm) {
					$.ajax({
						url: GlobalConfig.serverAddress + "/mSystem/removeRoomReservationTip",
						type: 'POST',
						cache: false,
						dataType:'json',
						contentType: "application/x-www-form-urlencoded;charset=utf-8",
						data: {
							roomId : roomId
						},
						success: function (data) {
							if (data.code == 0) {
								$('#reservationTip'+roomId).text('无预约');
							} else {
								swal("删除失败", data.desc, "error");
							}
						}
					});
				} else {
					swal("已取消", "您取消了删除操作！", "error");
				}
			});
	}
}

var NewsModule = {
	loadData : function(data) {
		if (undefined == data || undefined == data.length) {
			return;
		}
		$('.newsTip').hide();
		for (var i=0; i<data.length; i++) {
			var cell = data[i];
			var roomId = cell.roomId;
			if (undefined != cell.orderNewsNumber) {
				$('#orderItemNewsTip'+roomId).show();
				$('#orderItemNewsTip'+roomId).text(cell.orderNewsNumber);
			}
			if (undefined != cell.customerMessageNewsNumber) {
				$('#messageNewsTip'+roomId).show();
				$('#messageNewsTip'+roomId).text(cell.customerMessageNewsNumber)
			}
		}
	}
}

var QRCodeModule = {
	currentRoomId : undefined,
	refreshFlag : false,
	requestRoomCodeImgName : function(roomId) {
		QRCodeModule.currentRoomId = roomId;
		$.ajax({
			url: GlobalConfig.serverAddress + "/mSystem/positionQRcode",
			type: 'GET',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				roomId : roomId
			},
			success: function (data) {
				if (data.code == 0) {
					QRCodeModule.loadData(data.data);
				} else {
					swal("获取顾客就餐二维码失败", data.desc, "error");
				}
			}
		});
	},
	refreshQRCode : function() {
		if (true == QRCodeModule.refreshFlag) {
			swal('正在刷新中', '', 'error');
			return;
		}
		QRCodeModule.refreshFlag = true;
		var roomId = QRCodeModule.currentRoomId;
		$.ajax({
			url: GlobalConfig.serverAddress + "/mSystem/refreshPositionQRcode",
			type: 'POST',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				roomId : QRCodeModule.currentRoomId
			},
			success: function (data) {
				if (data.code == 0) {
					QRCodeModule.requestRoomCodeImgName(roomId);
					QRCodeModule.refreshFlag = false;
				} else {
					QRCodeModule.refreshFlag = false;
					swal("刷新顾客就餐二维码失败", data.desc, "error");
				}
			}
		});
	},
	loadData : function(data) {
		$('#positionCodeImg').attr('src', "/vita/resource/qrcodeImg/"+data);
	}
}











