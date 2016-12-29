$(function() {
        	
        	$('#loginOut').click(function() {
                $.messager.confirm('系统提示', '您确定要退出本次登录吗?', function(r) {
                    if (r) {
                    	location.href = '/public/logout.html';
                    }
                });
            });
        	
        	$("#changePass").click(function() {
        		$('#htm_pass .opt_btn').show();
        		$('#htm_pass .loading').hide();
            	$('#htm_pass').window('open');
            });
        	
        	$('#htm_pass').window({
    			title: '修改密码',
    			modal : true,
    			width : 500,
    			height : 160,
    			shadow : false,
    			closed : true,
    			minimizable : false,
    			maximizable : false,
    			collapsible : false,
    			iconCls : 'icon-edit',
    			resizable : false,
    			onClose : function() {
    				$("#password_pass").val('');
    				$("#confirm_pass").val('');
    			}
    		});
        	
        	init();//该js方法在/resources/js/frameworkTheme/theme.js
        	$('body').fadeIn();
        });
        
        function editpwdSubmit(){
        	var password = $("#password_pass").val();
        	var repeatpwd = $("#confirm_pass").val();
        	var $form = $("#pass_form");
        	if(password != repeatpwd){
        		$.messager.alert('错误提示',"两次密码输入不同"); 
        		return;
        	}
        	$.post($form.attr("action"),$form.serialize(),
					function(result){
    					$('#htm_pass .opt_btn').show();
    	        		$('#htm_pass .loading').hide();
						if(result['resultCode'] == 0) {
							$('#htm_pass').window('close');  //关闭添加窗口
							$.messager.alert('提示',result['msg']);  //提示添加信息成功
							location.href = '/public/logout.html';
						} else {
							$.messager.alert('错误提示',result['msg']);  //提示添加信息失败
						}
					},"json");
        }