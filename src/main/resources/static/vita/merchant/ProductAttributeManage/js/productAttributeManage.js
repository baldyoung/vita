
$(function(){
	AttributeTypeModule.init();
});

/**
 * 属性类别模块
 * @type {{init: AttributeTypeModule.init, requestAndLoadData: AttributeTypeModule.requestAndLoadData, loadData: AttributeTypeModule.loadData, createDisplayUnitHTML: (function(*): string)}}
 */
var AttributeTypeModule = {
	init: function() {
		AttributeTypeModule.requestAndLoadData();
	},
	loadData: function(data) {
		var target = $('#attributeTypeDisplayArea');
		target.html('');
		for (var i = 0; i < data.length; i++) {
			target.append(AttributeTypeModule.createDisplayUnitHTML(data[i]));
		}
	},
	createDisplayUnitHTML: function(item) {
		var html = '<li class="dd-item" >' +
			'<div class="dd-handle">' +
			item.productAttributeTypeName +
			'<span onclick="DeleteAttributeTypeModule.deleteAction('+item.productAttributeTypeId+', \''+item.productAttributeTypeName+'\')" class="label label-danger pull-right"><i class="fa fa-trash"></i></span>' +
			'</div>' +
			'</li>';
		return html;
	},
	requestAndLoadData: function() {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mProductAttribute/attributeTypeList",
			type: 'GET',
			cache: false,
			dataType: 'json',
			//async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: null,
			success: function(data) {
				if (data.code != 0) {
					swal('数据获取失败', data.desc, 'error');
				} else {
					AttributeTypeModule.loadData(data.data);
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
	}
}
/**
 * 属性值模块
 * @type {{init: AttributeValueModule.init, loadTargetTypeValue: AttributeValueModule.loadTargetTypeValue, loadData: AttributeValueModule.loadData, createDisplayUnitHTML: AttributeValueModule.createDisplayUnitHTML}}
 */
var AttributeValueModule = {
	init: function() {

	},
	loadTargetTypeValue: function(tTypeId) {},
	loadData: function() {},
	createDisplayUnitHTML: function(item) {
		var html = '<li class="dd-item" ><div class="dd-handle">' +
			item.productAttributeValueName +
			'<span class="label label-warning pull-right"><i onclick="DeleteAttributeValueModule.deleteAction('+item.productAttributeValueId+',\''+item.productAttributeValueName+'\')" class="fa fa-trash"></i></span></div>' +
			'</li>';
		return html;
	},
	requestAndLoadData: function() {
		$.ajax({
			url: GlobalConfig.serverAddress + "/mProductAttribute/attributeValueList",
			type: 'GET',
			cache: false,
			dataType: 'json',
			async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: null,
			success: function(data) {
				if (data.code != 0) {
					swal('数据获取失败', data.desc, 'error');
				} else {
					AttributeValueModule.loadData(data.data);
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
	}
}

/**
 * 属性值的修改
 */
var AddAttributeTypeModule = {
		readyToAdd: function() {
			$('#addAttributeTypeBtn').hide();
			$('#attributeTypeNameText').val('');
			$('#addAttributeTypeModule').show();
		},
		cancelAction: function() {
			$('#addAttributeTypeModule').hide();
			$('#addAttributeTypeBtn').show();
		},
		addAction: function() {
			var newAttributeTypeName = $('#attributeTypeNameText').val();
			console.log('新添加的属性类型名为：' + newAttributeTypeName);
			AddAttributeTypeModule.cancelAction();
		}
	}
	/**
	 * 属性值的新增
	 */
var AddAttributeValueModule = {
	readyToAdd: function() {
		$('#addAttributeValueBtn').hide();
		$('#attributeValueNameText').val('');
		$('#addAttributeValueModule').show();
	},
	cancelAction: function() {
		$('#addAttributeValueModule').hide();
		$('#addAttributeValueBtn').show();
	},
	addAction: function() {
		var newAttributeValueName = $('#attributeValueNameText').val();
		console.log('新添加的属性类型名为：' + newAttributeValueName);
		AddAttributeValueModule.cancelAction();
	}
}

var DeleteAttributeTypeModule = {
	readyToDelete: function() {},
	deleteAction: function(tId, tName) {
		swal({
				title: "您确定要删除 " + tName + " 吗?",
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

				} else {
					swal("已取消", "您取消了删除操作！", "error");
				}
			});
	}
}

var DeleteAttributeValueModule = {
	readyToDelete: function() {},
	deleteAction: function(tId, tName) {
		swal({
				title: "您确定要删除 " + tName + " 吗?",
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

				} else {
					swal("已取消", "您取消了删除操作！", "error");
				}
			});
	}
}