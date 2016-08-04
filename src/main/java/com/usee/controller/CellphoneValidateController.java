package com.usee.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usee.service.CellphoneValidateService;
import com.usee.service.SqlInjectService;
import com.usee.utils.MD5Util;

@Controller
@RequestMapping("/cellphonevalidate")
public class CellphoneValidateController {

	@Autowired
	CellphoneValidateService cellphoneValidateService;
	
	@Autowired
	private SqlInjectService sqlInjectService;

	private static final String RETURN_INFO = "returnInfo";
	private static final String VERIFICATION_CODE = "verificationCode";
	private static final String CELLPHONE = "cellphone";
	private static final String SEND_CODE_TIME = "vcSendTime";

	@RequestMapping("getcode")
	@ResponseBody
	protected Map<String, Object> sendCode(@RequestBody String cellphoneJson) {
		Map<String, Object> returnMap = new HashMap<String, Object>();

		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();
		
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(cellphoneJson);
		
		try {
			map = mapper.readValue(handJson, new TypeReference<Map<String, String>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		String cellphone = map.get("cellphone");

		StringBuilder code = new StringBuilder();
		Random random = new Random();
		// 生成6位验证码
		for (int i = 0; i < 6; i++) {
			code.append(String.valueOf(random.nextInt(10)));
		}

		// 使用MD5加密
		String verificationCode = MD5Util.getMD5(code.toString());

		// 并将重要信息返回前端
		returnMap.put(CELLPHONE, cellphone);
		returnMap.put(VERIFICATION_CODE, verificationCode);
		returnMap.put(SEND_CODE_TIME, new Date().getTime());

		// 发送信息
		cellphoneValidateService.sendMessage(cellphone, code.toString());

		return returnMap;
	}

	@RequestMapping("register")
	@ResponseBody
	protected Map<String, Object> register(@RequestBody String cellphoneJson) {
		Map<String, Object> returnMap = new HashMap<String, Object>();

		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();
		
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(cellphoneJson);
		
		try {
			map = mapper.readValue(handJson, new TypeReference<Map<String, String>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		String cellphone = map.get("cellphone");

		if (!cellphoneValidateService.isRegister(cellphone)) {
			// 假如用户没有注册过
			StringBuilder code = new StringBuilder();
			Random random = new Random();
			// 生成6位验证码
			for (int i = 0; i < 6; i++) {
				code.append(String.valueOf(random.nextInt(10)));
			}
			// 使用MD5加密
			String verificationCode = MD5Util.getMD5(code.toString());

			// 并将重要信息返回前端
			returnMap.put(RETURN_INFO, "success");
			returnMap.put(CELLPHONE, cellphone);
			returnMap.put(VERIFICATION_CODE, verificationCode);
			returnMap.put(SEND_CODE_TIME, new Date().getTime());
			// 发送信息
			cellphoneValidateService.sendMessage(cellphone, code.toString());
		} else {
			// 假如用户注册过了
			returnMap.put(RETURN_INFO, "registered");
		}
		
		System.out.println(returnMap);

		return returnMap;
	}
}
