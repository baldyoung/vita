
var MyAudioModelCell ;//= createAudioModel(false);
//MyAudioModelCell.selectAudio("src/vedio/1.mp3");
//test.startLoopPlay(5, 5000);
//MyAudioModelCell.playAudio();
var IntervalCell ;//= loopExcute("MyAudioModelCell.playAudio()", 3000);

//生成一个Audio操作对象
function createAudioModel(isShow){
	
	var div = document.createElement('div');
	var str = "<audio id='myAudioInAudioModel' src='' style=' display:"+(isShow == true ? "" : "none")+" ; '   controls='controls' >";
	str += " Your browser does not support the audio element. ";
	str += " </audio>"
	div.innerHTML = str;
	document.body.appendChild(div)
	var myAudio = document.getElementById('myAudioInAudioModel');

var AudioModel = {
	Timeouter : undefined, //循环播放计时器
	Times : 0, //循环播放次数
	TimeLength : undefined,
	AudioModelDiv : div, //Audio外层的Div
	MyAudio : myAudio, //生成的Audio
	CurrentAudioName : undefined, //当前播放的音乐
	CurrentAudioPath : undefined, //当前播放音乐的路径
	
	selectAudio : function(audioName){ //选择音乐
		//var myAudio = document.getElementById("myAudioInAudioModel");
		if(!showMsgIfNull(this.MyAudio, "MyAudio is null !")){
			this.MyAudio.src = audioName;
			this.CurrentAudioName = audioName;
		}
	},
	playAudio : function(){ //播放选择的音乐
		if(!showMsgIfNull(this.MyAudio, "MyAudio is null !")) 
			this.MyAudio.play();
	},
	pauseAudio : function(isPause){ //音乐播放暂停
		if(!showMsgIfNull(this.MyAudio, "MyAudio is null !")) 
			this.MyAudio.pause();
			this.MyAudio.load();
	},
	//***************** 
	loopPlay : function(t){ //循环播放的处理逻辑
		
		if(t.Times==-1 || t.Times>0){
			if(t.Times!=-1) t.Times--;
			t.Timeouter = setTimeout(t.loopPlay, t.TimeLength);
			t.playAudio();
		}
	},
	startLoopPlay : function(times, timeLength){ //循环播放的次数，-1代表无限循环, timeLength代表间隔时长（单位毫秒）

		if(!showMsgIfNull(this.MyAudio, "MyAudio is null !")){
			this.Timeouter = setInterval(this.playAudio, timeLength);
			//this.Times = times;
			//this.TimeLength = timeLength;
			//this.loopPlay(this);
			//var temp = this.loopPlay;
			//this.Timeouter = setTimeout(this.loopPlay(), timeLength); //一次性定时器
			//setInterval(this.playAudio(), timeLength); 只会执行一次，因为setInterval调用的第一个参数是this.playAudio返回的值。
			//Timeouter = 1; setInterval(temp, timeLength);
			//console.log("startLoopPlay->this.Times:"+this.Times);
			//console.log("startLoopPlay->this.Timeouter:"+this.Timeouter);
		}		
	},
	closeLoopPlay : function(){
		if(!showMsgIfNull(this.Timeouter, "Timeouter is null!")){
			this.pauseAudio(true);
			this.Times = 0;
			//this.closeTimeout(Timeouter);
			closeInterval(Timeouter);
			this.Timeouter = undefined;
			
		}
			
	}
		
}

	return AudioModel;
}


//如果target不存在则打印指定的错误信息并返回true，否则返回false
function showMsgIfNull(target, msg){
	if(undefined == target || null == target){
		return true;
	} 
	return false;
}
//开启周期调用
function loopExcute(t, tl){
	var temp = setInterval(t, tl);
	return temp;
}
//关闭周期调用
function loopStop(t, e){
	clearInterval(t);
	e.pauseAudio(true);
}
//开启周期音乐播放
function closeAudioLoop(){
	if(IntervalCell!=undefined){
		loopStop(IntervalCell, MyAudioModelCell);
		IntervalCell=undefined;
	}

}
//关闭周期音乐播放
function startAudioLoop(){
	if(IntervalCell!=undefined) {
		closeAudioLoop();
	}
	MyAudioModelCell.playAudio();
	IntervalCell = loopExcute("MyAudioModelCell.playAudio()", 5000);
}




