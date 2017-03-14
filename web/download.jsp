<%--
  Created by IntelliJ IDEA.
  User: SNNU
  Date: 2015/3/17
  Time: 14:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%
    String contextPath = request.getContextPath();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <title>文件下载</title>
    <link rel="stylesheet" type="text/css" href="js/bootstrap/css/bootstrap.css" media="screen"/>
</head>
<body>
<div id="downloadTip" style="margin: 50px auto; width: 800px;">
    <div class="alert alert-success text-center" role="alert">
        <strong>正在努力合并要下载的文件，请耐心等待</strong>
        <img src="js/fancybox/fancybox_loading.gif"/>
    </div>
</div>
<iframe name="fileMerge" style="display: none" id="fileMerge"></iframe>
<form name="frm" id="frm" target="fileMerge" method="post" action="<%=contextPath%>/ExportRecord.do">
    <input type="hidden" name="ids" id="ids">
</form>
<form name="frmFile" id="frmFile" method="post" action="<%=contextPath%>/DownloadFile.do">
    <input type="hidden" name="file" id="file">
    <input type="hidden" name="isDeleted" id="isDeleted">
</form>
</body>

<script>
    window.onload = function() {
        var ids = document.getElementById("ids");
        ids.value = '<%=request.getParameter("ids")%>';
        if (ids.value == 'null') {
            window.close();
        }
        document.getElementById("frm").submit();
    }

    function callback(downloadFileName, isDeleted) {//iframe回调函数
        var msg;
        if (downloadFileName != "") {
            msg = "合并完成,正在下载";
            document.getElementById("downloadTip").innerHTML =  '<div class="alert alert-success text-center" role="alert"><strong>' + msg + '</strong></div>';
            document.getElementById("file").value = downloadFileName;
            if (isDeleted == "1") {
                document.getElementById("isDeleted").value = isDeleted;
            }
            document.getElementById("frmFile").submit();
        } else {
            if (isDeleted == "1") {
                msg = "合并文件出错,无法进行下载";
            } else {
                msg = "下载文件时出错";
            }
            document.getElementById("downloadTip").innerHTML = '<div class="alert alert-danger text-center" role="alert"><strong>' + msg + '</strong></div>';
        }
    }
</script>
</html>
