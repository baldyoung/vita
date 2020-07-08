

var MyAudioModelCell ;//= createAudioModel(false);
//控制是否需要声音提醒
var isPlayAudio ;// = true; 

var url = "/Vita_war_exploded"

//订单类型集合
var OrderFormTypeList = [];
//订单集合
var OrderFormList = [];
//订单详情列的集合
var OrderFormDataList = [];
//当前打开的订单编号
var currentOFId = undefined;
//当前打开的订单组编号
var currentOFTId = undefined;

//与主iframe中的声音按钮同步参数
function updatePlayAudioState(){
	//console.log("closePlayAudio:");
	var ACI = window.parent.window.document.getElementById('AudioActionIcon');
	var t = ACI.className == "glyphicon glyphicon-volume-off" ? false : true;
	isPlayAudio=t;
	if(false == t)
		closeAudioLoop();
}


$(document).ready(function () {
	
	//openFullScreen();

    getListOfOFT();
	//声音模块初始化
	MyAudioModelCell = createAudioModel(false);
	//设置所要播放的音乐
	MyAudioModelCell.selectAudio("audio/1.mp3");
	//控制是否需要声音提醒
	isPlayAudio = true; 
	updatePlayAudioState();
	
    //setInterval("refreshOFT()",5000);


});
//***********************************************************************************************************************************************start version X
//打开指定订单组
function toRoom(t){
	window.open(url+"/Vita_Front/productMenu/ProductMenu.html?oftId="+t);
}


//构造指定订单组界面HTML代码
function createRoomHTML(t){
    var html = "";
    html+="<div id='Room"+t.id+"' class=\"col-sm-4\" >"
    html+="<div class=\"ibox\" style=' '>"
    html+="<div class=\"ibox-title\">"
    html+="<span id='RoomState"+t.id+"' class=\"label label-primary pull-right\">空闲</span>"
    html+="<h5>"+t.name+"&nbsp;&nbsp;-&nbsp;&nbsp;可容纳用餐人数:"+t.maxPeopleNum+"</h5>"
    html+="</div>"
    html+="<div class=\"ibox-content\">"
    html+="<div class=\"team-members\">"
    if(t.limitNum!=1)
        html+="<button class=\"btn btn-info \" type=\"button\" data-toggle=\"modal\" data-target=\"#myModal\" onclick='openTakeoutWin(\""+t.id+"\")' ><i class=\"fa fa-paste\"></i> 订单详情<span id='RoomMsgTip"+t.id+"' class=\"badge badge-danger\" style='background:#ED5565; color:black;  left:4px; display:none; padding:0 0 0 0; '>0</span></button>"
    else
        html+="<button class=\"btn btn-info \" type=\"button\" data-toggle=\"modal\" data-target=\"#OrderFormWindow\" onclick='openOFWin(\""+t.id+"\")' ><i class=\"fa fa-paste\"></i> 订单详情<span id='RoomMsgTip"+t.id+"' class=\"badge badge-danger\" style='background:#ED5565; color:black;  left:4px; display:none; padding:0 0 0 0;  '>0</span></button>"
    html+="<button class=\"btn btn-primary \" type=\"button\" style='margin-left:5px; ' onclick=\"toRoom('"+t.id+"')\"><i class=\"fa fa-building-o\"></i>进入"
    html+="</button>"
    html+="</div>"
    html+="<h4>备注</h4>"
    html+="<textarea style='resize:none; height:80px; width:100%; '   >"+t.oftRemark+"</textarea>"
    //html+="<div>"
    //html+="<span>当前项目进度：</span>"
    //html+="<div id='proProcessTip"+t.id+"' class=\"stat-percent\">0%</div>"
    //html+="<div class=\"progress progress-mini\">"
    //html+="<div id='proProcess"+t.id+"'style=\"width: 0%;\" class=\"progress-bar\"></div>"
    //html+="</div>"
    //html+="</div>"
    html+="<div class=\"row  m-t-sm\">"
    html+="<div class=\"col-sm-4\">"
    html+="<div  class=\"font-bold\">客人数量</div>"
    html+="<div id='RoomPeopleNum"+t.id+"' >0</div>"
    html+="</div>"
    html+="<div class=\"col-sm-4\">"
    html+="<div  class=\"font-bold\">商品数量</div>"
    html+="<div id='RoomProNum"+t.id+"' >0</div>"
    html+="</div>"
    html+="<div class=\"col-sm-4 text-right\">"
    html+="<div class=\"font-bold\">订单预算</div>"
    html+="<div id='RoomTotalAmount"+t.id+"'>0</div>"
    html+="</div>"
    html+="</div>"
    html+="</div>"
    html+="</div>"
    html+="</div>"
    return html;
}
//加载指定数据的订单组模型
function loadRoomList(t){
    var roomList = $('#RoomList');
    var temp ;
    var i=0;
    roomList.html('');
    for(i=0;i<t.length;i++){
        temp = createRoomHTML(t[i]);
        roomList.append(temp);
    }
    //refreshRoomList();
}
//获取订单组数据并加载对应模型
function getListOfOFT() {
    $.ajax({
        url: url + "/Vita_Back/getListOfOFT",
        type: 'POST',
        cache: false,
        //dataType:'json',
        dataType: 'text',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({}),
        //data:temp,
        //processData: false,
        //contentType: false,
        success: function (data) {
            //将json字符串转换为json对象
            data = JSON.parse(data);
            //console.log(data);
            if (data.result == 'succeed') {
                var k = JSON.parse(data.inf);
                loadRoomList(k);
                refreshOFT();
            } else if (data.result == 'default') {
            }
        }
    });
}

