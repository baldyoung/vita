
$(function() {
    AudioModule.init();
    NewsModule.init();
    swal('欢迎使用vita', '', '');
});

/**
 * 音频模块
 * @type {{currentStatus: boolean}}
 */
var AudioModule = {
    ACI : null,
    currentStatus : true,
    intervalCell : undefined,
    myAudioUnit : undefined,
    init : function() {
        AudioModule.ACI = document.getElementById('AudioActionIcon');
        //声音模块初始化
        AudioModule.myAudioUnit = createAudioModel(false);
        //设置所要播放的音乐
        AudioModule.myAudioUnit.selectAudio("audio/1.mp3");
    },
    play : function() {
        if (AudioModule.currentStatus == true && undefined == AudioModule.intervalCell) {
            AudioModule.startAudioLoop();
        }
    },
    changeStatus : function() {
        AudioModule.currentStatus = !AudioModule.currentStatus;
        AudioModule.ACI.className = (AudioModule.currentStatus ? "glyphicon glyphicon-volume-up" : "glyphicon glyphicon-volume-off");
        if (!AudioModule.currentStatus) {
            AudioModule.close();
        } else {
            AudioModule.start();
        }
    },
    start : function() {
        // 状态开关
        AudioModule.currentStatus = true;
    },
    close : function() {
        // 状态开关
        AudioModule.currentStatus = false;
        AudioModule.closeAudioLoop();
    },
    startAudioLoop : function() {
        AudioModule.myAudioUnit.playAudio();
        if (undefined == AudioModule.intervalCell) {
            AudioModule.intervalCell = setInterval("AudioModule.startAudioLoop()", 5000);
        }
    },
    closeAudioLoop : function () {
        if (undefined != AudioModule.intervalCell) {
            clearInterval(AudioModule.intervalCell);
            AudioModule.intervalCell = undefined;
        }
        AudioModule.myAudioUnit.pauseAudio(true);
    }
}

/**
 * 消息模块
 * @type {{init: NewsModule.init, checkCurrentParam: NewsModule.checkCurrentParam, ACI: HTMLElement, mainIframe: HTMLElement}}
 */
var NewsModule = {
    subIframe : null,
    socket : undefined,
    newsData : [],
    init : function() {
        // 获取websocket连接的密钥
        var key = NewsModule.requestSocketLinkKey();
        if (SocketModule.init() && undefined != key) {
            var tSocket = SocketModule.createSocket("/mSystemMessage/"+key);
            if (undefined != tSocket) {
                tSocket.onmessage = function(data) {
                    data = data.data;
                    data = JSON.parse(data);
                    var list = data;
                    console.log(list);
                    if (list.length > 0) {
                        AudioModule.play();
                    } else {
                        AudioModule.closeAudioLoop();
                    }
                    NewsModule.newsData = list;
                    NewsModule.loadData(list);
                }
                tSocket.onclose = function() {

                };
                setInterval("NewsModule.loadCurrentNewsData()", 1000);
                return;
            }
        }
        // websocket不可用，改用ajax轮询
        NewsModule.startRequestLoop();
    },
    startRequestLoop : function() {
        setInterval("NewsModule.requestAndLoadCurrentNews()", 5000);
    },
    loadCurrentNewsData : function() {
        // 折中的方法，因为websocket的onmessage中无法调用iframe中的函数！
        // 通过一个中间变量存储最新的消息数据，然后客户端启用一个函数轮询，时刻监视中间变量的消息数据！
        //
        NewsModule.loadData(NewsModule.newsData);
    },
    loadData : function(data) {
        var temp = window.document.getElementById("J_iframe");
        var target = $(window.parent.document).find("#J_iframe").attr("src");
        if (target == '../Working/workingL.html' ) {
            temp.contentWindow.NewsModule.loadData(data);
        }
    },
    requestSocketLinkKey : function() {
        var targetData = undefined;
        $.ajax({
            url: GlobalConfig.serverAddress + "/mSystem/mSystemMessageKey",
            type: 'GET',
            cache: false,
            dataType:'json',
            async : false,
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: null,
            success: function (data) {
                if (data.code == 0) {
                    targetData = data.data;
                } else {
                    // swal("操作失败", data.desc, "error");
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                // location.reload();
                //console.log(XMLHttpRequest);
            }
        });
        return targetData;
    },
    requestAndLoadCurrentNews : function () {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mSystem/currentNews",
            type: 'GET',
            cache: false,
            dataType:'json',
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: null,
            success: function (data) {
                if (data.code == 0) {
                    var list = data.data;
                    if (list.length > 0) {
                        AudioModule.play();
                    } else {
                        AudioModule.closeAudioLoop();
                    }
                    NewsModule.loadData(list);
                } else {
                    swal("获取系统消息失败", data.desc, "error");
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                location.reload();
                //console.log(XMLHttpRequest);
            }
        });
    }
}

