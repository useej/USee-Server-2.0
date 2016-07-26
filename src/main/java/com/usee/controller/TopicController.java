package com.usee.controller;


import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import antlr.collections.List;

import com.usee.service.impl.DanmuServiceImp;
import com.usee.service.impl.TopicServiceImpl;


@Controller
public class TopicController {
	@Autowired
	private TopicServiceImpl topicService;
	
	@RequestMapping(value = "topic")
	public String test(Model model){
		return "getdm";
	}
	
	@RequestMapping(value = "getUserTopics", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getUserTopics(@RequestBody String userID){
		JSONObject userIDJson =  new JSONObject().fromObject(userID);
		String userTopics = topicService.getUserTopics(userIDJson.getString("userId"));
		System.out.println(userTopics);
		return userTopics ;
	}
	
	@RequestMapping(value = "getNearbyTopics", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getNearbyTopics(@RequestBody String location ){
		JSONObject locationJson = new JSONObject().fromObject(location);
		double ux = (Double) locationJson.get("lon");
		double uy = (Double) locationJson.get("lon");
		int userRadius =  (Integer) locationJson.get("radius");
		String NearbyTopics = topicService.getNearbyTopics(ux,uy,userRadius);
		System.out.println(NearbyTopics);
		return NearbyTopics;
	}

}
