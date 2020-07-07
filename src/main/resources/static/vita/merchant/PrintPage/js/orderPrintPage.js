/**
 *
 */

$(function() {

    var orderId = GlobalMethod.getArgsFromLocationHref('oi');
    OrderPrintModule.loadTargetOrder(orderId);
});

var OrderPrintModule = {
    orderPrintDisplayAreaId : 'orderPrintWordView',
    loadTargetOrder : function(orderId) {
        var orderData = OrderPrintModule.requestOrderData(orderId);
        if (undefined == orderData) {
            return;
        }
        // 获取url中给定的就餐位名称
        var t = GlobalMethod.getArgsFromLocationHref('drn');
        orderData.diningRoomName = unescape(t);
        // 获取url中给定的订单编号（该编号对应的是每个就餐位下的订单序号！！！）
        orderData.orderNumber = GlobalMethod.getArgsFromLocationHref('on');
        orderData.diningTypeName = (orderData.orderTypeFlag == 0 ? '堂食' : '打包');
        // 生成预订单视图，并将该视图加载到指定区域
        var orderPrintView = OrderPrintModule.createOrderPrintView(orderData);
        $('#' + OrderPrintModule.orderPrintDisplayAreaId).text(orderPrintView);
        var target = document.getElementById(OrderPrintModule.orderPrintDisplayAreaId);
        target.style.height = 20 + target.scrollHeight + 'px';
        setTimeout("window.print()", 1000);
    },
    createOrderPrintView : function(orderData) {
        var word = '';
        orderData.orderNumberName = '订单' + orderData.orderNumber + '';
        var temp = OrderPrintModule.createOrderPrintWord(orderData);
        word += '^^^^^^^^^^^^^^^^^^^^^^\n';
        word += '       前台用票\n';
        word += temp;
        word += '\n^^^^^^^^^^^^^^^^^^^^^^\n';
        word += '       配菜用票\n';
        word += temp;
        word += '\n^^^^^^^^^^^^^^^^^^^^^^\n';
        word += '       厨师用票\n';
        word += temp;
        return word;
    },
    requestOrderData : function(orderId) {
        var targetData = undefined;
        $.ajax({
            url: GlobalConfig.serverAddress + "/mOrder/orderInfo",
            type: 'GET',
            cache: false,
            dataType: 'json',
            async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: {
                orderId : orderId
            },
            success: function(data) {
                if (data.code != 0) {
                    swal('获取订单数据失败', data.desc, 'error');
                } else {
                    targetData = data.data;
                }
            },
            error: function() {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
        return targetData;
    },
    createOrderPrintWord: function (order) {
        // desc : 拼接给定订单的打印视图（视图以字符串的形式展现）
        var temp = '----------------------\n';
        temp += '【' + order.diningTypeName + ' - ' + order.orderPresetTime + '】\n';
        temp += order.diningRoomName + ' (' + order.orderNumberName + ')\n';
        temp += '----------------------\n';
        var list = order.itemList;
        for (var i = 0, k = 1; i < list.length; i++, k++) {
            var cell = list[i];
            var str = k + '.' + cell.orderProductName;
            var len = OrderPrintModule.theLengthOfString(str) + OrderPrintModule.theLengthOfInteger(cell.orderProductQuantity);
            console.log("test->"+cell.orderProductName+"(总长："+len+")");
            if (len > 22) {
                len = 22 - (len % 22);
            } else {
                len = 22 - len;
            }
            console.log("test->"+cell.orderProductName+"(补位："+len+")");
            temp += str + OrderPrintModule.createBlank(len) + cell.orderProductQuantity + '\n';
            if (!GlobalMethod.isEmpty(cell.orderProductRemarks)) {
                temp += '注：' + cell.orderProductRemarks + '\n';
            }
            temp += '\n';
        }
        temp += '----------------------\n';
        temp += GlobalMethod.toDateString(order.orderCreateDateTime) + '\n';
        temp += '++++++++++++++++++++++\n';
        return temp;
    },
    theLengthOfString: function (str) {
        // desc : 获取给的字符串的占位长度（一个汉字两个占位，英文字母一个占位）
        var strlen = 0;
        for (var i = 0; i < str.length; i++) {
            if (str.charCodeAt(i) > 255) {
                //如果是汉字，则字符串长度加2
                strlen += 2;
            } else {
                strlen++;
            }
        }
        return strlen;
    },
    theLengthOfInteger: function (number) {
        // desc : 计算给的数值是几位数
        if (number == undefined) {
            return 0;
        }
        var k = 1;
        while (number > 9) {
            number /= 10;
            k++;
        }
        return k;
    },
    createBlank: function (number) {
        // desc : 构建指定长度的空格字符串
        var temp = '';
        while (number-- > 0) {
            temp += ' ';
        }
        return temp;
    },
}