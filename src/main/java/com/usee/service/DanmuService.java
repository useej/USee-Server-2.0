package com.usee.service;

import net.sf.json.JSONObject;

import com.usee.model.Comment;
import com.usee.model.Danmu;

public interface DanmuService {

	public void sendDammu(JSONObject danmu);

	public String getDanmuDetails(String danmuId);

	public String getDanmubyTopic(String topicId, String pageNum, String pageSize);
	
	public String getLatestDanmuId();
	
	public Danmu getDanmu(String danmuId);
	
	public Comment commentDanmu(JSONObject danmuComment);
}
