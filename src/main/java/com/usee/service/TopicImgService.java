package com.usee.service;

import java.util.List;

import org.json.JSONException;

import com.qiniu.api.auth.AuthException;
import com.usee.model.Topicimg;

import net.sf.json.JSONObject;

public interface TopicImgService {
	
	public String getuptoken() throws AuthException, JSONException;
	
	public Topicimg saveTopicimg(JSONObject Topicimg);
	
	public List<String> getTopicimg(String topicID);
	
	public String getFirstTopicimg(String topicID);
	
}
