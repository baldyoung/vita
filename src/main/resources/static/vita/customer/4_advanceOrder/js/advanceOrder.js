$(function() {
	init();
});

function init() {
	AdvanceOrderModule.requestAdvanceOrderData();
}


var AdvanceOrderModule = {
	displayAreaId: "#itemDisplayArea",
	orderData: undefined,
	productLabelData: undefined,
	requestAdvanceOrderData: function() {
		AdvanceOrderModule.orderData = test_data.advanceOrderData;
		AdvanceOrderModule.productLabelData = test_data.productLabelData;
		AdvanceOrderModule.loadAdvanceOrderData();
	},
	loadAdvanceOrderData: function() {
		var data = AdvanceOrderModule.orderData.itemList;
		console.log(data);
		var target = $(AdvanceOrderModule.displayAreaId);
		var validCellHTML = "",
			invalidCellHTML = "",
			temp;
		for (var i = 0; i < data.length; i++) {
			temp = data[i];
			if (temp.isValid) {
				validCellHTML += AdvanceOrderModule.createValidItemHTML(temp);
			} else {
				invalidCellHTML += AdvanceOrderModule.createInvalidItemHTML(temp);
			}
		}
		target.html('');
		if (invalidCellHTML != "") {
			target.html(invalidCellHTML);
			target.append('<div style="width:100%; color:#EF4F4F; font-size:10px; text-align: right;"> 以上商品已被其它顾客抢先一步选购了！ </div>');
		}
		target.append(validCellHTML);
		data = AdvanceOrderModule.orderData;
		$('#diningTypeName').text(data.diningTypeName);
		$('#diningTime').text(data.diningTime);
		$('#itemNumber').text(data.itemNumber);
		$('#orderAmount').text(data.orderAmount);
		$('#discount').text(data.discount);
		$('#discountAmount').text(parseFloat(data.orderAmount) - parseFloat(data.receivableAmount));
		$('#receivableAmount').text(data.receivableAmount);
	},
	requestProductLabelData: function() {

	},
	submitAdvanceOrderData: function() {

	},
	getItem: function(tItemId) {
		var data = AdvanceOrderModule.orderData.itemList,
			i,
			temp;
		for (i = 0; i < data.length; i++) {
			temp = data[i];
			if (temp.itemId == tItemId) {
				return temp;
			}
		}
		return undefined;
	},
	getLabelValueList: function(tLabelTypeId) {
		if (undefined == tLabelTypeId) {
			return undefined;
		}
		var i,
			productLabelData = AdvanceOrderModule.productLabelData,
			temp;
		for (i = 0; i < productLabelData.length; i++) {
			temp = productLabelData[i];
			if (tLabelTypeId == temp.labelTypeId) {
				return temp.labelValueList;
			}
		}
		return undefined;
	},
	getLabelValue: function(tLabelValueCellId) {
		if (undefined == tLabelValueCellId) {
			return undefined;
		}
		var i, j,
			productLabelData = AdvanceOrderModule.productLabelData,
			temp, cell;
		for (i = 0; i < productLabelData.length; i++) {
			temp = productLabelData[i].labelValueList;
			for (j = 0; j < temp.length; j++) {
				cell = temp[j];
				if (cell.labelValueId == tLabelValueCellId) {
					return cell;
				}
			}
		}
		return undefined;
	},
	createValidItemHTML: function(itemData) {
		var html = '<div class="zjzz-buylist-det" style="margin-bottom: 1px;">';
		html += '<img src="' + GlobalConfig.productImgRelativePath + itemData.productPicture + '" />';
		html += '<div class="zjzz-buylist-gdetail">';
		html += '<span class="zjzz-buylist-gtit1">' + itemData.productName + '</span>';
		html += '<span class="zjzz-buylist-gmoney">';
		html += '<i class="zjzz-buylist-gm1">' + itemData.productPrice + '</i>';
		html += '<i class="zjzz-buylist-gm2">x' + itemData.quantity + '</i>';
		html += '</span>';
		if (undefined != itemData.labelTypeValueId) {
			var temp = AdvanceOrderModule.getLabelValue(itemData.labelTypeValueId);
			html += '<span class="zjzz-buylist-gtit1" style="font-size:13px; " onclick="AdvanceOrderModule.selectLabel(' + itemData.itemId + ')">口味：<i id="itemLabel' + itemData.itemId + '" style=" height:20px; background: #' + temp.labelValueColor + '; border-radius: 5px; padding:0 5px 0 5px;">' + temp.labelValueName + '</i></span>';
		}
		html += '</div>';
		html += '</div>';
		return html;
	},
	createInvalidItemHTML: function(itemData) {
		var html = '<div class="zjzz-buylist-det" style="background:#FF685E; margin-bottom: 1px ;">';
		html += '<img src="' + GlobalConfig.productImgRelativePath + itemData.productPicture + '" />';
		html += '<div class="zjzz-buylist-gdetail">';
		html += '<span class="zjzz-buylist-gtit1">' + itemData.productName + '</span>';
		html += '<span class="zjzz-buylist-gmoney">';
		html += '<i class="zjzz-buylist-gm1">' + itemData.productPrice + '</i>';
		html += '<i class="zjzz-buylist-gm2">x' + itemData.quantity + '</i>';
		html += '</span>';
		html += '</div>';
		html += '</div>';
		return html;
	},
	selectLabel : function(itemId) {
		var item = AdvanceOrderModule.getItem(itemId);
		var labelList = AdvanceOrderModule.getLabelValueList(item.labelTypeId); 
		var html = '<div style="width:100%; background:#e5be6b; height:100px; border-top-left-radius: 15px; border-top-right-radius: 15px;">';
		html += '<div style="width:100%; background: #ff8a0c; border-top-left-radius: 15px; border-top-right-radius: 15px; padding: 5px 0 5px 0; text-align: center;">';
		html += '<i style="height:30px;">请选择该菜品口味</i>';
		html += '<a onclick="layer.closeAll()" style="top:0px; width:30px;height:30px;background:url(/customer/0_common/img/arrow-bottom.png) no-repeat center center;background-size:50%; float:right; margin-right:8px; "></a>';
		html += '</div>';
		html += '<div style="width:100%; padding: 3px 20px 10px 20px;">';
		for (var i = 0; i < labelList.length; i++) {
			var temp = labelList[i];
			html += '<input onclick="AdvanceOrderModule.updateItemLabel(' + itemId + ', ' + temp.labelValueId + ')" type="button" style="height:30px; padding:2px 10px 2px 10px; margin: 6px 5px 5px 3px; border-radius:5px; background:#' + temp.labelValueColor + '; float:left; " value="' + temp.labelValueName + '" />';
		}
		html += '</div>';
		html += '</div>';
		layer.open({
			type: 1,
			content: html,
			anim: 'up',
			style: 'position:fixed; bottom:0; left:0; width: 100%; height: 200px; border:none; border-top-left-radius: 15px; border-top-right-radius: 15px; background:#e5be6b; '
		});
	},
	updateItemLabel : function(tItemId, tNewLabelValueId) {
		var item = AdvanceOrderModule.getItem(tItemId);
		item.labelTypeValueId = tNewLabelValueId;
		var label = AdvanceOrderModule.getLabelValue(tNewLabelValueId);
		var target = $('#itemLabel'+tItemId);
		target.css("background", "#"+label.labelValueColor);
		target.text(label.labelValueName);
		layer.closeAll();
		console.log("修改商品项标签:");
		console.log(AdvanceOrderModule.orderData);
	}
};

