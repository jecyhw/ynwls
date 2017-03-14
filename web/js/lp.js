/**
 * Created by SNNU on 2015/5/10.
 */
$(document).ready(function() {
    $(".content").css("top", $(".header").height());

    mp = createMap("map");
    locateByIp(mp, function () {
        if (window.WebSocket){
            ws();
        } else{
            longPoll();//采用ajax长连接
        }
        // rt.start();
    });

    $(".panel").each(function(index, panel) {
        var $bc = $(panel).find("button.clear"),
            $bs = $(panel).find("button.search"),
            id = $bc.attr("data-for");
        $bs.click(function() {
            var $in = $("#" + id);
            if ($in.val() == "") {
                tooltipShow($in, 1000, "用户名不能为空");
            } else {
                eval(id + '($bs,  "' + $in.val() +  '")');//根据字符串来动态执行upi或者uhi函数
            }
        });

        $bc.click(function() {
            eval("c_" + id + "()");
            $("#" + id).val("");
        });
    });

    $("#online-number").click(function () {
        rt.setViewport();
    });
});
var mp;
var rt = {
    /**
     * 地图上的marker
     */
    mk: [],
    /**
     * 记录查询出来的的uid
     */
    qd: [],
    /**
     * 查询的历史轨迹
     */
    py: [],
    /**
     * 颜色索引
     */
    color_index : -1,
    /**
     * 默认每五秒更新一次，如果某次请求未获取到更新数据,更新时间将会逐渐增加，如有数据更新，更新时间
     */
    lpt: 5000,
    /**
     * 最长的更新时间为1分钟
     */
    maxLpt: 60000,
    /**
     * 最短的更新时间为5秒
     */
    minLpt: 5000,
    /**
     * 每次更新的人数
     */
    upc: 0,
    /**
     * 默认离当前时间相差5分钟,用来检查用户是否离线
     */
    dit: 5,
    /**
     * 每个dit时间检查离线用户
     */
    olt: 1000 * 60 * 5,
    /**
     * 离线用户检查的计时器
     */
    timer: undefined,
    /**
     * 在线人数
     */
    olc: 0,
    /**
     * 根据uid来删除用户相关的信息
     * @param uid
     */
    del: function(uid) {
        var t = rt.mk[uid];
        mp.removeOverlay(t);
        rt.qd[uid] = rt.py[uid] = rt.mk[uid]  = undefined;
        rt.online(-1);
        tooltipShow($(".online"), 2000, t.inf.t.replace(/\d{4}-\d{2}-\d{2} /, "") + " " + t.inf.n + "下线了", {
            placement: "right"
        });
    },
    /**
     *
     * @param inf us.inf对象
     * @param bd 百度地图坐标
     */
    update: function (inf, bd) {
        rt.mk[inf.i].update(bd, inf);
        tooltipShow($(".online"), 2000, inf.t.replace(/\d{4}-\d{2}-\d{2} /, "") + " " + inf.n + "更新了", {
            placement: "right"
        });
    },
    /**
     *
     * @param inf us.inf对象
     * @param bd 百度地图坐标
     */
    add: function (inf, bd) {
        var t = new Rtol(bd, inf);
        rt.mk[inf.i] = t;
        t.setMap(mp);//添加到地图
        tooltipShow($(".online"), 2000, inf.t.replace(/\d{4}-\d{2}-\d{2} /, "") + " " + inf.n + "上线了", {
            placement: "right"
        });
        rt.online(1);
        return t;
    },
    clear: function() {
        rt.qd = rt.py = rt.mk = [];
        rt.color_index = -1;
        mp.clearOverlays();
    },

    exists: function (uid) {
        return rt.mk[uid];
    },
    /**
     * 离线用户检查
     */
    offline: function () {
        var di = [];
        //先获取离线用户
        for (var i in rt.mk) {
            if (!rt.qd[i] && rt.mk[i].inf.comByT()) {//当前uid不在查询的uid中，且时间和现在超过rt.olt分钟
                di.push(i);
            }
        }
        //执行删除
        for (var i = di.length - 1; i >= 0; i--) {
            rt.del(di[i]);
        }
    },
    /**
     * 启动离线用户检查
     */
    start: function() {
        rt.timer = setInterval(rt.offline, rt.olt);//每隔五分钟检查一次
    },
    /**
     * 停止离线用户检查
     */
    stop: function () {
        clearInterval(rt.timer);
    },
    getci: function () {
        if (rt.color_index >= color_list.length) {
            rt.color_index = 0;
        } else {
            rt.color_index++;
        }
        return color_list[rt.color_index];
    },

    setViewport: function () {
        var bd = [];
        for (var i in rt.mk) {
            bd.push(rt.mk[i].point);
        }
        setViewport(bd);
    },
    online: function (c) {
        rt.olc += c;
        $(".online").html(rt.olc);
    }
};

