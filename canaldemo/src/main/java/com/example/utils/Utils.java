package com.example.utils;

import com.alibaba.google.common.base.Joiner;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;

public class Utils {
	/**
	 * 检测参数是否合法，非法时抛出异常
	 *
	 * @param expression
	 *            为true时抛出异常
	 * @param errorMessageTemplate
	 * @param errorMessageArgs
	 */
	public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
		if (expression) {
			throw new IllegalArgumentException(String.format(errorMessageTemplate, errorMessageArgs));
		}
	}

	/**
	 * 非法状态检测
	 *
	 * @param expression
	 *            为true时抛出异常
	 * @param errorMessageTemplate
	 * @param errorMessageArgs
	 */
	public static void checkStatus(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
		if (expression) {
			throw new IllegalStateException(String.format(errorMessageTemplate, errorMessageArgs));
		}
	}

	/**
	 * 字符串首字母小写
	 *
	 * @param s
	 * @return
	 */
	public static String toLowerCaseFirstChar(String s) {
		if (s == null || s.trim().equals("")) {
			return s;
		}
		if (Character.isLowerCase(s.charAt(0))) {
			return s;
		} else {
			return new StringBuilder().append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
		}
	}

	public static void removeNull(Map<String, Object> map) {
		if (map == null) {
			return;
		}
		Iterator<Entry<String, Object>> ite = map.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<String, Object> key = ite.next();
			if (key.getValue() == null) {
				ite.remove();
			}
		}
	}

	public static ClassLoader getClassLoader() {
		ClassLoader ret = Thread.currentThread().getContextClassLoader();
		return ret != null ? ret : Utils.class.getClassLoader();
	}

	public static final String getLocalIp() throws Exception {
		String ipString = "";
		Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
			Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = (InetAddress) addresses.nextElement();
				if (ip != null && ip instanceof Inet4Address && !ip.getHostAddress().equals("127.0.0.1")) {
					return ip.getHostAddress();
				}
			}
		}
		return ipString;
	}

	/**
	 * 字节转字符串
	 * @param ascii
	 * @return
	 */
	public static String asciiToString(byte[] ascii) {
		StringBuilder builder = new StringBuilder();
		for(byte b : ascii) {
			builder.append((char)b);
		}
		return builder.toString();
	}

	/**
	 * 按照默认排序方式返回数组排序拼接字符串
	 * @param tables
	 * @return
	 */
	public static String sortArray(String[] array) {
		TreeSet<String> sortTables = new TreeSet<>();
		sortTables.addAll(Arrays.asList(array));
		return Joiner.on(",").join(sortTables);
	}

	/**
	 * 检查异常堆栈中是否指定异常类
	 * @param throwable
	 * @param cls
	 * @return
	 */
	public static boolean existThrowable(Throwable throwable, Class<? extends Throwable> cls) {
		if(throwable == null || cls == null) {
			return false;
		}
		while(throwable != null) {
			if(throwable.getClass().equals(cls)) {
				return true;
			}
			throwable = throwable.getCause();
		}
		return false;
	}

	/**
	 * 向现有url中添加参数，编码参数key和value，如果url存在参数则使用&拼接新增参数，否则使用?拼接参数
	 * @param url
	 * @param key
	 * @param value
	 * @return
	 */
	public static String addUrlParams(String url, String key, String value) {
		if(url == null) {
			return url;
		}
		if(url.indexOf("?") != -1) {
			url += "&";
		} else {
			url += "?";
		}
		try {
			url += URLEncoder.encode(key, "utf-8") + "=" + URLEncoder.encode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {}
		return url;
	}

	public static void main(String[] args) throws Exception {
//		System.err.println(getLocalIp());
		System.err.println(sortArray(new String[] {"qwe", "123aaa", "bbb", "ee_ee"}));

		String url = "http://127.0.0.1:8811/log/test";
		String url2 = addUrlParams(url, "tim==&&e", "12321!@#$%^&*=-98e");
		System.err.println(url2);
		System.err.println(addUrlParams(url2, "xx&dd==", "lkdjflkajf*&$+==dsf"));
	}
}
