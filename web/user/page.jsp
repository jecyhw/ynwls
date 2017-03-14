<%@ page import="com.cn.dao.CheckUserDao" %>
<%@ page import="com.cn.util.TableName" %>
<%@ page import="com.cn.util.DBUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cn.bean.SjyfiUserEntity" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.cn.dao.AEntityDao" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<div class="panel-heading">用户角色分配</div>
<table class="table table-bordered table-hover table-condensed table-striped" style="font-size: 12px;">
    <thead>
    <tr>
        <th></th>
        <th>用户账号</th>
        <th>姓名</th>
        <th>生日</th>
        <th>性别</th>
        <th>工作单位</th>
        <th>省,市,县</th>
        <th>乡镇</th>
        <th>注册时间</th>
        <th>角色</th>
    </tr>
    </thead>
    <tbody>
    <%
        int pageNum = 10;
        int curPage;
        String temp = null;
        try {
            curPage = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            curPage = 1;
        }
        int role;
        try {
            temp = request.getParameter("role");
            role = Integer.parseInt(temp);
        } catch (Exception e) {
            if (temp == null) {//无role参数表示未分配角色
                role = -1;
            } else  {//role参数为非数字字符时为所有角色
                role = -2;
            }

        }
        CheckUserDao userDao = new CheckUserDao();
        StringBuilder sb = new StringBuilder();
        String condition = "";
        switch (role) {
            case -1:
                condition = " where role not between 0 and 3 ";
                break;
            case 0:
                condition = " where role = 0 ";
                break;
            case 1:
                condition = " where role = 1 ";
                break;
            case 2:
                condition = " where role = 2 ";
                break;
            case 3:
                condition = " where role = 3 ";
                break;
            default:
        }
        sb.append("select * from ")
                .append(TableName.getUser())
                .append(condition)
                .append(" limit ")
                .append((curPage - 1) * 10)
                .append(",")
                .append(pageNum);

        List result = DBUtil.queryMulti(userDao, sb.toString(), new ArrayList());
        for (Object user : result) {
            SjyfiUserEntity userEntity = (SjyfiUserEntity) user;
    %>
    <tr>
        <td><input type="checkbox" id="<%=userEntity.getUid()%>" data-role="<%=userEntity.getRole()%>"></td>
        <td><%=userEntity.getAccount()%>
        </td>
        <td><%=userEntity.getName()%>
        </td>
        <td><%=new SimpleDateFormat("yyyy-MM-dd").format(userEntity.getBirthday())%>
        </td>
        <td><% Byte gender = userEntity.getGender();
            if (gender.equals(2)) {
                out.print("女");
            } else if (gender.equals(1)) {
                out.print("男");
            } else {
                out.print("保密");
            }
        %></td>
        <td><%=userEntity.getOrganization()%>
        </td>
        <td><%=new StringBuilder().append(userEntity.getProvince())
                .append(", ")
                .append(userEntity.getCity())
                .append(", ")
                .append(userEntity.getCounty() == null ? "" : userEntity.getCounty()).toString()%>
        </td>
        <td><%=userEntity.getTownship() == null ? "" : userEntity.getTownship()%>
        </td>
        <td><%=new SimpleDateFormat("yyyy-MM-dd").format(userEntity.getAdd_time())%>
        </td>
        <td data-role="<%=userEntity.getRole()%>"><%
            switch (userEntity.getRole()) {
                case 0:
                    out.print("管理员");
                    break;
                case 1:
                    out.print("超级用户");
                    break;
                case 2:
                    out.print("动物调查用户");
                    break;
                case 3:
                    out.print("植物调查用户");
                    break;
                default:
                    out.print("未分配角色");
                    break;
            }
        %></td>
    </tr>
    <%
        }
        if (result.size() == 0) {
    %>
    <tr>
        <td class="text-center" colspan="10">该角色的用户记录0条</td>
    </tr>
    <%}%>
    </tbody>
