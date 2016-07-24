package com.usee.service;

import com.usee.model.Danmu;

public interface DanmuService {

	public void sendDammu(Danmu danmu, boolean isAnnonymous);

	public String getDanmuDetails(String danmuId);
	
	public void postMessage();

	public String getDanmubyTopic(String topicId, String pageNum, String pageSize);
	
	public String getLatestDanmuId();
	
	public Danmu getDanmu(String danmuId);
}
