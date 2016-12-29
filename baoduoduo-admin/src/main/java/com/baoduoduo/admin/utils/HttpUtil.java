package com.baoduoduo.admin.utils;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.net.ssl.SSLContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;



/**
 * http请求类  可以用于https请求
 * @author Administrator
 *
 */

public final class HttpUtil {

	// 禁止实例化和继承
	private HttpUtil() {}

	private static final Log logger = LogFactory.getLog(HttpUtil.class);

	public static String httpPostRequest(String url, String params) {
		return httpRequest(false, "POST", params, url);
	}

	public static String httpGetRequest(String url) {
		return httpRequest(false, "GET", null, url);
	}
	
	public static String httpGetRequest(String url,Map<String, String> params){
		String requestUrl = getUrl(url, params);
		return httpGetRequest(requestUrl);
	}

	public static String httpsPostRequest(String url, String params) {
		return httpRequest(true, "POST", params, url);
	}

	public static String httpsGetRequest(String url) {
		return httpRequest(true, "GET", null, url);
	}
	
	/**
	 * 键值对传参数
	 * @param url
	 * @param params
	 * @return
	 */
	public static String httpPostRequest(String url,Map<String, String> params) {
		CloseableHttpClient client = getClient(false);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
		
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);
		EntityBuilder entityBuilder = EntityBuilder.create();
		try{
			if(params!=null){
				
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> e:params.entrySet()) {
					list.add(new BasicNameValuePair(e.getKey(), e.getValue()));
				}
				entityBuilder.setParameters(list);
			}
			post.setEntity(entityBuilder.build());
			CloseableHttpResponse response = client.execute(post);
			HttpEntity responseentity = response.getEntity();
			if (responseentity != null) {
	            String result = EntityUtils.toString(responseentity, "UTF-8");
	            logger.info("url==> "+url+" result==> "+result);
				return result;
			}else{
				throw new NullPointerException();
			}
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	
	
	
	public static String httpsGetRequest(String url,Map<String, String> params) {
		String requestUrl = getUrl(url, params);
		return httpsGetRequest(requestUrl);
	}
	
	
	private static String httpRequest(Boolean isHttps, String method,
			String params, String url) {
		try {
			CloseableHttpClient client = getClient(isHttps);
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(3000)
					.setConnectTimeout(3000).build();
			CloseableHttpResponse response = null;
			if ("GET".equals(method)) {
				HttpGet get = new HttpGet(url);
				get.setConfig(requestConfig);
				get.setHeader("Accept", "application/json");
				response = client.execute(get);
			 }else {
				HttpPost post = new HttpPost(url);
				post.setConfig(requestConfig);
				post.setHeader("Accept", "application/json");
				HttpEntity postEntity = new StringEntity(params,ContentType.APPLICATION_JSON);
				post.setEntity(postEntity);
				response = client.execute(post);
			}
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
                    String result = EntityUtils.toString(entity, "UTF-8");
                    logger.info("url==> "+url+" result==> "+result);
					return result;
				} else {
					logger.error("Method:" + method + " isHttps: " + isHttps
							+ " params: " + params + " url: " + url
							+ " response is null");
				}
			} finally {
				response.close();
				client.close();
			}
		} catch (Exception e) {
			logger.error(
					"Method:" + method + " isHttps: " + isHttps + " params: "
							+ params + " url: " + url + " request failed", e);
		}
		return null;
	}

	private static CloseableHttpClient getClient(boolean isHttps){
		if (isHttps) {
			return Objects.requireNonNull(createSSLInsecureClient());
		}
		return HttpClients.createDefault();
	}
	
	private static CloseableHttpClient createSSLInsecureClient() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
					null, new TrustStrategy() {
						// 信任所有证书
						@Override
						public boolean isTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
							return true;
						}
					}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception e) {
			logger.error("get ssl client failed", e);
		}
		return null;
	}
	
	private static String getUrl(String url,Map<String, String> params){
		StringBuilder requestUrl = new StringBuilder(url);
		int i = 0;
		for (Entry<String, String> entry:params.entrySet()){
			String value = entry.getValue();
			if (value==null || "".equals(value)) {
				continue;
			}
			try {
				value = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (i==0) {
				if (url.matches(".*\\?.*")) {
					requestUrl.append("&");
				}else{
					requestUrl.append("?");
				}
				requestUrl.append(entry.getKey()).append("=").append(value);
			}else {
				requestUrl.append("&").append(entry.getKey()).append("=").append(value);
			}
			i++;
		}
		return requestUrl.toString();
	}
	
	public static String httpRequestStream(InputStream in, String url,String name) throws ClientProtocolException, IOException{
		return httpRequestStream(in,url,name,null);
	}
	
	/**
	 * 上传文件 按流上传
	 * @param in
	 * @param url
	 * @param name
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String httpRequestStream(InputStream in, String url,String name , Map<String, String> params) throws ClientProtocolException, IOException{
		CloseableHttpClient client = getClient(false);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
		if(params!=null){
			url = getUrl(url, params);
		}
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);
		post.setHeader("Accept", "application/json");
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();

		
		builder.addBinaryBody(name, in);
		
		post.setEntity(builder.build());
		CloseableHttpResponse response = client.execute(post);
		HttpEntity responseentity = response.getEntity();
		if (responseentity != null) {
            String result = EntityUtils.toString(responseentity, "UTF-8");
            logger.info("url==> "+url+" result==> "+result);
			return result;
		}else{
			throw new NullPointerException();
		}
	}
	
	public static String httpRequestFile(File file, String url,String name) throws ClientProtocolException, IOException{
		return httpRequestFile(file,url,name,null);
	}
	
	
	public static String httpRequestFile(File file, String url,String name,Map<String, String> params) throws ClientProtocolException, IOException{
		CloseableHttpClient client = getClient(false);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
		if(params!=null){
			url = getUrl(url, params);
		}
		
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);
		post.setHeader("Accept", "application/json");
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addBinaryBody(name, file);
		post.setEntity(builder.build());
		CloseableHttpResponse response = client.execute(post);
		HttpEntity responseentity = response.getEntity();
		if (responseentity != null) {
            String result = EntityUtils.toString(responseentity, "UTF-8");
            logger.info("url==> "+url+" result==> "+result);
			return result;
		}else{
			throw new NullPointerException();
		}
	}
	
	
	/**
	 * 判断url是否存在
	 * @param URLName
	 * @return
	 */
    public static boolean urlExists(String URLName) {
        try {
        	//设置此类是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）。
            HttpURLConnection.setFollowRedirects(false);
            //到 URL 所引用的远程对象的连接
            HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
            /* 设置 URL 请求的方法， GET POST HEAD OPTIONS PUT DELETE TRACE 以上方法之一是合法的，具体取决于协议的限制。*/
            con.setRequestMethod("HEAD");
            //从 HTTP 响应消息获取状态码
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

	
}
