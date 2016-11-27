package com.usee.service;

import java.util.List;
import java.util.Map;

import com.usee.model.Comment;
import com.usee.model.Danmu;

import net.sf.json.JSONObject;

public interface DanmuService {

	public void sendDammu(JSONObject danmu);

	public String getDanmuDetails(int danmuId, String currentUserId);

	public String getDanmubyTopic(String topicId, String pageNum, String pageSize);
	
	public int getLatestDanmuId();
	
	public Danmu getDanmu(int danmuId);
	
	public Comment commentDanmu(JSONObject danmuComment);
	
	public boolean upDanmu(JSONObject jsonObject);
	
	public boolean downDanmu(JSONObject jsonObject);
	
	public boolean favDanmu(JSONObject jsonObject);
	
	public void updateUserDanmu(String userId, int danmuId);
	
	public String getFavDanmuList(JSONObject jsonObject);
	
	public int updateUserAction(JSONObject jsonObject);

    public String getLatestDanmuList(JSONObject jsonObject);

    public String haveNewComments(JSONObject jsonObject);

    public String reportContent(JSONObject jsonObject);
    
    public boolean deleteComment(JSONObject jsonObject);

    public void deleteReplyComment(int commentID);
    
    public boolean deleteDanmu(JSONObject jsonObject);
    
    public List<Map<String, String>> getIntervalDanmu(String topicID, String startTime, String endTime);

    public String getNewDanmu(JSONObject jsonObject);
    
    public int checkDanmu(JSONObject danmu);
    
    public int checkComment(JSONObject danmu);
    
    public List<Danmu> getHotDanmu(String topicID);
}