function setViewport(bd) {
    if (bd.length > 0) {
        var bounds = new qq.maps.LatLngBounds(bd[0], bd[0]);
        for (var i = 1; i < bd.length; ++i) {
            bounds.extend(bd[i]);
        }
        mp.fitBounds(bounds);
    }
}

var us = {};
/**
 *
 * @param urt 用户的更新数据
 */
us.inf = function(urt){
    /**
     * 海拔
     * @type {number|Number}
     */
    this.h = urt.altitude;
    /**
     * 颜色值
     */
    this.c = rt.getci();
    /**
     * 用户的gps的经度
     * @type {Number}
     */
    this.j = urt.longitude;
    /**
     * 用户的gps的纬度
     * @type {Number}
     */
    this.w = urt.latitude;
    /**
     * 用户的更新时间
     * @type {Date String}
     */
    this.t = urt.time;
    /**
     * 用户的名字
     * @type {String}
     */
    this.n = urt.name;
    /**
     * 用户的uid
     * @type {int}
     */
    this.i = urt.uid;
    /**
     * 判断两个gps是否一致
     * @param a us.inf对象
     * @returns {boolean}
     */
    this.eqGps = function(a){
        return this.j == a.j && this.w == a.w;
    };
    /**
     * 判断是否相等
     * @param a
     * @returns {boolean}
     */
    this.equals = function (a) {
        return a.t == this.t;
    }
    /**
     * 将gps的经纬度用逗号连接
     * @returns {string}
     */
    this.ts = function () {
        return new qq.maps.LatLng(this.w, this.j);
    };
    /**
     * 和当前时间比较,判断差是否大于5分钟
     * @returns {boolean}
     */
    this.comByT = function() {
        return (new Date().getTime() - Date.parse(this.t.replace(/-/g, '/'))) / (1000 * 60) > rt.dit;
    }
};

/**
 * 获取gps转换百度坐标的的请求参数形式,并且过滤掉在地图上已显示但坐标未更新的用户数据
 * @param inf us.inf对象
 * @returns {Array}
 */
function formatGps(inf) {
    var re = [],
        len = inf.length;
    for (var i = 0; i < len; i++) {
        re.push(inf[i].ts());
    }
    return re;
}

/**
 * 过滤已显示用户的信息且获取的更新数据未变 ,并转换为us.inf对象集合
 * @param urt 所有的用户更新信息
 * @returns {Array}
 */
function filterInf(urt) {
    var re = [];
    if (urt) {
        for (var i = urt.length - 1; i >= 0; i--) {
            var inf = new us.inf(urt[i]);
            if (rt.mk[inf.i] && rt.mk[inf.i].inf.eqGps(inf)) {
                continue;
            }
            re.push(inf);
        }
    }
    return re;
}

/**
 * 将用户的更新信息转换成us.inf对象集合
 * @param urt
 * @returns {Array}
 */
function convertToInf(urt) {
    var re = [];
    if (urt) {
        for (var i = urt.length - 1; i >= 0; i--) {
            re.push(new us.inf(urt[i]));
        }
    }
    return re;
}


/**
 *根据用户的坐标来更新的Marker的位置
 * @param inf us.inf对象集合
 * @param bdPoints 百度坐标集合,一一对应
 * @returns {boolean}
 */
function updateMk(inf, bds) {
    if (!bds || bds.length == 0)//没有需要更新的用户
        return false;

    var len = bds.length;
    for (var i = 0; i < len; i++) {
        if (rt.exists(inf[i].i)) {
            rt.update(inf[i], bds[i])
        } else {
            rt.add(inf[i], bds[i]);
        }
    }
    rt.setViewport();//重新设置最佳视野
    return true;
}

function longPoll() {
    $.ajax({
        type: "post",
        url: web_prefix + "/QueryUserPosition.do",
        dataType: "json",
        success: function (data) {
            if (data.status == 0) {
                var inf = filterInf(data.result);
                gps2bd(formatGps(inf), function(bds) {
                    updateMk(inf, bds);
                    setTimeout(longPoll, rt.lpt);
                });
            }
        },
        error: function () {
            setTimeout(longPoll, rt.lpt);
        }
    });
}

/**
 * webSocket
 */
function ws() {
    $.getJSON(web_prefix + "/QueryUserPosition.do", function (data) {
        if (data.status == 0) {
            var inf = filterInf(data.result);
            gps2bd(formatGps(inf), function(bds) {
                updateMk(inf, bds);
            });
        }
    });

    var wsUrl = "ws://" + window.location.host + web_prefix + "/ShowUserByRealTime";
    var ws = new window.WebSocket(wsUrl);
    ws.onerror = function() {
        longPoll();
    };

    ws.onmessage = function (event) {
        var inf = filterInf(eval(event.data));
        gps2bd(formatGps(inf), function(bds) {
            updateMk(inf, bds);
        });
    };
}

