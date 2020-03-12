$(function() {
	init();

});
var packageOptionStatus = true;

function openOrClosePackageOption() {
	var temp = (packageOptionStatus ? "关闭" : "打开")
	swal({
			title: "您确定要" + temp + "吗？",
			//text: "删除后将无法恢复，请谨慎操作！",
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
			//... ajax请求成功后执行后面步骤
			packageOptionStatus = !packageOptionStatus;
			if (!packageOptionStatus) {
				$("#packageOptionBtn").text("打开");
				$("#packageOptionBtn").removeClass("btn-warning");
				$("#packageOptionBtn").addClass("btn-primary");
				$("#packageOptionArea").removeClass("active");
			} else {
				$("#packageOptionBtn").text("关闭");
				$("#packageOptionBtn").removeClass("btn-primary");
				$("#packageOptionBtn").addClass("btn-warning");
				$("#packageOptionArea").addClass("active");
			}
		});

}

function init() {
	if (!packageOptionStatus) {
		$("#packageOptionBtn").text("打开");
		$("#packageOptionBtn").removeClass("btn-warning");
		$("#packageOptionBtn").addClass("btn-primary");
		$("#packageOptionArea").removeClass("active");
	} else {
		$("#packageOptionBtn").text("关闭");
		$("#packageOptionBtn").removeClass("btn-primary");
		$("#packageOptionBtn").addClass("btn-warning");
		$("#packageOptionArea").addClass("active");
	}
}