package com.usee.dao;

import java.util.List;

import com.usee.model.UserTopic;

public interface UserTopicDao {
	public void saveUserTopic(UserTopic userTopic);
	
	public UserTopic getUserTopic(int id);
	
	public int getLatestFrequency();
	
	public void updateUserTopic(String userId, String topicId ,int randomIconId, int randomNameId, String lastVisitTime, int frequency, String userIcon);
	
	public List<UserTopic> getUserTopicbyUserId(String userId);
	
	public UserTopic getUniqueUserTopicbyUserIdandTopicId(String userId, String topicId);
	
	public void updateUserTopicLVTandFrequency(String userId, String topicId, String lastVisitTime, int frequency);
}
