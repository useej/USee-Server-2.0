package com.usee.service;

import com.usee.model.User;


public interface OAuthLoginService {

	public User handleQQUserInfo(String access_token, String openid);
	public User handleWeiboUserInfo(String access_token, String uid);
	public User handleWeinxinUserInfo(String access_token, String openid);
}
