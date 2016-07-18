package com.usee.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usee.model.User;
import com.usee.service.OAuthLoginService;
import com.usee.service.UserService;
import com.usee.utils.UUIDGeneratorUtil;

/*
 * 第三方登录servlet
 */
@Controller
@RequestMapping("/oauthlogin")
public class OAuthLoginController {
	
	@Resource
	private OAuthLoginService oauthLoginService;
	
	@Resource
	private UserService userService;
	
	/**
	 * 安卓用户第三方登录方法 将客户端传输过来的信息存储到数据库中
	 */
	@ResponseBody
	@RequestMapping("/android")
	public Map<String, Object> androidLoginOAuth(@RequestBody User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String qqOpenId = user.getQqOpenId();
		String weixinOpenId = user.getWeixinOpenID();
		String weiboOpenId = user.getWeiboOpenId();
		
		User validateUser = null;
		if(qqOpenId != null) {
			// QQ登录
			validateUser = userService.getUserByOpenId("qqOpenId", qqOpenId);
		} else if (weixinOpenId != null) {
			// 微信登录
			validateUser = userService.getUserByOpenId("weixinOpenId", weixinOpenId);
		} else {
			// 微博登录
			validateUser = userService.getUserByOpenId("weiboOpenId", weiboOpenId);
		} 
		// 假如数据库中没有对应的用户信息,则证明用户第一次登录
		if(validateUser == null) {
			user.setUserID(UUIDGeneratorUtil.getUUID());
			user.setCreateTime(new Date());
			userService.addUser(user);
		} else {}
		
		map.put("user", user);
		return map;
	}


	/**
	 * QQ第三方登录
	 */
	@ResponseBody
	@RequestMapping("/qq")
	public User qqLoginOAuth(@RequestBody String json) {
		String access_token = null;
		String openid = null;
		
		try {
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String,String> map = new HashMap<String, String>();
			map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});
			access_token = map.get("access_token");
			openid = map.get("openid");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 处理QQ用户信息
		User user = oauthLoginService.handleQQUserInfo(access_token, openid);
		
		return user;
	}
	
	/**
	 * 新浪微博第三方登录
	 */
	@ResponseBody
	@RequestMapping("/weibo")
	public User weiboLoginOAuth(@RequestBody String json) {
		
		String access_token = null;
		String uid = null;
		
		try {
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String,String> map = new HashMap<String, String>();
			map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});
			access_token = map.get("access_token");
			uid = map.get("uid");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 处理微博用户信息
		User user = oauthLoginService.handleWeiboUserInfo(access_token, uid);
		
        return user;
	}
	

	/**
	 * 微信第三方登录
	 */
	@ResponseBody
	@RequestMapping("/weixin")
	public User weixinLoginOAuth(@RequestBody String json) {
		
		String access_token = null;
		String openid = null;
		
		try {
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String,String> map = new HashMap<String, String>();
			map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});
			access_token = map.get("access_token");
			openid = map.get("openid");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		// 处理微信用户信息
		User user = oauthLoginService.handleWeinxinUserInfo(access_token, openid);
		
	    return user;
	}

}
