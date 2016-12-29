/**
 * @author zxx
 * @time 2016年7月31日上午11:05:34
 */
package com.baoduoduo.admin.controller.authentication;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.baoduoduo.admin.utils.Contants;
import com.baoduoduo.admin.utils.MsgCode;
import com.google.code.kaptcha.Producer;

/**
 * @author zxx
 * @time 2016年7月31日
 *
 */
@Controller
@RequestMapping("/")
public class AuthenticationController {
	
	@Autowired
	private Producer captchaProducer;
	
	 @RequestMapping({"/"})
	  public ModelAndView index(HttpServletRequest req)
	  {
		 ModelAndView mav = new ModelAndView();
	    String requestedWith = req.getHeader("X-Requested-With");
	    boolean isAjax = (requestedWith != null) ? "XMLHttpRequest".equals(requestedWith) : false;
	    if (isAjax)
	      mav.setViewName("public/ajaxSessionTimeout");
	    else
	      mav.setViewName("public/login");

	    return mav;
	  }

	  @RequestMapping({"secure/index"})
	  public ModelAndView index1()
	  {
		  ModelAndView mav = new ModelAndView("secure/index");
		  return mav;
	  }

	  @RequestMapping({"public/loginFailed"})
	  @ResponseBody
	  public Map<String, Object> loginFailed(HttpServletRequest request) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	      AuthenticationException exception = (AuthenticationException)session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
	      map.put(Contants.MSG, exception.getMessage());
	    } else {
	      map.put(Contants.MSG, "账号或密码错误");
	    }
	    return map;
	  }

	  @RequestMapping({"public/loginSuccess"})
	  @ResponseBody
	  public Map<String, Object> loginSuccess(HttpServletRequest request) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put(Contants.RESULT_CODE, MsgCode.SUCCESS.getCode());
	    map.put(Contants.MSG, "登录成功");
	    return map;
	  }

	  @RequestMapping({"public/login.html"})
	  public ModelAndView publicIndex(HttpServletRequest req)
	  {
	    ModelAndView mav = new ModelAndView();
	    String requestedWith = req.getHeader("X-Requested-With");
	    boolean isAjax = (requestedWith != null) ? "XMLHttpRequest".equals(requestedWith) : false;
	    if (isAjax)
	      mav.setViewName("public/ajaxSessionTimeout");
	    else
	      mav.setViewName("public/login");

	    return mav;
	  }

	  @RequestMapping({"public/logout.html"})
	  public void logout(HttpServletRequest request, HttpServletResponse response)
	  {
	    try
	    {
	      request.getSession().invalidate();
	      if (request.getCookies() == null) return;
	      Cookie cookie = request.getCookies()[0];
	      cookie.setMaxAge(0);
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

	  @RequestMapping({"public/validateImage"})
	  public String image(HttpServletRequest request, HttpServletResponse response)
	    throws IOException
	  {
	    response.setDateHeader("Expires", -3226862938698547200L);
	    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
	    response.addHeader("Cache-Control", "post-check=0, pre-check=0");
	    response.setHeader("Pragma", "no-cache");
	    response.setContentType("image/jpeg");
	    String capText = this.captchaProducer.createText();
	    request.getSession().setAttribute("KAPTCHA_SESSION_KEY", capText);
	    BufferedImage bi = this.captchaProducer.createImage(capText);
	    ServletOutputStream out = response.getOutputStream();
	    ImageIO.write(bi, "jpg", out);
	    try {
	      out.flush();
	    } finally {
	      out.close();
	    }
	    return null;
	  }

	  @RequestMapping({"public/accessdenied.html"})
	  public ModelAndView accessdenied()
	  {
	    ModelAndView mnv = new ModelAndView();
	    mnv.setViewName("public/error/304");
	    return mnv;
	  }

	  @RequestMapping({"public/404.html"})
	  public ModelAndView error403()
	  {
	    ModelAndView mnv = new ModelAndView();
	    mnv.setViewName("public/error/404");
	    return mnv;
	  }
}
