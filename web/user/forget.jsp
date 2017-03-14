<%--
  Created by IntelliJ IDEA.
  User: jecyhw
  Date: 2015/6/17
  Time: 17:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <title>注册</title>
    <link rel="stylesheet" type="text/css" href="../js/jquery-ui/jquery-ui.css" />
    <link href="../js/bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
    <script type="text/javascript" src="../js/jquery.js"></script>
    <script src="../js/bootstrap/js/tooltip.js"></script>
    <style>
        #wizard {
            border: 1px solid #dddddd;
            margin: 20px auto;
            border-radius: 5px;
        }

        #wizard #status {
            height: 35px;
            list-style: none outside none;
            background: none repeat scroll 0% 0% #123 ;
        }

        #wizard #status li {
            float: left;
            color: #fff;
            padding: 10px 20px;
        }

        #wizard #status li.active {
            background-color: #336699;
            font-weight: normal;
        }

        .page {
            margin: 20px 35px;
            width: 500px;
            display: none;
            clear: both;
        }

        .page.in {
            display: block;
        }

        .page h3 {
            height: 42px;
            font-size: 16px;
            border-bottom: 1px dotted #cccccc;
            margin-bottom:20px;
            padding-bottom: 5px;
            font-weight: bold;
        }

        .page h3 em {
            font-size: 12px;
            font-weight: 500;
            font-style: normal;
        }

        .page.page-sm {
            width: 300px;
            margin: 0px auto;
        }
    </style>
</head>
<body>
<link href="../css/ie8.css" rel="stylesheet" media="screen">
<div class="navbar navbar-default">
  <div class="container navbar-container">
    <div class="navbar-header">
      <a class="navbar-brand"><strong>Web端轨迹管理系统</strong></a>
    </div>

    <form class="navbar-form navbar-right" role="search">
      <a type="button" class="btn btn-primary" href="../index.html">返回到登陆</a>
    </form>
  </div>
