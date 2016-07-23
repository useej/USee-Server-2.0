package com.usee.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usee.model.User;
import com.usee.service.UserService;
import com.usee.utils.MD5Util;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final String RETURN_INFO = "returnInfo";
	private static final String DEFAULT_CELLPHONE = "usee";

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
		// 如果手机号不准确直接返回错误
		if (cellphone.length() != 11 || cellphone.equals(DEFAULT_CELLPHONE)) {
			returnMap.put(RETURN_INFO, "error");
			return returnMap;
		}
		if (userService.getUserByCellphone(cellphone) != null) {
			// 如果该用户已经存在直接返回存在
			returnMap.put(RETURN_INFO, "exist");
			return returnMap;
		} else {
			// 未加密的原始密码
			String originPassword = user.getPassword();
			// 处理用户信息
			userService.addUser(user);
			// 还原密码
			user.setPassword(originPassword);

			System.out.println(user.toString());

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
		// 如果手机号不准确直接返回错误
		if (cellphone.length() != 11 || cellphone.equals(DEFAULT_CELLPHONE)) {
			map.put(RETURN_INFO, "error");
			return map;
		}

		// 密码使用MD5加密
		String mdPassword = MD5Util.getMD5(user.getPassword());
		User validateUser = userService.getUserByCellphone(cellphone);
		if (validateUser == null) {
			map.put(RETURN_INFO, "inexistence");
		} else if (!mdPassword.equals(validateUser.getPassword())) {
			map.put(RETURN_INFO, "passwordErr");
		} else {
			// 还原密码
			validateUser.setPassword(user.getPassword());
			map.put(RETURN_INFO, "success");
			map.put("user", validateUser);
			System.out.println(validateUser);
		}
		return map;
	}

	/**
	 * 忘记密码
	 */
	@ResponseBody
	@RequestMapping("/forgetpassword")
	public Map<String, Object> forgetPassword(@RequestBody User user) {
		Map<String, Object> returnMap = new HashMap<String, Object>();

		// 如果手机号不准确直接返回错误
		if (user.getCellphone().length() != 11 || user.getCellphone().equals(DEFAULT_CELLPHONE)) {
			returnMap.put(RETURN_INFO, "error");
			return returnMap;
		}
		// 数据库中对应的手机号的user信息
		User validateUser = userService.getUserByCellphone(user.getCellphone());
		// 未加密的原始密码
		String originPassword = user.getPassword();
		validateUser.setPassword(originPassword);

		String result = "error";
		// 修改密码
		if (userService.changePassword(validateUser)) {
			result = "success";
		}
		// 还原密码
		validateUser.setPassword(originPassword);
		returnMap.put(RETURN_INFO, result);
		returnMap.put("user", validateUser);
		System.out.println(validateUser);
		return returnMap;
	}

