package com.usee.service;

import com.usee.model.User;


public interface OAuthLoginService {

	public User handleQQUserInfo(String access_token, String openid, String fileRootDir);
	public User handleWeiboUserInfo(String access_token, String uid, String fileRootDir);
	public User handleWeinxinUserInfo(String access_token, String openid, String fileRootDir);
	public void addUser(User user, String fileRootDir);
	
}
