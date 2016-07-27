package com.usee.dao;

import java.util.List;

import com.usee.model.Danmu;

public interface DanmuDao {

	public void saveDanmu(Danmu danmu);
	
	public Danmu getDanmu(int id);

	public List<Danmu> getDanmuList(String topicId);
	
	public List<Danmu> getDanmuList(String topicId, int pageNum,
			int pageSize);
	
	//public List<Object[]> getDanmuDetails(int danmuId);
	
	public int getLatestDanmuId();
	
	public List getDanmubyUserId(String userId);
	
	public String getTopicIdbyDanmuId(int danmuId);
}
