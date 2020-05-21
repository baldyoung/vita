var url = "/Vita_war_exploded"
var currentorderId = undefined;

$(function() {
	// 创建一个分页条，并请求第3页
	PagingBarModule.build({
		pageOptionalAreaId: 'pagingBtnDisplayArea',
		totalAmountOfData: 100,
		maxNumberOfDisplayPageButton: 6, // 
		maxAmountOfOnePage: 6,
		//loadPageIndex : 3,

	});
	testFun();
	//requestorderList();
});

/**
 * 账单表格模块
 * @type {{init: BillTableModule.init, requestAndLoadData: BillTableModule.requestAndLoadData, loadData: BillTableModule.loadData}}
 */
var BillTableModule = {
	init : function() {

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
			'data': t, //表格的数据源
			'columns': [{
				data: 'orderId'
			}, {
				data: 'orderTypeName'
			}, {
				data: 'orderDiningTypeName'
			}, {
				data: 'orderTotalAmount'
			}, {
				data: 'orderReceivedAmount'
			}, {
				data: 'orderItemQuantity'
			}, {
				data: 'orderCustomerName'
			}, {
				data: "orderCreateDate"
			}],
			"columnDefs": [{
				"render": function(data, type, row) {
					var a = "";
					a += "<button class=\"btn btn-primary \" style='margin-top:3px;' type=\"button\" data-toggle=\"modal\" data-target=\"#OrderFormWindow\" onclick=\"deleteorder('" + row.id + "')\"><i class=\"fa fa-building-o\"></i>删除"
					a += "</button>"
					a += "<button class=\"btn btn-primary \" style='margin-top:3px; margin-left:5px; ' type=\"button\" data-toggle=\"modal\" data-target=\"#orderInfoPanel\"onclick=\"requestorderDList('" + row.id + "')\"><i class=\"fa fa-building-o\"></i>详情"
					a += "</button>"
					return a;
				},
				"targets": 8
			}]
		})
	},
	requestAndLoadData : function() {
		$.ajax({
			url: GlobalConfig.serverAddress + "",
			type: 'GET',
			cache: false,
			dataType: 'json',
			//async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: filterData,
			success: function(data) {
				if (data.code != 0) {
					swal('获取数据失败', data.desc, 'error');
				} else {

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
	init : function() {

	},
	reset : function() {

	},
	packageData : function() {

	}
}
/**
 * 账单统计模块
 * @type {{init: BillCountModule.init, requestAndLoadData: BillCountModule.requestAndLoadData, loadData: BillCountModule.loadData}}
 */
var BillCountModule = {
	init : function() {

	},
	loadData : function() {

	},
	requestAndLoadData : function() {

	}
}
/**
 * 账单详情模块
 * @type {{}}
 */
var BillInfoModule = {

}



//加载订单表格数据表格
function loadorderTable(t) {
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
		'data': t, //表格的数据源
		'columns': [{
			data: 'orderId'
		}, {
			data: 'orderTypeName'
		}, {
			data: 'orderDiningTypeName'
		}, {
			data: 'orderTotalAmount'
		}, {
			data: 'orderReceivedAmount'
		}, {
			data: 'orderItemQuantity'
		}, {
			data: 'orderCustomerName'
		}, {
			data: "orderCreateDate"
		}],
		"columnDefs": [{
			"render": function(data, type, row) {
				var a = "";
				a += "<button class=\"btn btn-primary \" style='margin-top:3px;' type=\"button\" data-toggle=\"modal\" data-target=\"#OrderFormWindow\" onclick=\"deleteorder('" + row.id + "')\"><i class=\"fa fa-building-o\"></i>删除"
				a += "</button>"
				a += "<button class=\"btn btn-primary \" style='margin-top:3px; margin-left:5px; ' type=\"button\" data-toggle=\"modal\" data-target=\"#orderInfoPanel\"onclick=\"requestorderDList('" + row.id + "')\"><i class=\"fa fa-building-o\"></i>详情"
				a += "</button>"
				return a;
			},
			"targets": 8
		}]

		//$('#OrderFormData_wrapper').css('padding-bottom', '0px');

	})

}
//请求数据，并加载到页面
function requestorderList() {

	$.ajax({
		url: url + "/queryAllOrderForm.action",
		type: 'post',
		cache: false,
		//dataType:'json',
		dataType: 'text',
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify({}),
		//data:temp,
		//processData: false,
		//contentType: false,
		success: function(data) {
			//将json字符串转换为json对象
			data = JSON.parse(data);
			//console.log(data);
			if (data.result == 'succeed') { //获取成功
				var temp = JSON.parse(data.inf); //将inf字段转换为json对象
				loadorderTable(temp) //将获取到的数据交给指定函数进行加载，显示在页面上

			} else if (data.result == 'default') { //获取失败
			}
		}
	});
}
//加载订单详细数据 数据表格
function loadorderDTable(t) {
	console.log(t);
	var dataTable = $('#orderDataDTable');
	if ($.fn.dataTable.isDataTable(dataTable)) {
		dataTable.DataTable().destroy();
	}
	dataTable.DataTable({
		'searching': true, //去掉搜索框
		'bLengthChange': true, //去掉每页显示多少条数据方法
		"serverSide": false, //关闭分页操作，默认就是关闭
		"autoWidth": false, //
		"bSort": true, //打开排序功能
		//"order": [[ 0, "desc" ]], //默认排序的指标
		'data': t, //表格的数据源
		'columns': [{
			data: 'id'
		}, {
			data: 'ofId'
		}, {
			data: 'pId'
		}, {
			data: 'pName'
		}, {
			data: 'pPrice'
		}, {
			data: 'pQuantity'
		}, {
			data: 'pAmount'
		}, {
			data: 'hasRead'
		}],
		"columnDefs": [{
			"render": function(data, type, row) {
				var a = "";
				//
				// a+="<button class=\"btn btn-primary \" style='margin-top:3px;' type=\"button\" data-toggle=\"modal\" data-target=\"#OrderFormWindow\" onclick=\"deleteorder('"+row.id+"')\"><i class=\"fa fa-building-o\"></i>删除"
				// a+="</button>"

				return a;
			},
			"targets": 8
		}]

	})

}
//请求数据，并加载到页面
function requestorderDList(id) {

	$.ajax({
		url: url + "/queryAllOrderFormData.action",
		type: 'get',
		cache: false,
		dataType: 'json',
		//dataType: 'text',
		contentType: "application/json; charset=utf-8",
		//data: JSON.stringify({id:t}),
		data: {
			id: id
		},
		//processData: false,
		//contentType: false,
		success: function(data) {
			//将json字符串转换为json对象
			//data = JSON.parse(data);
			//console.log(data);
			if (data.result == 'succeed') { //获取成功
				var temp = JSON.parse(data.inf); //将inf字段转换为json对象

				loadorderDTable(temp) //将获取到的数据交给指定函数进行加载，显示在页面上
			} else if (data.result == 'default') { //获取失败
			}
		}
	});
}

//删除指定订单
function deleteorder(t) {
	$.ajax({
		url: url + "/delOrderForm.action",
		type: 'get',
		cache: false,
		//dataType:'json',
		dataType: 'text',
		contentType: "application/json; charset=utf-8",
		//data: JSON.stringify({id:t}),
		data: {
			id: t
		},
		//processData: false,
		//contentType: false,
		success: function(data) {
			//将json字符串转换为json对象
			data = JSON.parse(data);
			//console.log(data);
			if (data.result == 'succeed') { //获取成功
				requestorderList();
			} else if (data.result == 'default') { //获取失败
			}
		}
	});
}

//加载订单的详细详细
function loadorderInf(t) {
	currentOrderFormId = t.id;
	$('#ofTimeText').val(t.ofTime);
	$('#userNameText').val(t.userName);
	$('#ofTypeIdText').val(t.ofTypeId);
	$('#ofTypeNameText').val(t.ofTypeName);
	$('#customerNumText').val(t.customerNum);
	$('#ofProQuantityText').val(t.ofProQuantity);
	$('#ofTotalAmountText').val(t.ofTotalAmount);
	$('#ofProceedsText').val(t.ofProceeds);
	$('#ofStateText').val(t.ofState);
	$('#customerRemarkText').val(t.customerRemark);
	$('#merchantRemarkText').val(t.merchantRemark);
}

//加载指定订单的信息
function openorderInfWin(tid, tofTime, tuserName, tofTypeId, tofTypeName, tcustomerNum, tofProQuantity, tofTotalAmount, tofProceeds, tofState, tcustomerRemark, tmerchantRemark) {
	var temp = {
		id: tid,
		ofTime: tofTime,
		userName: tuserName,
		ofTypeId: tofTypeId,
		ofTypeName: tofTypeName,
		customerNum: tcustomerNum,
		ofProQuantity: tofProQuantity,
		ofTotalAmount: tofTotalAmount,
		ofProceeds: tofProceeds,
		ofState: tofState,
		customerRemark: tcustomerRemark,
		merchantRemark: tmerchantRemark

	}

	loadorderInf(temp);
}

function testFun() {
	var temp = [{
		orderId: "10010",
		orderTypeName: "3号桌",
		orderDiningTypeId: "1",
		orderDiningTypeName: "午餐",
		orderTotalAmount: "335.00",
		orderReceivedAmount: "320.00",
		orderItemQuantity: "13",
		orderHandlerName: "李五",
		orderCustomerName : "梁院长（人民医院）15179798888",
		orderCreateDate: "2020-01-03",
		orderCreatteDateTime: "2020-01-03 12:33:35",
		orderFinishedDateTime: "2020-01-03 13:53:03"
	}, {
		orderId: "10011",
		orderTypeName: "1号桌",
		orderDiningTypeId: "1",
		orderDiningTypeName: "午餐",
		orderTotalAmount: "835.00",
		orderReceivedAmount: "未结账",
		orderItemQuantity: "15",
		orderHandlerName: "李五",
		orderCustomerName : "王校长（实验小学）15179799999",
		orderCreateDate: "2020-01-03",
		orderCreatteDateTime: "2020-01-03 12:33:35",
		orderFinishedDateTime: "2020-01-03 13:53:03"
	}];
	loadorderTable(temp);
}