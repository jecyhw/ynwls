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
    <link rel="stylesheet" type="text/css" href="css/imgareaselect-default.css">
    <link rel="stylesheet" type="text/css" href="js/jquery-ui/jquery-ui.css" />
    <link rel="stylesheet" type="text/css" href="css/jquery.plupload.ui.css"/>
    <link rel="stylesheet" type="text/css" href="js/bootstrap/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="js/fancybox/jquery.fancybox.css"  media="screen">
    <link rel="stylesheet" type="text/css" href="js/fancybox/helpers/jquery.fancybox-buttons.css"  media="screen">

    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript" src="js/bootstrap/js/tooltip.js"></script>
    <script type="text/javascript" src="js/jquery-ui/jquery-ui.js"></script>
    <script type="text/javascript" src="js/jquery-ui/i18n/jquery.ui.datepicker-zh-CN.js"></script>
    <script type="text/javascript" src="js/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="js/plupload/jquery.plupload.ui.js"></script>
    <script type="text/javascript" src="js/plupload/zh_CN.js"></script>
    <script type="text/javascript" src="js/jquery.imgareaselect.js"></script>
    <script type="text/javascript" src="js/jquery.json.js"></script>
    <script type="text/javascript" src="js/fancybox/jquery.mousewheel-3.0.6.pack.js"></script>
    <script type="text/javascript" src="js/fancybox/jquery.fancybox.js"></script>
    <script type="text/javascript" src="js/fancybox/helpers/jquery.fancybox-buttons.js"></script>

    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=QT9ntk6IBtEHGSy4BG7zOXoU"></script>
    <script type="text/javascript" src="js/all.js"></script>
    <script type="text/javascript" src="js/gps_bd.js"></script>
    <script type="text/javascript" src="js/jmap.js"></script>
    <script type="text/javascript" src="js/trackRecord.js"></script>

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
            width: 120px ;
        }

        .input-xss {
            display: inline ;
            width: 90px;
        }
        .stabs-hack {
            padding-left: 0px;
            padding-right: 0px;
        }

        .top-left {
            width: 800px;
            text-align: center;
            position: absolute;
            top: 0px;
            left: 0px;
        }

        .top-right {
            position: absolute;
            left: 800px;
            right: 0px;
            top: 0px;
            text-align: center;
            min-width: 300px;
        }
        #show_rtup {
            display: block;
            position: absolute;
            width: 100%;
            height: 100%;
        }
        #show_rtup img {
            width: 100%;
            height: 100%;
        }

        .top-right-topright {
            position: absolute;
            right: 0px;
            top: 0px;
        }

        .top-right-bottomright {
            position: absolute;
            right: 0px;
            bottom: 0px;
        }

        .bottom {
            position: absolute;
            bottom: 0px;
            top: 163px;
            width: 100%;
        }

        .bottom-left {
            float: left;
            position: relative;
            width: 403px;
            height: 100%;
            overflow-y: auto;
        }

        #map {
            overflow: hidden;
            position: relative;
            height: 100%;
            border-top: 1px solid #dddddd;
            border-left: 1px solid #dddddd;
        }

        /* upload file css start*/
        .file-upload-list {
            display: none;
            position: absolute;
            right: 20px;
            bottom: 0px;
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
            background: url(image/sprite_n1_ccdf642.png) 0px -192px no-repeat
            !important;
        }

        .upload_close {
            background: url(image/sprite_n1_ccdf642.png) 0px -177px no-repeat
            !important;
        }

        .upload_minimize {
            background: url(image/sprite_n1_ccdf642.png) 0px -162px no-repeat
            !important;
        }
        /* upload file css end*/
        .table-overlay {
            position: absolute;
            top: 0px;
            left: 0px;
            right: 0px;
            bottom: 0px;
            z-index: 100;
            display: none;
        }
        .table-overlay {
            position: fixed;
            width: 403px;
        }

        .sjyfi-loading {
            position: absolute;
            top: 50%;
            left: 50%;
            margin-top: -22px;
            margin-left: -22px;
            width: 44px;
            height: 44px;
            background: url('js/fancybox/fancybox_loading.gif') center center no-repeat;
        }
        .fancybox-inner table td img:hover {
            cursor: pointer;
        }
    </style>
