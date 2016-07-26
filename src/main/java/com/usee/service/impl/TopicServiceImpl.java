package com.usee.service.impl;

import java.util.ArrayList;
import java.util.List;

import javassist.bytecode.Descriptor.Iterator;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.usee.dao.TopicDao;
import com.usee.dao.impl.DanmuDaoImp;
import com.usee.dao.impl.TopicDaoImpl;
import com.usee.model.Topic;
import com.usee.service.TopicService;
import com.usee.utils.Distance;

@Service
public class TopicServiceImpl implements TopicService {

	@Resource
	private TopicDaoImpl topicdao;
	@Resource
	private DanmuDaoImp danmudao;
	
	
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
		list1 = topicdao.getUserTopicsID("userID");
		list2 = danmudao.getDanmubyUserId("userID");
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


	public String getNearbyTopics(double ux,double uy,int userRadius) {
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
		double topiclon = (Double) tempJsonObject.get("lon");
		double topiclat = (Double) tempJsonObject.get("lat");
		a1=a.GetDistance(ux, uy,topiclon , topiclat);
		System.out.println(a1);
			if(a1>userRadius){
				array1.discard(i-a2);
				a2=a2+1;
			}
		}
		
	
			object.put("topic", array1.toString());
			
			return object.toString();
		
	
	}
}