</div>
<div>
    <form class="container" style="width: 600px;">
        <div class="" id="wizard">
            <ul id="status">
                <li class=""><strong>1.</strong>输入用户名和邮箱地址</li>
                <li class=""><strong>2.</strong>邮箱验证</li>
                <li class="active"><strong>3.</strong>重置密码</li>
            </ul>

            <div class="items">
                <div class="page page-sm">
                    <div class="form-group">
                        <label for="account">用户名</label><span  class="text-danger">(必填项)</span>
                        <input type="text" class="form-control" name="account" id="account" data-required data-msg="用户名不能为空">
                    </div>
                    <div class="form-group">
                        <label for="email">邮箱</label><span  class="text-danger">(必填项)</span>
                        <input type="text" class="form-control" name="email" id="email" data-required="^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$" data-msg="邮箱格式不正确">
                    </div>
                    <div class="form-group">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox">由于您在注册时未填写邮箱,是否将本次输入邮箱绑定到注册邮箱
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <button type="button" class="btn btn-primary btn-block">找回密码</button>
                    </div>
                </div>
                <div class="page">
                    <div class="form-group text-center">
                        密码重置邮件已发送至你的邮箱：<span class="text-primary">1147352923@qq.com</span>
                    </div>
                    <div class="form-group text-center">
                        请在24小时内登录你的邮箱接收邮件，链接激活后可重置密码。
                    </div>
                    <div class="form-group text-center">
                        <a href="http://mail.qq.com" class="btn btn-primary">登陆qq邮箱查看</a>
                    </div>
                </div>
                <div class="page page-sm in">
                    <div class="form-group">
                        用户名:<span class="text-primary"><%="jecyhw"%></span>
                    </div>
                    <div class="form-group">
                        邮箱:<span class="text-primary"><%="1147352923@qq.com"%></span>
                    </div>
                    <div class="form-group">
                        <label for="password">新密码</label><span  class="text-danger">(必填项)</span>
                        <input type="password" class="form-control" name="password" id="password" data-required data-msg="密码不能为空">
                    </div>
                    <div class="form-group">
                        <label for="checkPassword">确认密码</label><span  class="text-danger">(必填项)</span>
                        <input type="password" class="form-control" id="checkPassword" data-required data-msg="确认密码不能为空">
                    </div>
                    <div class="form-group">
                        <button type="button" class="btn btn-primary btn-block">确定</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<script>
    $(document).ready(function () {
        var $li = $("#wizard #status li"),
                li_index = 0,
                $page = $(".items .page"),
                userExist = -1;

        function checkUser(val, callback) {
            $.ajax({
                type: "post",
                url: web_prefix + "/CheckUserExist",
                dataType: "json",
                data: {
                    "account": val
                },
                success: function (data) {
                    callback(data);
                }
            });
        }

        var $account =  $("#account");
        $account.blur(function () {//验证用户名是否存在
            userExist = -1;
            var val = $account.val();
            if (val) {
                checkUser(val, function(data) {
                    if (data.status != 0) {
                        tooltipShow($account, 2000, "该用户名已存在,请重新填写用户名");
                        userExist = 1;
                    } else {
                        userExist = 0;
                    }
                });
            }
        });

        $("button").click(function() {
            var $this = $(this),
                    dir = $this.attr("data-direction");
            if (dir) {
                var pass = true;
                if (dir == "1") {//跳转至下一步骤，需要先进行验证
                    $page.eq(li_index).find("input[data-required]").each(function () {//验证填写是否符合要求
                        var $this = $(this),
                                s = $this.attr("data-required"),
                                val = $this.val();
                        if (!s) {
                            if (!val && val.length == 0) {
                                pass = false;
                            }
                        } else {
                            var regex = new RegExp(s);
                            if (!regex.test(val)) {
                                pass = false;
                            }
                        }
                        if (!pass) {
                            tooltipShow($this, 2000, $this.attr("data-msg"));
                            return false;
                        }
                    });

                    if (pass) {//继续验证
                        if (li_index == 0) {
                            var $check = $("#checkPassword");//检查密码是否一致
                            if (userExist == 1) {
                                tooltipShow($account, 2000, "该用户名已存在,请重新填写用户名");
                                pass = false;
                            }
                            if (pass) {
                                if ($("#password").val() != $check.val()) {
                                    tooltipShow($check, 2000, "密码不一致,请重新确认密码");
                                    pass = false;
                                }
                            }

                            if (pass) {//继续验证用户名
                                pass = false;//先设置pass等于false
                                if (userExist == -1) {//验证用户名是否存在(第一次验证)
                                    $this.addClass("disabled");//按钮失效
                                    checkUser($account.val(), function (data) {
                                        if (data.status != 0) {
                                            tooltipShow($account, 2000, "该用户名已存在,请重新填写用户名");
                                            userExist = 1;
                                        } else {
                                            userExist = 0;
                                            $li.eq(li_index).removeClass("active");
                                            $page.eq(li_index).removeClass("in");
                                            li_index += parseInt(dir);
                                            $li.eq(li_index).addClass("active");
                                            $page.eq(li_index).addClass("in");
                                        }
                                        $this.removeClass("disabled");
                                    });
                                } else if (userExist == 1) {//用户名已验证,且用户已存在
                                    tooltipShow($account, 2000, "该用户名已存在,请重新填写用户名");
                                } else {
                                    pass = true;
                                }
                            }
                        }
                    }
                }

                if (!pass) {//验证不通过
                    return;
                }
                $li.eq(li_index).removeClass("active");
                $page.eq(li_index).removeClass("in");
                li_index += parseInt(dir);
                $li.eq(li_index).addClass("active");
                $page.eq(li_index).addClass("in");
            } else {//完成注册
                var index = li_index;
                $.ajax({
                    type: "post",
                    dataType: "json",
                    url: web_prefix + "/Register",
                    data: $("form").serializeArray(),
                    success: function (data) {
                        if (data.status == 0) {
                            setTimeout(function () {
                                window.location.href = "../index.html";
                            }, 5000);
                            $page.eq(index).find(".alert-success").removeClass("hide");
                            $page.eq(index).find("button").addClass("disabled");
                        } else {
                            $page.eq(index).find(".alert-danger").removeClass("hide");
                            setTimeout(function () {
                                $page.eq(index).find(".alert-danger").addClass("hide");
                            }, 5000);
                        }
                    },
                    error: function () {
                        $page.eq(index).find(".alert-danger").removeClass("hide");
                        setTimeout(function () {
                            $page.eq(index).find(".alert-danger").addClass("hide");
                        }, 5000);
                    }
                })
            }
        }).keyup(function() {
            if (e.keyCode == 13) {
                $(this).click();
            }
        });

    });
</script>
</body>
</html>