</head>
<body>
<div>
    <div id="stabs" class="top-left">
        <ul>
            <li><a href="#stabs-0">记录人检索</a></li>
            <li><a href="#stabs-1">地点检索</a></li>
            <li><a href="#stabs-2">时间段检索</a></li>
            <li><a href="#stabs-3">区域检索</a></li>
            <li><a href="#stabs-4">综合检索</a></li>
        </ul>
        <div id="stabs-0" class="stabs-hack">
            <label class="sr-only control-label" for="recorder0">相关记录人</label>
            <input class="form-control input-sm form-control-hack" type="text" name="recorder"
                   placeholder="相关记录人" id="recorder0"/>
            <button class="btn btn-primary btn-sm" type="button" name="search_submit" id="search_submit0" data-loading-text="正在查询...">
                <span>搜索</span>
                <span class="glyphicon glyphicon-search"></span>
            </button>
        </div>
        <div id="stabs-1" class="stabs-hack">
            <label class="sr-only control-label" for="address1">相关地址</label>
            <input class="form-control input-sm form-control-hack" type="text" name="address"
                   placeholder="相关地址" id="address1"/>
            <button class="btn btn-primary btn-sm" type="button" name="search_submit" id="search_submit1">
                <span>搜索</span>
                <span class="glyphicon glyphicon-search"></span>
            </button>
        </div>
        <div id="stabs-2" class="stabs-hack">
            <label class="control-label" for="startTime2">从</label>
            <input class="form-control input-sm input-xss" type="text" name="startTime" placeholder="开始时间" id="startTime2"/>
            <label class="control-label" for="endTime2">到</label>
            <input class="form-control input-sm input-xss" type="text" name="endTime" placeholder="结束时间" id="endTime2"/>
            <button class="btn btn-primary btn-sm" type="button" name="search_submit" id="search_submit2">
                <span>搜索</span>
                <span class="glyphicon glyphicon-search"></span>
            </button>
        </div>
        <div id="stabs-3" class="stabs-hack">
            <table style="margin: 0px auto; width: 600px">
                <tr>
                    <td>
                        <label class="control-label" for="left3">左上角:经度</label>
                        <input class="form-control input-sm input-xss" type="text" name="left" id="left3"/>
                        <label class="control-label" for="top3">纬度</label>
                        <input class="form-control input-sm input-xss" type="text" name="top" id="top3"/>
                    </td>
                    <td rowspan="2">
                        <button class="btn btn-primary btn-sm" type="button" name="region_capture"
                                id="region_capture3">
                            <span>选择区域</span>
                            <span class="glyphicon glyphicon-screenshot"></span>
                        </button>
                        <button class="btn btn-primary btn-sm" type="button" name="search_submit" id="search_submit3">
                            <span>搜索</span>
                            <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </td>
                </tr>
                <tr>
                    <td><label class="control-label" for="right3">右下角:经度</label>
                        <input class="form-control input-sm input-xss" type="text" name="right" id="right3"/>
                        <label class="control-label" for="bottom3">纬度</label>
                        <input class="form-control input-sm input-xss" type="text" name="bottom"  id="bottom3"/>
                    </td>
                    <td></td>
                </tr>
            </table>
        </div>
        <div id="stabs-4" class="stabs-hack">
            <table style="margin: 0px auto; width: 796px">
                <tr>
                    <td style="width: 350px">
                        <label class="control-label" for="startTime4">时间段:</label>
                        <label class="control-label" for="startTime4">从</label>
                        <input class="form-control input-sm input-xss" type="text"
                               name="startTime" placeholder="开始时间"  id="startTime4"/>
                        <label class="control-label" for="endTime4">到</label>
                        <input class="form-control input-sm input-xss" type="text" name="endTime"
                               placeholder="结束时间"  id="endTime4"/>
                    </td>
                    <td  style="width: 76px">
                        <label class="control-label" for="left4">区域坐标:</label>
                    </td>
                    <td  style="width: 370px">
                        <label class="control-label" for="left4">左上角:经度</label>
                        <input class="form-control input-sm input-xss" type="text" name="left"  id="left4"/>
                        <label class="control-label" for="top4">纬度</label>
                        <input class="form-control input-sm input-xss" type="text" name="top"  id="top4"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="control-label" for="address4">地点:</label>
                        <input	class="form-control input-sm form-control-hack" type="text" name="address"
                                  placeholder="相关地址"  id="address4"/>
                        <label class="control-label" for="recorder4">记录人:</label>
                        <input class="form-control input-sm form-control-hack" type="text"
                               name="recorder" placeholder="相关记录人"  id="recorder4"/></td>
                    <td></td>
                    <td>
                        <label class="control-label" for="right4">右下角:经度</label>
                        <input class="form-control input-sm input-xss" type="text"
                               name="right"  id="right4" />
                        <label class="control-label" for="bottom4">纬度</label>
                        <input class="form-control input-sm input-xss" type="text" name="bottom"  id="bottom4"/>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <button class="btn btn-primary btn-sm" type="button" name="search_submit" id="search_submit4">
                            <span>搜索</span>
                            <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </td>
                    <td>
                        <button class="btn btn-primary btn-sm" type="button" name="region_capture"
                                id="region_capture4">
                            <span>选择区域</span>
                            <span class="glyphicon glyphicon-screenshot"></span>
                        </button>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="top-right">
        <a id="show_rtup" href="showRTUserPosition.html" target="_blank">
            <div class="tooltip right" role="tooltip">
                <div class="tooltip-arrow"></div>
                <div class="tooltip-inner">
                    点击查看用户实时位置
                </div>
            </div>
        </a>
        <div class="top-right-topright">
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
            <a class="btn btn-primary btn-sm" href="user/manage.jsp" title="有<%=count%>个用户未分配角色">
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
        <div class="top-right-bottomright">
            <div class="btn-group">
                <button type="button" class="btn btn-primary btn-sm"
                        id="file_upload">
                    <span>文件上传</span>
                    <span class="glyphicon glyphicon-upload"></span>
                </button>
                <% try { Integer role = (Integer)session.getAttribute("role"); if (role >= 0 && role < 2) {
                %>
                <button type="button" class="btn btn-primary btn-sm"
                        id="save_track_record">
                    <span>保存轨迹</span>
                    <span class="glyphicon glyphicon-import"></span>
                </button>
                <%}} catch (Exception e) {}%>

                <button type="button" class="btn btn-primary btn-sm"
                        id="export_track_record">
                    <span>导出轨迹</span>
                    <span class="glyphicon glyphicon-export"></span>
                </button>
            </div>
        </div>
    </div>
    <div class="bottom">
        <div class="bottom-left">
            <table class="table table-bordered table-hover table-condensed table-striped">
                <thead>
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
            <div class="table-overlay"><div class="sjyfi-loading"></div></div>
        </div>
        <div id="map"></div>
    </div>

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
<%--<div style="display: none !important;" id="preload"></div>--%>
<%--<div id="ui-dialog"></div>--%>
</body>
</html>