//	/**
//	 * 修改密码,原始密码前台验证
//	 */
//	@ResponseBody
//	@RequestMapping("/modifypassword")
//	public Map<String, Object> modifyPassword2(@RequestBody User user) {
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//		// 数据库中对应的手机号的user信息
//		User modifyUser = userService.getUser(user.getUserID());
//		// 未加密的原始密码
//		String originPassword = user.getPassword();
//		modifyUser.setPassword(originPassword);
//
//		// 修改密码
//		if (userService.modifyPassword(modifyUser)) {
//			// 还原密码
//			modifyUser.setPassword(originPassword);
//			returnMap.put("user", modifyUser);
//		}
//		return returnMap;
//	}

	/**
	 * 修改密码，原始密码后台验证
	 */
	@ResponseBody
	@RequestMapping("/modifypassword")
	public Map<String, Object> modifyPassword(@RequestBody String json) {
		Map<String, Object> returnMap = new HashMap<String, Object>();

		String userID = null;
		String oldPassword = null;
		String newPassword = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<String, String>();
			map = mapper.readValue(json, new TypeReference<Map<String, String>>() {
			});
			userID = map.get("userID");
			oldPassword = map.get("oldPassword");
			newPassword = map.get("newPassword");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 数据库中对应的userID的user信息
		User modifyUser = userService.getUser(userID);
		// 判断原密码是否正确
		if(!MD5Util.getMD5(oldPassword).equals(modifyUser.getPassword())) {
			returnMap.put("returnInfo", "passwordErr");
		} else {
			modifyUser.setPassword(newPassword);
			userService.modifyPassword(modifyUser);
			modifyUser.setPassword(newPassword);
			returnMap.put("returnInfo", "success");
			returnMap.put("user", modifyUser);
			System.out.println(modifyUser);
		}
		return returnMap;
	}
	
	
	/**
	 * 更新用户信息
	 */
	@ResponseBody
	@RequestMapping("/updateuser")
	public Map<String, Object> updateUser(@RequestBody User updateUser) {
		Map<String, Object> returnMap = new HashMap<String, Object>();

		User user = userService.getUser(updateUser.getUserID());
		if (updateUser.getGender() != user.getGender()) {
			user.setGender(updateUser.getGender());
		}
		if (updateUser.getNickname() != null && !updateUser.getNickname().equals(user.getNickname())) {
			user.setNickname(updateUser.getNickname());
		}
		if (updateUser.getUserIcon() != null && !updateUser.getUserIcon().equals(user.getUserIcon())) {
			user.setUserIcon(updateUser.getUserIcon());
		}

		userService.updateUser(updateUser);
		// 加入数据库中的user信息为默认的手机号(用户是用第三方登录的，没有设置手机号和密码)
		// 则将手机号和密码置为空再返回给前端
		if (user.getCellphone().equals(DEFAULT_CELLPHONE)) {
			user.setCellphone(null);
		}
		// 密码置为空
		user.setPassword(null);
		returnMap.put("user", user);
		System.out.println(user.toString());
		return returnMap;
	}

	/**
	 * 绑定手机号
	 */
	@ResponseBody
	@RequestMapping("/bindcellphone")
	public Map<String, Object> bindCellphone(@RequestBody User user) {
		Map<String, Object> returnMap = new HashMap<String, Object>();

		// 如果手机号不准确直接返回错误
		if (user.getCellphone().length() != 11 || user.getCellphone().equals(DEFAULT_CELLPHONE)) {
			returnMap.put(RETURN_INFO, "error");
			return returnMap;
		}

		User updateUser = userService.getUser(user.getUserID());
		updateUser.setCellphone(user.getCellphone());

		userService.updateUser_Cellphone(updateUser);
		// 还原密码
		updateUser.setPassword(user.getPassword());

		returnMap.put(RETURN_INFO, "success");
		returnMap.put("user", updateUser);
		return returnMap;
	}

	/**
	 * 绑定第三方社交账号
	 */
	@ResponseBody
	@RequestMapping("/bindoauth")
	public Map<String, Object> bindOAuth(@RequestBody User user) {
		Map<String, Object> returnMap = new HashMap<String, Object>();

		User updateUser = userService.getUser(user.getUserID());
		if (user.getOpenID_qq() != null && !user.getOpenID_qq().equals(updateUser.getOpenID_qq())) {
			updateUser.setOpenID_qq(user.getOpenID_qq());
		}
		if (user.getOpenID_wx() != null && !user.getOpenID_wx().equals(updateUser.getOpenID_wx())) {
			updateUser.setOpenID_wx(user.getOpenID_wx());
		}
		if (user.getOpenID_wb() != null && !user.getOpenID_wb().equals(updateUser.getOpenID_wb())) {
			updateUser.setOpenID_wb(user.getOpenID_wb());
		}

		userService.updateUser_OAuth(updateUser);

		// 加入数据库中的user信息为默认的手机号(用户是用第三方登录的，没有设置手机号和密码)
		// 则将手机号和密码置为空再返回给前端
		if (user.getCellphone().equals(DEFAULT_CELLPHONE)) {
			user.setCellphone(null);
		} 
		// 密码置为空
		user.setPassword(null);
		returnMap.put("user", updateUser);
		return returnMap;
	}

	/**
	 * 上传用户头像
	 */
	@ResponseBody
	@RequestMapping("/uploadicon")
	public Map<String, Object> uploadIcon(@RequestParam MultipartFile headPhotoFile, HttpServletRequest request)
			throws IOException {
		Map<String, Object> returnMap = new HashMap<String, Object>();

		String userID = request.getParameter("userID");
		String realPath = null;
		String newFileName = null;
		String returnInfo = null;
		if (headPhotoFile.isEmpty()) {
			returnInfo = "fileinexistence";
		} else {
			// 获取Web项目的全路径
			realPath = request.getSession().getServletContext().getRealPath("/") + "userIcons\\";
			System.out.println(realPath);
			newFileName = userID + ".jpg";
			FileUtils.copyInputStreamToFile(headPhotoFile.getInputStream(), new File(realPath, newFileName));
			returnInfo = "success";
		}
		User user = userService.getUser(userID);
		user.setUserIcon("userIcons\\" + newFileName);
		userService.updateUser(user);
		// 密码置为空
		user.setPassword(null);
		
		returnMap.put(RETURN_INFO, returnInfo);
		returnMap.put("user", user);
		return returnMap;
	}
}