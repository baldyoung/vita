var url = "/Vita_war_exploded"
var currentOrderTypeId = undefined;
$(document).ready(function() {

	testFun();
	//requestOrderTypeList();
});

//加载用户数据表格
function loadOrderTypeTable(t) {
	//console.log('loadOrderTypeTable...,')
	var dataTable = $('#OrderTypeTable');
	if ($.fn.dataTable.isDataTable(dataTable)) {
		dataTable.DataTable().destroy();
	}
	dataTable.DataTable({
		'searching': true, //去掉搜索框
		'bLengthChange': true, //去掉每页显示多少条数据方法
		"serverSide": false, //关闭分页操作，默认就是关闭
		"autoWidth": false, //
		"bSort": true, //打开排序功能
		"order": [
			[1, "desc"],
			[0, 'asc']
		], //默认排序的指标
		"pageLength": 25, //默认每页显示的数据条数
		'data': t, //表格的数据源
		'columns': [{
			data: 'orderTypeId'
		}, {
			data: 'orderTypeGrade'
		}, {
			data: 'orderTypeName'
		}, {
			data: 'orderTypeInfo'
		}],
		"columnDefs": [{
				"render": function(data, type, row) {
					var a = "";
					a += "<button class=\"btn btn-primary \" style='margin-top:3px;' type=\"button\" onclick=\"deleteOrderType('" + row.id + "')\"><i class=\"fa fa-building-o\"></i>删除";
					a += "</button>";
					a += "<button class=\"btn btn-primary \" style='margin-top:3px; margin-left:5px; ' type=\"button\" data-toggle=\"modal\" data-target=\"#alertOrderTypePanel\" onclick=\"openOrderTypeInfWin('" + row.id + "','" + row.grade + "','" + row.name + "',  '" + row.limitNum + "','" + row.maxPeopleNum + "','" + row.oftRemark + "' )\"><i class=\"fa fa-building-o\"></i>修改";
					a += "</button>";
					return a;
				},
				"targets": 4
			}]
			//$('#OrderFormData_wrapper').css('padding-bottom', '0px');

	})

}
//请求数据，并加载到页面
function requestOrderTypeList() {
	$.ajax({
		url: url + "/queryAllOrderFormType.action",
		type: 'POST',
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
				loadOrderTypeTable(temp) //将获取到的数据交给指定函数进行加载，显示在页面上

			} else if (data.result == 'default') { //获取失败
			}
		}
	});
}

//删除指定订单类型
function deleteOrderType(t) {
	swal({
			title: "您确定要删除吗？",
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
			if (!isConfirm) {
				return;
			} 
		});
	$.ajax({
		url: url + "/delOrderFormType.action",
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
				requestOrderTypeList();
				swal("删除成功！", "您已经永久删除了这条信息。", "success");
			} else if (data.result == 'default') { //获取失败
			}
		}
	});
}

//加载订单类型的详细详细
function loadOrderTypeInf(t) {
	currentOrderTypeId = t.id;
	$('#idText').val(t.id);
	$('#gradeText').val(t.grade);
	$('#NAMEText').val(t.name);
	$('#limitNumText').val(t.limitNum);
	$('#maxPeopleNumText').val(t.maxPeopleNum);
	$('#oftRemarkText').val(t.oftRemark);
}

//重置订单类型详细窗口
function resetOrderTypeInf() {
	$('#idText').val('');
	$('#gradeText').val('');
	$('#NAMEText').val('');
	$('#limitNumText').val('');
	$('#maxPeopleNumText').val('');
	$('#oftRemarkText').val('');
}
//加载指定订单类型的信息
function openOrderTypeInfWin(tid, tgrade, tNAME, tlimitNum, tmaxPeopleNum, toftRemark) {
	var temp = {
			id: tid,
			grade: tgrade,
			name: tNAME,
			limitNum: tlimitNum,
			maxPeopleNum: tmaxPeopleNum,
			oftRemark: toftRemark

		}
		//currentOrderTypeId=tid;
	$('#myModal_title').text('订单类型信息-修改')
	loadOrderTypeInf(temp);
}

