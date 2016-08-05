package com.usee.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javassist.bytecode.Descriptor.Iterator;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.usee.dao.TopicDao;
import com.usee.dao.impl.DanmuDaoImp;
import com.usee.dao.impl.TopicDaoImpl;
import com.usee.dao.impl.UserTopicDaoImp;
import com.usee.model.Topic;
import com.usee.model.UserTopic;
import com.usee.service.TopicService;
import com.usee.utils.Distance;
import com.usee.utils.TimeUtil;

@Service
public class TopicServiceImpl implements TopicService {

	@Resource
	private TopicDaoImpl topicdao;
	@Resource
	private DanmuDaoImp danmudao;
	@Resource
	private UserTopicDaoImp userTopicDao;
	
	private static final String DEFAULT_USERICON = "1.png";
	
	public Topic getTopic(String id) {
		return topicdao.getTopic(id);
	}

	
	public List<Topic> getAllTopic() {
		return topicdao.getAllTopic();
	}

	
	public void addTopic(Topic topic) {
		topicdao.addTopic(topic);
	}

	
	public boolean delTopic(String id) {
		return topicdao.delTopic(id);
	}


	public String getUserTopics(String userID) {
		List list1 = new ArrayList();
		List list2 = new ArrayList();
		List list3 = new ArrayList();
		List<String> userTopics = new ArrayList();
		list1 = topicdao.getUserTopicsID(userID);
		list2 = danmudao.getDanmubyUserId(userID);
		/*
		检查重复
		 */	
		 for (int i = 0; i < list2.size() - 1; i++)
         {
             for (int j = i + 1; j < list2.size(); j++)
             {
                 if (list2.get(i).equals(list2.get(j)))
                 {
                     list2.remove(j);
                     j--;
                 }
             }
         }
		list1.addAll(list2);
		
		for(int i=0; i<list1.size();i++){   
		       String a = (String) list1.get(i); 
		       userTopics.addAll(topicdao.getUserTopics(a));
		}
		 JSONArray array = JSONArray.fromObject(userTopics);
		 JSONObject object = new JSONObject();
			object.put("topic", array.toString());
			
			
			return object.toString();
	}


	public String getNearbyTopics(double ux,double uy,int userRadius, String userID) {
		Distance a= new Distance();
		List list = new ArrayList();
		List<Topic> list1 = new ArrayList();
		list1 = topicdao.getAllTopic();
		JSONArray array = JSONArray.fromObject(list1);
		JSONArray array1 = JSONArray.fromObject(list1);
		JSONObject object = new JSONObject();
		int a2 = 0;
		
		for(int i=0;i<list1.size();i++){
		JSONObject tempJsonObject = array.getJSONObject(i);
		double a1;
		double a3;
		double topiclon = (Double) tempJsonObject.get("lon");
		double topiclat = (Double) tempJsonObject.get("lat");
		a1=topiclon-ux;
		a3=topiclat-uy;
			if((a1<-0.1||a1>0.1) && (a1<-0.1||a1>0.1)){
				array1.discard(i-a2);
				a2=a2+1;
			}
		}
		
		for(int i=0;i<array1.size();i++){
			JSONObject tempJsonObject = array1.getJSONObject(i);
			String topicId=tempJsonObject.getString("id");
			UserTopic usertopic =  userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userID, topicId);
			if(usertopic!= null){
				tempJsonObject.put("isread", true);
			}
			else{
				tempJsonObject.put("isread", false);
				}
		}
		
	
			object.put("topic", array1.toString());
			
			return object.toString();
		
	
	}

	@Override
	public JSONObject getUserIconbyTopic(String userId, String topicId) {
		JSONObject jsonObject= new JSONObject();
		if(userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userId, topicId) != null){
			jsonObject.put("israndom", false);
			jsonObject.put("iconname", userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userId, topicId).getUserIcon());
			return jsonObject;
		}
		else {
			jsonObject.put("israndom", true);
			jsonObject.put("iconname", DEFAULT_USERICON);
			return jsonObject;
		}
	}
	
	public void updateUser_topic(String userID, String topicID) {
		TimeUtil timeutil = new TimeUtil();
		String lastVisitTime = timeutil.currentTimeStamp;
		int frequency = userTopicDao.getLatestFrequency() + 1;
		userTopicDao.updateUserTopicLVTandFrequency(userID,topicID,lastVisitTime,frequency);
		
	}


	public String createTopic(JSONObject topic){
		TimeUtil timeutil = new TimeUtil();
		String currentTime = timeutil.currentTimeStamp;
		String title = topic.getString("title");
		String description = topic.getString("description");
		int radius = topic.getInt("radius");
		Double lon = topic.getDouble("lon");
		Double lat = topic.getDouble("lat");
		String userid = topic.getString("userid");
		
		List list = new ArrayList();
		list=topicdao.getAllTopicId();
		int max=0;
		for(int i=0;i<list.size();i++){
		int	a = Integer.valueOf((String) list.get(i));
		if(a>max){
			max=a;		
			}
		}
		max++;
		String newid = String.valueOf(max);
		Topic newtopic = new Topic();
		newtopic.setId(newid);
		newtopic.setTitle(title);
		newtopic.setDescription(description);
		newtopic.setRadius(radius);
		newtopic.setLon(lon);
		newtopic.setLat(lat);
		newtopic.setUserID(userid);
		newtopic.setPoi(null);
		newtopic.setCreate_time(currentTime);
		topicdao.addTopic(newtopic);
		
		return newid;
	}


	@Override
	public String searchTopic(String keyword) {
		List list1 = new ArrayList();
		List<String> userTopics = new ArrayList();
		list1 = topicdao.searchTopic(keyword);
		
		 JSONArray array = JSONArray.fromObject(list1);
		 JSONObject object = new JSONObject();
			object.put("topic", array.toString());
			return object.toString();
	}
}