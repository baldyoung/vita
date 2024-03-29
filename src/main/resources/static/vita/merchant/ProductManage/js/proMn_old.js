var uploaderX; //webuploader对象
var newProductInf; //新增或修改时，商品的信息对象
var currentProductId; //当前商品详情窗口里打开的商品编号
var currentProductImg; //当前商品的图片

var productTypeList; //商品类型集合

$(document).ready(function() {
	// 创建一个分页条，并请求第3页
	PagingBarModule.build({
		pageOptionalAreaId: 'pagingBtnDisplayArea',
		totalAmountOfData: 100,
		maxNumberOfDisplayPageButton: 6, // 
		maxAmountOfOnePage: 6,
		//loadPageIndex : 3,

	});
	testFun();

	//return;

	requestAllProductType();
	requestAllProduct();
	resetTargetProdut();
	//console.log("readyForProductInf...")
	var _$modal = $('#myModal5');
	_$modal.css('display', 'block');
	_$modal.addClass("webuploader-element-invisible");
	uploaderX = readyForWebUploader();
	//console.log("uploaderX_StateA");
	//console.log(uploaderX)
	//uploader.reset();
	_$modal.on('show.bs.modal', function() {
		_$modal.removeClass("webuploader-element-invisible");
	});
	/*
	 * requestOFT()
    
	*/
	/*
解决webUploader 模拟框按钮失效问题
以上解决途径，取自
https://www.cnblogs.com/zinan/p/6939733.html
 */
});
//**************************************************************************************************************************页面逻辑处理调用
//加载所有商品
function loadProductTable(t) {
	var dataTable = $('#productTable');
	if ($.fn.dataTable.isDataTable(dataTable)) {
		dataTable.DataTable().destroy();
	}
	dataTable.DataTable({
		'searching': false, //去掉搜索框
		'bLengthChange': false, //去掉每页显示多少条数据方法
		"serverSide": false, //关闭分页操作，默认就是关闭
		"autoWidth": false, //
		"bSort": false, //打开排序功能
		"order": [
			[1, "desc"],
			[0, 'asc']
		], //默认排序的指标
		'data': t, //表格的数据源    
		"pageLength": 20, //默认每页显示的数据条数
		"info": false,
		"paging": false,
		'columns': [{
			data: 'productId'
		}, {
			data: 'productName'
		}, {
			data: 'productTypeName'
		}, {
			data: 'productAttributeName'
		}, {
			data: 'productPrice'
		}, {
			data: 'productStockStatus'
		}, {
			data: 'productIsShow'
		}, {
			data: 'productPicture'
		}, {
			data: 'productGrade'
		}],
		"columnDefs": [{
				"render": function(data, type, row) {
					var a = "";
					//a += "<button type='button' class='btn btn-primary' onclick='showEditStudent(\""+0+"\")' data-toggle='modal' data-target='#myModal5' title='编辑用户' data-toggle='dropdown' style='margin-right:15px; margin-bottom: -1px;'><i class='fa fa-pencil-square-o'></i>&nbsp;编辑</button>"
					//a += "<button type='button' class='btn btn-primary' style='margin-top:3px;' onclick='loadOrderForm(\""+row.id+"\")'  data-toggle='dropdown' data-target=\"#OrderFormWindow\" style='margin-right:15px; margin-bottom: -1px;'><i class='fa fa-building-o'></i>&nbsp;查看</button>"
					//a += "<button type='button' class='btn btn-primary' onclick='courseList(\""+row.id+"\")' data-toggle='modal' data-target='#courseListDialog' title='课程列表' data-toggle='dropdown' style='margin-right:15px; margin-bottom: -1px;'><i class='fa fa-list'></i>&nbsp;课程列表</button>"
					a += "<button class=\"btn btn-primary \" style='margin-top:3px;' type=\"button\"  onclick=\"deleteProduct('" + row.productId + "')\"><i class=\"fa fa-building-o\"></i>删除"
					a += "</button>"
					a += "<button class=\"btn btn-primary \" style='margin-top:3px; margin-left:5px; ' type=\"button\" data-toggle=\"modal\" data-target=\"#myModal5\" onclick=\"readyProWin('" + row.productId + "')\"><i class=\"fa fa-building-o\"></i>修改"
					a += "</button>"
					return a;
				},
				"targets": 9
			}]
			//$('#OrderFormData_wrapper').css('padding-bottom', '0px');

	})
}
//恢复WebUploader到初始状态
function readyForProductInf() {
	var _$modal = $('#myModal5');
	_$modal.css('display', 'block');
	_$modal.addClass("webuploader-element-invisible");

	closeUploader();
	uploaderX.destroy();
	uploaderX = readyForWebUploader();
	closeUploader();
	_$modal.on('show.bs.modal', function() {
		_$modal.removeClass("webuploader-element-invisible");
	});
}
/*关闭上传框窗口后恢复上传框初始状态*/
function closeUploader() {
	// 移除所有缩略图并将上传文件移出上传序列
	//uploaderX.reset();
	for (var i = 0; i < uploaderX.getFiles().length; i++) {
		// 将图片从上传序列移除
		//uploaderX.removeFile(uploaderX.getFiles()[i]);
		uploaderX.removeFile(uploaderX.getFiles()[i], true);
		delete uploaderX.getFiles()[i];
		// 将图片从缩略图容器移除
		var $li = $('#' + uploaderX.getFiles()[i].id);
		$li.off().remove();

	}
	//setState('pedding');

	// 重置文件总个数和总大小
	//fileCount = 0;
	//fileSize = 0;
	// 重置uploader，目前只重置了文件队列
	uploaderX.reset();
	// 更新状态等，重新计算文件总个数和总大小
	//updateStatus();
}

