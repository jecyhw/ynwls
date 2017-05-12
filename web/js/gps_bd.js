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

    //新建一个ImageMapType，实现ImageMapTypeOptions规范
    var earthlayer = new qq.maps.ImageMapType({
        name: 'tentxun',
        alt: 'tentxun',
        tileSize: new qq.maps.Size(256, 256),
        minZoom: 16,
        maxZoom: 19,
        opacity: 1,
        getTileUrl: function (tile, zoom) {
            var z = zoom,
                x = tile.x,
                y = tile.y;
            return 'http://159.226.15.190:8081/Maps/' + z + '/' + x + '/' + y + '.png';
        }
    });

    mp.overlayMapTypes.push(earthlayer);

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
