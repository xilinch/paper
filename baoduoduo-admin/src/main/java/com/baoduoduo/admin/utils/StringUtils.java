/**
 * @author zxx
 * @time 2016年7月29日上午9:30:47
 */
package com.baoduoduo.admin.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

/**
 * @author zxx
 * @time 2016年7月29日
 *
 */
public class StringUtils {
	
	private static Random rnd = new Random();
	private static final Logger log = Logger.getLogger(StringUtils.class);

	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	final static String digitsBase36 = "0123456789abcdefghijklmnopqrstuvwxyz";

	/**
	 * 判断一个字符串是否为null或是空字符串
	 * <p>
	 * 
	 * @param str
	 *            The string for checking
	 * @return true if the string is neither null nor empty string
	 */
	public static boolean isEmpty(String str) {
		return str == null || (str.trim().length() == 0);
	}

	public static boolean isEmpty(Object obj) {
		return obj == null || isEmpty(obj.toString());
	}

	/**
	 * byte数组转化为16进制的String
	 * <p>
	 * 
	 * @param byteData
	 *            byte[] 字节数组
	 * @return String
	 *         <p>
	 *         把字节数组转换成可视字符串
	 *         </p>
	 */
	public static String toHex(byte byteData[]) {
		return toHex(byteData, 0, byteData.length);
	}

	/**
	 * 将字符串data按照encode转化为byte数组，然后转化为16进制的String
	 * 
	 * @param data
	 *            源字符串
	 * @param encode
	 *            字符编码
	 * @return 把字节数组转换成可视字符串
	 */
	public static String toHex(String data, String encode) {
		try {
			return toHex(data.getBytes(encode));
		} catch (Exception e) {
			log.error("toHex:" + data + ",encode:" + encode);
		}
		return "";
	}

	/**
	 * byte转化为16进制的String
	 * 
	 * @param b
	 * @return 16进制的String
	 */
	public static String toHex(byte b) {
		byte[] buf = { b };
		return toHex(buf);
	}

	/*
	 * public static String toHex2(byte byteData[], int off, int len) {
	 * //等效于下面的toHex，但速度慢10倍以上 StringBuffer buf = new StringBuffer(len * 2); int
	 * i;
	 * 
	 * for (i = off; i < len; i++) { if ( ( (int) byteData[i] & 0xff) < 0x10) {
	 * buf.append("0"); } buf.append(Integer.toString( (int) byteData[i] & 0xff,
	 * 16)); } return buf.toString(); }
	 */
	/**
	 * byte数组的部分字节转化为16进制的String
	 * 
	 * @param byteData
	 *            待转换的byte数组
	 * @param offset
	 *            开始位置
	 * @param len
	 *            字节数
	 * @return 16进制的String
	 */
	public static String toHex(byte byteData[], int offset, int len) {
		char buf[] = new char[len * 2];
		int k = 0;
		for (int i = offset; i < len; i++) {
			buf[k++] = digits[((int) byteData[i] & 0xff) >> 4];
			buf[k++] = digits[((int) byteData[i] & 0xff) % 16];
		}
		return new String(buf);
	}

	/*
	 * public static byte[] hex2Bytes( String hex ) { //等效于下面的hex2Bytes，但速度慢8倍以上
	 * if( isEmpty(hex) || hex.length() %2> 0 ) {
	 * log.error("hex2Bytes: invalid HEX string:" + hex ); return null; } int
	 * len = hex.length() /2; byte[] ret = new byte[ len ]; int k = 0; for (int
	 * i = 0; i < len; i++) { ret[i] = (byte)Integer.parseInt(
	 * hex.substring(k,k+2), 16 ); k += 2; } return ret; }
	 */
	/**
	 * 将16进制的字符串转换为byte数组，是toHex的逆运算
	 * 
	 * @param hex
	 *            16进制的字符串
	 * @return byte数组
	 */
	public static byte[] hex2Bytes(String hex) {
		if (isEmpty(hex) || hex.length() % 2 > 0) {
			log.error("hex2Bytes: invalid HEX string:" + hex);
			return null;
		}
		int len = hex.length() / 2;
		byte[] ret = new byte[len];
		int k = 0;
		for (int i = 0; i < len; i++) {
			int c = hex.charAt(k++);
			if (c >= '0' && c <= '9')
				c = c - '0';
			else if (c >= 'a' && c <= 'f')
				c = c - 'a' + 10;
			else if (c >= 'A' && c <= 'F')
				c = c - 'A' + 10;
			else {
				log.error("hex2Bytes: invalid HEX string:" + hex);
				return null;
			}
			ret[i] = (byte) (c << 4);
			c = hex.charAt(k++);
			if (c >= '0' && c <= '9')
				c = c - '0';
			else if (c >= 'a' && c <= 'f')
				c = c - 'a' + 10;
			else if (c >= 'A' && c <= 'F')
				c = c - 'A' + 10;
			else {
				log.error("hex2Bytes: invalid HEX string:" + hex);
				return null;
			}
			ret[i] += (byte) c;
		}
		return ret;
	}

