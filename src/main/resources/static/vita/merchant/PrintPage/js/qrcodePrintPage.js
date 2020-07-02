$(function() {
	var t = GlobalMethod.getArgsFromLocationHref("t");
	t = "/resource/qrcodeImg/" + t;
	$('#printUnit').attr('src', t);
	t = GlobalMethod.getArgsFromLocationHref("diningRoomName");
	t = unescape(t);
	console.log(t);
	$('#diningRoomNameArea').text('>>> '+t);
	// htmlToImgModule.toImg('printArea' );
	// htmlToImgModule.toImgWithHeight('printArea', 150);
	setTimeout("readyToPrint()", 1000);
});

function readyToPrint() {
	swal({
			title: "确定打印吗？",
			text: "",
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
				setTimeout("window.print()", 1500);
			} else {
				GlobalMethod.goBack();
			}
		});
}

