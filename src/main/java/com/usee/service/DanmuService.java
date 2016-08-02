package com.usee.service;

import net.sf.json.JSONObject;

import com.usee.model.Comment;
import com.usee.model.Danmu;

public interface DanmuService {

	public void sendDammu(JSONObject danmu);

	public String getDanmuDetails(int danmuId);

	public String getDanmubyTopic(String topicId, String pageNum, String pageSize);
	
	public int getLatestDanmuId();
	
	public Danmu getDanmu(int danmuId);
	
	public Comment commentDanmu(JSONObject danmuComment);
	
	public boolean upDanmu(JSONObject jsonObject);
	
	public boolean downDanmu(JSONObject jsonObject);
	
	public boolean favDanmu(JSONObject jsonObject);
}
