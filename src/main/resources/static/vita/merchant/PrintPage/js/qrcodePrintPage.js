$(function() {
	var t = GlobalMethod.getArgsFromLocationHref("t");
	t = "/resource/qrcodeImg/" + t;
	$('#printUnit').attr('src', t);
	t = GlobalMethod.getArgsFromLocationHref("diningRoomName");
	t = unescape(t);
	// console.log(t);
	$('#diningRoomNameArea').text('>>> '+t);
	// htmlToImgModule.toImg('printArea' );
	// htmlToImgModule.toImgWithHeight('printArea', 150);
	setTimeout("window.print()", 1500);
});


