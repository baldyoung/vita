var SocketModule = {
    init: function () {
        if (typeof (WebSocket) == "undefined") {
            SocketModule.printError("您的浏览器不支持WebSocket");
            // 初始化websocket服务失败
            return false;
        }
        return true;
    },
    createSocket : function(socketUrl) {
        if (!SocketModule.init()) {
            return null;
        }
        // 实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接
        // var socketUrl = "ws://localhost/mSystemMessage/123";
        var socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function () {
            console.log("websocket已打开");
        };
        // 获得消息事件
        socket.onmessage = function (msg) {
            console.log(serverMsg);
        };
        // 关闭事件
        socket.onclose = function () {
            console.log("websocket已关闭");
        };
        // 发生了错误事件
        socket.onerror = function () {
            console.log("websocket发生了错误");
        }
        return socket;
    },
    printError: function (errorMessage) {
        SocketModule.onError(errorMessage);
    },
    onError: function (errorMessage) {
        console.log(errorMessage);
    }
}


var socket;

function openSocket() {
    if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
    } else {
        console.log("您的浏览器支持WebSocket");
        //实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接
        var userId = document.getElementById('userId').value;
        // var socketUrl="ws://127.0.0.1:22599/webSocket/"+userId;
        var socketUrl = "ws://localhost/mSystemMessage/123";
        console.log(socketUrl);
        if (socket != null) {
            socket.close();
            socket = null;
        }
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function () {
            console.log("websocket已打开");
            //socket.send("这是来自客户端的消息" + location.href + new Date());
        };
        //获得消息事件
        socket.onmessage = function (msg) {
            var serverMsg = "收到服务端信息：" + msg.data;
            console.log(serverMsg);
            //发现消息进入    开始处理前端触发逻辑
        };
        //关闭事件
        socket.onclose = function () {
            console.log("websocket已关闭");
        };
        //发生了错误事件
        socket.onerror = function () {
            console.log("websocket发生了错误");
        }
    }
}

function sendMessage() {
    if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
    } else {
        // console.log("您的浏览器支持WebSocket");
        var toUserId = document.getElementById('toUserId').value;
        var contentText = document.getElementById('contentText').value;
        var msg = '{"toUserId":"' + toUserId + '","contentText":"' + contentText + '"}';
        console.log(msg);
        socket.send(msg);
    }
}