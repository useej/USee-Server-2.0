package com.usee.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usee.model.Comment;
import com.usee.service.MessageService;

@Controller
@RequestMapping("/message")
public class MessageController {
	
	@Autowired
	MessageService messageService;
	
	
	@ResponseBody
	@RequestMapping(value = "getNewMsgsNum", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public Map<String, Integer> getNewMsgsNum(@RequestBody String json) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		String userID = null;
		String latestReadTime = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<String, String>();
			map = mapper.readValue(json, new TypeReference<Map<String, String>>() {
			});
			userID = map.get("userID");
			latestReadTime = map.get("latestReadTime");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int newMsgsNum = messageService.getNewMsgsNum(userID, latestReadTime);
		
		returnMap.put("newMsgsNum", newMsgsNum);
		return returnMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "getNewMsgs", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public Map<String, List<Comment>> getNewMsgs(@RequestBody String json) {
		Map<String, List<Comment>> returnMap = new HashMap<String, List<Comment>>();
		
		String userID = null;
		String latestReadTime = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<String, String>();
			map = mapper.readValue(json, new TypeReference<Map<String, String>>() {
			});
			userID = map.get("userID");
			latestReadTime = map.get("latestReadTime");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Comment> list = messageService.getNewMsgs(userID, latestReadTime);
	
		returnMap.put("newMsgs", list);
		return returnMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "getallMsgs", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public Map<String, List<Comment>> getallMsgsbyID(@RequestBody String json) {
		Map<String, List<Comment>> returnMap = new HashMap<String, List<Comment>>();
		
		String userID = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<String, String>();
			map = mapper.readValue(json, new TypeReference<Map<String, String>>() {
			});
			userID = map.get("userID");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Comment> list = messageService.getallMsgsbyID(userID);
		
		returnMap.put("allMsgs", list);
		return returnMap;
	}
}
