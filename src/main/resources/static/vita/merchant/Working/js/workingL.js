

$(function(){
	console.log("workingL.js content...");
	registerMonitor();
	RoomModule.init();
	
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
		var  html = '<div id="Room3" class="col-sm-4 roomPanel" style="width:480px; height:300px;">' +
			'<div class="ibox roomPanel" style=" ">' +
			'<div class="ibox-title roomPanelTop">' +
			'<span class="pull-left" style="cursor:pointer; font-size:10px;">' +
			'<span class="glyphicon glyphicon-qrcode" aria-hidden="true" data-toggle="modal" data-target="#qrCodePanel"></span>' +
			'</span>' +
			'<span class="label label-warning pull-right roomStatusLabel">已用</span>' +
			'<h5>' +
			'<span class="pull-left">&nbsp;&nbsp;'+item.diningRoomName+'&nbsp;&nbsp;-&nbsp;&nbsp;</span>' +
			'</h5>' +
			'</div>' +
			'<div class="ibox-content roomPanelBottom">' +
			'<div class="col-sm-12 roomOptionalArea2">' +
			'<div class="col-sm-5" style="padding: 0 0 0 0;">' +
			'<div class="team-members roomOptionalArea">' +
			'<button onclick="BillModule.requestAndLoadData('+item.diningRoomId+')" class="btn btn-info " type="button" data-toggle="modal" data-target="#OrderFormWindow" style="margin-bottom:5px; " onclick="">' +
			'<i class="fa fa-paste"></i> 订单详情' +
			'<span class="badge badge-danger" style="background:#ED5565; color:black;  display:;    ">10</span>' +
			'</button>' +
			'<button class="btn btn-primary " data-toggle="modal" data-target="#customerMsgPanel" type="button" style="margin-bottom:5px; " onclick="">' +
			'<i class="glyphicon glyphicon-envelope"></i> 客户消息' +
			'<span class="badge badge-danger" style="background:#ED5565; color:black; display:;    ">3</span>' +
			'</button>' +
			'<button class="btn btn-primary " data-toggle="modal" data-target="#roomPreOrderHistoryPanel" type="button" style="margin-bottom:5px; background:#e0a9f9; " onclick="">' +
			'<i class="glyphicon glyphicon-tag"></i> 预定记录' +
			'<span class="badge badge-danger" style="background:#ED5565; color:black;  display:;    ">6</span>' +
			'</button>' +
			'<button class="btn btn-primary " data-toggle="modal" data-target="#roomStatusPanel" type="button" style="margin-bottom:5px; " onclick="">' +
			'<i class="glyphicon glyphicon-edit"></i> 修改状态' +
			'</button>' +
			'</div>' +
			'</div>' +
			'<div class="col-sm-7" style="padding: 0 0 0 0;">' +
			'<span class="label label-warning pull-left roomStatusLabel" style="background:#F4C20B; width:100%;">' +
			'<div class="roomPanelPreTip">2020-02-09&nbsp;【晚餐】</div>' +
			'<div class="roomPanelPreTip">刘先生15179798118</div>' +
			'</span>' +
			'<textarea style="resize:none; height:100px; width:100%; border:0px solid; margin-bottom: 5px; cursor:pointer; " readonly="true">'+item.diningRoomInfo+'</textarea>' +
			'</div>' +
			'</div>' +
			'<div class="row  m-t-sm">' +
			'<div class="col-sm-4">' +
			'<div class="font-bold">客人数量</div>' +
			'<div id="RoomPeopleNum3">未知</div>' +
			'</div>' +
			'<div class="col-sm-4">' +
			'<div class="font-bold">商品数量</div>' +
			'<div id="RoomProNum3">未知</div>' +
			'</div>' +
			'<div class="col-sm-4 text-right">' +
			'<div class="font-bold">账单预算</div>' +
			'<div id="RoomTotalAmount3">未知</div>' +
			'</div>' +
			'</div>' +
			'</div>' +
			'</div>' +
			'</div>';
		return html;
	}
}


var BillModule = {
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
					BillModule.loadData(data);
				} else {
					swal("获取数据失败", data.desc, "error");
					$("#closeOrderFormWindowBtn").trigger("click"); //模拟点击关闭按钮
				}
			}
		});
	},
	loadData : function (data) {
		// 加载就餐位信息
		$('#diningRoomNameText').text(data.billOwnerName);
		$('#billNumberText').text(data.billNumber);
		$('#customerNameText').text(data.billCustomerName);
		$('#createDateTimeText').text(GlobalMethod.toDateString(data.billStartDateTime));
		$('#customerNumberText').text(data.billCustomerNumber);
		$('#orderNumberText').text(data.billOrderQuantity);
		$('#billAmount').text();
		// 加载账单统览
		var target = $('#tab-1');
		//target.html('');
		var orderList = data.orderList;
		/*for (var i=0; i<orderList.length; i++) {
			var order = orderList[i];
			var html = BillModule.createOrderUnitHTML(order, i+1);
			target.append(html);
		}*/
		// 加载订单统览
		target = $('#tab-2');
		target.html('');
		orderList = data.orderList;
		for (var i=0; i<orderList.length; i++) {
			var order = orderList[i];
			var html = BillModule.createOrderUnitHTML(order, i+1);
			target.append(html);
		}
	},
	createBillOrderTitleHTML : function() {
		var html = '<div class="feed-element" style="margin-top:0px; padding-bottom: 0px; background:#D0E9C6;">' +
			'<i class="orderItemUnit" style="width:20%;">#&nbsp;订单1&nbsp;(堂食 &nbsp; 12:32)</i>' +
			'<i class="orderItemUnit" style="float:right; margin-right:5px;">总额:233.00</i>' +
			'<i class="orderItemUnit" style="float:right; margin-right:10px;">2020-02-03 12:32:33</i>' +
			'</div>';
		return html;
	},
	createBillItemUnitHTML : function (item) {


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
		item.orderProductItemStatusDesc = '';
		item.amount = amount;
		var html = '<tr>' +
			'<td>' +
			'<strong>'+item.orderProductName+'</strong>' + '<span style="font-size:9px; color:#DE0B07;">'+item.orderProductItemStatusDesc+'</span>' +
			'</td>' +
			'<td>'+item.orderProductRemarks+'</td>' +
			'<td>'+item.orderProductQuantity+'</td>' +
			'<td>'+item.orderProductPrice+'</td>' +
			'<td>'+amount+'</td>' +
			'</tr>';
		return html;
	}
}

















