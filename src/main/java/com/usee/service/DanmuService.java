package com.usee.service;

import com.usee.model.Danmu;

public interface DanmuService {

	public void sendDammu(Danmu message);

	public String getDanmuDetails(String danmuId);

	
	public void postMessage();

	public String getDanmubyTopic(String topicId, String pageNum, String pageSize);
}
