package com.usee.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.usee.dao.impl.DanmuDaoImp;
import com.usee.dao.impl.UserTopicDaoImp;
import com.usee.model.Danmu;
import com.usee.model.UserTopic;
import com.usee.service.DanmuService;
import com.usee.utils.RandomNumber;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class DanmuServiceImp implements DanmuService{
	@Resource
	private DanmuDaoImp danmudao;
	@Resource
	private UserTopicDaoImp userTopicDao;
	
	public static int MaxRandomNameNumber = 100;
	public static int MaxRandomIconNumber = 10;
	
	public void sendDammu(Danmu danmu) {
		// TODO Auto-generated method stub
		String userId = danmu.getUserId();
		
		RandomNumber rn = new RandomNumber();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = df.format(new Date());
		
		if(!danmudao.userIdCheck(userId)){
			UserTopic userTopic = new UserTopic();
			
			userTopic.setUserId(userId);
			userTopic.setTopicId(danmu.getTopicId());
			userTopic.setFirstvisit_time(currentTime);
			userTopic.setRandomNameID(rn.getRandom(1, MaxRandomNameNumber));
			userTopic.setRandomIconID(rn.getRandom(1, MaxRandomIconNumber));
			userTopic.setLastVisit_time(currentTime);
			userTopic.setFrequency(userTopicDao.getLatestFrequency() + 1);
			
			userTopicDao.saveUserTopic(userTopic);
		}
		else{
			userTopicDao.updateUserTopic(userId, currentTime, userTopicDao.getLatestFrequency() + 1);
		}
		danmudao.saveDanmu(danmu);
	}

	
	public String getDanmubyTopic(String topicId, String pageNum,
			String pageSize) {
		// TODO Auto-generated method stub
		List<Danmu> list = new ArrayList<Danmu>();
		if((pageNum == "" && pageSize == "")||(pageNum == null && pageSize == null)){
			list = danmudao.getDanmuList(topicId);
			System.out.println(list.get(0).getMessages());
		}
		else {
			int _pageNum = Integer.parseInt(pageNum);
			int _pageSize = Integer.parseInt(pageSize);
			list = danmudao.getDanmuList(topicId,_pageNum, _pageSize);
			System.out.println(list.get(0).getMessages());
		}
		JSONArray array = JSONArray.fromObject(list);
		JSONObject object = new JSONObject();
		object.put("danmu", array.toString());
		
		return object.toString();
	}

	
	public String getDanmuDetails(String danmuId) {
		// TODO Auto-generated method stub
		List<Object[]> list = new ArrayList<Object[]>();
		list = danmudao.getDanmuDetails(danmuId);
		JSONArray array = JSONArray.fromObject(list);
		JSONObject object = new JSONObject();
		object.put("danmudetails", array.toString());
		
		return object.toString();
	}
	
	
	public void postMessage() {
		// TODO Auto-generated method stub
		
	}
	
	public String getLatestDanmiId(){
		// TODO Auto-generated method stub
		return danmudao.getLatestDanmiId();
	}
}
