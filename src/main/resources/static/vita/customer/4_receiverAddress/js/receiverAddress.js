
var isGeolocation = true; 

$(function() {
	
	
	//defaultFun();
	isGeolocation = false;
	//GlobalMethod.requestLocation(successFun, defaultFun);
	defaultFun();

	return;
	var map = new AMap.Map('container', {
		resizeEnable: true, // 没用
		zooms: [4, 18], //设置地图级别范围
		zoom: 16,
		center: [115.327295, 26.341718]
	});
	var marker = new AMap.Marker({
		position: new AMap.LngLat(115.327295, 26.341718), // 经纬度对象，也可以是经纬度构成的一维数组[116.39, 39.9]
		title: '北京'
	});
	map.add(marker);

	AMapUI.loadUI(['misc/PositionPicker'], function(PositionPicker) {
		var map = new AMap.Map('container', {
			zoom: 16
		})
		var positionPicker = new PositionPicker({
			mode: 'dragMap', //设定为拖拽地图模式，可选'dragMap'、'dragMarker'，默认为'dragMap'
			map: map //依赖地图对象
		});
		//TODO:事件绑定、结果处理等
	});

	AMap.plugin('AMap.Geolocation', function() {
		var geolocation = new AMap.Geolocation({
			//使用web浏览器进行精准定位，并返回定位结果；
			//注：下面的配置信息，可以去除谷歌定位默认的定位小圆圈；
			enableHighAccuracy: true, //是否使用高精度定位，默认:true
			timeout: 5, //超过10秒后停止定位，默认：无穷大
			maximumAge: 0, //定位结果缓存0毫秒，默认：0
			convert: true, //自动偏移坐标，偏移后的坐标为高德坐标，默认：true
			showButton: true, //显示定位按钮，默认：true
			buttonPosition: 'LB', //定位按钮停靠位置，默认：'LB'，左下角
			buttonOffset: new AMap.Pixel(10, 20), //定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
			showMarker: false, //定位成功后在定位到的位置显示点标记，默认：true
			showCircle: false, //定位成功后用圆圈表示定位精度范围，默认：true  （去掉圆形区域）
			panToLocation: true, //定位成功后将定位到的位置作为地图中心点，默认：true
			zoomToAccuracy: false //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
		});
		map.addControl(geolocation);
		geolocation.getCurrentPosition(function(status, result) {
				if (status == 'complete') {
					console.log('====result====', result); // result为精准定位成功的返回结果；
				} else {
					console.log('=====error result ====', result); //如果status不等于'complete'，则定位失败；返回的result中包含失败信息；
				}
			})
			//可代替上方getCurrentPosition
			//geolocation.getCurrentPosition();
			//AMap.event.addListener(geolocation, 'complete', onComplete);//返回定位信息
			//AMap.event.addListener(geolocation, 'error', onError);      //返回定位出错信息
	})

})

function successFun(lg, lt) {
	test2(lg, lt);
}
function defaultFun() {
	test2(115.363595,26.338068);
}

function test2(lg, lt) {
	AMapUI.loadUI(['misc/PositionPicker'], function(PositionPicker) {

            var map = new AMap.Map('container', {

                zoom: 16,
				//center: [115.327295, 26.341718],
				center : [lg, lt],
                scrollWheel: false

            });

            map.plugin(['AMap.Geolocation','AMap.ToolBar'], function () {

                var toolbar = new AMap.ToolBar();

                map.addControl(toolbar);

                var geolocation = new AMap.Geolocation({

                    enableHighAccuracy: true, // 是否使用高精度定位，默认:true

                    timeout: 5000,          // 超过5秒后停止定位，默认：无穷大

                    maximumAge: 0,            // 定位结果缓存0毫秒，默认：0

                    convert: true,            // 自动偏移坐标，偏移后的坐标为高德坐标，默认：true

                    showButton: true,        // 显示定位按钮，默认：true

                    buttonPosition: 'LB',    // 定位按钮停靠位置，默认：'LB'，左下角

                    buttonOffset: new AMap.Pixel(10, 20), // 定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)

                    showMarker: true,        // 定位成功后在定位到的位置显示点标记，默认：true

                    showCircle: true,        // 定位成功后用圆圈表示定位精度范围，默认：true

                    panToLocation: true,      // 定位成功后将定位到的位置作为地图中心点，默认：true

                    zoomToAccuracy:true      // 定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false

                });

                map.addControl(geolocation);

                geolocation.getCurrentPosition();

                AMap.event.addListener(geolocation, 'complete', onComplete); // 返回定位信息

                AMap.event.addListener(geolocation, 'error', onError);      // 返回定位出错信息

            });

            var positionPicker = new PositionPicker({

                mode: 'dragMap',

                map: map

            });

            function onComplete(obj){

                console.log("成功");

            }

            function onError(obj) {
            	//if (!isGeolocation)
					$('#addressText').text('定位失败，请拖拽标识到收货地点！');
              

            }

            positionPicker.on('success', function(positionResult) {

                console.log(positionResult);

                var sheng=positionResult.regeocode.addressComponent.province;

                var shi=positionResult.regeocode.addressComponent.city;

                var qx=positionResult.regeocode.addressComponent.district;

                var jd=positionResult.regeocode.addressComponent.township+

                    positionResult.regeocode.addressComponent.street+

                    positionResult.regeocode.addressComponent.streetNumber;
				var temp = sheng+shi+qx+jd;
				//if (!isGeolocation)
					$('#addressText').text(temp);

            });

            positionPicker.on('fail', function(positionResult) {
            	//if (!isGeolocation)
					$('#addressText').text('定位失败，请拖拽标识到收货地点！');
                console.log(positionResult);

            });

            positionPicker.start();

                    map.panBy(0, 1);

                    map.addControl(new AMap.ToolBar({

                        liteStyle: true

                    }))

        });

}


