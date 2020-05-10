

$(function(){
	console.log("workingL.js content...");
	registerMonitor();
	
	
});
function registerMonitor() {
	$('.roomStatusLabel').bind("click", function(){
		var id = $(this).attr("id");
		console.log(id);
	});
	$(".orderPanelInfoType").bind("click", function(){
		var id = $(this).children("a").attr("data-toggle");
		console.log(id);
		$(".tab-pane").removeClass("active");
		$("#"+id).addClass("active");
		$(".orderPanelInfoType").removeClass("active");
		$(this).addClass("active");
	})
};

var RoomModule = {
	init : function () {

	},
	requestData : function () {

	},
	loadData : function () {

	},
	createDisplayUnitHtml : function (item) {
		var  html = '<div id="Room3" class="col-sm-4 roomPanel" style="width:480px; height:300px;">' +
			'<div class="ibox roomPanel" style=" ">' +
			'<div class="ibox-title roomPanelTop">' +
			'<span class="pull-left" style="cursor:pointer; font-size:10px;">' +
			'<span class="glyphicon glyphicon-qrcode" aria-hidden="true" data-toggle="modal" data-target="#qrCodePanel"></span>' +
			'</span>' +
			'<span class="label label-warning pull-right roomStatusLabel">已用</span>' +
			'<h5>' +
			'<span class="pull-left">&nbsp;&nbsp;三号桌&nbsp;&nbsp;-&nbsp;&nbsp;</span>' +
			'</h5>' +
			'</div>' +
			'<div class="ibox-content roomPanelBottom">' +
			'<div class="col-sm-12 roomOptionalArea2">' +
			'<div class="col-sm-5" style="padding: 0 0 0 0;">' +
			'<div class="team-members roomOptionalArea">' +
			'<button class="btn btn-info " type="button" data-toggle="modal" data-target="#OrderFormWindow" style="margin-bottom:5px; " onclick="">' +
			'<i class="fa fa-paste"></i> 订单详情' +
			'<span class="badge badge-danger" style="background:#ED5565; color:black;  display:;    ">10</span>' +
			'</button>' +
			'<button class="btn btn-primary " data-toggle="modal" data-target="#customerMsgPanel" type="button" style="margin-bottom:5px; " onclick="">' +
			'<i class="glyphicon glyphicon-envelope"></i> 客户消息' +
			'<span class="badge badge-danger" style="background:#ED5565; color:black; display:;    ">3</span>' +
			'</button>' +
			'<button class="btn btn-primary " data-toggle="modal" data-target="#roomPreOrderHistoryPanel" type="button" style="margin-bottom:5px; background:#e0a9f9; " onclick="">' +
			'<i class="glyphicon glyphicon-tag"></i> 预定记录' +
			'<span class="badge badge-danger" style="background:#ED5565; color:black;  display:;    ">6</span>' +
			'</button>' +
			'<button class="btn btn-primary " data-toggle="modal" data-target="#roomStatusPanel" type="button" style="margin-bottom:5px; " onclick="">' +
			'<i class="glyphicon glyphicon-edit"></i> 修改状态' +
			'</button>' +
			'</div>' +
			'</div>' +
			'<div class="col-sm-7" style="padding: 0 0 0 0;">' +
			'<span class="label label-warning pull-left roomStatusLabel" style="background:#F4C20B; width:100%;">' +
			'<div class="roomPanelPreTip">2020-02-09&nbsp;【晚餐】</div>' +
			'<div class="roomPanelPreTip">刘先生15179798118</div>' +
			'</span>' +
			'<textarea style="resize:none; height:100px; width:100%; border:0px solid; margin-bottom: 5px; cursor:pointer; " readonly="true">空调、麻将桌、独立卫生间</textarea>' +
			'</div>' +
			'</div>' +
			'<div class="row  m-t-sm">' +
			'<div class="col-sm-4">' +
			'<div class="font-bold">客人数量</div>' +
			'<div id="RoomPeopleNum3">未知</div>' +
			'</div>' +
			'<div class="col-sm-4">' +
			'<div class="font-bold">商品数量</div>' +
			'<div id="RoomProNum3">未知</div>' +
			'</div>' +
			'<div class="col-sm-4 text-right">' +
			'<div class="font-bold">账单预算</div>' +
			'<div id="RoomTotalAmount3">未知</div>' +
			'</div>' +
			'</div>' +
			'</div>' +
			'</div>' +
			'</div>';
		return html;
	}
}

