	/**
	 * 字符编码转换
	 * 
	 * @param str
	 *            源字符串
	 * @param srcCharset
	 *            源字符串的编码方式
	 * @param dstCharset
	 *            目标字符串的编码方式（字节真正的编码方式）
	 * @return 转换后的字符串
	 */
	public static String charsetConvert(String str, String srcCharset, String dstCharset) {
		if (isEmpty(str))
			return "";
		try {
			return new String(str.getBytes(srcCharset), dstCharset);
		} catch (Exception e) {
			log.error("charsetConvert:" + e);
		}
		return str;
	}

	/**
	 * 将字符串从ISO格式转换为UTF-8格式
	 * <p>
	 * <p>
	 * 
	 * @param str
	 * @return String
	 */
	public static String iso2utf8(String str) {
		return charsetConvert(str, "ISO-8859-1", "UTF-8");
	}

	/**
	 * 将字符串从UTF-8格式转换为ISO格式
	 * <p>
	 * <p>
	 * 
	 * @param str
	 * @return String
	 */
	public static String utf82iso(String str) {
		return charsetConvert(str, "UTF-8", "ISO-8859-1");
	}

	/**
	 * 将字符串从ISO格式转换为gb2312格式
	 * <p>
	 * <p>
	 * 
	 * @param str
	 * @return String
	 */
	public static String iso2gbk(String str) {
		return charsetConvert(str, "ISO-8859-1", "GBK");
	}

	/**
	 * 将字符串从gb2312格式转换为ISO格式
	 * <p>
	 * <p>
	 * 
	 * @param str
	 * @return String
	 */
	public static String gbk2iso(String str) {
		return charsetConvert(str, "GBK", "ISO-8859-1");
	}

	/**
	 * 将字符串从UTF-8格式转换为gbk格式
	 * <p>
	 * <p>
	 * 
	 * @param str
	 * @return String
	 */
	public static String utf82gbk(String str) {
		return charsetConvert(str, "UTF-8", "GBK");
	}

	/**
	 * 将字符串从gb2312格式转换为UTF-8格式
	 * <p>
	 * <p>
	 * 
	 * @param str
	 * @return String
	 */
	public static String gbk2utf8(String str) {
		return charsetConvert(str, "GBK", "UTF-8");
	}

	/**
	 * 在字符串的左边添加多个字符pad，直到字符串的长度达到length为止，如果原始长度已经大于length，直接返回源串
	 * 
	 * @param str
	 *            源字符串
	 * @param pad
	 *            新加的站位符，通常是空格或0等参数
	 * @param length
	 *            目标长度
	 * @return 长度大于或等于length的新字符串
	 */
	public static String leftPadString(String str, char pad, int length) {
		if (str.length() >= length)
			return str;
		StringBuffer sb = new StringBuffer();
		while (sb.length() < length - str.length())
			sb.append(pad);
		sb.append(str);
		return sb.toString();
	}

	/**
	 * 在字符串的右边添加多个字符pad，直到字符串的长度达到length为止，如果原始长度已经大于length，直接返回源串
	 * 
	 * @param str
	 *            源字符串
	 * @param pad
	 *            新加的站位符，通常是空格或0等参数
	 * @param length
	 *            目标长度
	 * @return 长度大于或等于length的新字符串
	 */
	public static String rightPadString(String str, char pad, int length) {
		if (str.length() >= length)
			return str;
		StringBuffer sb = new StringBuffer(str);
		while (sb.length() < length)
			sb.append(pad);
		return sb.toString();
	}

