<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>登陆</title>
<meta name="robots" content="index,follow" />
<meta name="renderer" content="webkit">
<meta http-equiv="Cache-Control" content="no-siteapp,no-transform" />
<meta name ="viewport" content ="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no,email=no" />
<meta name="msapplication-tap-highlight" content="no">
<jsp:include page="./common/head.jsp"></jsp:include>
<link rel="stylesheet" href="/resources/3rd/login/css/reset.css" type="text/css">
<link rel="stylesheet" href="/resources/3rd/login/css/common.css" type="text/css">
<style type="text/css">
.display-none{display:none;}
.error{color:red;}
#login-tips{
	position: relative;
	top: -19px;
	height: 1px;
	text-align: center;
	}
</style>
<!--[if lt IE 9]>
<script type="text/javascript" src="/resources/3rd/login/js/html5.js"></script>
<script type="text/javascript" src="/resources/3rd/login/js/respond.min.js"></script>
<![endif]-->
<script type="text/javascript" src="/resources/3rd/login/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/resources/js/xiaoniu/login.js"></script>

</head>
<body class="login_bg">
	<header>
    	<h3>包多多内容管理系统</h3>
    </header>
    <div class="content">
    	<div class="tm_bd"><div class="tm_bg"></div> </div>
        <div class="con_bg">
    	<h3>管理员登录</h3>
    	<div class=" error tips display-none" id="login-tips"></div>
        <div class="content_main fr">
			<s:url value="/j_spring_security_check" var="login" />
			<form action="${login}" method="post" id="loginForm">
				<p class="user">
					<span>用户名：</span><input type="text" name="j_username" class="put_one">
				</p>
				<p class="password">
					<span>密   码：</span><input type="password" name="j_password" class="put_two">
					<input type='hidden' name='_csrf' value='${_csrf.token}'/>
				</p>
				<p class="yzm"><span>验证码：</span><input type="text" name="j_validate_code" maxlength="4"><em><img src="<c:url value='/public/validateImage'/>" id="validateImg" width="85px" height="30p"></em></p>
				<p class="btn">
					<input type="button" class="login_btn" value="" onclick="loginSubmit()">
					<input type="button" class="reset_btn" onclick="$('#loginForm').find('input').val('')">
				</p>
			</form>
        </div>
        </div>
    </div>
    <footer>
    	<p>聆创科技</p>
    </footer>
</body>
</html>