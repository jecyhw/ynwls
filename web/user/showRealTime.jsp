<%--
  Created by IntelliJ IDEA.
  User: jecyhw
  Date: 2015/6/17
  Time: 16:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <title>用户实时位置显示</title>

    <link rel="stylesheet" type="text/css" href="../js/bootstrap/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="../js/fancybox/jquery.fancybox.css"  media="screen">
    <style>
        html, body {
            width: 100%;
            height: 100%;
            padding: 0;
            margin: 0;
            position: relative;
        }

        .content {
            position: absolute;
            width: 100%;
            top: 60px;
            bottom: 0;
        }

        .left {
            padding-top: 10px;
            float: left;
            width: 350px;
            position: relative;
            height: 100%;
            overflow-y: auto;
            overflow-x: hidden;
        }

        #map {
            height: 100%;

        }
        .pr0 {
            padding-top: 0px;
            padding-bottom: 0px;
        }
    </style>
    <script type="text/javascript" src="../js/jquery.js"></script>
    <script type="text/javascript" src="../js/color.js"></script>
    <script type="text/javascript" src="../js/bootstrap/js/tooltip.js"></script>
    <script charset="utf-8" src="http://map.qq.com/api/js?v=2.exp&libraries=convertor&key=IMZBZ-S7VRW-NXERI-RLSJY-HHHCT-MBFI4"></script>
    <script type="text/javascript" src="../js/fancybox/jquery.fancybox.js"></script>
    <script type="text/javascript" src="../js/all.js"></script>
    <script type="text/javascript" src="../js/gps_bd.js"></script>
    <script type="text/javascript" src="../js/rtol.js"></script>
    <script type="text/javascript" src="../js/lp.js"></script>
</head>
<body>
<div class="header">
    <jsp:include page="header.jsp"/>
</div>
<div class="content">
    <div class="left container">
        <div class="">
            <label class="btn-xs btn-primary" id="online-number">当前在线人数
                <span class="badge online">0</span>
            </label>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading"><h3 class="panel-title">查找用户当前地理位置信息</h3></div>
            <div class="panel-body">
                <div class="form-group">
                    <label for="upi">用户名</label>
                    <input type="text" class="form-control" id="upi" >

                </div>
                <div>
                    <button type="submit" class="btn btn-primary clear" data-for="upi">清除</button>
                    <button type="submit" class="btn btn-primary search" data-for="upi">查找</button>
                </div>

            </div>
        </div>


        <div class="panel panel-default">
            <div class="panel-heading"><h3 class="panel-title">查找用户历史地理位置信息</h3></div>
            <div class="panel-body">
                <div class="form-group">
                    <label for="uhi">用户名</label>
                    <input type="text" class="form-control" id="uhi">
                </div>
                <div>
                    <button type="submit" class="btn btn-primary clear" data-for="uhi">清除</button>
                    <button type="submit" class="btn btn-primary search" data-for="uhi">查找</button>
                </div>
            </div>
        </div>
    </div>
    <div id="map">
    </div>
</div>
</body>
</html>
