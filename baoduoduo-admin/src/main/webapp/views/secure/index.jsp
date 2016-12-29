<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="_csrf" content="${_csrf.token}"/>  
	<meta name="_csrf_header" content="${_csrf.headerName}"/> 
    <title>包多多后台管理系统</title>
    <jsp:include page="../public/common/head.jsp"></jsp:include>
    <script type="text/javascript" src="<c:url value='/resources/js/xiaoniu/theme.js'/>?r=1134"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/xiaoniu/secure_index.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/xiaoniu/default.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/xiaoniu/CRUD.css'/>"/>
    <script type="text/javascript" src="/resources/js/xiaoniu/index.js"></script>
    <style type="text/css">
    	
	    body {
			overflow-y: hidden;
			margin:0; 
			scroll=no;
	    }
    	#header-inner {
			text-align: left;
			width: 100%;
			padding: 10px 0;
			font-size:14px !important;
		}
		
		#topmenu {
			text-align: right;
		}
		
		#topmenu a {
			display: inline-block;
			padding: 1px 3px;
			text-decoration: none;
			color: #fff;
			font-size:12px;
		}
    	
    	#footer {
    		height: 30px;
    		background: #D2E0F2;
    	 	display: none;
    	}
    	#indexTab {
    		display: none;
    	}
    	#indexTab ul li{
    		margin-top:10px;
    		font-size: 14px;
    		line-height: 20px;
    	}
    	#loading {
    		position:absolute;
			width:300px;
			height:50px;
			top:50%;
			left:50%;
			margin:-25px 0 0 -150px;
			text-align:center;
			z-index:99999;
			font-size: 16px;
			color: #000;
    	}
    </style>
</head>
<body class="easyui-layout">
    <div region="north" border="false" style="background-color: rgb(102, 102, 102); color:white; text-align: center; width: 100%;  background-position: initial initial; background-repeat: initial initial;" title="" class="panel-body panel-body-noheader panel-body-noborder layout-body ">
       <div id="header-inner">
				<table cellpadding="0" cellspacing="0" style="width:100%;">
					<tbody><tr>
						<td rowspan="2" style="width:20px;">
						</td>
						<td style="height:52px;">
							<div style="color:#fff;font-size:22px;font-weight:bold;">
								<a href="#" style="color:#fff;font-size:22px;font-weight:bold;text-decoration:none">管理系统</a>
							</div>
							<div style="color:#fff">
								<a href="#" style="color:#fff;text-decoration:none">Protect your dream.</a>
							</div>
						</td>
						<td style="padding-right:5px;text-align:right;vertical-align:bottom;">
							<div id="topmenu">
								<a href="javascript:void(0);"><sec:authentication property="principal.adminUserInfo.userName" /></a>
								<a href="javascript:void(0);" id="changePass">修改密码</a>
								<a href="javascript:void(0);" id="loginOut">安全退出</a>
							</div>
						</td>
					</tr>
				</tbody></table>
			</div>
    </div>
    <div region="west" hide="true" split="true" title="导航菜单" style="width:180px;" id="west">
	<div id="nav" class="easyui-accordion" fit="true" border="false">
	</div>
    </div>
    <div id="mainPanle" region="center" style="background: #eee; overflow-y:hidden;">
        <div id="tabs" class="easyui-tabs"  fit="true" border="false" >
		</div>
    </div>
    
	<div id="mm" class="easyui-menu" style="width:150px; display:none;">
		<div id="mm-tabupdate">刷新</div>
		<div class="menu-sep"></div>
		<div id="mm-tabclose">关闭</div>
		<div id="mm-tabcloseall">全部关闭</div>
		<div id="mm-tabcloseother">除此之外全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-tabcloseright">当前页右侧全部关闭</div>
		<div id="mm-tabcloseleft">当前页左侧全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-exit">退出</div>
	</div>

 	<div id="loading">
 		加载中...
 	</div>
 	
 	<!-- 更新密码窗口 -->
	<div id="htm_pass">
		<form id="pass_form" action="/secure/adminUserInfo/updateAdminSelfInfo" method="post">
			<table class="htm_edit_table"  width="480" class="none">
				<tbody>
					<tr>
						<td class="leftTd">新密码：</td>
						<td><input type="password" name="password" id="password_pass" style="width:205px;" onchange="validateSubmitOnce=true;"/></td>
						<td class="rightTd"><div id="password_passTip" class="tipDIV"></div></td>
					</tr>
					<tr>
						<td class="leftTd">确认密码：</td>
						<td><input type="password" name="confirm" id="confirm_pass" style="width:205px;" onchange="validateSubmitOnce=true;"/></td>
						<td class="rightTd"><div id="confirm_passTip" class="tipDIV"></div></td>
					</tr>
					<tr class="opt_btn">
						<td colspan="3" style="text-align: center;padding-top: 10px;">
							<a class="easyui-linkbutton" iconCls="icon-ok" onclick="editpwdSubmit()">确定</a> 
							<a class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#htm_pass').window('close');">取消</a>
						</td>
					</tr>
					<tr class="loading none">
						<td colspan="3" style="text-align: center; padding-top: 10px; vertical-align:middle;">
							<img alt="" src="<c:url value='/resources/images/loading.gif'/>" style="vertical-align:middle;">
							<span style="vertical-align:middle;">请稍后...</span>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

</body>
</html>