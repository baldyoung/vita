$(function() {

});

var CustomerHelpModule = {
	displayAreaId: "displayArea",
	requestHelpData: function() {

	},
	loadHelpData: function(helpData) {

	},
	showHelpData: function(t) {
		var temp = $("#helpText"+t);
		layer.open({
			content: temp.text(),
			btn: '好的'
		});
	},
	createHelpItemHTML: function(helpItem) {
		var html = ''
	}
}