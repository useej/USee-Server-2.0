package com.usee.service;

import java.util.List;

import com.usee.model.Topic;

import net.sf.json.JSONObject;

public interface TopicService {

	public Topic getTopic(String id);

	public List<Topic> getAllTopic();

	public void addTopic(Topic topic);

	public boolean delTopic(String id);

	public String getUserTopics(String userID);

	public String getNearbyTopics (double ux, double uy, int userRadius, String userID);

	public JSONObject getUserIconbyTopic(String userId, String topicId);

	public JSONObject getUserIconByComment(String userId, int danmuId);

	public void updateUser_topic(String userID,String topicID);

	public String createTopic(JSONObject topic);

	public String searchTopic(String keyword);

    public String getHotestTopics();

    public void dislikeTopic(String userID, List<String> topics);

    public String checkVersion();

    public void likeTopic(String userID, List<String> topics);

    public String getTopicTitleForWeb(String topicID);

	public String getTopicsbyType(String typeID);
	
	public String changeTypeOfTopic(Topic topic);

	public void updateType(String topicID,int type);
	
	public String getTopicIDBytitle(String topicTitle);
	
	public void addTopicType(String topicID, List<Integer> typeIDList);
	
	public String getCreatedTopicsByUser(String userID);

}
