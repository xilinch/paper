package com.baoduoduo.admin.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 作者 LXL 罗小龙
 * @version 1.0 创建时间：2012-8-3 上午10:43:03 类说明数字工具类，提供数字格式转换，获取随机数等方法
 */
public class NumberUtil {

	/**
	 * 货币缺省显示格式
	 */
	public final static String DEFAULT_MONEY_FORMAT = "#,000.00";

	/**
	 * <p>
	 * 把double型数字转换为指定格式的字符串。注意：如果要转换的数字的小数位多于要保留的小数位，
	 * 在转换为字符串之前，数字转换为相差最小的数字。如果不希望遵循这样的原则,则在调用 该方法之前,先处理要转换的数字.
	 * </p>
	 * 
	 * @param number
	 * @param format
	 *            标准的数字转换格式，如"#,##0.00"
	 * @return
	 */
	public static String format(double number, String format) {
		return new DecimalFormat(format).format(number);
	}

	/**
	 * 把long型数字转换为指定格式的字符串
	 * 
	 * @param number
	 * @param format
	 *            标准的数字格式，如"#,##0.00"
	 * @return
	 */
	public static String format(long number, String format) {
		return new DecimalFormat(format).format(number);
	}

	/**
	 * 把一个数字对象转换为指定格式的字符串
	 * 
	 * @param number
	 * @param format
	 * @return
	 */
	public static String format(Object number, String format) {
		return new DecimalFormat(format).format(number);
	}

	/**
	 * 把long型数字转换成国际标准数字格式的字符串 如：10000 转换后：10,000
	 * 
	 * @param number
	 * @return
	 */
	public static String format(long number) {
		return format(number, "#,###");
	}

	/**
	 * 把double型数字转换成指定小数位长度的百分比字符串
	 * 
	 * @param number
	 * @param fractionDigits
	 * @return
	 */
	public static String format(double number, int fractionDigits) {
		return format(number, fractionDigits, fractionDigits);
	}

	/**
	 * 把double型数字转换成指定小数位长度的百分比字符串 可以指定输出最大小数位的值和最少小数位的值
	 * 
	 * @param number
	 * @param maximum
	 * @param minimum
	 * @return
	 */
	public static String format(double number, int maximum, int minimum) {
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		percentFormat.setMaximumFractionDigits(maximum);
		percentFormat.setMinimumFractionDigits(minimum);
		return percentFormat.format(number);
	}

	/**
	 * 得到一个大于等于min且小于等于max的随机整数。注意：参数要为正整数，如果为负数，会发生 运行时异常。
	 * 
	 * @param min
	 *            最小的整数
	 * @param max
	 *            最大的整数
	 * @return
	 */
	public static int getRandomInt(int min, int max) {
		if (min == max) {
			return min;
		}

		if (min > max) {
			throw new IllegalArgumentException("NumberUtil.getRandomInt()方法非法参数值：min不能大于max");
		}
		Random rand = new Random();
		int ret = 0;

		// 如果随机数<min，则继续取随机数，直到随机数>=min
		while (true) {
			// 得到一个<=max的整数
			ret = rand.nextInt(max + 1);
			if (ret >= min) {
				break;
			}
		}
		return ret;
	}

	/**
	 * 得到一个精确度到小数点后setScale位并进行四舍五入处理的double数
	 * 
	 * @param number
	 *            待处理数
	 * @param setScale
	 *            精确度
	 * @return
	 */
	public static double roundHalfUp(double number, int setScale) {
		BigDecimal bigDecimal = new BigDecimal(number);
		return bigDecimal.setScale(setScale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 四舍五入返回整数
	 * 
	 * @param num
	 * @return int
	 */
	public static int setScale(Double num) {
		BigDecimal bd = new BigDecimal(num);
		return bd.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
	}

	/**
	 * 是否为数字
	 * 
	 * @param str
	 * @return description TODO author luoxiaolong date 2012-8-7 下午02:35:43
	 */
	public static boolean isNumeric(String str) {
		if (str != null && !str.trim().equals("")) {
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(str);
			if (!isNum.matches()) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 将分转成元
	 * 
	 * @param price
	 * @return
	 */
	public static BigDecimal formatFen2Yuan(int price) {
		BigDecimal bd = new BigDecimal(price);

		BigDecimal bf = new BigDecimal(100);

		return bd.divide(bf);
	}
}
