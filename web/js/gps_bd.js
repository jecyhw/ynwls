/**
 * Created by SNNU on 2015/5/7.
 */
//gps坐标数组,格式为['114.21892734521,29.575429778924','114.21892734521,29.575429778924'],callback全部转换完之后的回调函数，参数为百度坐标数组
function gps2bd(gpsPoints, callback) {
    if (!gpsPoints || gpsPoints.length == 0) {//不需要转换
        callback();
        return;
    }
    qq.maps.convertor.translate(gpsPoints, 1, function(res){
        callback(res);
    });
}

function createMap(id) {
    var mp = new qq.maps.Map(document.getElementById(id), {
        center: new qq.maps.LatLng(39.916527,116.397128),      // 地图的中心地理坐标。
        zoom:13
    });
    qq.maps.event.addListener(mp,'click',function(event) {
        console.log(event);
    });
    return mp;
}

function locateByIp(mp, callback) {
    //获取城市列表接口设置中心点
    var citylocation = new qq.maps.CityService({
        complete : function(result){
            mp.setCenter(result.detail.latLng);
            if (callback) {
                callback();
            }
        }
    });
    //根据用户IP查询城市信息。
    citylocation.searchLocalCity();
}

function lineStyle(options) {
    return $.extend({
        strokeColor: "#8A2BE2",
        fillColor: "",
        strokeWeight: 3
    }, options);
}

$.ajaxSetup({
    cache: false,
    complete: function (xhr, textStatus) {
        checkTimeout(xhr);
    }
});

function checkTimeout(xhr) {
    var sessionstatus = xhr.getResponseHeader("sessionstatus"); // 通过XMLHttpRequest取得响应头，sessionstatus，
    if (sessionstatus == "timeout") {// 如果超时就处理 ，指定要跳转的页面
        var i = 5;
        var $timeOutMsg = $('<div class="text-danger"><strong>会话超时，请重新登陆!</strong><hr/><p><span class="text-primary">'
        + i + '</span>秒后自动跳转自登陆页面</p><p>也可点击<a class="btn  btn-danger btn-sm">此处</a>跳转</p></div>');
        $.fancybox($timeOutMsg, {
            modal: true,
            closeBtn: false,
            afterShow: function() {
                var $spanSec = $timeOutMsg.find('span');
                var intervalId = setInterval(countDown, 1000);

                function countDown() {
                    if (i > 0) {
                        $spanSec.html(--i);
                    } else {
                        clearInterval(intervalId);
                        window.location.href = "../index.html";
                    }
                }

                $timeOutMsg.find('a').click(function () {
                    clearInterval(intervalId);
                    window.location.href = "../index.html";
                });
            }
        });
    }
}
