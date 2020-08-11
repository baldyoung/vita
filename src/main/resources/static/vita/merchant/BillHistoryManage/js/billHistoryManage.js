//var url = "/Vita_war_exploded"
//var currentorderId = undefined;

$(function() {
	BillFilterModule.init();
	BillTableModule.init();
	BillCountModule.init();
	BillSettleAccountModule.init();
});

/**
 * 账单表格模块
 * @type {{init: BillTableModule.init, requestAndLoadData: BillTableModule.requestAndLoadData, loadData: BillTableModule.loadData}}
 */
var BillTableModule = {
	pageMaxDataSize : 20,
	init : function() {
		var filterData = BillFilterModule.packageData();
		var billNumber = BillTableModule.requestCountInfo(filterData);
		$('#pagingBtnDisplayArea').html('');
		BillTableModule.loadData([]);
		// 创建一个分页条，并请求第3页
		PagingBarModule.currentPageIndex = undefined;
		PagingBarModule.build({
			pageOptionalAreaId: 'pagingBtnDisplayArea',
			totalAmountOfData: billNumber,
			maxNumberOfDisplayPageButton: 6, //
			maxAmountOfOnePage: BillTableModule.pageMaxDataSize,
			run : BillTableModule.requestTargetPageBill
			//loadPageIndex : 3,
		});
	},
	reloadCurrentPageData : function() {
		var temp = PagingBarModule.currentPageIndex;
		PagingBarModule.currentPageIndex = undefined;
		PagingBarModule.loadTargetPage(temp);
	},
	loadData : function(data) {
		var dataTable = $('#orderTable');
		if ($.fn.dataTable.isDataTable(dataTable)) {
			dataTable.DataTable().destroy();
		}
		dataTable.DataTable({
			'searching': false, //去掉搜索框
			'bLengthChange': false, //去掉每页显示多少条数据方法
			"serverSide": false, //关闭分页操作，默认就是关闭
			"autoWidth": false, //
			"bSort": false, //打开排序功能
			"order": [
				[0, "desc"]
			], //默认排序的指标
			"paging" : false,
			"info" :false,
			"pageLength": 25, //默认每页显示的数据条数
			'data': data, //表格的数据源
			'columns': [{
				data: 'billId'
			}, {
				data: 'billOwnerName'
			}, {
				data: 'billTotalAmount'
			}, {
				data: 'billReceivedAmount'
			}, {
				data: 'billCustomerName'
			}, {
				data: "billStartDateTime"
			}],
			"columnDefs": [{
				"render": function(data, type, row) {
					var a = "";
					/*a += "<button class=\"btn btn-primary \" style='margin-top:3px;' type=\"button\" data-toggle=\"modal\" data-target=\"#OrderFormWindow\" onclick=\"deleteorder('" + row.id + "')\"><i class=\"fa fa-building-o\"></i>删除"
					a += "</button>"*/
					a += "<button class=\"btn btn-primary \" style='margin-top:3px; margin-left:5px; ' type=\"button\" data-toggle=\"modal\" data-target=\"#orderInfoPanel\"onclick=\"BillInfoModule.loadTargetBillInfo('" + row.billNumber + "')\"><i class=\"fa fa-building-o\"></i>详情"
					a += "</button>"
					return a;
				},
				"targets": 6
			}]
		})
	},
	requestTargetPageBill : function(param) {
		var filterData = BillFilterModule.packageData();
		if (undefined != param) {
			filterData.startIndex = (param.currentPageIndex - 1) * param.maxAmountOfData;
			filterData.maxSize = param.maxAmountOfData;
		} else {
			filterData.startIndex = 0;
			filterData.maxSize = param.maxAmountOfData;
		}
		BillTableModule.requestAndLoadData(filterData);
	},
	requestCountInfo : function(filterData) {
		var targetData = 0;
		$.ajax({
			url: GlobalConfig.serverAddress + "/mCompletedBill/billNumberWithCondition",
			type: 'POST',
			cache: false,
			dataType: 'json',
			async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: filterData,
			success: function(data) {
				if (data.code != 0) {
					swal('获取数据失败', data.desc, 'error');
				} else {
					targetData = data.data;
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
		return targetData;
	},
	requestAndLoadData : function(data) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mCompletedBill/billListWithCondition",
			type: 'POST',
			cache: false,
			dataType: 'json',
			//async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: data,
			success: function(data) {
				if (data.code != 0) {
					swal('获取数据失败', data.desc, 'error');
				} else {
					var list = data.data;
					for (var i=0; i<list.length; i++) {
						var cell = list[i];
						list[i].billStartDateTime = GlobalMethod.toDateString(list[i].billStartDateTime);
						if (undefined == cell.billOwnerName) {
							cell.billOwnerName = '?';
						}
						if (undefined == cell.billReceivedAmount) {
							cell.billReceivedAmount = '未结';
						}
						if (undefined == cell.billCustomerName) {
							cell.billCustomerName = ' ';
						}

					}
					BillTableModule.loadData(data.data);
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
	}
}
/**
 * 账单筛选模块
 * @type {{init: BillFilterModule.init, packageData: BillFilterModule.packageData, reset: BillFilterModule.reset}}
 */
var BillFilterModule = {
	diningRoomBuffer : [],
	search : function() {
		BillTableModule.init();
	},
	init : function() {
	},
	reset : function() {
		$('#filterDiningRoom').val('');
		if (!$('#filterNoZeroBill').hasClass("checked")) {
			$('#filterNoZeroBill').addClass("checked");
		}
		if (!$('#filterZeroBill').hasClass("checked")) {
			$('#filterZeroBill').addClass("checked");
		}
		if (!$('#filterUnReceiveBill').hasClass("checked")) {
			$('#filterUnReceiveBill').addClass("checked");
		}
	},
	packageData : function() {
		var data = {
			finishFlag : $('#filterNoZeroBill').hasClass("checked") ? 1 : 0,
			zeroFlag : $('#filterZeroBill').hasClass("checked") ? 1 : 0,
			unPay : $('#filterUnReceiveBill').hasClass("checked") ? 1 : 0
		}
		if (!GlobalMethod.isEmpty($('#filterDiningRoom').val())) {
			data.diningRoomName = $('#filterDiningRoom').val();
		}
		return data;
	},
	click : function(tId) {
		var target = $('#'+tId);
		if (target.hasClass("checked")) {
			target.removeClass("checked");
		} else {
			target.addClass("checked");
		}
	}
}
/**
 * 账单统计模块
 * @type {{init: BillCountModule.init, requestAndLoadData: BillCountModule.requestAndLoadData, loadData: BillCountModule.loadData}}
 */
var BillCountModule = {
	init : function() {
		BillCountModule.requestAndLoadData();
	},
	loadData : function(data) {
		if (undefined == data.totalSales) {
			data.totalSales = 0;
		}
		$('#accountBillNumberText').text(data.billTotalNumber);
		$('#accountZeroBillNumberText').text(data.zeroBillNumber);
		$('#accountUnReceiveBillNumberText').text(data.unPayBillNumber);
		$('#accountTotalSalesText').text(data.totalSales);
		$('#accountUnReceiveAmountText').text(data.totalUnReceive);
	},
	requestAndLoadData : function() {
		// ajax请求，并加载数据
		$.ajax({
			url: GlobalConfig.serverAddress + "/mCompletedBill/billCountInfo",
			type: 'GET',
			cache: false,
			dataType: 'json',
			//async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: null,
			success: function(data) {
				if (data.code != 0) {
					swal('获取账单统计数据失败', data.desc, 'error');
				} else {
					BillCountModule.loadData(data.data);
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
	}
}
/**
 * 账单详情模块
 * @type {{}}
 */
var BillInfoModule = {
	billBuffer : undefined,
	init : function() {

	},
	loadTargetBillInfo : function(tBillNumber) {
		BillInfoModule.requestAndLoadData(tBillNumber);
	},
	loadData : function(data) {
		BillInfoModule.billBuffer = data;
		data.billRemarks = (undefined == data.billRemarks ? '' : data.billRemarks);
		data.billReceivedDateTime = GlobalMethod.toDateString(data.billReceivedDateTime);
		data.billStartDateTime = GlobalMethod.toDateString(data.billStartDateTime);
		data.billEndDateTime = GlobalMethod.toDateString(data.billEndDateTime);
		$('#billNumberText').text(data.billNumber);
		$('#billCustomerNumberText').text(data.billCustomerNumber);
		$('#billOwnerNameText').text(data.billOwnerName);
		$('#billCustomerNameText').text(data.billCustomerName);
		$('#billTotalAmountText').text(data.billTotalAmount);
		$('#billRemarksText').val(data.billRemarks);
		if (undefined != data.billReceivedAmount) {
			$('#billReceivedAmountText').css('color', 'black');
			$('#billReceivedAmountText').text(data.billReceivedAmount);
			$('#billReceivedDateTimeText').text(data.billReceivedDateTime);
			$('#settleAmountBtn').hide();
		} else {
			$('#billReceivedAmountText').css('color', 'red');
			$('#billReceivedAmountText').text('未结账');
			$('#billReceivedDateTimeText').text('');
			$('#settleAmountBtn').show();
		}
		$('#billDateTimeText').text(data.billStartDateTime + " 至 " + data.billEndDateTime);
		$('#orderNumberText').text(data.orderList.length);

		var orderList = data.orderList;
		var target = $('#orderDisplayArea');
		target.html('');
		for (var i=0; i<orderList.length; i++) {
			target.append(BillInfoModule.createOrderUnitHTML(orderList[i], i+1));
		}
	},
	createOrderUnitHTML : function(order, index) {
		order.diningTypeName = (order.orderTypeFlag == 0 ? '堂食' : '打包');
		order.fromTypeName = (order.orderInitiatorFlag == 0 ? '商家' : '顾客');
		var itemHtml = '';
		var list = order.itemList;
		var amount = 0.0;
		for (var i=0; i<list.length; i++) {
			itemHtml += BillInfoModule.createOrderItemUnitHTML(list[i]);
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
	requestAndLoadData : function(tBillNumber) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mCompletedBill/orderListInBill",
			type: 'POST',
			cache: false,
			dataType: 'json',
			//async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				billNumber : tBillNumber
			},
			success: function(data) {
				if (data.code != 0) {
					swal('获取账单详情失败', data.desc, 'error');
				} else {
					BillInfoModule.loadData(data.data);
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
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
		$('#shouldPay').val(BillInfoModule.billBuffer.billTotalAmount);
		$('#actuallyPay').val('');
		$('#billRemarksArea').val('');
	},
	selectDefaultRemarks : function(index) {
		var temp = BillSettleAccountModule.remarks[index];
		$('#billRemarksArea').val(temp);
	},
	settleAccount : function(type) {
		var data = {
			billNumber : BillInfoModule.billBuffer.billNumber,
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
		swal({
				title: "您确定结账金额为 " + data.receiveAmount + " 吗？",
				text: "结账后不可更改，请谨慎操作！",
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
					BillSettleAccountModule.requestSettleAccount(data);
				} else {
					swal("已取消结账", "", "error");
				}
			});
	},
	requestSettleAccount : function (sendData) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mCompletedBill/settleAccount",
			type: 'POST',
			cache: false,
			dataType:'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: sendData,
			success: function (data) {
				if (data.code == 0) {
					$('#billReceivedAmountText').css('color', 'black');
					$('#billReceivedAmountText').text(sendData.receiveAmount);
					$('#billReceivedDateTimeText').text(GlobalMethod.getCurrentDateTime());
					$('#billRemarksText').val(sendData.remarks);
					$('#settleAmountBtn').hide();
					BillTableModule.reloadCurrentPageData();
					$('#closeSettleAccountPanelBtn').trigger('click');
					ShowTipModule.success("账单已完结")
					//swal("", '', 'success');
				} else {
					swal("结账操作失败", data.desc, "error");
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
 * 账单删除模块
 * @type {{}}
 */
var BillDeleteModule = {

}
/**
 * 提示模块
 * @type {{init: ShowTipModule.init, success: ShowTipModule.success}}
 */
var ShowTipModule = {
	init : function() {

	},
	success : function(data) {
		toastr.options = {
			"closeButton": true,
			"debug": false,
			"progressBar": false,
			"positionClass": "toast-top-left",
			"onclick": null,
			"showDuration": "400",
			"hideDuration": "1000",
			"timeOut": "1500",
			"extendedTimeOut": "1000",
			"showEasing": "swing",
			"hideEasing": "linear",
			"showMethod": "fadeIn",
			"hideMethod": "fadeOut"
		}
		toastr.success(data);
	},
	warning : function(data) {
		toastr.options = {
			"closeButton": true,
			"debug": false,
			"progressBar": false,
			"positionClass": "toast-top-right",
			"onclick": null,
			"showDuration": "400",
			"hideDuration": "1000",
			"timeOut": "1500",
			"extendedTimeOut": "1000",
			"showEasing": "swing",
			"hideEasing": "linear",
			"showMethod": "fadeIn",
			"hideMethod": "fadeOut"
		}
		toastr.warning(data);
	},
	error : function(data) {
		toastr.options = {
			"closeButton": true,
			"debug": false,
			"progressBar": false,
			"positionClass": "toast-top-center",
			"onclick": null,
			"showDuration": "400",
			"hideDuration": "1000",
			"timeOut": "1500",
			"extendedTimeOut": "1000",
			"showEasing": "swing",
			"hideEasing": "linear",
			"showMethod": "fadeIn",
			"hideMethod": "fadeOut"
		}
		toastr.error(data);
	}

}

/*****************************************************************************************************************/
