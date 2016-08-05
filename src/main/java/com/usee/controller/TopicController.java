package com.usee.controller;


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import antlr.collections.List;

import com.usee.model.Topic;
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
		String userid = locationJson.getString("userid");
		String NearbyTopics = topicService.getNearbyTopics(ux,uy,userRadius,userid);
		System.out.println(NearbyTopics);
		return NearbyTopics;
	}

	@RequestMapping(value = "getusericonbytopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getUserIconbyTopic(@RequestBody String userAndTopicInfo, HttpServletRequest request){
		JSONObject userAndTopicInfoJson = new JSONObject().fromObject(userAndTopicInfo);
		String userId = userAndTopicInfoJson.getString("userid");
		String topicId = userAndTopicInfoJson.getString("topicid");

		JSONObject iconNameJsonObject = topicService.getUserIconbyTopic(userId, topicId);
		
		JSONObject userIconjJsonObject = new JSONObject();
		String userIcon = null;
		
		if(iconNameJsonObject.getBoolean("israndom")){
			userIcon = request.getSession().getServletContext().getRealPath("/") + "randomIcons/" + iconNameJsonObject.getString("iconname");
		}
		else {
			userIcon = request.getSession().getServletContext().getRealPath("/") + "userIcons/" + iconNameJsonObject.getString("iconname");
		}
		System.out.println(userIcon);
		userIconjJsonObject.put("usericon", userIcon);
		return userIconjJsonObject.toString();
	}

	
	@RequestMapping(value = "updateusertopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public void updateUser_topic(@RequestBody String ID){
		JSONObject IDJson =  new JSONObject().fromObject(ID);
		topicService.updateUser_topic(IDJson.getString("userid"),IDJson.getString("topicid"));
	}
	
	@RequestMapping(value = "createtopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Topic createtopic(@RequestBody String newTopic){
		JSONObject newTopicJson = new JSONObject().fromObject(newTopic);
		String newId = topicService.createTopic(newTopicJson);
		Topic userTopics = topicService.getTopic(newId);
		System.out.println(userTopics);
		return userTopics;
	}

	
	@RequestMapping(value = "searchtopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String searchtopic(@RequestBody String keyword){
		JSONObject keywordJson =  new JSONObject().fromObject(keyword);
		String userTopics = topicService.searchTopic(keywordJson.getString("keyword"));
		System.out.println(userTopics);
		return userTopics ;
	}
	
}
