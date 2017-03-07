package com.usee.service;

import java.util.Map;

import com.usee.model.User;

public interface WxService {
	
	public String getCodeUrl(String scope, String userId);
	
	public Map<String, String> getToken(String code);
	
	public User getUserInfo(String access_token, String openid);
	
}
