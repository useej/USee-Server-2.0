package com.usee.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usee.service.impl.JPushServiceImpl;

import net.sf.json.JSONObject;



@Controller
public class JPushController {
	
	@Autowired
	private JPushServiceImpl jPushServiceImpl;
	
	@RequestMapping(value = "pushall", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public boolean pushAll(@RequestBody String json){
		JSONObject pushJson = JSONObject.fromObject(json);
		String message = pushJson.getString("message");
		System.out.println(message);
		Map<String, String> extras = new HashMap<String, String>();
		extras.put("type", "system");
		
		jPushServiceImpl.push_all(message, extras);
		return true;
	}

}
