package com.usee.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usee.service.CellphoneValidateService;
import com.usee.utils.MD5Util;

@Controller  
@RequestMapping("/cellphonevalidate")  
public class CellphoneValidateController {  
	
	@Resource
	CellphoneValidateService cellphoneValidateService;
    
    private static final String VALIDATE_CODE = "validate_code";  
    private static final String VALIDATE_CELLPHONE = "validate_cellphone";  
    private static final String SEND_CODE_TIME = "send_code_time";  
      
      
    @RequestMapping("getcode")  
    @ResponseBody  
    protected Map<String, Object> sendCode(@RequestBody String cellphoneJson) {  
    	Map<String, Object> returnMap = new HashMap<String, Object>();
    	
    	ObjectMapper mapper = new ObjectMapper(); 
		Map<String,String> map = new HashMap<String, String>();
		try {
			map = mapper.readValue(cellphoneJson, new TypeReference<Map<String, String>>(){});
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
        
        // 并将重要信息返回前端
        returnMap.put(VALIDATE_CELLPHONE, cellphone);  
        returnMap.put(VALIDATE_CODE, code.toString());  
        returnMap.put(SEND_CODE_TIME, new Date().getTime());  
        
        // 使用MD5加密
        String validateCode = MD5Util .getMD5(code.toString());
        String smsText = "验证码：" + validateCode;  
       
        // 发送信息
        cellphoneValidateService.sendMessage(cellphone, smsText);  
        
        return returnMap;
    }   
}
