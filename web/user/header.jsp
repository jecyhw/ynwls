<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8"%>
<link href="../css/ie8.css" rel="stylesheet" media="screen">
<div class="navbar navbar-default">
    <div class="container navbar-container">
        <div class="navbar-header">
            <a class="navbar-brand"><strong>Web端轨迹管理系统</strong></a>
        </div>

        <form class="navbar-form navbar-right" role="search">
            <a type="button" class="btn btn-primary" href="trackRecord.jsp">返回</a>
            <button type="button" class="btn btn-primary disabled" ><%=session.getAttribute("userName")%></button>
        </form>
    </div>
</div>
