
var url = "/Vita_war_exploded"
var currentProTypeId=undefined;
$(document).ready(function(){

   // testFun();
    requestProTypeList();
});

//商品类型排序
function sortProductTypeByGrade(t){
	var i, temp;
	var result = [];
	while(t.length>0){
		for(temp=0, i=1;i<t.length;i++){			
			if(t[temp].grade < t[i].grade) temp=i;
			else if(t[temp].grade == t[i].grade && t[temp].id > t[i].id) temp = i;
		}
		if(t[temp].id >= 100)
			result[result.length] = t[temp];
		t.splice(temp, 1);
	}
	return result;
}

//加载商品类型数据表格
function  loadProTypeTable(t){
    //console.log('loadProTypeTable...,')
    var dataTable = $('#ProTypeTable');
    if ($.fn.dataTable.isDataTable(dataTable)) {
        dataTable.DataTable().destroy();
    }
    dataTable.DataTable({
        'searching':true, //去掉搜索框
        'bLengthChange': true, //去掉每页显示多少条数据方法
        "serverSide": false, //关闭分页操作，默认就是关闭
        "autoWidth" : false, //
        "bSort": true, //打开排序功能
        "order": [[ 1, "desc"], [0, 'asc' ]], //默认排序的指标
		"pageLength": 25, //默认每页显示的数据条数
        'data':t, //表格的数据源
        'columns':[{
            data:'productTypeId'
        },{
            data:'productTypeName'
        },{
            data:'productTypeGrade'
        },{
            data:'isShow'
        }
        ],
        "columnDefs": [{
            "render" : function(data, type, row) {
                var a = "";
                //a += "<button type='button' class='btn btn-primary' onclick='showEditStudent(\""+0+"\")' data-toggle='modal' data-target='#myModal5' title='编辑用户' data-toggle='dropdown' style='margin-right:15px; margin-bottom: -1px;'><i class='fa fa-pencil-square-o'></i>&nbsp;编辑</button>"
                //a += "<button type='button' class='btn btn-primary' style='margin-top:3px;' onclick='loadOrderForm(\""+row.id+"\")'  data-toggle='dropdown' data-target=\"#OrderFormWindow\" style='margin-right:15px; margin-bottom: -1px;'><i class='fa fa-building-o'></i>&nbsp;查看</button>"
                //a += "<button type='button' class='btn btn-primary' onclick='courseList(\""+row.id+"\")' data-toggle='modal' data-target='#courseListDialog' title='课程列表' data-toggle='dropdown' style='margin-right:15px; margin-bottom: -1px;'><i class='fa fa-list'></i>&nbsp;课程列表</button>"
                a+="<button class=\"btn btn-primary \" style='margin-top:3px;' type=\"button\" data-toggle=\"modal\" data-target=\"#OrderFormWindow\" onclick=\"deleteProType('"+row.id+"')\"><i class=\"fa fa-building-o\"></i>删除"
                a+="</button>"
                a+="<button class=\"btn btn-primary \" style='margin-top:3px; margin-left:5px; ' type=\"button\" data-toggle=\"modal\" data-target=\"#myModal\" onclick=\"openProTypeInfWin('"+row.id+"','"+row.grade+"','"+row.name+"',  '"+row.remark+"' )\"><i class=\"fa fa-building-o\"></i>修改"
                a+="</button>"
                return a;
            },
            "targets" :4
        }]
        //$('#OrderFormData_wrapper').css('padding-bottom', '0px');

    })


}
//请求数据，并加载到页面
function requestProTypeList(){

    $.ajax({
        url: GlobalConfig.serverAddress + "/mProductType/all",
        type: 'GET',
        cache: false,
        dataType:'json',
        // dataType: 'text',
        contentType: "application/json; charset=utf-8",
        data: {},
        //data:temp,
        //processData: false,
        //contentType: false,
        success: function (data) {
            if (data.code == 0) {
                data = data.data;
                loadProTypeTable(data);
            } else {
                swal("获取数据失败", data.desc, "error");
            }
        }
    });
}

//删除指定商品类型类型
function deleteProType(t){
    $.ajax({
        url: GlobalConfig.serverAddress + "/mProductType/delete",
        type: 'get',
        cache: false,
        dataType:'json',
        //dataType: 'text',
        contentType: "application/json; charset=utf-8",
        data:{productTypeId:t},
        //processData: false,
        //contentType: false,
        success: function (data) {
            if (data.code == 0) {
                requestProTypeList();
            } else {
                swal("删除失败", data.desc, "error");
            }
        }
    });
}

