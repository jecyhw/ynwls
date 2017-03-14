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
