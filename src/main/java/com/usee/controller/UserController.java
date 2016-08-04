package com.usee.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usee.model.User;
import com.usee.service.SqlInjectService;
import com.usee.service.UserService;
import com.usee.utils.Json2ObjectUtil;
import com.usee.utils.MD5Util;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final String RETURN_INFO = "returnInfo";
	private static final String DEFAULT_CELLPHONE = "<dbnull>";
	private static final String DEFAULT_PASSWORD = "<dbnull>";
	//private static final String USERICON_PREFIX = "http://114.215.209.102/USee/";
	private static final long VALIDITY_TIME = 600000;

	@Autowired
	private UserService userService;
	
	@Autowired
	private SqlInjectService sqlInjectService;

	/**
	 * 手机注册
	 * 需要传入：
	 * 		cellphone手机号
	 * 		password密码
	 * 		validateCode验证码(32个16进制数)
	 * 
	 */
	@ResponseBody
	@RequestMapping("/signin")
	public Map<String, Object> signin(@RequestBody String json) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(json);
		User user = Json2ObjectUtil.getUser(handJson);
		
		System.out.println("-------------------");
		System.out.println(user);
		System.out.println("-------------------");
		
		String cellphone = user.getCellphone();
		// 数据库中对应的手机号的user信息(发送验证码时保存的)
		User signinUser = userService.getUserByCellphone(cellphone);
		// 如果手机号不准确直接返回错误
		if (cellphone.length() != 11 || cellphone.equals(DEFAULT_CELLPHONE) ||
				signinUser == null) {
			returnMap.put(RETURN_INFO, "cellphoneErr");
			return returnMap;
		}
		
		if (!(signinUser.getPassword()).equals(DEFAULT_PASSWORD)) {
			// 如果该手机号用户已经存在并且密码不等于默认的密码，则证明用户已经注册
			returnMap.put(RETURN_INFO, "exist");
			return returnMap;
		} else if(!user.getVerificationCode().equals(signinUser.getVerificationCode())){
			// 验证码不正确
			returnMap.put(RETURN_INFO, "verificationCodeErr");
			return returnMap;
		} else {
			// 得到系统当前的时间
			long currentTime = System.currentTimeMillis();
			long verificationCode_creatTime = Long.parseLong(signinUser.getVcSendTime());
			user.setVcSendTime(verificationCode_creatTime + "");
			if((currentTime - verificationCode_creatTime) < VALIDITY_TIME) {
				// 如果验证码没有超过10分钟,则验证码有效
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
			} else {
				returnMap.put(RETURN_INFO, "verificationCodeInvalid");
				return returnMap;
			}
		}
	}

	/**
	 * 手机登录
	 */
	@ResponseBody
	@RequestMapping("/login")
	public Map<String, Object> login(@RequestBody String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(json);
		User user = Json2ObjectUtil.getUser(handJson);

		String cellphone = user.getCellphone();
		// 如果手机号不准确直接返回错误
		if (cellphone.length() != 11 || cellphone.equals(DEFAULT_CELLPHONE) ||
				userService.getUserByCellphone(cellphone) == null) {
			map.put(RETURN_INFO, "cellphoneErr");
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
	 * 需要传入：
	 * 		cellphone手机号
	 * 		password密码
	 * 		validateCode验证码(32个16进制数)
	 */
	@ResponseBody
	@RequestMapping("/forgetpassword")
	public Map<String, Object> forgetPassword(@RequestBody String json) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(json);
		User user = Json2ObjectUtil.getUser(handJson);
		System.out.println(handJson);
		
		String cellphone = user.getCellphone();
		// 数据库中对应的手机号的user信息
		User validateUser = userService.getUserByCellphone(cellphone);
		// 如果手机号不准确直接返回错误
		if (cellphone.length() != 11 || cellphone.equals(DEFAULT_CELLPHONE) ||
				validateUser == null) {
			returnMap.put(RETURN_INFO, "cellphoneErr");
			return returnMap;
		}
		
		if(!user.getVerificationCode().equals(validateUser.getVerificationCode())){
			// 验证码不正确
			returnMap.put(RETURN_INFO, "validateCodeErr");
			return returnMap;
		} else {
			// 得到系统当前的时间
			long currentTime = System.currentTimeMillis();
			long validateCode_creatTime = Long.parseLong(validateUser.getVcSendTime());
			if((currentTime - validateCode_creatTime) < VALIDITY_TIME) {
				// 如果验证码没有超过10分钟,则验证码有效
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
			} else {
				returnMap.put(RETURN_INFO, "validateCodeInvalid");
				return returnMap;
			}
		}
	}

	/**
	 * 修改密码，原始密码后台验证
	 */
	@ResponseBody
	@RequestMapping("/modifypassword")
	public Map<String, Object> modifyPassword(@RequestBody String json) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(json);
		System.out.println(handJson);
		String userID = null;
		String oldPassword = null;
		String newPassword = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<String, String>();
			map = mapper.readValue(handJson, new TypeReference<Map<String, String>>() {
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
			returnMap.put("userID", modifyUser.getUserID());
			returnMap.put("password", modifyUser.getPassword());
			System.out.println(modifyUser);
		}
		return returnMap;
	}
	
	
	/**
	 * 更新用户信息
	 */
	@ResponseBody
	@RequestMapping("/updateuser")
	public Map<String, Object> updateUser(@RequestBody String json) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(json);
		System.out.println(handJson);
		
		User updateUser = Json2ObjectUtil.getUser(handJson);
		
		
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
//		// 加入数据库中的user信息为默认的手机号(用户是用第三方登录的，没有设置手机号和密码)
//		// 则将手机号和密码置为空再返回给前端
//		if (user.getCellphone().equals(DEFAULT_CELLPHONE)) {
//			user.setCellphone(null);
//		}
		returnMap.put("userID", user.getUserID());
		returnMap.put("gender", user.getGender());
		returnMap.put("nickname", user.getNickname());
		returnMap.put("userIcon", user.getUserIcon());
		System.out.println(user.toString());
		return returnMap;
	}

	/**
	 * 绑定手机号
	 */
	@ResponseBody
	@RequestMapping("/bindcellphone")
	public Map<String, Object> bindCellphone(@RequestBody String json) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(json);
		User user = Json2ObjectUtil.getUser(handJson);
		System.out.println(handJson);
		
		String cellphone = user.getCellphone();
		
		// 如果手机号不准确直接返回错误
		if (cellphone.length() != 11 || cellphone.equals(DEFAULT_CELLPHONE)) {
			returnMap.put(RETURN_INFO, "cellphoneErr");
			return returnMap;
		}
		
		// 如果此手机号之前已经绑定过其他用户
		User bindUser = userService.getUserByCellphone(cellphone);
		if(!(bindUser.getUserID()).equals(user.getUserID())){
			returnMap.put(RETURN_INFO, "cellphoneBinding");
			return returnMap;
		}
		
		if(!user.getVerificationCode().equals(bindUser.getVerificationCode())){
			// 验证码不正确
			returnMap.put(RETURN_INFO, "verificationCodeErr");
			return returnMap;
		} else {
			// 得到系统当前的时间
			long currentTime = System.currentTimeMillis();
			long validateCode_creatTime = Long.parseLong(bindUser.getVcSendTime());
			if((currentTime - validateCode_creatTime) < VALIDITY_TIME) {
				// 如果验证码没有超过10分钟,则验证码有效
				
				// 得到要更新的user
				User updateUser = userService.getUser(user.getUserID());
				updateUser.setCellphone(user.getCellphone());
				updateUser.setPassword(user.getPassword());
				userService.updateUser_Cellphone(updateUser);
				
				// 还原密码
				updateUser.setPassword(user.getPassword());

				returnMap.put(RETURN_INFO, "success");
				returnMap.put("userID", updateUser.getUserID());
				returnMap.put("cellphone", updateUser.getCellphone());
				returnMap.put("password", updateUser.getPassword());
				return returnMap;
			} else {
				returnMap.put(RETURN_INFO, "validateCodeInvalid");
				return returnMap;
			}
		}
	}

	/**
	 * 绑定第三方社交账号
	 */
	@ResponseBody
	@RequestMapping("/bindoauth")
	public Map<String, Object> bindOAuth(@RequestBody String json) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(json);
		User user = Json2ObjectUtil.getUser(handJson);
		System.out.println(handJson);
		User updateUser = userService.getUser(user.getUserID());
		
		User validateUser = null;
		if (user.getOpenID_qq() != null && !user.getOpenID_qq().equals(updateUser.getOpenID_qq())) {
			updateUser.setOpenID_qq(user.getOpenID_qq());
			validateUser = userService.getUserByOpenId("openID_qq", user.getOpenID_qq());
			if(validateUser != null) {
				returnMap.put(RETURN_INFO, "exit");
				return returnMap;
			}
		}
		if (user.getOpenID_wx() != null && !user.getOpenID_wx().equals(updateUser.getOpenID_wx())) {
			updateUser.setOpenID_wx(user.getOpenID_wx());
			validateUser = userService.getUserByOpenId("openID_qq", user.getOpenID_qq());
			if(validateUser != null) {
				returnMap.put(RETURN_INFO, "exit");
				return returnMap;
			}
		}
		if (user.getOpenID_wb() != null && !user.getOpenID_wb().equals(updateUser.getOpenID_wb())) {
			updateUser.setOpenID_wb(user.getOpenID_wb());
			validateUser = userService.getUserByOpenId("openID_qq", user.getOpenID_qq());
			if(validateUser != null) {
				returnMap.put(RETURN_INFO, "exit");
				return returnMap;
			}
		}

		userService.updateUser_OAuth(updateUser);
		
		returnMap.put(RETURN_INFO, "success");
		returnMap.put("userID", updateUser.getUserID());
		returnMap.put("openID_qq", updateUser.getOpenID_qq());
		returnMap.put("openID_wx", updateUser.getOpenID_wx());
		returnMap.put("openID_wb", updateUser.getOpenID_wb());
		return returnMap;
	}
	
	/**
	 * 解除绑定第三方社交账号
	 */
	@ResponseBody
	@RequestMapping("/unbindoauth")
	public Map<String, Object> unbindOAuth(@RequestBody String json) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(json);
		User user = Json2ObjectUtil.getUser(handJson);
		System.out.println(handJson);
		User updateUser = userService.getUser(user.getUserID());
		
		if (user.getOpenID_qq() != null && user.getOpenID_qq().equals(updateUser.getOpenID_qq())) {
			updateUser.setOpenID_qq("");
		}
		if (user.getOpenID_wx() != null && user.getOpenID_wx().equals(updateUser.getOpenID_wx())) {
			updateUser.setOpenID_wx("");
		}
		if (user.getOpenID_wb() != null && user.getOpenID_wb().equals(updateUser.getOpenID_wb())) {
			updateUser.setOpenID_wb("");
		}

		userService.updateUser_OAuth(updateUser);
		
		returnMap.put("userID", updateUser.getUserID());
		returnMap.put("openID_qq", updateUser.getOpenID_qq());
		returnMap.put("openID_wx", updateUser.getOpenID_wx());
		returnMap.put("openID_wb", updateUser.getOpenID_wb());
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
			realPath = request.getSession().getServletContext().getRealPath("/") + "userIcons";
			System.out.println(realPath);
			newFileName = userID + ".jpg";
			FileUtils.copyInputStreamToFile(headPhotoFile.getInputStream(), new File(realPath, newFileName));
			returnInfo = "success";
		}
		User user = userService.getUser(userID);
		user.setUserIcon(newFileName);
		userService.updateUser(user);
		
		returnMap.put(RETURN_INFO, returnInfo);
		returnMap.put("userID", userID);
		returnMap.put("userIcon", user.getUserIcon());
		return returnMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "getUserInfo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public Map<String, Object> getUserInfo(@RequestBody String json) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(json);
		User user = Json2ObjectUtil.getUser(handJson);
		System.out.println(handJson);
		User getUser = userService.getUser(user.getUserID());
		returnMap.put("userID", getUser.getUserID());
		returnMap.put("gender", getUser.getGender());
		returnMap.put("nickname", getUser.getNickname());
		returnMap.put("userIcon", getUser.getUserIcon());
		
		return returnMap;
	}
	
//	public static String getUserIcon(String userIcon) {
//		if(userIcon.length() < 10) {
//			return USERICON_PREFIX + "randomIcons/" + userIcon;
//		} else {
//			return USERICON_PREFIX + "userIcons/" + userIcon;
//		}
//	}
}