



/**
 * 品类控制模块
 */
var ProductTypeModule = {
	displayAreaId : '#typeDisplayArea',
	selectedTypeId : undefined,
	
	init : function() {
		ProductTypeModule.requestData();
	},
	requestData : function() {
		var targetData = [];
		$.ajax({
			url: GlobalConfig.serverAddress + "/productType/list",
			type: 'GET',
			cache: false,
			dataType: 'json',
			//async: false, //设置同步
			contentType: "application/json; charset=utf-8",
			data: null,
			success: function(data) {
				if (data.code == 0) {
					targetData = data.data;
					targetData = sortProductTypeList(targetData);
					ProductTypeModule.showData(targetData);
					ProductTypeModule.selectUnit(targetData[0].productTypeId);
				} else {
					swal('获取品类数据失败', data.desc, 'error');
				}
			},
			error: function() {
				swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
		//return targetData;
	},
	showData : function(t) {
		var str = '<div class="classify-perch"></div>';
		var i;
		for (i=0; i<t.length; i++) {
			if (0 == i) {
				ProductTypeModule.selectedTypeId = t[i].typeId;
			}
			str += ProductTypeModule.createDisplayCellHTML(t[i]);
		}
		str += '<div class="classify-perch2"></div>';
		$(ProductTypeModule.displayAreaId).html(str);
	},
	createDisplayCellHTML : function(t) {
		var str = '<span id="typeUnit' + t.productTypeId + '" class="classify-text1" onclick="ProductTypeModule.selectUnit(' + t.productTypeId + ')" >' + t.productTypeName + '</span>';
		return str;
	},
	selectUnit : function(t) {
		console.log("t:"+t);
		$('#typeUnit'+ProductTypeModule.selectedTypeId).removeClass('pitch-on2');
		ProductTypeModule.selectedTypeId = t;
		$('#typeUnit'+t).addClass('pitch-on2');
		ProductModule.requestAndLoadData(ProductTypeModule.selectedTypeId);
	}
};

/**
 * 商品控制模块
 */
var ProductModule = {
	displayAreaId : '#productDisplayArea',
	productListBuffer : [], // 第一个数组存储的是可以下单的商品，第二个商品存储的是无法下单的商品
	sortType : 'productGrade',
	sortRule : 'desc', // 默认从大到下, 从小到大为asc
	requestAndLoadData : function(tProductTypeId) {
		// 同步ajax获取指定类型的商品数据
		$.ajax({
			url: GlobalConfig.serverAddress + "/product/validProductForType",
			type: 'GET',
			cache: false,
			dataType : 'json',
			async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			// contentType : 'application/json; charset=utf-8',
			data: {
				t : tProductTypeId
			},
			success: function(data) {
				if (data.code != 0) {
					//swal('获取商品信息失败', data.desc, 'error');
				} else {
					var temp = data.data;
					temp = sortProductList(temp);
					var i, j=0, k=0;
					ProductModule.productListBuffer[0] = [];
					ProductModule.productListBuffer[1] = [];
					for (i=0; i<temp.length; i++) {
						temp[i].currentQuantity = temp[i].currentQuantity == undefined ? 0 : temp[i].currentQuantity;
						if (1 == temp[i].productStockFlag && temp[i].productStock <= 0) {
							// 无效商品
							ProductModule.productListBuffer[1][j++] = temp[i];
						} else {
							// 有效商品
							ProductModule.productListBuffer[0][k++] = temp[i];
						}
					}
					ProductModule.showData();
				}
			},
			error: function() {
				//swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});

	},
	sortProductList : function(tProductList) { // 商品排序规则
		var i, itemF, itemE, item;
		for (i=0; i<tProductList.length; i++) {
			item = i;
			for (j=i+1; j<tProductList.length; j++) {
				itemF = tProductList[item];
				itemE = tProductList[j];
				if (ProductModule.sortType == 'productGrade') {
					if (itemE.productGrade > itemF.productGrade) {
						item = j;
					}
				} else if (ProductModule.sortRule == 'desc') {
					if (itemE[ProductModule.sortType] > itemF[ProductModule.sortType]) {
						item = j
					}
				} else {
					if (itemE[ProductModule.sortType] < itemF[ProductModule.sortType]) {
						item = j;
					}
				}
			}
			itemF = tProductList[item];
			tProductList[item] = tProductList[i];
			tProductList[i] = itemF;
		}
		return tProductList;
	},
	showData : function() { // 商品展示调用
		var i;
		var t = ProductModule.sortProductList(ProductModule.productListBuffer[0]);
		var target = $(ProductModule.displayAreaId);
		target.html('');
		for (i=0; i<t.length; i++) {
			target.append(ProductModule.createDisplayCellHTML(t[i]));
		};
		t = ProductModule.productListBuffer[1];
		for (i=0; i<t.length; i++) {
			target.append(ProductModule.createDisplayCellHTML(t[i]));
		};
		target.append('<div style="width:100%; margin-bottom:30px; text-align:center; font-size:10px; ">&nbsp;</div>');
	},
	createDisplayCellHTML : function(t) { // 每个商品的展示效果生成
		var forbidColor = '#F0E0E0';
		var forbidOption = (1 == t.productStockFlag && t.productStock <= 0);
		var str = '<div class="classify-box1" ' + (forbidOption ? ' style="background:#F0E0E0;" ' : '') + ' >';
            str += '<span class="classify-box1-img1"><img src="' + GlobalConfig.productImgRelativePath + t.productImgName + '" alt=""></span>';
            str += '<div class="classify-box2">';
            str += '<span class="classify-box2-text1">' + t.productName + (forbidOption ? '[已售罄]' : '') + '</span>';
            str += '<span class="classify-box2-text2">' + t.productPrice + '</span>';
            str += '<div class="shop-cart-box3">';
            if (forbidOption) {
            	str += '<span class="shop-cart-subtract" onclick="ShoppingCartModule.forbidOption()" ></span>';
            } else {
            	str += '<span class="shop-cart-subtract" onclick="ShoppingCartModule.delProduct(' + t.productId + ')" ></span>';
            }
		    str += '<input id="productAmountInCart' + t.productId + '" type="number" size="4" value="' + t.currentQuantity + '" id="tb_count" class="shop-cart-numer"  ' + (forbidOption ? ' style="background:#F0E0E0;" ' : '') + '  readonly="true" >';
		    if (forbidOption) {
		    	str += '<span class="shop-cart-add" onclick="ShoppingCartModule.forbidOption()" ></span>';
		    } else {
		    	str += '<span class="shop-cart-add" onclick="ShoppingCartModule.addProduct(' + t.productId +')" ></span>';
		    }
		    str += '</div>';
            str += '</div>';
           	str += '</div>';
        return str;
	},
	updateSortTypeOrRule : function(tType, tRule) {// 对商品进行排序
		ProductModule.sortType = tType;
		ProductModule.sortRule = tRule;
		ProductModule.showData();
		console.log('当前排序规则：'+tType+', '+tRule);
	}
}

/**
 * 购物车控制模块
 * @type {{delProduct: ShoppingCartModule.delProduct, addProduct: ShoppingCartModule.addProduct, forbidOption: ShoppingCartModule.forbidOption, requestUpdateShoppingCart: (function(*, *): boolean)}}
 */
var ShoppingCartModule = {
	
	addProduct : function(t) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/shoppingCart/addProduct",
			type: 'GET',
			cache: false,
			dataType : 'json',
			async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			// contentType : 'application/json; charset=utf-8',
			data: {
				productId : t,
				quantity : 1
			},
			success: function(data) {
				if (data.code != 0) {
					layer.open({
						content: ''+data.desc,
						skin: 'msg',
						time: 2
					});
				} else {
					var target = $('#productAmountInCart'+t);
					var currentAmount = parseInt(target.val());
					if (currentAmount < 99 && ShoppingCartModule.requestUpdateShoppingCart(t, 1)) {
						target.val(currentAmount+1);
					}
				}
			},
			error: function() {
				//swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});
	},
	delProduct : function(t) {
		$.ajax({
			url: GlobalConfig.serverAddress + "/shoppingCart/addProduct",
			type: 'GET',
			cache: false,
			dataType : 'json',
			async: false, //设置同步
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			// contentType : 'application/json; charset=utf-8',
			data: {
				productId : t,
				quantity : -1
			},
			success: function(data) {
				if (data.code != 0) {
					layer.open({
						content: ''+data.desc,
						skin: 'msg',
						time: 2
					});
				} else {
					var target = $('#productAmountInCart'+t);
					var currentAmount = parseInt(target.val());
					if (currentAmount > 0 && ShoppingCartModule.requestUpdateShoppingCart(t, -1)) {
						target.val(currentAmount-1);
					}
				}
			},
			error: function() {
				//swal('服务器连接失败', '请检查网络是否通畅', 'warning');
			}
		});

	},
	requestUpdateShoppingCart : function(productId, amount) {
		// 同步ajax请求，修改当前购物车内商品
		
		return true;	
	},
	forbidOption : function() {
		layer.open({
    		content: '商品库存不足，无法加入购物车',
    		skin: 'msg',
    		time: 3 //3秒后自动关闭
  		});
	}
};


function init() {
	
	$('#shopNameText').text(GlobalConfig.shopInfo.shopName);
	$(".classifyrt-text1").click(function () {
        $(this).addClass("tcolor-yellow");
        $(this).siblings(".classifyrt-text1").removeClass("tcolor-yellow");
        var sortType = $(this).attr('id');
        var sortRule = 'asc';
        if (!$(this).children(".sort-img2").hasClass('img3')) {
        	sortRule = 'desc';
        }
        ProductModule.updateSortTypeOrRule(sortType, sortRule);
	});
	$(".sort").click(function () {
		$(".sort").children(".sort-img").show();
		$(".sort").children(".sort-img2").hide();
        $(this).children(".sort-img").hide();
        $(this).children(".sort-img2").show();
        $(this).children(".sort-img2").toggleClass("img3");
	});
	$(".defaultSort").click(function () {
        $(".sort").children(".sort-img").show();
		$(".sort").children(".sort-img2").hide();
	});
	
};
function test() {
	ProductTypeModule.init();
};
$(function(){
	init();
	test();
});

// test
var test_productList = [
{
	productId : 1,
	productImg : '1.png',
	productName : '测试商品1',
	productPrice : 15.00,
	productStock : undefined, 
	productAmountInCart : 0,
	productGrade : 1,
	productSalesAmount : 601
},{
	productId : 2,
	productImg : '2.png',
	productName : '测试商品2',
	productPrice : 22.50,
	productStock : 0, 
	productAmountInCart : 0,
	productGrade : 2,
	productSalesAmount : 50
},{
	productId : 3,
	productImg : '3.png',
	productName : '测试商品3',
	productPrice : 32.50,
	productStock : undefined, 
	productAmountInCart : 0,
	productGrade : 3,
	productSalesAmount : 40
},{
	productId : 4,
	productImg : '4.png',
	productName : '测试商品4',
	productPrice : 111.50,
	productStock : undefined, 
	productAmountInCart : 0,
	productGrade : 4,
	productSalesAmount : 290
},{
	productId : 5,
	productImg : '5.png',
	productName : '测试商品5',
	productPrice : 10.50,
	productStock : 0, 
	productAmountInCart : 0,
	productGrade : 4,
	productSalesAmount : 290
},{
	productId : 6,
	productImg : '6.png',
	productName : '测试商品6',
	productPrice : 8.00,
	productStock : 0, 
	productAmountInCart : 0,
	productGrade : 4,
	productSalesAmount : 290
},{
	productId : 6,
	productImg : '6.png',
	productName : '测试商品6',
	productPrice : 8.00,
	productStock : 0, 
	productAmountInCart : 0,
	productGrade : 4,
	productSalesAmount : 290
},{
	productId : 6,
	productImg : '6.png',
	productName : '测试商品6',
	productPrice : 8.00,
	productStock : 0, 
	productAmountInCart : 0,
	productGrade : 4,
	productSalesAmount : 290
},{
	productId : 6,
	productImg : '6.png',
	productName : '测试商品6',
	productPrice : 8.00,
	productStock : 0, 
	productAmountInCart : 0,
	productGrade : 4,
	productSalesAmount : 290
},{
	productId : 6,
	productImg : '6.png',
	productName : '测试商品6',
	productPrice : 8.00,
	productStock : 0, 
	productAmountInCart : 0,
	productGrade : 4,
	productSalesAmount : 290
},{
	productId : 6,
	productImg : '6.png',
	productName : '测试商品6',
	productPrice : 8.00,
	productStock : 0, 
	productAmountInCart : 0,
	productGrade : 4,
	productSalesAmount : 290
},{
	productId : 6,
	productImg : '6.png',
	productName : '测试商品6',
	productPrice : 8.00,
	productStock : 0, 
	productAmountInCart : 0,
	productGrade : 4,
	productSalesAmount : 290
},{
	productId : 6,
	productImg : '6.png',
	productName : '测试商品6',
	productPrice : 8.00,
	productStock : 0, 
	productAmountInCart : 0,
	productGrade : 4,
	productSalesAmount : 290
},{
	productId : 6,
	productImg : '6.png',
	productName : '测试商品6',
	productPrice : 8.00,
	productStock : 0, 
	productAmountInCart : 0,
	productGrade : 4,
	productSalesAmount : 290
},{
	productId : 6,
	productImg : '6.png',
	productName : '测试商品6',
	productPrice : 8.00,
	productStock : 0, 
	productAmountInCart : 0,
	productGrade : 4,
	productSalesAmount : 290
},{
	productId : 6,
	productImg : '6.png',
	productName : '测试商品6',
	productPrice : 8.00,
	productStock : 0, 
	productAmountInCart : 0,
	productGrade : 4,
	productSalesAmount : 290
},{
	productId : 6,
	productImg : '6.png',
	productName : '测试商品6',
	productPrice : 8.00,
	productStock : 0, 
	productAmountInCart : 0,
	productGrade : 4,
	productSalesAmount : 290
}
];

var test_productTypeList = [
{
	typeId : 1,
	typeName : '测试类型',
},
{
	typeId : 2,
	typeName : '测试类型2',
},
{
	typeId :3,
	typeName : '测试类型2',
},
{
	typeId : 5,
	typeName : '测试类型2',
},
{
	typeId : 4,
	typeName : '测试类型2',
},
{
	typeId : 6,
	typeName : '测试类型2',
},
{
	typeId : 7,
	typeName : '测试类型2',
},
{
	typeId : 8,
	typeName : '测试类型2',
},
{
	typeId : 9,
	typeName : '测试类型2',
}
];



