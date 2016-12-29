$(function(){
	$('.content_main input').focus(function(){
		$(this).css('border-color','#5bacf9');
		})
	$('.content_main input').blur(function(){
		$(this).css('border-color','#e7e6e6');
		})
	$('.put_one').focus(function(){
		$(this).parent().addClass('user_color');
		$("#login-tips").addClass("display-none");
		})
	$('.put_one').blur(function(){
		$(this).parent().removeClass('user_color');
		})
	$('.put_two').focus(function(){
		$(this).parent().addClass('password_color');
		$("#login-tips").addClass("display-none");
		})
	$('.put_two').blur(function(){
		$(this).parent().removeClass('password_color');
	});
	$("#validateImg").click(function(){
			validateImg='/public/validateImage';
			$("#validateImg").attr("src",validateImg+"?"+Math.random());
		});
	$("input[name=j_validate_code]").blur(function(){
		$("#login-tips").addClass("display-none");
	});
	$("input[name=j_validate_code]").focus(function(){
		$("#login-tips").addClass("display-none");
	});
});
function loginSubmit(){
		var username = $("input[name=j_username]").val();
		if($.trim(username) == ""){
			$("#login-tips").html("账户不能为空");
			$("#login-tips").removeClass("display-none");
			return ;
		}
		var pwd = $("input[name=j_password]").val();
		if($.trim(pwd) == ""){
			$("#login-tips").html("密码不能为空");
			$("#login-tips").removeClass("display-none");
			return ;
		}
		$.post("/j_spring_security_check",$("#loginForm").serialize(),function(result){
			if(result.msg){
				if(result.msg == "Bad credentials"){
					$("#login-tips").html("账户或密码错误");
				}else{
					$("#login-tips").html(result.msg);
				}
				$("#login-tips").removeClass("display-none");
				$("#validateImg").click();
			}else{
				window.location.href = "/secure/index";
			}
		});
	}