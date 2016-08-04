package com.usee.utils;

/*
 * 此util用于判断前台传输到后台的参数是输入哪一类参数
 * 用于以后对不同类别的参数进行过滤已经encode操作
 */
public class PropertiesUtil {
	
	// 普通的 String 类型参数名
	public static String[] stringProperties = {
			// user表中的参数
			"userID", "nickname", "userIcon",
			"cellphone", "password", "createTime",
			"openID_qq", "openID_wx", "openID_wb",
			"verificationCode", "vcSendTime",
			// UserController所需的额外参数
			"oldPassword", "newPassword",
			// OAuthLoginController所需的额外参数
			"access_token", "uid", "openid",
			// MessageController所需的额外参数
			"latestReadTime"
			
	};
	
	// 所有的 int 参数名
	public static String[] intProperties = {
			"gender"
	};
	
	// 特殊的 String 参数名(弹幕和评论)
	public static String[] specialProperties = {
			
	};
	
	public static Boolean isStringProperties(String property) {
		for (String string : stringProperties) {
			if(property.equals(string)) {
				return true;
			}
		}
		return false;
	}

	public static Boolean isIntProperties(String property) {
		for (String string : intProperties) {
			if(property.equals(string)) {
				return true;
			}
		}
		return false;
	}
	
	public static Boolean isSpecialProperties(String property) {
		for (String string : specialProperties) {
			if(property.equals(string)) {
				return true;
			}
		}
		return false;
	}
	
}
