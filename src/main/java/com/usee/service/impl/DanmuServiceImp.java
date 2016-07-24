package com.usee.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	
	public void sendDammu(Danmu danmu, boolean isAnnonymous) {
		// TODO Auto-generated method stub
		String userId = danmu.getUserId();
		
		RandomNumber randomNumber = new RandomNumber();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = df.format(new Date());
		
		UserTopic userTopic = new UserTopic();
		userTopic.setUserId(userId);
		userTopic.setTopicId(danmu.getTopicId());
		userTopic.setFirstvisit_time(currentTime);
		userTopic.setLastVisit_time(currentTime);
		userTopic.setFrequency(userTopicDao.getLatestFrequency() + 1);
		
		if(isAnnonymous){
			userTopic.setRandomNameID(randomNumber.getRandom(1, MaxRandomNameNumber));
			userTopic.setRandomIconID(randomNumber.getRandom(1, MaxRandomIconNumber));
			userTopic.setUserIcon(randomNumber.getRandom(1, MaxRandomIconNumber) + ".png");
		}
		else{
			userTopic.setRandomNameID(0);
			userTopic.setRandomIconID(0);
			userTopic.setUserIcon(userId + ".png");
		}
		userTopicDao.saveUserTopic(userTopic);
		
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
		
		List<Object[]> list = new ArrayList<Object[]>();
		list = danmudao.getDanmuDetails(danmuId);
		JSONArray array = JSONArray.fromObject(list);
		JSONObject object = new JSONObject();
		object.put("danmudetails", array.toString());
		
		String userid = array.getJSONObject(0).get("userid").toString();
		if(!danmudao.userIdCheck(userid)){
			/*****************  TO BE DONE !!!
			 悄悄话需要后端过滤，获得当前用户的userID 
				1.	如果没有userID，即匿名使用，type=3的评论都不加载
				2.	如果有userID，返回type=3的、sender或者receiver = currentUserID的评论。 
			*/
		}
		
		return object.toString();
	}
	
	
	public void postMessage() {
		// TODO Auto-generated method stub
		
	}
	
	public String getLatestDanmuId(){
		// TODO Auto-generated method stub
		return danmudao.getLatestDanmuId();
	}
	
	public Danmu getDanmu(String danmuId){
		return danmudao.getDanmu(danmuId);
	}
}
