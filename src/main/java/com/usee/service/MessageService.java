package com.usee.service;

import java.util.List;

import com.usee.model.Comment;

public interface MessageService {
	public int getNewMsgsNum(String userID, String latestReadTime);
	
	public List<Comment> getNewMsgs(String userID, String latestReadTime);
	
	public List<Comment> getallMsgsbyID(String userID);
	
}
