package com.usee.dao;

import java.util.List;

import com.usee.model.Danmu;

public interface DanmuDao {

	public void saveDanmu(Danmu danmu);
	
	public Danmu getDanmu(String danmuId);

	public List<Danmu> getDanmuList(String topicId);
	
	public List<Danmu> getDanmuList(String topicId, int pageNum,
			int pageSize);
	
	public List<Object[]> getDanmuDetails(String danmuId);
	
	public String getLatestDanmuId();
	
	public List<Danmu> getDanmubyUserId(String userId);
}
