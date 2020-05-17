
$(function() {
    AudioModule.init();
    NewsModule.init();
    swal('欢迎使用vita', '', 'success');
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
    init : function() {
        NewsModule.startRequestLoop();
    },
    startRequestLoop : function() {
        setInterval("NewsModule.requestAndLoadCurrentNews()", 5000);
    },
    loadData : function(data) {
        var temp = window.document.getElementById("J_iframe");
        var target = $(window.parent.document).find("#J_iframe").attr("src");
        if (target == '../Working/workingL.html') {
            temp.contentWindow.NewsModule.loadData(data);
        }
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
                    swal("操作失败", data.desc, "error");
                }
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
        if(true == isFullScreen) {
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
    
    readyToUpdatePWD : function () {
        
    },
    readyToLogout : function () {
        
    }

}