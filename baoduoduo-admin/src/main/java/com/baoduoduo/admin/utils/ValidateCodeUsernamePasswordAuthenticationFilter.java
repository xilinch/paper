package com.baoduoduo.admin.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.google.code.kaptcha.Constants;

/**
 * 整合登陆验证码的认证
 * @author zxx
 *
 */
public class ValidateCodeUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	@Value("${isValidCode}")
	private Integer isValidCode;
	
	private Logger log = Logger.getLogger(getClass());
	private boolean postOnly = true;
	/**
	 * 表单提交时验证码的参数名
	 */
	public static final String VALIDATE_CODE_PARAMETER = "j_validate_code";
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		if ( username == null){
			username = "";
		}
		
		if ( password == null){
			password = "";
		}
		
		if (postOnly && !request.getMethod().equals("POST") ) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		
		username = username.trim();
		request.getSession().setAttribute("userNameInput", username);
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		
		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		
		//验证码校验
		if(isValidCode == 1){
			checkValidateCode(request);
		}
		Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);
		return authentication;
		
	}
	
	
	/**
	 * 比较session中的验证码和用户输入的验证码是否相等
	 * @param request
	 */
	private void checkValidateCode(HttpServletRequest request) {
		String sessionValidateCode = obtainSessionValidateCode(request);
		String validateCodeParameter = obtainValidateCodeParameter(request);
		if (!sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
			if(log.isDebugEnabled()){
				log.debug("===========the validate code is :" + sessionValidateCode +" ,but input is: " + validateCodeParameter);
			}
			throw new AuthenticationServiceException("验证码错误");
		}
	}
	
	private String obtainValidateCodeParameter(HttpServletRequest request) {
		Object obj = request.getParameter(VALIDATE_CODE_PARAMETER);
		return null == obj ? "" : obj.toString();
	}

	private String obtainSessionValidateCode(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
		return null == obj ? "" : obj.toString();
	}
	
	public boolean isPostOnly() {
		return postOnly;
	}

	@Override
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}
}