	/**
	 * 为int类型的数字限定位数，不足则前边补零，
	 * 
	 * @param num
	 *            源数字
	 * @param strLen
	 *            限定位数
	 * @return String 结果数字的字符串形式 若strLen<=0 返回int对应的字符串
	 */
	public static String intPadString(int num, int strLen) {
		return leftPadString(String.valueOf(num), '0', strLen);
	}

	/**
	 * 从src的搜索出介于begin和end之间的字符串，
	 * 如substring("user=admin&passwd=123&code=888","passwd=","&")将返回"123"
	 * 
	 * @param src
	 * @param begin
	 * @param end
	 * @return
	 */
	public static String subString(String src, String begin, String end) {
		return subString(src, 0, begin, end);
	}

	/**
	 * 从src的offset位置开始搜索出介于begin和end之间的字符串，
	 * 如subString("user=admin&passwd=123&code=888",0,"passwd=","&")将返回"123"
	 * 
	 * @param src
	 * @param offset
	 * @param begin
	 * @param end
	 * @return
	 */
	public static String subString(String src, int offset, String begin, String end) {
		if (isEmpty(src) || offset >= src.length())
			return "";
		int b = offset;
		int e = src.length();
		if (!isEmpty(begin)) {
			b = src.indexOf(begin, offset);
			if (b < 0)
				return "";
			b += begin.length();
		}
		if (!isEmpty(end) && b < e) {
			e = src.indexOf(end, b);
			if (e < 0)
				return "";
		}
		return src.substring(b, e);
	}

	/**
	 * 获取一个随机数字符串，限定位数，不足则前边补零，
	 * 
	 * @param maxValue
	 *            可能的最大随机数
	 * @param strLen
	 *            限定位数
	 * @return String 结果数字的字符串形式 若strLen<=0 返回int对应的字符串
	 */
	public static String getRandomNumberString(int maxValue, int strLen) {
		return intPadString(rnd.nextInt(maxValue), strLen);
	}