//webUploder初始化
function readyForWebUploader() {
	var tempXXX;
	var $ = jQuery, // just in case. Make sure it's not an other libaray.
		$wrap = $('#uploader'),
		// 图片容器
		$queue = $('<ul class="filelist"></ul>')
		.appendTo($wrap.find('.queueList')),
		// 状态栏，包括进度和控制按钮
		$statusBar = $wrap.find('.statusBar'),
		// 文件总体选择信息。
		$info = $statusBar.find('.info'),
		// 上传按钮
		$upload = $wrap.find('.uploadBtn'),
		// 没选择文件之前的内容。
		$placeHolder = $wrap.find('.placeholder'),
		// 总体进度条
		$progress = $statusBar.find('.progress').hide(),
		// 添加的文件数量
		fileCount = 0,
		// 添加的文件总大小
		fileSize = 0,
		// 优化retina, 在retina下这个值是2
		ratio = window.devicePixelRatio || 1,
		// 缩略图大小
		thumbnailWidth = 110 * ratio,
		thumbnailHeight = 110 * ratio,
		// 可能有pedding, ready, uploading, confirm, done.
		state = 'pedding',
		// 所有文件的进度信息，key为file id
		percentages = {},
		supportTransition = (function() {
			var s = document.createElement('p').style,
				r = 'transition' in s ||
				'WebkitTransition' in s ||
				'MozTransition' in s ||
				'msTransition' in s ||
				'OTransition' in s;
			s = null;
			return r;
		})(),
		// WebUploader实例
		uploader;
	if (!WebUploader.Uploader.support()) {
		alert('Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
		throw new Error('WebUploader does not support the browser you are using.');
	}
	// 实例化
	uploader = WebUploader.create({
		pick: {
			id: '#filePicker',
			label: '点击选择图片'
		},
		dnd: '#uploader .queueList',
		paste: document.body,
		accept: {
			title: 'Images',
			extensions: 'gif,jpg,jpeg,bmp,png',
			mimeTypes: 'image/*'
		},
		// swf文件路径
		swf: BASE_URL + '/Uploader.swf',
		disableGlobalDnd: true,
		chunked: true,
		// server: 'http://webuploader.duapp.com/server/fileupload.php',
		server:  GlobalConfig.serverAddress + "/mProduct/add",
		//限制，一次只能上传一个文件
		fileNumLimit: 1,
		fileSizeLimit: 50 * 1024 * 1024, // 50 M
		fileSingleSizeLimit: 10 * 1024 * 1024, // 10 M
		compress: false,
		formData: {
			// inf: 'testMsg'
		},
		//disableGlobalDnd:false

	});
	tempXXX = uploader;

	// 添加“添加文件”的按钮，
	/*uploader.addButton({
	    id: '#filePicker2',
	    label: '继续添加'
	});*/

	// 当有文件添加进来时执行，负责view的创建
	function addFile(file) {
		var $li = $('<li id="' + file.id + '">' +
				'<p class="title">' + file.name + '</p>' +
				'<p class="imgWrap"></p>' +
				'<p class="progress"><span></span></p>' +
				'</li>'),

			$btns = $('<div class="file-panel">' +
				'<span class="cancel">删除</span>' +
				'<span class="rotateRight">向右旋转</span>' +
				'<span class="rotateLeft">向左旋转</span></div>').appendTo($li),
			$prgress = $li.find('p.progress span'),
			$wrap = $li.find('p.imgWrap'),
			$info = $('<p class="error"></p>'),

			showError = function(code) {
				switch (code) {
					case 'exceed_size':
						text = '文件大小超出';
						break;

					case 'interrupt':
						text = '上传暂停';
						break;

					default:
						text = '上传失败，请重试';
						break;
				}

				$info.text(text).appendTo($li);
			};

		if (file.getStatus() === 'invalid') {
			showError(file.statusText);
		} else {
			// @todo lazyload
			$wrap.text('预览中');
			uploader.makeThumb(file, function(error, src) {
				if (error) {
					$wrap.text('不能预览');
					return;
				}

				var img = $('<img src="' + src + '">');
				$wrap.empty().append(img);
			}, thumbnailWidth, thumbnailHeight);

			percentages[file.id] = [file.size, 0];
			file.rotation = 0;
		}

		file.on('statuschange', function(cur, prev) {
			if (prev === 'progress') {
				$prgress.hide().width(0);
			} else if (prev === 'queued') {
				$li.off('mouseenter mouseleave');
				$btns.remove();
			}

			// 成功
			if (cur === 'error' || cur === 'invalid') {
				console.log(file.statusText);
				showError(file.statusText);
				percentages[file.id][1] = 1;
			} else if (cur === 'interrupt') {
				showError('interrupt');
			} else if (cur === 'queued') {
				percentages[file.id][1] = 0;
			} else if (cur === 'progress') {
				$info.remove();
				$prgress.css('display', 'block');
			} else if (cur === 'complete') {
				$li.append('<span class="success"></span>');
			}

			$li.removeClass('state-' + prev).addClass('state-' + cur);
		});

		$li.on('mouseenter', function() {
			$btns.stop().animate({
				height: 30
			});
		});

		$li.on('mouseleave', function() {
			$btns.stop().animate({
				height: 0
			});
		});

		$btns.on('click', 'span', function() {
			var index = $(this).index(),
				deg;

			switch (index) {
				case 0:
					uploader.removeFile(file);
					return;

				case 1:
					file.rotation += 90;
					break;

				case 2:
					file.rotation -= 90;
					break;
			}

			if (supportTransition) {
				deg = 'rotate(' + file.rotation + 'deg)';
				$wrap.css({
					'-webkit-transform': deg,
					'-mos-transform': deg,
					'-o-transform': deg,
					'transform': deg
				});
			} else {
				$wrap.css('filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation=' + (~~((file.rotation / 90) % 4 + 4) % 4) + ')');
				// use jquery animate to rotation
				// $({
				//     rotation: rotation
				// }).animate({
				//     rotation: file.rotation
				// }, {
				//     easing: 'linear',
				//     step: function( now ) {
				//         now = now * Math.PI / 180;

				//         var cos = Math.cos( now ),
				//             sin = Math.sin( now );

				//         $wrap.css( 'filter', "progid:DXImageTransform.Microsoft.Matrix(M11=" + cos + ",M12=" + (-sin) + ",M21=" + sin + ",M22=" + cos + ",SizingMethod='auto expand')");
				//     }
				// });
			}

		});

		$li.appendTo($queue);
	}

	// 负责view的销毁
	function removeFile(file) {
		var $li = $('#' + file.id);

		delete percentages[file.id];
		updateTotalProgress();
		$li.off().find('.file-panel').off().end().remove();
	}

	function updateTotalProgress() {
		var loaded = 0,
			total = 0,
			spans = $progress.children(),
			percent;

		$.each(percentages, function(k, v) {
			total += v[0];
			loaded += v[0] * v[1];
		});

		percent = total ? loaded / total : 0;

		spans.eq(0).text(Math.round(percent * 100) + '%');
		spans.eq(1).css('width', Math.round(percent * 100) + '%');
		updateStatus();
	}

	function updateStatus() {
		var text = '',
			stats;

		if (state === 'ready') {
			text = '选中' + fileCount + '张图片，共' +
				WebUploader.formatSize(fileSize) + '。';
		} else if (state === 'confirm') {
			stats = uploader.getStats();
			if (stats.uploadFailNum) {
				text = '已成功上传' + stats.successNum + '张照片至XX相册，' +
					stats.uploadFailNum + '张照片上传失败，<a class="retry" href="#">重新上传</a>失败图片或<a class="ignore" href="#">忽略</a>'
			}

		} else {
			stats = uploader.getStats();
			text = '共' + fileCount + '张（' +
				WebUploader.formatSize(fileSize) +
				'），已上传' + stats.successNum + '张';

			if (stats.uploadFailNum) {
				text += '，失败' + stats.uploadFailNum + '张';
			}
		}

		$info.html(text);
	}

	function setState(val) {
		var file, stats;

		if (val === state) {
			return;
		}

		$upload.removeClass('state-' + state);
		$upload.addClass('state-' + val);
		state = val;

		switch (state) {
			case 'pedding':
				$placeHolder.removeClass('element-invisible');
				$queue.parent().removeClass('filled');
				$queue.hide();
				$statusBar.addClass('element-invisible');
				uploader.refresh();
				break;

			case 'ready':
				$placeHolder.addClass('element-invisible');
				$('#filePicker2').removeClass('element-invisible');
				$queue.parent().addClass('filled');
				$queue.show();
				$statusBar.removeClass('element-invisible');
				uploader.refresh();
				break;

			case 'uploading':
				$('#filePicker2').addClass('element-invisible');
				$progress.show();
				$upload.text('暂停上传');
				break;

			case 'paused':
				$progress.show();
				$upload.text('继续上传');
				break;

			case 'confirm':
				$progress.hide();
				$upload.text('开始上传').addClass('disabled');

				stats = uploader.getStats();
				if (stats.successNum && !stats.uploadFailNum) {
					setState('finish');
					return;
				}
				break;
			case 'finish':
				stats = uploader.getStats();
				if (stats.successNum) {
					//alert( '上传成功' );
				} else {
					// 没有成功的图片，重设
					state = 'done';
					location.reload();
				}
				break;
		}

		updateStatus();
	}

	uploader.onUploadProgress = function(file, percentage) {
		var $li = $('#' + file.id),
			$percent = $li.find('.progress span');

		$percent.css('width', percentage * 100 + '%');
		percentages[file.id][1] = percentage;
		updateTotalProgress();
	};

	uploader.onFileQueued = function(file) {
		fileCount++;
		fileSize += file.size;

		if (fileCount === 1) {
			$placeHolder.addClass('element-invisible');
			$statusBar.show();
		}

		addFile(file);
		setState('ready');
		updateTotalProgress();
	};

	uploader.onFileDequeued = function(file) {
		fileCount--;
		fileSize -= file.size;

		if (!fileCount) {
			setState('pedding');
		}

		removeFile(file);
		updateTotalProgress();

	};

	uploader.on('all', function(type) {
		var stats;
		switch (type) {
			case 'uploadFinished':
				setState('confirm');
				break;

			case 'startUpload':
				setState('uploading');
				break;

			case 'stopUpload':
				setState('paused');
				break;

		}
	});
	//上传成功后调用
	uploader.on('uploadSuccess', function(file, response) {
		//console.log("上传成功后，后台返回")
		//console.log(response)
		if (response.result == 'succeed') { //添加商品信息提交成功
			requestAllProduct();
			$('#btnCloseProductInfWin').trigger('click');
			swal("保存成功", "商品集已变更", "success")
		}
		uploader.removeFile(file);

	});
	uploader.on('uploadE')

	uploader.onError = function(code) {
		alert('Eroor: ' + code);
	};

	$upload.on('click', function() {
		if ($(this).hasClass('disabled')) {
			return false;
		}

		if (state === 'ready') {
			uploader.upload();
		} else if (state === 'paused') {
			uploader.upload();
		} else if (state === 'uploading') {
			uploader.stop();
		}
	});

	$info.on('click', '.retry', function() {
		uploader.retry();
	});

	$info.on('click', '.ignore', function() {
		alert('todo');
	});
	//上传前调用
	uploader.on('uploadBeforeSend', function(obj, data) {
		//console.log('uploader befor ')
		//console.log(newProductInf)
		//传入表单参数
		data = $.extend(data, {
			productName : newProductInf.productName //JSON.stringify(newProductInf)
		});
	});

	$upload.addClass('state-' + state);
	updateTotalProgress();
	return tempXXX;
}

//提交商品信息给后台
function submit() {
	//检查信息格式是否正确
	//newProductInf=checkInf();
	//console.log($('.imgWrap').length)
	newProductInf = checkInf();
	if (undefined == newProductInf) return;
	if (0 == $('.imgWrap').length) { //未选择图片
		//console.log('未上传图片,将调用自定义ajax调用')
		if (undefined == newProductInf.picture) newProductInf.picture = 'default.gif'; //默认图片
		saveProductInfByAjax(newProductInf);
	} else { //选择了图片
		uploaderX.upload();
	}
	//$('.uploadBtn').trigger('click')

}

//检验数据是否合规范,并获取整理后的json对象
function checkInf() {
	var temp = {
		productPrice : $('#newProductPrice').val(),
		// priceUnit: $('#newProductPriceUnit').val(),
		productTypeId : $('#newProductTypeId').val(),
		productName : $('#newProductName').val(),
		// grade: $('#newProductGrade').val(),
		productStock : $('#newProductStock').val(),
		productInfo : $('#newProductRemark').val(),
		// grade: $('#newProductGrade').val(),
		productIsShow : $('#showInMenu').is(':checked') ? 'yes' : 'no'
	};
	if (GlobalMethod.isEmpty(temp.productInfo)) temp.productInfo = '';
	if (-1 != currentProductId) {
		temp.proId = currentProductId;
		temp.picture = currentProductImg;
	}

	return temp;
}

//加载商品类型----------加载获取到的所有商品类型到select标签内
function loadProdcutTypeList(t) {
	var temp = $('#newProductTypeId');
	// temp.html('');
	for (var i = 0; i < t.length; i++) {
		var str = '';
		str += '<option value="' + t[i].productTypeId + '">' + t[i].productTypeName + '</option>';
		temp.append(str);
	}
}

//预处理商品信息界面------参数t是商品id，新增商品时赋值为-1
function readyProWin(t) {
	//console.log('readProWin...')
	if (t == -1) { //新增商品
		currentProductId = -1;
		$('#changeProPicBtn').hide();
		$('#myModal5Title').css('display', '');
		$('#myModal5Title').text("新增商品")
		$('#productPicture').attr('src', '')
		$('#productPictureDiv').hide();
		$('#updateProPicWindow').show();
		readyForProductInf();
		resetTargetProdut();
	} else { //修改商品
		currentProductId = t;
		requestTargetProduct(t);
		$('#myModal5Title').css('display', 'none');
		$('#updateProPicWindow').hide();
		$('#productPictureDiv').show();
		$('#changeProPicBtn').show();
		$('#changeProPicBtn').show();
	}
}
//加载指定商品的详细详细
function loadTargetProduct(t) {
	currentProductImg = t.picture;
	$('#productPicture').attr('src', '../../res/ProductImg/' + t.picture)
	$('#newProductName').val(t.name);
	$('#newProductTypeId').val(t.ptId);
	$('#newProductPrice').val(t.price);
	$('#newProductPriceUnit').val(t.priceUnit);
	$('#newProductRemark').val(t.remark);
	$('#newProductStock').val(t.stock);
	//$('#newProductGrade').val(t.grade);
	$("#showInMenu").prop("checked", 'yes' == t.isShow ? true : false); //使用attr控制状态选中失败，采用prop调用可行
}
//重置商品详情窗口
function resetTargetProdut() {
	$('#newProductName').val("");
	$('#newProductTypeId').val("-1");
	$('#newProductPrice').val("");
	$('#newProductPriceUnit').val("元");
	$('#newProductRemark').val("");
	$('#newProductStock').val("");
	//$('#newProductGrade').val('50');
	$('#needStock').prop('checked', true);
	$("#showInMenu").prop("checked", true);
}
//修改商品信息时，替换图片按钮对应的函数调用
function readyUpdatePicture() {
	//console.log('readyUpdatePicture...')
	var temp = $('#updateProPicWindow');
	if (temp.css('display') == 'none') { //打开图片上传模块
		$('#updateProPicWindow').show();
		closeUploader();
		uploaderX.destroy();
		uploaderX = readyForWebUploader();
		closeUploader();
	} else { //关闭图片上传模块
		$('#updateProPicWindow').hide();
	}
}

//获取给定编号商品类型的信息
function getTargetProductType(t) {
	var i = 0;
	for (; i < productTypeList.length; i++)
		if (t == productTypeList[i].id) return productTypeList[i];
	return undefined;
}

//转换商品集的部分参数
function convertArgsOfPL(tPL) {
	var i, k, typeIdName;
	for (i = 0; i < tPL.length; i++) {
		k = getTargetProductType(tPL[i].ptId);
		if (k == undefined) typeIdName = '未指定';
		else typeIdName = k.name;
		tPL[i].typeIdName = typeIdName;
		tPL[i].isShowState = tPL[i].isShow == 'yes' ? '是' : '否';
	}
	return tPL;
}

//按要求过滤编号小于100的商品---------无奈，哭唧唧
function filterProductList(t) {
	var i = 0;
	while (i < t.length) {
		if (t[i].id < 100) {
			t.splice(i, 1);
			i--;
		}
		i++;
	}
	return t;
}

//商品集条件筛选中 “已展示”、“未展示”按钮的效果监控
var checkBox_isHasShown = $('#isHasShown');
var checkBox_notHasShown = $('#notHasShown');

function hasShown(t) {
	var temp;
	if ('is' == t) {
		temp = checkBox_isHasShown.attr('class');
		if (temp == 'icheckbox_square-green checked')
			temp = 'icheckbox_square-green';
		else temp = 'icheckbox_square-green checked';
		checkBox_isHasShown.attr('class', temp);
	} else if ('not' == t) {
		temp = checkBox_notHasShown.attr('class');
		if (temp == 'icheckbox_square-green checked')
			temp = 'icheckbox_square-green';
		else temp = 'icheckbox_square-green checked';
		checkBox_notHasShown.attr('class', temp);
	}
}
//重置商品集筛选条件
function resetFilterCondition() {
	$('#filterProductTypeList').val('全部');
	$('#isHasShown').attr('class', 'icheckbox_square-green checked');
	$('#notHasShown').attr('class', 'icheckbox_square-green checked');
	requestAllProduct();
}

//商品类型排序
function sortProductTypeByGrade(t) {
	var i, temp;
	var result = [];
	while (t.length > 0) {
		for (temp = 0, i = 1; i < t.length; i++) {
			if (t[temp].grade < t[i].grade) temp = i;
		}
		result[result.length] = t[temp];
		t.splice(temp, 1);
	}
	return result;
}

//在商品过滤条件中加载商品类型集
function loadFilterProductTypeList(t) {
	var target = $('#filterProductTypeList');
	var temp = '',
		i = 0;
	for (; i < t.length; i++) {
		temp += '<option value="' + t[i].productTypeId + '" >' + t[i].productTypeName + '</option>';
	}
	target.append(temp);
}

//获取商品过滤条件的数据
function getProductFilterCondition() {
	var result = {
		productTypeName: $('#filterProductTypeList').val(),
		isShow: ($('#isHasShown').attr('class') == 'icheckbox_square-green checked' ? 'yes' : 'no'),
		notShow: ($('#notHasShown').attr('class') == 'icheckbox_square-green checked' ? 'yes' : 'no')
	}
	return result;
}

//按条件过滤出指定的商品集合
function filterProductListByCondition(t) {
	var condition = getProductFilterCondition();
	var i, acIs, acNot, acName;
	acIs = condition.isShow == 'yes' ? true : false;
	acNot = condition.notShow == 'yes' ? true : false;

	for (i = 0; i < t.length; i++) {
		if ((condition.productTypeName == '全部' || condition.productTypeName == t[i].typeIdName)) {
			if (acIs && t[i].isShow == 'yes')
				continue;
			if (acNot && t[i].isShow == 'no')
				continue;
		}
		t.splice(i, 1);
		i--;
	}
	return t;
}

//加载商品过滤条件到商品集的标题
function loadFilterConditionToTitle() {
	var condition = getProductFilterCondition();
	var target = $('#productListTitle');
	target.text('当前筛选条件：' + condition.productTypeName + ' - ' + (condition.isShow == 'yes' ? '已展示' : '') + ' - ' + (condition.notShow == 'yes' ? '未展示' : ''))
}

//统计当前显示的商品集的信息
function statisticInfOfProductList(t) {
	var i;
	var result = {
		amount: 0,
		amount_isShow: 0,
		amount_stock: 0,
		amount_picture: 0,
		amount_type: 0
	}
	for (i = 0; i < t.length; i++) {
		if (t[i].isShow == 'yes') result.amount_isShow++;
		if (t[i].stock == 0) result.amount_stock++;
		if (t[i].picture == 'default.gif') result.amount_picture++;
		if (t[i].typeIdName == '未指定') result.amount_type++;
	}
	result.amount = t.length;
	return result;
}

// 统计并加载当前显示的商品集的简要信息
function loadInfOfProductList(t) {
	var temp = statisticInfOfProductList(t);
	$('#infProAmount').text(temp.amount);
	$('#infProAmount_isShow').text(temp.amount_isShow);
	$('#infProAmount_stock').text(temp.amount_stock);
	$('#infProAmount_picture').text(temp.amount_picture);
	$('#infProAmount_type').text(temp.amount_type);
}
// 库存字段的控制函数
function onNeedStockChange() {
	var target = $('#needStock');
	if (target.is(':checked')) {
		$('#newProductStock').val('');
		$('#newProductStock').prop('readOnly', 'true');
		$('#stockLabel').removeClass('red-font');
	} else {
		$('#newProductStock').val('100');
		$('#newProductStock').removeAttr('readOnly');
		$('#stockLabel').addClass('red-font');
	}
}



//************************************************************************************************************************** ajax请求
//请求商品类型并加载-----------ajax请求，获取所有商品类型
function requestAllProductType() {
	$.ajax({
		//url: url + "/Vita_Back/queryAllProductType",
		url : GlobalConfig.serverAddress + "/mProductType/all",
		type: 'GET',
		cache: false,
		dataType:'json',
		//dataType: 'text',
		contentType: "application/json; charset=utf-8",
		// data: JSON.stringify({}),
		//data:temp,
		//processData: false,
		//contentType: false,
		success: function(data) {
			console.log(data);
			if (data.code == 0) {
				// console.log(data.data);
				loadProdcutTypeList(data.data);
				loadFilterProductTypeList(data.data);

			} else {
				alert(data.desc);
				//swal(data.desc, '', 'error');
			}
			return;

		}
	});
}
//请求所有商品数据，并加载到页面-------------------ajax请求，获取所有商品数据
function requestAllProduct() {
	$.ajax({
		url: url + "/Vita_Back/queryAllProduct",
		type: 'POST',
		cache: false,
		//dataType:'json',
		dataType: 'text',
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify({}),
		//data:temp,
		//processData: false,
		//contentType: false,
		success: function(data) {
			//将json字符串转换为json对象
			data = JSON.parse(data);
			//console.log(data);
			if (data.result == 'succeed') { //获取成功
				var temp = JSON.parse(data.inf); //将inf字段转换为json对象
				temp = filterProductList(temp); //过滤掉编号小于100的数据
				temp = convertArgsOfPL(temp); //添加部分参数
				temp = filterProductListByCondition(temp); //按照条件过滤商品集
				loadFilterConditionToTitle()
				loadProductTable(temp) //将获取到的数据交给指定函数进行加载，显示在页面上
				loadInfOfProductList(temp)
			} else if (data.result == 'default') { //获取失败
			}
		}
	});
}
//请求指定编号的商品信息
function requestTargetProduct(t) {
	$.ajax({
		url: url + "/Vita_Back/getTargetProduct",
		type: 'POST',
		cache: false,
		//dataType:'json',
		dataType: 'text',
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify({
			proId: t
		}),
		//data:temp,
		//processData: false,
		//contentType: false,
		success: function(data) {
			//将json字符串转换为json对象
			data = JSON.parse(data);
			//console.log(data);
			if (data.result == 'succeed') { //获取成功
				var temp = JSON.parse(data.inf); //将inf字段转换为json对象
				//loadProductTable(temp)//将获取到的数据交给指定函数进行加载，显示在页面上
				loadTargetProduct(temp)
			} else if (data.result == 'default') { //获取失败
			}
		}
	});
}
//保持商品信息（新增或者修改）
function saveProductInfByAjax(t) {
	$.ajax({
		url: GlobalConfig.serverAddress + "/mProduct/add",
		type: 'POST',
		cache: false,
		dataType:'json',
		// dataType: 'text',
		contentType: "application/json; charset=utf-8",
		// contentType: "application/x-www-form-urlencoded;charset=utf-8",
		// contentType : 'multipart/form-data;',
		data: JSON.stringify(t),
		success: function(data) {
			if (data.code == 0) {
				swal('保存成功', '', 'success');
				return;
			}
			return;
			if (data.result == 'succeed') { //获取成功
				//var temp = JSON.parse(data.inf); //将inf字段转换为json对象
				requestAllProduct();
				$('#btnCloseProductInfWin').trigger('click');
				swal("保存成功", "商品集已变更", "success");
			} else if (data.result == 'default') { //获取失败
				swal("保存失败", "商品集未变更", "error")
			}
		}
	});
}
//删除指定商品
function deleteProduct(t) {
	$.ajax({
		url: url + "/Vita_Back/deleteTheProduct",
		type: 'POST',
		cache: false,
		//dataType:'json',
		dataType: 'text',
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify({
			proId: t
		}),
		//data:temp,
		//processData: false,
		//contentType: false,
		success: function(data) {
			//将json字符串转换为json对象
			data = JSON.parse(data);
			//console.log(data);
			if (data.result == 'succeed') { //获取成功
				//var temp = JSON.parse(data.inf); //将inf字段转换为json对象
				requestAllProduct();
				//$('#btnCloseProductInfWin').trigger('click');
				swal("删除成功", "商品集已变更", "success");
			} else if (data.result == 'default') { //获取失败
				swal("删除失败", "商品集未变更", "error")
			}
		}
	});
}

//**************************************************************************************************************************测试函数
function testFun() {

	var temp = [{
		productId: "10010",
		productTypeId: "1010",
		productTypeName: "家常菜",
		productAttributeId: "1010",
		productAttributeName: "菜品",
		productName: "红烧肉",
		productGrade: "50",
		productPrice: "43.00",
		productStockFlag: "no",
		productStock: "0",
		productStockStatus: "X",
		productIsShow: "yes",
		productPicture: "1.png",
		productInfo: "新鲜农家土猪肉"
	}, {
		productId: "10011",
		productTypeId: "1010",
		productTypeName: "家常菜",
		productAttributeId: "1011",
		productAttributeName: "炒菜",
		productName: "上海青",
		productGrade: "50",
		productPrice: "18.00",
		productStockFlag: "yes",
		productStock: "10",
		productStockStatus: "10",
		productIsShow: "yes",
		productPicture: "1.png",
		productInfo: "新鲜健康蔬菜"
	}]
	loadProductTable(temp);
}