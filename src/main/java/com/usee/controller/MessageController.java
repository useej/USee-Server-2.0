package com.usee.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usee.dao.TopicDao;
import com.usee.dao.impl.DanmuDaoImp;
import com.usee.model.Comment;
import com.usee.model.Message;
import com.usee.model.User;
import com.usee.service.MessageService;
import com.usee.service.SqlInjectService;
import com.usee.service.UserService;

@Controller
@RequestMapping("/message")
public class MessageController {
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private SqlInjectService sqlInjectService;
	
	@Autowired
	private DanmuDaoImp danmuDao;
	
	@Autowired
	private TopicDao topicDao;
	
	@ResponseBody
	@RequestMapping(value = "getNewMsgsNum", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public Map<String, Integer> getNewMsgsNum(@RequestBody String json) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		String userID = null;
		String latestReadTime = null;
		
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(json);

		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<String, String>();
			map = mapper.readValue(handJson, new TypeReference<Map<String, String>>() {
			});
			userID = map.get("userID");
			latestReadTime = map.get("latestReadTime");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int newMsgsNum = messageService.getNewMsgsNum(userID, latestReadTime);
		
		returnMap.put("newMsgsNum", newMsgsNum);
		return returnMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "getNewMsgs", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public Map<String, List<Message>> getNewMsgs(@RequestBody String json) {
		Map<String, List<Message>> returnMap = new HashMap<String, List<Message>>();
		
		String userID = null;
		String latestReadTime = null;
		
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(json);
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<String, String>();
			map = mapper.readValue(handJson, new TypeReference<Map<String, String>>() {
			});
			userID = map.get("userID");
			latestReadTime = map.get("latestReadTime");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Comment> list = messageService.getNewMsgs(userID, latestReadTime);
		
		List<Message> messageList = new ArrayList<Message>();
		
		for (Comment comment : list) {
			User user = userService.getUser(comment.getSender());
			// 根据danmuID得到话题名称
			int danmuId = comment.getDanmuId();
			String danmuUserID = danmuDao.getUserIDbyDanmuId(danmuId);
			String topicId = danmuDao.getTopicIdbyDanmuId(danmuId);
			String topicTitle = topicDao.getTopic(topicId).getTitle();
						
			Message message = new Message();
			message.setNickname(user.getNickname());
			message.setGender(user.getGender());
			message.setUserIcon(user.getUserIcon());
			message.setDanmuId(comment.getDanmuId());
			message.setContent(comment.getContent());
			message.setCreate_time(comment.getCreate_time());
			message.setType(comment.getType());
			message.setCommentId(comment.getId());
			message.setSender(comment.getSender());
			message.setDanmuUserID(danmuUserID);
			message.setTopicTitle(topicTitle);
			messageList.add(message);
		}
	
		System.out.println(messageList);
		
		returnMap.put("newMsgs", messageList);
		return returnMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "getallMsgs", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public Map<String, List<Message>> getallMsgsbyID(@RequestBody String json) {
		Map<String, List<Message>> returnMap = new HashMap<String, List<Message>>();
		
		String userID = null;
		
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(json);
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<String, String>();
			map = mapper.readValue(handJson, new TypeReference<Map<String, String>>() {
			});
			userID = map.get("userID");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Comment> list = messageService.getallMsgsbyID(userID);
		
		List<Message> messageList = new ArrayList<Message>();
		
		for (Comment comment : list) {
			User user = userService.getUser(comment.getSender());
			// 根据danmuID得到话题名称
			int danmuId = comment.getDanmuId();
			String danmuUserID = danmuDao.getUserIDbyDanmuId(danmuId);
			String topicId = danmuDao.getTopicIdbyDanmuId(danmuId);
			String topicTitle = topicDao.getTopic(topicId).getTitle();
						
			Message message = new Message();
			message.setNickname(user.getNickname());
			message.setGender(user.getGender());
			message.setUserIcon(user.getUserIcon());
			message.setDanmuId(comment.getDanmuId());
			message.setContent(comment.getContent());
			message.setCreate_time(comment.getCreate_time());
			message.setType(comment.getType());
			message.setCommentId(comment.getId());
			message.setSender(comment.getSender());
			message.setDanmuUserID(danmuUserID);
			message.setTopicTitle(topicTitle);
			messageList.add(message);
		}
	
		System.out.println(messageList);
		
		returnMap.put("newMsgs", messageList);
		return returnMap;
	}
}
