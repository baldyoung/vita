

$(document).ready(function() {
	DiningRoomModule.init();
});
var DiningRoomModule = {
	diningRoomBuffer : [],
	getDiningRoomInfo : function(roomId) {
		var list = DiningRoomModule.diningRoomBuffer;
		for (var i=0; i<list.length; i++) {
			if (list[i].diningRoomId == roomId) {
				return list[i];
			}
		}
		return undefined;
	},
	init : function () {
		DiningRoomModule.requestAndLoadData();
	},
	loadData : function (data) {
		DiningRoomModule.diningRoomBuffer = data;
		var dataTable = $('#OrderTypeTable');
		if ($.fn.dataTable.isDataTable(dataTable)) {
			dataTable.DataTable().destroy();
		}
		for (var i=0; i<data.length; i++) {
			var cell = data[i];
			cell.simpleInfo = GlobalMethod.toSimpleString(cell.diningRoomInfo, 10);
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
			'data': data, //表格的数据源
			'columns': [{
				data: 'diningRoomId'
			}, {
				data: 'diningRoomGrade'
			}, {
				data: 'diningRoomName'
			}, {
				data: 'simpleInfo'
			}],
			"columnDefs": [{
				"render": function(data, type, row) {
					var a = "";
					a += "<button class=\"btn btn-primary \" style='margin-top:3px;' type=\"button\" onclick=\"DeleteDiningRoomModule.deleteAction('" + row.diningRoomId + "', '"+row.diningRoomName+"')\"><i class=\"fa fa-building-o\"></i>删除";
					a += "</button>";
					a += "<button class=\"btn btn-primary \" style='margin-top:3px; margin-left:5px; ' type=\"button\" data-toggle=\"modal\" data-target=\"#alertOrderTypePanel\" onclick=\"UpdateDiningRoomModule.loadTargetRoom('" + row.diningRoomId + "')\"><i class=\"fa fa-building-o\"></i>修改";
					a += "</button>";
					return a;
				},
				"targets": 4
			}]
		});
	},
	requestAndLoadData : function () {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mDiningRoom/diningRoomList",
			type: 'GET',
			cache: false,
			dataType: 'json',
			//async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: null,
			success: function(data) {
				if (data.code != 0) {
					swal('获取数据失败', data.desc, 'error');
				} else {
					DiningRoomModule.loadData(data.data);
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
	}
}

var AddDiningRoomModule = {
	readyToAction : function () {
		$('#newDiningRoomInfoText').val('');
		$("#newDiningRoomNameText").val('');
		$('#newDiningRoomGradeText').val('');
	},
	packageData : function () {
		var data = {
			roomName : $('#newDiningRoomNameText').val(),
			roomGrade : $('#newDiningRoomGradeText').val(),
			roomInfo : $('#newDiningRoomInfoText').val()
		}
		if (GlobalMethod.isEmpty(data.roomName)) {
			swal('就餐位名称不能为空', '', 'error');
			return undefined;
		}
		if (GlobalMethod.isEmpty(data.roomInfo)) {
			data.roomInfo = ' ';
		}
		if (GlobalMethod.isEmpty(data.roomGrade)) {
			data.roomGrade = 0;
		}
		return data;
	},
	addAction : function () {
		var data = AddDiningRoomModule.packageData();
		if (undefined == data) {
			return;
		}
		AddDiningRoomModule.sendData(data);
	},
	sendData : function (data) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mDiningRoom/addDiningRoom",
			type: 'POST',
			cache: false,
			dataType: 'json',
			//async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: data,
			success: function(data) {
				if (data.code != 0) {
					swal('新增失败', data.desc, 'error');
				} else {
					$('#closeAddWindowBtn').trigger('click');
					swal('新增成功', '', 'success');
					DiningRoomModule.init();
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
	}
}

var DeleteDiningRoomModule = {
	deleteAction : function (roomId, roomName) {
		swal({
				title: "您确定要删除 " + roomName + " 吗?",
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
					DeleteDiningRoomModule.sendData(roomId);
				} else {
					swal("已取消", "您取消了删除操作！", "error");
				}
			});
	},
	sendData : function (roomId) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mDiningRoom/deleteDiningRoom",
			type: 'POST',
			cache: false,
			dataType: 'json',
			//async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				roomId : roomId
			},
			success: function(data) {
				if (data.code != 0) {
					swal('删除失败', data.desc, 'error');
				} else {
					DiningRoomModule.init();
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
	}
}

var UpdateDiningRoomModule = {
	currentRoomId : undefined,
	loadTargetRoom : function (roomId) {
		UpdateDiningRoomModule.currentRoomId = roomId;
		var room = DiningRoomModule.getDiningRoomInfo(roomId);
		$('#diningRoomNameText').val(room.diningRoomName);
		$('#diningRoomGradeText').val(room.diningRoomGrade);
		$('#diningRoomInfoText').val(room.diningRoomInfo);
	},
	packageData : function () {
		var data = {
			roomId : UpdateDiningRoomModule.currentRoomId,
			roomName : $('#diningRoomNameText').val(),
			roomGrade : $('#diningRoomGradeText').val(),
			roomInfo : $('#diningRoomInfoText').val()
		}
		if (GlobalMethod.isEmpty(data.roomName)) {
			swal('名称不能为空', '', 'error');
			return undefined;
		}
		return data;
	},
	updateAction : function () {
		var data = UpdateDiningRoomModule.packageData();
		if (undefined == data) {
			return;
		}
		UpdateDiningRoomModule.sendData(data);
	},
	sendData : function (data) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mDiningRoom/updateDiningRoom",
			type: 'POST',
			cache: false,
			dataType: 'json',
			//async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: data,
			success: function(data) {
				if (data.code != 0) {
					swal('修改失败', data.desc, 'error');
				} else {
					$('#btnCloseUpdateWindow').trigger('click');
					DiningRoomModule.init();
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
	}
}










