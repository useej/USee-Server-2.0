package com.usee.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usee.model.User;

public class Json2ObjectUtil {
	
	public static User getUser(String json) {
		
		User user = new User();
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<String, String>();
			map = mapper.readValue(json, new TypeReference<Map<String, String>>() {
			});
			if(map.containsKey("userID")) {
				user.setUserID(map.get("userID"));
			}
			if(map.containsKey("gender")) {
				user.setGender(Integer.parseInt(map.get("gender")));
			}
			if(map.containsKey("nickname")) {
				user.setNickname(map.get("nickname"));
			}
			if(map.containsKey("userIcon")) {
				user.setUserIcon(map.get("userIcon"));
			}
			if(map.containsKey("cellphone")) {
				user.setCellphone(map.get("cellphone"));
			}
			if(map.containsKey("password")) {
				user.setPassword(map.get("password"));
			}
			if(map.containsKey("createTime")) {
				user.setCreateTime(map.get("createTime"));
			}
			if(map.containsKey("openID_qq")) {
				user.setOpenID_qq(map.get("openID_qq"));
			}
			if(map.containsKey("openID_wx")) {
				user.setOpenID_wx(map.get("openID_wx"));
			}
			if(map.containsKey("openID_wb")) {
				user.setOpenID_qq(map.get("openID_wb"));
			}
			if(map.containsKey("verificationCode")) {
				user.setVerificationCode(map.get("verificationCode"));
			}
			if(map.containsKey("vcSendTime")) {
				user.setVcSendTime(map.get("vcSendTime"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;
	}
}
