package com.usee.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/*
 * 此util用于将某些关键字以及特殊字符进行过滤或者encode、decode
 */
public class KeywordUtil {

	public static String CHARSET = "UTF-8";

	// 需要屏蔽的特殊字符
	public static String inj_spechars = "':*:%:;:-:+:,";
	// 需要屏蔽的关键字
	public static String inj_keyword = "and :exec :insert :select :delete :update :count :chr :mid :master :truncate :char :declare :or ";

	// 需要encode的关键字
	public static String encode_keyword = "and:exec:insert:select:delete:update:count:chr:mid:master:truncate:char:declare:or";

	// 需要decode的关键字
	public static String decode_keyword = "YW5k:ZXhlYw==:aW5zZXJ0:c2VsZWN0:ZGVsZXRl:dXBkYXRl:Y291bnQ=:Y2hy:bWlk:bWFzdGVy:dHJ1bmNhdGU=:Y2hhcg==:ZGVjbGFyZQ==:b3I=";

	// 过滤特殊字符
	public static String specharsFilter(String str) {
		String handStr = str;

		String inj_stra[] = inj_spechars.split(":");
		for (int i = 0; i < inj_stra.length; i++) {
			int index = handStr.indexOf(inj_stra[i]);
			if (index != -1) {
				StringBuffer sb = new StringBuffer();
				sb.append(handStr.substring(0, index));
				sb.append(handStr.substring(index + 1, handStr.length()));
				handStr = sb.toString();
				i--;
			}
		}

		return handStr;
	}

	// 过滤sql关键字
	public static String SqlkeywordFilter(String str) {
		String handStr = str;

		String inj_stra[] = inj_keyword.split(":");
		for (int i = 0; i < inj_stra.length; i++) {
			int index = handStr.indexOf(inj_stra[i]);
			String keyword = inj_stra[i];
			if (index != -1) {
				StringBuffer sb = new StringBuffer();
				sb.append(handStr.substring(0, index));
				sb.append(handStr.substring(index + keyword.length(), handStr.length()));
				handStr = sb.toString();
				i--;
			}
		}

		return handStr;
	}

	// 对sql关键字进行encode
	public static String SqlkeywordEncode(String str) {
		String handStr = str;

		String inj_stra[] = encode_keyword.split(":");
		for (int i = 0; i < inj_stra.length; i++) {
			int index = handStr.indexOf(inj_stra[i]);
			String keyword = inj_stra[i];
			if (index != -1) {
				StringBuffer sb = new StringBuffer();
				sb.append(handStr.substring(0, index));
				// 对关键字进行encode
				String encode = Base64.getUrlEncoder().encodeToString(keyword.getBytes());
				sb.append(encode);
				sb.append(handStr.substring(index + keyword.length(), handStr.length()));
				handStr = sb.toString();
				i--;
			}
		}

		return handStr;
	}

	// 对sql关键字进行decode
	public static String SqlkeywordDecode(String str) throws UnsupportedEncodingException {
		String handStr = str;

		String inj_stra[] = decode_keyword.split(":");
		for (int i = 0; i < inj_stra.length; i++) {
			int index = handStr.indexOf(inj_stra[i]);
			String keyword = inj_stra[i];
			if (index != -1) {
				StringBuffer sb = new StringBuffer();
				sb.append(handStr.substring(0, index));
				// 对关键字进行encode
				String decode = new String(Base64.getUrlDecoder().decode(keyword),"UTF-8");
				sb.append(decode);
				sb.append(handStr.substring(index + keyword.length(), handStr.length()));
				handStr = sb.toString();
				i--;
			}
		}

		return handStr;
	}

}
