package com.usee.dao;

import com.usee.model.UserTopic;

public interface UserTopicDao {
	public void saveUserTopic(UserTopic userTopic);
	
	public void updateUserTopic(String lastVisit_time, String frequency);
	
	public int getLatestFrequency();
	
}
