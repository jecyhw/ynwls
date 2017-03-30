<%@ page import="com.cn.util.DBUtil" %>
<%@ page import="com.cn.util.TableName" %>
<%@ page import="com.cn.dao.AEntityDao" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.ArrayList" %>
<%--
  Created by IntelliJ IDEA.
  User: jecyhw
  Date: 2014/10/12
  Time: 12:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <title>轨迹管理</title>
    <link rel="stylesheet" type="text/css" href="../css/imgareaselect-default.css">
    <link rel="stylesheet" type="text/css" href="../js/jquery-ui/jquery-ui.css" />
    <link rel="stylesheet" type="text/css" href="../css/jquery.plupload.ui.css"/>
    <link rel="stylesheet" type="text/css" href="../js/bootstrap/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="../js/fancybox/jquery.fancybox.css"  media="screen">
    <link rel="stylesheet" type="text/css" href="../js/fancybox/helpers/jquery.fancybox-buttons.css"  media="screen">

    <script type="text/javascript" src="../js/jquery.js"></script>
    <script type="text/javascript" src="../js/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="../js/jquery-ui/jquery-ui.js"></script>
    <script type="text/javascript" src="../js/jquery-ui/i18n/jquery.ui.datepicker-zh-CN.js"></script>
    <script type="text/javascript" src="../js/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="../js/plupload/jquery.plupload.ui.js"></script>
    <script type="text/javascript" src="../js/plupload/zh_CN.js"></script>
    <script type="text/javascript" src="../js/jquery.imgareaselect.js"></script>
    <script type="text/javascript" src="../js/jquery.json.js"></script>
    <script type="text/javascript" src="../js/fancybox/jquery.mousewheel-3.0.6.pack.js"></script>
    <script type="text/javascript" src="../js/fancybox/jquery.fancybox.js"></script>
    <script type="text/javascript" src="../js/fancybox/helpers/jquery.fancybox-buttons.js"></script>
    <script charset="utf-8" src="http://map.qq.com/api/js?v=2.exp&libraries=convertor&key=IMZBZ-S7VRW-NXERI-RLSJY-HHHCT-MBFI4"></script>
    <script type="text/javascript" src="../js/coordtransform.js"></script>
    <script type="text/javascript" src="../js/all.js"></script>
    <script type="text/javascript" src="../js/gps_bd.js"></script>
    <script type="text/javascript" src="../js/jmap.js"></script>
    <script type="text/javascript" src="../js/trackRecord.js"></script>
    <script type="text/javascript" src="../js/fullscreen.js"></script>

    <style>
        html, body {
            width: 100%;
            height: 100%;
            margin: 0px;
            padding: 0px;
            font-size: 12px;
            overflow-y: hidden;
        }

        .form-control-hack, .form-group-hack input[type="text"] {
            display: inline ;
            width: 130px ;
        }

        .left {
            position: absolute;
            bottom: 0;
            top: 0;
            left: 0;
            width: 430px;
            padding: 5px;
            border-right: 1px solid #dddddd;
            overflow-y: auto;
        }

        .fullscreen {
            position: fixed;
            bottom: 0;
            top: 0;
            left: 0;
            right: 0;
        }

        .top {
            overflow: hidden;
            position: absolute;
            top: 0;
            left: 430px;
            height: 40px;
            right: 0;
            padding: 5px;
        }

        .map {
            overflow: hidden;
            position: absolute;
            top: 40px;
            left: 430px;
            bottom: 0;
            right: 0;
            border-top: 1px solid #dddddd;
        }

        /* upload file css start*/
        .file-upload-list {
            display: none;
            position: absolute;
            right: 20px;
            bottom: 0;
            z-index: 100;
            width: 600px;
        }

        .btn-hack {
            background: inherit!important;
        }

        .upload_maximum, .upload_close, .upload_minimize {
            width: 15px;
            height: 15px;
            display: inline-block;
        }

        .upload_maximum {
            background: url(../image/sprite_n1_ccdf642.png) 0px -192px no-repeat
            !important;
        }

        .upload_close {
            background: url(../image/sprite_n1_ccdf642.png) 0px -177px no-repeat
            !important;
        }

        .upload_minimize {
            background: url(../image/sprite_n1_ccdf642.png) 0px -162px no-repeat
            !important;
        }
        /* upload file css end*/
        .fancybox-inner table td img:hover {
            cursor: pointer;
        }

        .ui-datepicker select.ui-datepicker-month, .ui-datepicker select.ui-datepicker-year {
            color: black;
        }
        hr {
            margin-top: 5px;
            margin-bottom: 5px;
        }
    </style>
