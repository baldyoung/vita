
$(function() {
   OrderModule.init();
});

var OrderModule = {
    init : function () {
        $('#invalidItemTip').hide();
        var data = OrderModule.requestData();
        OrderModule.loadData(data);
    },
    requestData : function () {
        var targetData = undefined;
        $.ajax({
            url: GlobalConfig.serverAddress + "/order/lastOrder",
            type: 'GET',
            cache: false,
            dataType: 'json',
            async: false, //设置同步
            contentType: "application/json; charset=utf-8",
            data: null,
            success: function(data) {
                if (data.code == 0) {
                    targetData = data.data;
                } else {
                    layer.open({
                        content: '获取最新订单失败：'+data.desc,
                        skin: 'msg',
                        time: 3 //3秒后自动关闭
                    });
                }
            },
            error: function() {
                layer.open({
                    content: '连接服务器失败，请检查网络是否通畅！',
                    skin: 'msg',
                    time: 3 //3秒后自动关闭
                });
            }
        });
        return targetData;
    },
    loadData : function (data) {
        data.diningTypeName = toDiningTypeName(data.orderTypeFlag);
        data.diningTimeName = data.orderPresetTime + "左右";
        $('#orderNumberText').text("order"+data.orderId);
        $('#diningRoomNameText').text(data.diningRoomName);
        $('#diningTypeText').text(data.diningTypeName);
        $('#diningTimeText').text(data.diningTimeName);
        var target = $('#orderItemDisplayArea');
        target.html('');
        var list = data.itemList;
        var hasInvalidItem = false;
        for (var i=0; i<list.length; i++) {
            if (list[i].orderProductItemStatusFlag == 1) {
                hasInvalidItem = true;
            }
            target.append(OrderModule.createUnitHtml(list[i]));
        }
        if (hasInvalidItem) {
            $('#invalidItemTip').show();
        }

    },
    createUnitHtml : function (item) {
        var color = item.orderProductItemStatusFlag == 1 ? '#EF4F4F' : '#3CC51E';
        var html = '<div class="ddgz-a2-b1 first">' +
            '                <span class="ddgz-a2-yuan" style=\'background:'+color+'; border-radius: 5px;\'></span>' +
            '                <span class="ddgz-a2-t1">'+item.orderProductName +  ' * ' + item.orderProductQuantity+'</span>' +
            '            </div>';
        return html;
    }
}