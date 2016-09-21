package com.usee.dao;

import java.util.List;

import com.usee.model.UserTopic;
import com.usee.model.UserTopic_Visit;

public interface UserTopicDao {
	public void saveUserTopic(UserTopic userTopic);
	
	public UserTopic getUserTopic(int id);
	
	public int getLatestFrequency();
	
	public void updateUserTopic(String userId, String topicId ,int randomIconId, int randomNameId, String lastVisitTime, int frequency, String userIcon);
	
	public void updateRandomIconId(String userId, String topicId, int randomIconId);
	
	public List<UserTopic> getUserTopicbyUserId(String userId);
	
	public UserTopic getUniqueUserTopicbyUserIdandTopicId(String userId, String topicId);
	
	public void updateUserTopicLVTandFrequency(String userId, String topicId, String lastVisitTime, int frequency);
	
	public List<Integer> getuserRandomIconIdsbyTopic(String topicId);

    public void updateUserTopiclike(String userId, String topicId,int like);
    
    public void saveUserTopic_visit(UserTopic_Visit userTopic_visit);
    
    public void updateUserTopic_visit(String userId, String topicId, String lastVisitTime);
    
    public UserTopic_Visit getUserTopic_VisitByUserIdandTopicId(String userId, String topicId);
	
}
