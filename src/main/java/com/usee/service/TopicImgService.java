package com.usee.service;

import org.json.JSONException;

import com.qiniu.api.auth.AuthException;

import net.sf.json.JSONObject;

public interface TopicImgService {
	
	public String getuptoken() throws AuthException, JSONException;
	public String saveTopicimg(JSONObject Topicimg);
	
	
}
