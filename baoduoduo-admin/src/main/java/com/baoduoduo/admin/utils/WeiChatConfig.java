package com.baoduoduo.admin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.util.ResourceUtils;
//classpath:configs/weichat/redpack/apiclient_cert_open.p12
public final class WeiChatConfig {
	private static final Logger log = Logger.getLogger(WeiChatConfig.class);
	
	static Properties p = null;
	
	static{
		InputStream in = null;
		try {
			in = ResourceUtils.getURL("classpath:configs/weichat.properties").openStream();
			p = new Properties();
			p.load(in);
			appId = p.getProperty("WX.APP_ID");
			appSecret = p.getProperty("WX.APP_SECRET");
			chId = p.getProperty("WX.CH_ID");
			mpAppId = p.getProperty("WX.MPAPP_ID");
			mpAppSecret = p.getProperty("WX.MPAPP_SECRET");
			
		}  catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
		
	}
	
	public static String getProperties(String key) {
		return p.getProperty(key);
	}
	

	private static String appId;
	
	private static String appSecret;
	
	
	private static String chId;
	
	private static String mpAppId;
	
	private static String mpAppSecret;
	

	public static String getAppId() {
		return appId;
	}


	public static String getAppSecret() {
		return appSecret;
	}


	public static String getChId() {
		return chId;
	}


	public static String getMpAppId() {
		return mpAppId;
	}



	public static String getMpAppSecret() {
		return mpAppSecret;
	}

}