//保存订单类型信息
function saveOrderTypeInf() {
	var temp = {
		id: $('#idText').val(),
		grade: $('#gradeText').val(),
		name: $('#NAMEText').val(),
		limitNum: $('#limitNumText').val(),
		maxPeopleNum: 0, //$('#maxPeopleNumText').val(),
		oftRemark: $('#oftRemarkText').val()
	}

	if (currentOrderTypeId != undefined)
		temp.id = currentOrderTypeId;
	var tip = check(temp);
	if (true != tip) { //格式不正确
		showTip(tip);
	} else {
		postOrderTypeInf(temp)
	}
}
//修改订单信息
function postOrderTypeInf(t) {
	$.ajax({
		url: url + "/updateOrderFormType.action",
		type: 'get',
		cache: false,
		dataType: 'json',
		//dataType: 'text',
		contentType: "application/json; charset=utf-8",
		//data: JSON.stringify(t),
		data: t,
		//processData: false,
		//contentType: false,
		success: function(data) {
			//将json字符串转换为json对象
			//data = JSON.parse(data);
			//console.log(data);
			if (data.result == 'succeed') { //获取成功
				requestOrderTypeList();
				swal("操作成功", "")
				$('#btnSaveInfWin').trigger('click');
			} else if (data.result == 'default') { //获取失败
				swal("操作失败", data.inf);
			}
		}
	});
}

//准备添加新订单类型
function readForAddOrderType() {
	console.log("read");
	currentOrderTypeId = undefined;
	$('#myModal_title1').text('订单类型信息-新增');
	$('#addidText').val('');
	$('#addgradeText').val('66');
	$('#addNAMEText').val('');
	$('#addlimitNumText').val('1');
	$('#addmaxPeopleNumText').val('12');
	$('#oftRemarkText').val('');
}
///添加订单类型信息
function addOrderTypeInf() {

	$.ajax({
		url: url + "/addOrderFormType.action",
		type: 'get',
		cache: false,
		dataType: 'json',
		//dataType: 'text',
		contentType: "application/json; charset=utf-8",
		// data: JSON.stringify(t),
		data: {
			grade: $('#addgradeText').val(),
			name: $('#addNAMEText').val(),
			limitNum: $('#addlimitNumText').val(),
			maxPeopleNum: 0, //$('#addmaxPeopleNumText').val(),
			oftRemark: $('#addoftRemarkText').val()
		},
		//processData: false,
		//contentType: false,
		success: function(data) {
			//将json字符串转换为json对象
			//data = JSON.parse(data);
			//console.log(data);
			if (data.result == 'succeed') { //获取成功
				swal("操作成功", "");
				$('#btnCloseAddWin').trigger('click');
				requestOrderTypeList();
				resetOrderTypeInf();

			} else if (data.result == 'default') { //获取失败
				swal("操作失败", data.inf);
			}
		}
	});
}
////检查指定的json对象的格式是否正确
function check(t) {
	if (undefined == t || undefined == t.grade || undefined == t.name || undefined == t.limitNum) return undefined;
	if (t.grade.length <= 0 || t.grade.length >= 65535) return '订单类型等级需要大于0小于65535个字符';
	if (t.name.length >= 50) return '用户名称需要小于50个字符';
	if (t.limitNum.length <= 0 || t.limitNum.length >= 65535) return '最大订单数需要大于0小于65535个字符';
	if (t.maxPeopleNum.length <= 0 || t.maxPeopleNum.length >= 65535) return '最大顾客人数需要大于0小于65535个字符';
	if (t.name.length >= 200) return '备注需要小于200个字符';
	if (t.name == '') return '信息不能留空';

	return true;
}
//提示信息
function showTip(t) {
	toastr.options = {
		"closeButton": true,
		"debug": false,
		"progressBar": true,
		"positionClass": "toast-top-center",
		"onclick": null,
		"showDuration": "400",
		"hideDuration": "1000",
		"timeOut": "7000",
		"extendedTimeOut": "1000",
		"showEasing": "swing",
		"hideEasing": "linear",
		"showMethod": "fadeIn",
		"hideMethod": "fadeOut"
	}
	toastr.warning('' + t);

}

function testFun() {

	var temp = [{
		orderTypeId: "10010",
		orderTypeName: "一号桌",
		orderTypeGrade: "1",
		orderTypeInfo: "餐桌标准用餐人数16， 最大用餐人数24人。设备：空调、麻将桌、独立卫生间。"
	}, {
		orderTypeId: "10011",
		orderTypeName: "二号桌",
		orderTypeGrade: "2",
		orderTypeInfo: "餐桌标准用餐人数16， 最大用餐人数24人。设备：空调、麻将桌、独立卫生间。"
	}, {
		orderTypeId: "10013",
		orderTypeName: "三号桌",
		orderTypeGrade: "3",
		orderTypeInfo: "餐桌标准用餐人数16， 最大用餐人数24人。设备：空调、麻将桌、独立卫生间。"
	}]
	loadOrderTypeTable(temp);
}