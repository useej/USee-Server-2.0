package com.usee.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usee.service.SensitiveWordService;

@Controller
public class SensitiveWordController {
	
	@Autowired
	SensitiveWordService sensitiveWordService;
	
	@ResponseBody
	@RequestMapping(value = "getSWFileInfo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public Map<String, String> getSWFileInfo() {
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap = sensitiveWordService.getSWFileInfo();
		System.out.println(returnMap);
		return returnMap;
	}

}
