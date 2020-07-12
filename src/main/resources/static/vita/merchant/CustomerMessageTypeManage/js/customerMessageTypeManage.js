$(function() {
	CustomerMessageModule.init();
});

var CustomerMessageModule = {
	currentEditCellId: undefined,
	currentEditCellName : undefined,
	init: function() {
		var data = CustomerMessageModule.requestMessageList();
		CustomerMessageModule.loadMessageList(data);
	},
	commitEditData : function() {
		var data = CustomerMessageModule.packageEditAreaData();
		CustomerMessageModule.requestAddOrUpdateMessage(data);
	},
	readyToAdd: function() {
		$('#nameText').val('');
		CustomerMessageModule.currentEditCellId = undefined;
		CustomerMessageModule.currentEditCellName = undefined;
		$('#myModal').modal('show');
		$('#myModal_title').text('新增');
	},
	readyToUpdate: function(id, name) {
		CustomerMessageModule.currentEditCellId = id;
		CustomerMessageModule.currentEditCellName = name;
		$('#nameText').val(name);
		$('#myModal').modal('show');
		$('#myModal_title').text('修改');
	},
	readyToDelete : function(id, name) {
		swal({
            title: "您确定要删除 " + name + " 吗?",
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
				CustomerMessageModule.requestDeleteMessage(id);
            } else {
                swal("已取消", "您取消了删除操作！", "error");
            }
        });
	},
	loadMessageList: function(messageList) {
		var target = $('#messageDisplayArea');
		target.html('');
		for (var i = 0; i < messageList.length; i++) {
			var cell = messageList[i];
			target.append(CustomerMessageModule.createMessageUnitView(cell));
		}
	},
	createMessageUnitView: function(message) {
		var html = '<div class="alert alert-success alert-dismissable">' +
			'<button class="close option-unit" type="button" onclick="CustomerMessageModule.readyToDelete('+message.customerMessageTypeId+', \''+message.customerMessageTypeName+'\')">' +
			'<i class="fa fa-times red-font"></i>' +
			'</button>' +
			'<button class="close" type="button" onclick="CustomerMessageModule.readyToUpdate('+message.customerMessageTypeId+', \''+message.customerMessageTypeName+'\')">' +
			'<i class="fa fa-edit red-font"></i>' +
			'</button> ' +
			message.customerMessageTypeName +
			'</div>';
		return html;
	},
	packageEditAreaData: function() {
		var data = {
			customerMessageTypeId : CustomerMessageModule.currentEditCellId,
			customerMessageTypeName : $('#nameText').val()
		}
		return data;
	},
	requestAddOrUpdateMessage: function(messageData) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mCustomerMessageType/addOrUpdate",
			type: 'POST',
			cache: false,
			dataType: 'json',
			//async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: messageData,
			success: function(data) {
				if (data.code != 0) {
					swal('操作失败', data.desc, 'error');
				} else {
					CustomerMessageModule.init();
					$('#myModal').modal('hide');
					swal('操作成功', '', 'success');
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
	},
	requestDeleteMessage: function(messageId) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mCustomerMessageType/delete",
			type: 'POST',
			cache: false,
			dataType: 'json',
			//async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				customerMessageTypeId : messageId
			},
			success: function(data) {
				if (data.code != 0) {
					swal('操作失败', data.desc, 'error');
				} else {
					CustomerMessageModule.init();
					swal('操作成功', '', 'success');
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
	},
	requestMessageList: function() {
		var targetData = undefined;
		$.ajax({
			url: GlobalConfig.serverAddress + "/mCustomerMessageType/list",
			type: 'GET',
			cache: false,
			dataType: 'json',
			async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: null,
			success: function(data) {
				if (data.code != 0) {
					swal('获取数据失败', data.desc, 'error');
				} else {
					targetData = data.data;
					if (undefined == targetData) {
						targetData = [];
					}
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
		return targetData;
	}
};