</head>
<body>
<div>
    <div class="left">
        <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingOne">
                    <h4 class="panel-title">
                        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                            搜索
                        </a>
                    </h4>
                </div>
                <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
                    <div class="panel-body">
                        <div>
                            <label class="control-label" for="address">地点:</label>
                            <input	class="form-control form-control-hack input-sm" type="text" name="address"
                                      placeholder="相关地址"  id="address"/>
                            <label class="control-label" for="recorder">记录人:</label>
                            <input class="form-control form-control-hack input-sm" type="text"
                                   name="recorder" placeholder="相关记录人"  id="recorder"/>
                        </div>
                        <hr/>
                        <div>
                            <label class="control-label" for="startTime">时间段:</label>
                            <label class="control-label" for="startTime">从</label>
                            <input class="form-control form-control-hack input-sm" type="text"
                                   name="startTime" placeholder="开始时间"  id="startTime"/>
                            <label class="control-label" for="endTime">到</label>
                            <input class="form-control form-control-hack input-sm" type="text" name="endTime"
                                   placeholder="结束时间"  id="endTime"/>
                        </div>
                        <hr/>

                        <div>
                            <div>
                                <label class="control-label" for="left">左上角:经度</label>
                                <input class="form-control form-control-hack input-sm" disabled type="text" name="left"  id="left"/>
                                <label class="control-label" for="top">纬度</label>
                                <input class="form-control form-control-hack input-sm" disabled type="text" name="top"  id="top"/>
                            </div>
                            <p></p>
                            <div>
                                <label class="control-label" for="right">右下角:经度</label>
                                <input class="form-control form-control-hack input-sm" disabled type="text" name="right"  id="right" />
                                <label class="control-label" for="bottom">纬度</label>
                                <input class="form-control form-control-hack input-sm" disabled type="text" name="bottom"  id="bottom"/>
                            </div>
                            <p></p>
                            <div>
                                    <button class="btn btn-primary btn-sm btn-block" type="button" name="region_capture"
                                            id="region_capture">
                                        <span>在地图上选择区域</span>
                                        <span class="glyphicon glyphicon-screenshot"></span>
                                    </button>
                            </div>
                        </div>
                        <hr/>
                        <div>
                            <button class="btn btn-success btn-sm btn-block" type="button" name="search_submit" id="search_submit">
                                <span>搜索</span>
                                <span class="glyphicon glyphicon-search"></span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        <div class="panel panel-default">
            <table class="table table-bordered table-hover table-condensed table-striped table-responsive">
                <thead>
                <tr id="track-length" class="hidden">
                    <th colspan="4">轨迹总长度:<span>0</span>米</th>
                </tr>
                <tr>
                    <th>显示</th>
                    <th>名称</th>
                    <th>起止时间</th>
                    <th>文件大小</th>
                </tr>
                </thead>
                <tbody id="query-result">
                </tbody>
            </table>
        </div>
        <div>
            <h5 style="float: left">共<span id="total">1</span>条</h5>
            <nav style="float: right">
                <ul class="pagination" style="margin: 0;">

                </ul>
            </nav>
        </div>
         </div>
    <div class="top">
        <div style="float: left">
            <button type="button" class="btn btn-success btn-sm"
                    id="file_upload">
                <span>文件上传</span>
                <span class="glyphicon glyphicon-upload"></span>
            </button>
            <a class="btn btn-sm btn-success" href="showRealTime.jsp">
                用户实时位置
            </a>
            <a class="btn btn-success btn-sm" href="statistics.jsp">
                轨迹统计
            </a>
        </div>
        <div style="float: right">
            <% try { Integer role = (Integer)session.getAttribute("role"); if (role >= 0 && role < 2) {
                Object count = DBUtil.query(
                        new AEntityDao() {
                            @Override
                            public Object getEntity(ResultSet set) throws SQLException {
                                return set.getInt(1);
                            }
                        },
                        new StringBuilder()
                                .append("select count(*) from ")
                                .append(TableName.getUser())
                                .append(" where role not between 0 and 3").toString(),
                        new ArrayList());
            %>
            <a class="btn btn-primary btn-sm" href="manage.jsp" title="有<%=count%>个用户未分配角色">
                用户角色管理 <span class="badge"><%=count%></span>
            </a>
            <%}
            } catch (Exception e) {}%>

            <button type="button" class="btn btn-info btn-sm disabled"
                    id="account"><%=session.getAttribute("userName")%></button>
            <button type="button" class="btn btn-primary btn-sm" id="exit">
                <span>退出</span>
                <span class="glyphicon glyphicon-off"></span>
            </button>
        </div>
        <%--<div>--%>
            <%--<div class="btn-group">--%>
                <%--<% try { Integer role = (Integer)session.getAttribute("role"); if (role >= 0 && role < 2) {--%>
                <%--%>--%>
                <%--<button type="button" class="btn btn-primary btn-sm"--%>
                        <%--id="save_track_record">--%>
                    <%--<span>保存轨迹</span>--%>
                    <%--<span class="glyphicon glyphicon-import"></span>--%>
                <%--</button>--%>
                <%--<%}} catch (Exception e) {}%>--%>

                <%--<button type="button" class="btn btn-primary btn-sm"--%>
                        <%--id="export_track_record">--%>
                    <%--<span>导出轨迹</span>--%>
                    <%--<span class="glyphicon glyphicon-export"></span>--%>
                <%--</button>--%>
            <%--</div>--%>
        <%--</div>--%>
    </div>
    <div id="map" class="map"></div>
</div>
<div class="file-upload-list" id="file-upload-list">
    <div class="ui-widget-header text-right">
        <div class="btn-group btn-group-xs btn-hack">
            <button type="button" class="btn btn-hack" id="upload_minimize"
                    title="最小化">
                <span class="upload_minimize"></span>
            </button>
            <button type="button" class="btn btn-hack disabled"
                    id="upload_maximum" title="最大化">
                <span class="upload_maximum"></span>
            </button>
            <button type="button" class="btn btn-hack" id="upload_close"
                    title="关闭">
                <span class="upload_close"></span>
            </button>
        </div>
    </div>
    <div id="file-list">
        <div id="file-upload-error" class="alert alert-danger">
            <a href="#" class="close" data-dismiss="alert">&times;</a>
            <strong>错误！
            </strong>
            <p>您的浏览器没有Flash, Silverlight or HTML5 支持,无法文件上传。</p>
            <p>请检查你的浏览器是否安装Flash并且浏览器没有禁用Flash,建议使用最新版本的浏览器。</p>
        </div>
    </div>
</div>
</body>
</html>
