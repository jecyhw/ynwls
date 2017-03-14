/**
 * Created by jecyhw on 2014/10/15.
 */
var mp;//地图实例
$(document).ready(function () {
    loadBg();
    mp = createMap('map');
    locateByIp(mp);

    var proj;
    (function () {
        var overlay = new qq.maps.Overlay();
        overlay.draw = function() {
            proj = this.getProjection();
        };
        overlay.setMap(mp);
    })();

    var clipMap;//地图区域选择实例
	$("#stabs").tabs({
		activate: function(e, ui) {
            var height = $("#stabs").outerHeight();
			$(".bottom").css("top", height);
            $(".top-right").height(height);
            $('.table-overlay').css("top", height);
            var regionCapture =  ui.oldPanel.find("button[name='region_capture']");
            if (regionCapture.length > 0 && regionCapture.html().indexOf("选择区域") == -1) {
                regionCapture.children().eq(0).html('选择区域');
                mp.setOptions({
                    draggableCursor: 'default',
                    draggable: false
                });
                clipMap.setOptions({ hide: true, disable: true });
            }
		},
		create: function(e, ui) {
            var $tabSelf = $(this),
                height = $("#stabs").outerHeight();
            $(".bottom").css("top", height);
            $(".top-right").height(height);
            $('.table-overlay').css("top", height);
            query($.toJSON({recorder: $("#account").html()}));

            //地图区域绑定
            clipMap = $("#map").imgAreaSelect({
                disable: true,
                instance: true,
                parent: "#map",
                zIndex: 10,
                onSelectEnd: function (it, selection) {
                    var index = $("#stabs").tabs("option", "active"),
                        _topLeft = proj.fromContainerPixelToLatLng(new qq.maps.Point(selection.x1, selection.y1)),
                        _bottomRight = proj.fromContainerPixelToLatLng(new qq.maps.Point(selection.x2, selection.y2));
                    _topLeft = coordtransform.gcj02towgs84(_topLeft.lng, _topLeft.lat);
                    _bottomRight = coordtransform.gcj02towgs84(_bottomRight.lng, _bottomRight.lat);
                    $("#left" + index).val(_topLeft[0]);
                    $("#bottom" + index).val(_topLeft[1]);
                    $("#right" + index).val(_bottomRight[0]);
                    $("#top" + index).val(_bottomRight[1]);
                    console.log(selection);
                    console.log(_topLeft);
                    console.log(_bottomRight);
                }
            });

            var inFocus = false;
            $('input').focus(function() {
                inFocus = true;
            });

            $('input').blur(function() {
                inFocus = false;
            });

            $(document).keyup(function(e) {
                if (e.keyCode == 13) {
                    if (inFocus) {
                        $("#search_submit" + $tabSelf.tabs("option", "active")).click();
                    }
                }
            });

			$("button[name='search_submit']").click(function () {
                var tabsIndex = $tabSelf.tabs("option", "active"),
                    panel = $tabSelf.find("#stabs-" + tabsIndex),
                    count = 0,
                    cons = {},
                    $this = $(this);

                panel.find("input[type='text']").each(function (_index, text) {
                    if (!isNullOrEmpty(text.value)) {
                        cons[$(text).attr("name")] = text.value;
                        count++;
                    }
                });

                if (count > 0) {
                    var regionCapture = $tabSelf.find("#region_capture" + tabsIndex);
                    if (regionCapture.length > 0 && regionCapture.html().indexOf("选择区域") == -1) {
                        regionCapture.children().eq(0).html('选择区域');
                        mp.setOptions({
                            draggableCursor: '-moz-grab',
                            draggable: true
                        });
                        clipMap.setOptions({ hide: true, disable: true });
                    }
                } else {
                    cons['recorder'] = $('#account').text();
                }
                $this.addClass("disabled");
                var html = $this.html();
                $this.html("正在查询...");
                query($.toJSON(cons), function () {
                    $this.html(html);
                    $this.removeClass("disabled");
                });
            });

            function isNullOrEmpty(val) {
                return  val == null || val == "";
            }

            function query(conditions, callback) {
                var queryResult =  $("#query-result");
                $.ajax({
                    url: web_prefix + "/QueryRecord.do",
                    type: "post",
                    dataType: "json",
                    data: {
                        data: conditions
                    },
                    success: function (data) {
                        if (data.length == 0) {
                            queryResult.html('<tr><td colspan="4"  class="text-center">无搜索记录</td></tr>');
                        }
                        else {
                            var body = "";
                            $.each(data, function (k, v) {
                                if (v.trackid) {
                                    body += "<tr id='" + v.trackid + "'><td><input type='checkbox' data-length='" + Math.round(v.length) + "'/></td>"
                                    + "<td><a class='btn btn-link btn-xs' href='javascript:void(0)'>"
                                    + (v.name == undefined ? '无' : v.name) + "</a></td><td>"
                                    + (v.starttime == undefined ? '无' : v.starttime) + "</td><td>"
                                    + calcFileSize(v.filesize) + "</td></tr>";
                                }
                            });
                            queryResult
                                .hide()
                                .html(body)
                                .fadeIn();
                        }
                    },
                    error: function () {
                        queryResult
                            .hide()
                            .html('<tr><td colspan="4" class="text-center">查询出错</td></tr>')
                            .fadeIn();
                    },
                    beforeSend: function () {
                        jmap.clear();
                        // TODO 缺清除地图的所有覆盖物
                        $("#track-length").addClass("hidden").find("span").html(0);
                        queryResult
                            .hide()
                            .html('<tr><td colspan="4" class="text-center">正在查询中</td></tr>')
                            .fadeIn();
                    },
                    complete: function (xhr) {
                        checkTimeout(xhr);
                        if (callback) {
                            callback();
                        }
                    }
                });
            }

            $("button[name='region_capture']").each(function(index, clip){
                $(clip).click(function () {
                    var $it = $(this);
                    if ($it.html().indexOf("选择区域") > -1) {
                        $it.children().eq(0).html('取消截图');
                        mp.setOptions({
                            draggableCursor: 'default',
                            draggable: false
                        });
                        clipMap.setOptions({ enable: true });
                    } else {
                        $it.children().eq(0).html('选择区域');
                        mp.setOptions({
                            draggableCursor: '-moz-grab',
                            draggable: true
                        });
                        clipMap.setOptions({ hide: true, disable: true });
                    }
                });
            });
            //

            var  $startTime =  $("input[name='startTime']"),
                $endTime = $("input[name='endTime']");
            //日历绑定
            $startTime.each(function(index, time) {
                $(time).datepicker(
                    $.extend($.datepicker.regional['zh-CN'],{
                        dateFormat:"yy-mm-dd",
                        //changeMonth:true,
                        //changeYear:true,
                        maxDate: new Date(),
                        showButtonPanel: true,
                        numberOfMonths: 2,
                        //showOn: 'both',
                        onSelect: function (selectedDate ) {
                            $endTime.eq(index).datepicker("option", "minDate", selectedDate);
                            $endTime.eq(index).datepicker("option", "defaultDate", selectedDate);
                        }
                    })
                );
            });
            $endTime.each(function(index, time) {
                $(time).datepicker(
                    $.extend($.datepicker.regional['zh-CN'],{
                        dateFormat:"yy-mm-dd",
                        //changeMonth:true,
                        showButtonPanel: true,
                        numberOfMonths: 2,
                        //showOn: 'both',
                        onSelect: function (selectedDate ) {
                            $startTime.eq(index).datepicker("option", "maxDate", selectedDate);
                            $startTime.eq(index).datepicker("option", "defaultDate", selectedDate);
                        }
                    })
                );
            });
		}
	});

    $("#query-result").on("click", "input", function(){//将对应选中的轨迹显示到地图
        var it = this,
            id = it.parentNode.parentNode.id,
            track = $("#track-length"),
            track_span = track.find("span");
        if (it.checked) {
            //先查看之前是否已经查询过
            if (!jmap(id).show()) {
                $('.table-overlay').show();
                var $id = $('#' + id);
                $.ajax({
                    url: web_prefix + '/RouteRecordMapInfo.do',
                    data: { id: id },//获取tr的id
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        jmap.loadData(id, data, function(tag) {
                            if (tag) {//表示加载成功
                                $id.addClass('success');
                                if (track.hasClass("hidden")) {
                                    track.removeClass("hidden").fadeIn();
                                }
                                track_span
                                    .hide()
                                    .html(parseInt($(it).attr("data-length")) + parseInt(track_span.html()))
                                    .fadeIn();
                            } else {
                                $id.addClass("danger");
                                tooltipShow($(it), 2000, "轨迹显示失败");
                                it.checked = false;
                            }

                        });
                    },
                    error: function () {
                        $id.addClass('danger');
                    },
                    beforeSend: function () {
                        $("button[name='search_submit']").addClass("disabled");
                    },
                    complete: function (xhr) {
                        $("button[name='search_submit']").removeClass("disabled");
                        $('.table-overlay').hide();
                    }
                });
            } else {
                jmap(id).setViewPort();
                track_span
                    .hide()
                    .html(parseInt($(it).attr("data-length")) + parseInt(track_span.html()))
                    .fadeIn();
            }
        }
        else {
            //隐藏
            jmap(id).hide();
            track_span
                .hide()
                .html(parseInt(track_span.html()) - parseInt($(it).attr("data-length")))
                .fadeIn();
        }
    }).
        on('click', 'a', function(){//查看详细信息
            var it = this;
            $.getJSON(web_prefix + '/ViewSingleRouteRecordInfo.do',
                {
                    id: it.parentNode.parentNode.id
                },
                function(data) {
                    var name = data.name == undefined ? '无' : data.name,
                        author = data.author == undefined ? '无' : data.author,
                        starttime = data.starttime == undefined ? '无' : data.starttime,
                        endtime = data.endtime == undefined ? '无' : data.endtime,
                        length = data.length == undefined ? '无' : data.length,
                        maxaltitude = data.maxaltitude == undefined ? '无' : data.maxaltitude,
                        keysiteslist = data.keysiteslist == undefined ? '无' : data.keysiteslist,
                        annotation = data.annotation == undefined ? '无' : data.annotation,
                        path = data.path == undefined ? '无' : data.path,
                        filesize = data.filesize == undefined ? '无' : data.filesize;

                    var msg = '<table class="table table-bordered table-hover table-condensed table-striped">' +
                        '<tr>' +
                        '<td>名称:</td>' +
                        '<td><span>' + name + '</span></td>' +
                        '<td>提供者:</td>' +
                        '<td><span>' + author + '</span></td>' +
                        '<td rowspan="5" style="vertical-align: middle;"><img src="../image/download.png" id="'+ data.trackid + '"></td>' +
                        '</tr>' +
                        '<tr>' +
                        '<td>开始记录时间:</td>' +
                        '<td><span>' + starttime + '</span></td>' +
                        '<td>结束时间:</td>' +
                        '<td><span>' + endtime + '</span></td>' +
                        '</tr>' +
                        '<tr>'  +
                        '<td>轨迹长度:</td>' +
                        '<td><span>' + length + '米</span></td>' +
                        '<td>最大高度:</td>' +
                        '<td><span>' + maxaltitude + '米</span></td>' +
                        '</tr>' +
                        '<tr>' +
                        '<td>关键点:</td>' +
                        '<td><span>' + keysiteslist + '</span></td>' +
                        '<td>描述:</td>' +
                        '<td><span>' + annotation + '</span></td>' +
                        '</tr>' +
                        '<tr>' +
                        '<td>存放目录:</td>' +
                        '<td><span>' + path + '</span></td>' +
                        '<td>轨迹大小:</td>' +
                        '<td><span>' + calcFileSize(filesize) + '</span></td>' +
                        '</tr>' +
                        '</table>';
                    $.fancybox(msg);
                }
            );
        });

    $("body").on("click", "div.fancybox-inner table img", function() {
        exportTrackRecord("[" + this.id + "]")
    });

    function calcFileSize(fileSize) {
        if (!fileSize)
            return fileSize;
        var unit = "b";
        fileSize = parseFloat(fileSize);
        if (fileSize > 1023) {
            fileSize /= 1024;
            unit = "K";
            if (fileSize > 1023) {
                fileSize /= 1024;
                unit = "M";
                if (fileSize > 1023) {
                    fileSize /= 1024;
                    unit = "G";
                }
            }
            fileSize = fileSize.toFixed(2);
        }
        return fileSize + unit;
    }


    $(function() {
        var $fileList = $("#file-list"),
            $fileUploadList = $("#file-upload-list");
        $fileList.plupload({
            url : web_prefix +  '/UploadFile.do',
            max_retries: 3,
            chunk_size: '1024kb',
            filters : {
                max_file_size : '1000mb',
                mime_types: [
                    {title : "kmz files", extensions : "kmz"}
                ],
                prevent_duplicates: true,
                checkExisting: web_prefix + "/CheckFileExist.do"
            },
            flash_swf_url : 'js/plupload/Moxie.swf',
            silverlight_xap_url : 'js/plupload/Moxie.xap'
        });

        plupload.addFileFilter("checkExisting", function(checkUrl, file, cb) {
            if (checkUrl) {
                $.getJSON(checkUrl,
                    {filename: file.name},
                    function(data) {
                        if (data.status != 0) {
                            $fileList.plupload('notify', 'error', file.name + '文件在服务器端已存在');
                            cb(false);
                        }
                        else {
                            cb(true);
                        }
                    }
                );
            }
        });

        $("#file_upload").click(function() {
            $fileUploadList.show();
        });
        //上传最小化,上传最大化,上传关闭

        $("#upload_minimize").click(function () {
            $fileList.hide();
            $(this).addClass("disabled").next().removeClass("disabled");

        }).next().click(function () {
            $fileList.show();
            $(this).addClass("disabled").prev().removeClass("disabled");
        }).next().click(function () {
            var queueLength = $fileList.plupload('getFiles').length;//$("#kmz_file").uploadify("settings", "queueLength");
            if (queueLength > 0) {
                var $tipMsg = $('<div class="text-info">提示!<hr/><p><strong>列表中有未上传完成的文件，\n确定要放弃上传吗？</strong>' +
                '</p><div class="text-right"><a class="btn btn-info btn-sm">确定</a><a class="btn btn-info btn-sm focus">取消</a></div></div>');
                $.fancybox($tipMsg, {
                    modal: true,
                    closeBtn: false,
                    afterShow: function () {
                        $tipMsg.find(".focus").click(function () {
                            $.fancybox.close(true);
                        })
                            .prev().click(function () {
                                $fileUploadList.hide();
                                $fileList.plupload('clearQueue');
                                $.fancybox.close(true);
                            });
                    }
                });
            } else {
                $fileUploadList.hide();
            }
        });

        $("#file-upload-error").bind('closed.bs.alert', function () {
            $fileUploadList.hide();
        });
    });

    $("#exit").click(function() {
        $.ajax({
            url: web_prefix + "/UserLogOutServlet",
            type: "post",
            dataType: "json",
            async:false,
            complete: function() {
                window.location.href = "../index.html";
            }
        });
    });

    //导出选中轨迹
    $("#export_track_record").click(function () {
        var ids = jmap.ids();
        if (ids.length > 0) {
            exportTrackRecord($.toJSON(ids));
        } else {
            tooltipShow($(this), 2000, "请先选中要导出的轨迹", {
                placement: 'top'
            });
        }
    });

    //导出轨迹
    function exportTrackRecord(ids) {
        var $export_track_record = $("#export_track_record");
        $export_track_record.attr({"disabled":"disabled"});
        var val = $export_track_record.html()
        $export_track_record.html("正在导出...");
        var form = $("<form method='post' style='display: none;' action= '"
        + web_prefix
        +"/user/download.jsp' target='_blank'><input type='hidden' name='ids' value='"
        + ids +"'/></form>");   //定义一个form表单
        $(document.body).append(form);  //将表单放置在web中
        form.submit();
        form.remove();
        $export_track_record.html(val);
        $export_track_record.removeAttr("disabled");
    }
    //保存选中轨迹
    $("#save_track_record").click(function () {
        var ids = jmap.ids(),
            $it = $(this);
        if (ids.length > 1) {
            $it.attr({"disabled":"disabled"});
            var val = $it.html();
            $it.html("正在保存...");
            $.getJSON(web_prefix + "/SaveRecord.do",
                {
                    ids: $.toJSON(ids)
                },
                function(data) {
                    var msg;
                    if (data.status == 0) {
                        msg = '<div class="text-info">提示!<hr/><div class="text-center"><strong>保存成功。</strong></div></div>';
                    }
                    else {
                        msg = '<div class="text-info"><strong>提示!</strong><hr/><div class="text-center"><strong>保存失败。</strong></div></div>';
                    }
                    $.fancybox(msg);
                    //查询完后，恢复提交按钮
                    $it.removeAttr("disabled");
                    $it.html(val);
                }
            );
        } else {
            tooltipShow($(this), 2000, "请先选中至少两条轨迹", {
                placement: 'top'
            })
        }
    });


});

