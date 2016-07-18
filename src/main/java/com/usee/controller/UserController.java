package com.usee.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usee.model.User;
import com.usee.service.UserService;
import com.usee.utils.MD5Util;
import com.usee.utils.UUIDGeneratorUtil;

@Controller
@RequestMapping("/user")
public class UserController {
	
	private static final String RETURN_INFO = "returnInfo";

	@Resource
	private UserService userService;
	
	/**
	 * 手机注册
	 */
	@ResponseBody
	@RequestMapping("/signin")
	public Map<String, Object> signin(@RequestBody User user) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		String cellphone = user.getCellphone();
		if(userService.getUserByCellphone(cellphone) == null) {
			// 该用户已经存在
			returnMap.put(RETURN_INFO, "exist");
			return returnMap;
		} else {
			user.setUserID(UUIDGeneratorUtil.getUUID());
			user.setCreateTime(new Date());
			
			// 未加密的原始密码
			String originPassword = user.getPassword();
			// 密码使用MD5加密
			String md5Password = MD5Util.getMD5(originPassword);
			user.setPassword(md5Password);
			// 处理用户信息
			userService.addUser(user);
			// 还原密码
			user.setPassword(originPassword);
			returnMap.put(RETURN_INFO, "success");
			returnMap.put("user", user);
			return returnMap;
		}
	}
	
	/**
	 * 手机登录
	 */
	@ResponseBody
	@RequestMapping("/login")
	public Map<String, Object> login(@RequestBody User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String cellphone = user.getCellphone();
		// 密码使用MD5加密
		String mdPassword = MD5Util.getMD5(user.getPassword());
		User validateUser = userService.getUserByCellphone(cellphone);
		if(validateUser == null) {
			map.put(RETURN_INFO, "inexistence");
		} else if (mdPassword != validateUser.getPassword()) {
			map.put(RETURN_INFO, "passwordErr");
		} else {
			map.put(RETURN_INFO, "success");
			map.put("user", validateUser);
		}
		return map;
	}
	
	/**
	 * 忘记密码
	 */
	@ResponseBody
	@RequestMapping("/forgetpassword")
	public Map<String, String> forgetPassword(@RequestBody User user) {
		Map<String, String> returnMap = new HashMap<String, String>();
		
		String result = "error";
		// 未加密的原始密码
		String originPassword = user.getPassword();
		// 密码使用MD5加密
		String md5Password = MD5Util.getMD5(originPassword);
		user.setPassword(md5Password);
		// 修改密码
		if (userService.changePassword(user)) {
			result = "success";
		}
		// 还原密码
		user.setPassword(originPassword);
		returnMap.put(RETURN_INFO, result);
		returnMap.put("cellphone", user.getPassword());
		returnMap.put("password", user.getPassword());
		return returnMap;
	}

	/**
	 * 返回值为删除成功的标志
	 */
	@ResponseBody
	@RequestMapping("/delUser")
	public Map<String, Object> delUser(@RequestBody String idJson) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		String userId = null;
		try {
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String,String> map = new HashMap<String, String>();
			map = mapper.readValue(idJson, new TypeReference<Map<String, String>>(){});
			userId = map.get("userId");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String result = "error";
		if (userService.delUser(userId)) {
			result = "success";
		}
		returnMap.put(RETURN_INFO, result);
		return returnMap;
	}

	/**
	 * 返回值为更新除成功的标志
	 */
	@ResponseBody
	@RequestMapping("/updateUser")
	public Map<String, Object> updateUser(@RequestBody User user) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		String result = "error";
		// 密码使用MD5加密
		user.setPassword(MD5Util.getMD5(user.getPassword()));
		if (userService.updateUser(user)) {
			result = "success";
		}
		returnMap.put(RETURN_INFO, result);
		return returnMap;
	}

	@ResponseBody
	@RequestMapping("/getAllUser")
	public List<User> getAllUser() {
		List<User> user = new ArrayList<User>();
		user = userService.getAllUser();
		return user;
	}

	@ResponseBody
	@RequestMapping("/getUser")
	public User getUser(@RequestBody String idJson) {
		String userId = null;
		try {
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String,String> map = new HashMap<String, String>();
			map = mapper.readValue(idJson, new TypeReference<Map<String, String>>(){});
			userId = map.get("userId");
		} catch (IOException e) {
			e.printStackTrace();
		}
		User user = userService.getUser(userId);
		return user;
	}

}