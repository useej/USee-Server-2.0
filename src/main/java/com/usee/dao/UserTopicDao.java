package com.usee.dao;

import java.util.List;

import com.usee.model.UserTopic;

public interface UserTopicDao {
	public void saveUserTopic(UserTopic userTopic);
	
	public void updateUserTopic(String lastVisit_time, String frequency);
	
	public int getLatestFrequency();
	
	public List<UserTopic> getUserTopicbyUserId(String userId);
}
