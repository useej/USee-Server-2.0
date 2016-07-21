package com.usee.dao;

import java.util.List;

import com.usee.model.Danmu;

public interface DanmuDao {

	public void saveDanmu(Danmu danmu);

	public List<Danmu> getDanmuList(String topicId);

	public Danmu getDanmu(String messageId);
	
	public List<Danmu> getDanmuList(String topicId, int pageNum,
			int pageSize);
	
	public List<Object[]> getDanmuDetails(String danmuId);
	
	public String getLatestDanmiId();
	
	public boolean userIdCheck(String userId);
}
