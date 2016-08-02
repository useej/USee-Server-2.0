package com.usee.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usee.dao.MessageDao;
import com.usee.model.Comment;
import com.usee.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	MessageDao messageDao;

	public int getNewMsgsNum(String userID, String latestReadTime) {
		return messageDao.getNewMsgsNum(userID, latestReadTime);
	}

	public List<Comment> getNewMsgs(String userID, String latestReadTime) {
		return messageDao.getNewMsgs(userID, latestReadTime);
	}

	public List<Comment> getallMsgsbyID(String userID) {
		return messageDao.getallMsgsbyID(userID);
	}

}
