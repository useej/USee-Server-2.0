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

	@RequestMapping(value = "getusertopics", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getUserTopics(@RequestBody String userID){
		JSONObject userIDJson =  new JSONObject().fromObject(userID);
		String userTopics = topicService.getUserTopics(userIDJson.getString("userid"));
		System.out.println(userTopics);
		return userTopics ;
	}
	
	@RequestMapping(value = "getnearbytopics", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getNearbyTopics(@RequestBody String location ){
		JSONObject locationJson = new JSONObject().fromObject(location);
		double ux = locationJson.getDouble("lon");
		double uy = locationJson.getDouble("lat");
		int userRadius = locationJson.getInt("radius");
		String NearbyTopics = topicService.getNearbyTopics(ux,uy,userRadius);
		System.out.println(NearbyTopics);
		return NearbyTopics;
	}
	
	@RequestMapping(value = "updateusertopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public void updateUser_topic(@RequestBody String ID){
		JSONObject IDJson =  new JSONObject().fromObject(ID);
		topicService.updateUser_topic(IDJson.getString("userid"),IDJson.getString("topicid"));
	}
	
	@RequestMapping(value = "createtopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String sendDanmu(@RequestBody String newTopic){
		JSONObject newTopicJson = new JSONObject().fromObject(newTopic);
		topicService.createTopic(newTopicJson);
		String userTopics = topicService.getUserTopics(newTopicJson.getString("userid"));
		System.out.println(userTopics);
		return userTopics;
	}
}
