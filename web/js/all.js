/**
 * Created by SNNU on 2015/5/7.
 */
var web_prefix = "/ynwls";

/**
 *
 * @param $tag 标签的jq对象
 * @param st 显示时间
 */
function tooltipShow($tag, st, msg, options) {
    var o = $.extend({
        placement: 'auto',
        delay: { "show": 500, "hide": 100 },
        trigger: 'manual',
        container: 'body'
    }, options);

    $tag.attr("data-original-title", msg)
        .focus()
        .tooltip(o).tooltip("show");
    setTimeout(function () {
        $tag.tooltip("destroy");
    }, st);
}

$(document).ajaxComplete(function (e, xhr, options) {
    checkTimeout(xhr)
});

var timeOut = false;

function checkTimeout(xhr) {
    var sessionstatus = xhr.getResponseHeader("sessionstatus"); // 通过XMLHttpRequest取得响应头，sessionstatus，
    if (sessionstatus == "timeout" && !timeOut) {// 如果超时就处理 ，指定要跳转的页面
        timeOut = true;
        var i = 5;
        var $timeOutMsg = $('<div class="text-danger"><strong>会话超时，请重新登陆!</strong><hr/><p><span class="text-primary">'
            + i + '</span>秒后自动跳转自登陆页面</p><p>也可点击<a class="btn  btn-danger btn-sm">此处</a>跳转</p></div>');
        $.fancybox($timeOutMsg, {
            modal: true,
            closeBtn: false,
            autoCenter: true,
            afterShow: function () {
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