//加载订单组界面内容
function loadOFT(t){
    //console.log("--->loadOFT");
    //console.log(t);
	var acgA = false; //是否有新消息
    var i, temp;
    for(i=0;i<t.length;i++){
        temp = $('#RoomMsgTip'+t[i].id);
        $('#RoomPeopleNum'+t[i].id).text(t[i].peopleNum)
        $('#RoomProNum'+t[i].id).text(t[i].proNum)
        $('#RoomTotalAmount'+t[i].id).text(t[i].totalAmount)

        if(t[i].newMsgNum>0){//有新消息
			acgA = true;
            temp.text(t[i].newMsgNum);
            temp.show();
        }else{//无新消息
            temp.text(0);
            temp.hide();
        }
        temp = $('#RoomState'+t[i].id)
        if(t[i].hasOF=='yes'){//该订单组有订单
            var cls = "label label-warning pull-right";
            temp.attr("class", "label label-warning pull-right");
            temp.text("已用")
        }else{//该订单组下无订单
            temp.attr("class", "label label-primary pull-right");
            temp.text("空闲")
        }
    }
	if(true == acgA && true == isPlayAudio){
		startAudioLoop();
	}else{
		closeAudioLoop();
	}
}

//获取并加载订单组界面内容
function refreshOFT(){
    $.ajax({
        url: url + "/Vita_Back/refreshOFT",
        type: 'POST',
        cache: false,
        //dataType:'json',
        dataType: 'text',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({}),
        //data:temp,
        //processData: false,
        //contentType: false,
        success: function (data) {
            //将json字符串转换为json对象
            data = JSON.parse(data);
            //console.log(data);
            if (data.result == 'succeed') {
                var k = JSON.parse(data.inf);
                //console.log("--->refreshOFT");
                //console.log(k);
                loadOFT(k);
            } else if (data.result == 'default') {
				closeAudioLoop();
            }
        }
    });
}
//加载订单集合 -- 外卖组的订单列加载用
function loadOrderFormList(t){
    //console.log("loadOrderFormList..")
    //console.log(t)
    var dataTable = $('#OrderFormList');
    //if(undefined == dataTable) alert("dataTable is undefined")
    if ($.fn.dataTable.isDataTable(dataTable)) {
        dataTable.DataTable().destroy();
    }
    dataTable.DataTable({
        'searching':false, //去掉搜索框
        'bLengthChange': false, //去掉每页显示多少条数据方法
        "serverSide": false, //关闭分页操作，默认就是关闭
        "autoWidth" : false, //
        "bSort": true, //打开排序功能
        "order": [[ 0, "desc" ]],
        'data':t, //表格的数据源
        'columns':[{
            data:'id'
        },{
            data:'ofProQuantity'
        },{
            data:'ofTotalAmount'
        },{
            data:'ofTime'
        }
        ],
        "columnDefs": [{
            "render" : function(data, type, row) {
                var a = "";
                //a += "<button type='button' class='btn btn-primary' onclick='showEditStudent(\""+0+"\")' data-toggle='modal' data-target='#myModal5' title='编辑用户' data-toggle='dropdown' style='margin-right:15px; margin-bottom: -1px;'><i class='fa fa-pencil-square-o'></i>&nbsp;编辑</button>"
                //a += "<button type='button' class='btn btn-primary' style='margin-top:3px;' onclick='loadOrderForm(\""+row.id+"\")'  data-toggle='dropdown' data-target=\"#OrderFormWindow\" style='margin-right:15px; margin-bottom: -1px;'><i class='fa fa-building-o'></i>&nbsp;查看</button>"
                //a += "<button type='button' class='btn btn-primary' onclick='courseList(\""+row.id+"\")' data-toggle='modal' data-target='#courseListDialog' title='课程列表' data-toggle='dropdown' style='margin-right:15px; margin-bottom: -1px;'><i class='fa fa-list'></i>&nbsp;课程列表</button>"
                a+="<button class=\"btn btn-primary \" type=\"button\" data-toggle=\"modal\" data-target=\"#OrderFormWindow\" onclick=\"openOFWinW('"+row.id+"')\"><i class=\"fa fa-building-o\"></i>查看"
                if(row.newMsgNum>0)
                    a+="<span class=\"badge badge-danger\" style='background:#ED5565; color:black;  left:4px; '>"+row.newMsgNum+"</span>"
                a+="</button>"
                
                return a;
            },
            "targets" :4
        }]
    })
}
//获取并加载外卖组订单列
function openTakeoutWin(t){
    //console.log("openTakeoutWin..")
    //console.log(t)
    $.ajax({
        url: url + "/Vita_Back/getTargetListOfOF",
        type: 'POST',
        cache: false,
        //dataType:'json',
        dataType: 'text',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({oftId:t}),
        //data:temp,
        //processData: false,
        //contentType: false,
        success: function (data) {
            //将json字符串转换为json对象
            data = JSON.parse(data);
            //console.log(data);
            if (data.result == 'succeed') {
                var k = JSON.parse(data.inf);
                currentOFTId=t;
                loadOrderFormList(k)
            } else if (data.result == 'default') {
            }
        }
    });
}
//清空订单窗口的内容
function resetOrderFormData(){
    $('#ofId').val("")
    $('#ofTime').val("")
    $('#oftName').val("")
    $('#ofUser').val("")
    $('#ofProQuantity').val("")
    $('#ofTotalAmount').val("")
    var dataTable = $('#OrderFormData');
    //if(undefined == dataTable) alert("dataTable is undefined")
    if ($.fn.dataTable.isDataTable(dataTable)) {
        dataTable.DataTable().destroy();
    }
}
//加载指定订单的订单窗口内容
function loadOrderForm(t){
    //console.log('loadOrderForm..')
    //console.log(t)
    //t = getTargetOF(t)
    resetOrderFormData();
    if(undefined==t || undefined==t.id){
        //currentOFId=undefined;
        //resetOrderFormData();
        swal("当前无订单！", "", "error");
		$("#OrderFormWindowBtnClose").trigger("click"); //模拟点击关闭按钮
        $('.confirm').one("click", function(){ //一次性监听器
            $("#OrderFormWindowBtnClose").trigger("click"); //模拟点击关闭按钮
        });
        return ;
    }
    currentOFId=t.id;
    $('#ofId').val(t.id)
    $('#ofTime').val(t.ofTime)
    $('#oftName').val(t.ofTypeName)
    $('#ofUser').val(t.userName)
    $('#ofProQuantity').val(t.ofProQuantity)
    $('#ofTotalAmount').val(t.ofTotalAmount)
    $('#customerRemark').val(t.customerRemark)
    $('#merchantRemark').val(t.merchantRemark)
    //loadOrderFormData(t.data);
}
//获取并加载普通组订单内容 --- t 是订单类型编号
function openOFWin(t){
    //console.log('openOFWin..')
    //console.log(t)
    //currentOFTId=t;
    resetOrderFormData()
    $.ajax({
        url: url + "/Vita_Back/getTargetOF",
        type: 'POST',
        cache: false,
        //dataType:'json',
        dataType: 'text',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({oftId:t}),
        //data:temp,
        //processData: false,
        //contentType: false,
        success: function (data) {
            //将json字符串转换为json对象
            data = JSON.parse(data);
            //console.log(data);
            if (data.result == 'succeed') {
                var k = JSON.parse(data.inf);
                loadOrderForm(k)
                currentOFId=k.id;
                requestOFD(k.id)
            } else if (data.result == 'default') {
            }
        }
    });
    /*var oft = getTargetOFT(t);
    loadOrderForm(oft.ofList[0]);*/
}
//获取并加载外卖组订单内容 --- t 是订单编号
function openOFWinW(t){
    //console.log('openOFWin..')
    //console.log(t)
    resetOrderFormData()
    $.ajax({
        url: url + "/Vita_Back/getTargetOF",
        type: 'POST',
        cache: false,
        //dataType:'json',
        dataType: 'text',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({ofId:t}),
        //data:temp,
        //processData: false,
        //contentType: false,
        success: function (data) {
            //将json字符串转换为json对象
            data = JSON.parse(data);
            //console.log(data);
            if (data.result == 'succeed') {
                //currentOFTId=t;
                var k = JSON.parse(data.inf);
                loadOrderForm(k)
                //console.log("########")
                //console.log(k);
                //console.log(k.id);
                currentOFId=k.id;
                requestOFD(k.id)
            } else if (data.result == 'default') {
            }
        }
    });
    /*var oft = getTargetOFT(t);
    loadOrderForm(oft.ofList[0]);*/
}
//加载订单详情内容
function loadOrderFormData(t){
    //console.log('loadOrderFormData..')
    //console.log(t)
    var Number=1;
    if(undefined == t) t=[];
    for(var i=0;i<t.length;i++)
        t[i].Number=Number++;
    var dataTable = $('#OrderFormData');
    dataTable.DataTable({

        'searching':false, //去掉搜索框
        'bLengthChange': false, //去掉每页显示多少条数据方法
        "serverSide": false, //关闭分页操作，默认就是关闭
        "autoWidth" : false, //
        "bSort": true, //打开排序功能
        "order": [[ 0, "asc" ]],
        'destroy':true,
        'data':t, //表格的数据源
        'columns':[{
            data:'Number'
        },{
            data:'pId'
        },{
            data:'pName'
        },{
            data:'pPrice'
        },{
            data:'pQuantity'
        },{
            data:'pAmount'
        }
        ],
        "columnDefs": [{
            "render" : function(data, type, row) {
                var a = "";
                /*a += "<button type='button' class='btn btn-primary' onclick='showEditStudent(\""+0+"\")' data-toggle='modal' data-target='#myModal5' title='编辑用户' data-toggle='dropdown' style='margin-right:15px; margin-bottom: -1px;'><i class='fa fa-pencil-square-o'></i>&nbsp;编辑</button>"
                */a += "<button id='btnDel"+row.id+"' type='button' class='btn btn-primary' style='margin-top:3px;' onclick='deleteOFD(\""+row.id+"\")' title='删除' data-toggle='dropdown' style='margin-right:15px; margin-bottom: -1px;'><i class='fa fa-remove'></i>&nbsp;删除</button>"
                //a += "<button type='button' class='btn btn-primary' onclick='courseList(\""+row.id+"\")' data-toggle='modal' data-target='#courseListDialog' title='课程列表' data-toggle='dropdown' style='margin-right:15px; margin-bottom: -1px;'><i class='fa fa-list'></i>&nbsp;课程列表</button>"
                if(row.hasRead=='no')
                    a += "<button id='btnRead"+row.id+"' type='button' class='btn btn-primary' style='margin-top:3px; margin-left:2px;' onclick='setHasRead(\""+row.id+"\")'  data-toggle='dropdown' style='margin-right:15px; margin-bottom: -1px;'><i class='fa fa-check-circle'></i>&nbsp;已阅</button>"
                return a;
            },
            "targets" :6
        }]
    })
}
//获取并加载订单详情内容 --- t是订单编号
function requestOFD(t){
    $.ajax({
        url: url + "/Vita_Back/getTargetListOfOFD",
        type: 'POST',
        cache: false,
        //dataType:'json',
        dataType: 'text',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({ofId:t}),
        //data:temp,
        //processData: false,
        //contentType: false,
        success: function (data) {
            //将json字符串转换为json对象
            data = JSON.parse(data);
            //console.log(data);
            if (data.result == 'succeed') {
                var k = JSON.parse(data.inf);
                loadOrderFormData(k)
            } else if (data.result == 'default') {
            }
        }
    });
}