/**
 * 全屏模块
 * @type {{init: FullScreenModule.init, currentStatus: boolean}}
 */
var FullScreenModule = {
    currentStatus : false,
    init : function () {

    },
    changeFullScreen : function () {
        //改变全屏状态
        FullScreenModule.currentStatus = !FullScreenModule.currentStatus;
        if(true == FullScreenModule.currentStatus) {
            openFullScreen();
            return;
        }
        closeFullScreen();
    }
}
/**
 * 用户模块
 * @type {{readyToLogout: UserModule.readyToLogout, readyToUpdatePWD: UserModule.readyToUpdatePWD}}
 */
var UserModule = {
    readyToUpdateUserInfo : function () {
        $('#userNameText').val('');
        $('#userAccountText').val('');
        $('#userNewPasswordText').val('');
        $('#userOldPasswordText').val('');
        $.ajax({
            url: GlobalConfig.serverAddress + "/mUser/currentUserInfo",
            type: 'GET',
            cache: false,
            dataType: 'json',
            //async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: null,
            success: function(data) {
                if (data.code != 0) {
                    swal('获取登录信息失败', data.desc, 'error');
                } else {
                    var temp = data.data;
                    temp.userTypeName = (temp.merchantUserGrade >= 3 ? '管理员' : '普通' );
                    $('#userNameText').val(temp.merchantUserName);
                    $('#userAccountText').val(temp.merchantUserAccount);
                    $('#userNewPasswordText').val('');
                    $('#userOldPasswordText').val('');
                    $('#userGradeText').val(temp.userTypeName);
                }
            },
            error: function() {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    },
    readyToLogout : function () {
        swal({
                title: "您确定要退出系统吗?",
                text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function(isConfirm) {
                if (isConfirm) {
                    UserModule.logout();
                } else {
                    swal("已取消", "已取消登出！", "error");
                }
            });
    },
    logout : function() {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mUser/logout",
            type: 'POST',
            cache: false,
            dataType: 'json',
            //async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: null,
            success: function(data) {
                if (data.code != 0) {
                    swal('操作异常', data.desc, 'error');
                } else {
                    console.log("登出成功！");
                    location.reload();
                }
            },
            error: function() {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    },
    updateAction : function() {
        var data = {};
        var temp = $('#userNameText').val();
        if (!GlobalMethod.isEmpty(temp)) {
            data.userName = temp;
        }
        temp = $('#userAccountText').val();
        if (!GlobalMethod.isEmpty(temp)) {
            data.userAccount = temp;
        }
        temp = $('#userNewPasswordText').val();
        if (!GlobalMethod.isEmpty(temp)) {
            data.userNewPassword = temp;
        }
        temp = $('#userOldPasswordText').val();
        if (!GlobalMethod.isEmpty(temp)) {
            data.userOldPassword = temp;
        } else {
            swal('请输入旧密码', '', 'error');
            return;
        }
        UserModule.sendData(data);
    },
    sendData : function(data) {
        $.ajax({
            url: GlobalConfig.serverAddress + "/mUser/updateCurrentUser",
            type: 'POST',
            cache: false,
            dataType: 'json',
            //async: false, //设置同步
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data: data,
            success: function(data) {
                if (data.code != 0) {
                    swal('修改失败', data.desc, 'error');
                } else {
                    swal('修改成功', '', 'success');
                    $('#closeUpdateCurrentInfoWindowBtn').trigger('click');
                }
            },
            error: function() {
                swal('服务器连接失败', '请检查网络是否通畅', 'warning');
            }
        });
    }

}

/**
 * 功能操作模块
 * @type {{optionAreaUp: OptionModule.optionAreaUp, optionAreaDown: OptionModule.optionAreaDown, changeOptionArea: OptionModule.changeOptionArea, optionAreaStation: string}}
 */
var OptionModule = {
    optionAreaStation : 'down',
    changeOptionArea : function() {
        if ('down' == OptionModule.optionAreaStation) {
            OptionModule.optionAreaUp();
        } else {
            OptionModule.optionAreaDown();
        }
    },
    optionAreaUp : function () {
        $('#optionTitleArea').slideUp();
        $('#optionTitleTip').show();
        OptionModule.optionAreaStation = 'up';
    },
    optionAreaDown : function () {
        $('#optionTitleTip').hide();
        $('#optionTitleArea').slideDown();
        OptionModule.optionAreaStation = 'down';
    }
}