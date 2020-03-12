


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