//对新的商品详情的已阅标记
function setHasRead(t){
    RsetReadOFD(t);
}
//标记订单详情列为已阅
function RsetReadOFD(t){
    $.ajax({
        url: url + "/Vita_Back/setReadOFD",
        type: 'POST',
        cache: false,
        //dataType:'json',
        dataType: 'text',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({ofdId:t}),
        //data:temp,
        //processData: false,
        //contentType: false,
        success: function (data) {
            //将json字符串转换为json对象
            data = JSON.parse(data);
            //console.log(data);
            if (data.result == 'succeed') {
                $('#btnRead'+t).hide();
				closeAudioLoop();
            } else if (data.result == 'default') {
            }
        }
    });
}
//删除指定订单详情列
function deleteOFD(t){
    $.ajax({
        url: url + "/Vita_Back/deleteOFD",
        type: 'POST',
        cache: false,
        //dataType:'json',
        dataType: 'text',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({ofdId:t}),
        //data:temp,
        //processData: false,
        //contentType: false,
        success: function (data) {
            //将json字符串转换为json对象
            data = JSON.parse(data);
            //console.log(data);
            if (data.result == 'succeed') {
                //$('#btnDel'+t).parent().parent().hide();
                requestOFD(currentOFId);
            } else if (data.result == 'default') {
            }
        }
    });
}




