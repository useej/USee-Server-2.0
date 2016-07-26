package com.usee.utils;

import java.security.MessageDigest;

public class MD5Util {
	
	/**
	 * MD5加密
	 * 
	 * @param message 要进行MD5加密的字符串
	 * @return 加密结果为32位字符串,字母大写
	 */
	public static String getMD5(String message) {
		MessageDigest messageDigest = null;
		StringBuffer md5StrBuff = new StringBuffer();
		System.out.println(message);
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			// 处理中文字符加密
			messageDigest.reset();
		    messageDigest.update(message.getBytes("UTF-8"));
			byte[] byteArray = messageDigest.digest();

//			byte[] byteArray = messageDigest.digest(message.getBytes("UTF-8"));
			// 将结果128位结果转为32个16进制字符串
			for (int i = 0; i < byteArray.length; i++) {
				int temp = byteArray[i] & 0Xff;
				String hexString = Integer.toHexString(temp);
				if (hexString.length() < 2) {
					md5StrBuff.append("0").append(hexString);
				} else {
					md5StrBuff.append(hexString);
				}
			}
		} catch (Exception e) {
			System.out.println(">>>>>>>>>>>>>>>" + e.toString());
		}
		return md5StrBuff.toString().toUpperCase();// 字母大写
	}

}