function test() {
	selectLabel(1);
}

var test_data = {
	productLabelData: [{
		labelTypeId: "1",
		labelValueList: [{
			labelValueId: "101",
			labelValueName: "正常",
			labelValueColor: "009F95"
		}, {
			labelValueId: "102",
			labelValueName: "非常辣",
			labelValueColor: "FC746C"
		}, {
			labelValueId: "103",
			labelValueName: "不辣",
			labelValueColor: "8CB2C5"
		}]
	},{
		labelTypeId: "2",
		labelValueList: [{
			labelValueId: "201",
			labelValueName: "201",
			labelValueColor: "009F95"
		}, {
			labelValueId: "202",
			labelValueName: "202",
			labelValueColor: "FC746C"
		}, {
			labelValueId: "203",
			labelValueName: "203",
			labelValueColor: "8CB2C5"
		}]
	}],
	advanceOrderData: {
		orderId: "k00101",
		diningTypeName: "堂食",
		diningTime: "现在",
		itemNumber: "10",
		orderAmount: "300",
		discount: "0.8",
		receivableAmount: "240",
		receivedAmount: "240",
		itemList: [{
			itemId: "1001",
			productName: "赣南脐橙",
			productPrice: "30.00",
			productPicture: "3.png",
			quantity: "1",
			labelTypeId: "1",
			labelTypeValueId: "101",
			isValid: true
		}, {
			itemId: "1002",
			productName: "赣北苹果",
			productPrice: "25.00",
			productPicture: "1.png",
			quantity: "2",
			labelTypeId: "2",
			labelTypeValueId: "201",
			isValid: true
		}, {
			itemId: "1003",
			productName: "失效商品测试",
			productPrice: "888.00",
			productPicture: "6.png",
			quantity: "1",
			labelTypeId: "1",
			labelTypeValueId: "101",
			isValid: false
		}, {
			itemId: "1004",
			productName: "赣北苹果",
			productPrice: "25.00",
			productPicture: "1.png",
			quantity: "1",
			isValid: true
		}]
	}

}