</table>
<div class="panel-body">
    <div class="row">
        <div class="col-sm-4 col-xs-4">
        <%
            sb.delete(0, sb.length());
            sb.append("select count(*) from ")
                    .append(TableName.getUser())
                    .append(condition);
            Integer count = (Integer) DBUtil.query(
                    new AEntityDao() {
                        @Override
                        public Object getEntity(ResultSet set) throws SQLException {
                            return set.getInt(1);
                        }
                    },
                    sb.toString(),
                    new ArrayList());
            int num = count / pageNum;//计算总页数
            if (count % pageNum > 0) {
                num++;
            }
            if (num > 1) {//总页数至少要为2
        %>
        <ul class="pagination" style="margin: 0px;">
            <% if (curPage > 1) { %>
            <li>
                <a href="javascript:load('page.jsp?role=<%=role%>&page=<%=(curPage-1)%>')" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <%}
            int start, end;
                if (num <= 5) {
                    start = 1;
                    end = num;
                } else if (curPage <= 3) {
                   start = 1;
                    end = 5;
                } else if (num - curPage < 3) {
                    start = num - 4;
                    end = num;
                } else {
                    start = curPage - 2;
                    end = curPage + 2;
                }
                for (int i = start; i <= end; i++) {
                    if (curPage == i) {
                        %> <li class="active"><a href="javascript:void(0)"><%=i%></a></li>
                <%
                    } else {
            %>
            <li><a href="javascript:load('page.jsp?role=<%=role%>&page=<%=i%>')"><%=i%></a></li>

            <%}}if (curPage < num) {%>
            <li>
                <a href="javascript:load('page.jsp?role=<%=role%>&page=<%=(curPage+1)%>')" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
            <%}%>
        </ul>
        <%}%>
    </div>
    <div class="col-sm-4 col-xs-4">
        <div class="dropdown">
            <button class="btn btn-primary btn-sm dropdown-toggle" type="button" id="filter-role" data-toggle="dropdown"
                    aria-expanded="true">
                用户角色刷选
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu" role="menu" aria-labelledby="filter-role">
                <%
                    if (role == 0) {
                        out.print("<li class=\"disabled\" role=\"presentation\"><a role=\"menuitem\" tabindex=\"-1\" href=\"javascript:void(0)\">管理员</a></li>");
                    } else {
                        out.print("<li role=\"presentation\"><a role=\"menuitem\" tabindex=\"-1\" href=\"javascript:load('page.jsp?role=0')\">管理员</a></li>");
                    }

                    if (role == 1) {
                        out.print("<li role=\"presentation\" class=\"disabled\"><a role=\"menuitem\" tabindex=\"-1\" href=\"javascript:void(0)\">超级用户</a></li>");
                    } else {
                        out.print("<li role=\"presentation\"><a role=\"menuitem\" tabindex=\"-1\" href=\"javascript:load('page.jsp?role=1')\">超级用户</a></li>");
                    }

                    if (role == 2) {
                        out.print("<li role=\"presentation\" class=\"disabled\"><a role=\"menuitem\" tabindex=\"-1\" href=\"javascript:void(0)\">动物调查用户</a></li>");
                    } else {
                        out.print("<li role=\"presentation\"><a role=\"menuitem\" tabindex=\"-1\" href=\"javascript:load('page.jsp?role=2')\">动物调查用户</a></li>");
                    }

                    if (role == 3) {
                        out.print("<li role=\"presentation\" class=\"disabled\"><a role=\"menuitem\" tabindex=\"-1\" href=\"javascript:void(0)\">植物调查用户</a></li>");
                    } else {
                        out.print("<li role=\"presentation\"><a role=\"menuitem\" tabindex=\"-1\" href=\"javascript:load('page.jsp?role=3')\">植物调查用户</a></li>");
                    }

                    if (role == -1) {
                        out.print("<li role=\"presentation\" class=\"disabled\"><a role=\"menuitem\" tabindex=\"-1\" href=\"javascript:void(0)\">未分配角色</a></li>");
                    } else {
                        out.print("<li role=\"presentation\"><a role=\"menuitem\" tabindex=\"-1\" href=\"javascript:load('page.jsp?role=-1')\">未分配角色</a></li>");
                    }

                    if (role == -2) {
                        out.print("<li role=\"presentation\" class=\"disabled\" data-role=\"all\"><a role=\"menuitem\" tabindex=\"-1\" href=\"javascript:void(0)\">所有用户</a></li>");
                    } else {
                        out.print("<li role=\"presentation\"><a role=\"menuitem\" tabindex=\"-1\" href=\"javascript:load('page.jsp?role=all')\">所有用户</a></li>");
                    }
                %>
            </ul>
        </div>
    </div>
    <div class="col-sm-4 col-xs-4">
        <div class="dropdown">
            <button class="btn btn-primary btn-sm dropdown-toggle" type="button" id="edit-role" data-toggle="dropdown" aria-expanded="true">
                角色修改 <span class="badge">0</span>
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu" role="menu" aria-labelledby="edit-role">
                <li role="presentation" data-role="0"><a role="menuitem" tabindex="-1" onclick="edit(this,0)" href="javascript:void(0)">管理员</a></li>
                <li role="presentation" data-role="1"><a role="menuitem" tabindex="-1" onclick="edit(this,1)" href="javascript:void(0)">超级用户</a></li>
                <li role="presentation" data-role="2"><a role="menuitem" tabindex="-1" onclick="edit(this,2)" href="javascript:void(0)">动物调查用户</a></li>
                <li role="presentation" data-role="3"><a role="menuitem" tabindex="-1" onclick="edit(this,3)" href="javascript:void(0)">植物调查用户</a></li>
            </ul>
            <button id="role-all" class="btn btn-info btn-sm">全选</button>
            <button id="role-unselect" class="btn btn-info btn-sm">反选</button>
            <button id="role-reset" class="btn btn-info btn-sm">重置</button>
        </div>
    </div>
</div>
</div>
<div class="panel-body" id="role-edit-msg" style="display: none;">
    <div class="alert alert-danger text-center"><strong>操作失败,请稍后重试</strong></div>
</div>