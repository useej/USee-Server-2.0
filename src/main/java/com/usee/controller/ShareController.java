package com.usee.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.usee.service.impl.TopicImgServiceImp;
import com.usee.utils.DownloadURL;
import com.usee.utils.ShareTopicUtil;

@Controller
public class ShareController {
	
	@Resource
	private TopicService topicService;
	@Autowired
	private TopicImgServiceImp topicImgService;
	@Autowired
	private SqlInjectService sqlInjectService;
	
	public static String SHATR_CONTENT = "这个话题很有趣，邀请你一起来弹吧!";
	
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
//		String topicDescription = topic.getDescription();
		String firstTopicImg = topicImgService.getFirstTopicimg(topicId);
		String topicTitle = topic.getTitle();
		
		if(firstTopicImg == null) {
			firstTopicImg = "";
		}
		
		// 使用topic信息组装成topicUrl
		String topicUrl = ShareTopicUtil.getShareURL() 
				+ "?topicid=" + topicId;
		
		shareMap.put("topicUrl", topicUrl);
		shareMap.put("title", topicTitle);
		shareMap.put("shareContent", SHATR_CONTENT);
		shareMap.put("topicImg", firstTopicImg);
		
		
		System.out.println(shareMap);
		return shareMap;
	}
	
	@RequestMapping("/download")
	public void downloadAPK(HttpServletRequest request,HttpServletResponse response){
		String downloadURL = DownloadURL.getDownloadURL();
		try {
			response.sendRedirect(downloadURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
