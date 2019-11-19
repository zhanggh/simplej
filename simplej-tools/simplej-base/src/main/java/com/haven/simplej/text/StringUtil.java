package com.haven.simplej.text;

import com.google.common.collect.Maps;
import com.haven.simplej.cache.CacheManager;
import com.vip.vjtools.vjkit.collection.MapUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by haven.zhang on 2019/1/3.
 */
public class StringUtil extends StringUtils {
	/**
	 * 转换为下划线
	 *
	 * @param camelCaseName
	 * @return
	 */
	public static String underscoreName(String camelCaseName) {

		StringBuilder result = new StringBuilder();
		if (camelCaseName != null && camelCaseName.length() > 0) {
			result.append(camelCaseName.substring(0, 1).toLowerCase());
			for (int i = 1; i < camelCaseName.length(); i++) {
				char ch = camelCaseName.charAt(i);
				if (Character.isUpperCase(ch)) {
					result.append("_");
					result.append(Character.toLowerCase(ch));
				} else {
					result.append(ch);
				}
			}
		}
		return result.toString();
	}

	/**
	 * 转换为驼峰
	 *
	 * @param underscoreName
	 * @return
	 */
	public static String camelCaseName(String underscoreName) {
		StringBuilder result = new StringBuilder();
		if (underscoreName != null && underscoreName.length() > 0) {
			boolean flag = false;
			for (int i = 0; i < underscoreName.length(); i++) {
				char ch = underscoreName.charAt(i);
				if ("_".charAt(0) == ch) {
					flag = true;
				} else {
					if (flag) {
						result.append(Character.toUpperCase(ch));
						flag = false;
					} else {
						result.append(ch);
					}
				}
			}
		}
		return result.toString();
	}

	/**
	 * 正则匹配
	 * @param srcString 原字符串
	 * @param regular 正则表达式
	 * @return boolean
	 */
	public static boolean match(String srcString, String regular) {
		Pattern p = CacheManager.getObject(regular);
		if (p == null) {
			p = Pattern.compile(regular);
			CacheManager.putLocal(regular, p);
		}
		Matcher m = p.matcher(srcString);
		return m.matches();
	}

	/**
	 * 整型转换成字节数组
	 * @param iSource 整数
	 * @return byte[]
	 */
	public static byte[] Int2ByteArray(int iSource) {
		byte[] bLocalArr = new byte[4];
		for (int i = 0; i < 4; i++) {
			bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
		}
		return bLocalArr;
	}

	/**
	 * 字节转换成int
	 * @param bRefArr 字节数组
	 * @return int
	 */
	public static int ByteArray2Int(byte[] bRefArr) {
		int iOutcome = 0;

		for (int i = 0; i < 4; ++i) {
			byte bLoop = bRefArr[i];
			iOutcome += (bLoop & 255) << 8 * i;
		}

		return iOutcome;
	}

	/**
	 * 将querystirng :key=value&key2=value2...转换成map
	 * @param queryString queryString
	 * @return Map
	 */
	public static Map<String, String> parse2Map(String queryString) {
		String[] keyValues = StringUtil.split(queryString, "&");
		if (keyValues == null) {
			return new HashMap<>();
		}
		Map<String, String> requestMap = Maps.newHashMap();
		for (String keyValue : keyValues) {
			String[] params = StringUtil.split(keyValue, "=");
			if (params != null && params.length == 2) {
				requestMap.put(params[0], params[1]);
			} else if (params != null && params.length == 1) {
				requestMap.put(params[0], StringUtil.EMPTY);
			}
		}
		return requestMap;
	}

	/**
	 * 把map 转换成queryString
	 * @param params 参数
	 * @return String
	 */
	public static String convert2QueryString(Map<String, String> params) {
		if (MapUtil.isEmpty(params)) {
			return StringUtil.EMPTY;
		}
		StringBuilder sb = new StringBuilder();
		params.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}


}
