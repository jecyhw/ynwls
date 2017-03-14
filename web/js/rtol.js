/**
 * Created by SNNU on 2015/5/14.
 */
function Rtol(point, inf) {
    this.point = point;
    this.inf = inf;
    qq.maps.Overlay.call(this, point, inf);
}

Rtol.prototype = new qq.maps.Overlay();
Rtol.prototype.construct = function () {
    var it = this,
        option = Rtol.DEFAULTS,
        $div = $(option.template),
        $inner = $div.find(".tooltip-inner"),
        $arrow = $div.find(".tooltip-arrow"),
        inf = it.inf,
        div = it.div = $div[0];

    $div.css({
        //"z-index": BMap.Overlay.getZIndex(it.point.lat),
        "white-space": "nowrap"
    });

    $arrow.css({
        "border-left-width": 0,
        "left": option.al,
        "margin-left": 0,
        "border-top-color": inf.c
    });
    $inner
        .html(inf.n)
        .css({
            "background-color": inf.c,
            "color": getColor(inf.c)
        }).hover(function () {
            $inner.css({
                cursor: "pointer",
                "text-decoration": "underline"
            });
        }, function () {
            $inner.css({
                cursor: "default",
                "text-decoration": "none"
            });
        }).click(function () {
            var $pop;
            if (infoWindow.update(it)) {
                $pop = infoWindow.pop;
                $pop.show();
            } else {
                $pop  = infoWindow.pop;
                if ($pop.is(":visible")) {
                    $pop.hide();
                    it.pop = undefined;
                    return ;
                } else {
                    $pop.show();
                }
            }
            it.pop = $pop[0];
            it.pop.width = $pop.outerWidth();
            it.pop.height = $pop.outerHeight();
            it.draw();

            //计算是否需要平移
            var mapB = mp.getBounds();//获取可视区域
            var ne = {
                x: parseInt($pop.css("left")) + it.pop.width,
                y: parseInt($pop.css("top"))
            };
            var mne = it.getProjection().fromLatLngToContainerPixel(mapB.getNorthEast());
            var dx = mne.x - ne.x - 20,
                dy = mne.y - ne.y + 10;
            if (dx >= 0) {
                dx = 0;
            }
            if (dy <= 0) {
                dy = 0;
            }

            if (dy == 0) {//正北方向不需要平移，检查正南方向
                var sw = {
                    x: parseInt( $div.css("left")),
                    y: parseInt($pop.css("top")) + it.pop.height
                };
                var msw = it.getProjection().fromLatLngToContainerPixel(mapB.getSouthWest());
                dy = msw.y -sw.y - 10;
                if (dy >= 0) {
                    dy = 0;
                }
            }
            mp.panBy(dx, dy);
        });

    this.getPanes().overlayMouseTarget.appendChild(div);
    div.height = $div.outerHeight();
    div.width = $div.outerWidth();
    return div;
};

Rtol.prototype.draw = function () {
    var it = this,
        pixel = it.getProjection().fromLatLngToDivPixel(it.point),
        $div = $(it.div),
        option = Rtol.DEFAULTS;
    $div.css({
        top: pixel.y - it.div.height,
        left: pixel.x - option.al
    });
    if (it.pop && it.inf == infoWindow.inf) {
        $(it.pop).css({
            top: pixel.y - it.div.height / 2 - it.pop.height / 2,
            left: pixel.x - option.al + it.div.width
        });
    }
};

Rtol.prototype.destroy = function() {
    //移除dom
    this.div.parentNode.removeChild(this.div);
    this.div = null;
};

Rtol.prototype.update = function (point, inf) {
    this.point = point;
    this.inf = inf;
    this.draw();
    infoWindow.update(inf);
};

Rtol.prototype.highlight = function () {
    var it = this;
    var $inner = $(it.div).find(".tooltip-inner"),
        fc = ["#000000", "#FFFFFF"],
        fci = 0;
    it.oldColor = $inner.css("color");
    if (it.oldColor != fc[fci]) {
        fci = 1;
    }
    it.timer = setInterval(function () {
        $inner.css("color", fc[fci]);
        fci = 1 - fci;
    }, 500);

    setTimeout(function () {
        it.unHighlight();
    }, 5000)
};

Rtol.prototype.on = function (type, fn) {
    $(this.div).on(type, fn);
};

Rtol.prototype.unHighlight = function () {
    var $inner = $(this.div).find(".tooltip-inner");
    clearInterval(this.timer);
    this.timer = undefined;
    $inner.css("color", this.oldColor);
    this.oldColor = undefined;
};

Rtol.DEFAULTS = {
    al: 4,
    ah: 5,
    template: '<div class="tooltip top in" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'
};

function getColor(bg) {
    var s = 0;
    for (var i = bg.length - 1; i > 0; i--) {
        s += parseInt(bg[i], 16);
    }
    return  (90 - s) >= s ? "#FFFFFF" : "#000000";
}

var infoWindow = {
    update: function (overlay) {
        var pop = infoWindow.pop;
        if (!pop) {
            pop = infoWindow.pop = $(infoWindow.DEFAULTS.template);
            overlay.getPanes().floatPane.appendChild(pop[0]);
            pop.find('button').on("click", function() {
                pop.hide();
            });
            pop.find("a").on("click", function () {
                uhi(null, infoWindow.inf.n);
                pop.hide();
            });
        }

        if (overlay.inf != infoWindow.inf) {
            var inf = overlay.inf;
            pop.find(".name").html(inf.n);
            pop.find(".j").html(inf.j);
            pop.find(".w").html(inf.w);
            pop.find(".h").html(inf.h);
            pop.find(".t").html(inf.t);
            infoWindow.inf = inf;
            return true;
        }
        return false;
    }
};


infoWindow.DEFAULTS = {
    template: '<div class="popover fade right in">'+
    '<div class="arrow"></div>'+
    '<div class="popover-title">用户信息'+
    '<button type="button" class="close" data-dismiss="alert" aria-label="Close">'+
    '<span aria-hidden="true">&times;</span>'+
    '</button>'+
    '</div>'+
    '<div class="popover-content" style="white-space: nowrap;">'+
    '<table class="table table-bordered table-hover table-condensed table-striped" style="margin-bottom: 0px;">'+
    '<tbody>'+
    '<tr><td>用户名:</td><td><span class="name"></span></td></tr>'+
    '<tr><td>经度:</td><td><span class="j"></span></td></tr>'+
    '<tr><td>纬度:</td><td><span class="w"></span></td></tr>'+
    '<tr><td>海拔:</td><td><span class="h"></span></td></tr>'+
    '<tr><td>更新时间:</td><td><span class="t"></span></td></tr>'+
    '<tr><td colspan="2" align="center"><a href="javascript:void(0)">查看当天轨迹</a></td></tr>'+
    '</tbody>'+
    '</table>'+
    '</div>'+
    '</div>'
};
