package com.usee.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.usee.dao.impl.DanmuDaoImp;
import com.usee.dao.impl.UserDaoImpl;
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
	@Resource
	private UserDaoImpl userDao;
	
	public static int MaxRandomNameNumber = 100;
	public static int MaxRandomIconNumber = 10;
	
	public void sendDammu(JSONObject danmu) {
		// TODO Auto-generated method stub
		String userId = danmu.getString("userid");
		
		RandomNumber randomNumber = new RandomNumber();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = df.format(new Date());
		int randomUserIconId = randomNumber.getRandom(1, MaxRandomIconNumber);
		
		UserTopic userTopic = new UserTopic();
		userTopic.setUserId(userId);
		userTopic.setTopicId(danmu.getString("topicid"));
		userTopic.setFirstvisit_time(currentTime);
		userTopic.setLastVisit_time(currentTime);
		userTopic.setFrequency(userTopicDao.getLatestFrequency() + 1);
		
		if(danmu.getBoolean("isannoymous")){
			userTopic.setRandomNameID(randomNumber.getRandom(1, MaxRandomNameNumber));
			userTopic.setRandomIconID(randomUserIconId);
			//userTopic.setUserIcon(randomNumber.getRandom(1, MaxRandomIconNumber) + ".png");
		}
		else{
			userTopic.setRandomNameID(0);
			userTopic.setRandomIconID(0);
			userTopic.setUserIcon(userId + ".png");
		}
		userTopicDao.saveUserTopic(userTopic);
		
		Danmu newDanmu = new Danmu();
		newDanmu.setDevId(danmu.getString("devid"));
		newDanmu.setUserId(userId);
		newDanmu.setStatus("0");
		newDanmu.setTopicId(danmu.getString("topicid"));
		newDanmu.setLon(danmu.getString("lon"));
		newDanmu.setLat(danmu.getString("lat"));
		newDanmu.setCreate_time(currentTime);
		newDanmu.setAddress("江苏省南京市佛城西路河海大学江宁校区");
		newDanmu.setDelete_time(danmu.getString("delete_time"));
		newDanmu.setMessages(danmu.getString("messages"));
		if(danmu.getBoolean("isannoymous")){
			newDanmu.setUserIcon(randomUserIconId + ".png");
		}
		else {
			newDanmu.setUserIcon(userId + ".png");
		}
		danmudao.saveDanmu(newDanmu);
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
			list = danmudao.getDanmuList(topicId, _pageNum, _pageSize);
			System.out.println(list.get(0).getMessages());
		}
		JSONArray array = JSONArray.fromObject(list);
		JSONObject object = new JSONObject();
		object.put("danmu", array.toString());
		
		return object.toString();
	} 

	
	public String getDanmuDetails(String danmuId) {
		
		List<Object[]> list = new ArrayList<Object[]>();
		list = danmudao.getDanmuDetails(danmuId);
		JSONArray array = JSONArray.fromObject(list);
		
		JSONObject tempJsonObject = array.getJSONObject(0);
		Iterator it = tempJsonObject.keys(); 

		String userid = tempJsonObject.get("userid").toString();
		//System.out.println(userid);
		if(userDao.getUser(userid) == null){
			/*
			 悄悄话需要后端过滤，获得当前用户的userID 
				1.	如果没有userID，即匿名使用，type=3的评论都不加载
				2.	如果有userID，返回type=3的、sender或者receiver = currentUserID的评论。 
			*/
			
			while (it.hasNext()) {  
				String key = (String) it.next();  
				String value = tempJsonObject.getString(key);  
				if(key == "type" && value == "3"){
					tempJsonObject.remove(key);
				}
			}
		}
		else{
			while (it.hasNext()) { 
				String key = (String) it.next();  
				String value = tempJsonObject.getString(key); 
				if((key == "sender" && value != userid) && (key == "receiver" && value != userid) && (key == "type" && value == "3")){
					tempJsonObject.remove(key);
				}
			}
		}
		
		JSONObject object = new JSONObject();
		object.put("danmudetails", array.toString());
		
		return object.toString();
	}
	
	public String getLatestDanmuId(){
		// TODO Auto-generated method stub
		return danmudao.getLatestDanmuId();
	}
	
	public Danmu getDanmu(String danmuId){
		return danmudao.getDanmu(danmuId);
	}
}