	public static String getRandomNumberStringBase36(int strLen) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strLen; i++) {
			sb.append(digitsBase36.charAt(rnd.nextInt(36)));
		}
		return sb.toString();
	}

	/**
	 * 生成随机数字和字母
	 * 
	 * @param strLen
	 * @return
	 */
	public String getRandomNumberString(int strLen) {

		String val = "";
		Random random = new Random();

		// 参数length，表示生成几位随机数
		for (int i = 0; i < strLen; i++) {

			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	/**
	 * 获取一个随机数
	 * 
	 * @param maxValue
	 *            可能的最大随机数
	 * @return 不大于maxValue的整型数
	 */
	public static int getRandomNumber(int maxValue) {
		return rnd.nextInt(maxValue);
	}

	/**
	 * 计算字符串的md5的摘要信息
	 * 
	 * @param s
	 *            源字符串
	 * @return 32字节的16进制的字符串
	 */
	public static String md5(String s) {
		return md5(s, null);
	}

	/**
	 * 计算字符串的md5的摘要信息
	 * 
	 * @param data
	 *            源字符串
	 * @param key
	 *            salt字符串，
	 * @return 32字节的16进制的字符串
	 */
	public static String md5(String data, String key) {
		return doHash(data, key, "MD5");
	}

	/**
	 * 计算字符串的SHA1的摘要信息
	 * 
	 * @param s
	 *            源字符串
	 * @return 32字节的16进制的字符串
	 */
	public static String sha1(String s) {
		return md5(s, null);
	}

	/**
	 * 计算字符串的SHA1的摘要信息
	 * 
	 * @param data
	 *            源字符串
	 * @param key
	 *            salt字符串，
	 * @return 32字节的16进制的字符串
	 */
	public static String sha1(String data, String key) {
		return doHash(data, key, "SHA1");
	}

	/**
	 * 计算字符串的摘要信息
	 * 
	 * @param data
	 *            源字符串
	 * @param key
	 *            salt字符串，
	 * @param digestName
	 *            摘要算法名称，可以是MD5，SHA1等
	 * @return 32字节的16进制的字符串
	 */
	public static String doHash(String data, String key, String digestName) {
		String ret = "";
		if (isEmpty(data))
			return ret;

		try {
			MessageDigest mgd = MessageDigest.getInstance(digestName);
			mgd.update(data.getBytes());
			byte[] bytes = null;
			if (isEmpty(key)) {
				bytes = mgd.digest();
			} else {
				bytes = mgd.digest(key.getBytes());
			}
			mgd.reset();
			ret = toHex(bytes, 0, bytes.length);
		} catch (NoSuchAlgorithmException e) {
			log.error("hash error:" + e);
		}
		return ret;
	}

	/**
	 * 对html中的特殊字符转换成相应的字符实体<br>
	 * 如 {@literal &} {@literal <} {@literal >} {@literal "} {@literal '} 空格 回车
	 * 换行等
	 * 
	 * <pre>
	 * <b>HTML字符实体(HTML Character Entities)释义</b>
	 * 一个字符实体(Character Entity)分成三部分：
	 * （1）第一部分是一个&符号，英文叫ampersand；
	 * （2）第二部分是实体(Entity)名字或者是#加上实体(Entity)编号；
	 * （3）第三部分是一个分号。
	 * 实例：要显示小于号，就可以写{@literal &lt;}或者{@literal &#60};
	 * 
	 * 用实体(Entity)名字的好处是比较好理解，一看lt，大概就猜出是less than的意思，
	 * 但是其劣势在于并不是所有的浏览器都支持最新的Entity名字。而实体(Entity)编号，各种浏览器都能处理。
	 * 
	 * <font color="red">注意：Entity是区分大小写的。</font>
	 * 
	 * 参考：
	 * http://www.fh888.com/showfile.html?articleid=AA60A6086B7F49E0B3C3610621B29A20&projectid=5&username=cary
	 * http://www.w3.org/TR/html401/charset.html#h-5.3
	 * </pre>
	 * 
	 * @param content
	 *            原始的html代码
	 * @return 转义后的html代码
	 */
	public static String fixHtml(String content) {
		if (content == null || content.trim().length() == 0)
			return content;
		StringBuilder builder = new StringBuilder(content.length());
		for (int i = 0; i < content.length(); ++i) {
			switch (content.charAt(i)) {
			case '<':
				builder.append("&lt;");
				break;
			case '>':
				builder.append("&gt;");
				break;
			case '"':
				builder.append("&quot;");
				break;
			case '\'':
				builder.append("&#39;");
				break;
			case '%':
				builder.append("&#37;");
				break;
			case ';':
				builder.append("&#59;");
				break;
			case '(':
				builder.append("&#40;");
				break;
			case ')':
				builder.append("&#41;");
				break;
			case '&':
				builder.append("&amp;");
				break;
			case '+':
				builder.append("&#43;");
				break;
			case '\r':// do nothing
				break;
			case '\n':
				builder.append("&lt;br&gt;");
				break;
			case '\t':
				builder.append("&#09;");
				break;
			case ' ':
				builder.append("&nbsp;");
				break;
			default:
				builder.append(content.charAt(i));
				break;
			}
		}
		return builder.toString();
	}

	/**
	 * 对html中的特殊字符转义进行还原，如&，<, >, ", ', 空格，回车，换行等
	 * 
	 * @param content
	 *            转义后的html代码
	 * @return 原始的html代码
	 */
	public static String unfixHtml(String content) {
		String ret = content;
		ret = ret.replaceAll("&lt;", "<").replaceAll("&#60;", "<");
		ret = ret.replaceAll("&gt;", ">").replaceAll("&#62;", ">");
		ret = ret.replaceAll("&quot;", "\"").replaceAll("&#34;", "\"");
		ret = ret.replaceAll("&#39;", "'");
		ret = ret.replaceAll("&#37;", "%");
		ret = ret.replaceAll("&#59;", ";");
		ret = ret.replaceAll("&#40;", "(");
		ret = ret.replaceAll("&#41;", ")");
		ret = ret.replaceAll("&nbsp;", " ");
		ret = ret.replaceAll("&amp;", "&");
		ret = ret.replaceAll("&#43;", "+");
		ret = ret.replaceAll("&#09;", "\t");
		ret = ret.replaceAll("<br>", "\n").replaceAll("&#10;", "\n");
		return ret;
	}

	/**
	 * 判断email地址是否符合规范
	 * 
	 * @param email
	 *            待检查的email地址
	 * @return 符合返回true，否则返回false
	 */
	public static boolean isEmail(String email) {
		if (isEmpty(email))
			return false;
		return Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email);
	}

	/**
	 * 判断是否是手机号
	 * 
	 * @param email
	 *            待检查的电话号码串
	 * @return 符合返回true，否则返回false
	 */
	public static boolean isMobilePhone(String s) {
		if (isEmpty(s))
			return false;
		return Pattern.matches("^(13|15|18|14|17)\\d{9}$", s);
	}

	public static boolean isPhoneNumber(String s) {
		if (isEmpty(s))
			return false;
		return Pattern.matches("^[0-9\\-\\(\\)\\ ]+$", s);
	}

	public static boolean isDate(String s) {
		if (isEmpty(s))
			return false;
		return Pattern.matches("^[0-9]{4}\\-[0-9]{1,2}\\-[0-9]{1,2}$", s);
	}

	public static boolean isNumber(String s) {
		if (isEmpty(s))
			return false;
		return Pattern.matches("^[-]*[0-9\\.]+$", s);
	}

	public static boolean isOnlyLetter(String s) {
		if (isEmpty(s))
			return false;
		return Pattern.matches("^[a-zA-Z\\ \\']+$", s);
	}

	public static boolean isImageFile(String s) {
		if (isEmpty(s))
			return false;
		return Pattern.matches("(.*)\\.(jpeg|jpg|bmp|gif|png)$", s);
	}

	public static boolean isOnlyChinese(String s) {
		if (isEmpty(s))
			return false;
		return Pattern.matches("[^u4e00-u9fa5]+$", s);
	}

	public static boolean isUrl(String s) {
		if (isEmpty(s))
			return false;
		return Pattern.matches("^(https|http|ftp|rtsp|mms)?:\\/\\/[^s]*$", s);
	}

	/**
	 * 可识别的windows GUID字符串转换为16位byte数组
	 * 
	 * @param guid
	 *            GUID字符串
	 * @return 16位byte数组
	 */
	public static byte[] guid2bytes(String guid) {
		// windows guid:75B22630-668E-11CF-A6D9-00AA0062CE6C ==>
		// 3026B2758E66CF11A6D900AA0062CE6C
		StringBuffer sb = new StringBuffer(guid);
		sb.replace(0, 2, guid.substring(6, 8)).replace(2, 4, guid.substring(4, 6)).replace(4, 6, guid.substring(2, 4)).replace(6, 8, guid.substring(0, 2));
		sb.replace(9, 11, guid.substring(11, 13)).replace(11, 13, guid.substring(9, 11));
		sb.replace(14, 16, guid.substring(16, 18)).replace(16, 18, guid.substring(14, 16));
		return StringUtils.hex2Bytes(sb.toString().replace("-", ""));
	}

	/**
	 * 转换16位byte数组为可识别的windows GUID字符串
	 * 
	 * @param buf
	 *            16位byte数组
	 * @return GUID字符串
	 */
	public static String bytes2Guid(byte[] buf) {
		return bytes2Guid(buf, 0);
	}

	/**
	 * 转换16位byte数组为可识别的windows GUID字符串
	 * 
	 * @param buf
	 *            byte数组
	 * @param offset
	 *            数组的开始位置
	 * @return GUID字符串
	 */
	public static String bytes2Guid(byte[] buf, int offset) {
		// 3026B2758E66CF11A6D900AA0062CE6C ==>
		// 75B22630-668E-11CF-A6D9-00AA0062CE6C
		final int guidSize = 16;
		if (buf == null || offset < 0 || (offset + guidSize > buf.length))
			return "";

		String hex = StringUtils.toHex(buf, offset, guidSize);
		StringBuffer sb = new StringBuffer();
		sb.append(hex.subSequence(6, 8)).append(hex.subSequence(4, 6)).append(hex.subSequence(2, 4)).append(hex.subSequence(0, 2));
		sb.append("-").append(hex.subSequence(10, 12)).append(hex.subSequence(8, 10));
		sb.append("-").append(hex.subSequence(14, 16)).append(hex.subSequence(12, 14));
		sb.append("-").append(hex.subSequence(16, 20));
		sb.append("-").append(hex.substring(20));
		return sb.toString().toUpperCase();
	}

	/**
	 * 删除字符串的第一个和最后一个字符
	 * 
	 * @param str
	 * @return 去除首字符和最后一个字符后的字符串
	 */
	public static String truncateFirstEnd(String str) {
		if (isEmpty(str))
			return str;

		String tmp = str.substring(1);
		return tmp.substring(0, tmp.length() - 1);
	}

	/**
	 * 正则表达式 获取相关结果
	 * 
	 * @param src
	 * @param reg
	 * @param groupIndex
	 * @return
	 */
	public static Matcher getMatcherGroup(String src, String reg) {
		if (StringUtils.isEmpty(src))
			return null;
		Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		return p.matcher(src.toLowerCase());
	}

	/**
	 * 对字符串进行解码或反转义的。
	 * 
	 * @param src
	 * @return
	 */
	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	public static String stringValue(String src, String def) {
		return StringUtils.isEmpty(src) ? def : src;
	}

	public static Long string2Long(Object obj) {
		if (obj == null || false == NumberUtil.isNumeric(obj + ""))
			return 0L;
		return Long.valueOf(obj + "");
	}

	// 把如“398万5千”之类的数据转换成为“3985000”
	public static int cnNum2Int(String s) {
		String numStr = "";
		int result = 0;
		int util = 1;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c > 47 && c < 58) {
				numStr += String.valueOf(c);
				continue;
			}
			switch (c) {
			case '十':
				util = 10;
				break;
			case '百':
				util = 100;
				break;
			case '千':
				util = 1000;
				break;
			case '万':
				util = 10000;
				break;
			case '亿':
				util = 100000000;
				break;
			}
			if (!numStr.equals("")) {
				int temp = Integer.parseInt(numStr);
				result += temp * util;
				util = 1;
				temp = 0;
				numStr = "";
				continue;
			}
			result = result * util;
			util = 1;
		}
		if (!numStr.equals(""))
			result += Integer.parseInt(numStr) * util;

		return result;
	}

	/**
	 * 优惠券前缀5位(13177),"13"表示13年,177表示一年中的第177天
	 * 
	 * @return
	 */
	public static String getSnPrefix() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(new Date());
		String last2year = year.substring(2, 4);
		String dayOfYear = "";
		int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		if (day < 10) {
			dayOfYear = "00" + day;
		} else if (day < 100) {
			dayOfYear = "0" + day;
		} else {
			dayOfYear = "" + day;
		}
		return last2year + dayOfYear;
	}

	public static String randNum(int n) {
		char[] arrChar = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuilder num = new StringBuilder();
		Random rnd = new Random();
		for (int i = 0; i < n; i++) {
			num.append(arrChar[rnd.nextInt(9)]);
		}
		return num.toString();
	}

	public static int convertStrToInt(String str) {
		int n = -1;
		if (null != str && !"".equals(str.trim())) {
			try {
				n = parseInt(str.trim());
			} catch (Exception e) {
			}
		}
		return n;
	}

	public static int parseInt(String idStr) throws Exception {
		if (StringUtils.isNullOrBlank(idStr)) {
			return -1;
		}
		try {
			idStr = idStr.trim();
			return Integer.parseInt(idStr);
		} catch (Exception e) {
			throw new Exception("抱歉，你输入的id值是非数字型！");
		}
	}

	public static boolean isNullOrBlank(String str) {
		if (str == null) {
			return true;
		}
		str = str.trim();
		if (!str.equals("")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 字符串不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		return !isNullOrBlank(str);
	}

	/**
	 * 获取指定长度的随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "BCLMHcdefgDFGhijklEm45IJK6S1WZ7abvwXYxyz02n8opq39ANrstTUVuOPQR";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 以当前时间毫秒数为准获取唯一字符串
	 * 
	 * @return
	 */
	public static String getUniqueString() {
		String key = new Date().getTime() + "";
		String base = "lEm45IJK6S1WZ7abvwXYxyz02";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < key.length(); i++) {
			try {
				Thread.sleep(1l);
				int index = Integer.parseInt(key.charAt(i) + "");
				if (index == 0) {
					sb.append(getRandomString(2));
				}
				sb.append(base.charAt(index));

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return sb.toString();
	}

	/**
	 * 以当前时间毫秒数为准获取唯一字符串(数字)
	 * 
	 * @return
	 */
	public static String getUniqueNumber() {
		Date date = new Date();
		String key = date.getTime() + "";
		// System.err.println(key);
		Map<String, String> baseKey = new HashMap<String, String>();
		baseKey.put("0", "3");
		baseKey.put("1", "1");
		baseKey.put("2", "7");
		baseKey.put("3", "5");
		baseKey.put("4", "8");
		baseKey.put("5", "9");
		baseKey.put("6", "4");
		baseKey.put("7", "2");
		baseKey.put("8", "6");
		baseKey.put("9", "0");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < key.length(); i++) {
			try {
				Thread.sleep(1l);
				String index = key.charAt(i) + "";
				sb = sb.append(baseKey.get(index));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return sb.toString();
	}

	/**
	 * 数组去重去空，有空需要抽取到工具类
	 * 
	 * @param array
	 * @return
	 */
	public static String[] trimArray(String[] array) {

		Set<String> set = new TreeSet<String>();

		for (String i : array) {
			if (!StringUtils.isEmpty(i))
				set.add(i);
		}

		String[] des = new String[set.size()];
		int j = 0;
		for (String i : set) {
			des[j++] = i;
		}

		return des;

	}

	/**
	 * 是否为金额
	 * 
	 * @param amount
	 * @return
	 */
	public static boolean isMoney(String amount) {
		if (StringUtils.isNullOrBlank(amount)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
		Matcher match = pattern.matcher(amount);

		return match.matches();
	}

	public static String escapeHtml(String source) {
		if (source == null) {
			return "";
		}
		source = source.replaceAll("<", "&lt;");
		source = source.replaceAll(">", "&gt;");
		return source;
	}

	public static void main(String[] args) {
		String a = "</script></script>";
		a = escapeHtml(a);
		System.out.println(a);
	}

	/**
	 * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
	 * 例如：HelloWorld->HELLO_WORLD
	 * 
	 * @param name
	 *            转换前的驼峰式命名的字符串
	 * @param caps
	 *            是否大写：否时： HelloWorld->hello_world
	 * @return 转换后下划线大写方式命名的字符串
	 */
	public static String underscoreName(String name, boolean caps) {
		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			// 将第一个字符处理成大写
			result.append(name.substring(0, 1).toUpperCase());
			// 循环处理其余字符
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				// 在大写字母前添加下划线
				if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
					result.append("_");
				}
				// 其他字符直接转成大写
				result.append(s.toUpperCase());
			}
		}
		String word = result.toString();
		if (caps) {
			return word;
		} else {
			return word.toLowerCase();
		}
	}

	/**
	 * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
	 * 例如：HELLO_WORLD->HelloWorld
	 * 
	 * @param name
	 *            转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String camelName(String name) {
		StringBuilder result = new StringBuilder();
		// 快速检查
		if (name == null || name.isEmpty()) {
			// 没必要转换
			return "";
		} else if (!name.contains("_")) {
			// 不含下划线，仅将首字母小写
			return name.substring(0, 1).toLowerCase() + name.substring(1);
		}
		// 用下划线将原始字符串分割
		String camels[] = name.split("_");
		for (String camel : camels) {
			// 跳过原始字符串中开头、结尾的下换线或双重下划线
			if (camel.isEmpty()) {
				continue;
			}
			// 处理真正的驼峰片段
			if (result.length() == 0) {
				// 第一个驼峰片段，全部字母都小写
				result.append(camel.toLowerCase());
			} else {
				// 其他的驼峰片段，首字母大写
				result.append(camel.substring(0, 1).toUpperCase());
				result.append(camel.substring(1).toLowerCase());
			}
		}
		return result.toString();
	}

	// 过滤特殊字符串
	public static String StringFilter(String str) throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	
	/**
	 * 将字符串转换为整数数组
	 * 
	 * @param idsStr
	 * @return
	 */
	public static Integer[] convertStringToIds(String strIds) {
		String[] strs = strIds.split(",");
		Integer[] ids = new Integer[strs.length];
		for (int i = 0; i < strs.length; i++) {
			if (!"".equals(strs[i].trim())) {
				ids[i] = Integer.parseInt(strs[i].trim());
			}
		}
		return ids;
	}
}
