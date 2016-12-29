package com.baoduoduo.admin.utils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信公众号相关工具类
 *
 * @author luoxl
 */
public class WechatUtil {

	private static final Log logger = LogFactory.getLog(WechatUtil.class);
	private static JSONObject token = null;

	/** 获得jsapi_ticket */
	public static final String JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}&type=jsapi";

	/** 获取token */
	private final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

	/* 字段名 */
	public final static String ACCESS_TOKEN_NAME = "access_token";
	public final static String EXPIRES_IN_NAME = "expires_in";
	public final static String UPDATE_TIME_NAME = "updateTime";

	/**
	 * 获取全局access_token
	 * 
	 */
	public static String getAccessToken() {

		String accessToken = null;

		if (token == null) {
			token = new JSONObject();
			// 初始化刷新
			flushAccessToken();
		} else {
			long updateTime = token.getLongValue(UPDATE_TIME_NAME);
			long nowTime = new Date().getTime();
			int expires_in = token.getIntValue(EXPIRES_IN_NAME);
			// 超时刷新
			if ((nowTime - updateTime) / 1000 > expires_in) {
				flushAccessToken();
			}
		}
		accessToken = token.getString(ACCESS_TOKEN_NAME);

		return accessToken;
	}

	/**
	 * 刷新全局access_token
	 * 
	 */
	private static void flushAccessToken() {
		String url = MessageFormat.format(ACCESS_TOKEN_URL, WeiChatConfig.getMpAppId(), WeiChatConfig.getMpAppSecret());
		String resp = HttpUtil.httpsGetRequest(url);

		JSONObject node = JSONObject.parseObject(resp);
		String accessToken = node.getString(ACCESS_TOKEN_NAME);
		if (!"".equals(accessToken)) {
			int expires_in = node.getIntValue(EXPIRES_IN_NAME);
			token.put(UPDATE_TIME_NAME, new Date().getTime());
			token.put(ACCESS_TOKEN_NAME, accessToken);
			token.put(EXPIRES_IN_NAME, expires_in);
		}
	}

	/**
	 * 获取随机字符串
	 * 
	 * @return
	 */
	public static String getNonceStr() {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

		int len = str.length();
		Random rd = new Random();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 32; i++) {
			int index = rd.nextInt(len);
			sb.append(str.charAt(index));
		}
		return sb.toString();
	}

	/**
	 * 获取时间戳
	 * 
	 * @return
	 */
	public static String getTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	/**
	 * 使用微信sdk需要进行签名
	 */
	public static Map<String, String> signJsSdk(HttpServletRequest request, String url) {
		String jsapi_ticket = getJsapiTicket(request);
		Map<String, String> signInfo = WechatSign.sign(jsapi_ticket, url);
		return signInfo;
	}

	/**
	 * 获取jsapi_ticket
	 * 
	 * @return
	 */
	public static String getJsapiTicket(HttpServletRequest request) {
		// 在全局进行查询，如果没有，进行获取和设置
		String jsapi_ticket = (String) request.getSession().getServletContext().getAttribute("jsapi_ticket");
		long currentTime = System.currentTimeMillis();// 当前毫秒
		Long lastTime = (Long) request.getSession().getServletContext().getAttribute("lastTime");
		long lastTimeLong = 0; // 上次保存时间
		if (StringUtils.isEmpty(lastTime)) {
			lastTimeLong = currentTime;
			request.getSession().getServletContext().setAttribute("lastTime", lastTimeLong);
		} else {
			lastTimeLong = Long.valueOf(lastTime);
		}
		// 7180秒，就需要重新生成jsapi_ticket
		long betweenTime = currentTime - lastTimeLong;// 间隔时间
		if (StringUtils.isEmpty(jsapi_ticket) || betweenTime > 7180 * 1000) {
			request.getSession().getServletContext().setAttribute("lastTime", currentTime);
			String access_token = getAccessToken();
			try {
				String jsApiurl = MessageFormat.format(JSAPI_TICKET, access_token);
				String jsApiResponse = HttpUtil.httpsGetRequest(jsApiurl);
				if (jsApiResponse != null) {
					JSONObject result = JSONObject.parseObject(jsApiResponse);
					if (result.containsKey("ticket")) {
						jsapi_ticket = result.getString("ticket");
						request.getSession().getServletContext().setAttribute("jsapi_ticket", jsapi_ticket);
					} else {
						logger.error("获取ticket失败：" + result + " Url: " + jsApiurl);
					}
				}
			} catch (Exception e) {
				logger.error("获取jsApiurl错误--->" + e.getMessage());
			}
		}
		return jsapi_ticket;
	}

}