//准备结账操作
function readyToSettleAccount(){
    //console.log('currentOFId:'+currentOFId);
    //var temp = getTargetOF(currentOFId);
    $('#shouldPay').val($('#ofTotalAmount').val())
    $('#actuallyPay').val($('#ofTotalAmount').val())
}
//结账操作
function settleAccount(){

    //alert("结账成功")
    //deleteOF(currentOFTId, currentOFId)
    $.ajax({
        url: url + "/Vita_Back/settleAccount",
        type: 'POST',
        cache: false,
        //dataType:'json',
        dataType: 'text',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({ofId:currentOFId, actuallyPay:$('#actuallyPay').val()}),
        //data:temp,
        //processData: false,
        //contentType: false,
        success: function (data) {
            //将json字符串转换为json对象
            data = JSON.parse(data);
            //console.log(data);
            if (data.result == 'succeed') {
                currentOFId=undefined;
                $("#modal-form").modal('hide');
                $("#OrderFormWindowBtnClose").trigger("click"); //模拟点击关闭按钮
                var temp = $('#myModal');
                if(temp.css('display') != 'none'){ //检测当前的是否打开了外卖组下的订单浏览窗口
                    openTakeoutWin(currentOFTId)
                }
                swal("结账成功", "实收金额:"+$('#actuallyPay').val(), 'success')
            } else if (data.result == 'default') {
            }
        }
    });

    //$('#modal-form').hide();

    //$('#myModalBtnClose').trigger("click"); //模拟点击关闭按钮----这个是关闭外卖订单集浏览窗口


}


