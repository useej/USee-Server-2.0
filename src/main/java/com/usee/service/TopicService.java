package com.usee.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.usee.model.Topic;

public interface TopicService {

	public Topic getTopic(String id);

	public List<Topic> getAllTopic();

	public void addTopic(Topic topic);

	public boolean delTopic(String id);
	
	public String getUserTopics(String userID);
	
	public String getNearbyTopics (double ux, double uy, int userRadius, String userID);
	
	public JSONObject getUserIconbyTopic(String userId, String topicId);
	
	public void updateUser_topic(String userID,String topicID);
	
	public String createTopic(JSONObject topic);
	
	public String searchTopic(String keyword);
			
}