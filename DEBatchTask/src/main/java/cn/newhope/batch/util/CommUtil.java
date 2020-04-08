/*
 * 创建日期 2004-4-20
 *
 */
package cn.newhope.batch.util;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jack
 * 
 */
public class CommUtil {
	 private static Logger logger = LoggerFactory.getLogger(CommUtil.class);

	public static String trim(String str) {
		if (str == null)
			return null;
		return str.trim();
	}

	public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}

	/**
	 * 判断字符串是否为空或空串；其中空串是指经过trim后字符串长度为0。 为空或空串返回true，否则返回false。
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if (str == null || (str.trim()).equals("") || (str.trim()).equals("null") || (str.trim()).equals("[]"))
			return true;
		return false;
	}

	/**
	 * 判断字符串不是空或空串；其中空串是指经过trim后字符串长度为0。 为空或空串返回false，否则返回true。
	 * 
	 * @param str
	 * @return
	 */
	public static boolean notBlank(String str) {
		return !isBlank(str);
	}

	public static boolean isDigital(String str) {
		if (isBlank(str)) {
			return true;
		}
		Pattern ptn = Pattern.compile("^(-|\\+)?\\d*$");
		Matcher mat = ptn.matcher(str);
		return mat.matches();
	}

	public static boolean isNum(String param) {
		String regex = "^\\d+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(param);
		return m.matches();
	}

	/**
	 * 判断两个字符串是否相等 都为空或空串，为相等
	 * 
	 * @param str
	 * @return
	 */
	public static boolean equals(String str1, String str2) {
		if (notBlank(str1)) {
			return str1.equals(str2);
		} else if (notBlank(str2)) {
			return str2.equals(str1);
		}
		return true;
	}

	/**
	 * 判断字符串数组中包含某个字符串。 包含返回true，否则返回false。 例： String[] s = {"a", "b"};
	 * contains(s, "a"); // 返回true
	 * 
	 * @param array
	 * @param str
	 * @return
	 */
	public static boolean contains(String[] array, String str) {
		if (isBlank(str) || array == null)
			return false;
		for (int i = 0; i < array.length; i++) {
			if (str.equals(array[i]))
				return true;
		}
		return false;
	}

	/**
	 * 判断字符串数组中包含某个字符串。不区分大小写。 包含返回true，否则返回false。 例： String[] s = {"a", "b"};
	 * contains(s, "a"); // 返回true
	 * 
	 * @param array
	 * @param str
	 * @return
	 */
	public static boolean containsIgnoreCase(String[] array, String str) {
		if (isBlank(str) || array == null)
			return false;
		for (int i = 0; i < array.length; i++) {
			if (str.equalsIgnoreCase(array[i]))
				return true;
		}
		return false;
	}

	/**
	 * 判断某个字符串在字符串数组中的位置。 返回-1表示数组不存在该字符串。 例： String[] s = {"a", "b"};
	 * position(s, "a"); // 返回0
	 * 
	 * @param array
	 * @param str
	 * @return
	 */
	public static int position(String[] array, String str) {
		if (isBlank(str) || array == null)
			return -1;
		for (int i = 0; i < array.length; i++) {
			if (str.equals(array[i]))
				return i;
		}
		return -1;
	}

	/**
	 * 判断某个字符串在字符串数组中的位置，允许跳过部分字符串。 如果设置method，表示str以array中某个串开始或结束
	 * 
	 * @param array
	 * @param str
	 * @param step
	 * @param method
	 *            "s"开始 "e"结束 其他表示相同
	 * @return
	 */
	public static int posStep(String[] array, String str, int step,
			String method) {
		if (isBlank(str) || array == null)
			return -1;
		for (int i = 0; i < array.length; i++) {
			if (step > 0 && i % step != 0) {
				continue;
			}
			if ("s".equalsIgnoreCase(method)) {
				if (str.startsWith(array[i])) {
					return i;
				}
			} else if ("e".equalsIgnoreCase(method)) {
				if (str.endsWith(array[i])) {
					return i;
				}
			} else {
				if (str.equals(array[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	public static int posIgnoreCase(String[] array, String str) {
		if (isBlank(str) || array == null)
			return -1;
		for (int i = 0; i < array.length; i++) {
			if (str.equalsIgnoreCase(array[i]))
				return i;
		}
		return -1;
	}

	/**
	 * 截短过长字符串
	 * 
	 * @param s
	 * @param 保留的汉字长度
	 * @return
	 */
	public static String strTooLong(String str, int len) {
		if (isBlank(str) || len < 1)
			return str;
		byte[] b = null;
		try {
			b = str.getBytes("GBK");
		} catch (Exception e) {
		}
		int i_len = len * 2;
		if (b.length < i_len) {
			return str;
		}
		byte[] c = new byte[len * 2];
		System.arraycopy(b, 0, c, 0, i_len);
		for (int i = 0, j = 0; i < i_len; i++) {
			if (b[i] == '\t' || b[i] == '\n') {
				i_len = i;
				break;
			}
			if (j == 0 && b[i] < 0 && i == i_len - 1) {
				// 最后一个字节是汉字首字节
				i_len--;
				break;
			}
			if (j == 1 && i == i_len - 2 && b[i + 1] < 0) {
				// 倒数第二个字节是汉字尾字节，且最后一个字节是汉字首字节
				i_len--;
				break;
			}
			if (j == 0 && b[i] < 0) {
				// 汉字首字节
				j = 1;
			} else if (j == 1) {
				// 汉字尾字节
				j = 0;
			}
		}
		String ret = str;
		try {
			ret = new String(c, 0, i_len, "GBK");
		} catch (Exception e) {
		}
		return ret;
	}

	/**
	 * 返回两个int值中较大的值。 例： maxInt(5,6); // 返回值为6
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int maxInt(int a, int b) {
		return a > b ? a : b;
	}

	public static double maxDouble(double a, double b) {
		return a > b ? a : b;
	}

	/**
	 * 返回两个int值中较小的值。 例： maxInt(5,6); // 返回值为5
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int minInt(int a, int b) {
		return a < b ? a : b;
	}

	public static double minDouble(double a, double b) {
		return a < b ? a : b;
	}

	public static String minDate(String a, String b) {
		if (getDaysTween(a, b) > 0)
			return b;
		else
			return a;
	}

	public static String maxDate(String a, String b) {
		if (getDaysTween(a, b) > 0)
			return a;
		else
			return b;
	}

	/**
	 * 根据指定分隔拆分字符串，返回字符串数组。 例： String[] s = strBreak("a.b.c.d.e","."); //
	 * s为{"a","b","c","d","e"}
	 * 
	 * @param array
	 * @param str
	 * @return
	 */
	public static String[] strBreak(String str, String delim) {
		if (isBlank(str) || delim == null)
			return null;

		StringTokenizer st = new StringTokenizer(str, delim);
		int length = st.countTokens();
		if (length < 1)
			return null;

		String[] strArray = new String[length];
		String s = null;
		int i = 0;
		while (st.hasMoreTokens()) {
			s = st.nextToken();
			strArray[i++] = s;
		}
		return strArray;
	}

	/**
	 * 取中文长度
	 * 
	 * @param str
	 * @return
	 */
	public static int cnlength(String str) {
		if (isBlank(str))
			return 0;
		byte[] b = str.getBytes();
		int len = 0;
		for (int i = 0; i < b.length; i++) {
			if (b[i] < 0) {
				i++;
			}
			len++;
		}
		return len;
	}

	/**
	 * 按中文长度拆分
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String[] cnstrBreak(String str, int length) {
		if (isBlank(str) || length < 1)
			return null;
		byte[] b = str.getBytes();
		int begin = 0;
		int t_len = 0;
		ArrayList list = new ArrayList();
		for (int i = 0; i < b.length; i++) {
			if (b[i] < 0) {
				i++;
			}
			t_len++;
			if (t_len == length) {
				t_len = 0;
				list.add(new String(b, begin, i - begin + 1));
				begin = i + 1;
			}
		}
		if (t_len > 0) {
			list.add(new String(b, begin, b.length - begin));
		}
		String[] ret = new String[list.size()];
		list.toArray(ret);
		return ret;
	}

	/**
	 * 去除字符串数组中重复的字符串。
	 * 
	 * @param strArray
	 * @return
	 */
	public static String[] undupArray(String[] strArray) {
		if (strArray == null || strArray.length <= 1) {
			return strArray;
		}
		HashMap m = new HashMap();
		List l = new Vector();
		for (int i = 0; i < strArray.length; i++) {
			if (strArray == null || m.get(strArray[i]) != null) {
				continue;
			}
			m.put(strArray[i], strArray[i]);
			l.add(strArray[i]);
		}
		if (m.size() == strArray.length) {
			return strArray;
		}
		String[] strArray2 = new String[m.size()];
		l.toArray(strArray2);
		return strArray2;
	}

	/**
	 * 根据指定分隔拆分字符串，返回字符串数组。 不剔出空字符串 例： String[] s = strBreak("a.b.c..d","."); //
	 * s为{"a","b","c","","e"}
	 * 
	 * @param array
	 * @param str
	 * @return
	 */
	public static String[] strBreakWithBlank(String str, String delim) {
		if (isBlank(str) || delim == null)
			return null;

		String[] strArray = str.split("\\" + delim);
		return strArray;
	}

	/**
	 * 根据指定分隔组合字符串数组，返回字符串。 例： String[] s = {"a","b","c","d","e"}; String t =
	 * strJoin(s, "."); // t为"a.b.c.d.e"
	 * 
	 * @param strArray
	 * @param str
	 * @return
	 */
	public static String strJoin(String[] strArray, String delim) {
		if (strArray == null || delim == null)
			return null;
		return strJoin(strArray, delim, 0, strArray.length);
	}

	public static String strJoin(String[] strArray, String delim, int begin,
			int end) {
		if (strArray == null || delim == null)
			return null;

		String str = "";
		String td = "";
		int length = strArray.length;
		for (int i = begin; i < length && i < end; i++) {
			if (notBlank(strArray[i])) {
				str += td + strArray[i];
				td = delim;
			}
		}
		return str;
	}

	public static String strJoinWithBlank(String[] strArray, String delim) {
		if (strArray == null || delim == null)
			return null;
		return strJoinWithBlank(strArray, delim, 0, strArray.length);
	}

	public static String strJoinWithBlank(String[] strArray, String delim,
			int begin, int end) {
		if (strArray == null || delim == null)
			return null;

		String str = "";
		String td = "";
		int length = strArray.length;
		for (int i = begin; i < length && i < end; i++) {
			str += td + getNotNull(strArray[i]);
			td = delim;
		}
		return str;
	}

	/**
	 * 组合字符串数组用于sql的in条件字符串。 例： String[] s = {"a","b","c","d","e"}; String t =
	 * strJoin(s); // t为'a','b','c','d','e'
	 * 
	 * @param strArray
	 * @return
	 */
	public static String strJoinAsIn(String[] strArray) {
		if (strArray == null)
			return null;

		return strJoinAsIn(strArray, 0, strArray.length);
	}

	public static String strJoinAsIn(String[] strArray, int begin, int end) {
		if (strArray == null)
			return null;

		String str = "";
		String td = "";
		int length = strArray.length;
		for (int i = begin; i < length && i < end; i++) {
			if (notBlank(strArray[i])) {
				str += td + "'" + strArray[i] + "'";
				td = ",";
			}
		}
		return str;
	}

	/**
	 * 首字母大写。 例： String s = initialUpperCase("word"); // 返回值为"Word"
	 * 
	 * @param str
	 * @return
	 */
	public static String initialUpperCase(String str) {
		if (isBlank(str))
			return str;
		String ret = str.substring(0, 1);
		ret = ret.toUpperCase() + str.substring(1);
		return ret;
	}

	/**
	 * 填充字符串
	 * 
	 * @param s1
	 * @param s2
	 * @param len
	 * @return
	 */
	public static String fill(String s1, String s2, int len) {
		StringBuffer bf = new StringBuffer(s1);
		byte[] b = s1.getBytes();
		int start = b.length;
		for (int i = start; i < len; i++) {
			bf.append(s2);
		}
		return bf.toString();
	}

	/**
	 * 拼接日期时间字符串。 在时期与时间串之间加一空格； 时间输入省略了分或分秒时，补齐零； 日期为空时，按1970-01-01算。 例：
	 * dtJoin("1990-01-23", ""); // 返回值为"1990-01-23 00:00:00"
	 * dtJoin("1990-01-23", "1"); // 返回值为"1990-01-23 1:00:00"
	 * dtJoin("1990-01-23", "1:23"); // 返回值为"1990-01-23 1:23:00" dtJoin("",
	 * "1:23"); // 返回值为"1970-01-01 1:23:00"
	 * 
	 * @param dateStr
	 *            日期串，格式为YYYY-MM-DD
	 * @param timeStr
	 *            时间串，格式为HH:mm:ss
	 * @return
	 */
	public static String dtJoin(String dateStr, String timeStr) {
		String d = dateStr;
		String t = timeStr;
		if (CommUtil.isBlank(d)) {
			if (CommUtil.isBlank(t))
				return null;
			else {
				d = "1970-01-01";
			}
		}
		if (CommUtil.isBlank(t)) {
			t = "00:00:00";
		} else if (t.length() <= 2) { // hh
			t = t + ":00:00";
		} else if (t.length() <= 5) { // HH:mm
			t = t + ":00";
		}
		return d + " " + t;
	}

	/**
	 * 拼接日期字符串。
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return 日期 格式: YYYY-MM-DD
	 */
	public static String dateJoin(int year, int month, int day) {

		if (year > 9999 || year < 1000 || month < 1 || month > 12 || day < 1
				|| day > 31)
			return "";

		String y = Integer.toString(year);

		String m = "";
		if (month > 9)
			m = Integer.toString(month);
		else
			m = "0" + month;

		String d = "";
		if (day > 9)
			d = Integer.toString(day);
		else
			d = "0" + day;

		return (y + "-" + m + "-" + d);
	}

	// 邓汉斌 修改

	/**
	 * 返回当前年月日期,格式yyyyMMdd
	 * 
	 * @return String 年月日期
	 */
	public static String getCurrentYMD() {
		String date = null;
		try {
			DateFormat myformat = new SimpleDateFormat("yyyyMMdd");
			date = myformat.format(new Date());
		} catch (Exception e) {
			//System.out.println(e);
		}
		return date;
	}

	// 邓汉斌 修改

	/**
	 * 返回当前年月日期,格式yyyyMM
	 * 
	 * @return String 年月日期
	 */
	public static String getCurrentYM() {
		String date = null;
		try {
			DateFormat myformat = new SimpleDateFormat("yyyyMM");
			date = myformat.format(new Date());
		} catch (Exception e) {
			//System.out.println(e);
		}
		return date;
	}

	/*****
	 * 判断yyyyMMdd 字符串日期是否有效
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isDateFormat(String date) {
		Pattern p = Pattern.compile("[1-9][0-9]{3}[0-1][0-9][0-3][0-9]");
		Matcher m = p.matcher(date);
		return m.matches();
	}

	public static String convertObj2Str(Object obj) {
		if (obj == null)
			return "";
		return obj.toString().trim();
	}

	// 邓汉斌--结束

	/**
	 * 拼接时间字符串。
	 * 
	 * @param hour
	 *            时
	 * @param minute
	 *            分
	 * @param second
	 *            秒
	 * @return 时间 格式: HH:mm:ss
	 */
	public static String timeJoin(int hour, int minute, int second) {

		if (second < 0 || second >= 60) {
			minute += second / 60;
			second = second % 60;
		}
		if (minute < 0 || minute >= 60) {
			hour += minute / 60;
			minute = minute % 60;
		}
		if (hour > 24 || hour < 0) {
			hour = hour % 24;
		}

		String h = "";
		if (hour > 9)
			h = Integer.toString(hour);
		else
			h = "0" + hour;

		String m = "";
		if (minute > 9)
			m = Integer.toString(minute);
		else
			m = "0" + minute;

		String s = "";
		if (second > 9)
			s = Integer.toString(second);
		else
			s = "0" + second;

		return (h + ":" + m + ":" + s);
	}

	/**
	 * 日期时间加上一个时间.
	 * 
	 * @param dateStr
	 *            日期时间 "YYYY-MM-DD HH:mm:ss"
	 * @param timeStr
	 *            时间 "HH:mm:ss" 或者 "HH:mm" 或者 "HH"
	 * @return 时间日期 YYYY-MM-DD
	 */
	public static String addTime(String dateStr, String timeStr) {
		try {
			Calendar cal = Calendar.getInstance();
			StringTokenizer strTk = new StringTokenizer(timeStr, ":");
			DateFormat myformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date date = myformat.parse(dateStr);
			cal.setTime(date);
			cal.add(Calendar.HOUR, Integer.parseInt(strTk.nextToken()));
			if (strTk.hasMoreElements()) {
				cal.add(Calendar.MINUTE, Integer.parseInt(strTk.nextToken()));
			}
			date = cal.getTime();
			return CommUtil.formatDate(date, "yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 取出日期时间中的日期
	 * 
	 * @param dt
	 *            时间日期 "YYYY-MM-DD HH:mm:ss"
	 * @return 时间 YYYY-MM-DD
	 */
	public static String getDateOfDT(String dt) {
		String date = "";
		if (notBlank(dt)) {
			if (dt.length() <= 10) {
				date = dt;
			} else {
				date = dt.substring(0, 10);
			}
		}
		return date;
	}

	/**
	 * 取出日期时间中的时间
	 * 
	 * @param dt
	 *            时间日期 "YYYY-MM-DD HH:mm:ss"
	 * @return 时间 HH:mm:ss
	 */
	public static String getTimeOfDT(String dt) {
		String time = "";
		if (notBlank(dt) && dt.length() > 11) {
			if (dt.length() <= 19) {
				time = dt.substring(11);
			} else {
				time = dt.substring(11, 19);
			}
		}
		return time;
	}

	/**
	 * 除去类名前的包名前缀.
	 * 
	 * @param String
	 *            name 完整的类名
	 * @return String 返回除去前缀的类名
	 */
	public static String getClassName(String name) {
		StringTokenizer st = new StringTokenizer(name, ".");
		String clssName = null;
		while (st.hasMoreTokens())
			clssName = st.nextToken();
		return clssName;
	}

	/**
	 * 返回输入的字符串代表的Calendar对象.
	 * 
	 * @param String
	 *            str 输入的字符串,格式=yyyy-MM-dd HH:mm:ss.
	 * @return Calendar 返回代表输入字符串的Calendar对象.
	 */
	public static Calendar getTime(String str) {
		Calendar cal = Calendar.getInstance();
		try {
			DateFormat myformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = myformat.parse(str);
			cal.setTime(date);
		} catch (Exception e) {
			//System.out.println(e);
		}
		return cal;
	}

	/**
	 * 返回输入的字符串代表的Calendar对象.
	 * 
	 * @param String
	 *            str 输入的字符串
	 * @param String
	 *            str 输入的字符串日期格式, 缺省为yyyy-MM-dd
	 * @return Calendar 返回代表输入字符串的Calendar对象
	 */
	public static Calendar getTime(String str, String pattern) {
		Calendar cal = Calendar.getInstance();
		if (pattern == null)
			pattern = "yyyy-MM-dd";
		try {
			DateFormat myformat = new SimpleDateFormat(pattern);
			Date date = myformat.parse(str);
			cal.setTime(date);
		} catch (Exception e) {
			//System.out.println(e);
		}
		return cal;
	}

	/**
	 * 返回当前时间,格式HH:mm:ss
	 * 
	 * @return String 当前时间
	 */
	public static String getCurrentTime() {
		String time = null;
		try {
			DateFormat myformat = new SimpleDateFormat("HH:mm:ss");
			time = myformat.format(new Date());
		} catch (Exception e) {
			//System.out.println(e);
		}
		return time;
	}

	/**
	 * 返回当前时间,格式HH:mm:ss
	 * 
	 * @return String 当前时间
	 */
	public static String getCurrentDateTime() {
		String time = null;
		try {
			DateFormat myformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			time = myformat.format(new Date());
		} catch (Exception e) {
			//System.out.println(e);
		}
		return time;
	}

	
	public static String getCurrDateTime(String p) {
		String time = null;
		try {
			DateFormat myformat = new SimpleDateFormat(
					"yyyyMMddHHmmss");
			time = myformat.format(new Date());
		} catch (Exception e) {
			//System.out.println(e);
		}
		return time;
	}
	
	public static String getTodayStartTime() {
		Calendar cal = Calendar.getInstance();
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}
	
	public static String getYesterdayStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}
	
	
	/**
	 * 返回当前时间,格式HH:mm:ss
	 * 
	 * @return String 当前时间
	 */
	public static String getCurrDateTime() {
		String time = null;
		try {
			DateFormat myformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			time = myformat.format(new Date());
		} catch (Exception e) {
			//System.out.println(e);
		}
		return time;
	}

	public static String getCureDateTimeSSS() {
		String time = null;
		try {
			DateFormat myformat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
			time = myformat.format(new Date());
		} catch (Exception e) {
			//System.out.println(e);
		}
		return time;
	}

	/**
	 * 整理输入时间,分进刻,秒为0,格式HH:mm:ss
	 * 
	 * @return String 当前时间
	 */
	public static String trimTime(String time) {
		String ret = new String(time);
		try {
			int h = getHour(time);
			int m = getMinute(time);
			int a = (int) Math.round(m * 1.0 / 15.0) * 15;
			ret = timeJoin(h, a * 1, 0);
		} catch (Exception e) {
			//System.out.println(e);
		}
		return ret;
	}

	/**
	 * 返回当前年月日期,格式yyyy-MM-dd
	 * 
	 * @return String 年月日期
	 */
	public static String getCurrentDate() {
		String date = null;
		try {
			DateFormat myformat = new SimpleDateFormat("yyyy-MM-dd");
			date = myformat.format(new Date());
		} catch (Exception e) {
			//System.out.println(e);
		}
		return date;
	}

	/*
	 * 取昨天日期 返回8位字符串
	 */
	static public String getYestoday() {
		Date nowdate = new Date();
		long oneday = 24 * 60 * 60 * 1000;
		long now = nowdate.getTime();
		long yestoday = now - oneday;
		Date yesdate = new Date(yestoday);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(yesdate);
	}

	/**
	 * 返回当前年 格式YYYY
	 * 
	 * @return String 年
	 */
	public static String getCurrentYear() {
		String year = null;
		try {
			DateFormat myformat = new SimpleDateFormat("yyyy-MM-dd");
			String date = myformat.format(new Date());
			year = date.substring(0, 4);
		} catch (Exception e) {
			//System.out.println(e);
		}
		return year;
	}

	/**
	 * 返回日期的年
	 * 
	 * @param dateStr
	 *            输入的字符串 格式YYYY-MM-DD
	 * @return int 年
	 */
	public static int getYear(String dateStr) {
		if (dateStr == null || dateStr.length() < 4)
			return 0;
		String year = dateStr.substring(0, 4);
		return parseInt(year);
	}

	/**
	 * 返回日期的月
	 * 
	 * @param dateStr
	 *            输入的字符串 格式YYYY-MM-DD
	 * @return int 月
	 */
	public static int getMonth(String dateStr) {
		if (dateStr == null || dateStr.length() < 7)
			return 0;
		String month = dateStr.substring(5, 7);
		return parseInt(month);
	}

	/**
	 * 返回日期的月
	 * 
	 * @param dateStr
	 *            输入的字符串 格式YYYY-MM-DD
	 * @return int 月
	 */
	public static int getDay(String dateStr) {
		if (dateStr == null || dateStr.length() < 10)
			return 0;
		String day = dateStr.substring(8, 10);
		return parseInt(day);
	}

	/**
	 * 返回时间的小时
	 * 
	 * @param timeStr
	 *            输入的字符串 格式HH:mm:ss
	 * @return int 小时
	 */
	public static int getHour(String timeStr) {
		if (timeStr == null || timeStr.length() < 2)
			return 0;
		String hour = timeStr.substring(0, 2);
		return parseInt(hour);
	}

	/**
	 * 返回时间的分钟
	 * 
	 * @param timeStr
	 *            输入的字符串 格式HH:mm:ss
	 * @return int 分钟
	 */
	public static int getMinute(String timeStr) {
		if (timeStr == null || timeStr.length() < 5)
			return 0;
		String minute = timeStr.substring(3, 5);
		return parseInt(minute);
	}

	/**
	 * 返回时间的分钟
	 * 
	 * @param timeStr
	 *            输入的字符串 格式HH:mm:ss
	 * @return int 分钟
	 */
	public static int getSecond(String timeStr) {
		if (timeStr == null || timeStr.length() < 8)
			return 0;
		String second = timeStr.substring(6, 8);
		return parseInt(second);
	}

	/**
	 * 返回两个Date对象的之间的分钟数
	 * 
	 * @param Date
	 *            date1 作为被减数的Date对象
	 * @param Date
	 *            date2 作为减数的Date对象
	 * @return int 两个Date对象的之间的分钟数
	 */
	public static int getMinutesTween(Date date1, Date date2) {
		long mill1 = date1.getTime();
		long mill2 = date2.getTime();
		return (int) ((mill1 - mill2) / (1000 * 60));
	}

	public static int getMinutesTween(String date1, String date2) {
		Calendar cal1 = getTime(formatDate(date1, "yyyy-MM-dd HH:mm:ss"));
		Calendar cal2 = getTime(formatDate(date2, "yyyy-MM-dd HH:mm:ss"));
		Date d1 = cal1.getTime();
		Date d2 = cal2.getTime();
		return getMinutesTween(d1, d2);
	}

	/**
	 * 返回两个Date对象的之间的小时数
	 * 
	 * @param Date
	 *            date1 作为被减数的Date对象
	 * @param Date
	 *            date2 作为减数的Date对象
	 * @return int 两个Date对象的之间的小时数
	 */
	public static int getHoursTween(Date date1, Date date2) {
		long mill1 = date1.getTime();
		long mill2 = date2.getTime();
		return (int) ((mill1 - mill2) / (1000 * 60 * 60));
	}

	/**
	 * 返回两个string(格式是：yyyy-MM-dd HH:mm:ss)的之间的小时数
	 * 
	 * @param String
	 *            date1 作为被减数的String对象
	 * @param String
	 *            date2 作为减数的String对象
	 * @return int 两个Date对象的之间的小时数
	 */
	public static int getHoursTween(String date1, String date2) {
		Calendar cal1 = getTime(formatDate(date1, "yyyy-MM-dd HH:mm:ss"));
		Calendar cal2 = getTime(formatDate(date2, "yyyy-MM-dd HH:mm:ss"));
		Date d1 = cal1.getTime();
		Date d2 = cal2.getTime();
		return getHoursTween(d1, d2);
	}

	/**
	 * 返回两个Date对象的之间的秒数
	 * 
	 * @param Date
	 *            date1 作为被减数的Date对象
	 * @param Date
	 *            date2 作为减数的Date对象
	 * @return int 两个Date对象的之间的秒数
	 */
	public static int getSecondTween(Date date1, Date date2) {
		long mill1 = date1.getTime();
		long mill2 = date2.getTime();
		return (int) ((mill1 - mill2) / (1000));
	}

	/**
	 * 返回两个string(格式是：yyyy-MM-dd HH:mm:ss)的之间的秒数
	 * 
	 * @param String
	 *            date1 作为被减数的String对象
	 * @param String
	 *            date2 作为减数的String对象
	 * @return int 两个Date对象的之间的秒数
	 */
	public static int getSecondTween(String date1, String date2) {
		Calendar cal1 = getTime(formatDate(date1, "yyyy-MM-dd HH:mm:ss"));
		Calendar cal2 = getTime(formatDate(date2, "yyyy-MM-dd HH:mm:ss"));
		Date d1 = cal1.getTime();
		Date d2 = cal2.getTime();
		return getSecondTween(d1, d2);
	}

	/**
	 * 返回两个Date对象的之间的天数
	 * 
	 * @param Date
	 *            date1 作为被减数的Date对象
	 * @param Date
	 *            date2 作为减数的Date对象
	 * @return int 两个Date对象的之间的天数
	 */
	public static int getDaysTween(Date date1, Date date2) {
		// return toJulian(date1) - toJulian(date2);
		long mill1 = date1.getTime();
		long mill2 = date2.getTime();
		return (int) ((mill1 - mill2) / (1000 * 60 * 60 * 24));
	}

	/**
	 * 返回两个String(格式是：yyyy-MM-dd HH:mm:ss)对象的之间的天数
	 * 
	 * @param String
	 *            date1 作为被减数的String对象
	 * @param String
	 *            date2 作为减数的String对象
	 * @return int 两个String对象的之间的天数
	 */
	public static int getDaysTween(String date1, String date2) {
		String s1 = date1;
		String s2 = date2;
		s1 = formatDate(s1, "yyyy-MM-dd");
		s2 = formatDate(s2, "yyyy-MM-dd");
		if (s1.length() < 11) {
			s1 = s1 + " 00:00:00";
		}
		if (s2.length() < 11) {
			s2 = s2 + " 00:00:00";
		}
		Calendar cal1 = getTime(formatDate(s1, "yyyy-MM-dd HH:mm:ss"));
		Calendar cal2 = getTime(formatDate(s2, "yyyy-MM-dd HH:mm:ss"));
		Date d1 = cal1.getTime();
		Date d2 = cal2.getTime();
		return getDaysTween(d1, d2);
	}

	public static int getMonthsTween(String date1, String date2) {
		int y1 = getYear(date1);
		int m1 = getMonth(date1);
		int y2 = getYear(date2);
		int m2 = getMonth(date2);
		int m = 0;
		m += (y1 - y2) * 12;
		m += (m1 - m2);
		return m;
	}

	/**
	 * 取日期在周中的位置，周一为1，周日为7
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(String date) {
		int year = CommUtil.getYear(date);
		int month = CommUtil.getMonth(date);
		int day = CommUtil.getDay(date);

		// 取出该周起止日期.
		GregorianCalendar cal = new GregorianCalendar(year, month - 1, day);
		int dw = cal.get(GregorianCalendar.DAY_OF_WEEK);
		if (dw == GregorianCalendar.SUNDAY) {
			return 7;
		}
		return dw - 1;
	}

	/*********
	 * 判断日期月
	 * 
	 * @date 2017年6月5日
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isMonth2Month(String date1, String date2) {
		int y1 = getYear(date1);
		int m1 = getMonth(date1);
		int y2 = getYear(date2);
		int m2 = getMonth(date2);
		boolean r = false;
		int d1 = y1 * 12 + m1;
		int d2 = y2 * 12 + m2;
		if (d1 >= d2) {
			r = true;
		}
		return r;

	}

	public static Date parseDate(String str,String pattern){

		try {
			SimpleDateFormat sdf=new SimpleDateFormat(pattern);
			return sdf.parse(str);
		}catch (Exception e){
			logger.error("日期格式转换异常",e);
		}
		return null;
	}

	/**
	 * 取某年月的最大天数
	 * 
	 * @param 年
	 * @param 月
	 * @return
	 */
	public static int getMaxDay(int y, int m) {
		int[] md = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (m > 0 && m < 13) {
			if (m != 2) {
				return md[m - 1];
			} else if (isLeapYear(y)) {
				return md[1] + 1;
			} else {
				return md[1];
			}
		}
		return 0;
	}

	public static boolean isLeapYear(int y) {
		if (y % 4 != 0)
			return false;
		if (y % 100 == 0) {
			return (y % 1000 == 0);
		}
		return true;
	}

	/**
	 * 将GB2312字符串转成ISO8859-1字符串
	 * 
	 * @param String
	 *            GB2312字符串
	 * @return String ISO8859-1字符串
	 */
	public static String getUTF4GB(String str) {
		if (str == null)
			return null;
		try {
			return new String(str.getBytes("GB2312"), "ISO8859_1");
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * 将ISO8859-1字符串转成GBK字符串
	 * 
	 * @param String
	 *            ISO8859-1字符串
	 * @return String GBK字符串
	 */
	public static String getGBStr(String str) {
		if (str == null)
			return null;
		try {
			return new String(str.getBytes("ISO8859_1"), "GBK");
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * 将Date数据类型转换为特定的格式, 如格式为null, 则使用缺省格式yyyy-MM-dd.
	 * 
	 * @param Date
	 *            day 日期
	 * @param String
	 *            toPattern 要转换成的日期格式
	 * @return String 返回日期字符串
	 */
	public static String formatDate(Date day, String toPattern) {
		String date = null;
		if (day != null) {
			try {
				SimpleDateFormat formatter = null;
				if (toPattern != null)
					formatter = new SimpleDateFormat(toPattern);
				else
					formatter = new SimpleDateFormat("yyyy-MM-dd");
				date = formatter.format(day);
			} catch (Exception e) {
				//System.out.println(e);
				return null;
			}
			return date;
		} else
			return null;
	}

	public static String formatDate(long time, String toPattern) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(time);
		Date date = cal.getTime();
		return formatDate(date, toPattern);
	}

	/**
	 * 将原有的日期格式的字符串转换为特定的格式, 如(原有和转换)格式为null, 则使用缺省格式yyyy-MM-dd.
	 * 
	 * @param String
	 *            value 日期格式的字符串
	 * @param String
	 *            fromPattern 原有的日期格式
	 * @param String
	 *            toPattern 转换成的日期格式
	 * @return String 返回日期字符串
	 */
	public static String formatDate(String value, String fromPattern,
			String toPattern) {
		String date = null;
		if (toPattern == null)
			toPattern = "yyyy-MM-dd";
		if (value != null) {
			try {
				SimpleDateFormat formatter = null;
				if (fromPattern != null)
					formatter = new SimpleDateFormat(fromPattern);
				else
					formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date day = formatter.parse(value);
				formatter.applyPattern(toPattern);
				date = formatter.format(day);
			} catch (Exception e) {
				// logger.error(e);
				return value;
			}
			return date;
		} else
			return null;
	}

	/**
	 * 将原有的日期格式的字符串转换为特定的格式, 原有格式为yyyy-MM-dd.
	 * 
	 * @param String
	 *            value 日期格式的字符串
	 * @param String
	 *            toPattern 转换成的日期格式
	 * @return String 返回日期字符串
	 */
	public static String formatDate(String value, String toPattern) {
		return formatDate(value, toPattern, toPattern);
	}

	/**
	 * 判断是否合法日期 允许2005-1-1，2005-01-01
	 * 
	 * @param value
	 * @return
	 */
	public static boolean validDate(String value) {
		if (isBlank(value)) {
			return false;
		}
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date day = formatter.parse(value);
			String date = formatter.format(day);
			if (value.equals(date)) {
				return true;
			}
			int idx1 = value.indexOf("-");
			int idx2 = value.indexOf("-", idx1 + 1);
			// //System.out.println("idx1="+idx1);
			// //System.out.println("idx2="+idx2);
			if (idx1 < 0 || idx2 < 0 || idx2 <= idx1) {
				return false;
			}
			String y = value.substring(0, idx1);
			String m = value.substring(idx1 + 1, idx2);
			String d = value.substring(idx2 + 1);
			// //System.out.println("y="+y);
			// //System.out.println("m="+m);
			// //System.out.println("d="+d);
			if (parseInt(y) == getYear(date) && parseInt(m) == getMonth(date)
					&& parseInt(d) == getDay(date)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断是否合法日期
	 * 
	 * @param value
	 * @param 格式
	 *            ，如"yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static boolean validDate(String value, String fmt) {
		if (isBlank(value)) {
			return false;
		}
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(fmt);
			Date day = formatter.parse(value);
			String date = formatter.format(day);
			// //System.out.println("date="+date);
			if (value.equals(date)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 格式化数字字符串为特定格式的字符串.
	 * 
	 * @param String
	 *            s 原有的数字字符串
	 * @param int dot 后跟小数点位数
	 * @return String 返回特定格式的字符串
	 */
	public static String formatNumber(String s, int dot) {
		double d;
		if (s == null)
			return "";
		try {
			d = Double.parseDouble(s);
		} catch (NumberFormatException e) {
			// logger.error(e);
			return s;
		}
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(dot);
		nf.setMaximumFractionDigits(dot);
		return nf.format(d);
	}

	/**
	 * 格式化double类型为特定格式的字符串.
	 * 
	 * @param double d 双精度数字
	 * @param int dot 后跟小数点位数
	 * @return String 返回特定格式的字符串
	 */
	public static String formatNumber(double d, int dot) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(dot);
		nf.setMaximumFractionDigits(dot);
		return nf.format(d);
	}

	/**
	 * 格式化double类型为特定格式的.不带千位分隔符
	 * 
	 * @param double d 双精度数字
	 * @param int dot 后跟小数点位数
	 * @return String 返回特定格式的字符串
	 */
	public static String formatDecimal(double d, int dot) {
		if ("NaN".equals(Double.toString(d))) {
			return "NaN";
		}
		return new BigDecimal(Double.toString(d)).setScale(dot,
				BigDecimal.ROUND_HALF_UP).toString();
		/*
		 * caojie 此算法有问题 CommUtil.formatMoney("4.125") 结果为 4.12
		 * 
		 * NumberFormat nf = NumberFormat.getInstance(); DecimalFormat df =
		 * (DecimalFormat) nf; df.setMinimumFractionDigits(dot);
		 * df.setMaximumFractionDigits(dot); String pattern = "#0"; if (dot>0) {
		 * pattern += "."; for (int i=0; i<dot; i++) { pattern += "0"; } }
		 * df.applyPattern(pattern); df.setDecimalSeparatorAlwaysShown(false);
		 * return df.format(d);
		 */
	}

	/**
	 * 格式化金额
	 */
	public static String formatMoney(double d) {
		return formatDecimal(d, 2);
	}

	/**
	 * 智能格式化 自动判断小数点后位数和是否万、百万、亿进位
	 * 
	 * @param d
	 * @return
	 */
	public static String formatMoney(String s) {
		String ret = null;
		double d = CommUtil.parseDouble(s);
		int i = (int) d;
		if (CommUtil.isZero(d - i * 1.0)) {
			if (i < 100) {
				return CommUtil.formatNumber(d, 0);
			}
			int[] m = { 100000000, // 亿
					10000000, // 千万
					1000000, // 百万
					10000, // 万
					1000, // 千
					100 // 百
			};
			boolean[] mf = { true, // 亿
					false, // 千万
					false, // 百万
					true, // 万
					false, // 千
					false // 百
			};
			String[] md = { "亿", "000万", "00万", "万", "千", "百" };
			for (int t = 0; t < m.length; t++) {
				if (i % m[t] == 0 && (mf[t] || i / m[t] < 10)) {
					return CommUtil.formatNumber(i / m[t], 0) + md[t];
				}
			}
			ret = CommUtil.formatNumber(d, 0);
		} else {
			ret = CommUtil.formatNumber(d, 2);
		}
		return ret;
	}

	/**
	 * 格式化利率
	 */
	public static String formatIntRate(double d) {
		return formatDecimal(d, 8);
	}

	/**
	 * 格式化double类型为特定格式的字符串.
	 * 
	 * @param double d 双精度数字
	 * @param int dot 后跟小数点位数
	 * @param int type 0-不进行转换,1-百分数计算,2-除10000
	 * @return String 返回特定格式的字符串
	 */
	public static String formatNumber(String s, int dot, int type) {
		if (type == 0)
			return formatNumber(s, dot);
		if (type == 1) {
			double d;
			try {
				d = Double.parseDouble(s);
			} catch (NumberFormatException e) {
				// logger.error(e);
				return s;
			}
			d = d * 100;
			return formatNumber(d, dot);

		}
		if (type == 2) {
			double d;
			try {
				d = Double.parseDouble(s);
			} catch (NumberFormatException e) {
				// logger.error(e);
				return s;
			}
			d = d / 10000;
			return formatNumber(d, dot);

		}
		return s;
	}

	/**
	 * 格式化String为double型，如果字符串为空，返回0.
	 * 
	 * @param String
	 *            s 双精度数字
	 * @return double 返回double类型数值
	 */
	public static double parseDouble(String s) {
		double d = 0;
		if (s != null) {
			try {
				d = Double.parseDouble(s);
			} catch (NumberFormatException e) {
				try {
					String t = s.replaceAll(",", "");
					d = Double.parseDouble(t);
				} catch (NumberFormatException e2) {
					d = 0;
					// logger.error(e.getMessage());
				}
			}
		}
		return d;
	}

	/**
	 * 格式化String为float型，如果字符串为空，返回0.
	 * 
	 * @param String
	 *            s 双精度数字
	 * @return float 返回float类型数值
	 */
	public static float parseFloat(String s) {
		float f = 0;
		if (s != null) {
			try {
				f = Float.parseFloat(s);
			} catch (NumberFormatException e) {
				f = 0;
				// logger.error(e.getMessage());
			}
		}
		return f;
	}

	/**
	 * 格式化String为int型，如果字符串为空，返回0.
	 * 
	 * @param String
	 *            s 双精度数字
	 * @return int 返回int类型数值
	 */
	public static int parseInt(String s) {
		int i = 0;
		if (s != null) {
			try {
				i = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				i = 0;
				double t = parseDouble(s);
				if (t != 0) {
					i = (int) t;
				}

				// logger.error(e.getMessage());
			}
		}
		return i;
	}

	/**
	 * 取double的整数部分
	 * 
	 * @param d
	 * @return
	 */
	public static int getIntPart(double d) {
		String s = Double.toString(d);
		int i = s.indexOf(".");
		if (i < 0) {
			return parseInt(s);
		}
		return parseInt(s.substring(0, i));
	}

	public static String moveDotRight(String s, int step) {
		int idx_dot = s.indexOf(".");
		String s1 = "", s2 = "";
		if (idx_dot > -1) {
			s1 = s.substring(0, idx_dot);
			s2 = s.substring(idx_dot + 1);
		} else {
			s1 = s;
		}
		int len = s2.length();
		for (int i = len; i < step; i++) {
			s2 += "0";
		}
		String ret = s1 + s2.substring(0, step);
		ret = parseInt(ret) + "";
		if (s2.length() > step) {
			ret += "." + s2.substring(step);
		}
		return ret;
	}

	/**
	 * 格式化数字String，把其中的逗号去掉，如果字符串为空，返回0.
	 * 
	 * @param String
	 *            s 双精度数字
	 * @return String 返回新的格式字符串
	 */
	public static String StringConvertor(String s) {
		StringBuffer bf = new StringBuffer();
		if (s != null) {
			for (int i = 0; i < s.length(); i++) {
				if (s.charAt(i) == ',')
					continue;
				else
					bf.append(s.charAt(i));
			}
		}
		return bf.toString();
	}

	/**
	 * 控制得到的字符串不为空
	 * 
	 * @param s
	 * @return
	 */
	public static String getNotNull(String s) {
		if (s == null || s.trim().equals(""))
			return "";
		return s;
	}

	/**
	 * 控制得到的字符串的显示
	 * 
	 * @param s
	 * @return
	 */
	public static String formatNull(String s) {
		if (s == null || s.trim().equals(""))
			return "/";
		return s;
	}

	/**
	 * 起始日期加上天数
	 * 
	 * @param date
	 * @param d
	 * @return
	 */
	public static String addDate(String date, int d) {
		String ret = null;
		if (date.length() < 10)
			return null;
		try {

			Calendar e = getTime(date, "yyyy-MM-dd");

			e.add(Calendar.DATE, d);

			Date endDate = e.getTime();

			ret = formatDate(endDate, "yyyy-MM-dd");
		} catch (Exception ex) {
			//System.out.println("not date format");
		}
		return ret;
	}

	/**
	 * 起始日期加上月数
	 * 
	 * @param date
	 * @param m
	 * @return
	 */
	public static String addMonth(String date, int m) {
		String ret = null;
		if (date.length() < 10)
			return null;
		try {

			Calendar e = getTime(date, "yyyy-MM-dd");

			e.add(Calendar.MONTH, m);

			Date endDate = e.getTime();

			ret = formatDate(endDate, "yyyy-MM-dd");
		} catch (Exception ex) {
			//System.out.println("not date format");
		}
		return ret;
	}

	/**
	 * 起始日期加上年数
	 * 
	 * @param date
	 * @param y
	 * @return
	 */
	public static String addYear(String date, int y) {
		String ret = null;
		if (date == null || date.length() < 10)
			return null;
		try {

			Calendar e = getTime(date, "yyyy-MM-dd");

			e.add(Calendar.YEAR, y);

			Date endDate = e.getTime();

			ret = formatDate(endDate, "yyyy-MM-dd");
		} catch (Exception ex) {
			//System.out.println("not date format");
		}
		return ret;
	}

	/**
	 * 起始时间加上分钟
	 * 
	 * @param date
	 * @param y
	 * @return
	 */
	public static String addMin(String dt, int y) {
		String ret = null;
		if (dt == null || dt.length() < 10)
			return null;
		try {
			Calendar e = getTime(dt);
			e.add(Calendar.MINUTE, y);
			Date endDate = e.getTime();
			ret = formatDate(endDate, "yyyy-MM-dd HH:mm:ss");
		} catch (Exception ex) {
			//System.out.println("not date format");
		}
		return ret;
	}

	/**
	 * 处理页面的显示,将\n,转为<br>
	 * ,如果长度超过50个字符,添加一个<br>
	 * 
	 * @param str
	 * @return
	 */
	public static String getHtmlStr(String str) {
		// 去除字符串中的html标签
		if (isBlank(str))
			return "";

		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\r\n", "<br>");
		str = str.replaceAll("\n", "<br>");
		str = str.replaceAll("\r", "<br>");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll(" ", "&nbsp;");

		return str;
	}

	/**
	 * 得到utf格式的字符串,可应用于下载文件时,对文件名的处理
	 */
	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					//System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 补全时间字符串。 时间输入省略了分或分秒时，补齐零； 例： tmFill("01:00"); // 返回值为"01:23:00"
	 * 
	 * @param timeStr
	 *            时间串，格式为HH:mm:ss
	 * @return
	 */
	public static String tmFill(String timeStr) {

		String t = timeStr;
		if (CommUtil.isBlank(t)) {
			t = "00:00:00";
		} else if (t.length() <= 2) { // hh
			t = t + ":00:00";
		} else if (t.length() <= 5) { // HH:mm
			t = t + ":00";
		}
		return t;
	}

	/**
	 * 
	 * @param d
	 * @return
	 */
	public static boolean isZero(double d) {
		if (Math.abs(d) < 0.0000000001)
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @param d
	 * @return
	 */
	public static double getAmtPart(double d, double part, int k) {
		double a = d * part / k;
		Double c = new Double(a);
		int b = c.intValue();
		a = 1.00 * b * k;
		return a;
	}

	/**
	 * 格式化字段
	 * 
	 * @param validPat
	 * @param value
	 * @return 是否被格式化.
	 */
	public static String formatValue(String validPat, String value) {

		if (validPat.equals("currencyPat")) { // 金额
			value = CommUtil.formatNumber((String) value, 2);

		} else if (validPat.equals("shortStrPat")) {// 短字符串
			if (value.length() > 10) {
				value = value.substring(0, 10);
				value = value + "...";
			}

		} else if (validPat.equals("exRatePat")) { // 外汇牌价
			value = CommUtil.formatNumber((String) value, 4);

		} else if (validPat.equals("10kPat")) { // 万元
			value = CommUtil.formatNumber((String) value, 2, 2) + "万";

		} else if (validPat.equals("numberPat")) { // 整数
			value = CommUtil.formatNumber((String) value, 0, 0);
		} else if (validPat.equals("currIntPat")) { // 无小数金额
			value = CommUtil.formatNumber((String) value, 0);

		} else if (validPat.equals("ratePat")) { // %
			value = CommUtil.formatNumber((String) value, 4, 1) + "%";
		} else if (validPat.equals("ratePat1")) { // %
			value = CommUtil.formatNumber((String) value, 4, 1) + "";
		} else if (validPat.equals("rate0Pat")) { // %
			value = CommUtil.formatNumber((String) value, 0, 1) + "%";

		} else if (validPat.equals("rate2Pat")) { // %
			value = CommUtil.formatNumber((String) value, 2, 1) + "%";

		} else if (validPat.equals("ratefPat")) { // %
			value = CommUtil.formatNumber((String) value, 4, 1) + "%";
		} else if (validPat.equals("rate2fPat")) { // %
			value = CommUtil.formatNumber((String) value, 2, 1) + "%";
		} else if (validPat.equals("upRatePat")) { // %
			if (parseDouble(value) >= 10) {
				value = "不封顶";
			} else {

				value = CommUtil.formatNumber((String) value, 4, 1) + "%";
			}
		} else if (validPat.equals("monthPat")) { // 月
			int t = CommUtil.parseInt((String) value);
			int y = t / 12;
			int m = t % 12;
			if (m <= 0) {
				value = y + "年";
			} else if (y <= 0) {
				if (m == 6)
					value = "半年";
				else
					value = m + "个月";
			} else {
				if (m == 6)
					value = y + "年半";
				else
					value = y + "年零" + m + "个月";
			}
		} else if (validPat.equals("datePat")) { // 日期
			value = CommUtil.formatDate((String) value, "yyyy-MM-dd",
					"yyyy年M月d日");

		} else if (validPat.equals("dateInputPat")) { // 日期
			value = CommUtil.formatDate((String) value, "yyyy-MM-dd");
		} else if (validPat.equals("datetimePat")) { // 日期时间
			String date = getDateOfDT(value);
			String time = getTimeOfDT(value);
			if (notBlank(date)) {
				date = formatDate((String) date, "yyyy-MM-dd", "yyyy年M月d日");
				if (notBlank(time)) {
					value = date + " " + time;
				} else {
					value = date;
				}
			}
		} else if (validPat.equals("shortDatePat")) {
			String date = getDateOfDT(value);
			if (notBlank(date)) {
				date = formatDate((String) date, "yyyy-MM-dd", "MM-dd");
				value = date;
			}
		} else if (validPat.equals("shortDateTimePat")) {
			String date = getDateOfDT(value);
			String time = getTimeOfDT(value);
			if (time.length() == 8) {
				time = time.substring(0, 5);
			}
			if (notBlank(date)) {
				date = formatDate((String) date, "yyyy-MM-dd", "MM-dd");
				if (notBlank(time)) {
					value = date + " " + time;
				} else {
					value = date;
				}
			}
		} else if (validPat.equals("yearMonthPat")) {
			value = CommUtil.formatDate((String) value, "yyyyMM", "yyyy年M月");

		} else if (validPat.equals("jdPat")) {
			String year = (String) value.substring(0, 4);
			int month = parseInt((String) value.substring(4));
			if (month <= 3)
				value = year + "年一季度";
			else if (month > 3 && month <= 6)
				value = year + "年二季度";
			else if (month > 6 && month <= 9)
				value = year + "年三季度";
			else if (month > 9)
				value = year + "年四季度";

		} else if (validPat.equals("cebTerm")) {
			if (value.length() == 3) {
				if ("000".equals(value)) {
					value = "自定义";
				} else if (value.startsWith("0")) {
					value = CommUtil.parseInt(value.substring(1)) + "年";
				} else if (value.startsWith("1")) {
					value = CommUtil.parseInt(value.substring(1)) + "个月";
				} else if (value.startsWith("2")) {
					value = CommUtil.parseInt(value.substring(1)) + "天";
				}
			} else if (value.length() == 2) {
				value = CommUtil.parseInt(value) + "个月";
			}
		}
		// //System.out.println("validPat="+validPat+"value="+value);
		return value;

	}

	/**
	 * 字符串替换
	 * 
	 * @param oldStr
	 *            原有的字符串
	 * @param subStr
	 *            被替换的字符串
	 * @param rStr
	 *            替换的字符串
	 * @return 返回替换后的字符串
	 */
	public static String replaceAll(String oldStr, String subStr, String rStr) {
		if (CommUtil.isBlank(oldStr)) {
			return "";
		}
		int i = oldStr.indexOf(subStr);
		int sl = subStr.length();
		String rsltStr = oldStr;
		int l = rStr.length() - subStr.length();
		while (i != -1) {
			String fStr = rsltStr.substring(0, i);
			String eStr = rsltStr.substring(i + sl, rsltStr.length());
			rsltStr = fStr + rStr + eStr;
			i = rsltStr.indexOf(subStr, i + l + 1);
			// i = rsltStr.indexOf(subStr);
		}
		return rsltStr;
	}

	/**
	 * 将18位身份证转为15位
	 * 
	 * @param oldStr
	 * @return
	 */
	public static String get15IDCard(String oldStr) {
		if (oldStr == null || oldStr.length() != 18) {
			return null;
		}
		String pre6 = oldStr.substring(0, 6);
		String ends = oldStr.substring(8, 17);
		return pre6 + ends;
	}

	/**
	 * 将15位身份证号转换为18位身份证号
	 * 
	 * @param 15位身份证号
	 * @param 世纪
	 * @return 返回替换后的字符串
	 */
	public static String get18IDCard(String oldStr, String cenStr) {
		if (oldStr == null || oldStr.length() != 15) {
			return null;
		}

		/*
		 * 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。
		 * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码
		 * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码
		 * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 4、顺序码
		 * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码
		 * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
		 * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5
		 * 8 4 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10
		 * 校验码: 1 0 X 9 8 7 6 5 4 3 2
		 */
		if (CommUtil.isBlank(cenStr)) {
			cenStr = "19";
		}
		String newStr = oldStr.substring(0, 6) + cenStr + oldStr.substring(6);
		int w[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		int s = 0;
		for (int i = 0; i < 17; i++) {
			String a = newStr.substring(i, i + 1);
			s = s + w[i] * CommUtil.parseInt(a);
		}
		int y = s % 11;
		String m[] = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		newStr = newStr + m[y];

		return newStr;
	}

	/**
	 * 产生符合正态分布（高斯分布）的随机数
	 * 
	 * @param 均值
	 * @param 标准差
	 * @return 返回随机数
	 */
	public static double gaussianRNG(double mu, double sigma) {

		if (sigma <= 0.0) {
			return mu;
		}

		/*
		 * 理论证明12个[0,1]间的均匀分布之和标准化后能很好的近似一个标准正态随机变量.
		 */
		/*
		 * double r,sum=0.0;
		 * 
		 * for ( int i=1; i<=12; i++ ) { sum = sum + Math.random(); } r =
		 * (sum-6.00) * sigma + mu;
		 * 
		 * return r;
		 */

		/*
		 * Box 和 Muller 在 1958 年给出了由均匀分布的随机变量生成正态分布的随机变量的算法。 设 U1, U2 是区间 (0, 1)
		 * 上均匀分布的随机变量，且相互独立。 X1 = sqrt(-2*log(U1)) * cos(2*PI*U2); X2 =
		 * sqrt(-2*log(U1)) * sin(2*PI*U2);
		 */

		double r1 = Math.random();
		double r2 = Math.random();

		return Math.sqrt(-2 * Math.log(r1)) * Math.cos(2 * Math.PI * r2)
				* sigma + mu;

	}

	public static void fileCopy(File sourceFile, File targetFile)
			throws IOException {
		FileInputStream input = new FileInputStream(sourceFile);
		FileOutputStream output = new FileOutputStream(targetFile);
		byte[] inputByte = new byte[8192];
		// BufferedReader aReader = new BufferedReader(new
		// FileReader(sourceFile));
		while (input.read(inputByte) != -1) {
			output.write(inputByte);
		}
		output.flush();
		input.close();
		output.close();
	}

	static public byte[] readFromStream(InputStream ins) throws IOException {
		if (ins == null) {
			//System.out.println("readFromStream is null!");
			return null;
		}
		BufferedInputStream in = null;
		ByteArrayOutputStream bout = null;
		try {
			in = new BufferedInputStream(ins);
			bout = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = in.read(b)) != -1) {
				bout.write(b, 0, len);
			}
		} catch (EOFException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
		return bout.toByteArray();
	}

	public static boolean nowInOpenTime(String sysStartTime, String sysCloseTime) {
		String currTime = getCurrentTime();

		if (!isMinutePat(sysStartTime)) {
			sysStartTime = "07:00:00";
		} else {
			sysStartTime = sysStartTime + ":00";
		}

		if (!isMinutePat(sysCloseTime)) {
			sysCloseTime = "07:00:00";
		} else {
			sysCloseTime = sysCloseTime + ":00";
		}
		int i = CommUtil.getSecondTween(
				"2005-01-01 " + CommUtil.formatDate(currTime, "HH:mm:ss"),
				"2005-01-01 " + sysStartTime);
		if (i >= 0) {
			int j = CommUtil.getSecondTween(
					"2005-01-01 " + CommUtil.formatDate(currTime, "HH:mm:ss"),
					"2005-01-01 " + sysCloseTime);
			if (j < 0) {
				return true;
			}
		}
		return false;
	}

	static boolean isMinutePat(String minStr) {
		if (minStr == null || minStr.trim().length() != 5) {
			return false;
		} else {
			try {
				if (Integer.parseInt(minStr.substring(0, 2)) >= 0
						&& Integer.parseInt(minStr.substring(0, 2)) <= 23) {
					if (minStr.substring(2, 3).equals(":")) {
						if (Integer.parseInt(minStr.substring(3, 5)) >= 0
								&& Integer.parseInt(minStr.substring(3, 5)) < 60) {
							return true;
						}
					}
				}
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	}

	/*
	 * 十六进制字符串转为byte数组 如3132转换为12
	 */
	public static byte[] hexStr2byte(String hex) {
		byte[] b = hex.getBytes();
		if ((b.length % 2) != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	public static String md5(String input) {
		try {
			MessageDigest alg = MessageDigest.getInstance("MD5"); // or "SHA-1"
			alg.update(input.getBytes());
			byte[] digest = alg.digest();
			String sout = byte2hex(digest);
			if (sout != null && sout.length() > 100) {
				sout = sout.substring(0, 100);
			}
			return sout;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 掩盖字符串部分内容
	 * 
	 * @param 原字符串
	 * @param 掩盖方法
	 *            ：0全部 1从开头 2从结尾
	 * @param 开始位置
	 * @param 掩盖长度
	 * @param 是否处理中文
	 * @return
	 */
	public static String mask(String input, int method, int begin, int masklen,
			boolean chinese) {
		try {
			if (isBlank(input)) {
				return "";
			}
			byte[] a_b = null;
			String[] a_cn = null;
			int len = 0;
			if (chinese) {
				a_cn = cnstrBreak(input, 1);
				len = a_cn.length;
			} else {
				a_b = input.getBytes();
				len = a_b.length;
			}
			int i_begin = 0;
			int i_end = len;
			if (method == 1) {
				i_begin = begin;
				i_end = begin + masklen;
			} else if (method == 2) {
				i_begin = len - begin - masklen;
				i_end = len - begin;
			}
			for (int i = i_begin; i < len && i < i_end; i++) {
				if (i < 0) {
					continue;
				}
				if (chinese) {
					a_cn[i] = "*";
				} else {
					a_b[i] = '*';
				}
			}
			if (chinese) {
				return strJoin(a_cn, "");
			}
			return new String(a_b);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 返回UTF-8 的XML字符串
	 * 
	 */
/*
	public static String getXMLStr(Element root) {
		XMLOutputter outter = new XMLOutputter();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outter.output(new Document(root), outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String returnStr = "";
		try {
			returnStr = outputStream.toString("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return returnStr;
	}
*/

	/*
	 * 验证手机号码
	 */
	public static boolean isMobileNO(String mobile) {
		// Pattern p =
		// Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Pattern p = Pattern.compile("^1[0-9][0-9]{9}");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	/*
	 * 验证文件格式问xls
	 */
	public static boolean isXlsType(String s) {
		Pattern p = Pattern.compile("(.xls|.XLS)$");
		Matcher m = p.matcher(s);
		return m.matches();
	}

	/**
	 * 由于系统查询in 类型的sql出现配置输入参数type为in的时候
	 * 系统只会对数据加上括号。字符串则需要用单引号处理。故根据需要进行处理，调用该方法 传入的字符形式为a,b,c
	 * 生成的返回样式为'a','b','c' 如果传入的为空 则返回""字符串 如果传入的没有出现,分割 则直接返回原值
	 * 
	 * @param value
	 * @return
	 */
/*	public static String getSqlDot(String value) {
		if (isBlank(value)) {
			return "";
		}
		String[] pars = strBreak(value, StaticCode.DISPLAY_DELIMITER);

		StringBuffer returnValue = new StringBuffer("");

		for (int i = 0; pars != null && i < pars.length; i++) {
			String string = pars[i];
			if (i == 0) {
				returnValue.append("'");
				returnValue.append(string);
				returnValue.append("'");
			} else {
				returnValue.append(",'");
				returnValue.append(string);
				returnValue.append("'");
			}
		}

		return returnValue.toString();
	}*/

	/**
	 * 替换特殊字符
	 * 
	 * @param displayValue
	 * @return
	 */
	public static String replaceStr(String displayValue) {
		if (displayValue != null) {
			displayValue = displayValue.replaceAll("<", "&lt;");

			displayValue = displayValue.replaceAll(">", "&gt;");
		} else {
			displayValue = "";
		}
		return displayValue;
	}



	public static double getDoubleVal(Map<String, Object> datMap, String field) {
		double value = 0;
		String val = "";
		if(datMap.get(field) == null || datMap.get(field).toString().equals("null")){
			
		}else{
			val = ""+datMap.get(field);
		}
		String valueStr = "" + val;
		try {
			value = Double.parseDouble("".equals(valueStr) ? "0" : valueStr);// 取现额度
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return value;
	}

	public static String getObject2StrVal(Object o) {
		return o == null ? "" : o.toString();
	}

	public static String getMapStringValue(Map<String, String> dataMap,
			String fieldName) {

		return (dataMap.get(fieldName) == null || isBlank(dataMap
				.get(fieldName))) ? "0" : dataMap.get(fieldName);
	}

	public static Integer getIntegerVal(Map<String, Object> dataMap,
			String field) {
		return dataMap.get(field) == null || dataMap.get(field).toString().equals("") || dataMap.get(field).toString().equals("null") ? 0 : (Integer) dataMap.get(field);
	}
	
	public static Long getLongegerVal(Map<String, Object> dataMap,
			String field) {
		return dataMap.get(field) == null || dataMap.get(field).toString().equals("") || dataMap.get(field).toString().equals("null") ? 0l : Long.parseLong(dataMap.get(field).toString()) ;
	}

	public static Long getMapLongVal(Map<String, Object> dataMap, String field) {
		return dataMap.get(field) == null ? 0 : Long.parseLong((""+ dataMap.get(field)));
	}

	public static String getStringMap2Val(Map<String, String> dataMap,
			String fieldName) {
		return dataMap.get(fieldName) == null ? "" : dataMap.get(fieldName);
	}

	public static String getMapObject2Str(Map<String, Object> dataMap,
			String fieldName) {
		return dataMap.get(fieldName) == null
				|| dataMap.get(fieldName).toString().equals("null") ? ""
				: (String) dataMap.get(fieldName);
	}
	
	
	/******
	 * 字符串中获取数字
	 * @date 2017年9月26日
	 * @param str
	 * @return
	 */
	public static String getStringToNumber(String str){
		String regEx="[^0-9]";   
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(str);   
		return m.replaceAll("").trim();
	}
	
	/*********
	 * 
	* @Title: startsWith 
	* @Description: 判断字符串是否以某字符开头
	* @param @param startStr
	* @param @param str
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	public static boolean startsWith(String startStr,String str) {
		if(CommUtil.isBlank(str))return false;
		return str.startsWith(startStr);
	}
	
	//判断一个字符串是否是另一字符串的严格子字符串
	public static boolean strictChildString(String childStr,String str) {
		if(CommUtil.isBlank(str))return false;
		return !str.startsWith(childStr) && str.indexOf(childStr) != -1 && !str.endsWith(childStr);
		 
	}
	
	public static void main(String args[]){
		System.out.println(getYesterdayStartTime());
	}

}