//关闭订单详情窗口后，刷新页面
function refreshWin(){
    refreshOFT();
    var temp = $('#myModal');
    if(temp.css('display') != 'none'){ //检测当前的是否打开了外卖组下的订单浏览窗口
        //console.log("flag:"+currentOFTId)
        openTakeoutWin(currentOFTId)
    }
}




















//***************************************************************************************************************************************************end version X







































//订单组数据统计
function statisticOFT(t){
    var tempOFT = getTargetOFT(t);
    var i=0, tempOF, list=tempOFT.ofList;
    var k, j, m, x;
    var result={
        peopleNum:0,
        proNum:0,
        totalAmount:0,
        newMsgNum:0
    }
    for(i=0;i<list.length;i++){//遍历订单组下的订单
        tempOF=getTargetOF(list[i]);
        result.peopleNum+=parseInt(tempOF.customerNum);
        result.proNum+=parseInt(tempOF.ofProQuantity);
        result.totalAmount+=parseFloat(tempOF.ofTotalAmount);
        //result.customerNum
        k=tempOF.data;
        for(j=0, x=0;j<k.length;j++){//遍历订单详情下是否有未读消息
            m=getTargetOFD(k[j]);
            if(m.hasRead=='no') x++;
        }
        tempOF.newMsgNum=x;
        result.newMsgNum+=parseInt(tempOF.newMsgNum);
    }
    return result;
}
//刷新订单类型组的状态
function refreshRoomListF(){
    var i=0, k, m;
    var temp;
    for(i=0;i<OrderFormTypeList.length;i++){
        temp = $('#RoomMsgTip'+OrderFormTypeList[i].id);
        k=statisticOFT(OrderFormTypeList[i].id);
        $('#RoomPeopleNum'+OrderFormTypeList[i].id).text(k.peopleNum)
        $('#RoomProNum'+OrderFormTypeList[i].id).text(k.proNum)
        $('#RoomTotalAmount'+OrderFormTypeList[i].id).text(k.totalAmount)

        if(undefined!=k && k.newMsgNum>0){//有新消息
            temp.text(k.newMsgNum);
            temp.show();
        }else{//无新消息
            temp.text(0);
            temp.hide();
        }
        temp = $('#RoomState'+OrderFormTypeList[i].id)
        if(OrderFormTypeList[i].ofList.length>0){//该订单组有订单
            var cls = "label label-warning pull-right";
            temp.attr("class", "label label-warning pull-right");
            temp.text("已用")
        }else{//该订单组下无订单
            temp.attr("class", "label label-primary pull-right");
            temp.text("空闲")
        }

    }

    temp = $('#myModal');
    if(temp.css('display') != 'none'){ //检测当前的是否打开了外卖组下的订单浏览窗口
        openTakeoutWin(currentOFTId)
    }
}