function log(msg) {
    console.log(msg);
}

//加载背景图片
function loadBg() {
    var img_bg_count = 2, img_bg_load_count = 0;
    var img_bgs = new Array(img_bg_count);
    for (var i = 0; i < img_bg_count; i++) {
        img_bgs[i] = new Image();
        img_bgs[i].onload = function () {
            img_bg_load_count++;
            if (img_bg_count == img_bg_load_count) {
                var $show_rtup = $("#show_rtup"),
                    offset = $show_rtup.offset(),
                    $tip = $show_rtup.find(".tooltip"),
                    osTop = offset.top + $tip.height() / 2,
                    osL = $show_rtup.width() - $tip.width() - 10,
                    tipW = $tip.width() + 10;
                $show_rtup.hover(function (e) {
                    $tip.addClass("in");
                }, function () {
                    $tip.removeClass("in");
                }).mousemove(function (e) {
                    var top = e.pageY - osTop,
                        left = e.pageX - offset.left;
                    if (left > osL) {
                        if ($tip.hasClass("right")) {
                            $tip.removeClass("right").addClass("left");
                        }
                        left -= tipW;
                    } else {
                        if ($tip.hasClass("left")) {
                            $tip.removeClass("left").addClass("right");
                        }
                    }
                    $tip.css({
                        top: top,
                        left: left
                    });
                });
                $show_rtup.append($(this));
                img_bg_load_count = 0;
                window.setInterval(function () {
                    img_bg_load_count = (img_bg_load_count + 1) % img_bg_count;
                    $show_rtup.children("img").replaceWith($(img_bgs[img_bg_count - img_bg_load_count - 1]));
                }, 5000);
            }
        }
        img_bgs[i].src = web_prefix + "/image/top_bg" + i + ".jpg";
    }
}
