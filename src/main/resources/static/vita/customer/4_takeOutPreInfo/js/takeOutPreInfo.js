$(function() {

	var i;
	// GlobalMethod.requestLocation(i, '12');

});

function init() {

}

var ReceiverInfoModule = {
	receiverData: {},
	init: function() {
		// 通过ajax获取购物车的选定数据
	},
	toSelectAddress: function() {
		if (!ReceiverInfoModule.checkReceiverInfo()) {
			return;
		}
		GlobalMethod.redirectURL('../4_receiverAddress/receiverAddress.html');

	},
	checkReceiverInfo: function() {
		var receiverData = {
			receiverName: $('#receiverNameText').val(),
			receiverPhone: $('#receiverPhoneText').val()
		};
		if (GlobalMethod.isEmpty(receiverData.receiverName)) {
			layer.open({
				content: '请先填写验收人姓名',
				skin: 'msg',
				time: 3 //2秒后自动关闭
			});
			return false;
		}
		if (GlobalMethod.isEmpty(receiverData.receiverPhone)) {
			layer.open({
				content: '请先填写验收人手机号',
				skin: 'msg',
				time: 3 //2秒后自动关闭
			});
			return false;
		}
		if (GlobalConfig.currentModel == 'test') {
			return true;
		}
		return ReceiverInfoModule.verifyCode();
	},
	verifyCode: function() {
		var result = false;
		var code = $('#codeText').val();
		if (GlobalMethod.isEmpty(code)) {
			layer.open({
				content: '请填写手机验证码',
				skin: 'msg',
				time: 3 //2秒后自动关闭
			});
			return false;
		}
		// 发送ajax请求验证短信验证码是否正确
		$.ajax({
			url: GlobalConfig.serverAddress + "checkVerifyCode",
			type: 'POST',
			cache: false,
			async: false, //设置同步
			dataType: 'json',
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				receiverName: $('#receiverNameText').val(),
				receiverPhone: $('#receiverPhoneText').val(),
				verifyCode: $('#codeText').val()
			},
			success: function(data) {
				if (data.result == 'success') {
					result = true;
				} else {
					result = false;
					swal({
						title: "验证失败",
						text: data.desc,
						type: "error"
					});
				}
			},
			error: function() {
				swal({
					title: "服务器连接失败",
					text: "请检查网络是否通畅！",
					type: "warning"
				});
			}
		});
		return result;
	}
}
