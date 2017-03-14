/**
 * Created by acm on 12/4/14.
 */
(function () {
    var mapData = [];
    var jmap = function (id) {
        return new jmap.fn.init(id);
    };
    jmap.fn = jmap.prototype = {
        show: function () {
            return show(this.id);
        },

        hide: function () {
            return hide(this.id);
        },

        remove: function () {
            return remove(this.id);
        },

        setViewPort: function () {
            var overlay = mapData[this.id];
            if (overlay) {
                var bounds;
                var points = [];
                for (var i in overlay) {
                    if (overlay[i].getBounds) {
                        if (bounds) {
                            bounds.union(overlay[i].getBounds());
                        } else {
                            bounds = overlay[i].getBounds();
                        }
                    }
                }
                mp.fitBounds(bounds);
            }
        }
    };

    function show(id) {
        var overlay = mapData[id];
        if (overlay) {
            for (var i in overlay) {
                overlay[i].setMap(mp);
            }
            return true;
        }
        return false;
    };

    function hide(id) {
        var overlay = mapData[id];
        if (overlay) {
            for (var i in overlay) {
                overlay[i].setMap(null);
            }
            return true;
        }
        return false;
    };

    function remove(id) {
        var overlay = mapData[id];
        if (overlay) {
            for (var i in overlay) {
                overlay[i].setMap(null);
            }
            delete mapData[id];
            return true;
        }
        return false;
    };

    var init = jmap.fn.init = function (id) {
        if (id) {
            this.id = id;
        }
        return this;
    };

    /**
     *
     * @param id 要显示的id
     * @param data 数据
     * @param callback 处理完之后的回调函数
     */
    jmap.loadData = function (id, data, callback) {
        new Draw(id, data, callback).work();
    };

    jmap.ids = function () {
        var ids = [];
        for (var index in mapData) {
            try {
                if (mapData[index][0].isVisible() == true) {
                    ids.push(index);
                }
            } catch (e) {
            }
        }
        return ids;
    }

    jmap.clear = function () {
        for (var i in mapData) {
            remove(i);
        }
        mapData = [];
    }

    function Draw(id, data, callback) {
        var size = new qq.maps.Size(42, 34),//折线的起点和终点的图标大小
            overlay = [];

        this.work = function () {
            polyLine();
        };

        function polyLine() {//由于路线存在多条，坐标转换需要一条一条转换，也就是第一组gsp坐标转换完成百度坐标，才能进行后一组转换,并且先对路线解析完，再解析关键点
            var routePlaceMark, routePlaceMarks = data.routePlaceMarks;
            if (routePlaceMarks && routePlaceMarks.length > 0) {
                getNextRoute();
            } else {//获取信息失败
                callback(false);
            }

            function getNextRoute() {
                if (routePlaceMarks.length > 0) {
                    routePlaceMark = routePlaceMarks.pop();
                    var gpsPoints = [], route = routePlaceMark.route;
                    for (var i in route) {
                        gpsPoints.push(new qq.maps.LatLng(route[i].latitude, route[i].longitude));
                    }
                    gps2bd(gpsPoints, function (bdPoints) {
                        if (!bdPoints || bdPoints.length == 0) {//路线解析失败,停止解析
                            callback(false);
                        } else {
                            var rs = routePlaceMark.routeStyle;
                            var ls = rs ? {
                                    strokeColor: rs.color,
                                    strokeWeight: parseInt(rs.width)
                                } : {};
                            ls.map = mp;
                            ls.path = bdPoints;

                            overlay.push(new qq.maps.Polyline(lineStyle(ls)));

                            overlay.push(marker(bdPoints[0]));//起点
                            overlay.push(marker(bdPoints[bdPoints.length - 1], 0, 34));//终点

                            getNextRoute();
                        }
                    });
                } else {
                    placeMark();//对应折线中的点描述(标注)
                }
            }

            //生成折线的起点终点覆盖物
            function marker(point, x, y) {//x,y表示图片偏移
                x = x | 0;
                y = y | 0;
                return new qq.maps.Marker(
                    {
                        position: point,
                        icon: new qq.maps.MarkerImage("../image/baidu/markers.png", size,
                            new qq.maps.Point(x, y)),
                        map: mp
                    });
            }
        }

        function placeMark() {//对应折线中的点描述
            var gpsPoints = [], keyPointPlaceMarks = data.keyPointPlaceMarks;
            if (keyPointPlaceMarks && keyPointPlaceMarks.length > 0) {
                $.each(keyPointPlaceMarks, function (index, keyPointPlaceMark) {
                    gpsPoints.push(new qq.maps.LatLng(keyPointPlaceMark.coordinate.latitude, keyPointPlaceMark.coordinate.longitude));
                });
            }
            gps2bd(gpsPoints, function (bdPoints) {
                if (bdPoints && bdPoints.length > 0) {
                    for (var index in bdPoints) {
                        (function (keyPointPlaceMark) {
                            var marker = new qq.maps.Marker({position: bdPoints[index], map: mp});
                            overlay.push(marker);
                            qq.maps.event.addListener(marker, "click", function () {
                                slide(keyPointPlaceMark.desc, keyPointPlaceMark.name);//图片，视频展示
                            });
                        })(keyPointPlaceMarks[index]);
                    }
                }
                showInMap();
            });

            function slide(desc, name) {
                if (desc && desc.length > 0) {
                    var msg = [];
                    $.each(desc, function (index, val) {
                        var child = {};
                        if (val.indexOf('photo') > -1) {
                            child.type = "image";
                            child.href = web_prefix + '/Image.do?path=' + val + '&thumbnail=true';
                            child.title = '<button type="button" style="margin-right: 5px;" class="btn btn-primary btn-xs" onclick="viewOriginalImg(this, \''
                                + val + '\')">查看原图</button>'
                                + name;
                        } else {
                            child.type = 'html';
                            child.scrolling = "no";
                            child.title = '<a target="_blank" class="btn btn-primary btn-sm" href="' + web_prefix + '/DownloadFile.do?file='+ val + '">'
                                + '点击下载<span class="glyphicon glyphicon-download"></span></a>' +  name;
                            child.href = '<video controls="controls" width="640" height="360">'
                                + '<source src="' + val + '" type="video/mp4" />'
                                + '<object type="application/x-shockwave-flash" data="http://player.longtailvideo.com/player.swf" width="640" height="360">'
                                + '<param name="movie" value="http://player.longtailvideo.com/player.swf" />'
                                + '<param name="allowFullScreen" value="true" />'
                                + '<param name="wmode" value="transparent" />'
                                + '<param name="flashVars" value="controlbar=over&amp;file=' + val + '" />'
                                + '<span class="text-danger"><strong>该视频无法播放,请点击下面按钮进行下载</strong></span>'
                                + '</object>'
                                + '</video>';
                        }
                        msg.push(child);
                        $.fancybox(msg, {
                            loop: false,
                            closeBtn: false,
                            modal: true,
                            helpers: {
                                title: {type: 'inside'},
                                buttons: {}
                            }
                        });
                    });
                } else {
                    $.fancybox(name, {
                        autoCenter: true
                    })
                }
            }
        }

        function showInMap() {
            mapData[id] = overlay;
            jmap(id).setViewPort();
            callback(true);
        }
    };

    init.prototype = jmap.fn;
    window.jmap = jmap;
})();


function viewOriginalImg(it, val) {
    var ori = new Image(), $it = $(it), old = $it.html();
    ori.src = web_prefix + '/Image.do?path='+ val;
    $it.html("正在加载");
    function replace() {
        $it.parents("div.fancybox-skin:first").find("div.fancybox-inner").html(ori);
        ori.className = "fancybox-image";
        $it.html(old).addClass("disabled");
    }
    if (ori.complete) {
        replace();
    } else {
        ori.onload = function () {
            replace();
        };
    }
}
