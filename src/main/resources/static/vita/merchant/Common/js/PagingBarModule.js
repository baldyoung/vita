// 分页组件 - 分页条

function pagingBarModuleTestRun(param) {
	console.log('PagingBarModule -> ' + param.currentPageIndex + ", " + param.maxAmountOfData);
}

var PagingBarModule = {
	// --- 必要参数
	pageOptionalAreaId: undefined, // 分页操作区的id
	totalAmountOfData: undefined, // 被分页数据的总数
	maxNumberOfDisplayPageButton: 5, // 分页按钮的最大展示数量
	currentPageIndex: undefined, // 当前展示的页标（第一页为1）
	maxAmountOfOnePage: 10, // 每一页的数据量
	maxPageIndex: undefined, // 最大的页标值
	run: pagingBarModuleTestRun, // 页标改变后会被调用的函数，且该函数的第一个参数会被传入新的页标值
	// --- 定制参数
	lastBtnCss: '',
	nextBtnCss: '',
	pageBtnCss: '',
	lastBtnStyle: 'margin-right: 5px; display: inline-block;height: 38px;line-height: 38px;padding: 0 18px;background-color: rgb(30, 159, 255);color: #fff;white-space: nowrap;text-align: center;font-size: 14px;border: none;border-radius: 2px;cursor: pointer;',
	nextBtnStyle: 'display: inline-block;height: 38px;line-height: 38px;padding: 0 18px;background-color: rgb(30, 159, 255);color: #fff;white-space: nowrap;text-align: center;font-size: 14px;border: none;border-radius: 2px;cursor: pointer;',
	pageBtnStyle: 'margin-right: 5px; display: inline-block;height: 38px;line-height: 38px;padding: 0 18px;background-color: rgb(30, 159, 255);color: #fff;white-space: nowrap;text-align: center;font-size: 14px;border: none;border-radius: 2px;cursor: pointer;',
	// --- 相关调用
	build: function(param) {
		PagingBarModule.pageOptionalAreaId = param.pageOptionalAreaId;
		PagingBarModule.totalAmountOfData = param.totalAmountOfData;
		if (undefined == PagingBarModule.pageOptionalAreaId || undefined == PagingBarModule.totalAmountOfData) {
			console.log("error : PagingBarModule -> build 'pageOptionalAreaId' or 'totalAmountOfData' is null!");
			return;
		}
		if (undefined != param.maxNumberOfDisplayPageButton) {
			PagingBarModule.maxNumberOfDisplayPageButton = param.maxNumberOfDisplayPageButton;
		}
		if (undefined != param.maxAmountOfOnePage) {
			PagingBarModule.maxAmountOfOnePage = param.maxAmountOfOnePage;
		}
		if (undefined != param.run) {
			PagingBarModule.run = param.run;
		}
		if (undefined != param.lastBtnCss) {
			PagingBarModule.lastBtnCss = param.lastBtnCss;
		}
		if (undefined != param.nextBtnCss) {
			PagingBarModule.nextBtnCss = param.nextBtnCss;
		}
		if (undefined != param.pageBtnCss) {
			PagingBarModule.pageBtnCss = param.pageBtnCss;
		}
		PagingBarModule.maxPageIndex = Math.ceil(PagingBarModule.totalAmountOfData / PagingBarModule.maxAmountOfOnePage);
		if (PagingBarModule.maxPageIndex < PagingBarModule.maxNumberOfDisplayPageButton) {
			PagingBarModule.maxNumberOfDisplayPageButton = PagingBarModule.maxPageIndex;
		}
		var currentPageIndex = 1;
		if (undefined != param.loadPageIndex) {
			currentPageIndex = param.loadPageIndex;
		}
		PagingBarModule.createOptionalButton();
		if (0 == PagingBarModule.totalAmountOfData) {
			$('#lastPageBtn').css('cursor', 'not-allowed');
			$('#lastPageBtn').css('background', '#c3c9cf');
			$('#nextPageBtn').css('cursor', 'not-allowed');
			$('#nextPageBtn').css('background', '#c3c9cf');
		}
		PagingBarModule.loadTargetPage(currentPageIndex);
	},
	// --- 内部调用
	createOptionalButton: function() {
		var target = $('#' + PagingBarModule.pageOptionalAreaId);
		var str = "<button id='lastPageBtn' style='" + PagingBarModule.lastBtnStyle + "'" + (PagingBarModule.lastBtnCss != undefined ? ("class='" + PagingBarModule.lastBtnCss + "'") : "") + " onclick='PagingBarModule.loadLastPage()' >上一页</button>";
		str += "<span id='pageNumberArea' >";
		for (var i = 1; i <= PagingBarModule.maxNumberOfDisplayPageButton; i++) {
			str += "<button id='pageNumber" + i + "' " + (PagingBarModule.pageBtnCss != undefined ? ("class='" + PagingBarModule.pageBtnCss + "'") : "") + "  style='" + PagingBarModule.pageBtnStyle + "' onclick='PagingBarModule.loadPageOfBtn(" + i + ")' >" + i + "</button>";
		}
		str += "</span>";
		str += "<button id='nextPageBtn' " + (PagingBarModule.nextBtnCss != undefined ? ("class='" + PagingBarModule.nextBtnCss + "'") : "") + "  style='" + PagingBarModule.nextBtnStyle + "' onclick='PagingBarModule.loadNextPage()' >下一页</button>";
		target.html(str);
	},
	loadPageOfBtn: function(btnId) {
		var temp = $('#pageNumber' + btnId);
		PagingBarModule.loadTargetPage(parseInt(temp.text()));
	},
	loadLastPage: function() {
		PagingBarModule.loadTargetPage(PagingBarModule.currentPageIndex - 1);
	},
	loadNextPage: function() {
		PagingBarModule.loadTargetPage(PagingBarModule.currentPageIndex + 1);
	},
	loadTargetPage: function(index) {
		var isInvalid = PagingBarModule.currentPageIndex == index || 1 > index || PagingBarModule.maxPageIndex < index || 0 == PagingBarModule.totalAmountOfData;
		if (isInvalid) {
			return;
		}
		PagingBarModule.currentPageIndex = index;
		var requestData = {
			currentPageIndex: PagingBarModule.currentPageIndex,
			maxAmountOfData: PagingBarModule.maxAmountOfOnePage
		};
		//将请求页的序号与数量发送给后台，后台进行处理及响应，后台返回最新数据包括（被分页的数据的总条数、...）
		//...（这里应该进行ajax请求调用）
		if (undefined != PagingBarModule.run) {
			PagingBarModule.run(requestData);
		} else {
			console.log('warning : PagingBarModule -> loadTargetPage "PagingBarModule.run" is undefined.');
			return;
		}
		//获取成功后由前端进行处理，并改变指定页所对应按钮的状态
		PagingBarModule.updateBtnStatus();
	},
	updateBtnStatus: function() {
		var i,
			temp;
		var currentIndex = PagingBarModule.currentPageIndex;
		var currentMinIndex = parseInt($('#pageNumber1').text()),
			currentMaxIndex = parseInt($('#pageNumber' + PagingBarModule.maxNumberOfDisplayPageButton).text());
		// 更新页标按钮的状态
		if (currentIndex < currentMinIndex) {
			for (i = 1; i <= PagingBarModule.maxNumberOfDisplayPageButton; i++) {
				temp = $('#pageNumber' + i);
				temp.css('background', '#1E9FFF');
				temp.text(i - 1 + currentIndex);
			}
			$('#pageNumber1').css('background', '#ffc361');
		} else if (currentIndex > currentMaxIndex) {
			for (i = PagingBarModule.maxNumberOfDisplayPageButton; i > 0; i--) {
				temp = $('#pageNumber' + i);
				temp.css('background', '#1E9FFF');
				temp.text(currentIndex - (PagingBarModule.maxNumberOfDisplayPageButton - i));
			}
			$('#pageNumber' + PagingBarModule.maxNumberOfDisplayPageButton).css('background', '#ffc361');
		} else {
			for (i = 1; i <= PagingBarModule.maxNumberOfDisplayPageButton; i++) {
				temp = $('#pageNumber' + i);
				if (currentIndex == temp.text()) {
					temp.css('background', '#ffc361');
					continue;
				}
				temp.css('background', '#1E9FFF');
			}
		}
		// 更新“上一页”、“下一页”两个按钮的状态
		if (1 != PagingBarModule.maxPageIndex) {
			$('#nextPageBtn').css('cursor', 'pointer');
			$('#nextPageBtn').css('background', '#1E9FFF');
			$('#lastPageBtn').css('cursor', 'pointer');
			$('#lastPageBtn').css('background', '#1E9FFF');
		}
		if (PagingBarModule.currentPageIndex == 1) {
			$('#lastPageBtn').css('cursor', 'not-allowed');
			$('#lastPageBtn').css('background', '#c3c9cf');
		}
		if (PagingBarModule.currentPageIndex == PagingBarModule.maxPageIndex) {
			$('#nextPageBtn').css('cursor', 'not-allowed');
			$('#nextPageBtn').css('background', '#c3c9cf');
		}
	}

}

