package com.usee.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usee.model.Topic;
import com.usee.service.SqlInjectService;
import com.usee.service.TopicService;

@Controller
public class shareController {
	
	@Resource
	private TopicService topicService;
	
	@Autowired
	private SqlInjectService sqlInjectService;
	
	@ResponseBody
	@RequestMapping("/sharetopic")
	public Map<String, String> shareTopic(@RequestBody String json){
		
		String topicId = null;
		Map<String, String> shareMap = new HashMap<String, String>();
		// 防注入
		String handJson = sqlInjectService.SqlInjectHandle(json);
		
		try {
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String,String> map = new HashMap<String, String>();
			map = mapper.readValue(handJson, new TypeReference<Map<String, String>>(){});
			topicId = map.get("topicId");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		// 得到topic的信息
		Topic topic = topicService.getTopic(topicId);
		String topicDescription = topic.getDescription();
		String topicTitle = topic.getTitle();
		
		// 使用topic信息组装成topicUrl
		String topicUrl = "http://114.215.209.102/USee/danmaku.html?topictitle="
				+ topicTitle + "&topicid=" + topicId;
		
		shareMap.put("topicUrl", topicUrl);
		shareMap.put("topicDescription", topicDescription);
		
		return shareMap;
	}

}
