package com.usee.service;

import java.util.Map;

import com.usee.model.User;

public interface WechatRedirectService {
	
	public Map<String, String> getToken(String code);
	
	public User getUserInfo(String access_token, String openid);
	
	public void addUser(User user, String fileRootDir);
}