/**
 * 查询用户的最新地理位置信息
 */
function upi($btn, name) {
    $.getJSON( web_prefix + '/QueryUserPosition.do', {name:name}, function (data) {
        if (data.status == 0) {
            //先清空rt.qd
            c_upi();
            var urt = data.result, i, uid;
            if (urt.length == 0) {
                tooltipShow($btn, 2000, "未查找到用户信息");
            } else {
                var bds = [],
                    inf = convertToInf(urt);

                for (i = 0; i < inf.length;) {//先过滤掉在地图上已显示的用户信息
                    uid = inf[i].i;//获取uid
                    var mk = rt.mk[uid];
                    if (mk) {//在地图上已显示
                        mk.highlight();
                        bds.push(mk.point);//添加百度坐标
                        rt.qd[uid] = uid;//添加查询用户的uid
                        inf.splice(i, 1);
                    } else {
                        i++;
                    }
                }

                //下一步将未在地图上显示的用户信息显示出来
                gps2bd(formatGps(inf), function (_bds) {
                    if (_bds) {
                        for (var i = 0; i < inf.length; ) {
                            uid = inf[i].i;//获取uid
                            rt.qd[uid] = uid;
                            var mk = rt.mk[uid];
                            if (!mk) {//在地图上未显示,因为是异步，还是需要再次判断
                                mk = rt.add(inf[i], _bds[i]);
                            }
                            mk.highlight();
                            bds.push(mk.point);
                        }
                    }
                    setViewport(bds);
                });

                tooltipShow($btn, 2000, "查找到" + bds.length + "个用户信息");
            }
        }
    });
}
/**
 * 清除查询的用户实时位置
 */
function c_upi() {
    for (var i in rt.qd) {
        rt.mk[rt.qd[i]].unHighlight();
    }
    rt.qd = [];
}

/**
 * 查询用户的历史轨迹
 */
function uhi($btn, name) {
    $.getJSON( web_prefix + '/QueryUserHistory.do', {name: name}, function (data) {
        if (data.status == 0) {
            c_uhi();
            var uh = data.result;
            if (uh.length == 0 && $btn) {
                tooltipShow($btn, 2000, "未查找到用户信息");
            } else {
                var rc = 0, sc = 0, bdArr = [];
                $.each(uh, function (index, uhi) {
                    (function(uhis) {
                        var gps = [];
                        $.each(uhis.history, function(_index, his) {
                            gps.push(new qq.maps.LatLng(his.latitude, his.longitude));
                        });
                        gps2bd(gps, function (bds) {
                            if (bds) {
                                var options = {}, uid = uhis.uid;
                                options.strokeColor = rt.mk[uid] ? rt.mk[uid].inf.c : rt.getci();
                                options.path = bds;
                                rt.py.push(new qq.maps.Polyline(lineStyle(options)));
                                bdArr = bdArr.concat(bds);
                                sc++;
                            }
                            rc++;
                            if (rc == uh.length) {//全部解析完
                                for (var i = bdArr.length - 1; i >= 0; i--) {
                                    var marker = new qq.maps.Marker({position: bdArr[i]});
                                    (function (his){
                                        var infoWindow = new qq.maps.InfoWindow({
                                            content: '<table class="table table-bordered table-hover table-condensed table-striped" style="margin-bottom: 0px;">'+
                                            '<tbody>'+
                                            '<tr><td>经度:</td><td><span class="j">' + his.longitude + '</span></td></tr>'+
                                            '<tr><td>纬度:</td><td><span class="w">' + his.latitude + '</span></td></tr>'+
                                            '<tr><td>海拔:</td><td><span class="h">' + his.altitude + '</span></td></tr>'+
                                            '<tr><td>时间:</td><td><span class="t">' + his.time + '</span></td></tr>'+
                                            '</tbody>'+
                                            '</table>',
                                            position: marker,
                                            map: mp
                                        });  // 创建信息窗口对象
                                        qq.maps.event.addListener(marker, "click", function () {
                                            infoWindow.open();
                                        });
                                    })(uhis.history[i]);
                                    rt.py.push(marker);
                                }
                                for (var i = rt.py.length - 1; i >= 0; i--) {
                                    rt.py[i].setMap(mp);
                                }
                                setViewport(bdArr);
                                if ($btn) {
                                    tooltipShow($btn, 2000, "查找到" + sc + "个用户信息");
                                }
                            }
                        });
                    })(uhi);
                });
            }
        }
    });
}

/**
 * 移除查询的历史记录
 */
function c_uhi() {
    for (var i = rt.py.length - 1; i >= 0; i--) {
        rt.py[i].setMap(null);
    }
    rt.py = [];
}
