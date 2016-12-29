<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理员信息</title>
<jsp:include page="../public/common/head.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/xiaoniu/CRUD.css'/>?v=1"/>
<script type="text/javascript" src="<c:url value='/resources/js/xiaoniu/dateTool.js'/>?r=1134"></script>
<script type="text/javascript" src="<c:url value='/resources/js/xiaoniu/common.js'/>?r=4"></script>
<script type="text/javascript">
	commonTable.loadDateURI = "/secure/adminUserInfo/queryAdminUserInfoVOList";
	commonTable.batchUpdateValidURI = "/secure/adminUserInfo/batchUpdateValid?strIds=";
	commonTable.batchDeleteURI = "/secure/adminUserInfo/batchDelete?strIds=";
	commonTable.updateURI = "/secure/adminUserInfo/updateAdminUserInfo";
	commonTable.insertURI = "/secure/adminUserInfo/insertAdminUserInfo";
	commonTable.title = "管理员信息列表";
	commonTable.columns = [
		{field:'ck',checkbox:true},
		{field:'id', title: '用户ID',align:'center',  },
		{field:'userName',title: '用户名',align:'center',width:120},
		{field:'loginCode',title: '登录账号',align:'center',width:140},
		{field:'roleDesc',title: '角色',align:'center'},
		validColumn,
		createTimeColumn,
		updateTimeColumn,
		{field:'operator',title: '操作',align:'center',
			formatter: function(value,row,index){
					return "<a href='#' onclick='javascript:commonTable.initUpdateWindow("+index+")'>修改</a>";
				}
		},
	];
	commonTable.beforeInitUpdateWindow = function(index){
		var rows = $("#html_table").datagrid("getRows"),
		row = rows[index];
		$("#edit_form_userName").val(row.userName);
		$("#edit_form_loginCode").val(row.loginCode);
		$("#edit_form_id").val(row.id);
		$("#edit_form_password").val('');
		$('#edit_form_valid').combobox("setValue",row.valid);
		$('#edit_form_role').combobox("setValue",row.roleId);
	};
	commonTable.beforeSave = function(){
		var userName = $("#edit_form_userName").val();
		var loginCode 	= $("#edit_form_loginCode").val();
		var password	= $("#edit_form_password").val();
		if(!userName || $.trim(userName) == ""){
			$.messager.alert('提示',"用户名不能为空");
			return false;
		}
		if(!loginCode || $.trim(loginCode) == ""){
			$.messager.alert('提示',"登录账号不能为空");
			return false;
		}
		if(!password || $.trim(password) == ""){
			$.messager.alert('提示',"登录账号不能为空");
			return false;
		}
		return true;
	};
	
	$(function(){
		showPageLoading();
		commonTable.defineAddWindow(460, 290);
		commonTable.init();
		removePageLoading();
	});
	
</script>
</head>
<body>
	<div id="html_table">
	
	</div>
	
	<!-- tool bar -->
	<div id="table_tb" style="padding:5px;height:auto" class="none">
		<a href="javascript:void(0);" onclick="javascript:commonTable.initAddWindow()"class="easyui-linkbutton" title="添加" plain="true" iconCls="icon-add" id="addBtn">添加</a>
		<a href="javascript:void(0);" onclick="javascript:commonTable.batchDelete()"class="easyui-linkbutton" title="删除" plain="true" iconCls="icon-cut" id="delBtn">删除</a>
	</div>
	
	<!-- 添加-->
	<div id="htm_edit">
		<form id="edit_form" method="post">
			<table id="htm_edit_table" width="450">
				<tbody>
					<tr>
						<td>用户名:</td>
						<td><input id="edit_form_userName" name="userName" class="clear-input"/></td>
					</tr>
					<tr>
						<td>登陆账号:</td>
						<td><input id="edit_form_loginCode" name="loginCode" class="clear-input"/></td>
					</tr>
					<tr>
						<td>密码:</td>
						<td><input id="edit_form_password" name="password" class="clear-input"/></td>
					</tr>
					<tr>
						<td>角色:</td>
						<td>
							<select id="edit_form_role" name="roleId" class="easyui-combobox clear-combobox">
								<option value="1">超级管理员</option>
								<option value="2">管理员</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>有效性:</td>
						<td>
							<select id="edit_form_valid" name="valid" class="easyui-combobox clear-combobox">
								<option value="1">有效</option>
								<option value="0">无效</option>
							</select>
						</td>
					</tr>
					<tr>
						<td><input style="display:none" name="id" readonly="readonly" id="edit_form_id"></td>
					</tr>
					<tr>
						<td class="opt_btn" colspan="2" style="text-align: center;padding-top: 10px;">
							<a class="easyui-linkbutton" id="edit_form_submit_btn" iconCls="icon-ok" onclick="javascript:commonTable.save();">确定</a> 
							<a class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#htm_edit').window('close');">取消</a>
						</td>
					</tr>
					<tr class="loading none">
						<td colspan="2" style="text-align: center; padding-top: 10px; vertical-align:middle;">
							<img alt="" src="/resources/images/loading.gif" style="vertical-align:middle;">
							<span style="vertical-align:middle;">请稍后...</span>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>