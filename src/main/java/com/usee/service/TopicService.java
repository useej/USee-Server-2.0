package com.usee.service;

import java.util.List;

import com.usee.model.Topic;

public interface TopicService {

	public Topic getTopic(String id);

	public List<Topic> getAllTopic();

	public void addTopic(Topic topic);

	public boolean delTopic(String id);
}
