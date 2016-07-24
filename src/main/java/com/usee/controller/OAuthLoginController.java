package com.usee.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usee.model.User;
import com.usee.service.OAuthLoginService;
import com.usee.service.UserService;

/*
 * 第三方登录servlet
 */
@Controller
@RequestMapping("/oauthlogin")
public class OAuthLoginController {
	private static final String DEFAULT_CELLPHONE = "<dbnull>";
	
	@Autowired
	private OAuthLoginService oauthLoginService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 安卓用户第三方登录方法 将客户端传输过来的信息存储到数据库中
	 */
	@ResponseBody
	@RequestMapping("/android")
	public Map<String, Object> androidLoginOAuth(@RequestBody User user, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String openID_qq = user.getOpenID_qq();
		String openID_wx = user.getOpenID_wx();
		String openID_wb = user.getOpenID_wb();
		
		User validateUser = null;
		if(openID_qq != null) {
			// QQ登录
			validateUser = userService.getUserByOpenId("openID_qq", openID_qq);
		} else if (openID_wx != null) {
			// 微信登录
			validateUser = userService.getUserByOpenId("openID_wx", openID_wx);
		} else {
			// 微博登录
			validateUser = userService.getUserByOpenId("openID_wb", openID_wb);
		} 
		// 假如数据库中没有对应的用户信息,则证明用户第一次登录
		if(validateUser == null) {
			oauthLoginService.addUser(user, request.getSession().getServletContext().getRealPath("/"));
			map.put("firstLogin", 1);	
			// 加入数据库中的user信息为默认的手机号(用户是用第三方登录的，没有设置手机号和密码)
			// 则将手机号和密码置为空再返回给前端
			if(user.getCellphone().equals(DEFAULT_CELLPHONE)) {
				user.setCellphone(null);
				user.setPassword(null);
			}
			map.put("user", user);
		} else {
			map.put("firstLogin", 0);
			if(validateUser.getCellphone().equals(DEFAULT_CELLPHONE)) {
				validateUser.setCellphone(null);
			}
			validateUser.setPassword(null);
			map.put("user", validateUser);
		}
		return map;
	}


	/**
	 * QQ第三方登录
	 */
	@ResponseBody
	@RequestMapping("/qq")
	public User qqLoginOAuth(@RequestBody String json, HttpServletRequest request) {
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
		User user = oauthLoginService.handleQQUserInfo(access_token, openid, 
				request.getSession().getServletContext().getRealPath("/"));
		
		return user;
	}
	
	/**
	 * 新浪微博第三方登录
	 */
	@ResponseBody
	@RequestMapping("/weibo")
	public User weiboLoginOAuth(@RequestBody String json, HttpServletRequest request) {
		
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
		User user = oauthLoginService.handleWeiboUserInfo(access_token, uid, 
				request.getSession().getServletContext().getRealPath("/"));
		
        return user;
	}
	

	/**
	 * 微信第三方登录
	 */
	@ResponseBody
	@RequestMapping("/weixin")
	public User weixinLoginOAuth(@RequestBody String json, HttpServletRequest request) {
		
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
		User user = oauthLoginService.handleWeinxinUserInfo(access_token, openid, 
				request.getSession().getServletContext().getRealPath("/"));
		
	    return user;
	}

}
