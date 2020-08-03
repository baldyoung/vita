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
        socketUrl = "ws://" + SocketModule.getServerAddress() + socketUrl;
        console.log(socketUrl);
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
        // socket.send(msg); // 发送消息到服务端
        return socket;
    },
    printError: function (errorMessage) {
        SocketModule.onError(errorMessage);
    },
    onError: function (errorMessage) {
        console.log(errorMessage);
    },
    getServerAddress : function() {
       	var curWwwPath = window.document.location.href;
      	var pathName = window.document.location.pathname;
       	var pos = curWwwPath.indexOf(pathName);
       	var startIndex = curWwwPath.indexOf("//");
       	var localhostPath = curWwwPath.substring(startIndex+2, pos);
    	console.log(localhostPath);
    	return localhostPath
    }
}
