package com.usee.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usee.model.Topic;
import com.usee.service.impl.TopicServiceImpl;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


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
		String userIcon = iconNameJsonObject.getString("iconname");
		String userName = iconNameJsonObject.getString("username");
		int isAnonymous = iconNameJsonObject.getInt("isAnonymous");
		int randomIconId = iconNameJsonObject.getInt("randomIconId");

		userIconjJsonObject.put("usericon", userIcon);
		userIconjJsonObject.put("username", userName);
		userIconjJsonObject.put("isanonymous", isAnonymous);
		userIconjJsonObject.put("randomIconId", randomIconId);
		return userIconjJsonObject.toString();
	}
	
	@RequestMapping(value = "getusericonbycomment", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getUserIconByComment(@RequestBody String userAndTopicInfo, HttpServletRequest request){
		JSONObject userAndTopicInfoJson = new JSONObject().fromObject(userAndTopicInfo);
		String userId = userAndTopicInfoJson.getString("userid");
		int danmuId = userAndTopicInfoJson.getInt("danmuid");

		JSONObject iconNameJsonObject = topicService.getUserIconByComment(userId, danmuId);
		
		JSONObject userIconjJsonObject = new JSONObject();
		String userIcon = iconNameJsonObject.getString("iconname");
		String userName = iconNameJsonObject.getString("username");
		int isAnonymous = iconNameJsonObject.getInt("isAnonymous");
		int randomIconId = iconNameJsonObject.getInt("randomIconId");

		userIconjJsonObject.put("usericon", userIcon);
		userIconjJsonObject.put("username", userName);
		userIconjJsonObject.put("isanonymous", isAnonymous);
		userIconjJsonObject.put("randomIconId", randomIconId);
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

    @RequestMapping(value = "gethotsearch", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getHotSearch(){
        return topicService.getHotestTopics();
    }

    @RequestMapping(value = "disliketopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public void disliketopic(@RequestBody String topic){
        JSONObject topicjson =  new JSONObject().fromObject(topic);
        String userID = topicjson.getString("userid");
        String topics = topicjson.getString("topics");
        String[] topicArray = topics.split(",");
        ArrayList<String> list = new ArrayList<String>();
        for(String s:topicArray){
            list.add(s);
        }
        topicService.dislikeTopic(userID, list);
    }

    @RequestMapping(value = "checkversion", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String checkVersion(){
        JSONObject jsonObject = new JSONObject();
        String[] version = topicService.checkVersion().split(",");
        jsonObject.put("versionCode", version[0]);
        jsonObject.put("versionName", version[1]);
        return jsonObject.toString();
    }

    @RequestMapping(value = "liketopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public void liketopic(@RequestBody String topic){
        JSONObject topicjson =  new JSONObject().fromObject(topic);
        String userID = topicjson.getString("userid");
        String topics = topicjson.getString("topics");
        String[] topicArray = topics.split(",");
        ArrayList<String> list = new ArrayList<String>();
        for(String s:topicArray){
            list.add(s);
        }
        topicService.likeTopic(userID, list);
    }

}
