<%@ page import="com.cn.util.DBUtil" %>
<%@ page import="com.cn.util.TableName" %>
<%@ page import="java.util.*" %>
<%@ page import="com.cn.util.Out" %>
<%--
  Created by IntelliJ IDEA.
  User: jecyhw
  Date: 2015/6/13
  Time: 12:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String role = request.getParameter("role");
    String uid[] = request.getParameter("uid").split(",");
    List values = new ArrayList();
    values.add(role);
    values.addAll(Arrays.asList(uid));

    StringBuilder sb = new StringBuilder();
    sb.append("update ")
            .append(TableName.getUser())
            .append(" set role = ? where uid in(");
    if (uid.length > 0) {
        sb.append("?");
    }

    for (int i = uid.length - 1; i > 0; i--) {
        sb.append(",").append("?");
    }
    sb.append(")");
    int count = DBUtil.update(sb.toString(), values);
    Map<String, String> result = new Hashtable<String, String>();
    if (count > 0) {
        result.put("result", "true");
    } else {
        result.put("result", "false");
    }
    Out outResult = new Out(response);
    outResult.printJson(result);
    outResult.close();
%>