//获取指定编号的订单组对象
function getTargetOFT(t){
    var i=0;
    for(i=0;i<OrderFormTypeList.length;i++){
        if(OrderFormTypeList[i].id==t) return OrderFormTypeList[i];
    }
    return undefined;
}
//获取指定编号的订单对象
function getTargetOF(t){
    var i=0;
    for(i=0;i<OrderFormList.length;i++){
        if(OrderFormList[i].id==t) return OrderFormList[i];
    }
    return undefined;
}
//获取指定编号的订单详情对象
function getTargetOFD(t){
    var i=0;
    for(i=0;i<OrderFormDataList.length;i++){
        if(OrderFormDataList[i].id==t) return OrderFormDataList[i];
    }
    return undefined;
}







//删除指定订单的缓存数据
function deleteOF(m, t){
    var i, temp, j, k, n
    temp = getTargetOFT(m);
    temp = temp.ofList;
    for(i=0;i<temp.length;i++){
        if(temp[i]==t){
            temp.splice(i, 1);
            break;
        }
    }
    for(i=0;i<OrderFormList.length;i++){
        temp = OrderFormList[i]
        if(temp.id==t){
            for(j=0;j<temp.data.length;j++){
                k=temp.data[j]
                for(n=0;n<OrderFormDataList.length;n++)
                    if(OrderFormDataList[n].id==k){
                        OrderFormDataList.splice(n,1);
                        break;
                    }
            }
            OrderFormList.splice(i, 1);
            break;
        }
    }
}





