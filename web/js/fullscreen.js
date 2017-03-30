/**
 * Created by jecyhw on 2017/3/29.
 */
$(document).ready(function () {
    var btnHtml = '<div style="position: absolute; z-index: 100000; right: 5px; bottom: 5px;"><a href="#" id="fullscreen" class="btn btn-xs btn-danger"><i class="glyphicon glyphicon-resize-full"></i> 全屏</a><a href="#" id="exit_fullscreen" class="btn btn-xs btn-danger hide"><i class="glyphicon glyphicon-resize-small"></i> 退出全屏</a></div>'
    var divMap = $('#map');
    if (divMap.length > 0) {
        divMap.append(btnHtml);
        var fs = divMap.find('#fullscreen'), efs = divMap.find('#exit_fullscreen');

        fs.click(function () {
            efs.removeClass('hide');
            fs.addClass('hide');
            divMap.removeClass('map').addClass('fullscreen');
        });
        efs.click(function () {
            efs.addClass('hide');
            fs.removeClass('hide');
            divMap.addClass('map').removeClass('fullscreen');
        })
    }
});
