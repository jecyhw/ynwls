<%--
  Created by IntelliJ IDEA.
  User: jecyhw
  Date: 2017/3/23
  Time: 10:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>轨迹统计</title>
    <link rel="stylesheet" type="text/css" href="../js/jquery-ui/jquery-ui.css" />
    <link rel="stylesheet" type="text/css" href="../js/bootstrap/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="../js/datatables/dataTables.bootstrap.min.css" media="screen"/>


    <script type="text/javascript" src="../js/jquery.js"></script>
    <script type="text/javascript" src="../js/echarts/echarts.min.js"></script>

    <script type="text/javascript" src="../js/jquery-ui/jquery-ui.js"></script>
    <script type="text/javascript" src="../js/jquery-ui/i18n/jquery.ui.datepicker-zh-CN.js"></script>
    <script type="text/javascript" src="../js/all.js"></script>
    <script type="text/javascript" src="../js/datatables/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="../js/datatables/dataTables.bootstrap.min.js"></script>

</head>
<style>
    .ui-datepicker select.ui-datepicker-month, .ui-datepicker select.ui-datepicker-year {
        color: black;
    }
</style>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <p></p>
    <div class="row">
        <div class="col-sm-12">
            <div class="form-inline right">
                <label class="control-label" for="startTime2">从</label>
                <input class="form-control input-sm input-xss" type="text" name="startTime" placeholder="开始时间" id="startTime2"/>
                <label class="control-label" for="endTime2">到</label>
                <input class="form-control input-sm input-xss" type="text" name="endTime" placeholder="结束时间" id="endTime2"/>
                <button type="submit" class="btn btn-sm btn-success" id="query">查询</button>
            </div>
        </div>
    </div>
    <hr/>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div class="row">
        <div class="col-md-12">
            <div id="length" style="width: 100%;height:400px;"></div>
        </div>
        <div class="col-md-12">
            <div id="sum" style="width: 100%;height:400px;"></div>
        </div>
    </div>
    <div>
        <table id="page" class="table table-bordered table-hover table-condensed table-striped" cellspacing="0" width="100%">
        </table>
    </div>
</div>
<script type="text/javascript">
    var  $startTime =  $("input[name='startTime']"),
        $endTime = $("input[name='endTime']");
    //日历绑定
    $startTime.each(function(index, time) {
        $(time).datepicker(
            $.extend($.datepicker.regional['zh-CN'],{
                dateFormat:"yy-mm-dd",
                changeMonth:true,
                changeYear:true,
                maxDate: new Date(),
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
                changeMonth:true,
                changeYear:true,
                showButtonPanel: true,
                //showOn: 'both',
                onSelect: function (selectedDate ) {
                    $startTime.eq(index).datepicker("option", "maxDate", selectedDate);
                    $startTime.eq(index).datepicker("option", "defaultDate", selectedDate);
                }
            })
        );
    });
    // 基于准备好的dom，初始化echarts实例
    var sumChart = echarts.init(document.getElementById('sum'));
    var lengthChart = echarts.init(document.getElementById('length'));

    var dataAxis, seriesData;

    var pageDt = $('#page').DataTable( {
        language: {
            url: '../js/datatables/chinese.json'
        },
        columns: [
            { data: 'name', title: '名字' },
            { data: 'count', title: '出勤次数' },
            { data: 'value', title: '总长度(米)'}
        ]
    } );

    $('#query').click(function () {
        sumChart.showLoading();
        $.getJSON(web_prefix + '/Statistics/total', { startTime: $startTime.val(), endTime: $endTime.val() }).done(function (data) {
            dataAxis = [];
            seriesData = [];
            $.each(data, function (i, obj) {
                if (obj.value > 0) {
                    dataAxis.push(obj.name);
                    seriesData.push(obj.value);
                }
            });
            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: '轨迹总长度(米)'
                },
                tooltip: {},
                legend: {
                    data:['总长度']
                },
                xAxis: {
                    data: dataAxis
                },
                yAxis: {},
                dataZoom: [
                    {
                        type: 'inside'
                    }
                ],
                series: [
                    {
                        name: '总长度',
                        type: 'bar',
                        barWidth: 10,
                        data: seriesData
                    }
                ]
            };
            sumChart.setOption(option);
            sumChart.hideLoading();

            pageDt.clear();
            pageDt.rows.add(data).order([2, 'desc']).draw();
        });
        queryLength('<%=session.getAttribute("userName")%>');
    }).click();
    // Enable data zoom when user click bar.
    var zoomSize = 6;


    sumChart.on('click', function (params) {
        console.log(dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)]);
        sumChart.dispatchAction({
            type: 'dataZoom',
            startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
            endValue: dataAxis[Math.min(params.dataIndex + zoomSize / 2, seriesData.length - 1)]
        });
        queryLength(params.name);
    });

    function queryLength(name) {
        lengthChart.showLoading();
        $.getJSON(web_prefix + '/Statistics/length', { startTime: $startTime.val(), endTime: $endTime.val(), author: name }).done(function (data) {
            var axis = [], series = [];
            $.each(data, function (i, obj) {
                if (obj.value > 0) {
                    axis.push(obj.name);
                    series.push(obj.value);
                }
            });
            // 指定图表的配置项和数据
            var option = {
                title: {
                    text:name +  '-历史轨迹长度(米)'
                },
                tooltip: {},
                legend: {
                    data:['折线图', '柱状图']
                },
                xAxis: {
                    data: axis
                },
                yAxis: {},
                series: [
                    {
                        name: '折线图',
                        type: 'line',
                        lineStyle: {
                            normal: {
                                width: 3,
                                shadowColor: 'rgba(0,0,0,0.4)',
                                shadowBlur: 10,
                                shadowOffsetY: 10
                            }
                        },
                        data: series
                    },
                    {
                        name: '柱状图',
                        type: 'bar',
                        barWidth: 10,
                        label: {
                            normal: {
                                show: true,
                                position: 'top'
                            }
                        },
                        data: series
                    }
                ]
            };
            lengthChart.setOption(option);
            lengthChart.hideLoading();
        });
    }
</script>
</body>
</html>

