package com.usee.dao;

import com.usee.model.TopicType;

public interface TopicTypeDao {
	
	public void addTopictype(TopicType newtopictype);
	
	public void updateTopictype(TopicType topictype);
	
	public void delTypeOfTopic(String topicID);
	
	public String getTypeOfTopic(String topicID);


}
