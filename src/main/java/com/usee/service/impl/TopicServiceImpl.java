package com.usee.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.usee.dao.TopicDao;
import com.usee.model.Topic;
import com.usee.service.TopicService;

@Service
public class TopicServiceImpl implements TopicService {

	@Resource
	private TopicDao topicDao;
	
	@Override
	public Topic getTopic(String id) {
		return topicDao.getTopic(id);
	}

	@Override
	public List<Topic> getAllTopic() {
		return topicDao.getAllTopic();
	}

	@Override
	public void addTopic(Topic topic) {
		topicDao.addTopic(topic);
	}

	@Override
	public boolean delTopic(String id) {
		return topicDao.delTopic(id);
	}
}
