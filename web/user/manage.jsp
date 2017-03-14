
<%--
  Created by IntelliJ IDEA.
  User: jecyhw
  Date: 2015/6/10
  Time: 23:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="utf-8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <link href="../js/bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">

    <title>用户角色管理</title>
    <script src="../js/jquery.js"></script>
    <script src="../js/bootstrap/js/bootstrap.js"></script>
    <!--[if lt IE 9]><script src="js/html5.js"></script><![endif]-->
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container-fluid">
    <div class="panel panel-default">
        <jsp:include page="page.jsp"/>
    </div>
</div>
</body>
<script>
    var $edit_span;
    function load(url) {
        $.get(url, function(data) {
            $(".panel").html(data);
            $edit_span = $("#edit-role").find(".badge");//每次加载后要重新赋值
            $edit_span.data("uid", "");
        });
    }

    function edit(it, role) {
        if (!$(it).parent().hasClass("disabled")) {
            $.get(
                    "editRole.jsp?role=" + role + "&uid=" + $edit_span.data("uid"),
                    function (data) {
                        if (data.result == "true") {
                            var $all = $("#filter-role").parent().find("li[data-role=all]");
                            var uid = $edit_span.data("uid").split(",");
                            if ($all.length == 0) {
                                for (var i = uid.length - 1; i >= 0; i--) {
                                    var id = uid[i];
                                    if (id.length > 0) {
                                        $("#" + id).parents("tr:first").fadeOut("slow", function () {
                                            $(this).remove();
                                        });
                                    }
                                }
                            } else {
                                var roleText = $(it).html();
                                for (var i = uid.length - 1; i >= 0; i--) {
                                    var id = uid[i];
                                    if (id.length > 0) {
                                        $("#" + id).attr("data-role", role).parents("tr:first").addClass("success").fadeOut("slow", function () {
                                            $it = $(this);
                                            $it.find("td[data-role]").html(roleText);
                                            $it.fadeIn("slow", function () {
                                                $it.removeClass("success");
                                            });
                                        });
                                    }
                                }
                            }
                            $("#role-reset").click();
                        } else {
                            $("#role-edit-msg").slideUp("slow", function () {
                                $(this).slideDown("slow");
                            });
                        }
                    },
                    "json"
            );
        }
    }

    $(document).ready(function () {
        $edit_span = $("#edit-role").find(".badge");
        $edit_span.data("uid", "");

        $(".panel")
                .on("click", "table input[type=checkbox]", function() {
                    var $span = $edit_span,
                            checked = parseInt($span.html()),
                            uid = $span.data("uid"),
                            id = $(this).attr("id");
                    if ($(this).is(":checked")) {
                        $span.html(checked + 1);
                        if (uid) {
                            id = "," + id;
                        }
                        $span.data("uid", uid + id);
                    } else {
                        $span.html(checked - 1);
                        $span.data("uid", uid.replace(new RegExp("," + id + "|^" + id + ",?"), ""));
                    }
                })
                .on("click", "#role-all", function () {
                    $("table").find("input[type=checkbox]").each(function () {
                        var $it = $(this);
                        if (!$it.is(":checked")) {
                            $it.click();
                        }
                    }) ;
                })
                .on("click", "#role-reset", function () {
                    $("table").find("input[type=checkbox]").each(function () {
                        var $it = $(this);
                        if ($it.is(":checked")) {
                            $it.click();
                        }
                    }) ;
                })
                .on("click", "#role-unselect", function () {
                    $("table").find("input[type=checkbox]").each(function () {
                        $(this).click();
                    }) ;
                })
                .on("click", "#edit-role", function () {
                    var uid = $edit_span.data("uid").split(","), role = undefined, i, len = 0;
                    for (i = uid.length - 1; i >= 0; i--) {
                        var id = uid[i];
                        if (id.length > 0) {
                            len++;
                            if (role == undefined) {
                                role = $("#" + id).attr("data-role");
                            }
                            else if (role != $("#" + id).attr("data-role")) {
                                break;
                            }
                        }
                    }
                    var $pa = $(this).parent();
                    if (len == 0) {
                        $pa.find("li").addClass("disabled");
                    } else {
                        $pa.find("li.disabled").removeClass("disabled");
                        if (role != undefined && i < 0) {
                            $pa.find("li[data-role=" + role + "]").addClass("disabled");
                        }
                    }
                })
                .find("table input[type=checkbox]").attr("checked", false);
    });
</script>
</html>