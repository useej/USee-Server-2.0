package com.usee.dao;

import java.util.List;

import com.usee.model.Topic;

public interface TopicDao {

	public Topic getTopic(String id);

	public List<Topic> getAllTopic();

	public void addTopic(Topic topic);

	public boolean delTopic(String id);
	
	public List getUserTopicsID(String userID);
	
	public List getUserTopics(String topicID);
	

}
