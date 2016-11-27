package com.usee.dao;

import java.util.List;

import com.usee.model.Topicimg;

public interface TopicimgDao {
	public void savetopicimg(Topicimg topicimg);
	
	public List<String> gettopicimg(String topicID);
	
	public String getFirsttopicimg(String topicID);
}