//加载商品类型的详细详细
function loadProTypeInf(t){
    currentProTypeId=t.id;
    $('#idText').val(t.id);
    $('#gradeText').val(t.grade);
    $('#NAMEText').val(t.name);
    $('#remarkText').val(t.remark);
}

//重置商品类型详细窗口
function resetProTypeInf(){
    $('#idText').val('');
    $('#gradeText').val('');
    $('#NAMEText').val('');
    $('#remarkText').val('');
}
//加载指定商品类型的信息
function openProTypeInfWin(tid, tgrade,tNAME, tremark){
    var temp = {
        id:tid,
        grade:tgrade,
        name:tNAME,
        remark:tremark

    }
    //currentProTypeId=tid;
    $('#myModal_title').text('商品类型信息-修改')
    loadProTypeInf(temp);
}



//保存商品类型信息
function saveProTypeInf(){
    var temp = {
        id: $('#idText').val(),
        grade: $('#gradeText').val(),
        name: $('#NAMEText').val(),
        remark: $('#remarkText').val()
    }

    if(currentProTypeId != undefined)
        temp.id=currentProTypeId;
    var tip = check(temp);
    if(true!=tip){//格式不正确
        showTip(tip);
    }else{
        postProTypeInf(temp)
    }
}

function postProTypeInf(t){
    $.ajax({
        url: url + "/updateProductType.action",
        type: 'get',
        cache: false,
        dataType:'json',
        //dataType: 'text',
        contentType: "application/json; charset=utf-8",
        //data: JSON.stringify(t),
        data:t,
        //processData: false,
        //contentType: false,
        success: function (data) {
            //将json字符串转换为json对象
            //data = JSON.parse(data);
            //console.log(data);
            if (data.result == 'succeed') {//获取成功
                requestProTypeList();
                swal("操作成功", "")
				$('#btnSaveInfWin').trigger('click');
            } else if (data.result == 'default') { //获取失败
                swal("操作失败", data.inf);
            }
        }
    });
}

//准备添加新商品类型
function readForAddProType(){
    console.log("read");
    currentProTypeId=undefined;
    $('#myModal_title1').text('商品类型信息-新增');
	
	
    $('#addidText').val('');
    $('#addgradeText').val('50');
    $('#addNAMEText').val('');
    $('#addremarkText').val('');
}
///添加商品类型信息
function addProTypeInf() {

    $.ajax({
        url: url + "/addProductType.action",
        type: 'get',
        cache: false,
        dataType: 'json',
        //dataType: 'text',
        contentType: "application/json; charset=utf-8",
        // data: JSON.stringify(t),
        data: {
            grade: $('#addgradeText').val(),
            name: $('#addNAMEText').val(),
            remark: $('#addremarkText').val()
        },
        //processData: false,
        //contentType: false,
        success: function (data) {
            //将json字符串转换为json对象
            //data = JSON.parse(data);
            //console.log(data);
            if (data.result == 'succeed') {//获取成功
                swal("操作成功", "")
                requestProTypeList();
                resetProTypeInf();
				$('#btnCloseAddWin').trigger('click');
            } else if (data.result == 'default') { //获取失败
                swal("操作失败", data.inf);
            }
        }
    });
}
////检查指定的json对象的格式是否正确
function check(t){
 if(undefined==t || undefined==t.grade || undefined==t.name || undefined==t.remark) return undefined;
 if(t.grade.length<=0||t.grade.length>=255) return '商品类型等级需要大于0小于255个字符';
 if(t.name.length>=30 ) return '用户名称需要小于30个字符';
 if(t.remark.length>=200) return '备注需要小于20个字符';
 if(t.name=='') return '信息不能留空';

 return true;
}
//提示信息
function showTip(t){
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "progressBar": true,
        "positionClass": "toast-top-center",
        "onclick": null,
        "showDuration": "400",
        "hideDuration": "1000",
        "timeOut": "7000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
    toastr.warning(''+t);

}


function testFun(){


    var temp = [{
        id:'10010',
        grade:'1',
        NAME:'川菜',
        remark:'123456789',
       
    },{
        id:'10011',
        grade:'2',
        NAME:'粤菜',
        remark:'123456789',
    },{
        id:'10012',
        grade:'3',
        NAME:'云南菜',
        remark:'123456789',
    },{
       id:'10013',
        grade:'4',
        NAME:'主食',
        remark:'123456789',
    },{
        id:'10014',
        grade:'5',
        NAME:'饮品',
        remark:'123456789',
    }]
    loadProTypeTable(temp);
}